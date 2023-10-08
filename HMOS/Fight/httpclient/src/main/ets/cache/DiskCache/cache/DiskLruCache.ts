/*
 * Copyright (C) 2022 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import fs from '@ohos.file.fs'
import { CustomMap } from './CustomMap'
import { FileUtils } from './FileUtils'
import { FileReader } from './FileReader'
import { DiskCacheEntry } from './DiskCacheEntry'
import { CryptoJS } from '@ohos/crypto-js'
import { Response } from '../../../response/Response'
import { Logger } from '../../../utils/Logger';
import hilog from '@ohos.hilog';


export class DiskLruCache {
    // 默认缓存数据最大值
    private static readonly DEFAULT_MAX_SIZE: number = 300 * 1024 * 1024

    // 缓存journal文件名称
    private static readonly journal: string = 'journal'

    // 缓存journal备份文件名称
    private static readonly journalTemp: string = 'journal_temp'

    // 备份文件save标识符
    private static readonly SAVE: string = 'DIRTY'

    // 备份文件read标识符
    private static readonly READ: string = 'READ'

    // 备份文件remove标识符
    private static readonly REMOVE: string = 'REMOVE'

    //备份文件清除全部标识符
    private static readonly CLEAN: string = 'CLEAN'

    // 缓存文件路径地址
    private path: string = ''

    // 缓存journal文件路径
    private journalPath: string = ''

    // 缓存journal备份文件路径
    private journalPathTemp: string = ''

    // 缓存数据最大值
    private maxSize: number = DiskLruCache.DEFAULT_MAX_SIZE

    // 当前缓存数据值
    private size: number = 0

    // 缓存数据集合
    private cacheMap: CustomMap<string, DiskCacheEntry> = new CustomMap<string, DiskCacheEntry>()

    private journalTitle = ''

    private constructor(path: string, maxSize: number) {
        this.path = path
        this.maxSize = maxSize
        this.journalPath = path + DiskLruCache.journal
        this.journalPathTemp = path + DiskLruCache.journalTemp
    }

    /**
     * 打开context获取的cache路径中的缓存，如果不存在缓存，则创建新缓存
     *
     * @param context 上下文
     * @param maxSize 缓存数据最大值，默认值为300M
     */
    public static create(context, title: string, maxSize?: number): DiskLruCache {
        if (!!!context) {
            throw new Error('DiskLruCache create context is empty, checking the parameter');
        }
        if (!!!maxSize) {
            maxSize = DiskLruCache.DEFAULT_MAX_SIZE
        }
        if (maxSize <= 0) {
            throw new Error("DiskLruCache create maxSize <= 0, checking the parameter");
        }
        if (!!!title) {
            throw new Error("DiskLruCache create title is empty, checking the parameter");
        }

        // 使用默认应用在内部存储上的缓存路径，作为存储地址
        let path = globalThis.cacheDir + FileUtils.SEPARATOR

        if (path.endsWith(FileUtils.SEPARATOR)) {
            path = path
        } else {
            path = path + FileUtils.SEPARATOR
        }
        let journalPath = path + DiskLruCache.journal
        let journalPathTemp = path + DiskLruCache.journalTemp


        // 判断日志文件是否存在，如果没有初始化创建
        if (FileUtils.getInstance().exist(journalPath)) {
            let stat = fs.statSync(journalPath)
            if (stat.size > 0) {
                FileUtils.getInstance().createFile(journalPathTemp)
                FileUtils.getInstance().copyFile(journalPath, journalPathTemp)
                let diskLruCache: DiskLruCache = new DiskLruCache(path, maxSize)
                diskLruCache.journalTitle = title
                diskLruCache.readJournal(journalPathTemp)
                diskLruCache.resetJournalFile()
                return diskLruCache
            } else {
                return new DiskLruCache(path, maxSize)
            }
        } else {
            FileUtils.getInstance().createFile(journalPath)
            let diskCache = new DiskLruCache(path, maxSize);
            diskCache.setTitle(title)
            return diskCache
        }
    }

    /**
     * 设置disk缓存最大数据值
     *
     * @param max 缓存数据最大值
     */
    setMaxSize(max: number) {
        if (max <= 0 || max > DiskLruCache.DEFAULT_MAX_SIZE) {
            throw new Error('setMaxSize error, checking the parameter');
        }
        this.maxSize = max
        this.trimToSize()
    }

    getMaxSize(): number {
        return this.maxSize
    }

    /**
     * 存储文件头
     */
    setTitle(title: string) {
        let fileSize
        if (!!!title) {
            throw new Error('content is null, checking the parameter')
        }
        fileSize = title.length;
        if (this.fileSizeMoreThenMaxSize(fileSize)) {
            throw new Error('content must be less then DiskLruCache Size, checking the parameter')
            return
        }
        this.journalTitle = title
        this.size = this.size + fileSize
        FileUtils.getInstance().writeData(this.journalPath, title + FileReader.LF)
    }

    /**
     * 存储disk缓存数据
     *
     * @param key 键值
     * @param content 文件内容
     */
    set(key: string, content: ArrayBuffer | string | Array<string>) {
        if (!!!key) {
            throw new Error('key is null, checking the parameter')
        }
        let fileSize
        if (content instanceof ArrayBuffer) {
            if (content == null || content.byteLength == 0) {
                throw new Error('content is null. checking the parameter')
            }
            fileSize = content.byteLength
        } else if (content instanceof Array) {
            for (var index = 0;index < content.length; index++) {
                fileSize = fileSize + content[index].length
            }
        } else {
            if (!!!content) {
                throw new Error('content is null, checking the parameter')
            }
            fileSize = content.length;
        }
        if (this.fileSizeMoreThenMaxSize(fileSize)) {
            throw new Error('content must be less then DiskLruCache Size, checking the parameter')
            return
        }
        key = CryptoJS.MD5(key)
        this.size = this.size + fileSize
        this.putCacheMap(key, fileSize)
        FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.SAVE + ' ' + key + FileReader.LF)
        this.trimToSize()
        let tempPath = this.path + key
        FileUtils.getInstance().writeNewFile(tempPath, content)
    }

    /**
     * 异步存储disk缓存数据
     *
     * @param key 键值
     * @param content 文件内容
     */
    async setAsync(key: string, content: ArrayBuffer | string): Promise<void> {
        if (!!!key) {
            throw new Error('key is null, checking the parameter')
        }
        let fileSize
        if (content instanceof ArrayBuffer) {
            if (content == null || content.byteLength == 0) {
                throw new Error('content is null. checking the parameter')
            }
            fileSize = content.byteLength
        } else {
            if (!!!content) {
                throw new Error('content is null, checking the parameter')
            }
            fileSize = content.length;
        }
        if (this.fileSizeMoreThenMaxSize(fileSize)) {
            throw new Error('content must be less then DiskLruCache Size, checking the parameter')
            return
        }
        key = CryptoJS.MD5(key)
        this.size = this.size + fileSize
        this.putCacheMap(key, fileSize)
        await FileUtils.getInstance().writeDataAsync(this.journalPath, DiskLruCache.SAVE + ' ' + key + FileReader.LF)
        this.trimToSize()
        let tempPath = this.path + key
        await FileUtils.getInstance().writeNewFileAsync(tempPath, content)
    }

    /**
     * 获取key缓存数据
     *
     * @param key key 键值
     */
    get(key: string): ArrayBuffer[] {
        if (!!!key) {
            throw new Error('key is null,checking the parameter');
        }

        key = CryptoJS.MD5(key)
        let path = this.path + key;
        if (FileUtils.getInstance().exist(path + "_1.txt")) {
            let header: ArrayBuffer = FileUtils.getInstance().readFile(path + "_0.txt")
            let body: ArrayBuffer = FileUtils.getInstance().readFile(path + "_1.txt")
            this.putCacheMap(key, header.byteLength + body.byteLength)
            FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.READ + ' ' + key + FileReader.LF)
            return [header, body]
        } else {
            return null;
        }
    }

    /**
     * 异步获取key缓存数据
     *
     * @param key 键值
     */
    async getAsync(key: string): Promise<ArrayBuffer[]> {
        if (!!!key) {
            throw new Error('key is null,checking the parameter');
        }
        key = CryptoJS.MD5(key)
        let path = this.path + key;
        if (FileUtils.getInstance().exist(path + "_1.txt")) {
            let header: ArrayBuffer = await FileUtils.getInstance().readFileAsync(path + "_0.txt")
            let body: ArrayBuffer = await FileUtils.getInstance().readFileAsync(path + "_1.txt")
            this.putCacheMap(key, header.byteLength + body.byteLength)
            await FileUtils.getInstance().writeDataAsync(this.journalPath, DiskLruCache.READ + ' ' + key + FileReader.LF)
            return [header, body]
        } else {
            return null;
        }
    }

    /**
     * 获取key缓存数据绝对路径
     *
     * @param key 键值
     */
    getFileToPath(key: string): string {
        if (!!!key) {
            throw new Error('key is null,checking the parameter');
        }
        key = CryptoJS.MD5(key);
        let path = this.path + key;
        if (FileUtils.getInstance().exist(path)) {
            FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.READ + ' ' + key + FileReader.LF);
            return path
        } else {
            return null
        }
    }

    /**
     * 异步获取key缓存数据绝对路径
     *
     * @param key 键值
     */
    async getFileToPathAsync(key: string): Promise<string> {
        if (!!!key) {
            throw new Error('key is null,checking the parameter');
        }
        key = CryptoJS.MD5(key);
        let path = this.path + key;
        if (FileUtils.getInstance().exist(path)) {
            await FileUtils.getInstance().writeDataAsync(this.journalPath, DiskLruCache.READ + ' ' + key + FileReader.LF);
            return path
        } else {
            return null
        }
    }

    /**
     * 获取缓存路径
     */
    getPath(): string {
        return this.path;
    }

    /**
     * 删除key缓存数据
     *
     * @param key 键值
     */
    deleteCacheDataByKey(key: string): DiskCacheEntry {
        if (!!!key) {
            throw new Error('key is null,checking the parameter');
        }
        key = CryptoJS.MD5(key)
        let path = this.path + key;
        if (FileUtils.getInstance().exist(path)) {
            let ab = FileUtils.getInstance().readFile(path)
            this.size = this.size - ab.byteLength
            this.cacheMap.remove(key)
            FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.REMOVE + ' ' + key + FileReader.LF)
            FileUtils.getInstance().deleteFile(path + "_0.txt")
            FileUtils.getInstance().deleteFile(path + "_1.txt")
        }
        return this.cacheMap.get(key)
    }

    /**
     *遍历当前的磁盘缓存数据
     *
     * @param fn 遍历后方法回调
     */
    foreachDiskLruCache(fn) {
        this.cacheMap.each(fn)
    }

    /**
     * 清除所有disk缓存数据
     */
    cleanCacheData() {
        this.cacheMap.each((value, key) => {
            FileUtils.getInstance().deleteFile(this.path + "/" + key + "_0.txt")
            FileUtils.getInstance().deleteFile(this.path + "/" + key + "_1.txt")
        })
        FileUtils.getInstance().deleteFile(this.journalPath)
        this.cacheMap.clear()
        this.size = 0
    }

    getCacheMap() {
        return this.cacheMap;
    }

    /**
     * 返回当前DiskLruCache的size大小
     */
    getSize() {
        return this.size;
    }

    /**
     * 根据LRU算法删除多余缓存数据
     */
    public trimToSize() {
        Logger.info("is need delete file --> size " + this.size + ", this.maxSize " + this.maxSize)
        while (this.size > this.maxSize) {
            let tempKey: string = this.cacheMap.getFirstKey()
            let fileSize = FileUtils.getInstance().getFileSize(this.path + tempKey + "_0.txt")
            fileSize = FileUtils.getInstance().getFileSize(this.path + tempKey + "_1.txt")
            if (fileSize > 0) {
                this.size = this.size - fileSize
            }
            try {
                FileUtils.getInstance().deleteFile(this.path + tempKey + "_0.txt")
                FileUtils.getInstance().deleteFile(this.path + tempKey + "_1.txt")
                this.cacheMap.remove(tempKey)
            } catch (e) {
                this.cacheMap.remove(tempKey)
            }
            FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.REMOVE + ' ' + tempKey + FileReader.LF)
        }
    }

    public update(cache: Response, network: Response) {
        let cacheKey = CryptoJS.MD5(cache.request.url.url)
        let path = this.path + cacheKey + "_1.txt";
        if (FileUtils.getInstance().exist(path)) {
            let ab = FileUtils.getInstance().readFile(path)
            this.size = this.size - ab.byteLength
            FileUtils.getInstance().deleteFile(path)
            this.writeBodyToFile(cache.request.url.url, network.getBody())
        }
    }

    public evictAll() {
        this.cacheMap.each((value, key) => {
            this.deleteCacheDataByKey(key)
        })
    }

    private writeBodyToFile(key: string, content: ArrayBuffer | string) {
        if (!!!key) {
            throw new Error('key is null, checking the parameter')
        }
        let fileSize
        if (content instanceof ArrayBuffer) {
            if (content == null || content.byteLength == 0) {
                throw new Error('content is null. checking the parameter')
            }
            fileSize = content.byteLength
        } else {
            if (!!!content) {
                throw new Error('content is null, checking the parameter')
            }
            fileSize = content.length;
        }

        if (this.fileSizeMoreThenMaxSize(fileSize)) {
            throw new Error('content must be less then DiskLruCache Size, checking the parameter')
            return
        }

        key = CryptoJS.MD5(key)
        this.size = this.size + fileSize
        this.putCacheMap(key, fileSize)
        FileUtils.getInstance().writeData(this.journalPath, "UPDATE" + ' ' + key + FileReader.LF)
        this.trimToSize()
        let tempPath = this.path + key + "_1.txt"
        FileUtils.getInstance().writeNewFile(tempPath, content)
    }

    /**
     * 处理journal文件数据
     *
     * @param line 日志行字符串
     */
    private dealWithJournal(line: string) {
        let oldLine = ''
        try {
            let lineData = line.split(' ')
            if (lineData.length > 1 && oldLine != line) {
                if (lineData[0] != DiskLruCache.REMOVE) {
                    let headerPath = this.path + lineData[1] + "_0.txt"
                    let bodyPath = this.path + lineData[1] + "_1.txt"

                    let headerStat = fs.statSync(headerPath)
                    let bodyStat = fs.statSync(bodyPath)
                    if (headerStat.isFile() && headerStat.size > 0) {
                        this.size = this.size + headerStat.size + bodyStat.size
                        FileUtils.getInstance().writeData(this.journalPath, line + FileReader.LF)
                        this.putCacheMap(lineData[1], headerStat.size + bodyStat.size)
                    }
                    oldLine = line
                } else {
                    if (this.cacheMap.hasKey(lineData[1])) {
                        let cacheEntry: DiskCacheEntry = this.cacheMap.get(lineData[1])
                        this.size = this.size - cacheEntry.getLength()
                        this.cacheMap.remove(lineData[1])
                    }
                }
            }
        } catch (e) {
            hilog.info(0x0001, 'DiskLruCache - dealWithJournal e ', e);
        }
    }

    /**
     * 重置journal文件数据
     */
    private resetJournalFile() {
        FileUtils.getInstance().clearFile(this.journalPath)
        for (let key of this.cacheMap.keys()) {
            this.setTitle(this.journalTitle)
            FileUtils.getInstance().writeData(this.journalPath, DiskLruCache.SAVE + ' ' + key + FileReader.LF)
        }
    }

    /**
     * 读取journal文件的缓存数据
     *
     * @param path 日志缓存文件路径地址
     */
    private readJournal(path: string) {
        let fileReader = new FileReader(path)
        let line: string = ''
        let cacheFlag = 0
        while (!fileReader.isEnd()) {
            line = fileReader.readLine()
            line = line.replace(FileReader.LF, '').replace(FileReader.CR, '')
            if (cacheFlag > 4) {
                this.dealWithJournal(line)
            }
            cacheFlag++
        }
        fileReader.close()
        FileUtils.getInstance().deleteFile(this.journalPathTemp)
        this.trimToSize()
        cacheFlag = 0
    }

    /**
     * 缓存数据map集合
     *
     * @param key 键值
     * @param length 缓存文件大小
     */
    private putCacheMap(key: string, length?: number) {
        if (length > 0) {
            this.cacheMap.put(key, new DiskCacheEntry(key, length))
        } else if (!this.cacheMap.hasKey(key)) {
            this.cacheMap.put(key, new DiskCacheEntry(key))
        }
    }

    /**
     * 图片文件过大 直接超过DiskLruCache上限
     */
    private fileSizeMoreThenMaxSize(fileSize: number): boolean {
        if (fileSize > this.maxSize) {
            return true;
        }
        return false;
    }
}