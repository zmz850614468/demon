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

import { Logger } from '../utils/Logger';
import hilog from '@ohos.hilog';

class HttpTaskDispatcher {
    runningQueue = [];
    waitingQueue = [];
    syncCallObject = null;
    asyncCallObject = null;

    constructor() {
        // Array is used to implement a Queue
        this.runningQueue = [];
        this.waitingQueue = [];
        this.syncCallObject = null;
        this.asyncCallObject = null;
    }

    onComplete(result, call) {
        Logger.info('HttpTaskDispatcher onComplete : ' + JSON.stringify(result));
        //From running queue get call object and from call object get callback APIs
        var successCallback = call.getSuccessCallback();
        successCallback(result);

        //delete the executed call from runningQueue and process the next call from waiting queue
        this.processNextCall();
        Logger.info('HttpTaskDispatcher: onComplete - waitingQueue.length: ' + this.waitingQueue.length);
    }

    onError(error, call) {
        Logger.info('HttpTaskDispatcher onError : ' + JSON.stringify(error));
        var failureCallback = call.getFailureCallback();
        failureCallback(error);

        //delete the executed call from runningQueue and process the next call from waiting queue
        this.processNextCall();
    }

    processNextCall() {
        try {
            //remove the executed HttpCall in the runningQueue
            if (this.runningQueue.length > 0) {
                this.dequeue(this.runningQueue);
            }
            Logger.info('HttpTaskDispatcher processNextCall runningQueue size : ' + this.runningQueue.length);

            //execute the next HttpCall in the waitingQueue
            if (this.waitingQueue.length > 0) {
                let nextCall = this.dequeue(this.waitingQueue);
                this.enqueue(nextCall);
            }
            Logger.info('HttpTaskDispatcher processNextCall waitingQueue size : ' + this.waitingQueue.length);
        } catch (error) {
            hilog.error(0x0001, "HttpTaskDispatcher processNextCall caught error", error);
        }
    }

    enqueue(call) {
        // If running queue is not empty then push to running queue
        if (!this.runningQueue.length) {
            Logger.info('HttpTaskDispatcher enqueue : call is pushed to the running queue');
            this.promoteAndExecuteCall(call);
        } else {
            //else push to waiting queue
            Logger.info('HttpTaskDispatcher enqueue : call is pushed to the waiting queue');
            this.waitingQueue.push(call);
        }
    }

    promoteAndExecuteCall(call) {
        this.asyncCallObject = call;
        if (call.isCancelled()) {
            Logger.info('HttpTaskDispatcher promoteAndExecute call is marked cancelled');
            let errMsg = { code: '', data: 'Request canceled by user' };
            this.onError(errMsg, call);
            return;
        }
        let requestobj = call.getRequest();
        this.runningQueue.push(call);
        let interceptors2 = requestobj.interceptors;


        //Invoke framework API to perform the task
        this.executeFrameWorkCall(requestobj, interceptors2)
            .then((data) => {
                if (call.isCancelled()) {
                    Logger.info('HttpTaskDispatcher promoteAndExecute call is marked cancelled');
                    let errorMessage = { code: '', data: 'Request canceled by user' };
                    this.onError(errorMessage, call);
                } else {
                    Logger.info('HttpTaskDispatcher promoteAndExecute response received data : ' + JSON.stringify(data));
                    this.onComplete(data, call);
                }
            })
            .catch((err) => {
                hilog.info(0x0001, "HttpTaskDispatcher promoteAndExecute response received error", err);
                this.onError(err, call);
            });
    }

    executeFrameWorkCall(request, interceptors) {
        Logger.info('HttpTaskDispatcher executeFrameWorkCall');
        return new Promise(request.httpRequestProcess.process.bind(request.httpRequestProcess, interceptors));
    }

    execute(call) {
        Logger.info('HttpTaskDispatcher execute');
        this.syncCallObject = call;
        let request = call.getRequest();
        let interceptors = request.interceptors;
        return this.executeFrameWorkCall(request, interceptors);
    }

    dequeue(queue) {
        if (!queue.length) {
            Logger.error('HttpTaskDispatcher dequeue underflow');
            throw 'queue underflow error';
        }
        return queue.shift();
    }

    getQueuedCalls() {
        return this.waitingQueue;
    }

    getRunningCalls() {
        return this.runningQueue;
    }
}

export default HttpTaskDispatcher;