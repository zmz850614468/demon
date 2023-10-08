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
import StatusLine from './StatusLine';
import MediaType from './MediaType';
import ResponseBody from './ResponseBody';
import { HttpMethod } from '../http/HttpMethod';
import CacheStrategy from './CacheStrategy';
import InternalCache from './InternalCache';
import Request from '../Request';
import HttpDiskLruCache from './DiskCache/HttpDiskLruCache'
import { Response } from '../response/Response'
import { CryptoJS } from '@ohos/crypto-js'
import StringUtil from '../utils/StringUtil'

let VERSION = 201105
let ENTRY_BODY = 1
let ENTRY_COUNT = 2
let _cache: HttpDiskLruCache = null
let writeSuccessCount: number;
let writeAbortCount: number;
let networkCount: number;
let hitCount: number;
let requestCount: number;


namespace Cache {
    export class Cache implements InternalCache {
        constructor(filePath: string, maxSize: number) {
            _cache = HttpDiskLruCache.create(filePath, VERSION, ENTRY_COUNT, maxSize)
        }
        //返回一个md5的编码名字
        public key(url: string): string {
            return CryptoJS.MD5(url)
        }

        public get(request: Request): Response {

            let key = request.url.url
            let _snapshot: any = null

            try {
                // 通过DiskLruCache对象获取一个该key对象的缓存快照
                _snapshot = _cache.get(key);
                if ((_snapshot.getBody as string).length <= 0) {
                    return null;
                }
            } catch (e) {
                return null;
            }

            let response: Response = null
            if ((_snapshot.getBody as string).length > 0) {
                response = Entry.response(_snapshot)
            }

            return response
        }

        public put(response: Response) {
            try {
                let requestMethod = response.getRequest().method
                if (HttpMethod.invalidatesCache(requestMethod)) {
                    try {
                        // 如果是这些请求方法就移除缓存
                        this.remove(response.getRequest().url.url)
                    } catch (e) {
                        // The cache cannot be written.
                        return null;
                    }
                    return null;
                }
                // 如果请求方法不是get请求，那就直接返回null
                if (requestMethod != 'GET') {
                    // 不做非get请求的缓存，虽然其它方法的响应可以缓存，但是做起来成本太大且效率低下，所以放弃
                    return null;
                }
                let entry = new Entry(response);
                try {
                    entry.writeTo()
                } catch (e) {
                    return null;
                }

            } catch (e) {
                return null
            }

        }

        remove(request: Request) {
            _cache.remove(request.url.url)
        }

        abortQuietly(editor) {
            try {
                if (editor != null) {
                    editor.prototype.abort();
                }
            } catch (ignored) {
            }
        }

        public delete(): void {
            _cache.delete();
        }

        public evictAll() {
            _cache.evictAll();
        }

        public writeAbortCount(): number {
            return writeAbortCount
        }

        update(cache: Response, network: Response) {
            _cache.update(cache, network)
        }

        public writeSuccessCount(): number {
            return writeSuccessCount;
        }

        public size() {
            return _cache.size();
        }

        public maxSize(): number {
            return _cache.getMaxSize();
        }

        public flush() {
            _cache.flush();
        }

        public close() {
            _cache.close();
        }

        public directory(): string {
            return _cache.getDirectory();
        }

        public isClosed(): boolean {
            return _cache.isClosed();
        }

        trackResponse(cacheStrategy: CacheStrategy.CacheStrategy) {
            requestCount++;
            if (cacheStrategy.getNetWorkRequest() != null) {
                networkCount++;
            } else if (cacheStrategy.getCacheResponse() != null) {
                hitCount++;
            }
        }

        trackConditionalCacheHit() {
            hitCount++;
        }

        networkCount(): number {
            return networkCount;
        }

        hitCount(): number {
            return hitCount;
        }

        requestCount(): number {
            return requestCount;
        }
    }

    export class Entry {
        /** Synthetic response header: the local time when the request was sent. */
        private static SENT_MILLIS = "HttpClient" + "-Sent-Millis";
        private static RECEIVED_MILLIS = "HttpClient" + "-Received-Millis";
        private url: string
        private varyHeaders: object
        private requestMethod: string;
        private protocol: string;
        private code: number;
        private message: string;
        private responseHeaders: object;
        private sentRequestMillis: number;
        private receivedResponseMillis: number;
        private body: any
        private request: Request
        private noCache: boolean

        constructor(response: Response) {
            this.url = response.getRequest().url.url
            this.request = response.getRequest()
            this.varyHeaders = JSON.parse(response.getHeader())
            this.requestMethod = response.getRequest().method;
            this.protocol = response.protocol;
            this.code = response.getCode();
            this.message = response.message;
            this.sentRequestMillis = response.sentRequestAtMillis
            this.receivedResponseMillis = response.receivedResponseAtMillis
            this.body = response.getBody()
            this.noCache = response.getCacheControl().noCache
        }

        public static response(snapshot): Response {
            if (snapshot.getHeader == null) {
                return null
            }
            let header = snapshot.getHeader
            let body = snapshot.getBody
            let headerArray = header.split("\n")
            let contentType = ""
            let contentLength = ""
            let requestMillis = ""
            let responseMillis = ""
            let newProtocol = ""
            let newCode = -1
            let newHeader = ''
            let requestHeaders = {}

            for (var index = 0; index < headerArray.length; index++) {
                const element = headerArray[index];

                if (element.indexOf("contentType") != -1) {
                    contentType = element.split(":")[1]
                }
                if (element.indexOf("content-length") != -1) {
                    contentLength = element.split(":")[1]
                }
                if (element.indexOf("HttpClient-Sent-Millis") != -1) {
                    requestMillis = element.split(":")[1]
                }
                if (element.indexOf("HttpClient-Received-Millis") != -1) {
                    responseMillis = element.split(":")[1]
                }
                let tag = element.indexOf(":")
                if (index > 2 && tag != -1) {
                    let key = element.substring(0, tag - 1)
                    let value = element.substring(tag + 1, element.length).trim()
                    requestHeaders[key] = value
                }
                if (element.indexOf("HTTP") != -1 || element.indexOf("SPDY_3") != -1 || element.indexOf("H2_PRIOR_KNOWLEDGE") != -1 || element.indexOf("H2_PRIOR_KNOWLEDGE") != -1 || element.indexOf("QUIC") != -1) {
                    newProtocol = element.split(" ")[0]
                    newCode = Number(element.split(" ")[1])
                }
                if ((Number(headerArray[2]) + 3 + 2) <= index) {
                    if (!StringUtil.isEmpty(element)) {
                        newHeader = newHeader + element + "\n"
                    }
                }
            }

            let cacheRequest = new Request.Builder()
                .url(headerArray[0])
                .method(headerArray[1])
                .headers(requestHeaders)
                .build()

            let returnResponse = new Response(null).newBuilder()
                .setRequest(cacheRequest)
                .setProtocol(newProtocol)
                .setCode(newCode)
                .setHeader(newHeader)
                .setBody(body)
                .setSentRequestAtMillis(Number(requestMillis))
                .setReceivedResponseAtMillis(Number(responseMillis))
                .build();
            returnResponse.cacheControl = CacheControl.FORCE_CACHE()

            return returnResponse
        }

        //将Response 中的信息写入到map
        public writeTo(): void {
            let sinkMap = new Map<string, string>()
            for (let key in this.varyHeaders) {
                sinkMap.set(key, (this.varyHeaders as any)[key])
            }

            let sink = this.url + "\n"
            + this.requestMethod + "\n"

            let count = 0
            if (this.request.headers != undefined) {
                let text = ''
                for (let key in this.request.headers) {
                    count++
                    text = key + ": " + (this.request.headers as any)[key] + '\n'
                }
                sink = sink + count + "\n"
                sink = sink + text
                count = 0
            } else {
                sink = sink + count + "\n"
            }

            sink = sink + new StatusLine(this.protocol, this.code, this.message).toString() + "\n"

            sink = sink + (sinkMap.size + 2) + "\n"

            sinkMap.forEach((value, key) => {
                sink = sink + key + ": " + value + "\n"
            })
            sink = sink + Entry.SENT_MILLIS + ": " + this.sentRequestMillis + "\n"
            sink = sink + Entry.RECEIVED_MILLIS + ": " + this.receivedResponseMillis + "\n"

            _cache.writerFile(this.url, sink, this.body)
        }

        public matches(request: Request, response: Response): boolean {
            return this.url == request.url
            && this.requestMethod == request.method()
        }
    }

    export class CacheResponseBody extends ResponseBody {
        snapshot: any
        private _contentType: string = null
        private _contentLength: string = null
        private bodySource: any = null

        constructor(snapshot: any, contentType: string, contentLength: string) {
            super()
            this.snapshot = snapshot;
            this._contentType = contentType;
            this._contentLength = contentLength;
            let value = snapshot.getSource(ENTRY_BODY)

            this.bodySource = value
        }

        contentType(): MediaType {
            return this.contentType != null ? MediaType.parse(this._contentType) : null
        }

        contentLength(): number {
            try {
                return this._contentLength == null ? this._contentLength.length : -1
            } catch (e) {
                return -1
            }
        }

        source() {
            return this.bodySource;
        }
    }
}

export default Cache