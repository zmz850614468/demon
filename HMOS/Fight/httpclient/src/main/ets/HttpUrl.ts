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

import Url from '@ohos.url'
import connection from '@ohos.net.connection'
import { Utils } from './utils/Utils';

export class HttpUrl {
    url: string
    scheme: string
    host: string
    port: number
    address: Array<string>

    constructor(builder: Builder) {
        this.url = builder.url
        this.scheme = builder.scheme
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.address = builder.address;
    }

    public static get(url: string): HttpUrl {
        return new Builder().parse(null, url).build();
    }

    getUrl(): string {
        return this.url
    }

    getHost(): string {
        return this.host
    }

    getPort(): number {
        return this.port
    }

    getScheme(): string {
        return this.scheme
    }

    getAddress(): Array<string> {
        return this.address
    }

    /**
     * Returns the URL that would be retrieved by following {@code link} from this URL, or null if
     * the resulting URL is not well-formed.
     */
    resolve(link: string): HttpUrl {
        let builder: Builder = this.newBuilder(link);
        return builder != null ? builder.build() : null;
    }

    /**
     * Returns a builder for the URL that would be retrieved by following `link` from this URL,
     * or null if the resulting URL is not well-formed.
     */
    newBuilder(link: string): Builder {
        return new Builder().parse(this, link)
    }
}

class Builder {
    url: string
    scheme: string
    host: string
    port = -1
    address = new Array<string>()

    setPort(port: number): Builder {
        if (port <= 0 || port > 65535) throw Error("unexpected port: " + port);
        this.port = port;
        return this;
    }

    effectivePort(): number {
        return this.port != 0 ? this.port : this.defaultPort(this.scheme);
    }

    /**
     * Returns 80 if {@code scheme.equals("http")}, 443 if {@code scheme.equals("https")} and -1
     * otherwise.
     */
    defaultPort(scheme: string): number {
        if (scheme == "http") {
            return 80;
        } else if (scheme == "https") {
            return 443;
        } else {
            return -1;
        }
    }

    parse(base: HttpUrl, input: string): Builder {
        this.url = input
        const url = new Url.URL(input);
        this.host = url.hostname
        this.port = Number(url.port).valueOf()
        let self = this
        let netHandle = connection.getDefaultNetSync();
        netHandle.getAddressesByName(this.host).then(function (address) {
            for (var i = 0;i < address.length; i++) {
                self.address.push(address[i].address)
            }
            self.address = Array.from(new Set(self.address)) //去重
        })

        let pos = Utils.indexOfFirstNonAsciiWhitespace(input, 0, input.length)
        let limit = Utils.indexOfLastNonAsciiWhitespace(input, pos, input.length)

        // Scheme.
        let schemeDelimiterOffset = this.schemeDelimiterOffset(input, pos, limit)
        if (schemeDelimiterOffset != -1) {
            if (input.startsWith("https:", pos)) {
                this.scheme = "https"
                pos += "https:".length
            } else if (input.startsWith("http:", pos)) {
                this.scheme = "http"
                pos += "http:".length
            } else if (input.startsWith("ws:", pos)) {
                this.scheme = "ws"
                pos += "ws:".length
            } else {
                throw Error("Expected URL scheme 'http' or 'https' but was '" +
                input.substring(0, schemeDelimiterOffset) + "'")
            }
        } else if (base != null) {
            this.scheme = base.scheme
        } else {
            throw Error("Expected URL scheme 'http' or 'https' but no scheme was found")
        }
        return this
    }

    public build(): HttpUrl {
        return new HttpUrl(this);
    }

    private schemeDelimiterOffset(input: string, pos: number, limit: number): number {
        if (limit - pos < 2) return -1

        let c0 = input[pos]
        if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z')) return -1 // Not a scheme start char.

        for (var i = pos + 1; i < limit; i++) {
            let c = input.charAt(i);

            if ((c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
            || (c >= '0' && c <= '9')
            || c == '+'
            || c == '-'
            || c == '.') {
                continue; // Scheme character. Keep going.
            } else if (c == ':') {
                return i; // Scheme prefix!
            } else {
                return -1; // Non-scheme character before the first ':'.
            }
        }

        return -1 // No ':'; doesn't start with a scheme.
    }
}