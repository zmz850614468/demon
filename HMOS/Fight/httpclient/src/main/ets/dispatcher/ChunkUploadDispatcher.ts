/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { ChunkUploadOptions } from '../builders/BinaryFileChunkUpload';
import fs from '@ohos.file.fs';
import { Logger } from '../utils/Logger';
import HashMap from '@ohos.util.HashMap';
import hilog from '@ohos.hilog';

const TAG = 'ChunkUploadDispatcher: '

class ChunkUploadDispatcher {
    chunkedQueue = [];
    uploadCompleteQueue = [];
    uploadFailQueue = [];
    fileSize: number;
    chunkSize: number = 1024 * 1024 * 5;
    chunkCount: number;
    fileName: string;
    cachePath: string;
    name: string;
    maxUploadCount: number = 5
    uploadProgressMap: HashMap<string, number> = new HashMap()

    constructor() {
        this.chunkedQueue = [];
        this.uploadCompleteQueue = [];
        this.uploadFailQueue = [];
    }

    //切片
    async chunkFile(abilityContext, chunkUploadOptions: ChunkUploadOptions) {
        Logger.info('BinaryFileChunkUpload: addBinaryFile enter chunkUploadOptions is ')
        this.cachePath = abilityContext.cacheDir
        this.fileName = chunkUploadOptions.fileName
        let fileData = chunkUploadOptions.fileData
        let filePath = chunkUploadOptions.filePath
        if (chunkUploadOptions.chunkSize) {
            if (chunkUploadOptions.chunkSize < 1024 * 1024 * 1) {
                this.chunkSize = 1024 * 1024 * 1
            } else if (chunkUploadOptions.chunkSize > 1024 * 1024 * 50) {
                this.chunkSize = 1024 * 1024 * 50
            } else {
                this.chunkSize = chunkUploadOptions.chunkSize
            }
        }

        if (undefined == fileData && filePath != undefined) {
            let stat = fs.lstatSync(chunkUploadOptions.filePath);
            this.fileSize = stat.size
            if (undefined != chunkUploadOptions.name) {
                this.name = chunkUploadOptions.name
            }
            this.chunkCount = Math.ceil(this.fileSize / this.chunkSize)
            fs.open(chunkUploadOptions.filePath, fs.OpenMode.READ_WRITE).then((fd) => {
                for (let index = 0; index < this.chunkCount; index++) {
                    if (this.chunkCount == 1) {
                        const buffer = new ArrayBuffer(this.fileSize)
                        fs.read(fd.fd, buffer, {
                            offset: 0,
                            length: this.fileSize
                        }).then((readOut) => {
                            this.createChunkFile(index, buffer)
                        })
                    } else {
                        if (index * this.chunkSize == ((this.chunkCount - 1) * this.chunkSize)) {
                            let bufferSize = this.fileSize - index * this.chunkSize
                            const buffer = new ArrayBuffer(bufferSize)
                            fs.read(fd.fd, buffer, {
                                offset: 0,
                                length: bufferSize
                            }).then((readOut) => {
                                this.createChunkFile(index, buffer)
                            })
                        } else {
                            const buffer = new ArrayBuffer(this.chunkSize)
                            fs.read(fd.fd, buffer, {
                                offset: 0,
                                length: this.chunkSize
                            }).then((readOut) => {
                                this.createChunkFile(index, buffer)
                            })
                        }
                    }
                }
            });
        } else if (undefined != fileData && undefined == filePath) {
            this.fileSize = fileData.byteLength
            if (chunkUploadOptions.name) {
                this.name = chunkUploadOptions.name
            }

            this.chunkCount = Math.ceil(this.fileSize / this.chunkSize)
            for (let index = 0; index < this.chunkCount; index++) {
                let arrayBuffer
                if (index == this.chunkCount - 1) {
                    arrayBuffer = fileData.slice(index * this.chunkSize, this.fileSize)
                } else {
                    arrayBuffer = fileData.slice(index * this.chunkSize, (index + 1) * this.chunkSize)
                }
                this.createChunkFile(index, arrayBuffer)
            }
        }
    }

    getFileSize(): number {
        return this.fileSize
    }

    getChunkCount(): number {
        return this.chunkCount
    }

    createChunkFile(index: number, arrayBuffer: ArrayBuffer) {
        let name=this.fileName.substring(0,this.fileName.lastIndexOf("."))
        let chunkFileName=this.fileName.replace(name,name+index)
        let filePath = this.cachePath + '/' + chunkFileName
        let fileType = chunkFileName.substring(this.fileName.lastIndexOf('.') + 2);
        let furi = 'internal://cache/' + chunkFileName
        let that = this
        fs.open(filePath, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE).then((file) => {
            fs.write(file.fd, arrayBuffer).then((len) => {
                if (len == arrayBuffer.byteLength) {
                    fs.fdatasyncSync(file.fd);
                    fs.closeSync(file);
                    let fileRequestData = {
                        filename: chunkFileName,
                        name: that.name,
                        uri: furi,
                        type: fileType
                    };
                    Logger.info(TAG + 'BinaryFileChunkUpload: chunkFileName  is  ' + chunkFileName)
                    if (that.chunkedQueue.indexOf(fileRequestData) == -1) {
                        that.chunkedQueue = that.chunkedQueue.concat([fileRequestData])
                    }
                }
            }).catch((err) => {
                throw err
            })
        }).catch((err) => {
            throw err
        })
    }

    getWaitUploadFiles() {
        let chunkFileList = []
        let processedCount = this.uploadCompleteQueue.length + this.uploadFailQueue.length
        if (this.chunkCount - processedCount >= this.maxUploadCount) {
            chunkFileList = this.chunkedQueue.slice(processedCount, processedCount + this.maxUploadCount);
        } else {
            chunkFileList = this.chunkedQueue.slice(processedCount, this.chunkCount);
        }
        return chunkFileList
    }

    updateUploadProgress(key: string, uploadedSize: number) {
        if (this.uploadProgressMap.hasKey(key)) {
            this.uploadProgressMap.replace(key, uploadedSize)
        } else {
            this.uploadProgressMap.set(key, uploadedSize)
        }
    }

    getUploadProgress() {
        let total = 0
        this.uploadProgressMap.forEach((value, key) => {
            total += value
        })
        return total
    }

    deleteChunkedFiles(abilityContext, chunkedFiles) {
        let length = chunkedFiles.length
        for (let index = 0; index < length; index++) {
            let itemFileName = chunkedFiles[index].filename
            Logger.info(TAG + 'deleteChunkedFiles chunkedFiles.foreach enter itemFileName is ' + itemFileName);
            fs.unlink(abilityContext.cacheDir + '/' + itemFileName).then(() => {
                Logger.info(TAG + JSON.stringify(itemFileName) + " unlink success");
            }).catch(error => {
                hilog.info(0x0001, TAG + JSON.stringify(itemFileName) + " unlink failed", JSON.stringify(error));
            })
        }
    }
}

export default ChunkUploadDispatcher;