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

import { Response } from '../response/Response';
import fs from '@ohos.file.fs';
import MediaType from './MediaType'

let contentType: MediaType
let contentLength: number
let content: string

abstract class ResponseBody {
    private read: any

    public static create(contentType: MediaType, content: string): ResponseBody {
        let charset = 'UTF_8';
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = 'UTF_8';
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        contentType = contentType
        content = content
        contentLength = content.length

        return new ResponseBodyImpl()
    }

    public abstract contentType(): MediaType

    /**
     * Returns the number of bytes in that will returned by {@link #bytes}, or {@link #byteStream}, or
     * -1 if unknown.
     */
    public abstract contentLength(): number;

    public byteStream() {
        return this.source();
    }

    public abstract source(): string;

    /**
     * Returns the response as a byte array.
     *
     * <p>This method loads entire response body into memory. If the response body is very large this
     * may trigger an {@link OutOfMemoryError}. Prefer to stream the response body if this is a
     * possibility for your response.
     */
    public bytes(): string {
        let contentLength = this.contentLength();
        if (contentLength > Number.MAX_VALUE) {
            throw new Error("Cannot buffer entire body for content length: " + contentLength)
        }

        let bytes: string;
        let source
        try {
            let encodedString = String.fromCodePoint.apply(null, new Uint8Array(source.buffer))
            bytes = decodeURIComponent(escape(encodedString));
        } catch (e) {
            bytes = source;
        }

        if (contentLength != -1 && contentLength != bytes.length) {
            throw new Error("Content-Length ("
            + contentLength
            + ") and stream length ("
            + bytes.length
            + ") disagree");
        }
        return bytes;
    }

    charStream() {
        let r = this.read;
            r != null ? r : new BomAwareReader(this.source(), this.charset())
    }

    charset() {
        let contentType = this.contentType()
        return contentType != null ? 'UTF_8' : 'UTF_8'
    }
}

class BomAwareReader {
    private source: string
    private charset: string
    private closed: boolean
    private delegate

    constructor(source: string, charset: string) {
        this.source = source;
        this.charset = charset;
    }

    read(cbuf: string[], off: number, len: number): string | Error {
        if (this.closed) return new Error("Stream closed");

        let delegate = this.delegate;
        if (delegate == null) {
            let charset = this.charset;
            delegate = this.delegate
        }
        return cbuf.toString()
    }

    close() {
        this.closed = true;
        if (this.delegate != null) {
            this.delegate = null;
        } else {
            ArrayBuffer = null;
        }
    }
}


class ResponseBodyImpl extends ResponseBody {
    contentType(): MediaType {
        return contentType;
    }

    contentLength(): number {
        return contentLength
    }

    source(): string {
        return content;
    }
}


export default ResponseBody