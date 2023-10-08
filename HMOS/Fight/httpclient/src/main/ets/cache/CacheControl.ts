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

import StringUtil from '../utils/StringUtil';
//请求和响应遵循的缓存机制
class CacheControl {
    _maxAgeSeconds: number
    _sMaxAgeSeconds: number
    _isPrivate: boolean = false
    _isPublic: boolean = false
    _mustRevalidate: boolean = false
    _maxStaleSeconds: number
    _minFreshSeconds: number
    _onlyIfCached: boolean = false
    _noTransform: boolean = false
    _immutable: boolean = false
    _headerValue: string

    constructor(builder?, noCache?: boolean, noStore?: boolean, maxAgeSeconds?: number, sMaxAgeSeconds?: number, isPrivate?: boolean, isPublic?: boolean,
                mustRevalidate?: boolean, maxStaleSeconds?: number, minFreshSeconds?: number, onlyIfCached?: boolean, noTransform?: boolean, immutable?: boolean, headerValue?: string) {
        if (builder != null && builder != undefined) {
            this._noCache = builder?._noCache;
            this._noStore = builder?._noStore;
            this._maxAgeSeconds = builder?._maxAgeSeconds;
            this._sMaxAgeSeconds = -1;
            this._isPrivate = false;
            this._isPublic = false;
            this._mustRevalidate = false;
            this._maxStaleSeconds = builder?._maxStaleSeconds;
            this._minFreshSeconds = builder?._minFreshSeconds;
            this._onlyIfCached = builder?._onlyIfCached;
            this._noTransform = builder?._noTransform;
            this._immutable = builder?._immutable;
        } else {
            this._noCache = noCache;
            this._noStore = noStore;
            this._maxAgeSeconds = maxAgeSeconds;
            this._sMaxAgeSeconds = sMaxAgeSeconds;
            this._isPrivate = isPrivate;
            this._isPublic = isPublic;
            this._mustRevalidate = mustRevalidate;
            this._maxStaleSeconds = maxStaleSeconds;
            this._minFreshSeconds = minFreshSeconds;
            this._onlyIfCached = onlyIfCached;
            this._noTransform = noTransform;
            this._immutable = immutable;
            this._headerValue = headerValue;
        }
    }

    /** Builds a {@code Cache-Control} request header. */
    static get Builder() {
        class Builder {
            _noCache: boolean;
            _noStore: boolean;
            _maxAgeSeconds: number = -1;
            _maxStaleSeconds: number = -1;
            _minFreshSeconds: number = -1;
            _onlyIfCached: boolean;
            _noTransform: boolean;
            _immutable: boolean;

            /** Don't accept an unvalidated cached response. */
            public noCache(): Builder {
                this._noCache = true;
                return this;
            }

            /** Don't store the server's response in any cache. */
            public noStore(): Builder {
                this._noStore = true;
                return this;
            }

            /**
             * Sets the maximum age of a cached response. If the cache response's age exceeds {@code
             * maxAge}, it will not be used and a network request will be made.
             *
             * @param maxAge a non-negative integer. This is stored and transmitted with {@link
             * TimeUnit#SECONDS} precision; finer precision will be lost.
             */
            public maxAge(maxAge: number): Builder {
                if (maxAge < 0) throw new Error("maxAge < 0: " + maxAge);
                let maxAgeSecondsLong = maxAge * 1000;
                this._maxAgeSeconds = maxAgeSecondsLong > Number.MAX_VALUE
                    ? Number.MAX_VALUE
                    : maxAgeSecondsLong as number;
                return this;
            }

            /**
             * Accept cached responses that have exceeded their freshness lifetime by up to {@code
             * maxStale}. If unspecified, stale cache responses will not be used.
             *
             * @param maxStale a non-negative integer. This is stored and transmitted with {@link
             * TimeUnit#SECONDS} precision; finer precision will be lost.
             */
            public maxStale(maxStale: number): Builder {
                if (maxStale < 0) throw new Error("maxStale < 0: " + maxStale);
                let maxStaleSecondsLong = maxStale * 1000;
                this._maxStaleSeconds = maxStaleSecondsLong > Number.MAX_VALUE
                    ? Number.MAX_VALUE
                    : maxStaleSecondsLong;
                return this;
            }
            /**
             * Only accept the response if it is in the cache. If the response isn't cached, a {@code 504
             * Unsatisfiable Request} response will be returned.
             */
            public onlyIfCached(): Builder {
                this._onlyIfCached = true;
                return this;
            }

            /** Don't accept a transformed response. */
            public noTransform(): Builder {
                this._noTransform = true;
                return this;
            }

            public immutable(): Builder {
                this._immutable = true;
                return this;
            }

            public build(): CacheControl {
                return new CacheControl(this);
            }
        }

        return Builder
    }

    _noCache: boolean = false

    /**
     * In a response, this field's name "no-cache" is misleading. It doesn't prevent us from caching
     * the response; it only means we have to validate the response with the origin server before
     * returning it. We can do this with a conditional GET.
     *
     * <p>In a request, it means do not use a cache to satisfy the request.
     */
    public get noCache(): boolean {
        return this._noCache;
    }

    _noStore: boolean = false

    /** If true, this response should not be cached. */
    public get noStore(): boolean {
        return this._noStore;
    }

    static FORCE_NETWORK() {
        return new CacheControl.Builder().noCache().build();
    }

    static FORCE_CACHE() {
        return new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(Number.MAX_VALUE)
            .build();
    }

    /**
     * Returns the cache directives of {@code headers}. This honors both Cache-Control and Pragma
     * headers if they are present.
     */
    public static parse(headers: string) {
        let noCache: boolean = false
        let noStore: boolean = false
        let maxAgeSeconds: number = -1
        let sMaxAgeSeconds: number = -1
        let isPrivate: boolean = false
        let isPublic: boolean = false
        let mustRevalidate: boolean = false
        let maxStaleSeconds: number = -1
        let minFreshSeconds: number = -1
        let onlyIfCached: boolean = false
        let noTransform: boolean = false
        let immutable: boolean = false
        let canUseHeaderValue = true
        let headerValue = null

        let headerArray: string[] = []

        if (headers != undefined) {
            headerArray = headers.split("\n")
        }

        if (headerArray.length > 0) {
            headerArray.forEach((name) => {
                if (!StringUtil.isEmpty(name)) {

                    if (name.indexOf("Cache-Control".toLowerCase()) != -1) {

                        if (headerValue != null) {
                            // Multiple cache-control headers means we can't use the raw value.
                            canUseHeaderValue = false;
                        } else {
                            headerValue = name.split(":")[1];
                        }
                    } else if (name.indexOf("Pragma".toLowerCase()) != -1) {

                        // Might specify additional cache-control params. We invalidate just in case.
                        canUseHeaderValue = false;
                    }
                    let directiveValue = name

                    if (directiveValue.indexOf("no-cache") != -1) {
                        noCache = true;
                    } else if (directiveValue.indexOf("no-store") != -1) {
                        noStore = true;
                    } else if (directiveValue.indexOf("max-age") != -1) {
                        maxAgeSeconds = Number(name.split(":")[1])
                    } else if (directiveValue.indexOf("s-maxage") != -1) {
                        sMaxAgeSeconds = Number(name.split(":")[1])
                    } else if (directiveValue.indexOf("private") != -1) {
                        isPrivate = true;
                    } else if (directiveValue.indexOf("public") != -1) {
                        isPublic = true;
                    } else if (directiveValue.indexOf("must-revalidate") != -1) {
                        mustRevalidate = true;
                    } else if (directiveValue.indexOf("max-stale") != -1) {
                        maxStaleSeconds = Number(name.split(":")[1])
                    } else if (directiveValue.indexOf("min-fresh") != -1) {
                        minFreshSeconds = Number(name.split(":")[1])
                    } else if (directiveValue.indexOf("only-if-cached") != -1) {
                        onlyIfCached = true;
                    } else if (directiveValue.indexOf("no-transform") != -1) {
                        noTransform = true;
                    } else if (directiveValue.indexOf("immutable") != -1) {
                        immutable = true;
                    }
                }
            })
            if (!canUseHeaderValue) {
                headerValue = null;
            }
        }

        let value = new CacheControl(null, noCache, noStore, maxAgeSeconds, sMaxAgeSeconds, isPrivate, isPublic,
            mustRevalidate, maxStaleSeconds, minFreshSeconds, onlyIfCached, noTransform, immutable,
            headerValue)

        return value
    }

    /**
     * The duration past the response's served date that it can be served without validation.
     */
    public maxAgeSeconds(): number {
        return this._maxAgeSeconds;
    }

    /**
     * The "s-maxage" directive is the max age for shared caches. Not to be confused with "max-age"
     * for non-shared caches, As in Firefox and Chrome, this directive is not honored by this cache.
     */
    public sMaxAgeSeconds(): number {
        return this._sMaxAgeSeconds;
    }

    public isPrivate(): boolean {
        return this._isPrivate;
    }

    public isPublic(): boolean {
        return this._isPublic;
    }

    public mustRevalidate(): boolean {
        return this._mustRevalidate;
    }

    public maxStaleSeconds(): number {
        return this._maxStaleSeconds;
    }

    public minFreshSeconds(): number {
        return this._minFreshSeconds;
    }

    /**
     * This field's name "only-if-cached" is misleading. It actually means "do not use the network".
     * It is set by a client who only wants to make a request if it can be fully satisfied by the
     * cache. Cached responses that would require validation (ie. conditional gets) are not permitted
     * if this header is set.
     */
    public onlyIfCached(): boolean {
        return this._onlyIfCached;
    }

    public noTransform(): boolean {
        return this._noTransform;
    }

    public immutable(): boolean {
        return this._immutable;
    }

    public toString() {
        let result = this._headerValue;
        return result != null ? result : (this._headerValue = this.headerValue());
    }

    private headerValue(): string {
        let result: string = ''
        if (this._noCache) result.concat("no-cache, ");
        if (this._noStore) result.concat("no-store, ");
        if (this._maxAgeSeconds != -1) result.concat("max-age=").concat(String(this._maxAgeSeconds)).concat(", ");
        if (this._sMaxAgeSeconds != -1) result.concat("s-maxage=").concat(String(this._sMaxAgeSeconds)).concat(", ");
        if (this._isPrivate) result.concat("private, ");
        if (this._isPublic) result.concat("public, ");
        if (this._mustRevalidate) result.concat("must-revalidate, ");
        if (this._maxStaleSeconds != -1) result.concat("max-stale=").concat(String(this._maxStaleSeconds)).concat(", ");
        if (this._minFreshSeconds != -1) result.concat("min-fresh=").concat(String(this._minFreshSeconds)).concat(", ");
        if (this._onlyIfCached) result.concat("only-if-cached, ");
        if (this._noTransform) result.concat("no-transform, ");
        if (this._immutable) result.concat("immutable, ");
        if (result.length == 0) return "";

        result.slice(0, result.length - 2)
        return result.toString()
    }
}


export default CacheControl