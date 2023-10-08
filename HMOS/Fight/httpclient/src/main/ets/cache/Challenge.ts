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

class Challenge {
    private _scheme: string
    private _authParams: Map<string, string>

    constructor(scheme: string, authParams: Map<string, string> | string) {
        if (scheme == null) throw new Error('scheme == null')
        if (authParams == null) throw new Error("authParams == null");
        this._scheme = scheme;
        if (typeof (authParams) == 'string') {
            this._authParams = new Map<string, string>();
            this._authParams.set("realm", authParams)
        } else {
            let newAuthParams = new Map()


        }
    }

    withCharset(charset: string): Challenge {
        if (charset == null) throw new Error("charset == null");
        this._authParams = new Map(this._authParams);
        this._authParams.set("charset", charset)
        return new Challenge(this._scheme, this._authParams)
    }

    public scheme(): string {
        return this._scheme;
    }

    authParams() {
        return this._authParams;
    }

    realm(): string {
        return this._authParams.get("realm")
    }

    charset() {
        let charset = this._authParams.get("charset")
        if (charset != null) {
            return charset
        }
        return "ISO_8859_1"
    }

    hashCode() {
        let result = 29;
        result = 31 * result + this.hashcode(this._scheme);
        return result;
    }

    private hashcode(str) {
        var hash = 0, i, chr, len;
        if (str.length === 0) return hash;
        for (i = 0, len = str.length; i < len; i++) {
            chr = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + chr;
            hash |= 0; // Convert to 32bit integer
        }
        return hash;
    }
}

export default Challenge