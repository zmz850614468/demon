/**
 * Copyright (c) 2022 Huawei Device Co., Ltd.
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

export default {
    isEmpty: function (str: string): boolean {
        return str == 'undefined' || !!!str || !/[^\s]/.test(str);
    },
    removeSpace: function (str: string): string {
        if (this.isEmpty(str)) {
            return '';
        }
        return str.replace(/[\s]/g, '');
    },
    toStringArr(str: string): string[] {
        let arrResult: string[] = []
        if (this.isEmpty(str)) {
            return arrResult;
        }
        for (let i = 0;i < str.length; i++) {
            arrResult.push(str.charAt(i))
        }
        return arrResult
    },
    uint8ArrayToShowStr(uint8Array) {
        return Array.prototype.map
            .call(uint8Array, (x) => ('00' + x.toString(16)).slice(-2))
            .join('');
    },
    strArrToString(strArr: String[], offset: number, count: number) {
        if (!!!strArr || strArr.length == 0) {
            return ''
        }
        let resultStr: string = ''
        let maxStrCount = strArr.length - offset
        count = count > maxStrCount ? maxStrCount : count
        for (let i = offset;i < strArr.length; i++) {
            resultStr += strArr[i]
        }
        return resultStr
    },
    stringToUint8Array(str) {
        var arr = [];
        for (var i = 0, j = str.length; i < j; i++) {
            arr.push(str.charCodeAt(i));
        }
        return new Uint8Array(arr);
    },
    getCurrentDayTime() {
        let strResult: string = '';
        let dateNow: Date = new Date();
        strResult += (dateNow.getFullYear() % 1000 + '').padStart(2, '0')
        strResult += (dateNow.getMonth() + 1 + '').padStart(2, '0')
        strResult += (dateNow.getDate() + '').padStart(2, '0')
        strResult += (dateNow.getHours() + '').padStart(2, '0')
        strResult += (dateNow.getMinutes() + '').padStart(2, '0')
        strResult += (dateNow.getSeconds() + '').padStart(2, '0')
        return parseInt(strResult)
    },
    getNumberInStr(str: string) {
        if (this.isEmpty(str)) {
            return 0;
        }
        return parseInt(str)
    }
}