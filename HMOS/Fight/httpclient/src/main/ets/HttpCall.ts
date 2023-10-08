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

import { Response } from './response/Response';
import { Interceptor } from './Interceptor';
import { RealInterceptorChain } from './interceptor/RealInterceptorChain';
import { RetryAndFollowUpInterceptor } from './interceptor/RetryAndFollowUpInterceptor';
import { BridgeInterceptor } from './interceptor/BridgeInterceptor';
import { CacheInterceptor } from './interceptor/CacheInterceptor';
import { ConnectInterceptor } from './interceptor/ConnectInterceptor';
import { CallServerInterceptor } from './interceptor/CallServerInterceptor';
import Request from './Request';
import { Logger } from './utils/Logger';
import { Exchange } from './connection/Exchange';
import { RealWebSocket } from './RealWebSocket';
import hilog from '@ohos.hilog';

class HttpCall {
    client = null;
    originalRequest = null;
    isCancelledRequest = false;
    onSuccessCallback = null;
    onFailureCallback = null;
    forWebSocket: boolean = false;
    exchange: Exchange = null
    interceptorScopedExchange: Exchange = null
    ws: RealWebSocket
    private callsPerHost = 0

    constructor(httpClient, originalRequest, forWebSocket: boolean) {
        this.client = httpClient;
        this.originalRequest = originalRequest;
        this.isCancelledRequest = false;
        this.onSuccessCallback = null;
        this.onFailureCallback = null;
        this.forWebSocket = forWebSocket;
    }

    getRequest() {
        return this.originalRequest;
    }

    getClient() {
        return this.client;
    }

    /**
     *
     * 根据响应结果，给需要解析的实体对象赋值（自定义请求对象）
     * @param responseCode -- 响应码
     * @param requiredBodyObj -- 需要得到的实体对象
     * @param responseObj -- response.result，响应结果的result值(要解析的对象)
     */
    getRequiredEntryObjValue(responseCode: number, requiredBodyObj: Object, responseObj: Object) {
        if (responseObj == null) {
            return null; // 整个响应结果为null
        } else {
            if (responseCode == 200) {
                for (let objProperties in requiredBodyObj) {
                    if (requiredBodyObj.hasOwnProperty(objProperties) && responseObj.hasOwnProperty(objProperties)) {
                        requiredBodyObj[objProperties] = responseObj[objProperties];
                    }
                }
                // 请求成功（200），但是没有相应字段则返回null，否则返回具体对象
                return JSON.stringify(requiredBodyObj) === "{}" ? {} : requiredBodyObj;
            } else { // responseCode !=200
                return {}; // 请求错误返回一个空对象
            }
        }
    }

    async executed(): Promise<any> {
        this.originalRequest.isSyncCall = true;
        try {
            let response = await this.getResponseWithInterceptorChain(); // response 结果
            return this.getRequiredEntryObjValue(
            response.responseCode,
            this.getRequest().requiredBodyObjects, //获得request实体对象
            JSON.parse(response.result));
        } finally {
            this.client.dispatcher.finished(this, this.originalRequest.isSyncCall)
        }
    }

    async execute(): Promise<Response> {
        this.originalRequest.isSyncCall = true;
        try {
            this.client.dispatcher.executed(this)
            return this.getResponseWithInterceptorChain()
        } finally {
            this.client.dispatcher.finished(this, this.originalRequest.isSyncCall)
        }
    }

    enqueue(onSuccess, onFailure) {
        this.onSuccessCallback = onSuccess;
        this.onFailureCallback = onFailure;
        this.originalRequest.isSyncCall = false;
        this.client.dispatcher.enqueue(this);
    }

    webSocket(ws: RealWebSocket) {
        this.ws = ws
    }

    reuseCallsPerHostFrom(other: HttpCall) {
        this.originalRequest.url.host = other.callsPerHost
    }

    getCallsPerHost(): number {
        return this.callsPerHost
    }

    increment() {
        this.callsPerHost++
    }

    decrement() {
        this.callsPerHost--
    }

    getHost(): string {
        return this.originalRequest.url.getHost()
    }

    executeOn() {
        if (this.isCancelled()) {
            let errMsg = { code: '', data: 'Request canceled by user' };
            this.onError(errMsg);
            return;
        }
        this.getResponseWithInterceptorChain()
            .then((data) => {
                if (this.isCancelled()) {
                    let errorMessage = { code: '', data: 'Request canceled by user' };
                    this.onError(errorMessage);
                } else {
                    Logger.info('HttpCall promoteAndExecute response received data : ' + JSON.stringify(data));
                    if (this.getRequest().isCustomRequestFlag) { // 自定义请求
                        this.onComplete(this.getRequiredEntryObjValue(
                        data.responseCode,
                        this.getRequest().requiredBodyObjects, //获得request实体对象
                        JSON.parse(data.result))
                        );
                    } else { // 常规请求
                        this.onComplete(data);
                    }
                }
                this.client.dispatcher.finished(this, this.originalRequest.isSyncCall)
            })
            .catch((err) => {
                this.onError(err);
                this.client.dispatcher.finished(this, this.originalRequest.isSyncCall)
            });
    }

    onComplete(result) {
        Logger.info('HttpCall onComplete : ' + JSON.stringify(result));
        //From running queue get call object and from call object get callback APIs
        var successCallback = this.getSuccessCallback();
        successCallback(result);
    }

    onError(error) {
        hilog.info(0x0001, "HttpCall onError", JSON.stringify(error));
        var failureCallback = this.getFailureCallback();
        failureCallback(error);
    }

    getSuccessCallback() {
        return this.onSuccessCallback;
    }

    getFailureCallback() {
        return this.onFailureCallback;
    }

    cancel() {
        this.isCancelledRequest = true;
    }

    isCancelled() {
        return this.isCancelledRequest;
    }

    getResponseWithInterceptorChain(): Promise<Response> {
        let interceptors = new Array<Interceptor>();
        if (!!this.client.interceptors && this.client.interceptors.length > 0) {
            Array.prototype.push.apply(interceptors, this.client.interceptors);
        }
        interceptors.push(new RetryAndFollowUpInterceptor(this.client))
        interceptors.push(new BridgeInterceptor())
        interceptors.push(new CacheInterceptor(this.client.cache))
        interceptors.push(new ConnectInterceptor())
        interceptors.push(new CallServerInterceptor(this.forWebSocket, this.client.dns()))

        //构建责任链
        const chain = new RealInterceptorChain(this, interceptors, 0, this.originalRequest
            , this.client.connectionTimeout, this.client.readTimeout, this.client.writeTimeout, this.ws, null)
        const response = chain.proceedI(this.originalRequest) //处理责任链中的拦截器，开启链式调用
        return response
    }

    retryAfterFailure(request: Request): boolean {
        Logger.info('HttpCall request.url.getAddress().length = ' + request.url.getAddress().length);
        return (request.url.getAddress().length > 1)
    }
}

export default HttpCall;
