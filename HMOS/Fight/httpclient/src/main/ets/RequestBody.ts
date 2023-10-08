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

import FormatUtils from './utils/FormatUtils'
import { Logger } from './utils/Logger'

var _mimes = Symbol();
var _content = Symbol();

var LF = '\r\n';

class RequestBody {
    private oneShot = false

    constructor() {
        this[_mimes] = {};
        this[_content] = null;
    }

    get mimes() {
        return this[_mimes];
    }

    set mimes(value) {
        this[_mimes] = value;
    }

    get content() {
        return this[_content];
    }

    set content(value) {
        this[_content] = value;
    }

    static create(...args: any[]) {
        if (args[0] == undefined || args[0] == null || args[0] == '') {
            throw new Error('Incorrect request body content');
        }

        var rb = new RequestBody();

        if (args[0] && FormatUtils.checkTypedArrayType(args[0])) {
            rb.content = args[0].buffer;
        } else if (args[0] && FormatUtils.isArrayBuffer(args[0])) {
            rb.content = args[0];
        }
        else if (typeof args[0] === 'string') {
            rb.content = args[0];
        } else {
            rb.content = JSON.stringify(args[0]);
            if (rb.content && (rb.content.localeCompare('{}') == 0)) {
                rb.content = null;
            }
        }
        Logger.info("request body contents:" + rb.content);

        rb.mimes = {};

        for (var _len = args.length, mimes = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
            mimes[_key - 1] = args[_key];
        }

        if (mimes && mimes.length) {
            if (typeof mimes[0] === 'string') rb.mimes = FormatUtils.mimeStringArrayToObject(mimes);
            else rb.mimes = mimes[0];
        }
        Logger.info("request body mimes:" + rb.mimesToString());
        return rb;
    }

    mimesToString() {
        var res = '';

        for (var mimeName in this.mimes) {
            if (Object.prototype.hasOwnProperty.call(this.mimes, mimeName)) res += mimeName + ': '
            + this.mimes[mimeName] + LF;
        }

        return res;
    }

    isOneShot(): boolean {
        return this.oneShot
    }
}

export default RequestBody;