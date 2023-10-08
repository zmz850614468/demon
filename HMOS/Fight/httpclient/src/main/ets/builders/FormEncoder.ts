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

import RequestBody from '../RequestBody'
import Mime from './Mime'

var _query = Symbol();

class FormEncoder {
    constructor(build) {
        if (!arguments.length) {
            build = new FormEncoder.Builder();
        }
        this[_query] = build[_query];
    }

    static get Builder() {
        class Builder {
            constructor() {
                this[_query] = '';
            }

            add(name, value) {
                if (this[_query].length > 0) this[_query] += '&';
                this[_query] += this.percentageEncode(name) + '=' + this.percentageEncode(value);
                return this;
            }

            percentageEncode(str) {
                var ret = '';
                var temp;
                for (var i = 0; i < str.length; i++) {
                    var c = str.charCodeAt(i);

                    if ((c >= 97 && c <= 122) ||
                    (c >= 65 && c <= 90) ||
                    (c >= 48 && c <= 57)) {
                        ret += str[i];
                    } else {
                        if (c == 32 /*SPACE*/
                        || (c == 33 /*!*/
                        ) || (c >= 35 /*#*/
                        && c <= 44 /*,*/
                        ) || (c == 47 /*/*/
                        ) || (c == 58 /*:*/
                        ) ||
                        (c == 59 /*;*/
                        ) || (c == 61 /*=*/
                        ) || (c == 63 /*?*/
                        ) ||
                        (c == 64 /*@*/
                        ) || (c == 91 /*[*/
                        ) || (c == 93 /*]*/
                        )) {
                            ret += '%';
                            temp = (c & 0xF0) >> 4;
                            if (temp > 9)
                                ret += String.fromCharCode('A'.charCodeAt(0) + temp - 10);
                            else
                                ret += temp;
                            temp = c & 0x0F;
                            if (temp > 9)
                                ret += String.fromCharCode('A'.charCodeAt(0) + temp - 10);
                            else
                                ret += temp;
                        }
                        else {
                            ret += str[i];
                        }
                    }
                }
                return ret;
            }

            build() {
                return new FormEncoder(this);
            }
        }

        return Builder;
    }

    createRequestBody() {
        return RequestBody.create(this[_query],
        new Mime.Builder()
            .contentType('application/x-www-form-urlencoded')
            .build());
    }
}

export default FormEncoder;