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
import { Logger } from './Logger';
import util from '@ohos.util';

function FormatUtils() {
};


var TAG = 'FormatUtils:: ';

/**
 * convert a key/value object into a query string
 *
 * @param keyValueObject a key/value object
 *
 * @returns {String} a query string
 */
FormatUtils.objectToQueryString = function objectToQueryString(keyValueObject) {
    var value = null;
    var res = '';
    var keyValue = void 0;

    if (keyValueObject === null) return '';

    for (var key in keyValueObject) {
        if (Object.prototype.hasOwnProperty.call(keyValueObject, key)) {
            value = keyValueObject[key].toString();
            keyValue = key + '=' + value;
            res += res === '' ? '?' + keyValue : '&' + keyValue;
        }
    }

    return res;
}

/**
 * convert a mime string array into key/value Object.
 * <pre>
 *     mimeStringArrayToObject(['Content-Type:image/jpg', 'Content-ID: myid'])
 *     ==>
 *     {
 *          'Content-Type': 'image/jpg',
 *          'Content-ID': 'myid',
 *     }
 * </pre>
 *
 * @param {Array} mimes array of strings that each represent a mime header.
 *
 * @return {Object} a key/value Object
 */
FormatUtils.mimeStringArrayToObject = function mimeStringArrayToObject(mimes) {
    if (!Array.isArray(mimes)) return mimes;

    var keyValue = {};

    var msg = TAG + 'mimeStringArrayToObject(..) --> mimes array is not formatted correctly';

    var _iteratorNormalCompletion = true;
    var _didIteratorError = false;
    var _iteratorError = undefined;

    try {
        for (var _iterator = mimes[Symbol.iterator](),
            _step;!(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
            var item = _step.value;
            if (typeof item === 'string') {
                var arr = item.split(':');
                if (arr.length < 2) {
                    keyValue[arr[0].trim()] = arr[1].trim();
                }
            }
        }
    } catch (err) {
        _didIteratorError = true;
        _iteratorError = err;
    } finally {
        try {
            if (!_iteratorNormalCompletion && _iterator.return) {
                _iterator.return();
            }
        } finally {
            if (_didIteratorError) {
                //error
            }
        }
    }

    return keyValue;
}

/**
 * string 转 Uint8Array
 * @param str 输入String
 */
FormatUtils.stringToUint8Array = function stringToUint8Array(str) {
    let that = new util.TextEncoder();
    let buffer = new ArrayBuffer(str.length);
    let array = new Uint8Array(buffer);
    let result = that.encodeInto(str, array);
    return array;
}

/**
 * string 转 string
 * @param str Uint8Array
 */
FormatUtils.uint8ArrayTostring = function uint8ArrayTostring(array) {
    let textDecoder = new util.TextDecoder("utf-8", { ignoreBOM: true });
    let retStr = textDecoder.decode(array, { stream: false });
    return retStr;
}
FormatUtils.mergeUint8Array = function mergeUint8Array(a, b) {
    // Checks for truthy values on both arrays
    if (!a && !b) Logger.info('Please specify valid arguments for parameters a and b.');

    // Checks for truthy values or empty arrays on each argument
    // to avoid the unnecessary construction of a new array and
    // the type comparison
    if (!b || b.length === 0) return a;
    if (!a || a.length === 0) return b;

    // Make sure that both typed arrays are of the same type
    if (Object.prototype.toString.call(a) !== Object.prototype.toString.call(b))
        Logger.info('The types of the two arguments passed for parameters a and b do not match.');
    Logger.info("a length:" + a.byteLength + ", b length" + b.byteLength);
    let ret = new Uint8Array(a.byteLength + b.byteLength);
    ret.set(a);
    ret.set(b, a.byteLength);
    Logger.info("merged array" + this.uint8ArrayTostring(ret))
    return ret
};

FormatUtils.isArrayBuffer = function isArrayBuffer(value) {
    const hasArrayBuffer = typeof ArrayBuffer === 'function';
    const { toString } = Object.prototype;
    return hasArrayBuffer && (value instanceof ArrayBuffer || toString.call(value) === '[object ArrayBuffer]');
}

FormatUtils.copyUint8Array = function copyUint8Array(src) {
    return new Uint8Array(src);
}
FormatUtils.checkTypedArrayType = function checkTypedArrayType(someTypedArray) {
    const typedArrayTypes = [
        Int8Array,
        Uint8Array,
        Uint8ClampedArray,
        Int16Array,
        Uint16Array,
        Int32Array,
        Uint32Array,
        Float32Array,
        Float64Array,
        BigInt64Array,
        BigUint64Array
    ];
    const checked = typedArrayTypes.filter(ta => someTypedArray.constructor === ta);
    return checked.length && checked[0].name || null;
}

export default FormatUtils;