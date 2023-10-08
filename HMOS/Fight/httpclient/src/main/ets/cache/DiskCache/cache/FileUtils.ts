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
import { Logger } from '../../../utils/Logger';

export class FileUtils {
    static readonly SEPARATOR: string = '/'
    private static sInstance: FileUtils
    base64Str: string = ''

    /**
     * 单例实现FileUtils类
     */
    public static getInstance(): FileUtils {
        if (!this.sInstance) {
            this.sInstance = new FileUtils();
        }
        return this.sInstance;
    }

    /**
     * 新建文件
     *
     * @param path 文件绝对路径及文件名
     * @return number 文件句柄id
     */
    createFile(path: string): number {
        return fs.openSync(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE).fd
    }

    /**
     * 删除文件
     *
     * @param path 文件绝对路径及文件名
     */
    deleteFile(path: string): void {
        fs.unlinkSync(path);
    }

    /**
     * 拷贝文件
     *
     * @param src 文件绝对路径及文件名
     * @param dest 拷贝到对应的路径
     */
    copyFile(src: string, dest: string) {
        fs.copyFileSync(src, dest);
    }

    /**
     * 异步拷贝文件
     *
     * @param src 文件绝对路径及文件名
     * @param dest 拷贝到对应的路径
     */
    async copyFileAsync(src: string, dest: string): Promise<void> {
        await fs.copyFile(src, dest);
    }

    /**
     * 清空已有文件数据
     *
     * @param path 文件绝对路径
     */
    clearFile(path: string): number {
        return fs.openSync(path, fs.OpenMode.TRUNC).fd
    }

    /**
     * 向path写入数据
     *
     * @param path 文件绝对路径
     * @param content 文件内容
     */
    writeData(path: string, content: ArrayBuffer | string) {
        let fd = fs.openSync(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
        let stat = fs.statSync(path)
        fs.writeSync(fd.fd, content, { offset: stat.size })
        fs.closeSync(fd)
    }

    /**
     * 异步向path写入数据
     *
     * @param path 文件绝对路径
     * @param content 文件内容
     */
    async writeDataAsync(path: string, content: ArrayBuffer | string): Promise<void> {
        let fd = await fs.open(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
        let stat = await fs.stat(path)
        await fs.write(fd.fd, content, { offset: stat.size })
        await fs.close(fd)
    }

    /**
     * 判断path文件是否存在
     *
     * @param path 文件绝对路径
     */
    exist(path: string): boolean {
        try {
            return fs.accessSync(path)
        } catch (e) {
            return false
        }
    }

    /**
     * 向path写入数据
     *
     * @param path 文件绝对路径
     * @param data 文件内容
     */
    writeNewFile(path: string, data: ArrayBuffer | string | Array<string>) {
        if (data instanceof Array) {
            for (var index = 0; index < data.length; index++) {
                this.createFile(path + "_" + index + ".txt")
            }
            this.writeFile(path, data)
        } else {
            this.createFile(path)
            this.writeFile(path, data)
        }
    }

    /**
     * 向path写入数据
     *
     * @param path 文件绝对路径
     * @param data 文件内容
     */
    async writeNewFileAsync(path: string, data: ArrayBuffer | string): Promise<void> {
        await fs.open(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
        let fd = await fs.open(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
        await fs.truncate(fd.fd)
        await fs.write(fd.fd, data)
        await fs.fsync(fd.fd)
        await fs.close(fd)
    }

    /**
     * 获取path的文件大小
     *
     * @param path 文件绝对路径
     */
    getFileSize(path: string): number {
        try {
            let stat = fs.statSync(path)
            return stat.size
        } catch (e) {
            Logger.error("FileUtils getFileSize e " + e)
            return -1
        }
    }

    /**
     * 读取路径path的文件
     *
     * @param path 文件绝对路径
     */
    readFile(path: string): ArrayBuffer {
        let fd = fs.openSync(path, 0o2);
        let length = fs.statSync(path).size
        let buf = new ArrayBuffer(length);
        fs.readSync(fd.fd, buf)
        return buf
    }

    /**
     * 读取路径path的文件
     *
     * @param path 文件绝对路径
     */
    async readFileAsync(path: string): Promise<ArrayBuffer> {
        let stat = await fs.stat(path);
        let fd = await fs.open(path, 0o2);
        let length = stat.size;
        let buf = new ArrayBuffer(length);
        await fs.read(fd.fd, buf);
        return buf
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹绝对路径，只有是权限范围内的路径，可以生成
     * @param recursive
     */
    createFolder(path: string, recursive?: boolean) {
        if (recursive) {
            if (!this.existFolder(path)) {
                let lastInterval = path.lastIndexOf(FileUtils.SEPARATOR)
                if (lastInterval == 0) {
                    return
                }
                let newPath = path.substring(0, lastInterval)
                this.createFolder(newPath, true)
                if (!this.existFolder(path)) {
                    fs.mkdirSync(path)
                }
            }
        } else {
            if (!this.existFolder(path)) {
                fs.mkdirSync(path)
            }
        }
    }

    /**
     * 判断文件夹是否存在
     *
     * @param path 文件夹绝对路径
     */
    existFolder(path: string): boolean {
        try {
            let stat = fs.statSync(path)
            return stat.isDirectory()
        } catch (e) {
            Logger.error("FileUtils folder exist e " + e)
            return false
        }
    }

    private writeFile(path: string, content: ArrayBuffer | string | Array<string>) {
        if (content instanceof Array) {
            for (var index = 0; index < content.length; index++) {
                try {
                    let fd = fs.openSync(path + "_" + index + ".txt", fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
                    fs.truncateSync(fd.fd)
                    fs.writeSync(fd.fd, content[index])
                    fs.fsyncSync(fd.fd)
                    fs.closeSync(fd)
                } catch (e) {
                }
            }
        } else {
            let fd = fs.openSync(path, fs.OpenMode.READ_WRITE | fs.OpenMode.CREATE)
            fs.truncateSync(fd.fd)
            fs.writeSync(fd.fd, content)
            fs.fsyncSync(fd.fd)
            fs.closeSync(fd)
        }

    }
}