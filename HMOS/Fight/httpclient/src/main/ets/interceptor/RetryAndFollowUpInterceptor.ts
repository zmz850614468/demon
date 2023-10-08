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

import HttpClient from '../HttpClient';
import { Chain, Interceptor } from '../Interceptor';
import { Response } from '../response/Response';
import { RealInterceptorChain } from './RealInterceptorChain';
import { Logger } from '../utils/Logger';
import Request from '../Request';
import { Type } from '../connection/Proxy';
import { Exchange } from '../connection/Exchange';
import { HttpMethod } from '../http/HttpMethod';
import { Utils } from '../utils/Utils';
import HttpCall from '../HttpCall';
import ConstantManager from '../ConstantManager'
import { HttpStatusCodes } from '../code/HttpStatusCodes';
import {
    CURLE_HTTP2,
    CURLE_HTTP2_STREAM,
    CURLE_OPERATION_TIMEDOUT,
    CURLE_PEER_FAILED_VERIFICATION,
    CURLE_REMOTE_FILE_NOT_FOUND,
    CURLE_SSL_CERTPROBLEM,
    CURLE_UNSUPPORTED_PROTOCOL
} from '../code/HttpErrorCodes';
import hilog from '@ohos.hilog';

export class RetryAndFollowUpInterceptor implements Interceptor {
    private client: HttpClient
    private MAX_FOLLOW_UPS = 20

    constructor(client: HttpClient) {
        this.client = client
    }

    async intercept(chain: Chain): Promise<Response> {
        let realChain = chain as RealInterceptorChain
        let request = realChain.requestI() //获取本次请求的 Request 对象
        this.MAX_FOLLOW_UPS = request.retryMaxLimit
        const call = realChain.callI()
        let followUpCount = 0 //重定向次数
        let priorResponse: Response = null
        let error = null;

        while (true) {
            Logger.info("RetryAndFollowUpInterceptor intercept while =" + call.isCancelled())
            let responsePromise: Promise<Response>
            let response: Response
            if (call.isCancelled()) { //首先进行安全检查，判断请求是否被取消
                let error = { code: '', data: 'Request canceled by user' };
                return Promise.reject(error)
            }
            //通过创建好的request对象，去网络获取到response并传递给下一级拦截器，执行下一个拦截器
            responsePromise = realChain.proceedI(request) //如果抛出异常就说明可能需要重试

            await responsePromise.then((data) => {
                response = data;
                error = null;
                Logger.info('RetryAndFollowUpInterceptor responseCode = ' + data.responseCode)
            }).catch((err) => {
                hilog.info(0x0001, "RetryAndFollowUpInterceptor err =", JSON.stringify(err));
                error = err;
                response = new Response().newBuilder()
                    .setRequest(request)
                    .setBody(err.message)
                    .setCode(err.code)
                    .build();
                // An attempt to communicate with a server failed. The request may have been sent.
                let requestSendStarted = !(err.code == CURLE_HTTP2 || err.code == CURLE_HTTP2_STREAM)
                if (!this.recover(err, call, request, requestSendStarted)) {
                    // 如果没有必要重试，拦截器会直接抛出异常
                    return Promise.reject(err)
                }
            })

            //不属于以上情况则构建新的request和使用响应结果构建新的response对象供下次使用
            response = response.newBuilder()
                .setRequest(request)
                .setPriorResponse(priorResponse != null ? priorResponse.newBuilder().setBody(null).build() : null)
                .build();
            let exchange = call.interceptorScopedExchange
            let followUp = this.followUpRequest(request, response, exchange)
            Logger.info('RetryAndFollowUpInterceptor followUp = ' + followUp)
            if (!followUp) { //如果构建的request为空，不再进行请求，直接返回response
                if (error) {
                    return Promise.reject(error)
                }
                return response
            }
            //如果当前重试次数达到最大次数(默认20),直接抛异常，不再重试
            if (++followUpCount > this.MAX_FOLLOW_UPS) {
                let error = { code: -1, data: 'Too many follow-up requests: ' + followUpCount };
                return Promise.reject(error)
            }
            request = followUp // 修改下次重定向的request
            if (request.debugCode != null) {
                response.responseCode = request.debugCode
            }
            priorResponse = response // 记录上一次的response
        }
    }

    /**
     * Report and attempt to recover from a failure to communicate with a server. Returns true if
     * `e` is recoverable, or false if the failure is permanent. Requests with a body can only
     * be recovered if the body is buffered or if the failure occurred before the request has been
     * sent.
     */
    private recover(e: any, call: HttpCall, userRequest: Request, requestSendStarted: boolean): boolean {
        // The application layer has forbidden retries. 如果用户不允许重试则放弃重试
        if (!userRequest.retryOnConnectionFailure) return false

        // We can't send the request body again. 同一个请求（request）不能被调用两次，如果请求是一次性的并且已经发送过了（requestSendStarted==true），那就不允许重试了
        if (requestSendStarted && this.requestIsOneShot(e, userRequest)) return false

        // This exception is fatal. 出现了严重的问题，判断请求是否可重试
        if (!this.isRecoverable(e, requestSendStarted)) return false

        // No more routes to attempt. 没有可供重试的路由那也不能重试
        if (!call.retryAfterFailure(userRequest)) return false

        // For failure recovery, use the same route selector with a new connection.
        return true
    }

    private isRecoverable(e: any, requestSendStarted: Boolean): Boolean {
        // If there was a protocol problem, don't recover. 如果是协议错误，那不能重试
        if (e.code == CURLE_UNSUPPORTED_PROTOCOL) {
            return false
        }

        // If there was an interruption don't recover, but if there was a timeout connecting to a route
        // we should try the next route (if there is one). 如果不是超时异常那也不能重试
        if (e.code == CURLE_OPERATION_TIMEDOUT) {
            return !requestSendStarted
        }

        // Look for known client-side or negotiation errors that are unlikely to be fixed by trying again with a different route. ssl握手异常不能重试
        if (e.code == CURLE_SSL_CERTPROBLEM) {
            // If the problem was a CertificateException from the X509TrustManager, do not retry
            return false
        }

        if (e.code == CURLE_PEER_FAILED_VERIFICATION) { //ssl握手未授权异常，不能重试
            // e.g. a certificate pinning error.
            return false
        }
        // An example of one we might want to retry with a different route is a problem connecting to a
        // proxy and would manifest as a standard IOException. Unless it is one we know we should not
        // retry, we return true and try a new route.
        return true
    }

    private requestIsOneShot(e: any, userRequest: Request): Boolean {
        let requestBody = userRequest.body
        return (requestBody != null && requestBody.isOneShot()) || e.code == CURLE_REMOTE_FILE_NOT_FOUND
    }

    //大部分是switch/case的各种返回码处理。followUpRequest方法从宏观上来讲，是输入response，生成新的requests。
    //如果response的内容不需要重试，则直接返回null。如果需要重试，则根据response的内容，生成重试策略，返回重试发出的reques
    private followUpRequest(request: Request, userResponse: Response, exchange?: Exchange): Request {
        Logger.info('RetryAndFollowUpInterceptor followUpRequest')
        let route = exchange?.connection()?.getRoute()
        let responseCode = userResponse.responseCode //服务器返回的状态码
        if (request.debugCode != null) {
            responseCode = request.debugCode
        }
        let method = request.method
        switch (responseCode) {
            case HttpStatusCodes.HTTP_PERM_REDIRECT:
            case HttpStatusCodes.HTTP_TEMP_REDIRECT:
            case HttpStatusCodes.HTTP_MULT_CHOICE:
            case HttpStatusCodes.HTTP_MOVED_PERM:
            case HttpStatusCodes.HTTP_MOVED_TEMP:
            case HttpStatusCodes.HTTP_SEE_OTHER:
                return this.buildRedirectRequest(request, userResponse, method)

            case HttpStatusCodes.HTTP_UNAUTHORIZED: //401
                if (!!!this.client.authenticator) {
                    Logger.info('RetryAndFollowUpInterceptor followUpRequest 401,need authenticator')
                    return request
                }
                let requestR: Request = this.client.authenticator.authenticate(request, userResponse)
                Logger.info('authenticate requestR:url' + JSON.stringify(requestR))
                return requestR

            case HttpStatusCodes.HTTP_PROXY_AUTH: //407
                Logger.info('RetryAndFollowUpInterceptor 407 route = ' + route)
                if (route) {
                    let selectedProxy = route.getProxy()
                    if (selectedProxy.getType() != Type.HTTP) {
                        throw Error("Received HTTP_PROXY_AUTH (407) code while not using proxy")
                    }
                }
                return this.client.proxyAuthenticator.authenticate(request, userResponse)

            case HttpStatusCodes.HTTP_CLIENT_TIMEOUT: //408
                Logger.info('RetryAndFollowUpInterceptor request.retryOnConnectionFailure = ' + request.retryOnConnectionFailure)
            // 408's are rare in practice, but some servers like HAProxy use this response code. The
            // spec says that we may repeat the request without modifications. Modern browsers also
            // repeat the request (even non-idempotent ones.)
                if (!request.retryOnConnectionFailure) { //应用层指示不要重试请求
                    // The application layer has directed us not to retry the request.
                    return null
                }
                Logger.info('RetryAndFollowUpInterceptor request.body=' + request.body)
                let requestBody = request.body
                if (requestBody != null && requestBody.isOneShot()) { //只允许请求一次不允许重试请求
                    return null
                }
                Logger.info('RetryAndFollowUpInterceptor priorResponse = ' + userResponse.priorResponse)
                let priorResponse = userResponse.priorResponse
                if (priorResponse != null) {
                    Logger.info('RetryAndFollowUpInterceptor priorResponse.responseCode = ' + priorResponse.responseCode)
                }
                if (priorResponse != null && priorResponse.responseCode == HttpStatusCodes.HTTP_CLIENT_TIMEOUT) { //和上次请求结果一样为超时、即两次请求结果都为408时，放弃重试
                    // We attempted to retry and got another timeout. Give up.
                    return null
                }
                if (this.retryAfter(userResponse, 0) > 0) { //响应头里面的Retry-After大于0，放弃重试
                    return null
                }
                return request

            case HttpStatusCodes.HTTP_MISDIRECTED_REQUEST: // 421
            // httpclient can coalesce HTTP/2 connections even if the domain names are different. See
            // RealConnection.isEligible(). If we attempted this and the server returned HTTP 421, then
            // we can retry on a different connection.
                let requestBody421 = request.body
                if (requestBody421 != null && requestBody421.isOneShot()) { //只允许请求一次不允许重试请求
                    return null
                }
                if (exchange == null || !exchange.isCoalescedConnection()) { //请求不是合并连接不允许重试请求
                    return null
                }
            //                exchange.connection.noCoalescedConnections()
                return request

            case HttpStatusCodes.HTTP_UNAVAILABLE: //503
                let priorResponse503 = userResponse.priorResponse
                if (priorResponse503 != null && priorResponse503.responseCode == HttpStatusCodes.HTTP_UNAVAILABLE) { //和上次请求结果一样都为503时，放弃重试
                    // We attempted to retry and got another timeout. Give up.
                    return null
                }
                if (this.retryAfter(userResponse, Number.MAX_VALUE) == 0) { //响应头里面的Retry-After大于0，放弃重试
                    // specifically received an instruction to retry without delay
                    return request
                }
                return null

            default:
                Logger.info('RetryAndFollowUpInterceptor default ----------')
                return null
        }
    }

    private retryAfter(userResponse: Response, defaultDelay: number): number {
        Logger.info('RetryAndFollowUpInterceptor retryAfter userResponse.header = ' + userResponse.header)
        let header: string
        if (!!JSON.parse(userResponse.header)["retry-after"]) {
            header = JSON.parse(userResponse.header)["retry-after"]
        }
        Logger.info('RetryAndFollowUpInterceptor retryAfter header = ' + header)
        if (!!!header) { //header为空时返回默认值
            return defaultDelay;
        }
        Logger.info('RetryAndFollowUpInterceptor retryAfter header.matches() = ' + header.match("\\d+"))
        // currently ignores a HTTP-date, and assumes any non int 0 is a delay
        if (header.match("\\d+")) {
            return Number(header).valueOf()
        }
        Logger.info('RetryAndFollowUpInterceptor retryAfter Number.MAX_VALUE = ' + Number.MAX_VALUE)
        return Number.MAX_VALUE;
    }

    private buildRedirectRequest(request: Request, userResponse: Response, method: string): Request {
        Logger.info('RetryAndFollowUpInterceptor buildRedirectRequest request.followRedirects = ' + request.followRedirects)
        // Does the client allow redirects?
        if (!request.followRedirects) return null //用户不允许重定向

        //取出重定向的地址，如果是null则不管，如果不为null就直接通过HttpUrl url = userResponse.request().url().resolve(location);拼接成一个httpUrl
        let location
        if (!!JSON.parse(userResponse.header)["Location"]) {
            location = JSON.parse(userResponse.header)["Location"]
        }

        if (request.debugUrl != null) {
            location = request.debugUrl
        }

        if (!!!location) { //header为空时返回默认值
            return null;
        }

        let url = request.url.resolve(location)
        Logger.info('RetryAndFollowUpInterceptor resolve url = ' + url.getUrl())
        // Don't follow redirects to unsupported protocols.
        if (url == null) return null;

        Logger.info('RetryAndFollowUpInterceptor url.getScheme = ' + url.getScheme())

        // If configured, don't follow redirects between SSL and non-SSL.
        let sameScheme = url.getScheme() == request.url.getScheme(); //判断当前重定向是否是http和https之间的重定向
        if (!sameScheme && !request.followSslRedirects) return null; //如果确实是http和https之间的重定向，那就得看用户是否让这种重定向，如果不让那就返回null不用管
        Logger.info('RetryAndFollowUpInterceptor method = ' + method)
        // Most redirects don't include a request body.
        let requestBuilder = request
        Logger.info('RetryAndFollowUpInterceptor HttpMethod.permitsRequestBody() = ' + HttpMethod.permitsRequestBody(method))

        if (HttpMethod.permitsRequestBody(method)) {
            let responseCode = userResponse.responseCode
            let maintainBody = HttpMethod.redirectsWithBody(method) ||
            responseCode == HttpStatusCodes.HTTP_PERM_REDIRECT ||
            responseCode == HttpStatusCodes.HTTP_TEMP_REDIRECT
            if (HttpMethod.redirectsToGet(method) && responseCode != HttpStatusCodes.HTTP_PERM_REDIRECT && responseCode != HttpStatusCodes.HTTP_TEMP_REDIRECT) { //重定向的方法不是GET或者HEAD的就不用管
                requestBuilder.method = 'GET'
                requestBuilder.body = null
            } else {
                let requestBody = maintainBody ? request.body : null
                requestBuilder.method = method
                requestBuilder.body = requestBody
            }
            if (!maintainBody && !requestBuilder.headers) {
                if (requestBuilder.headers.has("Transfer-Encoding")) {
                    requestBuilder.removeHeader("Transfer-Encoding")
                }
                if (requestBuilder.headers.has("Content-Length")) {
                    requestBuilder.removeHeader("Content-Length")
                }
                if (requestBuilder.headers.has(ConstantManager.CONTENT_TYPE)) {
                    requestBuilder.removeHeader(ConstantManager.CONTENT_TYPE)
                }
            }
        }
        Logger.info('RetryAndFollowUpInterceptor Utils.canReuseConnectionFor() = ' + Utils.canReuseConnectionFor(request.url, url))
        // When redirecting across hosts, drop all authentication headers. This
        // is potentially annoying to the application layer since they have no
        // way to retain them.
        if (!Utils.canReuseConnectionFor(request.url, url)) {
            if (!requestBuilder.headers && requestBuilder.headers.has("Authorization")) {
                requestBuilder.removeHeader("Authorization") //跨主机重定向去除身份验证信息
            }
        }
        requestBuilder.setHttpUrl(url)
        return requestBuilder
    }
}
