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

import HttpCall from '../HttpCall';
import Request from '../Request';
import { Chain, Interceptor } from '../Interceptor';
import { Response } from '../response/Response';
import { Exchange } from '../connection/Exchange';
import { Logger } from '../utils/Logger';
import { RealWebSocket } from '../RealWebSocket';


export class RealInterceptorChain implements Chain {
    private call: HttpCall
    private interceptors: Array<Interceptor>
    private index: number = 0
    private request: Request
    private connectTimeoutMillis: number
    private readTimeoutMillis: number
    private writeTimeoutMillis: number
    private exchange: Exchange
    private ws: RealWebSocket

    constructor(call: HttpCall,
                interceptors: Array<Interceptor>,
                index: number,
                request: Request,
                connectTimeoutMillis: number,
                readTimeoutMillis: number,
                writeTimeoutMillis: number,
                webSocket?: RealWebSocket,
                exchange?: Exchange) {
        this.call = call
        this.interceptors = interceptors
        this.index = index
        this.request = request
        this.connectTimeoutMillis = connectTimeoutMillis
        this.readTimeoutMillis = readTimeoutMillis
        this.writeTimeoutMillis = writeTimeoutMillis
        this.exchange = exchange
        this.ws = webSocket
    }

    connectTimeoutMillisI(): number {
        return this.connectTimeoutMillis
    }

    webSocket() {
        return this.ws
    }

    withConnectTimeoutI(timeout: number): Chain {
        if (this.exchange == null) throw Error("Timeouts can't be adjusted in a network interceptor")
        return new RealInterceptorChain(this.call, this.interceptors, this.index, this.request, timeout,
        this.readTimeoutMillis, this.writeTimeoutMillis, this.ws, this.exchange)
    }

    readTimeoutMillisI(): number {
        return this.readTimeoutMillis
    }

    withReadTimeoutI(timeout: number): Chain {
        if (this.exchange == null) throw Error("Timeouts can't be adjusted in a network interceptor")
        return new RealInterceptorChain(this.call, this.interceptors, this.index, this.request, this.connectTimeoutMillis,
            timeout, this.writeTimeoutMillis, this.ws, this.exchange)
    }

    writeTimeoutMillisI(): number {
        return this.writeTimeoutMillis
    }

    withWriteTimeoutI(timeout: number): Chain {
        if (this.exchange == null) throw Error("Timeouts can't be adjusted in a network interceptor")
        return new RealInterceptorChain(this.call, this.interceptors, this.index, this.request, this.connectTimeoutMillis,
        this.readTimeoutMillis, timeout, this.ws, this.exchange)
    }

    callI(): HttpCall {
        return this.call
    }

    requestI(): Request {
        return this.request
    }

    proceedI(request: Request): Promise<Response> {
        // Call the next interceptor in the chain.
        // 实例化下一个拦截器对应的RealIterceptorChain对象，这个对象会在传递给当前的拦截器
        let next = new RealInterceptorChain(this.call, this.interceptors, this.index + 1, request, this.connectTimeoutMillis,
        this.readTimeoutMillis, this.writeTimeoutMillis, this.ws, this.exchange)
        let interceptor = this.interceptors[this.index] //得到当前的拦截器：interceptors是存放拦截器的Array
        let response = interceptor.intercept(next) //调用当前拦截器的intercept()方法，并将下一个拦截器的RealIterceptorChain对象传递下去
        Logger.info("RealInterceptorChain real response =" + JSON.stringify(response))
        return response
    }
}
