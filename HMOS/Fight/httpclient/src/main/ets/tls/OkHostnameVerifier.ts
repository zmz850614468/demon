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

import cryptoFramework from '@ohos.security.cert';
import { Logger } from '../utils/Logger';


export default class OkHostnameVerifier {
    public static INSTANCE: OkHostnameVerifier = new OkHostnameVerifier()
    static ALT_DNS_NAME: number = 2;
    static ALT_IPA_NAME = 7;

    constructor() {

    }

    public static allSubjectAltNames(certificate: cryptoFramework.X509Cert): string[] {
        let altIpaNames: string[] = this.getSubjectAltNames(certificate, OkHostnameVerifier.ALT_IPA_NAME);
        let altDnsNames: string[] = this.getSubjectAltNames(certificate, OkHostnameVerifier.ALT_DNS_NAME);
        let result: string[] = [];
        result = result.concat(altIpaNames)
        result = result.concat(altDnsNames)
        return result;
    }

    private static getSubjectAltNames(certificate: cryptoFramework.X509Cert, nameType: number): string[] {
        let result: string[] = []
        Logger.info('getSubjectAltNames:' + JSON.stringify(certificate.getSubjectAltNames))

        //let subjectAltNames: Uint8Array[] = certificate.getSubjectAltNames().data;
        /*if (subjectAltNames == null) {
            return [];
        }
        for (let subjectAltName of subjectAltNames) {
            let itemResult: string = StringUtil.uint8ArrayToShowStr(subjectAltName)
            result.push(itemResult)
        }*/
        return result;
    }

    /** Returns true if {@code certificate} matches {@code ipAddress}. */
    public verifyIpAddress(ipAddress: string, ipAddressRemote: string): boolean {
        return ipAddress == ipAddressRemote;
    }

    /**
     * Returns {@code true} iff {@code hostname} matches the domain name {@code pattern}.
     *
     * @param hostname lower-case host name.
     * @param pattern domain name pattern from certificate. May be a wildcard pattern such as {@code
     * *.android.com}.
     */
    public verifyHostnameByPattern(hostname: string, pattern: string): boolean {
        // Basic sanity checks
        // Check length == 0 instead of .isEmpty() to support Java 5.
        if ((hostname == null) || (hostname.length == 0) || (hostname.startsWith("."))
        || (hostname.endsWith(".."))) {
            // Invalid domain name
            return false;
        }
        if ((pattern == null) || (pattern.length == 0) || (pattern.startsWith("."))
        || (pattern.endsWith(".."))) {
            // Invalid pattern/domain name
            return false;
        }

        // Normalize hostname and pattern by turning them into absolute domain names if they are not
        // yet absolute. This is needed because server certificates do not normally contain absolute
        // names or patterns, but they should be treated as absolute. At the same time, any hostname
        // presented to this method should also be treated as absolute for the purposes of matching
        // to the server certificate.
        //   www.android.com  matches www.android.com
        //   www.android.com  matches www.android.com.
        //   www.android.com. matches www.android.com.
        //   www.android.com. matches www.android.com
        if (!hostname.endsWith(".")) {
            hostname += '.';
        }
        if (!pattern.endsWith(".")) {
            pattern += '.';
        }
        // hostname and pattern are now absolute domain names.

        pattern = pattern.toLowerCase();
        // hostname and pattern are now in lower case -- domain names are case-insensitive.

        if (pattern.indexOf("*") == -1) {
            // Not a wildcard pattern -- hostname and pattern must match exactly.
            return hostname == pattern;
        }
        // Wildcard pattern

        // WILDCARD PATTERN RULES:
        // 1. Asterisk (*) is only permitted in the left-most domain name label and must be the
        //    only character in that label (i.e., must match the whole left-most label).
        //    For example, *.example.com is permitted, while *a.example.com, a*.example.com,
        //    a*b.example.com, a.*.example.com are not permitted.
        // 2. Asterisk (*) cannot match across domain name labels.
        //    For example, *.example.com matches test.example.com but does not match
        //    sub.test.example.com.
        // 3. Wildcard patterns for single-label domain names are not permitted.

        if ((!pattern.startsWith("*.")) || (pattern.indexOf('*', 1) != -1)) {
            // Asterisk (*) is only permitted in the left-most domain name label and must be the only
            // character in that label
            return false;
        }

        // Optimization: check whether hostname is too short to match the pattern. hostName must be at
        // least as long as the pattern because asterisk must match the whole left-most label and
        // hostname starts with a non-empty label. Thus, asterisk has to match one or more characters.
        if (hostname.length < pattern.length) {
            // hostname too short to match the pattern.
            return false;
        }

        if ("*." == pattern) {
            // Wildcard pattern for single-label domain name -- not permitted.
            return false;
        }

        // hostname must end with the region of pattern following the asterisk.
        let suffix: string = pattern.substring(1);
        if (!hostname.endsWith(suffix)) {
            // hostname does not end with the suffix
            return false;
        }

        // Check that asterisk did not match across domain name labels.
        let suffixStartIndexInHostname: number = hostname.length - suffix.length;
        if ((suffixStartIndexInHostname > 0)
        && (hostname.lastIndexOf('.', suffixStartIndexInHostname - 1) != -1)) {
            // Asterisk is matching across domain name labels -- not permitted.
            return false;
        }

        // hostname matches pattern
        return true;
    }

    /** Returns true if {@code certificate} matches {@code hostname}. */
    private verifyHostname(hostname: string, certificate: cryptoFramework.X509Cert): boolean {
        hostname = hostname.toLowerCase();
        let altNames: string[] = OkHostnameVerifier.getSubjectAltNames(certificate, OkHostnameVerifier.ALT_DNS_NAME);
        for (let altName of altNames) {
            if (this.verifyHostnameByPattern(hostname, altName)) {
                return true;
            }
        }
        return false;
    }
}