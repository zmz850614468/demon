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
import { Logger } from '../utils/Logger';

var cookieStrSplitter = /[:](?=\s*[a-zA-Z0-9_\-]+\s*[=])/g;

function CookieAccessInfo(domain, path, secure, script) {
    if (this instanceof CookieAccessInfo) {
        this.domain = domain || undefined;
        this.path = path || '/';
        this.secure = !!secure;
        this.script = !!script;
        return this;
    }
    return /*new */
    CookieAccessInfo(domain, path, secure, script);
}

function Cookie(cookiestr?, requestDomain?, requestPath?) {
    if (cookiestr instanceof Cookie) {
        return cookiestr;
    }
    if (this instanceof Cookie) {
        this.name = null;
        this.value = null;
        this.expirationDate = Infinity;
        this.path = String(requestPath || '/');
        this.explicitPath = false;
        this.domain = requestDomain || null;
        this.explicitDomain = false;
        this.secure = false; //how to define default?
        this.noscript = false; //httponly
        if (cookiestr) {
            this.parse(cookiestr, requestDomain, requestPath);
        }
        return this;
    }
    // @ts-ignore
    return new Cookie(cookiestr, requestDomain, requestPath);
}

Cookie.prototype.toString = function toString() {
    var str = [this.name + '=' + this.value];
    if (this.expirationDate !== Infinity) {
        str.push('expires=' + (new Date(this.expirationDate)).toUTCString());
    }
    if (this.domain) {
        str.push('domain=' + this.domain);
    }
    if (this.path) {
        str.push('path=' + this.path);
    }
    if (this.secure) {
        str.push('secure');
    }
    if (this.noscript) {
        str.push('httponly');
    }
    return str.join('; ');
};
Cookie.prototype.toValueString = function toValueString() {
    return this.name + '=' + this.value;
};
Cookie.prototype.parse = function parse(str, requestDomain, requestPath) {
    if (this instanceof Cookie) {
        var parts = str.split(';').filter(function (value) {
            return !!value;
        });
        var i;
        var pair = parts[0].match(/([^=]+)=([\s\S]*)/);
        if (!pair) {
            return;
        }
        var key = pair[1];
        var value = pair[2];
        if (typeof key !== 'string' || key.length === 0 || typeof value !== 'string') {
            return;
        }
        this.name = key;
        this.value = value;
        for (i = 1; i < parts.length; i += 1) {
            pair = parts[i].match(/([^=]+)(?:=([\s\S]*))?/);
            key = pair[1].trim().toLowerCase();
            value = pair[2];
            switch (key) {
                case "httponly":
                    this.noscript = true;
                    break;
                case "expires":
                    this.expirationDate = value ? Number(this.convertFromStringToDate(value)) : Infinity;
                    break;
                case "path":
                    this.path = value ? value.trim() : "";
                    this.explicitPath = true;
                    break;
                case "domain":
                    this.domain = value ? value.trim() : "";
                    this.explicitDomain = !!this.domain;
                    break;
                case "secure":
                    this.secure = true;
                    break;
            }
        }
        if (!this.explicitPath) {
            this.path = requestPath || '/';
        }
        if (!this.explicitDomain) {
            this.domain = requestDomain;
        }
        return this;
    }
    return /*new */
    Cookie().parse(str, requestDomain, requestPath);
};

Cookie.prototype.matches = function matches(accessInfo) {
    //  if (accessInfo === CookieAccessInfo.All) {
    if (accessInfo === this.CookieAccessInfo) {
        return true;
    }
    if (this.noscript && accessInfo.script ||
    this.secure && !accessInfo.secure ||
    !this.collidesWith(accessInfo)) {
        return false;
    }
    return true;
};
Cookie.prototype.collidesWith = function collidesWith(accessInfo) {
    if ((this.path && !accessInfo.path) || (this.domain && !accessInfo.domain)) {
        return false;
    }
    if (this.path && accessInfo.path.indexOf(this.path) !== 0) {
        return false;
    }
    if (this.explicitPath && accessInfo.path.indexOf(this.path) !== 0) {
        return false;
    }
    var accessDomain = accessInfo.domain && accessInfo.domain.replace(/^[\.]/, '');
    var cookieDomain = this.domain && this.domain.replace(/^[\.]/, '');
    if (cookieDomain === accessDomain) {
        return true;
    }
    if (cookieDomain) {
        if (!this.explicitDomain) {
            return false; // we already checked if the domains were exactly the same
        }
        var wildcard = accessDomain.indexOf(cookieDomain);
        if (wildcard === -1 || wildcard !== accessDomain.length - cookieDomain.length) {
            return false;
        }
        return true;
    }
    return true;
};
Cookie.prototype.convertFromStringToDate = function convertFromStringToDate(responseDate) {
    let dateComponents = responseDate.split(" ");
    if (dateComponents.length === 4) {
        let datePieces = dateComponents[1].split("-");
        let timePieces = dateComponents[2].split(":");
        return new Date(
        datePieces[2],
            "JanFebMarAprMayJunJulAugSepOctNovDec".indexOf(datePieces[1]) / 3,
        datePieces[0],
        timePieces[0],
        timePieces[1],
        timePieces[2]
        );
    } else if (dateComponents.length === 6) {
        Logger.info(dateComponents);
        let datePieces = [dateComponents[1], dateComponents[2], dateComponents[3]];
        let timePieces = dateComponents[4].split(":");
        Logger.info(timePieces + "--" + datePieces);
        return new Date(
        datePieces[2],
            "JanFebMarAprMayJunJulAugSepOctNovDec".indexOf(datePieces[1]) / 3,
        datePieces[0],
        timePieces[0],
        timePieces[1],
        timePieces[2]
        );
    }
    else {
        return Date.parse(responseDate);
    }
}

export default Cookie;