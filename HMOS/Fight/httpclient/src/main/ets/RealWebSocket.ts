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

import webSocket from '@ohos.net.webSocket';
import { WebSocketListener } from './WebSocketListener';
import Request from './Request';
import { WebSocket } from './WebSocket';
import { Logger } from './utils/Logger';
import { ArrayDeque } from './utils/ArrayDeque';
import { Utils } from './utils/Utils';
import Url from '@ohos.url'
import hilog from '@ohos.hilog';

export class RealWebSocket implements WebSocket {
    private listener: WebSocketListener
    private originalRequest: Request
    private ws: webSocket.WebSocket
    private mQueueSize: number = 0
    private failed = false
    private messageAndCloseQueue = new  ArrayDeque<any>()
    private MAX_QUEUE_SIZE = 16 * 1024 * 1024
    private CLOSE_CLIENT_GOING_AWAY = 1001
    private connectEvent: Function
    private originRetryCount: number
    private retryCount: number = 0
    private url: string
    private isConnected = false
    private intervalID: number = -1

    constructor(request: Request, listener: WebSocketListener) {
        this.originRetryCount = request.retryMaxLimit < request.retryConnectionCount ? request.retryMaxLimit : request.retryConnectionCount
        this.ws = webSocket.createWebSocket();
        this.listener = listener
        this.originalRequest = request
        this.isConnected = false
        this.retryCount = 0
        this.intervalID = -1
        this.initEvent()
    }

    cancel() {
        //todo
    }

    async send(text: string | ArrayBuffer): Promise<boolean> {
        return await new Promise((resolve, reject) => {
            if (!!!text) {
                resolve(false)
            }
            let data: string = ''
            if (text instanceof ArrayBuffer) {
                let ab = text as ArrayBuffer
                data = Utils.Utf8ArrayToStr(ab)
            }
            if (this.mQueueSize + data.length > this.MAX_QUEUE_SIZE) {
                this.close(this.CLOSE_CLIENT_GOING_AWAY)
                resolve(false)
            }

            this.mQueueSize = data.length

            this.sendMessage(text).then((isSuccess) => {
                resolve(isSuccess)
            })
        })
    }

    async close(code: number, reason?: string): Promise<boolean> {
        if (!!this.listener) {
            this.listener.onClosing(this, code, reason)
        }

        if (this.failed) {
            return false
        }
        this.messageAndCloseQueue.addTail(new Close(code, reason))

        return await new Promise((resolve, reject) => {
            this.runWriter().then((isSuccess) => {
                resolve(isSuccess)
            })
        })
    }

    request(): Request {
        return this.originalRequest
    }

    queueSize(): number {
        return this.mQueueSize
    }

    connect(url, connectEvent) {
        this.url = url
        this.connectEvent = connectEvent

        let urlObject = Url.URL.parseURL(url);
        let host = urlObject.hostname
        let port = urlObject.port || '80'
        let pathname = urlObject.pathname
        let search = urlObject.search

        if (this.originalRequest.headers["Sec-WebSocket-Extensions"] != null) {
            this.failWebSocket(new Error(
                "Request header not permitted: 'Sec-WebSocket-Extensions'"), null)
            return
        }

        let requestHeader = "GET " + pathname + search + " HTTP/1.1\r\n"
        + "Host: " + host + ":" + port + "\r\n"
        + "Connection: Upgrade\r\n"
        + "Cache-Control: no-cache\r\n"
        + "Upgrade: websocket\r\n"
        + "Sec-WebSocket-Version: 13\r\n"

        let header = this.originalRequest.headers
        if (!!header) {
            Object.keys(header).forEach(function (key) {
                requestHeader = requestHeader.concat(key).concat(': ').concat(header[key].toString() + "\r\n");
            })
        }
        requestHeader += "\r\n"

        let params = this.originalRequest.params
        if (!!params) {
            let isBegin = true;
            Object.keys(params).forEach(function (key) {
                url = url.toString().concat(isBegin ? '?' : '&');
                url = url.concat(key).concat('=').concat(params[key].toString());
                isBegin = false;
            })
        }

        this.ws.connect(url, { header: requestHeader }, (err, value) => {
            //todo   connect方法目前存在问题，不管URL是否有效，结果都是连接成功，需要OS侧修改
            if (!err) {
                this.connectEvent(null, value)
            } else {
                this.connectEvent(err, null)
                this.failWebSocket(err)
            }
        })

        if (this.intervalID == -1) {
            let that = this
            this.intervalID = setInterval(function () {
                if (that.isConnected) {
                    clearInterval(that.intervalID)
                    that.intervalID = -1
                } else {
                    if (that.retryCount < that.originRetryCount) {
                        that.ws = webSocket.createWebSocket();
                        that.initEvent()
                        that.ws.connect(url, { header: that.originalRequest.headers }, (err, value) => {
                        })
                        that.retryCount++
                        Logger.info("ws------on retryCount " + that.retryCount);
                    } else {
                        that.connectEvent("can not connect server", null)
                        that.retryCount == 0
                        that.isConnected = false
                        clearInterval(that.intervalID)
                        that.intervalID = -1
                    }
                }
            }, 1000 * 5)
        }
    }

    failWebSocket(e: Error, response?: string) {
        this.listener.onFailure(this, e, response)
        this.failed = true
    }

    private initEvent() {
        this.ws.on('open', (err, value) => {
            this.isConnected = true
            this.listener.onOpen(this, "")
            this.retryCount == 0
            this.connectEvent(null, value)
            clearInterval(this.intervalID)
            this.intervalID = -1
            Logger.info("ws------on open " + JSON.stringify(value));
        });

        this.ws.on('message', (err, value) => {
            this.listener.onMessage(this, value)
            Logger.info("ws------on message, msg:" + JSON.stringify(value));
        });

        this.ws.on('close', (err, value) => {
            Logger.info("ws------on close " + JSON.stringify(value));
            this.isConnected = false
            if (value.code == 1000) {
                this.retryCount == 0;
                this.intervalID = -1;
                this.listener.onClosed(this, value.code, value.reason);
                return
            }

            if (this.retryCount < this.originRetryCount) {
                this.retryCount++
                this.intervalID = -1
                this.connect(this.url, this.connectEvent)
                Logger.info("ws------on -retryCount " + this.retryCount);
            }
            this.listener.onClosed(this, value.code, value.reason)
        });

        this.ws.on('error', (err) => {
            this.listener.onFailure(this, err)
            hilog.error(0x0001, "ws------on error, error", JSON.stringify(err));
        });
    }

    private async sendMessage(message): Promise<boolean> {
        if (!!!this.ws) {
            return await new Promise((resolve, reject) => {
                resolve(false)
            })
        }

        this.messageAndCloseQueue.addTail(new Message(message))
        return await new Promise((resolve, reject) => {
            this.runWriter().then((isSuccess) => {
                resolve(isSuccess)
            })
        })
    }

    private async runWriter(): Promise<boolean> {
        let messageOrClose = this.messageAndCloseQueue.poll()
        if (!!!messageOrClose) {
            return false
        }

        if (messageOrClose instanceof Close) {
            let close = messageOrClose as Close
            return await new Promise((resolve, reject) => {
                this.ws.close({
                    code: close.code,
                    reason: close.reason
                }, (err, value) => {
                    if (!err) {
                        this.ws = null;
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                });
            })
        }

        if (messageOrClose instanceof Message) {
            let msg = messageOrClose as Message
            return await new Promise((resolve, reject) => {
                this.ws.send(msg.data, (err, value) => {
                    if (!err) {
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                })
            })
        }
    }
}

export default class Message {
    data: string
    formatOpcode?: number

    constructor(data: string, formatOpcode?: number) {
        this.data = data
        this.formatOpcode = formatOpcode
    }

    getData() {
        return this.data
    }

    getFormatOpcode() {
        return this.formatOpcode
    }
}


export class Close {
    code: number
    reason?: string

    constructor(code: number, reason?: string) {
        this.code = code
        this.reason = reason
    }

    getCode() {
        return this.code
    }

    getReason() {
        return this.reason
    }
}