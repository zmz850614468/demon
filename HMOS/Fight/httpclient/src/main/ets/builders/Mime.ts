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

import ConstantManager from '../ConstantManager'

var _mime = Symbol();

class Mime {
    constructor(build) {
        if (!arguments.length) {
            build = new Mime.Builder();
        }
        this[_mime] = build[_mime];
    }

    static get Builder() {
        class Builder {
            constructor() {
                this[_mime] = {};
            }

            contentType(...arg: any[]) {
                for (var _len = arg.length, extra = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
                    extra[_key - 1] = arg[_key];
                }
                this._addMimeHeader(ConstantManager.CONTENT_TYPE, arg[0], extra);
                return this;
            }

            version(value) {
                for (var _len2 = arguments.length, extra = Array(_len2 > 1 ? _len2 - 1 : 0),
                    _key2 = 1; _key2 < _len2; _key2++) {
                    extra[_key2 - 1] = arguments[_key2];
                }

                this._addMimeHeader('MIME-Version', value, extra);

                return this;
            }

            contentID(value) {
                for (var _len3 = arguments.length, extra = Array(_len3 > 1 ? _len3 - 1 : 0),
                    _key3 = 1; _key3 < _len3; _key3++) {
                    extra[_key3 - 1] = arguments[_key3];
                }

                this._addMimeHeader('Content-ID', value, extra);

                return this;
            }

            contentLocation(value) {
                for (var _len4 = arguments.length, extra = Array(_len4 > 1 ? _len4 - 1 : 0),
                    _key4 = 1; _key4 < _len4; _key4++) {
                    extra[_key4 - 1] = arguments[_key4];
                }

                this._addMimeHeader('Content-Location', value, extra);

                return this;
            }

            contentDescription(value) {
                for (var _len5 = arguments.length, extra = Array(_len5 > 1 ? _len5 - 1 : 0),
                    _key5 = 1; _key5 < _len5; _key5++) {
                    extra[_key5 - 1] = arguments[_key5];
                }

                this._addMimeHeader('Content-Description', value, extra);

                return this;
            }

            contentDisposition(value) {
                for (var _len6 = arguments.length, extra = Array(_len6 > 1 ? _len6 - 1 : 0),
                    _key6 = 1; _key6 < _len6; _key6++) {
                    extra[_key6 - 1] = arguments[_key6];
                }

                this._addMimeHeader('Content-Disposition', value, extra);

                return this;
            }

            contentTransferEncoding(value) {
                for (var _len7 = arguments.length, extra = Array(_len7 > 1 ? _len7 - 1 : 0),
                    _key7 = 1; _key7 < _len7; _key7++) {
                    extra[_key7 - 1] = arguments[_key7];
                }

                this._addMimeHeader('Content-Transfer-Encoding', value, extra);

                return this;
            }

            _addMimeHeader(name, value, extra) {
                var res = value;

                var cParams = extra.length;

                if (cParams % 2 == 0) {
                    for (var ix = 0; ix <= extra.length - 2; ix += 2) {
                        res += '; ' + extra[ix + 0] + '=' + extra[ix + 1];
                    }
                }

                this[_mime][name] = res;
            }

            build() {
                return new Mime(this);
            }
        }

        return Builder;
    }

    getMime() {
        return this[_mime];
    }
}

export default Mime;