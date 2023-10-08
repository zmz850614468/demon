/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ChunkUploadDispatcher from '../dispatcher/ChunkUploadDispatcher';

var _data = Symbol();
var _uploadProgress = Symbol();
var _chunkUploadDispatcher = Symbol();
var _uploadCallback = Symbol();

class BinaryFileChunkUpload {
    constructor(build) {
        if (!arguments.length) {
            build = new BinaryFileChunkUpload.Builder();
        }
        this[_data] = build[_data];
        this[_uploadProgress] = build[_uploadProgress];
        this[_chunkUploadDispatcher] = build[_chunkUploadDispatcher];
        this[_uploadCallback] = build[_uploadCallback]
    }

    static get Builder() {
        class Builder {
            constructor() {
                this[_data] = [];
                this[_uploadProgress] = null;
                this[_uploadCallback] = null;
                this[_chunkUploadDispatcher] = new ChunkUploadDispatcher();
            }

            addBinaryFile(abilityContext, chunkUploadOptions: ChunkUploadOptions) {
                this[_chunkUploadDispatcher].chunkFile(abilityContext, chunkUploadOptions)
                return this;
            }

            addData(fname, fvalue) {
                let dataObject = { name: fname, value: fvalue };
                this[_data].push(dataObject);
                return this;
            }

            addUploadProgress(uploadProgressCallback: (uploadedSize: number, totalSize: number) => {}) {
                this[_uploadProgress] = uploadProgressCallback;
                return this;
            }

            addUploadCallback(uploadCallback: (stat: string, errQueue?) => {}) {
                this[_uploadCallback] = uploadCallback
                return this;
            }

            build() {
                return new BinaryFileChunkUpload(this);
            }
        }

        return Builder;
    }

    getData() {
        if (this[_data]) {
            return this[_data];
        }
        return [];
    }

    getUploadProgress() {
        if (this[_uploadProgress]) {
            return this[_uploadProgress];
        }
        return undefined;
    }

    getChunkUploadDispatcher() {
        if (this[_chunkUploadDispatcher]) {
            return this[_chunkUploadDispatcher];
        }
        return undefined;
    }

    getUploadCallback() {
        if (this[_uploadCallback]) {
            return this[_uploadCallback];
        }
        return undefined;
    }
}

export default BinaryFileChunkUpload;

export interface ChunkUploadOptions {
    fileData?: ArrayBuffer;
    filePath: string;
    fileName: string;
    chunkSize?: number;
    name?: string;
}