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

import MediaType from './MediaType';
import ResponseBody from './ResponseBody';

class RealResponseBody extends ResponseBody {
    private contentTypeString: string
    private _contentLength: number
    private _source: string

    constructor(contentTypeString: string, contentLength: number, source: string) {
        super()
        this.contentTypeString = contentTypeString;
        this._contentLength = contentLength;
        this._source = source;
    }

    contentType(): MediaType {
        return this.contentTypeString != null ? MediaType.parse(this.contentTypeString) : null;
    }

    contentLength(): number {
        return this._contentLength;
    }

    source(): string {
        return this._source
    }
}

export default RealResponseBody