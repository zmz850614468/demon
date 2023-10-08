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

import Request from './Request';
import HttpCall from './HttpCall';
import { Response } from './response/Response';
import { RealWebSocket } from './RealWebSocket';


export interface Interceptor {
    intercept(chain: Chain): Promise<Response>
}

export interface Chain {
    requestI(): Request

    webSocket(): RealWebSocket

    proceedI(request: Request): Promise<Response>

    callI(): HttpCall

    connectTimeoutMillisI(): number

    withConnectTimeoutI(timeout: number): Chain

    readTimeoutMillisI(): number

    withReadTimeoutI(timeout: number): Chain

    writeTimeoutMillisI(): number

    withWriteTimeoutI(timeout: number): Chain
}