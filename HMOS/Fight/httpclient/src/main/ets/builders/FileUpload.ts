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

var _files = Symbol();
var _data = Symbol();

class FileUpload {
    constructor(build) {
        if (!arguments.length) {
            build = new FileUpload.Builder();
        }
        this[_files] = build[_files];
        this[_data] = build[_data];
    }

    static get Builder() {
        class Builder {
            constructor() {
                this[_files] = [];
                this[_data] = [];
            }

            addFile(furi) {
                let fileName = furi.substring(furi.lastIndexOf('/') + 1);
                let fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
                let fname = fileName.substring(0, fileName.lastIndexOf('.'));
                let fileObject = { filename: fileName, name: fname, uri: furi, type: fileType };
                this[_files].push(fileObject);
                return this;
            }

            addData(fname, fvalue) {
                let dataObject = { name: fname, value: fvalue };
                this[_data].push(dataObject);
                return this;
            }

            build() {
                return new FileUpload(this);
            }
        }

        return Builder;
    }

    getFile() {
        if (this[_files]) {
            return this[_files];
        }
        return [];
    }

    getData() {
        if (this[_data]) {
            return this[_data];
        }
        return [];
    }
}

export default FileUpload;