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

import http from '@ohos.net.http';
import HttpClient from '../HttpClient'
import Request from '../Request'
import request from '@ohos.request';
import { gInterceptors } from '../utils/DefaultInterceptor';
import { Logger } from '../utils/Logger';
import BaseWorker from './BaseWorker';
import CallbackResponse from '../callback/CallbackResponse';
import FileUtils from '../utils/FileUtils';
import hilog from '@ohos.hilog';


var TAG = 'HttpRequestProcess:: ';

var _request = Symbol();
var _nodeRequest = Symbol();
var _response = Symbol();
var _data = Symbol();
var _timeout = Symbol();

var httpCarrier;

class Response {
    constructor() {

    }
}

class HttpRequestProcess extends BaseWorker {
    constructor($request, $id = '0', $priorityKey = 0) {
        super($id, $priorityKey);
        this[_request] = $request;
        this[_nodeRequest] = null;
        this[_response] = null;
        this[_data] = null;
    }

    get request() {
        return this[_request];
    }

    get data() {
        return this[_data];
    }

    get response() {
        return this[_response];
    }

    process(lInterceptors?, onComplete?, onError?) {
        super.process(onComplete, onError);
        this[_data] = null;
        this[_response] = null;

        var self = this;
        var lRequest = this.request;

        if (null != lInterceptors && lInterceptors.request.interceptorList
        && lInterceptors.request.interceptorList.length != 0) {
            lRequest = this.handleInterceptor(lInterceptors.request.interceptorList, lRequest);
        }

        if (null != gInterceptors && gInterceptors.request.interceptorList
        && gInterceptors.request.interceptorList.length != 0) {
            lRequest = this.handleInterceptor(gInterceptors.request.interceptorList, lRequest);
        }

        var body = lRequest.body;
        var parsedUrl = lRequest.url;

        httpCarrier = http.createHttp();

        var headers = body ? Object.assign(lRequest.headers, body.mimes) : Object.assign({}, lRequest.headers);

        var options = {
            method: lRequest.method,
            header: headers,
        };

        if (null != lRequest.params) {
            options['params'] = lRequest.params;
            var isbegin = true;
            Object.keys(lRequest.params).forEach(function (key) {
                Logger.info('Params key - ' + key + ', value - '
                + lRequest.params[key].toString());
                parsedUrl = parsedUrl.toString().concat(isbegin ? '?' : '&');
                parsedUrl = parsedUrl.concat(key).concat('=').concat(lRequest.params[key].toString());
                isbegin = false;
            })
        }

        var client = lRequest.client;
        if (client) {
            if (null != client.connectionTimeout) {
                options['connectTimeout'] = client.connectionTimeout;
                Logger.info('HttpRequestProcess : connectionTimeout:' + client.connectionTimeout);
            }

            if (null != client.readTimeout) {
                options['readTimeout'] = client.readTimeout;
                Logger.info('HttpRequestProcess : readTimeout:' + client.readTimeout);
            }
        }
        if (body && null != body.content) {
            options['extraData'] = body.content;
        }

        function requestOnResponse(res) {
            // from documentation
            // This properly handles multi-byte characters that would otherwise be potentially
            // mangled if you simply pulled the Buffers directly and called buf.toString(encoding) on them.
            // If you want to read the data as strings, always use this method.

            if (null != lRequest.cookieJar) {
                lRequest.cookieJar.saveFromResponse(res, parsedUrl, lRequest.cookieManager);
                Logger.info('saveFromResponse :  saveFromResponse - completed');

            }
            if (null != lRequest.convertor) {
                try {
                    Logger.info('HttpRequestProcess :  convertor -started ');
                    let body = lRequest.convertor.convertResponse(res.result);
                    let callbackresponse = new CallbackResponse(body, res);
                    self.notifyComplete(callbackresponse);
                } catch (err) {
                    hilog.info(0x0001, "HttpRequestProcess :  convertor -error", err);
                    let errMsg = { code: '', data: 'Response convertor failed to parse the response :' + res.result };
                    self.notifyError(errMsg);
                }
                return;
            }
            self[_response] = res;

            self[_data] = res.result;
            Logger.info('HttpRequestProcess :  notifyComplete -res ' + JSON.stringify(res));
            self.notifyComplete(self);
        }

        function uploadOnResponse(data) {
            self.notifyComplete(data);
        }

        function requestOnResponseDownload(res) {
            self.notifyComplete(res);
        }

        function requestOnError(err) {
            self.notifyError(err);
        }

        if ('UPLOAD' == lRequest.method) {
            Logger.info('HttpRequestProcess : Upload request - Parsed Url - ' + parsedUrl.toString());
            lRequest.files.forEach(async function (file) {
                Logger.info('HttpRequestProcess : File  = ' + JSON.stringify(file))
                let config = {
                    url: parsedUrl,
                    header: {},
                    method: "POST",
                    files: [file],
                    data: [lRequest.data],
                };
                this[_nodeRequest] = request.uploadFile(lRequest.abilityContext, config, (err, data) => {
                    if (err) {
                        requestOnError(err);
                        return;
                    }
                    uploadOnResponse(data)
                });
            })
        } else if ('DOWNLOAD' == lRequest.method) {
            let defaultFileDownloadDirectory = lRequest.abilityContext.filesDir;
            let fileName = '';
            var downloadRequestData = { url: parsedUrl, filePath: '' };
            if (headers) {
                downloadRequestData['header'] = headers;
            }
            if (lRequest.filePath) {
                defaultFileDownloadDirectory = lRequest.filePath;
            }
            if (lRequest.fileName) {
                fileName = lRequest.fileName;
            } else {
                const fileParts = parsedUrl.split('/');
                fileName = fileParts[fileParts.length-1];
            }
            downloadRequestData["filePath"] = defaultFileDownloadDirectory + "/" + fileName;
            Logger.info('downloadRequestData :  = ' + JSON.stringify(downloadRequestData))

            if (FileUtils.exist(downloadRequestData["filePath"])) {
                Logger.info('filePath exits :  = ' + downloadRequestData["filePath"])
                FileUtils.deleteFile(downloadRequestData["filePath"])
            }
            request.downloadFile(lRequest.abilityContext, downloadRequestData).then((downloadTask) => {
                Logger.info('download starts :  = ' + downloadTask)
                if (downloadTask) {
                    requestOnResponseDownload(downloadTask);
                }
            }).catch((err) => {
                hilog.info(0x0001, "download err :  =", JSON.stringify(err));
                requestOnError(err);
            })

        } else {

            Logger.info("------request--------------");
            Logger.info('Parsed Url - ' + parsedUrl.toString());
            Logger.info('options - ' + JSON.stringify(options));
            Logger.info("------request--------------");
            this[_nodeRequest] = httpCarrier.request(parsedUrl, options, (err, data) => {
                Logger.info('HttpProcessRequest : response received for lRequest.tag : '
                + lRequest.tag + ' lRequest.isSync: ' + lRequest.isSyncCall);
                let dispatcher = lRequest.client.dispatcher;
                let call = dispatcher.asyncCallObject;
                //Only calls from queue can be marked cancel, therefore only these calls can be cancelled
                if (!lRequest.isSyncCall
                && lRequest.tag === call.getRequest().tag
                && dispatcher.asyncCallObject.isCancelled()) {
                    Logger.info('HttpProcessRequest : call is marked cancelled');
                    let errMsg = { code: '', data: 'Request canceled by user' };
                    dispatcher.onError(errMsg, call);
                } else {
                    if (err == null) {
                        var lData = data;
                        Logger.info('HttpProcessRequest response received: ' + JSON.stringify(lData))
                        if (null != lInterceptors && lInterceptors.response.interceptorList
                        && lInterceptors.response.interceptorList.length != 0) {
                            lData = this.handleInterceptor(lInterceptors.response.interceptorList, lData);
                        }

                        if (null != gInterceptors && gInterceptors.response.interceptorList
                        && gInterceptors.response.interceptorList.length != 0) {
                            lData = this.handleInterceptor(gInterceptors.response.interceptorList, lData);
                        }

                        Logger.info('HttpProcessRequest after interceptors response received: ' + JSON.stringify(lData))

                        //Send redirect request, if required.
                        if (lRequest.followRedirects) {
                            Logger.info('HttpRequestProcess : Follow redirect is enabled');
                            self[_response] = lData;
                            var status = self.response.responseCode;
                            Logger.info('HttpRequestProcess : Response Code - ' + status);
                            if (status == 300 || status == 301 || status == 302
                            || status == 303 || status == 307 || status == 308) {
                                //300 - Multiple Choices, 301 - Moved Permanently, 302 - Moved Temporarily,
                                //303 - See Other, 307 - Temporary Redirect, 308 - Permanent Redirect.
                                var redirectMaxLimit = lRequest.redirectMaxLimit;
                                var redirectCount = lRequest.redirectionCount;
                                Logger.info('HttpRequestProcess : Redirect - ' + redirectCount + '/' + redirectMaxLimit);
                                if (redirectCount <= redirectMaxLimit) { //Default redirect max limit is 20.
                                    Logger.info('HttpRequestProcess : URL Moved');
                                    Logger.info('HttpRequestProcess : Header - ' + JSON.stringify(self.response.header));
                                    Logger.info('HttpRequestProcess : Location - ' + JSON.stringify(self.response.header.Location));
                                    if (self.response.header.Location == undefined) {
                                        Logger.info('HttpRequestProcess: Undefined location URL');
                                        requestOnResponse(lData);
                                        return;
                                    }
                                    var location = self.response.header.Location.toString();
                                    Logger.info('HttpRequestProcess : Redirecting to ' + location);
                                    var count = redirectCount + 1;
                                    //Send redirect request
                                    this.sendRequest(lRequest, location, count, onComplete, onError);
                                } else {
                                    Logger.info('HttpRequestProcess : URL Moved, Redirect max limit reached');
                                    requestOnResponse(lData);
                                }
                            } else {
                                requestOnResponse(lData);
                            }
                        } else {
                            requestOnResponse(lData);
                        }
                    } else {
                        //onError
                        if (lRequest.retryOnConnectionFailure && (err.data == 'IOException')) {
                            Logger.info('HttpRequestProcess: IOException occurred - ' + err.data);
                            var retryMaxLimit = lRequest.retryMaxLimit;
                            var retryCount = lRequest.retryConnectionCount;
                            Logger.info('HttpRequestProcess : Retry - ' + retryCount + '/' + retryMaxLimit);
                            if (retryCount <= retryMaxLimit) { //Default retry max limit is 20.
                                var count = retryCount + 1;
                                //Retry request on connection failure
                                this.sendRequest(lRequest, parsedUrl, count, onComplete, onError);
                            } else {
                                Logger.info('HttpRequestProcess : Retry max limit reached');
                                requestOnError(err);
                            }
                        }
                        else {
                            Logger.info('HttpRequestProcess: Exception occurred - ' + err.data);
                            requestOnError(err);
                        }
                    }
                }
            });
        }
    }

    sendRequest(request, location, count, onComplete, onError) {
        Logger.info('HttpProcessRequest sendRequest location : ' + location + ' count : ' + count +
        ' retryConnectionCount : ' + request.retryConnectionCount + ' request.redirectionCount :'
        + request.redirectionCount);
        request.url = location;
        request.retryConnectionCount = count;
        request.redirectionCount = count;
        request.client.dispatcher.executeFrameWorkCall(request, request.client.interceptors).then(onComplete, onError);
    }

    handleInterceptor(interceptorList, data) {
        var lData = data;

        if (null != interceptorList && interceptorList.length != 0) {
            var interceptorLength = interceptorList.length;
            for (var i = 0; i < interceptorLength; i++) {
                let iData = interceptorList[i](lData);
                if (null != iData) {
                    lData = iData;
                }
            }
        }
        return lData;
    }

    stop() {
        if (this[_nodeRequest]) this[_nodeRequest].abort();
    }

    dispose() {
        this.stop();
        this[_nodeRequest] = null;
        this[_request] = null;
    }

    close() {
        httpCarrier.destroy();
    }
}

export default HttpRequestProcess;