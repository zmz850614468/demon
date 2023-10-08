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

import Protocol from '../protocols/Protocol';
import { Response } from '../response/Response'

class StatusLine {
    public static HTTP_TEMP_REDIRECT = 307;
    public static HTTP_PERM_REDIRECT = 308;
    public static HTTP_CONTINUE = 100;
    public protocol: string;
    public code: number;
    public message: string;

    constructor(protocol: string, code: number, message: string) {
        this.protocol = protocol;
        this.code = code;
        this.message = message;
    }

    public static get(response: Response): StatusLine {
        return new StatusLine(response.protocol, response.getCode(), response.message);
    }

    public static parse(statusLine: string): StatusLine {
        let codeStart: number
        let protocol: string

        if (statusLine.startsWith("HTTP/1.")) {
            if (statusLine.length < 9 || statusLine.charAt(8) != ' ') {
                throw new Error("Unexpected status line: " + statusLine);
            }
            let httpMinorVersion = Number(statusLine.charAt(7)) - Number('0');
            codeStart = 9;
            if (httpMinorVersion == 0) {
                protocol = Protocol.HTTP_1_0;
            } else if (httpMinorVersion == 1) {
                protocol = Protocol.HTTP_1_1;
            } else {
                throw new Error("Unexpected status line: " + statusLine);
            }
        } else if (statusLine.startsWith("ICY ")) {
            // Shoutcast uses ICY instead of "HTTP/1.0".
            protocol = Protocol.HTTP_1_0;
            codeStart = 4;
        } else {
            throw new Error("Unexpected status line: " + statusLine);
        }

        // Parse response code like "200". Always 3 digits.
        if (statusLine.length < codeStart + 3) {
            throw new Error("Unexpected status line: " + statusLine);
        }

        let code;
        try {
            code = Number.parseInt(statusLine.substring(codeStart, codeStart + 3));
        } catch (e) {
            throw new Error("Unexpected status line: " + statusLine);
        }

        let message = "";

        if (statusLine.length > codeStart + 3) {
            if (statusLine.charAt(codeStart + 3) != ' ') {
                throw new Error("Unexpected status line: " + statusLine);
            }
            message = statusLine.substring(codeStart + 4);
        }

        return new StatusLine(protocol, code, message);

    }

    public toString(): string {
        let result = ''
        result = result + (this.protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1")
        + " " + this.code
        if (this.message != null) {
            result = result + ' ' + this.message
        }
        return result;
    }
}

export default StatusLine