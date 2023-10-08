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

import util from '@ohos.util';
import RequestBody from '../RequestBody';
import Mime from './Mime';
import FormatUtils from '../utils/FormatUtils'
import { Logger } from '../utils/Logger'

const MULTIPART_MARK = '--';
const LF = '\r\n';

var _boundary = Symbol();
var _content = Symbol();
var _type = Symbol();

/**
 * a simple multi part builder. use it for <code>multipart</code> content types,
 * where the request body is composed of parts (bodies of their own)
 *
 */
class MultiPart {

    /**
     * @param {String} build the boundary of the parts
     */
    constructor(build) {
        if (!arguments.length) {
            build = new MultiPart.Builder();
        }
        this[_content] = build[_content];
        this[_type] = build[_type];
        this[_boundary] = build[_boundary];
    }

    static get Builder() {
        class Builder {
            constructor() {
                this[_content] = null;
                this[_type] = MultiPart.FORMDATA;
                this[_boundary] = '----------196f00b77b968397849367c61a2080';
            }

            addPart(body) {
                if (body == undefined || body == null || body == '') {
                    throw new Error('Incorrect body content');
                }
                var mimesStr = body.mimesToString();
                let headStr = MULTIPART_MARK + this[_boundary] + LF + mimesStr + LF;
                let headArr = FormatUtils.stringToUint8Array(headStr)
                let endArr = new Uint8Array([13, 10]);
                let dataBuffer;
                if (body.content && body.content.byteLength !== undefined) {
                    dataBuffer = new Uint8Array(body.content);
                }
                else if (typeof body.content === 'string') {
                    dataBuffer = FormatUtils.stringToUint8Array(body.content);
                }
                let totalLength = headArr.byteLength + dataBuffer.byteLength + endArr.byteLength
                let totalArr = new Uint8Array(totalLength)
                totalArr.set(headArr)
                totalArr.set(dataBuffer, headArr.byteLength)
                totalArr.set(endArr, (headArr.byteLength + dataBuffer.byteLength))
                if (this[_content])
                    this[_content] = FormatUtils.mergeUint8Array(this[_content], totalArr);
                else
                    this[_content] = FormatUtils.copyUint8Array(totalArr);
                return this;
            }

            type($type) {
                this[_type] = $type;
                return this;
            }

            boundary(value) {
                this[_boundary] = value;
                return this;
            }

            build() {
                return new MultiPart(this);
            }
        }

        return Builder;
    }

    /**
     * The 'mixed' subtype of 'multipart' is intended for use when the body
     * parts are independent and need to be bundled in a particular order. Any
     * 'multipart' subtypes that an implementation does not recognize must be
     * treated as being of subtype 'mixed'.
     */
    static get MIXED() {
        return 'multipart/mixed'
    }

    /**
     * The 'multipart/alternative' type is syntactically identical to
     * 'multipart/mixed', but the semantics are different. In particular, each
     * of the body parts is an 'alternative' version of the same information.
     */
    static get ALTERNATIVE() {
        return 'multipart/alternative'
    }

    /**
     * This type is syntactically identical to 'multipart/mixed', but the
     * semantics are different. In particular, in a digest, the default {@code
     * Content-Type} value for a body part is changed from 'text/plain' to
     * 'message/rfc822'.
     */
    static get DIGEST() {
        return 'multipart/digest'
    }

    /**
     * This type is syntactically identical to 'multipart/mixed', but the
     * semantics are different. In particular, in a parallel entity, the order
     * of body parts is not significant.
     */
    static get PARALLEL() {
        return 'multipart/parallel'
    }

    /**
     * The media-type multipart/form-data follows the rules of all multipart
     * MIME data streams as outlined in RFC 2046. In forms, there are a series
     * of fields to be supplied by the user who fills out the form. Each field
     * has a name. Within a given form, the names are unique.
     */
    static get FORMDATA() {
        return 'multipart/form-data'
    }

    /**
     * A multipart/related is used to indicate that each message part is a component of an aggregate whole.
     * It is for compound objects consisting of several inter-related components - proper display cannot be achieved
     * by individually displaying the constituent parts. The message consists of a root part (by default, the first)
     * which reference other parts inline, which may in turn reference other parts. Message parts are commonly
     * referenced by the 'Content-ID' part header. The syntax of a reference is unspecified and is instead dictated
     * by the encoding or protocol used in the part. One common usage of this subtype is to send a web page complete
     * with images in a single message. The root part would contain the HTML document, and use image tags to
     * reference images stored in the latter parts. Defined in RFC 2387
     */
    static get RELATED() {
        return 'multipart/related'
    }

    /**
     * build the request body
     *
     * @return {RequestBody} <code>RequestBody</code> instance
     */
    createRequestBody() {
        if (this[_content]) {
            let endStr = MULTIPART_MARK + this[_boundary] + MULTIPART_MARK + LF;
            this[_content] = FormatUtils.mergeUint8Array(this[_content], FormatUtils.stringToUint8Array(endStr));

        }

        Logger.info(FormatUtils.uint8ArrayTostring(this[_content]));

        return RequestBody.create(this[_content], new Mime.Builder().contentType(this[_type]
        + ';boundary=' + this[_boundary]).build().getMime());
    }
}

export default MultiPart;