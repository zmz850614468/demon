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

import { Chain, Interceptor } from '../Interceptor';
import { Response } from '../response/Response';
import { RealInterceptorChain } from './RealInterceptorChain';
import { HttpStatusCodes } from '../code/HttpStatusCodes';
import http from '@ohos.net.http';
import { Http } from '../Http';
import { Logger } from '../utils/Logger';
import request from '@ohos.request';
import FileUtils from '../utils/FileUtils';
import { Dns } from '../Dns';
import { Utils } from '../utils/Utils';
import { RouteSelector } from '../connection/RouteSelector';
import Protocol from '../protocols/Protocol';
import buffer from '@ohos.buffer';
import ChunkUploadDispatcher from '../dispatcher/ChunkUploadDispatcher';
import BinaryFileChunkUpload from '../builders/BinaryFileChunkUpload';
import ConstantManager from '../ConstantManager';
import connection from '@ohos.net.connection';
import { DnsSystem } from '../dns/DnsSystem';
import hilog from '@ohos.hilog';

export class CallServerInterceptor implements Interceptor {
    maxUploadCount: number = 5
    fileName: string = ''
    uploadProgress
    routeSelector: RouteSelector
    error: string = "ERROR"
    success: string = "SUCCESS"
    uploadCallback
    chunkUploadDispatcher: ChunkUploadDispatcher
    private forWebSocket: boolean = false
    private dns: Dns

    constructor(forWebSocket: boolean, dns: Dns) {
        this.forWebSocket = forWebSocket
        this.dns = dns
    }

    async intercept(chain: Chain): Promise<Response> {
        let sentRequestMillis = new Date().getTime()
        chain = chain as RealInterceptorChain
        const userRequest = chain.requestI()
        Logger.info("userRequest.method=" + userRequest.method)
        if (!!!this.dns && !!userRequest.getCa() && !userRequest.isDnsInterceptor) {
            this.dns = new DnsSystem()
        }
        var body = userRequest.body;
        var parsedUrl: string = userRequest.url.getUrl();
        let hostOrIp = Utils.getDomainOrIp(parsedUrl)
        let regexp = new RegExp("(\\d*\\.){3}\\d*")
        let isIPv6 = Utils.isIPv6(hostOrIp)
        let isIp = regexp.test(hostOrIp)

        if (!isIp || !isIPv6) {
            if (!!!this.routeSelector) {
                if (!!this.dns) {
                    await this.dns.lookup(hostOrIp).then((netAddress) => {
                        if (!!!netAddress) {
                            new Error("dns parser not available ip")
                            return
                        }
                        let addressArray: Array<connection.NetAddress> = Utils.arrayUnrepeated(netAddress)
                        if (Utils.isIPv6(addressArray[0].address)) {
                            let ipv4Address: Array<connection.NetAddress> = [];
                            let ipv6Address: Array<connection.NetAddress> = [];
                            for (var i = 0;i < addressArray.length; i++) {
                                if (Utils.isIPv6(addressArray[i].address)) {
                                    ipv6Address.push(addressArray[i])
                                } else {
                                    ipv4Address.push(addressArray[i])
                                }
                            }
                            addressArray = [];
                            addressArray = addressArray.concat(ipv4Address).concat(ipv6Address)
                        }
                        this.routeSelector = new RouteSelector(this.dns)
                        this.routeSelector.resetNextInetSocketAddress(addressArray)
                        parsedUrl = parsedUrl.replace(hostOrIp, this.routeSelector.next().add.uriHost)
                    })
                }
            } else {
                if (this.routeSelector.hasNextRoute()) {
                    parsedUrl = parsedUrl.replace(hostOrIp, this.routeSelector.next().add.uriHost)
                }
            }
        }

        if (this.forWebSocket) {
            let ws = chain.webSocket()
            return new Promise<Response>(function (resolve, reject) {
                if (!!ws) {
                    ws.connect(parsedUrl, (err, value) => {
                        const response = new Response()
                        if (!err) {
                            response.responseCode = 200
                            response.result = value.toString()
                            resolve(response);
                            hilog.info(0x0001, "ws---connect ", "success");

                        } else {
                            reject(err)
                            hilog.info(0x0001, "ws---connect fail---", JSON.stringify(value));
                        }
                    });
                }
            })
        }
        var headers = body ? Object.assign(userRequest.headers, body.mimes) : Object.assign({}, userRequest.headers);
        var options = {
            method: userRequest.method,
            header: headers,
            usingProtocol: (userRequest.protocol == http.HttpProtocol.HTTP2) ? http.HttpProtocol.HTTP2 : http.HttpProtocol.HTTP1_1
        };
        if (null != userRequest.params) {
            options['params'] = userRequest.params;
            var isbegin = true;
            Object.keys(userRequest.params).forEach(function (key) {
                Logger.info('Params key - ' + key + ', value - '
                + userRequest.params[key].toString());
                parsedUrl = parsedUrl.toString().concat(isbegin ? '?' : '&');
                parsedUrl = parsedUrl.concat(key).concat('=').concat(userRequest.params[key].toString());
                isbegin = false;
            })
        }
        var client = userRequest.client;
        if (client) {
            if (null != client.connectionTimeout) {
                options['connectTimeout'] = client.connectionTimeout;
                Logger.info('CallServerInterceptor : connectionTimeout:' + client.connectionTimeout);
            }
            if (null != client.readTimeout) {
                options['readTimeout'] = client.readTimeout;
                Logger.info('CallServerInterceptor : readTimeout:' + client.readTimeout);
            }
        }
        if (body && null != body.content) {
            options['extraData'] = body.content;
        }

        if (!!this.dns || userRequest.isDnsInterceptor) {
            let httpRequest = Http.createHttp();
            httpRequest.setCaData(userRequest.getCa())
            Logger.info('HTTP this.dns   CallServerInterceptor body = ' + body)
            Logger.info('HTTP this.dns   CallServerInterceptor options - ' + JSON.stringify(options));
            return new Promise<Response>(function (resolve, reject) {
                httpRequest.request(parsedUrl, userRequest.url.getUrl(), userRequest, options, (err, data) => {
                    if (!err) {
                        const response = new Response().newBuilder()
                            .setRequest(userRequest)
                            .setBody(JSON.stringify(data.result))
                            .setSentRequestAtMillis(sentRequestMillis)
                            .setReceivedResponseAtMillis(new Date().getTime())
                            .setHeader(JSON.stringify(data.header))
                            .setProtocol(Protocol.HTTP_1_1)
                            .setCode(data.responseCode)
                            .build()
                        resolve(response)
                    } else {
                        hilog.info(0x0001, "CallServerInterceptor err", JSON.stringify(err));
                        reject(err)
                    }
                    httpRequest.destroy()
                })
            })
            return
        }

        if ('UPLOAD' == userRequest.method) {
            Logger.info('CallServerInterceptor : Upload request - Parsed Url - ' + parsedUrl.toString());
            let config = {
                url: parsedUrl,
                header: headers,
                method: "POST",
                files: userRequest.files,
                data: userRequest.data,
            };
            return new Promise<Response>(function (resolve, reject) {
                request.uploadFile(userRequest.abilityContext, config).then((data) => {
                    const response = new Response()
                    response.uploadTask = data
                    resolve(response);
                }).catch((err) => {
                    reject(err)
                });
            })
        } else if ('CHUNK_UPLOAD' == userRequest.method) {
            Logger.info('CallServerInterceptor : chunk upload request - Parsed Url - ' + parsedUrl.toString());
            let that = this
            let requestData = userRequest.data
            let binaryFileChunkUpload = userRequest.BinaryFileChunkUpload == undefined ? undefined : userRequest.BinaryFileChunkUpload as BinaryFileChunkUpload
            if (binaryFileChunkUpload == undefined) {
                Logger.error('CallServerInterceptor : binaryFileChunkUpload is undefined');
                return
            }
            Logger.info('CallServerInterceptor: requestData is ' + JSON.stringify(requestData))
            this.uploadCallback = binaryFileChunkUpload.getUploadCallback()
            this.uploadProgress = binaryFileChunkUpload.getUploadProgress()
            this.chunkUploadDispatcher = binaryFileChunkUpload.getChunkUploadDispatcher()
            if (this.chunkUploadDispatcher == undefined) {
                Logger.error('CallServerInterceptor : this.chunkUploadDispatcher is undefined');
                return
            }
            Logger.info('CallServerInterceptor: chunkCount is ' + this.chunkUploadDispatcher.getChunkCount() + ', fileSize is ' + this.chunkUploadDispatcher.getFileSize())

            let header = new Map<string, any>()
            header.set(ConstantManager.CONTENT_TYPE, "multipart/form-data")

            let config = {
                url: parsedUrl,
                header: header,
                method: "POST",
                files: [],
                data: requestData
            }

            let chunkFileList = []
            let chunkedList = this.chunkUploadDispatcher.chunkedQueue
            let length: number = chunkedList.length;
            if (length == 0) {
                Logger.info('CallServerInterceptor: length == 0')
                let timeoutId = setTimeout(() => {
                    clearTimeout(timeoutId)
                    let dataList = this.chunkUploadDispatcher.chunkedQueue
                    let dataLength = dataList.length
                    if (dataLength == 0) {
                        Logger.error('CallServerInterceptor: chunk file failed')
                        return
                    } else if (dataLength >= that.maxUploadCount) {
                        chunkFileList = dataList.slice(0, that.maxUploadCount);
                        config.files = chunkFileList
                        this.uploadChunkFile(userRequest.abilityContext, config, 1)
                    } else {
                        chunkFileList = dataList.slice(0, dataLength);
                        config.files = chunkFileList
                        this.uploadChunkFile(userRequest.abilityContext, config, 1)
                    }
                }, 500)
            } else if (length >= that.maxUploadCount) {
                chunkFileList = chunkedList.slice(0, that.maxUploadCount);
                config.files = chunkFileList
                this.uploadChunkFile(userRequest.abilityContext, config, 1)
            } else {
                chunkFileList = chunkedList.slice(0, length);
                config.files = chunkFileList
                this.uploadChunkFile(userRequest.abilityContext, config, 1)
            }
        } else if ('DOWNLOAD' == userRequest.method) {
            let defaultFileDownloadDirectory = userRequest.abilityContext.filesDir;
            let fileName = '';
            var downloadRequestData = { url: parsedUrl, filePath: '' };
            if (headers) {
                downloadRequestData['header'] = headers;
            }
            if (userRequest.filePath) {
                defaultFileDownloadDirectory = userRequest.filePath;
            }
            if (userRequest.fileName) {
                fileName = userRequest.fileName;
            } else {
                const fileParts = parsedUrl.split('/');
                fileName = fileParts[fileParts.length-1];
            }

            downloadRequestData["filePath"] = defaultFileDownloadDirectory + "/" + fileName;
            Logger.info('downloadRequestData :  = ' + JSON.stringify(downloadRequestData))

            if (FileUtils.exist(downloadRequestData["filePath"])) {
                Logger.info('DOWNLOAD filePath exits :  = ' + downloadRequestData["filePath"])
                FileUtils.deleteFile(downloadRequestData["filePath"])
            }
            if (!downloadRequestData["enableMetered"]) { //  默认情况允许流量计费下载  非必传，适配半容器下载，不加该字段则任务失败
                downloadRequestData["enableMetered"] = true;
            }
            return new Promise<Response>(function (resolve, reject) {
                request.downloadFile(userRequest.abilityContext, downloadRequestData).then((downloadTask) => {
                    Logger.info('DOWNLOAD download starts :  = ' + downloadTask)
                    if (downloadTask) {
                        const response = new Response()
                        response.downloadTask = downloadTask
                        response.responseCode = HttpStatusCodes.HTTP_OK
                        resolve(response);
                    }
                }).catch((err) => {
                    Logger.info('download err :  = ' + JSON.stringify(err))
                    reject(err);
                })
            })
        } else if ('tlsSocket' == userRequest.method) {
            Logger.info('CallServerInterceptor tlsSocket request - ' + JSON.stringify(userRequest.url));
            userRequest.tlsRequst.setAddress(userRequest.url.getHost())
            userRequest.tlsRequst.setPort(userRequest.url.getPort())
            return new Promise<Response>(function (resolve, reject) {
                userRequest.tlsRequst.excute(userRequest.data, (errState, data) => {
                    if (!!!errState) {
                        const response = new Response()
                        response.responseCode = HttpStatusCodes.HTTP_OK
                        response.result = data
                        resolve(response)
                    } else {
                        reject(errState)
                    }
                })
            })
        } else {
            let httpRequest = http.createHttp();
            Logger.info('CallServerInterceptor parsedUrl body = ' + body)
            Logger.info('CallServerInterceptor parsedUrl options - ' + JSON.stringify(options));
            return new Promise<Response>(function (resolve, reject) {
                httpRequest.request(parsedUrl, options, (err, data) => {
                    if (!err) {

                        let resultData: string = ''
                        if (data.result instanceof ArrayBuffer) {
                            let resultBuffer: ArrayBuffer = data.result as ArrayBuffer;
                            let bufferContent = buffer.from(resultBuffer)
                            let unitString: Uint8Array = JSON.parse(JSON.stringify(bufferContent)).data;
                            resultData = Utils.Utf8ArrayToStr(unitString);
                        } else if (typeof data.result == 'object') {
                            resultData = JSON.stringify(data.result);
                        } else {
                            resultData = data.result.toString();
                        }

                        const response = new Response().newBuilder()
                            .setRequest(userRequest)
                            .setBody(resultData)
                            .setSentRequestAtMillis(sentRequestMillis)
                            .setReceivedResponseAtMillis(new Date().getTime())
                            .setHeader(JSON.stringify(data.header))
                            .setProtocol((userRequest.protocol == http.HttpProtocol.HTTP2) ? Protocol.HTTP_2 : Protocol.HTTP_1_1)
                            .setCode(data.responseCode)
                            .build()

                        if (null != userRequest.cookieJar) {
                            userRequest.cookieJar.saveFromResponse(data, parsedUrl, userRequest.cookieManager)
                        }

                        resolve(response)
                    } else {
                        hilog.info(0x0001, "CallServerInterceptor err", JSON.stringify(err));
                        reject(err)
                    }
                    httpRequest.destroy()
                })
            })
        }
    }

    async uploadChunkFile(abilityContext, uploadConfig, executeCount) {
        Logger.info('CallServerInterceptor: uploadChunkFile enter uploadConfig is ' + JSON.stringify(uploadConfig) + ', executeCount is ' + executeCount)
        let that = this;
        request.uploadFile(abilityContext, uploadConfig)
            .then(uploadTask => {
                Logger.info('CallServerInterceptor: uploading ');
                uploadTask.on('progress', function callback(uploadedSize, totalSize) {
                    that.chunkUploadDispatcher.updateUploadProgress(executeCount.toString(), uploadedSize)
                    if (that.uploadProgress) {
                        that.uploadProgress(that.chunkUploadDispatcher.getUploadProgress(), that.chunkUploadDispatcher.getFileSize())
                    }
                })

                uploadTask.on('complete', data => {
                    Logger.info('CallServerInterceptor: upload success executeCount is ' + executeCount);
                    that.chunkUploadDispatcher.uploadCompleteQueue = that.chunkUploadDispatcher.uploadCompleteQueue.concat(uploadConfig.files)
                    uploadTask.off('complete')
                    that.chunkUploadDispatcher.deleteChunkedFiles(abilityContext, uploadConfig.files)

                    Logger.info('CallServerInterceptor: chunkCount is ' + this.chunkUploadDispatcher.getChunkCount() +
                    ', that.chunkUploadDispatcher.uploadCompleteQueue.length is ' + that.chunkUploadDispatcher.uploadCompleteQueue.length +
                    ', that.chunkUploadDispatcher.uploadFailQueue.length is ' + that.chunkUploadDispatcher.uploadFailQueue.length +
                    ', that.chunkUploadDispatcher.chunkedQueue.length is ' + that.chunkUploadDispatcher.chunkedQueue.length);
                    that.prepareUpload(abilityContext, uploadConfig, executeCount)
                });

                uploadTask.on('fail', data => {
                    Logger.error('CallServerInterceptor: fail  executeCount is ' + executeCount);
                    uploadTask.off('fail')
                    that.handleUploadFail(abilityContext, uploadConfig, executeCount)
                })
            })
            .catch(err => {
                Logger.error('CallServerInterceptor: upload failed err is ' + JSON.stringify(err));
                that.handleUploadFail(abilityContext, uploadConfig, executeCount)
            })
    }

    prepareUpload(abilityContext, uploadConfig, executeCount) {
        Logger.info('CallServerInterceptor: prepareUpload enter executeCount is ' + executeCount + ', uploadConfig is ' + JSON.stringify(uploadConfig))
        if (this.chunkUploadDispatcher.getChunkCount() > (this.chunkUploadDispatcher.uploadCompleteQueue.length + this.chunkUploadDispatcher.uploadFailQueue.length)) {
            executeCount++
            uploadConfig.files = this.chunkUploadDispatcher.getWaitUploadFiles()
            this.uploadChunkFile(abilityContext, uploadConfig, executeCount)
        } else {
            if (this.chunkUploadDispatcher.uploadCompleteQueue.length == this.chunkUploadDispatcher.getChunkCount()) {
                Logger.info('CallServerInterceptor: upload complete')
                if (this.uploadCallback) {
                    this.uploadCallback(this.success, this.chunkUploadDispatcher.uploadFailQueue)
                }
                if (this.uploadProgress) {
                    this.uploadProgress(this.chunkUploadDispatcher.getFileSize(), this.chunkUploadDispatcher.getFileSize())
                }
                this.chunkUploadDispatcher.uploadCompleteQueue = []
                this.chunkUploadDispatcher.chunkedQueue = []
            } else if (this.chunkUploadDispatcher.uploadFailQueue.length + this.chunkUploadDispatcher.uploadCompleteQueue.length == this.chunkUploadDispatcher.getChunkCount()
            && this.chunkUploadDispatcher.uploadCompleteQueue.length < this.chunkUploadDispatcher.getChunkCount()) {
                Logger.info('CallServerInterceptor: upload finish this.chunkUploadDispatcher.uploadFailQueue is ' + JSON.stringify(this.chunkUploadDispatcher.uploadFailQueue))
                if (this.uploadCallback) {
                    this.uploadCallback(this.error, this.chunkUploadDispatcher.uploadFailQueue)
                }
                this.chunkUploadDispatcher.uploadCompleteQueue = []
                this.chunkUploadDispatcher.uploadFailQueue = []
                this.chunkUploadDispatcher.chunkedQueue = []
            }
        }
    }

    handleUploadFail(abilityContext, uploadConfig, executeCount) {
        this.chunkUploadDispatcher.uploadFailQueue = this.chunkUploadDispatcher.uploadFailQueue.concat(uploadConfig.files);
        this.chunkUploadDispatcher.updateUploadProgress(executeCount.toString(), 0)
        if (this.uploadProgress) {
            this.uploadProgress(this.chunkUploadDispatcher.getUploadProgress(), this.chunkUploadDispatcher.getFileSize())
        }
        this.prepareUpload(abilityContext, uploadConfig, executeCount)
    }
}
