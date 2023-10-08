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

import { Chain, Interceptor } from '../Interceptor';
import { Response } from '../response/Response';
import CacheStrategy from '../cache/CacheStrategy';
import CacheRequest from '../cache/CacheRequest';
import Internal from '../cache/Internal';
import InternalCache from '../cache/InternalCache';
import Protocol from '../protocols/Protocol';
import { HttpMethod } from '../http/HttpMethod';
import HttpHeaders from '../core/HttpHeaders';
import { HttpStatusCodes } from '../code/HttpStatusCodes';
import { Logger } from '../utils/Logger';
import ConstantManager from '../ConstantManager'

export class CacheInterceptor implements Interceptor {
    cache: InternalCache

    constructor(cache: InternalCache) {
        this.cache = cache
    }

    static isEndToEnd(fieldName: string): boolean {
        let fieldNameToLowerCase = fieldName.toLowerCase()
        return "Connection".toLowerCase() != fieldNameToLowerCase
        && "Keep-Alive".toLowerCase() != fieldNameToLowerCase
        && "Proxy-Authenticate".toLowerCase() != fieldNameToLowerCase
        && "Proxy-Authorization".toLowerCase() != fieldNameToLowerCase
        && "TE".toLowerCase() != fieldNameToLowerCase
        && "Trailers".toLowerCase() != fieldNameToLowerCase
        && "Transfer-Encoding".toLowerCase() != fieldNameToLowerCase
        && "Upgrade".toLowerCase() != fieldNameToLowerCase
    }

    static isContentSpecificHeader(fieldName: string): boolean {
        let fieldNameToLowerCase = fieldName.toLowerCase()
        return "Content-Length".toLowerCase() == fieldNameToLowerCase
        || "Content-Encoding".toLowerCase() == fieldNameToLowerCase
        || ConstantManager.CONTENT_TYPE.toLowerCase() == fieldNameToLowerCase
    }

    private static combine(cachedHeaders: string, networkHeaders: string): string {
        let result = ''
        if (cachedHeaders != null) {
            let cacheHeaderList = cachedHeaders.split("\n")
            for (var index = 0; index < cacheHeaderList.length; index++) {
                const header = cacheHeaderList[index]
                let extract = header.indexOf(":")
                let fieldName = header.substring(0, extract - 1);
                let value = header.substring(extract + 1, header.length);
                if ("Warning".toLowerCase() == fieldName.toLowerCase() && value.startsWith("1")) {
                    continue; // Drop 100-level freshness warnings.
                }
                if (CacheInterceptor.isContentSpecificHeader(fieldName)
                || !this.isEndToEnd(fieldName)
                || networkHeaders.indexOf(fieldName) == -1
                ) {
                    Internal.instance.addLenient(result, fieldName, value);
                }
            }
        }

        if (networkHeaders != null) {
            let networkHeaderList = networkHeaders.split("\n")
            for (var i = 0; i < networkHeaderList.length; i++) {
                const header = networkHeaderList[index]
                let extract = header.indexOf(":")
                let fieldName = header.substring(0, extract - 1);
                let value = header.substring(extract + 1, header.length);
                if (!this.isContentSpecificHeader(fieldName) && this.isEndToEnd(fieldName)) {
                    Internal.instance.addLenient(result, fieldName, value)
                }
            }
        }

        return result
    }

    async intercept(chain: Chain): Promise<Response> {
        let networkResponse: Promise<Response> = null;
        let request = chain.requestI()
        // 尝试获取缓存的cache
        let cacheCandidate = this.cache != null ? this.cache.get(request) : null
        let now = new Date().getTime()
        // 传入当前时间、request以及从缓存中取出的cache，构建缓存策略
        let strategy = new CacheStrategy.Factory(now, request, cacheCandidate).compute()
        let networkRequest = strategy.getNetWorkRequest();
        let cacheResponse = strategy.getCacheResponse();
        if (null != this.cache) {
            this.cache.trackResponse(strategy);
        }

        //如果根据缓存策略strategy禁止使用网络，并且缓存无效，直接返回空的Response
        if (networkRequest == null && cacheResponse == null) {
            return new Promise<Response>((resolve, reject) => {
                let response = new Response.Builder(null)
                    .setRequest(request)
                    .setProtocol(Protocol.HTTP_1_1)
                    .setCode(HttpStatusCodes.HTTP_GATEWAY_TIMEOUT)
                    .setMessage("Unsatisfiable Request (only-if-cached)")
                    .setBody(null)
                    .setSentRequestAtMillis(-1)
                    .setReceivedResponseAtMillis(new Date().getTime())
                    .build()

                resolve(response)
            })
        }

        //如果根据缓存策略strategy禁止使用网络，且有缓存则直接使用缓存
        if (networkRequest == null) {
            return Promise.resolve(new Response.Builder(null)
                .setCacheResponse(cacheResponse)
                .build())
        }
        //执行下一个拦截器，发起网路请求
        networkResponse = chain.proceedI(networkRequest);

        let response: Response;
        await networkResponse.then((data) => {
            response = data
        })
        //本地有缓存
        if (cacheResponse != null && null != this.cache) {
            //返回304状态码（说明缓存还没过期或服务器资源没修改）
            if (response.responseCode == HttpStatusCodes.HTTP_NOT_MODIFIED) {
                //使用缓存数据
                cacheResponse.newBuilder()
                    .setHeader(CacheInterceptor.combine(cacheResponse.getHeader(), response.getHeader()))
                    .setSentRequestAtMillis(response.sentRequestAtMillis)
                    .setReceivedResponseAtMillis(response.receivedResponseAtMillis)
                    .setCacheResponse(this.stripBody(response))
                    .setNetworkResponse(this.stripBody(response))
                    .build();

                this.cache.trackConditionalCacheHit()
                this.cache.update(cacheResponse, response);
                Promise.resolve(cacheResponse)
            }
        }

        //如果网络资源已经修改：使用网络响应返回的最新数据
        let responseNew = response.newBuilder().setCacheResponse(this.stripBody(cacheResponse)).setNetworkResponse(response).build()
        //用户设置了缓存、将最新的数据缓存起来(首次进入)
        if (this.cache != null) {
            if (HttpHeaders.hasBody(responseNew) && responseNew.header.indexOf("no-cache") == -1) {
                let cacheRequest = this.cache.put(responseNew);
                return Promise.resolve(this.cacheWritingResponse(cacheRequest, responseNew))
            }
            if (HttpMethod.invalidatesCache(networkRequest.method) && null != this.cache) {
                try {
                    this.cache.remove(networkRequest);
                } catch (ignored) {
                    Logger.error("remove networkRequest : " + ignored)
                }
            }
        }

        return Promise.resolve(response);
    }

    stripBody(response?: Response): Response {
        try {
            if (response != null && response.getBody() != null) {
                return response.newBuilder().setBody(null).build()
            } else {
                return response
            }
        } catch (e) {
            return response
        }
    }

    private cacheWritingResponse(cacheRequest: CacheRequest, response: Response): Response {
        if (cacheRequest == null) return response;
        let cacheBodyUnbuffered = cacheRequest.body();
        if (cacheBodyUnbuffered == null) return response;

        const source = response.getBody()

        return response.newBuilder().setBody(source).build()
    }
}
