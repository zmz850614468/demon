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

import fs from '@ohos.file.fs';
import { Logger } from './Logger';
import hilog from '@ohos.hilog';

function FileUtils() {
}

FileUtils.isFilePathValid = function isFilePathValid(filePath) {
    const lastSlash = filePath.lastIndexOf("/");
    const directoryName = filePath.substring(0, lastSlash);
    const fileName = filePath.substring(lastSlash + 1);
    const downloadPathDirectory = "data/misc";
    Logger.info("isFilePathValid lastSlash INDEX : " + lastSlash + " directoryName : "
    + directoryName + " fileName : " + fileName);
    //Framework requires filepath to include fileName
    if (!fileName.includes(".")) {
        Logger.info("isFilePathValid filename does not include .");
        throw new Error("Invalid filepath. Need to include filename");
    }
    Logger.info("isFilePathValid fileName is ok");
    let isDir = false;
    let errorMsg = "";
    try {
        isDir = fs.statSync(directoryName).isDirectory();
    } catch (err) {
        hilog.info(0x0001, "isFilePathValid fs.statSync(directoryName).isDirectory()", err);
        isDir = false;
        errorMsg = err;
    }
    if (!isDir) {
        Logger.info("isFilePathValid isDir is  " + isDir + " errorMessage : " + errorMsg);
        throw new Error("Invalid filePath : " + errorMsg);
    }
    Logger.info("isFilePathValid filePath is valid");
    return true;

}

/**
 * 新建文件
 * @param path 文件绝对路径及文件名
 * @return number 文件句柄id
 */
FileUtils.createFile = function createFile(path) {
    Logger.info('createFile:' + path)
    return fs.openSync(path, fs.OpenMode.CREATE)
}

/**
 * 删除文件
 * @param path 文件绝对路径及文件名
 */
FileUtils.deleteFile = function deleteFile(path) {
    Logger.info('Delete file:' + path)
    fs.unlinkSync(path);
}

/**
 * 判断path文件是否存在
 */
FileUtils.exist = function exist(path) {
    try {
        let stat = fs.statSync(path)
        return stat.isFile()
    } catch (e) {
        hilog.info(0x0001, "FileUtils - ", "fileutils exsit e %{public}s path=> %{public}s", JSON.stringify(e), path);
        return false
    }
}

export default FileUtils;