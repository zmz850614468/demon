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
import socket from '@ohos.net.socket';
import Url from '@ohos.url'
import Request from './Request';
import buffer from '@ohos.buffer';
import { Logger } from './utils/Logger';
import ConstantManager from './ConstantManager'
import connection from '@ohos.net.connection';
import { Utils } from './utils/Utils';
import hilog from '@ohos.hilog';

export class Http {
    public static createHttp(): HttpRequest {
        let httpRequest = new HttpRequest()
        return httpRequest
    }
}

export class HttpRequest {
    responseHeader = new Map()
    private port: string
    private ALPNProtocols: Array<string> = ["spdy/1", "http/1.1"]
    private originUrl: string
    private isFirstSegment: boolean = true
    private valueMessageLength: number
    private response: HttpResponse
    private headerLength: number
    private contentLength: number
    private firstSegment: string = ""
    private isStartReadData: boolean
    private isContainChunked: boolean
    private contentType: string
    private responseData: string = ""
    private isHttps: boolean = true
    private callback: AsyncCallback<HttpResponse>
    private isClose: boolean = true
    private socket
    private secureOptions: socket.TLSSecureOptions = {
        ca: [],
        cert: '',
        key: '',
        passwd: '',
        protocols: [socket.Protocol.TLSv12],
        useRemoteCipherPrefer: true,
        signatureAlgorithms: '',
        cipherSuite: ''
    }

    constructor() {
        this.isClose = false
    }

    /**
     * Initiates an HTTP request to a given URL.
     *
     * @param url URL for initiating an HTTP request.
     * @param options Optional parameters {@link HttpRequestOptions}.
     * @param callback Returns {@link HttpResponse}.
     * @permission ohos.permission.INTERNET
     */
    public request(url: string, originUrl: string, request: Request, options: HttpRequestOptions, callback: AsyncCallback<HttpResponse>) {
        Logger.info('http request--> url >>>>>> ' + url)
        this.originUrl = originUrl
        let urlObject = Url.URL.parseURL(url);
        let protocol = urlObject.protocol.toLowerCase()
        if (protocol == "http:") {
            this.isHttps = false
            this.socket = socket.constructTCPSocketInstance();
        }

        if (protocol == "https:") {
            this.isHttps = true
            this.socket = socket.constructTLSSocketInstance();
        }

        Logger.info('http request--> option >>>>>> ' + JSON.stringify(options))
        Logger.info('http request--> request >>>>>>  ' + JSON.stringify(request))
        this.callback = callback
        this.bind(() => {
            this.on("message", value => {
                this.valueMessageLength = value.message.byteLength
                let content = Utils.Utf8ArrayToStr(value.message)
                if (this.isFirstSegment) {
                    let arr: string[] = content.split("\r\n");
                    for (let i = 0; i < arr.length; i++) {
                        Logger.info('http request--> serve response >>>>>>  ' + JSON.stringify(arr[i]))
                        let header = arr[i].split(": ")
                        if (header[0] == "Content-Type") {
                            let arrType = header[1].split("charset=")
                            if (arrType.length > 0) {
                                this.contentType = arrType[1]
                            }
                            break
                        }
                    }
                }

                    this.isFirstSegment == true ? false : false
                if (!!this.contentType && (this.contentType == "GB2312" || this.contentType == "GB18030" || this.contentType == "GBK")) {
                    content = Utils.strAnsi2Unicode(content)
                } else {
                    content = buffer.from(value.message).toString('utf8')
                }
                this.dealSuccessResult(content)
            });

            this.on('error', err => {
                if (!this.isClose) {
                    hilog.error(0x0001, 'http request-->on: error ', JSON.stringify(err))
                    this.dealFailResult(err.code, err.message, err.name)
                }
            });

            this.on('close', () => {
                this.isClose = true
                Logger.info("http request-->on: close")
            });

            this.connect(url, options, () => {
                this.send(url, request, options)
            })
        })
    }

    /**
     * 设置CA证书
     * @param caStr
     */
    public setCaData(caStr: string[]) {
        this.secureOptions.ca = caStr;
    }

    /**
     * Destroys an HTTP request.
     */
    public destroy() {
        setTimeout(() => {
            this.socket.close()
                .then(() => {
                    Logger.info('http request--> socket is closed')
                }).catch(err => {
                hilog.error(0x0001, 'http request-->socket close fail ', JSON.stringify(err))
            })
        }, 15000)
    }

    public on(type: string, callback: Callback<any>) {
        switch (type) {
            case 'message':
                this.socket.on('message', (v) => {
                    callback(v)
                });
                break
            case 'connect':
                this.socket.on('connect', (v) => {
                    callback(null)
                });
                break
            case 'close':
                this.socket.on('close', (v) => {
                    callback(null)
                });
                break
            case 'error':
                this.socket.on('error', (v) => {
                    callback(v)
                });
                break
        }
    }

    public off(type: string, callback: Callback<Object>) {
        switch (type) {
            case 'message':
                this.socket.off('message', (v) => {
                    callback(v)
                });
                break
            case 'connect':
                this.socket.off('connect', (v) => {
                    callback(null)
                });
                break
            case 'close':
                this.socket.off('close', (v) => {
                    callback(null)
                });
                break
            case 'error':
                this.socket.off('error', (v) => {
                    callback(v)
                });
                break
        }
    }

    private dealSuccessResult(res: string) {
        let currentRes = res
        if (this.callback) {
            if (!!!this.response) {
                this.response = new HttpResponse()
            }

            let arr: string[] = res.split("\r\n");
            if (!!!this.response.responseCode && !!!this.contentLength) {
                for (let i = 0; i < arr.length; i++) {

                    if (!!!this.response.responseCode) {
                        let status = arr[0].split(' ')
                        this.response.responseCode = Number.parseInt(status[1])
                        continue
                    }

                    let header = arr[i].split(": ")
                    if (!!!this.contentLength && header[0] == "Content-Length") {
                        this.contentLength = Number.parseInt(header[1])
                        continue
                    }

                    if (!!!this.isContainChunked && header[0] == "Transfer-Encoding" && header[1] == "chunked") {
                        this.isContainChunked = true
                        continue
                    }

                    if (arr[i] == "") {
                        this.isStartReadData = true
                        continue
                    }

                    if (!!!this.isStartReadData) {
                        this.responseHeader.set(header[0], header[1])
                    }

                    if (this.isStartReadData) {
                        if (this.isContainChunked) {
                            this.firstSegment += arr[i+1]
                        } else {
                            this.firstSegment += arr[i]
                        }
                    }
                }
                this.headerLength = this.valueMessageLength - Utils.strToArrayBuffer(this.firstSegment).byteLength
                res = this.firstSegment
            }

            if (this.isContainChunked) {
                if (res != "0\r\n\r\n") {
                    this.responseData += res
                } else {
                    this.responseData += " "
                }
                if (!currentRes.endsWith("0\r\n\r\n")) {
                    return
                }
                this.responseData = this.responseData.substring(0, this.responseData.length - 1);
                this.response.result = this.responseData
            } else {
                this.responseData += res
                let ab = Utils.strToArrayBuffer(this.responseData)
                if (this.headerLength + ab.byteLength < this.contentLength) {
                    return
                }
                this.response.result = this.responseData
            }

            this.response.header = Object.fromEntries(this.responseHeader.entries())
            this.callback(null, this.response)
        }
    }

    private dealFailResult(code: number, msg: string, name: string) {
        if (this.callback) {
            let errContent = {
                code: code,
                message: msg,
                name: name
            }
            Logger.error('http request--> fail：' + JSON.stringify(errContent))
            this.callback(errContent, null)
        }
    }

    private bind(callback) {
        let that = this;
        connection.getDefaultNet().then(function (netHandle) {
            connection.getConnectionProperties(netHandle, function (error, info) {
                let ip = info.linkAddresses[0].address.address
                that.socket.bind({ address: ip }, err => {
                    if (err) {
                        hilog.error(0x0001, 'http request-->', " bind socket fail " + JSON.stringify(err))
                        that.dealFailResult(err.code, 'bind fail', err.name)
                        return;
                    }
                    Logger.info('http request--> bind socket success')
                    callback()
                })
                return
            })
        })
    }

    private connect(url: string, options: HttpRequestOptions, callback) {
        let urlObject = Url.URL.parseURL(url);
        let host = urlObject.hostname
        let pathname = urlObject.pathname
        let search = urlObject.search

        let protocol = urlObject.protocol.toLowerCase()
        if (protocol == "http:") {
            this.port = urlObject.port || '80'
        } else if (protocol == "https:") {
            this.port = urlObject.port || '443'
        } else {
            this.port = "-1"
        }

        Logger.info(`http request--> connecting......    url:${url} , host:${host} , port:${this.port} , pathname=${pathname} , search=${search}`)

        let connectOptions = this.isHttps ? {
                                                ALPNProtocols: this.ALPNProtocols,
                                                address: { address: host, port: Number.parseInt(this.port), family: 1 },
                                                secureOptions: this.secureOptions,
                                            } : {
                                                    address: {
                                                        address: host,
                                                        port: Number.parseInt(this.port),
                                                        family: 1
                                                    },
                                                    timeout: options.connectTimeout
                                                }
        this.socket.connect(connectOptions, (err, data) => {
            if (!err) {
                Logger.info('http request--> connect socket success')
                callback()
                return
            }
            hilog.error(0x0001, 'http request--> connect socket fail ', JSON.stringify(err))
            this.dealFailResult(err.code, 'connect fail', err.name)
        });
    }

    private send(url: string, request: Request, options: HttpRequestOptions) {
        let sendBody = ''

        let urlObject = Url.URL.parseURL(url);
        let host = urlObject.hostname || ''
        let pathname = urlObject.pathname || ''
        let search = urlObject.search || ''
        Logger.info(`http request--> sending......    url:${url}  host:${host}, port=${this.port}, pathname=${pathname}, search=${search}`);

        let headers = ''
        if (options.header !== undefined) {
            for (let headerKey in options.header) {
                if (headerKey !== ConstantManager.CONTENT_TYPE) {
                    headers += headerKey + ': ' + options.header[headerKey] + '\r\n'
                }
            }
        }
        Logger.info('http request--> headers = ' + headers.replace(/\r\n/g, "........."))

        let hostName = Url.URL.parseURL(this.originUrl)
        switch (options.method.toString()) {
            case RequestMethod.GET:
                sendBody += "GET " + pathname + search + " HTTP/1.1\r\n"
                sendBody += "Host: " + hostName.hostname + "\r\n"
                sendBody += "Content-Type: application/json;charset=UTF-8\r\n"
                sendBody += "Cache-Control: no-cache\r\n"
                sendBody += 'Connection: keep-alive\r\n'
                sendBody += "\r\n"
                break
            case RequestMethod.DELETE:
                sendBody += "DELETE " + pathname + search + " HTTP/1.1\r\n"
                sendBody += "Host: " + hostName.hostname + "\r\n"
                sendBody += "Content-Type: application/json;charset=UTF-8\r\n"
                sendBody += "Cache-Control: no-cache\r\n"
                sendBody += 'Connection: keep-alive\r\n'
                sendBody += "\r\n"
                break
            case RequestMethod.PUT:
                let pa = ''
                if (typeof options.extraData == 'string' && options.extraData.length > 0) {
                    if (options.extraData.startsWith('{')) {
                        let obj = JSON.parse(options.extraData)
                        pa += '?'
                        for (let objKey in obj) {
                            pa += objKey + '=' + obj[objKey] + '&'
                        }
                        pa = pa.substring(0, pa.length - 1)
                    } else {
                        pa = options.extraData
                    }
                }

                sendBody += "PUT " + pathname + pa + " HTTP/1.1\r\n"
                sendBody += "Host: " + hostName.hostname + "\r\n"
                sendBody += 'Content-Type: application/json; charset=utf8\r\n'
                sendBody += 'Connection: keep-alive\r\n'
                if (headers.length > 0) {
                    sendBody += headers
                }
                sendBody += '\r\n'
                break
            case RequestMethod.POST:
                let params = ''
                if (search.length > 1) {
                    params = search.substring(1, search.length)
                } else if (typeof options.extraData == 'string' && options.extraData.length > 0) {
                    if (options.extraData.startsWith('{')) {
                        let obj = JSON.parse(options.extraData)
                        for (let objKey in obj) {
                            params += objKey + '=' + obj[objKey] + '&'
                        }
                        params = params.substring(0, params.length - 1)
                    } else {
                        params = options.extraData
                    }
                }

                Logger.info('http request--> params = ' + params.replace(/\r\n/g, "........."))

                sendBody += "POST " + pathname + " HTTP/1.1\r\n"
                sendBody += "Host: " + hostName.hostname + "\r\n"
                sendBody += 'Content-Type: application/x-www-form-urlencoded\r\n'
                if (headers.length > 0) {
                    sendBody += headers
                }
                if (params.length > 0) {
                    sendBody += 'Content-Length: ' + params.length + '\r\n'
                }
                sendBody += '\r\n'
                if (params.length > 0) {
                    sendBody += params + '\r\n'
                }
                sendBody += '\r\n'
                break
        }
        this.sendBody(sendBody)
    }

    private sendBody(body: string) {
        Logger.info("http request--> send body = " + body.toString().replace(/\r\n/g, " ........."));
        let sendBody = this.isHttps ? body.toString() : { data: body.toString(), encoding: 'UTF-8' }
        this.socket.send(sendBody)
            .then(() => {
                Logger.info('http request--> socket send success')
            })
            .catch((err) => {
                hilog.error(0x0001, 'http request-->socket send fail ', JSON.stringify(err))
                this.dealFailResult(err.code, 'socket send fail', err.name)
            })
    }
}

export interface HttpRequestOptions {
    /**
     * Request method.
     */
    method?: RequestMethod; // default is GET
    /**
     * Additional data of the request.
     * extraData can be a string or an Object (API 6) or an ArrayBuffer(API 8).
     */
    extraData?: string | Object | ArrayBuffer;
    /**
     * HTTP request header.
     */
    header?: Object; // default is 'content-type': 'application/json'
    /**
     * Read timeout period. The default value is 60,000, in ms.
     */
    readTimeout?: number; // default is 60s
    /**
     * Connection timeout interval. The default value is 60,000, in ms.
     */
    connectTimeout?: number; // default is 60s.
}

export enum RequestMethod {
    OPTIONS = "OPTIONS",
    GET = "GET",
    HEAD = "HEAD",
    POST = "POST",
    PUT = "PUT",
    DELETE = "DELETE",
    TRACE = "TRACE",
    CONNECT = "CONNECT"
}

export enum ResponseCode {
    OK = 200,
    CREATED,
    ACCEPTED,
    NOT_AUTHORITATIVE,
    NO_CONTENT,
    RESET,
    PARTIAL,
    MULT_CHOICE = 300,
    MOVED_PERM,
    MOVED_TEMP,
    SEE_OTHER,
    NOT_MODIFIED,
    USE_PROXY,
    BAD_REQUEST = 400,
    UNAUTHORIZED,
    PAYMENT_REQUIRED,
    FORBIDDEN,
    NOT_FOUND,
    BAD_METHOD,
    NOT_ACCEPTABLE,
    PROXY_AUTH,
    CLIENT_TIMEOUT,
    CONFLICT,
    GONE,
    LENGTH_REQUIRED,
    PRECON_FAILED,
    ENTITY_TOO_LARGE,
    REQ_TOO_LONG,
    UNSUPPORTED_TYPE,
    INTERNAL_ERROR = 500,
    NOT_IMPLEMENTED,
    BAD_GATEWAY,
    UNAVAILABLE,
    GATEWAY_TIMEOUT,
    VERSION
}

export class HttpResponse {
    /**
     * result can be a string (API 6) or an ArrayBuffer(API 8). Object is deprecated from API 8.
     */
    result: string | Object | ArrayBuffer;
    /**
     * Server status code.
     */
    responseCode: ResponseCode | number;
    /**
     * All headers in the response from the server.
     */
    header: Object;
    /**
     * @since 8
     */
    cookies: string;
}

export interface AsyncCallback<T, E = void> {
    /**
     * Defines the callback data.
     * @since 6
     */
    (err: BusinessError<E>, data: T): void;
}

export interface BusinessError<T = void> extends Error {
    /**
     * Defines the basic error code.
     * @since 6
     */
    code: number;
    /**
     * Defines the additional information for business
     * @type { ?T }
     * @since 9
     */
    data?: T;
}

export interface Callback<T> {
    /**
     * Defines the callback info.
     * @since 6
     */
    (data: T): void;
}