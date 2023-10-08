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

//The URL you passed to libcurl used a protocol that this libcurl does not support. The support might be a compile-time option that you did not use, it can be a misspelled protocol string or just a protocol libcurl has no code for
export const CURLE_UNSUPPORTED_PROTOCOL = 1

//A problem was detected in the HTTP2 framing layer. This is somewhat generic and can be one out of several problems, see the error buffer for details
export const CURLE_HTTP2 = 16

//Operation timeout. The specified time-out period was reached according to the conditions
export const CURLE_OPERATION_TIMEDOUT = 28

//problem with the local client certificate
export const CURLE_SSL_CERTPROBLEM = 58

//The remote server's SSL certificate or SSH fingerprint was deemed not OK
export const CURLE_PEER_FAILED_VERIFICATION = 60

//Stream error in the HTTP/2 framing layer
export const CURLE_REMOTE_FILE_NOT_FOUND = 78

//Stream error in the HTTP/2 framing layer
export const CURLE_HTTP2_STREAM = 92

