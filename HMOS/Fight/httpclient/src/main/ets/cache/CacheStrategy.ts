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

import CacheControl from './CacheControl';
import Request from '../Request'
import { Response } from '../response/Response'
import StringUtil from '../utils/StringUtil'
import { HttpStatusCodes } from '../code/HttpStatusCodes';


namespace CacheStrategy {

    export class CacheStrategy {
        _networkRequest: Request
        _cacheResponse: Response

        constructor(networkRequest: Request, cacheResponse: Response) {
            this._networkRequest = networkRequest
            this._cacheResponse = cacheResponse
        }

        static isCacheable(response: Response, request: Request): boolean {
            let responseCode = -1
            responseCode = response.responseCode
            switch (responseCode) {
                case HttpStatusCodes.HTTP_OK: //HTTP_OK
                case HttpStatusCodes.HTTP_NOT_AUTHORITATIVE: //HTTP_NOT_AUTHORITATIVE
                case HttpStatusCodes.HTTP_NO_CONTENT: //HTTP_NO_CONTENT
                case HttpStatusCodes.HTTP_MULT_CHOICE: //HTTP_MULT_CHOICE
                case HttpStatusCodes.HTTP_MOVED_PERM: //HTTP_MOVED_PERM
                case HttpStatusCodes.HTTP_NOT_FOUND: //HTTP_NOT_FOUND
                case HttpStatusCodes.HTTP_BAD_METHOD: //HTTP_BAD_METHOD
                case HttpStatusCodes.HTTP_GONE: //HTTP_GONE
                case HttpStatusCodes.HTTP_REQ_TOO_LONG: //HTTP_REQ_TOO_LONG
                case HttpStatusCodes.HTTP_NOT_IMPLEMENTED: //HTTP_NOT_IMPLEMENTED
                case HttpStatusCodes.HTTP_PERM_REDIRECT: //HTTP_PERM_REDIRECT
                // These codes can be cached unless headers forbid it.
                    break

                case HttpStatusCodes.HTTP_MOVED_TEMP:
                case HttpStatusCodes.HTTP_TEMP_REDIRECT:
                    if (response.getHeadersEntry("Expires") == null &&
                    (response.getCacheControl())._maxAgeSeconds == -1 &&
                    !(response.getCacheControl()).isPublic() &&
                    !(response.getCacheControl()).isPrivate()) {
                        return false
                    }
                    break
                default:
                    return false
                    break;
            }

            return!response.getCacheControl()._noStore && !request.cacheControl._noStore
        }

        getNetWorkRequest() {
            return this._networkRequest
        }

        getCacheResponse() {
            return this._cacheResponse
        }
    }

    export class Factory {
        nowMillis: number
        request: Request
        cacheResponse: Response
        requestHeader: string
        /** The server's time when the cached response was served, if known. */
        private servedDate?: number = -1
        private servedDateString?: string = null
        /** The last modified date of the cached response, if known. */
        private lastModified?: number = -1
        private lastModifiedString?: string = null
        /**
         * The expiration date of the cached response, if known. If both this field and the max age are
         * set, the max age is preferred.
         */
        private expires?: number = null
        /**
         * Extension header set by HttpClient specifying the timestamp when the cached HTTP request was
         * first initiated.
         */
        private sentRequestMillis = 0
        /**
         * Extension header set by HttpClient specifying the timestamp when the cached HTTP response was
         * first received.
         */
        private receivedResponseMillis = 0
        /** Etag of the cached response. */
        private etag?: string = null
        /** Age of the cached response. */
        private ageSeconds = -1

        constructor(nowMillis: number, _request: Request, cacheResponse?: Response) {
            this.nowMillis = nowMillis
            this.request = _request
            this.cacheResponse = cacheResponse //缓存中有对应的数据 但是打印出来就是空的 但是转成string 都是存在

            if (cacheResponse != null) {

                this.requestHeader = this.request.headers
                this.sentRequestMillis = cacheResponse.sentRequestAtMillis
                this.receivedResponseMillis = cacheResponse.receivedResponseAtMillis

                let cacheHeaderArray = (cacheResponse.getHeader()).split("\n")
                // 遍历header，保存Date、Expires、Last-Modified、ETag、Age等缓存机制相关字段的值
                cacheHeaderArray.forEach((entry) => {
                    if (!StringUtil.isEmpty(entry)) {
                        let fieldName = entry.split(":")[0]
                        let value = entry.split(":")[1]
                        if ("Date".toLocaleLowerCase() == fieldName.toLocaleLowerCase()) { //时间
                            //提取时间date --->
                            let indexNumber = entry.indexOf(":")
                            let timeValue = entry.substring(indexNumber + 1, entry.length)
                            this.servedDate = Date.parse(timeValue); //Sun, 13 Nov 2022 03:45:08 GMT //1668311108000  1668311108000
                            this.servedDateString = timeValue;
                        } else if ("Expires".toLocaleLowerCase() == fieldName.toLocaleLowerCase()) { //缓存到期日期
                            let indexNumber = entry.indexOf(":")
                            let timeValue = entry.substring(indexNumber + 1, entry.length)
                            this.expires = Date.parse(timeValue);
                        } else if ("Last-Modified".toLocaleLowerCase() == fieldName.toLocaleLowerCase()) { //是由服务器发送给客户端的HTTP请求头标签
                            let indexNumber = entry.indexOf(":")
                            let timeValue = entry.substring(indexNumber + 1, entry.length)
                            this.lastModified = Date.parse(timeValue);
                            this.lastModifiedString = timeValue;
                        } else if ("ETag".toLocaleLowerCase() == fieldName) {
                            this.etag = value;
                        } else if ("Age".toLocaleLowerCase() == fieldName) {
                            this.ageSeconds = Number(value)
                        }
                    }
                })
            }
        }

        /** Returns a strategy to satisfy [request] using [cacheResponse]. */
        compute(): CacheStrategy {
            let candidate = this.getCandidate()
            // We're forbidden from using the network and the cache is insufficient.
            if (candidate.getNetWorkRequest() != null && this.request.cacheControl._onlyIfCached && candidate.getCacheResponse() != null) {
                return new CacheStrategy(null, candidate.getCacheResponse())
            }
            return candidate
        }

        /**
         * Returns true if computeFreshnessLifetime used a heuristic. If we used a heuristic to serve a
         * cached response older than 24 hours, we are required to attach a warning.
         */
        private isFreshnessLifetimeHeuristic(): boolean {
            return (this.cacheResponse.cacheControl as undefined as CacheControl).maxAgeSeconds as undefined as number == -1 && this.expires == null
        }

        //检查缓存候选响应，如果需要的话讲修改原始请求的报头
        private getCandidate(): CacheStrategy {
            // 如果没有缓存 那就将response置null，直接进行网络请求
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null)
            }

            //通过响应码以及头部缓存控制字段判断响应能不能缓存
            // 如果不能缓存那就进行网络请求
            if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, null)
            }
            // 取出请求头缓存控制对象
            let requestCaching = this.request.cacheControl
            // noCache表明要忽略本地缓存
            // If-Modified-Since/If-None-Match说明缓存过期，需要服务端验证
            // 这两种情况就需要进行网络请求
            if (requestCaching.noCache || this.hasConditions(this.request)) {
                if (requestCaching.noCache()) {
                    return new CacheStrategy(this.request, null)
                }
            }
            let responseCaching = this.cacheResponse.getCacheControl()
            // 该响应已缓存的时长
            let ageMillis = this.cacheResponseAge()
            // 该响应可以缓存的时长
            let freshMillis = this.computeFreshnessLifetime()
            if (requestCaching != null && requestCaching.maxAgeSeconds() != -1) {
                // 取出两者最小值 ，从computeFreshnessLifetime方法可以知道就是拿Request和Response的CacheControl头中max-age值作比较
                freshMillis = Math.min(freshMillis, requestCaching.maxAgeSeconds() * 1000);
            }
            let minFreshMillis: number = 0
            //取出响应头缓存控制字段
            if (requestCaching._minFreshSeconds != -1) {
                // 这里是取出min_fresh值，即缓存过期后还能继续使用的时长
                minFreshMillis = requestCaching._minFreshSeconds * 1000
            }

            let maxStaleMillis: number = 0
            //取出响应头缓存控制字段
            if (!responseCaching._mustRevalidate && requestCaching._maxStaleSeconds != -1) {
                maxStaleMillis = requestCaching._maxStaleSeconds * 1000
            }

            // 如果响应头没有要求忽略本地缓存
            // 已缓存时长+最小新鲜度时长 < 最大新鲜度时长 + 过期后继续使用时长
            // 通过不等式转换：最大新鲜度时长减去最小新鲜度时长就是缓存的有效期，再加上过期后继续使用时长，那就是缓存极限有效时长
            //如果已缓存的时长小于极限时长，说明还没到极限，继续使用
            if (!responseCaching.noCache && ageMillis + minFreshMillis < this.cacheResponse.cacheControl._maxStaleSeconds + maxStaleMillis) {
                let builder = this.cacheResponse.newBuilder()
                if (ageMillis + minFreshMillis >= freshMillis) {
                    builder.setHeader("Warning", "110 HttpURLConnection \"Response is stale\"")
                }
                //如果缓存已超过一天并且响应中没有设置过期时间也需要添加警告
                let oneDayMillis = 24 * 60 * 60 * 1000
                if (ageMillis > oneDayMillis && this.isFreshnessLifetimeHeuristic()) {
                    builder.setHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"")
                }
                //缓存继续使用，不进行网络请求
                return new CacheStrategy(null, builder.build())
            }

            if (!responseCaching.noCache && this.cacheResponse.cacheControl._maxStaleSeconds > ageMillis) {
                return new CacheStrategy(null, this.cacheResponse)
            }

            //复制一份和当前请求一样的头部
            let conditionalRequestHeaders = this.requestHeader
            let url = this.request.url
            let conditionalRequest = this.request.newBuilder()
                .headers(conditionalRequestHeaders)
                .url(url.url)
                .build();

            return new CacheStrategy(conditionalRequest, this.cacheResponse)
        }

        /**
         * Returns the number of milliseconds that the response was fresh for, starting from the served
         * date.
         * 该响应可以缓存的时长
         */
        private computeFreshnessLifetime(): number {
            let responseCaching = this.cacheResponse.getCacheControl()
            if (responseCaching == null) {
                return -1;
            } else if (responseCaching.maxAgeSeconds() != -1) {
                return responseCaching._maxAgeSeconds * 1000
            } else if (this.expires != null) { //如果设置了过期时间
                let servedMillis = this.servedDate != null ? Number(this.servedDate) : this.receivedResponseMillis
                // 计算过期时间与产生时间的差就是可以缓存的时长
                let delta = Number(this.expires) - servedMillis
                return delta > 0 ? delta : 0
            } else if (this.lastModified != null && this.cacheResponse.getRequest().url == null) {
                // As recommended by the HTTP RFC and implemented in Firefox, the
                // max age of a document should be defaulted to 10% of the
                // document's age at the time it was served. Default expiration
                // dates aren't used for URIs containing a query.
                let servedMillis = this.servedDate != null
                    ? Number(this.servedDate)
                    : this.sentRequestMillis
                let delta = servedMillis - Number(this.lastModified)
                return delta > 0 ? (delta / 10) : 0;
            }
            //缓存不能继续使用了，需要进行网络请求
            return 0;
        }

        /**
         * Returns the current age of the response, in milliseconds. The calculation is specified by RFC
         * 7234, 4.2.3 Calculating Age.
         */
        private cacheResponseAge(): number {
            try {
                let apparentReceivedAge = this.servedDate != -1
                    ? Math.max(0, this.receivedResponseMillis - this.servedDate)
                    : 0;
                let receivedAge = this.ageSeconds != -1
                    ? Math.max(apparentReceivedAge, this.ageSeconds * 1000)
                    : apparentReceivedAge;
                let responseDuration = this.receivedResponseMillis - this.sentRequestMillis;
                let residentDuration = this.nowMillis - this.receivedResponseMillis;
                return receivedAge + responseDuration + residentDuration;
            } catch (e) {
            }

        }

        /**
         * Returns true if the request contains conditions that save the server from sending a response
         * that the client has locally. When a request is enqueued with its own conditions, the built-in
         * response cache won't be used.
         */
        private hasConditions(request: Request): boolean {
            let requestHeader = JSON.stringify(request.headers)

            return requestHeader.indexOf("If-Modified-Since") != -1 || requestHeader.indexOf("If-None-Match") != -1
        }
    }
}


export default CacheStrategy