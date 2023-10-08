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

import http from '@ohos.net.http';
import Request from '../Request'
import Headers from '../core/Headers'
import TreeSet from '@ohos.util.TreeSet';
import StringUtil from '../utils/StringUtil';
import ObjectUtil from '../utils/ObjectUtil';
import Challenge from '../authenticator/Challenge';
import ArrayList from '@ohos.util.ArrayList';
import { Response } from '../response/Response';
import { Logger } from '../utils/Logger';

/** Headers and utilities for internal use by HttpClient. */
export default class HttpHeaders {
    constructor() {
    }

    public static getResponsHeaders(response: Response): Headers {
        let headersBuilder = new Headers.Builder();
        let responseHeader: Object = JSON.parse(response.header)
        Logger.info("authenticate:getResponsHeaders:responseHeader" + JSON.stringify(responseHeader));
        let keys = Object.keys(responseHeader)
        for (let key of keys) {
            if (!StringUtil.isEmpty(key)) {
                headersBuilder.addLenient(key.toLowerCase(), responseHeader[key])
            }
        }

        return headersBuilder.build()
    }

    public static contentLength(response?: Response, headers?: Headers): number {
        let headersRes = (!!response ? response.header : headers)
        return headersRes["Content-Length"];
    }


    /**
     * Returns true if none of the Vary headers have changed between {@code cachedRequest} and {@code
     * newRequest}.
     */
    public static varyMatches(cachedResponse: Response, cachedRequest: Headers, newRequest: Request): boolean {
        HttpHeaders.varyFields(cachedResponse, cachedRequest).forEach((value, key) => {
            if (cachedRequest.values(value) != newRequest.headers[value]) {
                return false;
            }
        });

        return true;
    }
    /**
     * Returns true if a Vary header contains an asterisk. Such responses cannot be cached.
     */
    public static hasVaryAll(response: Response, responseHeaders: Headers): boolean {
        if (ObjectUtil.isEmpty(response) && ObjectUtil.isEmpty(responseHeaders)) {
            return false
        }
        return HttpHeaders.varyFields(response, responseHeaders).has('*')
    }

    /**
     * Returns the names of the request headers that need to be checked for equality when caching.
     */
    public static varyFields(response: Response, responseHeaders: Headers): TreeSet<string> {
        let result: TreeSet<string> = new TreeSet <string>()
        if (ObjectUtil.isEmpty(response) && ObjectUtil.isEmpty(responseHeaders)) {
            return result
        }
        let headersResult: Headers = (ObjectUtil.isEmpty(response) ? responseHeaders : HttpHeaders.getResponsHeaders(response))
        for (let i = 0, size = headersResult.size(); i < size; i++) {
            if (!("Vary".toLowerCase() == headersResult.name(i).toLowerCase())) {
                continue;
            }

            let value: string = headersResult.value(i);

            for (let varyField of value.split(",")) {
                result.add(varyField.trim());
            }
        }
        return result;
    }

    /**
     * Returns the subset of the headers in {@code response}'s request that impact the content of
     * response's body.
     */
    /* public static  varyHeaders(response:http.HttpResponse):Headers {
       // Use the request headers sent over the network, since that's what the
       // response varies on. Otherwise HttpClient-supplied headers like
       // "Accept-Encoding: gzip" may be lost.
       let requestHeaders:Headers = response.networkResponse().request().headers();
       Headers responseHeaders = response.headers();
       return varyHeaders(requestHeaders, responseHeaders);
     }*/

    /**
     * Returns the subset of the headers in {@code requestHeaders} that impact the content of
     * response's body.
     */
    public static varyHeaders(requestHeaders: Headers, responseHeaders: Headers): Headers {
        let varyFields: TreeSet<string> = HttpHeaders.varyFields(null, responseHeaders);
        if (varyFields.isEmpty()) return new Headers.Builder().build();

        let result = new Headers.Builder();
        for (let i = 0, size = requestHeaders.size(); i < size; i++) {
            let fieldName = requestHeaders.name(i);
            if (varyFields.has(fieldName)) {
                result.add(fieldName, requestHeaders.value(i));
            }
        }
        return result.build();
    }

    /**
     * Parse RFC 7235 challenges. This is awkward because we need to look ahead to know how to
     * interpret a token.
     *
     * <p>For example, the first line has a parameter name/value pair and the second line has a single
     * token68:
     *
     * <pre>   {@code
     *
     *   WWW-Authenticate: Digest foo=bar
     *   WWW-Authenticate: Digest foo=
     * }</pre>
     *
     * <p>Similarly, the first line has one challenge and the second line has two challenges:
     *
     * <pre>   {@code
     *
     *   WWW-Authenticate: Digest ,foo=bar
     *   WWW-Authenticate: Digest ,foo
     * }</pre>
     */
    public static challenges(response: Response): ArrayList<Challenge> {
        let responseField: string;
        if (response.responseCode == 401) {
            responseField = "www-authenticate";
        } else if (response.responseCode == 407) {
            responseField = "proxy-authenticate";
        } else {
            return new ArrayList();
        }
        return HttpHeaders.parseChallenges(HttpHeaders.getResponsHeaders(response), responseField);
    }

    public static parseChallenges(responseHeaders: Headers, headerName: string): ArrayList<Challenge> {
        let result: ArrayList<Challenge> = new ArrayList<Challenge>();
        for (let h = 0; h < responseHeaders.size(); h++) {
            if (headerName.toLowerCase() == responseHeaders.name(h).toLowerCase()) {
                let newAuthParams: Map<string, string> = new Map<string, string>();
                let itemValue: string = responseHeaders.value(h)
                if (itemValue.indexOf('=') != -1) {
                    let equalToValveArr: string[] = itemValue.split('=')
                    if (equalToValveArr[0].indexOf(' ') != -1) {
                        let spaceValveArr: string[] = equalToValveArr[0].split(' ')
                        newAuthParams.set(spaceValveArr[1].toLowerCase(), equalToValveArr[1])
                        let challenge: Challenge = new Challenge(spaceValveArr[0], newAuthParams);
                        result.add(challenge)
                    } else {
                        newAuthParams.set(equalToValveArr[0].toLowerCase(), equalToValveArr[1])
                        let challenge: Challenge = new Challenge(equalToValveArr[0], newAuthParams);
                        result.add(challenge)
                    }
                } else {
                    let challenge: Challenge = new Challenge(itemValue, newAuthParams);
                    result.add(challenge)
                }
            }
        }
        return result;
    }

    /** Returns true if the response must have a (possibly 0-length) body. See RFC 7231. */
    public static hasBody(response: Response): boolean {
        // HEAD requests never yield a body regardless of the response headers.
        //    if (response.request().method().equals("HEAD")) {
        //      return false;
        //    }

        let responseCode: number = response.responseCode;
        if ((responseCode < 100 || responseCode >= 200)
        && responseCode != 204
        && responseCode != 304) {
            return true;
        }

        // If the Content-Length or Transfer-Encoding headers disagree with the response code, the
        // response is malformed. For best compatibility, we honor the headers.
        if (HttpHeaders.contentLength(response) != -1
        || "chunked" == response.header["Transfer-Encoding"]) {
            return true;
        }

        return false;
    }

    //  public static receiveHeaders(CookieJar cookieJar, HttpUrl url, Headers headers) {
    //    if (cookieJar == CookieJar.NO_COOKIES) return;
    //
    //    List<Cookie> cookies = Cookie.parseAll(url, headers);
    //    if (cookies.isEmpty()) return;
    //
    //    cookieJar.saveFromResponse(url, cookies);
    //  }

    /**
     * Returns the next index in {@code input} at or after {@code pos} that contains a character from
     * {@code characters}. Returns the input length if none of the requested characters can be found.
     */
    public static skipUntil(input: string, pos: number, characters: string): number {
        for (; pos < input.length; pos++) {
            if (characters.indexOf(input.charAt(pos)) != -1) {
                break;
            }
        }
        return pos;
    }

    /**
     * Returns the next non-whitespace character in {@code input} that is white space. Result is
     * undefined if input contains newline characters.
     */
    public static skipWhitespace(input: string, pos: number): number {
        for (; pos < input.length; pos++) {
            let c: string = input.charAt(pos);
            if (c != ' ' && c != '\t') {
                break;
            }
        }
        return pos;
    }

    /**
     * Returns {@code value} as a positive integer, or 0 if it is negative, or {@code defaultValue} if
     * it cannot be parsed.
     */
    public static parseSeconds(value: string, defaultValue: number): number {
        try {
            let seconds: number = Number(value);
            if (seconds > Number.MAX_VALUE) {
                return Number.MAX_VALUE;
            } else if (seconds < 0) {
                return 0;
            } else {
                return seconds;
            }
        } catch (err) {
            return defaultValue;
        }
    }

    private static repeat(c: string, count: number): string {
        let resultStr: string = ''
        for (let i = 0;i < count; i++) {
            resultStr += c
        }
        return resultStr;
    }
}
