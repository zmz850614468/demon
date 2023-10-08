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
import { Logger } from '../utils/Logger';

export class Dispatcher {

    //The maximum number of requests to execute concurrently
    private maxRequests = 64
    //The maximum number of requests for each host to execute concurrently
    private maxRequestsPerHost = 5
    private callsPerHost = 0
    //Running synchronous calls. Includes canceled calls that haven't finished yet
    private runningSyncCalls = Array<HttpCall>()
    //Ready async calls in the order they'll be run
    private readyAsyncCalls = Array<HttpCall>()
    //Running asynchronous calls. Includes canceled calls that haven't finished yet
    private runningAsyncCalls = Array<HttpCall>()

    executed(call: HttpCall) {
        this.runningSyncCalls.push(call)
    }

    enqueue(call: HttpCall) {
        this.readyAsyncCalls.push(call)
        // Mutate the AsyncCall so that it shares the AtomicInteger of an existing running call to
        // the same host.
        if (!call.forWebSocket) { //请求任务是否是websocket请求
            //在异步任务等待队列和异步任务执行队列中寻找和当前请求主机名相同的请求任务
            let existingCall = this.findExistingCallWithHost(call.getHost())
            //是否存在域名相同的请求任务
            if (existingCall != null) {
                //对所有域名相同的请求任务公用同一个AtomicInteger，用于处理相同host的并发问题
                call.reuseCallsPerHostFrom(existingCall)
            }
        }
        this.promoteAndExecute()
    }

    /** Used by [execute/enqueue] to signal completion. */
    finished(call: HttpCall, isSyncCall: boolean) {
        Logger.info("HttpTaskDispatcher isSyncCall =" + isSyncCall)
        this.finishedAll(isSyncCall ? this.runningSyncCalls : this.runningAsyncCalls, call)
        if (!isSyncCall) {
            call.decrement() //与当前请求任务域名相同的并发数减1
        }
    }

    private finishedAll<T>(calls: Array<T>, call: T) {
        //异步任务执行队列移除当前请求任务
        if (!calls.splice(calls.indexOf(call))) throw Error("Call wasn't in-flight!")
        //继续执行promoteAndExecute获取异步任务执行队列里面的任务数是否大于0
        //大于0 ，还有任务需要执行，循环执行任务队列里面的任务
        this.promoteAndExecute()
    }

    private promoteAndExecute(): boolean {
        Logger.info("HttpTaskDispatcher promoteAndExecute this.readyAsyncCalls =" + JSON.stringify(this.readyAsyncCalls))
        let executableCalls = Array<HttpCall>()
        let isRunning: boolean
        let i = 0
        while (this.readyAsyncCalls[i]) { //循环等待队列
            let asyncCall = this.readyAsyncCalls[i]
            //判断异步任务执行队列里面的任务数是否超出允许的最大任务数64
            if (this.runningAsyncCalls.length >= this.maxRequests) break // Max capacity. //是 跳出循环
            //判断与等待队列取出的异步任务域名相同的请求并发数是否超过允许的单一host最大并发数5
            //是的话跳出本次循环，继续下次循环
            if (asyncCall.getCallsPerHost() >= this.maxRequestsPerHost) continue // Host max capacity.
            //从异步任务等待队列里面移除取出的异步任务
            this.readyAsyncCalls.splice(this.readyAsyncCalls.indexOf(this.readyAsyncCalls[i]))
            asyncCall.increment() //与取出的异步任务域名相同的并发数加1
            executableCalls.push(asyncCall) //将取出的异步任务添加到列表以及添加到任务管理器的异步任务执行队列
            this.runningAsyncCalls.push(asyncCall) //将取出的异步任务添加到任务管理器的异步任务执行队列
            i++
        }
        isRunning = this.runningCallsCount() > 0
        //循环异步任务列表executableCalls
        for (var j = 0;j < executableCalls.length; j++) {
            let asyncCall = executableCalls[j]
            asyncCall.executeOn() //执行异步任务executeOn
        }
        return isRunning
    }

    private runningCallsCount(): number {
        return this.runningAsyncCalls.length + this.runningSyncCalls.length
    }

    /**
     * 在异步任务等待队列和异步任务执行队列中寻找和当前请求主机名相同的请求任务
     * @param host
     */
    private findExistingCallWithHost(host: String): HttpCall {
        for (var i = 0;i < this.runningAsyncCalls.length; i++) {
            if (this.runningAsyncCalls[i].getHost() == host) return this.runningAsyncCalls[i];
        }
        for (var i = 0;i < this.readyAsyncCalls.length; i++) {
            if (this.readyAsyncCalls[i].getHost() == host) return this.readyAsyncCalls[i];
        }
        return null
    }
}
