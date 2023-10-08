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

import connection from '@ohos.net.connection';
import { Logger } from '../utils/Logger';
import hilog from '@ohos.hilog';

function DnsResolve() {
}


/**
 * This function gets IPAddresses corresponding to url by resolving DNS
 *
 * @param url urls who IP to resolve
 *
 * @returns {info} an array of IPAddress information
 */
DnsResolve.resolveDNSQuery = async function resolveDNSQuery(url) {

    var info = [];
    Logger.info("resolveDNSQuery and url is " + url);

    try {
        info = await connection.getAddressesByName(url);
        Logger.info("getDefaultNet & info " + JSON.stringify(info));
    } catch (err) {
        hilog.info(0x0001, "resolveDNSQuery getAddressesByName error stringified", JSON.stringify(err));
    }

    return info;
}


/**
 * This function gets IPAddresses corresponding to url by resolving DNS and calls the callback
 *
 * @param url urls who IP to resolve
 *
 *
 * @param callback callback function to be invoked after DNS resolve
 *
 *
 * @param cbObj callback object to be passed to callback function after DNS resolve
 *
 * @returns None
 */
DnsResolve.resolveDNSQueryCallback = async function resolveDNSQueryCallback(url, callback, cbObj) {

    var info = [];
    Logger.info("resolveDNSQueryCallback and url is " + url);

    try {
        info = await connection.getAddressesByName(url);
        callback(info, cbObj);

    } catch (err) {
        hilog.info(0x0001, "resolveDNSQueryCallback getAddressesByName error stringified", JSON.stringify(err));
    }

    callback(null, cbObj);
}

export default DnsResolve;