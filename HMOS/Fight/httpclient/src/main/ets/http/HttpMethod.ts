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

export class HttpMethod {
    static invalidatesCache(method: string): boolean {
        return method == "POST"
        || method == "PATCH"
        || method == "PUT"
        || method == "DELETE"
        || method == "MOVE"; // WebDAV
    }

    static requiresRequestBody(method: string): boolean {
        return method == "POST"
        || method == "PUT"
        || method == "PATCH"
        || method == "PROPPATCH" // WebDAV
        || method == "REPORT"; // CalDAV/CardDAV (defined in WebDAV Versioning)
    }

    static permitsRequestBody(method: string): boolean {
        return !(method == "GET" || method == "HEAD");
    }

    static redirectsWithBody(method: string): boolean {
        return method == "PROPFIND"; // (WebDAV) redirects should also maintain the request body
    }

    static redirectsToGet(method: string): boolean {
        // All requests but PROPFIND should redirect to a GET request.
        return method != "PROPFIND";
    }
}
