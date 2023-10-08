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

import { HttpStatusCodes } from '../code/HttpStatusCodes';
import CacheControl from '../cache/CacheControl';
import Request from '../Request';

export class Response {
    responseCode: number
    result: string
    header: string
    uploadTask: any
    downloadTask: any
    priorResponse: Response = null
    request: Request
    protocol: string
    message: string
    networkResponse: Response
    cacheResponse: Response
    sentRequestAtMillis: number
    receivedResponseAtMillis: number
    cacheControl: CacheControl

    constructor(builder?) {
        this.responseCode = builder?.responseCode;
        this.result = builder?.result;
        this.header = builder?.header;
        this.uploadTask = builder?.uploadTask;
        this.downloadTask = builder?.downloadTask;
        this.priorResponse = builder?.priorResponse

        this.request = builder?.request
        this.protocol = builder?.protocol
        this.message = builder?.message
        this.networkResponse = builder?.networkResponse
        this.cacheResponse = builder?.cacheResponse
        this.priorResponse = builder?.priorResponse
        this.sentRequestAtMillis = builder?.sentRequestAtMillis
        this.receivedResponseAtMillis = builder?.receivedResponseAtMillis
    }

    static get Builder() {
        return class Builder {
            responseCode: number
            result: string
            header: string
            uploadTask: any
            downloadTask: any
            priorResponse: Response = null
            request: Request
            protocol: string
            message: string
            networkResponse: Response
            cacheResponse: Response
            sentRequestAtMillis: number
            receivedResponseAtMillis: number
            cacheControl: CacheControl

            constructor(response: Response) {
                if (response != null && response != undefined) {
                    this.responseCode = response.responseCode;
                    this.result = response.result;
                    this.header = response.header;
                    this.uploadTask = response.uploadTask;
                    this.downloadTask = response.downloadTask;
                    this.priorResponse = response.priorResponse

                    this.request = response.request
                    this.protocol = response.protocol
                    this.message = response.message
                    this.networkResponse = response.networkResponse
                    this.cacheResponse = response.cacheResponse
                    this.sentRequestAtMillis = response.sentRequestAtMillis
                    this.receivedResponseAtMillis = response.receivedResponseAtMillis
                    this.cacheControl = response.cacheControl
                }
            }

            public setBody(body: string) {
                this.result = body;
                return this;
            }

            public setPriorResponse(priorResponse: Response) {
                if (priorResponse != null) this.checkPriorResponse(priorResponse);
                this.priorResponse = priorResponse;
                return this;
            }

            public checkPriorResponse(response: Response) {
                if (response.result != null) {
                    throw new Error("priorResponse.body != null");
                }
            }

            public setRequest(request: Request) {
                this.request = request;
                return this
            }

            public setProtocol(protocol: string) {
                this.protocol = protocol;
                return this;
            }

            public setCode(code: number) {
                this.responseCode = code
                return this;
            }

            public setMessage(message: string) {
                this.message = message;
                return this;
            }

            public setHeader(name: string, value?: string) {
                if (value != null) {
                    this.header = this.header + "," + name + ":" + value
                } else {
                    this.header = name
                }
                return this;
            }

            public setResult(body: string) {
                this.result = body
                return this;
            }

            public setNetworkResponse(networkResponse: Response) {
                this.networkResponse = networkResponse;
                return this;
            }

            public setCacheResponse(cacheResponse: Response) {
                this.cacheResponse = cacheResponse;
                return this
            }

            public setSentRequestAtMillis(sentRequestAtMillis: number) {
                this.sentRequestAtMillis = sentRequestAtMillis;
                return this
            }

            public setReceivedResponseAtMillis(receivedResponseAtMillis: number) {
                this.receivedResponseAtMillis = receivedResponseAtMillis;
                return this
            }

            public build(): Response {
                if (this.responseCode < 0) throw Error("responseCode < 0: " + this.responseCode);
                return new Response(this);
            }
        }
    }

    public getRequest(): Request {
        return this.request;
    }

    public getProtocol(): string {
        return this.protocol;
    }

    public getCode(): number {
        return this.responseCode;
    }

    public getMessage(): string {
        return this.message;
    }

    public isSuccessful(): boolean {
        return this.responseCode >= 200 && this.responseCode < 300;
    }

    public getHeader(): string {
        return this.header
    }

    public getHeadersEntry(name: string): string {
        let headerItem: string = ''
        let h = JSON.parse(this.header)

        for (let key in h) {
            if (key == name) {
                headerItem = h[key]
            }
        }

        return headerItem
    }

    public getBody(): string {
        return this.result
    }

    public setBody(body: string) {
        this.result = body
    }

    public isRedirect(): boolean {
        switch (this.responseCode) {
            case HttpStatusCodes.HTTP_PERM_REDIRECT:
            case HttpStatusCodes.HTTP_TEMP_REDIRECT:
            case HttpStatusCodes.HTTP_MULT_CHOICE:
            case HttpStatusCodes.HTTP_MOVED_PERM:
            case HttpStatusCodes.HTTP_MOVED_TEMP:
            case HttpStatusCodes.HTTP_SEE_OTHER:
                return true;
            default:
                return false;
        }
    }

    public getNetWorkResponse(): Response {
        return this.networkResponse
    }

    public getCacheResponse(): Response {
        return this.cacheResponse;
    }

    public getPriorResponse(): Response {
        return this.priorResponse;
    }

    public getCacheControl() {
        let result = this.cacheControl;
        return result != null ? result : (this.cacheControl = CacheControl.parse(this.header))
    }

    public getSentRequestAtMillis() {
        return this.sentRequestAtMillis;
    }

    public getReceivedResponseAtMillis() {
        return this.receivedResponseAtMillis;
    }

    public setCacheControl(cacheControl: CacheControl) {
        this.cacheControl = cacheControl
    }

    public toString() {
        return "Response{protocol="
        + (this.protocol)
        + ", code="
        + (this.responseCode)
        + ", message="
        + (this.message)
        + ", url="
        + (this.request).url
        + '}';
    }

    public newBuilder() {
        return new Response.Builder(this);
    }
}

