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

import { DiskCacheEntry } from './cache/DiskCacheEntry';
import fs from '@ohos.file.fs';
import util from '@ohos.util'
import { Response } from '../../response/Response'
import { DiskLruCache } from './cache/DiskLruCache'

const JOURNAL_FILE = "journal";

const MAGIC = "libcore.io.DiskLruCache";
const VERSION_1 = "1";
const LEGAL_KEY_PATTERN = "[a-z0-9_-]{1,120}";

let CLEAN = "CLEAN";
let DIRTY = "DIRTY";
let REMOVE = "REMOVE";
let READ = "READ";

let maxSize: number
let valueCount: number = 0

let _size: number = 0
let journalWriter: string
let lruEntries = new Map<string, DiskCacheEntry>()
let redundantOpCount: number
let initialized: boolean
let closed: boolean
let responseKey: string
let diskLruCache: DiskLruCache

class HttpDiskLruCache {
    directory: string
    /**
     * To differentiate between old and current snapshots, each entry is given a sequence number each
     * time an edit is committed. A snapshot is stale if its sequence number is not equal to its
     * entry's sequence number.
     */

    constructor(directory: string, appVersion: number, valueCount: number, maxSize: number) {
        this.directory = directory
        valueCount = valueCount
        maxSize = maxSize
        diskLruCache.setMaxSize(maxSize)
    }

    static create(filePath: string, appVersion: number, valueCount: number, maxSize: number) {
        if (maxSize <= 0) {
            throw new Error("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new Error("valueCount <= 0");
        }
        let title = MAGIC + "\n" + VERSION_1 + "\n" + appVersion + "\n" + valueCount + "\n"
        diskLruCache = DiskLruCache.create(globalThis.abilityContext, title)
        return new HttpDiskLruCache(filePath, appVersion, valueCount, maxSize)
    }

    static completeEdit(editor, success: boolean) {
        let entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new Error();
        }

        if (success && !entry.readable) {
            for (var i = 0; i < valueCount; i++) {
                if (!editor.written[i]) {
                    editor.abort();
                    throw new Error("Newly created entry didn't create value for index " + i);
                }
                try {
                    let exit = fs.accessSync(entry.dirtyFiles[i])
                    if (!exit) {
                        editor.abort();
                        return;
                    }
                } catch (e) {
                    editor.abort();
                    return;
                }
            }
        }

        for (var i = 0; i < valueCount; i++) {
            let dirty = entry.dirtyFiles[i];
            if (success) {
                try {
                    let exits = fs.accessSync(entry.dirtyFiles[i])
                    if (exits) {
                        let clean = entry.cleanFiles[i];
                        fs.renameSync(dirty, clean)
                        let oldLength = entry.lengths[i]
                        let newLength = fs.statSync(clean)
                        entry.lengths[i] = newLength.size
                        _size = _size - oldLength + newLength.size;
                    }
                } catch (e) {

                }
            } else {
                fs.rmdirSync(dirty)
            }
        }
    }

    //此方法解析Journal文件，将日志中的信息转换成Entry对象，并存储在lruEntries属性中
    public readFile(readPath: string): string {
        try {
            let fileID = fs.openSync(readPath, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
            let buf = new ArrayBuffer(1096);
            fs.readSync(fileID.fd, buf)
            let textDecoder = util.TextDecoder.create("utf-8", { ignoreBOM: true });
            let decodedString = textDecoder.decodeWithStream(new Uint8Array(buf), { stream: false });
            return decodedString
        } catch (e) {
            return null
        }
    }

    /**
     * 写入具体数据
     * @param fileName
     * @param context
     * @param type
     */
    public writerFile(fileName: string, header: string, body: string) {
        let array = new Array<string>();
        array.push(header)
        array.push(body)
        diskLruCache.set(fileName, array)
    }

    /**
     * Creates a new journal that omits redundant information. This replaces the current journal if it
     * exists.
     */
    rebuildJournal() {
        if (journalWriter != null) {
            journalWriter = this.newJournalWriter();
        }

        if (journalWriter == null || journalWriter == undefined) {
            return
        }

        let entry = new Entry(journalWriter)
        if (entry.cleanFiles == undefined || entry.cleanFiles == null) {
            journalWriter = this.newJournalWriter();
        } else {
            for (var i = 0;i < entry.cleanFiles.length; i++) {
                diskLruCache.deleteCacheDataByKey(entry.cleanFiles[i])
            }
        }
    }

    get(key: string) {
        this.validateKey(key);
        redundantOpCount++;
        journalWriter = READ + ' ' + key + "\n"
        let Header: string = null
        let Body: string = null

        try {
            let bufs = diskLruCache.get(key)
            let textDecoder = util.TextDecoder.create("utf-8", { ignoreBOM: true });
            if (bufs != null && bufs != undefined) {
                bufs.forEach((item, index) => {
                    if (index == 0) {
                        Header = textDecoder.decodeWithStream(new Uint8Array(item), { stream: false });
                    } else {
                        Body = textDecoder.decodeWithStream(new Uint8Array(item), { stream: false });
                    }
                })
                return {
                    getHeader: Header,
                    getBody: Body
                }
            } else {
                return null
            }
        } catch (e) {
            return null
        }
    }

    public edit(key: string) {
        responseKey = key
        return;
    }

    public getDirectory() {
        return this.directory;
    }

    getMaxSize() {
        return diskLruCache.getMaxSize();
    }

    setMaxSize(size: number) {
        maxSize = size;
        diskLruCache.setMaxSize(size)
    }

    size(): number {
        return diskLruCache.getSize();
    }

    journalRebuildRequired(): boolean {
        const redundantOpCompactThreshold = 2000
        return redundantOpCount >= redundantOpCompactThreshold
        && redundantOpCount >= lruEntries.size
    }

    remove(key: string) {
        this.validateKey(key)
        diskLruCache.deleteCacheDataByKey(key)
    }

    isClosed(): boolean {
        return closed
    }

    flush() {
        if (!initialized) return
        diskLruCache.trimToSize()
    }

    close() {
        diskLruCache.trimToSize()
    }

    delete() {
        this.close()
        diskLruCache.cleanCacheData()
    }

    update(cache: Response, network: Response) {
        diskLruCache.update(cache, network)
    }

    evictAll() {
        diskLruCache.evictAll()
    }

    //读取Journal文件 获取两个操作
    private newJournalWriter(): string {
        let buf = new ArrayBuffer(4096);
        let journalFile = fs.openSync(this.directory + "/" + JOURNAL_FILE, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
        let fileNumber = fs.readSync(journalFile.fd, buf)
        let textDecoder = util.TextDecoder.create("utf-8", { ignoreBOM: true });
        let decodedString = textDecoder.decodeWithStream(new Uint8Array(buf), { stream: false });
        return decodedString.substring(0, fileNumber)
    }

    private validateKey(key: string) {
        try {
            new RegExp(LEGAL_KEY_PATTERN).test(key)
        } catch (e) {
            throw new Error(
                "keys must match regex [a-z0-9_-]{1,120}: \"" + key + "\"");
        }
    }
}

class Entry {
    cleanFiles: string[]
    dirtyFiles: string[]

    constructor(value: string) {
        let array = value.split("\n")

        //第5个后就是全部的操作
        for (var index = 5; index < array.length; index++) {
            let value = array[index];

            if (value.indexOf(REMOVE) != -1) {
                this.cleanFiles.push(value)
            }

            if (value.indexOf(DIRTY) == -1 && this.cleanFiles.indexOf(value) == -1) {
                this.dirtyFiles.push(value)
            }
        }
        //在过滤一下 防止有重复数据
        for (let i = 0;i < this.cleanFiles.length; i++) {
            for (var index = 0; index < this.dirtyFiles.length; index++) {
                if (this.cleanFiles[i] == this.dirtyFiles[index]) {
                    this.dirtyFiles.splice(index, 1)
                }
            }
        }
    }

    writeLengths(writer: string) {
        this.dirtyFiles.push(writer + "\n")
    }
}

export default HttpDiskLruCache