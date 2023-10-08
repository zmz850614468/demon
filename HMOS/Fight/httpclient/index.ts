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

import Request from './src/main/ets/Request'
import HttpClient from './src/main/ets/HttpClient'
import RequestBody from './src/main/ets/RequestBody';
import { TimeUnit } from './src/main/ets/utils/Utils';
import FileUpload from './src/main/ets/builders/FileUpload';
import BinaryFileChunkUpload from './src/main/ets/builders/BinaryFileChunkUpload';
import FormEncoder from './src/main/ets/builders/FormEncoder';
import Mime from './src/main/ets/builders/Mime';
import MultiPart from './src/main/ets/builders/MultiPart';
import Cookie from './src/main/ets/cookies/Cookie';
import CookieJar from './src/main/ets/cookies/CookieJar';
import CookieStore from './src/main/ets/cookies/CookieStore';
import { CookiePolicy } from './src/main/ets/cookies/httpcookieutils';
import CookieManager from './src/main/ets/cookies/CookieManager';
import Route from './src/main/ets/core/Route';
import DnsResolve from './src/main/ets/dns/DnsResolve'
import gZipUtil from './src/main/ets/utils/gZipUtil';
import JsonCallback from './src/main/ets/callback/JsonCallback';
import StringCallback from './src/main/ets/callback/StringCallback';
import ByteStringCallback from './src/main/ets/callback/ByteStringCallback';
import Cache from './src/main/ets/cache/Cache'
import CacheControl from './src/main/ets/cache/CacheControl'

/**
 * httpclient
 * @since 6
 * @deprecated since 9
 */
var httpclient = {
  'Request': Request,
  'HttpClient': HttpClient,
  'RequestBody': RequestBody,
  'Mime': Mime,
  'MultiPart': MultiPart,
  'FileUpload': FileUpload,
  'BinaryFileChunkUpload': BinaryFileChunkUpload,
  'FormEncoder': FormEncoder,
  'TimeUnit': TimeUnit,
  'Cookie': Cookie,
  'CookieJar': CookieJar,
  'CookieStore': CookieStore,
  'CookiePolicy': CookiePolicy,
  'CookieManager': CookieManager,
  'gZipUtil': gZipUtil,
  'Route': Route,
  'DnsResolve': DnsResolve,
  'JsonCallback': JsonCallback,
  'StringCallback': StringCallback,
  'ByteStringCallback': ByteStringCallback,
  'Cache': Cache,
  'CacheControl': CacheControl
}

export { default as RequestBody } from './src/main/ets/RequestBody';

export { default as Cookie } from './src/main/ets/cookies/Cookie';

export { default as CookieJar } from './src/main/ets/cookies/CookieJar';

export { default as CookieStore } from './src/main/ets/cookies/CookieStore';

export { default as CookieManager } from './src/main/ets/cookies/CookieManager';

export { default as Route } from './src/main/ets/core/Route';

export { default as DnsResolve } from './src/main/ets/dns/DnsResolve';

export { default as gZipUtil } from './src/main/ets/utils/gZipUtil';

export { default as JsonCallback } from './src/main/ets/callback/JsonCallback';

export { default as StringCallback } from './src/main/ets/callback/StringCallback';

export { default as ByteStringCallback } from './src/main/ets/callback/ByteStringCallback';

export { CookiePolicy } from './src/main/ets/cookies/httpcookieutils';

export { default as FileUpload } from './src/main/ets/builders/FileUpload';

export { default as BinaryFileChunkUpload } from './src/main/ets/builders/BinaryFileChunkUpload';

export { default as FormEncoder } from './src/main/ets/builders/FormEncoder';

export { default as Mime } from './src/main/ets/builders/Mime';

export { default as MultiPart } from './src/main/ets/builders/MultiPart';

export { TimeUnit } from './src/main/ets/utils/Utils';

export { RealWebSocket } from './src/main/ets/RealWebSocket'

export { WebSocketListener } from './src/main/ets/WebSocketListener';

export { WebSocket, Factory } from './src/main/ets/WebSocket';

export { Dns } from './src/main/ets/Dns';

export { Interceptor, Chain } from './src/main/ets/Interceptor';

export { default as Credentials } from './src/main/ets/authenticator/Credentials';

export { default as Authenticator } from './src/main/ets/authenticator/Authenticator';

export { default as NetAuthenticator } from './src/main/ets/authenticator/NetAuthenticator';

export { default as Challenge } from './src/main/ets/authenticator/Challenge';

export { default as Headers } from './src/main/ets/core/Headers';

export { default as HttpHeaders } from './src/main/ets/core/HttpHeaders';

export { Response } from './src//main/ets/response/Response';

export { default as Request } from './src/main/ets/Request';

export { Utils } from './src/main/ets/utils/Utils';

export { default as RealTLSSocket } from './src/main/ets/tls/RealTLSSocket';

export { default as OkHostnameVerifier } from './src/main/ets/tls/OkHostnameVerifier';

export { default as StringUtil } from './src/main/ets/utils/StringUtil';

export { TLSSocketListener } from './src/main/ets/tls/TLSSocketListener';

export { default as Cache } from './src/main/ets/cache/Cache'

export { default as CacheControl } from './src/main/ets/cache/CacheControl'

export { default as Protocol } from './src/main/ets/protocols/Protocol'

export { Logger } from './src/main/ets/utils/Logger'

export { default as HttpClient } from './src/main/ets/HttpClient'


/**
 * httpclient
 * @since 6
 * @deprecated since 9
 */
export default httpclient;