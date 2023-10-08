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

class MediaType {
    private static TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static QUOTED = "\"([^\"]*)\"";
    private static TYPE_SUBTYPE = MediaType.TOKEN + "/" + MediaType.TOKEN
    private static PARAMETER = ";\\s*(?:" + MediaType.TOKEN + "=(?:" + MediaType.TOKEN + "|" + MediaType.QUOTED + "))?"
    private _mediaType: string
    private _type: string
    private _subtype: string
    private _charset: string

    constructor(mediaType: string, type: string, subtype: string, charset: string) {
        this._mediaType = mediaType;
        this._type = type;
        this._subtype = subtype;
        this._charset = charset;
    }

    /**
     * Returns a media type for {@code string}.
     *
     * @throws Error if {@code string} is not a well-formed media type.
     */
    public static get(mString: string): MediaType {
        let typeSubtype = MediaType.TYPE_SUBTYPE.match(mString)
        if (typeSubtype == null) {
            throw new Error("No subtype found for: \"" + mString + '"')
        }

        let type = typeSubtype.groups.key[1].toLocaleLowerCase()
        let subtype = typeSubtype.groups.key[2].toLowerCase()

        let charset = null
        let parameter = MediaType.PARAMETER.match(mString)
        for (var s = typeSubtype.lastIndexOf.bind(this);s < mString.length; s = parameter.lastIndexOf.bind(this)) {
            parameter.slice(s, mString.length)
            if (parameter == null) {
                throw Error("Parameter is not formatted correctly: \""
                + mString.substring(s)
                + "\" for: \""
                + mString
                + '"')
            }
            let name = parameter.groups[1]
            if (name == null || name.toLowerCase() != "charset") continue
            let charsetParameter
            let token = parameter.groups[2]
            if (token != null) {
                // If the token is 'single-quoted' it's invalid! But we're lenient and strip the quotes.
                charsetParameter = (token.startsWith("'") && token.endsWith("'") && token.length > 2)
                    ? token.substring(1, token.length - 1)
                    : token;
            } else {
                // Value is "double-quoted". That's valid and our regex group already strips the quotes.
                charsetParameter = parameter.groups[3];
            }
            if (charset != null && !charsetParameter.equalsIgnoreCase(charset)) {
                throw new Error("Multiple charsets defined: \""
                + charset
                + "\" and: \""
                + charsetParameter
                + "\" for: \""
                + mString
                + '"');
            }
            charset = charsetParameter;
        }

        return new MediaType(mString, type, subtype, charset)
    }

    /**
     * Returns a media type for {@code string}, or null if {@code string} is not a well-formed media
     * type.
     */
    public static parse(mString: string): MediaType {
        try {
            return this.get(mString);
        } catch (e) {
            return null;
        }
    }

    /**
     * Returns the high-level media type, such as "text", "image", "audio", "video", or
     * "application".
     */
    public type(): string {
        return this._type;
    }

    /**
     * Returns a specific media subtype, such as "plain" or "png", "mpeg", "mp4" or "xml".
     */
    public subtype(): string {
        return this._subtype;
    }

    /**
     * Returns the charset of this media type, or null if this media type doesn't specify a charset.
     */
    public charset(defaultValue?: string): any {
        try {
            return this.charset != null ? escape(defaultValue) : defaultValue
        } catch (e) {
            return defaultValue; // This charset is invalid or unsupported. Give up.
        }
    }

    /**
     * Returns the encoded media type, like "text/plain; charset=utf-8", appropriate for use in a
     * Content-Type header.
     */
    public toString(): string {
        return this._mediaType;
    }

    public equals(other: Object): boolean {
        return other instanceof MediaType && (other as MediaType)._mediaType == this._mediaType
    }

    public hashCode(): string {
        return this._mediaType
    }
}

export default MediaType