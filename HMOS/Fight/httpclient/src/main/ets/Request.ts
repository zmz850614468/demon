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

import { Logger } from './utils/Logger'
import { HttpUrl } from './HttpUrl';
import CacheControl from './cache/CacheControl';
import ConstantManager from './ConstantManager'
import FileUpload from './builders/FileUpload';
import BinaryFileChunkUpload from './builders/BinaryFileChunkUpload';

var _method = Symbol();
var _url = Symbol();
var _tag = Symbol();
var _body = Symbol();
var _files = Symbol();
var _data = Symbol();
var _headers = Symbol();
var _ca = Symbol();
var _params = Symbol();
var _queryParams = Symbol();
var _classResponse = Symbol();
var _dataAux = Symbol();
var _flagReturnRequestAsResponse = Symbol();
var _responseEncoding = Symbol();
var _connectTimeout = Symbol();
var _readTimeout = Symbol();
var _filePath = Symbol();
var _fileName = Symbol();
var _followRedirects = Symbol();
var _retryOnConnectionFailure = Symbol();
var _followSslRedirects = Symbol();
var _retryMaxLimit = Symbol();
var _redirectMaxLimit = Symbol();
var _redirectCount = Symbol();
var _retryCount = Symbol();
var _client = Symbol();
var _cookieJar = Symbol();
var _cookieManager = Symbol();
var _isSyncCall = Symbol();
var _debugMode = Symbol();
var _userAgent = Symbol();
var _contentType = Symbol();
var _convertorType = Symbol();
var _abilityContext = Symbol();
var _debugCode = Symbol();
var _debugUrl = Symbol();
var _requiredBodyObjects = Symbol(); // 需要的请求实体对象
var _isCustomRequest = Symbol();
var _tlsRequst = Symbol();
var _cacheControl = Symbol();
var _gzipBodyNeedBuffer = Symbol();
var _protocol = Symbol();
var _isDnsInterceptor = Symbol();
var _binaryFileChunkUpload = Symbol();

class Request {
    constructor(build) {
        if (!arguments.length) {
            build = new Request.Builder();
        }
        this[_method] = build[_method];
        this[_ca] = build[_ca];
        this[_url] = build[_url];
        this[_isDnsInterceptor] = build[_isDnsInterceptor];
        this[_tag] = build[_tag];
        this[_body] = build[_body];
        this[_files] = build[_files];
        this[_data] = build[_data];
        this[_headers] = build[_headers];
        this[_params] = build[_params];
        this[_queryParams] = build[_queryParams];
        this[_classResponse] = build[_classResponse];
        this[_dataAux] = build[_dataAux];
        this[_flagReturnRequestAsResponse] = build[_flagReturnRequestAsResponse];
        this[_responseEncoding] = build[_responseEncoding];
        this[_connectTimeout] = build[_connectTimeout];
        this[_readTimeout] = build[_readTimeout];
        this[_filePath] = build[_filePath];
        this[_fileName] = build[_fileName];
        this[_followRedirects] = build[_followRedirects];
        this[_retryOnConnectionFailure] = build[_retryOnConnectionFailure];
        this[_followSslRedirects] = build[_followSslRedirects];
        this[_retryMaxLimit] = build[_retryMaxLimit];
        this[_redirectMaxLimit] = build[_redirectMaxLimit];
        this[_redirectCount] = build[_redirectCount];
        this[_retryCount] = build[_retryCount];
        this[_cookieJar] = build[_cookieJar];
        this[_cookieManager] = build[_cookieManager];
        this[_convertorType] = build[_convertorType];
        this[_abilityContext] = build[_abilityContext];
        this[_debugCode] = build[_debugCode];
        this[_debugUrl] = build[_debugUrl];
        this[_requiredBodyObjects] = build[_requiredBodyObjects];
        this[_cacheControl] = build[_cacheControl]
        this[_isCustomRequest] = build[_isCustomRequest];
        this[_tlsRequst] = build[_tlsRequst];
        this[_gzipBodyNeedBuffer] = build[_gzipBodyNeedBuffer];
        this[_protocol] = build[_protocol];
        this[_binaryFileChunkUpload] = build[_binaryFileChunkUpload] as BinaryFileChunkUpload;
    }

    static get Builder() {
        class Builder {
            constructor(request?: Request) {
                if (!!request) {
                    this[_url] = request.url.getUrl()
                    this[_method] = request.method
                    this[_body] = request.body
                    this[_tag] = request.tag
                    this[_headers] = request.headers
                    this[_params] = request.params
                    this[_ca] = request.getCa()
                    return
                }
                this[_method] = '';
                this[_ca] = '';
                this[_url] = null;
                this[_isDnsInterceptor] = false;
                this[_tag] = new Date().getTime() + '';
                this[_body] = null;
                this[_files] = null;
                this[_data] = null;
                this[_headers] = {};
                this[_queryParams] = {};
                this[_params] = {};
                this[_classResponse] = null;
                this[_dataAux] = null;
                this[_flagReturnRequestAsResponse] = false;
                this[_responseEncoding] = 'utf8';
                this[_filePath] = null;
                this[_fileName] = null;
                this[_followRedirects] = true;
                this[_retryOnConnectionFailure] = true;
                this[_followSslRedirects] = true;
                this[_retryMaxLimit] = 20;
                this[_redirectMaxLimit] = 20;
                this[_redirectCount] = 1;
                this[_retryCount] = 1;
                this[_cookieJar] = null;
                this[_cookieManager] = null;
                this[_debugMode] = true;
                this[_userAgent] = '';
                this[_contentType] = '';
                this[_convertorType] = null;
                this[_requiredBodyObjects] = null;
                this[_cacheControl] = null;
                this[_isCustomRequest] = false; // 默认不是自定义请求
                this[_tlsRequst] = null;
                this[_gzipBodyNeedBuffer] = false;
                this[_protocol] = 'HTTP1_1';
                this[_binaryFileChunkUpload] = null;
            }

            setAbilityContext(abilityContext) {
                this[_abilityContext] = abilityContext;
                return this;
            }

            convertor(convertorType) {
                this[_convertorType] = convertorType;
                return this;
            }

            dnsInterceptor() {
                this[_isDnsInterceptor] = true
            }

            setCookieJar(cookieJar) {
                this[_cookieJar] = cookieJar;
                return this;
            }

            setCookieManager(cookieManager) {
                this[_cookieManager] = cookieManager;
                return this;
            }

            retryOnConnectionFailure(isRetryOnConnectionFailure) {
                this[_retryOnConnectionFailure] = isRetryOnConnectionFailure;
                return this;
            }

            retryMaxLimit(maxValue) {
                maxValue = (maxValue == undefined || maxValue == null || maxValue == ''
                || maxValue < 0 || maxValue > 20) ? this[_retryMaxLimit] : maxValue;
                this[_retryMaxLimit] = maxValue;
                return this;
            }

            retryConnectionCount(count) {
                this[_retryCount] = count;
                return this;
            }

            followRedirects(aFollowRedirects) {
                this[_followRedirects] = aFollowRedirects;
                return this;
            }

            followSslRedirects(aFollowSslRedirects) {
                this[_followSslRedirects] = aFollowSslRedirects;
                return this;
            }

            redirectMaxLimit(maxValue) {
                maxValue = (maxValue == undefined || maxValue == null || maxValue == ''
                || maxValue < 0 || maxValue > 20) ? this[_redirectMaxLimit] : maxValue;
                this[_redirectMaxLimit] = maxValue;
                return this;
            }

            redirectionCount(count) {
                this[_redirectCount] = count;
                return this;
            }

            dataAux(value) {
                this[_dataAux] = value;
                return this;
            }

            headers(value) {
                this[_headers] = value;
                return this;
            }

            cacheControl(cacheControl) {
                let value = cacheControl.toString();
                if (value.isEmpty()) return this.removeHeader("Cache-Control");
                return this
            }

            removeHeader(name) {
                this[_headers].removeHeader(name)
                return this;
            }

            addHeader(key, value) {
                this[_headers][key] = value;
                return this;
            }

            setDefaultUserAgent(value) {
                this[_headers]['user-agent'] = (value == undefined || value == null || value == ''
                    ? this[_userAgent] : value);
                return this;
            }

            setDefaultContentType(value) {
                this[_headers][ConstantManager.CONTENT_TYPE] = (value == undefined || value == null || value == ''
                    ? this[_contentType] : value);
                return this;
            }

            body(value) {
                if (value instanceof BinaryFileChunkUpload) {
                    this[_method] = 'CHUNK_UPLOAD';
                    this[_data] = value.getData() && value.getData() !== undefined ? value.getData() : null;
                    this[_binaryFileChunkUpload] = value
                } else if (value instanceof FileUpload) {
                    this[_method] = 'UPLOAD';
                    this[_files] = value.getFile() && value.getFile() !== undefined ? value.getFile() : null;
                    this[_data] = value.getData() && value.getData() !== undefined ? value.getData() : null;
                } else {
                    this[_body] = value;
                }
                return this;
            }

            url(value) {
                if (value == undefined || value == null || value == '') {
                    throw new Error('Incorrect URL parameters');
                }
                this[_url] = HttpUrl.get(value);
                return this;
            }

            tag(value) {
                this[_tag] = value;
                return this;
            }

            method(value) {
                this[_method] = value;
                return this;
            }

            ca(value) {
                this[_ca] = value
                return this;
            }

            queryParams(value) {
                this[_queryParams] = value;
                return this;
            }

            query(key, value) {
                this[_queryParams][key] = value;
                return this;
            }

            params(key, value) {
                this[_params][key] = value;
                return this;
            }

            addFileParams(files, data) {
                this[_method] = 'UPLOAD';
                this[_files] = files && files !== undefined ? files : null;
                this[_data] = data && data !== undefined ? data : null;
                return this;
            }

            responseEncoding(value) {
                this[_responseEncoding] = value;
                return this;
            }

            bufferResponse() {
                this[_responseEncoding] = null;
                return this;
            }

            textResponse() {
                this[_responseEncoding] = 'utf8';
                return this;
            }

            classResponse(value) {
                this[_classResponse] = value;
                return this;
            }

            setFileName(name) {
                this[_fileName] = name;
                return this;
            }

            setTlsRequst(value) {
                this[_method] = 'tlsSocket';
                this[_tlsRequst] = value;
                return this;
            }

            get(...arg: any[]) {
                if (arg.length > 0 && (arg[0] == null || arg[0] == '' || arg[0] == undefined)) {
                    throw new Error('Incorrect GET URL parameters');
                }
                var url = arg.length > 0 ? arg[0] : null;
                this[_method] = 'GET';
                if (url) {
                    this[_url] = HttpUrl.get(url);
                }
                return this;
            }

            put(body) {
                if (body == undefined || body == null || body == '') {
                    throw new Error('Incorrect PUT body parameters');
                }
                this[_method] = 'PUT';
                this[_body] = body;
                return this;
            }

            delete() {
                this[_method] = 'DELETE';
                return this;
            }

            head() {
                this[_method] = 'HEAD';
                return this;
            }

            options() {
                this[_method] = 'OPTIONS';
                return this;
            }

            post(...arg: any[]) {
                if (arg.length > 0 && (arg[0] == null || arg[0] == '' || arg[0] == undefined)) {
                    throw new Error('Incorrect POST body parameters');
                }
                var body = arg.length > 0 ? arg[0] : null;

                this[_method] = 'POST';
                if (body)
                    this[_body] = body;
                return this;
            }

            upload(files, data) {
                this[_method] = 'UPLOAD';
                this[_files] = files && files !== undefined ? files : null;
                this[_data] = data && data !== undefined ? data : null;
                return this;
            }

            download(...arg: any[]) {
                this[_method] = 'DOWNLOAD';
                var url = arg.length >= 1 && arg[0] !== undefined ? arg[0] : null;
                if (url) {
                    this[_url] = HttpUrl.get(url);
                }
                var fileName = arg.length >= 2 && arg[1] !== undefined ? arg[1] : null;
                if (fileName) {
                    Logger.info("Request: fileName : " + fileName)
                    this[_fileName] = fileName;
                }
                return this;
            }

            trace() {
                this[_method] = 'TRACE';
                return this;
            }

            connect() {
                this[_method] = 'CONNECT';
                return this;
            }

            request(method) {
                var body = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
                this[_method] = method;
                if (body) {
                    this[_body] = body;
                }
                return this;
            }

            setDefaultConfig(defaultConfig) {
                //this.setdebugmode(defaultConfig.debug_mode)
                this.setDefaultContentType(defaultConfig.content_type)
                this.setDefaultUserAgent(defaultConfig.user_agent)
                return this;
            }
            /**
             * Set custom request type object
             * @param value :Custom Type Object
             * @param flag :Is it a custom request
             */
            setEntryObj(value, flag?: boolean) {
                this[_requiredBodyObjects] = value;
                this[_isCustomRequest] = flag;
                return this;
            }

            setGzipBodyNeedBuffer(value) {
                this[_gzipBodyNeedBuffer] = value
                return this;
            }

            setProtocol(protocol) {
                this[_protocol] = protocol;
                return this;
            }

            build() {
                Logger.info('Request: Builder.build() invoked');
                if (!this[_url]) {
                    throw new Error('Incorrect  URL parameters');
                }
                if (this[_cookieJar] != null) {
                    Logger.info('Request: Build cookie... ');
                    let cookieString = this[_cookieJar].loadForRequest(this[_url].getUrl());
                    if (cookieString) {
                        Logger.info('Request: cookies loaded:' + cookieString);
                        this[_headers]['cookie'] = cookieString;
                    }
                }
                return new Request(this);
            }

            debugCode(debugCode) {
                this[_debugCode] = debugCode;
                return this;
            }

            debugUrl(debugUrl) {
                this[_debugUrl] = debugUrl;
                return this;
            }
        }

        return Builder;
    }

    get BinaryFileChunkUpload() {
        return this[_binaryFileChunkUpload]
    }

    get dataAux() {
        return this[_dataAux];
    }

    get abilityContext() {
        return this[_abilityContext];
    }

    get headers() {
        return this[_headers];
    }

    set headers(value) {
        this[_headers] = value;
    }

    get body() {
        return this[_body];
    }

    set body(value) {
        if (value instanceof BinaryFileChunkUpload) {
            this[_method] = 'CHUNK_UPLOAD';
            this[_data] = value.getData() && value.getData() !== undefined ? value.getData() : null;
            this[_binaryFileChunkUpload] = value
        } else if (value instanceof FileUpload) {
            this[_method] = 'UPLOAD';
            this[_files] = value.getFile() && value.getFile() !== undefined ? value.getFile() : null;
            this[_data] = value.getData() && value.getData() !== undefined ? value.getData() : null;
        } else {
            this[_body] = value;
        }
    }

    get url() {
        return this[_url];
    }

    set url(urlString) {
        this[_url] = HttpUrl.get(urlString);
    }

    get tag() {
        return this[_tag];
    }

    get method() {
        return this[_method];
    }

    set method(method) {
        this[_method] = method;
    }

    get queryParams() {
        return this[_queryParams];
    }

    get params() {
        return this[_params];
    }

    get responseEncoding() {
        return this[_responseEncoding];
    }

    get classResponse() {
        return this[_classResponse];
    }

    get filePath() {
        return this[_filePath];
    }

    get fileName() {
        return this[_fileName];
    }

    get filesDir() {
        return this[_files];
    }

    get followRedirects() {
        return this[_followRedirects];
    }

    get retryOnConnectionFailure() {
        return this[_retryOnConnectionFailure];
    }

    get followSslRedirects() {
        return this[_followSslRedirects];
    }

    get retryMaxLimit() {
        return this[_retryMaxLimit];
    }

    get retryConnectionCount() {
        return this[_retryCount];
    }

    set retryConnectionCount(count) {
        this[_retryCount] = count;
    }

    get redirectMaxLimit() {
        return this[_redirectMaxLimit];
    }

    get redirectionCount() {
        return this[_redirectCount];
    }

    set redirectionCount(count) {
        this[_redirectCount] = count;
    }

    get files() {
        return this[_files];
    }

    get data() {
        return this[_data];
    }

    get client() {
        return this[_client];
    }

    set client(httpClient) {
        this[_client] = httpClient;
    }

    get isSyncCall() {
        return this[_isSyncCall];
    }

    set isSyncCall(isSyncCallObject) {
        this[_isSyncCall] = isSyncCallObject;
    }

    get cookieJar() {
        return this[_cookieJar];
    }

    get cookieManager() {
        return this[_cookieManager];
    }

    get convertor() {
        return this[_convertorType];
    }

    get debugCode() {
        return this[_debugCode];
    }

    get debugUrl() {
        return this[_debugUrl];
    }

    get cacheControl() {
        let result = this[_cacheControl]
        if (result != null && result != undefined) {
            return result
        } else {
            let control = CacheControl.parse(JSON.stringify(this[_headers]))
            result = this[_cacheControl] = control
            return result
        }
    }

    get tlsRequst() {
        return this[_tlsRequst]
    }

    set tlsRequst(value) {
        this[_tlsRequst] = value
    }

    get gzipBodyNeedBuffer() {
        return this[_gzipBodyNeedBuffer]
    }

    set gzipBodyNeedBuffer(value) {
        this[_gzipBodyNeedBuffer] = value
    }

    get protocol() {
        return this[_protocol];
    }

    get requiredBodyObjects() {
        return this[_requiredBodyObjects];
    }

    set requiredBodyObjects(value) {
        this[_requiredBodyObjects] = value;
    }

    get isCustomRequestFlag() {
        return this[_isCustomRequest];
    }

    get isDnsInterceptor() {
        return this[_isDnsInterceptor]
    }

    addHeader(key, value) {
        this[_headers][key] = value;
    }

    getHeader(key: string) {
        return this[_headers][key] == null ? null : this[_headers][key]
    }

    setData(data) {
        this[_data] = data;
        return this
    }

    getCa() {
        return this[_ca]
    }

    setCacheControl(cacheControl) {
        this[_cacheControl] = cacheControl
    }

    newBuilder() {
        return new Request.Builder(this)
    }

    request(method) {
        var body = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
        this[_method] = method;
        if (body) {
            this[_body] = body;
        }
    }

    removeHeader(key) {
        this[_headers].delete(key)
    }

    setHttpUrl(url: HttpUrl) {
        if (url == null) throw Error("url == null");
        this[_url] = url;
    }
}

export default Request;