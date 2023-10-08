/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import Cookie from './Cookie'
import { Logger } from '../utils/Logger';
import { CookiePolicy } from './httpcookieutils';

var cookieStrSplitter = /[:](?=\s*[a-zA-Z0-9_\-]+\s*[=])/g;

function CookieJar() {
    var cookiesList, collidableCookie, cookies, cookiemap, cookieStore;
    this.cookieStore = null;
    this.cookies = Object.create(null);
}

CookieJar.prototype.setCookieStore = function setCookieStore(cookieStore) {
    this.cookieStore = cookieStore;
}
CookieJar.prototype.getCookieStore = function getCookieStore() {
    return this.cookieStore;
}
CookieJar.prototype.setCookie = function setCookie(cookie, requestDomain, requestPath) {
    var remove, i;
    Logger.info("httpclient- cookiejar setCookie: " + cookie);
    cookie = /*new */
    Cookie(cookie, requestDomain, requestPath);
    //Delete the cookie if the set is past the current time
    remove = cookie.expirationDate <= Date.now();
    Logger.info("httpclient- cookiejar expiry: " + remove);
    this.cookiesList = this.cookies[cookie.name] !== undefined ? this.cookies[cookie.name] : [];
    for (i = 0; i < this.cookiesList.length; i += 1) {
        this.collidableCookie = this.cookiesList[i];
        Logger.info("httpclient- cookiejar cookie checking: " + this.collidableCookie.name);
        if (this.collidableCookie.collidesWith(cookie)) {
            if (remove) {
                Logger.info("httpclient- cookiejar cookie removing: " + this.collidableCookie.name);
                this.cookiesList.splice(i, 1);
                if (this.cookiesList.length === 0) {
                    delete this.cookies[cookie.name];
                }
                return false;
            }
            Logger.info("httpclient- cookiejar cookie adding: " + cookie.name);
            this.cookiesList[i] = cookie;
            return cookie;
        }

        if (remove) {
            return false;
        }
        this.cookiesList.push(cookie);
        return cookie;
    }
    if (remove) {
        return false;
    }
    this.cookies[cookie.name] = [cookie];
    return this.cookies[cookie.name];
};

//returns a cookie
CookieJar.prototype.getCookie = function getCookie(cookieName, accessInfo) {
    var cookie, i;
    this.cookiesList = this.cookies[cookieName];
    if (!this.cookiesList) {
        Logger.info("httpclient- cookiejar getCookie nocookie: " + cookieName);
        return;
    }
    for (i = 0; i < this.cookiesList.length; i += 1) {
        cookie = this.cookiesList[i];
        if (cookie.expiration_date <= Date.now()) {
            if (this.cookiesList.length === 0) {
                delete this.cookies[cookie.name];
            }
        } else {
            if (accessInfo == undefined) {
                return cookie;
            }
            if (cookie.matches(accessInfo)) {
                Logger.info("httpclient- cookiejar matched cookie: " + cookie.name);
                return cookie;
            }
            Logger.info("httpclient- cookiejar NO matched cookie: " + cookieName);
        }
    }
}
//returns a list of cookies
CookieJar.prototype.getCookies = function getCookies(accessInfo) {
    var matches = [];
    var cookieName, cookie;
    Logger.info("httpclient- cookiejar getCookie checking cookies: ");
    for (cookieName in this.cookies) {
        cookie = this.getCookie(cookieName, accessInfo);
        if (cookie) {
            matches.push(cookie);
        }
    }
    return matches;
}
CookieJar.prototype.saveFromResponse = function saveFromResponse(resp, url, cookiemanager) {
    var cookiePolicy = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
    Logger.info("httpclient- cookiemanager: " + cookiemanager.toString());
    if (cookiemanager) {
        cookiePolicy = cookiemanager.cookiePolicy;
    }
    Logger.info("httpclient- checking cookie policy: " + cookiePolicy);
    if (cookiePolicy === CookiePolicy.ACCEPT_NONE)
        return;
    this.loadForRequest(url);
    var header = resp.header;

    Logger.info("httpclient- header: " + JSON.stringify(header));

    var responseJSON = JSON.parse(JSON.stringify(header));
    Logger.info("httpclient- cookie: " + responseJSON["set-cookie"]);
    if (responseJSON['set-cookie']) {
        if (cookiePolicy === CookiePolicy.ACCEPT_ORIGINAL_SERVER) {
            if (!this.domainMatches(responseJSON['set-cookie'], this.extractHostname(url))) {
                Logger.info("httpclient- cookie: the cookie domain are not matching");
                return;
            }
        }
        this.setCookie(responseJSON['set-cookie']);
        this.saveCookie(url);
    }
}
CookieJar.prototype.loadForRequest = function loadForRequest(url) {
    this.cookies = Object.create(null);
    this.loadCookie(url);
    var cookieHeader = '';
    for (let cookieName in this.cookies) {
        var cookie = this.getCookie(cookieName);
        if (cookieHeader.length > 0)
            cookieHeader = cookieHeader + ';';
        cookieHeader = cookieHeader + cookie.name + "=" + cookie.value;
    }
    Logger.info("httpclient- cookieHeader: " + cookieHeader);
    return cookieHeader;
}
CookieJar.prototype.saveCookie = function saveCookie(url) {
    url = this.extractHostname(url);
    Logger.info("httpclient- saveCookie:" + url);
    if (this.cookieStore) {
        this.cookieStore.writeCookie(url, JSON.stringify(this.cookies));
    }
    else {
        Logger.info("httpclient- loadCookie : not a persistent cookiestore");
    }
    Logger.info("httpclient- saveCookie completed:");

}

// jshint maxdepth:5
CookieJar.prototype.loadCookie = function loadCookie(url) {
    url = this.extractHostname(url);
    Logger.info("httpclient- loadCookie :" + url);
    if (this.cookieStore && this.cookieStore.readCookie(url)) {
        let cookieJSON = this.cookieStore.readCookie(url);
        Logger.info("httpclient- loadCookie JSON :" + cookieJSON);
        let json = JSON.parse(cookieJSON);
        for (let cookieName in json) {
            Logger.info(JSON.stringify(json[cookieName]));
            for (var key in json[cookieName]) {
                Logger.info("httpclient- " + JSON.stringify(json[cookieName][key]));
                let cookiejson = json[cookieName][key];
                let cookiestr = [cookiejson.name + "=" + cookiejson.value];
                if (cookiejson.expiration_date && (cookiejson.expiration_date !== Infinity)) {
                    cookiestr.push(
                        "expires=" + new Date(cookiejson.expiration_date).toUTCString()
                    );
                }
                if (cookiejson.domain)
                    cookiestr.push("domain=" + cookiejson.domain);

                if (cookiejson.path)
                    cookiestr.push("path=" + cookiejson.path);

                if (cookiejson.secure)
                    cookiestr.push("secure");

                if (cookiejson.noscript)
                    cookiestr.push("httponly");

                Logger.info("httpclient- cookiestr:" + cookiestr.join("; "));
                this.setCookie(cookiestr.join("; "));
            }
        }
    }
    else {
        Logger.info("httpclient- loadCookie : not a persistent cookiestore");
    }
}

//returns list of cookies that were set correctly. Cookies that are expired and removed are not returned.
CookieJar.prototype.setCookies = function setCookies(cookies, requestDomain, requestPath) {
    cookies = Array.isArray(cookies) ?
        cookies :
    cookies.split(cookieStrSplitter);
    var successful = [],
        i,
        cookie;
    cookies = cookies.map(function (item) {
        return /*new */
        Cookie(item, requestDomain, requestPath);
    });
    for (i = 0; i < cookies.length; i += 1) {
        cookie = cookies[i];
        if (this.setCookie(cookie, requestDomain, requestPath)) {
            successful.push(cookie);
        }
    }
    return successful;
};
CookieJar.prototype.extractHostname = function extractHostname(url) {
    var hostname;
    //find & remove protocol (http, ftp, etc.) and get hostname
    if (url.indexOf("//") > -1) {
        hostname = url.split('/')[2];
    }
    else {
        hostname = url.split('/')[0];
    }
    //find & remove port number
    hostname = hostname.split(':')[0];
    //find & remove "?"
    hostname = hostname.split('?')[0];
    return hostname;
}
CookieJar.prototype.domainMatches = function domainMatches(cookies, host) {

    var cookie = /*new */
    Cookie(cookies);
    var domain = cookie.domain;
    if (!domain) {
        Logger.info("httpclient- cookie: the cookie domain is not present");
        return false;
    }
    //Delete the cookie if the set is past the current time

    if (domain == null || host == null) return false;
    Logger.info("httpclient- domain:" + domain + "," + "host:" + host);
    // if there's no embedded dot in domain and domain is not .local
    var isLocalDomain = ".local".toLowerCase() === domain.toLowerCase();
    var embeddedDotInDomain = domain.indexOf(".");
    if (embeddedDotInDomain == 0) embeddedDotInDomain = domain.indexOf(".", 1);
    Logger.info("httpclient- " +
    "isLocalDomain:" +
    isLocalDomain +
    ",embeddedDotInDomain:" +
    embeddedDotInDomain
    );
    if (
        !isLocalDomain &&
        (embeddedDotInDomain == -1 || embeddedDotInDomain == domain.length - 1)
    )
        return false;
    Logger.info("httpclient- " + isLocalDomain + "," + embeddedDotInDomain);
    // if the host name contains no dot and the domain name
    // is .local or host.local
    var firstDotInHost = host.indexOf(".");
    if (
        firstDotInHost == -1 &&
        (isLocalDomain || domain.toLowerCase() === (host + ".local").toLowerCase())
    ) {
        return true;
    }

    var domainLength = domain.length;
    var lengthDiff = host.length - domainLength;
    if (lengthDiff == 0) {
        // if the host name and the domain name are just string-compare euqal
        return host.toLowerCase() === domain.toLowerCase();
    } else if (lengthDiff > 0) {
        // need to check H & D component
        var H = host.substring(0, lengthDiff);
        var D = host.substring(lengthDiff);
        return H.indexOf(".") == -1 && D.toLowerCase() === domain.toLowerCase();
    } else if (lengthDiff == -1) {
        // if domain is actually .host
        return (
            domain.charAt(0) == "." &&
            host.toLowerCase() === domain.substring(1).toLowerCase()
        );
    }

    return false;
}

export default CookieJar;