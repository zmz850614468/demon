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

import { RealWebSocket } from './RealWebSocket'

export abstract class WebSocketListener {
    abstract onOpen(webSocket: RealWebSocket, response: string): void;

    abstract onMessage(webSocket: RealWebSocket, text: string | ArrayBuffer): void;

    abstract onClosing(webSocket: RealWebSocket, code: number, reason?: string): void;

    abstract onClosed(webSocket: RealWebSocket, code: number, reason: string): void;

    abstract onFailure(webSocket: RealWebSocket, e: Error, response?: string): void;
}