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

import StringUtil from '../utils/StringUtil'

export default class Challenge {
    scheme: string;
    authParams: Map<string, string>;

    constructor(scheme: string, authParams?: Map<string, string>, realm?: string) {
        if (StringUtil.isEmpty(scheme)) throw new Error("scheme == null");
        if ((!!!authParams) && StringUtil.isEmpty(scheme)) throw new Error("authParams or realm == null");
        this.scheme = scheme;
        let newAuthParams: Map<string, string> = new Map<string, string>();

        if (!!authParams && authParams != undefined) {
            let iter = authParams.keys();
            let temp: string = iter.next().value;
            while (!StringUtil.isEmpty(temp)) {
                let value: string = authParams.get(temp)
                newAuthParams.set(temp.toLocaleLowerCase(), value);
                temp = iter.next().value;
            }
        }
        if (!StringUtil.isEmpty(realm)) {
            newAuthParams.set("realm", realm)
        }
        this.authParams = newAuthParams
    }

    /** Returns a copy of this charset that expects a credential encoded with {@code charset}. */
    public withCharset(charset: string): Challenge {
        if (StringUtil.isEmpty(charset)) throw new Error("charset == null");
        let authParams: Map<string, string> = new Map<string, string>();
        let iter = authParams.keys();
        let temp: string = iter.next().value;
        while (!StringUtil.isEmpty(temp)) {
            let value: string = authParams.get(temp)
            authParams.set(temp.toUpperCase(), value);
            temp = iter.next().value;
        }
        authParams.set("charset", charset);
        return new Challenge(this.scheme, authParams);
    }

    /** Returns the authentication scheme, like {@code Basic}. */
    public getScheme(): string {
        return this.scheme;
    }

    /**
     * Returns the auth params, including {@code realm} and {@code charset} if present, but as
     * strings. The map's keys are lowercase and should be treated case-insensitively.
     */
    public getAuthParams(): Map<string, string> {
        return this.authParams;
    }

    /** Returns the protection space. */
    public realm(): string {
        return this.authParams.get("realm");
    }

    /** Returns the charset that should be used to encode the credentials. */
    public charset(): string {
        let charset: string = this.authParams.get("charset");
        return!!charset ? charset : 'ISO-8859-1';
    }

    public equals(other: Object): boolean {
        return other instanceof Challenge
        && ((other as Challenge).getScheme() == this.scheme)
        && ((other as Challenge).getAuthParams() == this.authParams);
    }

    public hashCode(): number {
        let result: number = 29;
        result = 31 * result + this.custStringHashCode(this.scheme);
        result = 31 * result + this.customMapHashCode(this.authParams);
        return result;
    }

    public customMapHashCode(custMap: Map<string, string>) {
        let resultStr: string = ''
        let iter = custMap.keys();
        let temp: string = iter.next().value;
        while (!!temp) {
            let value: string = custMap.get(temp)
            resultStr += (temp + value)
            temp = iter.next().value;
        }
        return this.custStringHashCode(resultStr)
    }

    public custStringHashCode(str: string): number {

        let h = 0;
        for (let i = 0;i < str.length; i++) {
            let charCode: number = str.charCodeAt(i)
            h += (charCode * 31)
        }
        return h;
    }

    public toString(): string {
        return this.scheme + " authParams=" + this.authParams.toString();
    }
}
