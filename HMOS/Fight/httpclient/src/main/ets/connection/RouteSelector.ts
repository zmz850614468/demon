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

import { Route } from './Route';
import { Address } from '../Address';
import connection from '@ohos.net.connection';
import { Dns } from '../Dns';

export class RouteSelector {
    netAddresses = new Array<Route>()
    nextRouteIndex: number = 0
    dns: Dns

    constructor(dns: Dns) {
        this.dns = dns
    }

    resetNextInetSocketAddress(add: Array<connection.NetAddress>) {
        if (!!add) {
            for (let i = 0; i < add.length; i++) {
                let address = new Address(add[i].address, add[i].port)
                let route = new Route()
                route.add = address
                this.netAddresses.push(route)
            }
        }
    }

    hasNextRoute(): boolean {
        return this.nextRouteIndex < this.netAddresses.length
    }

    next(): Route {
        if (!!this.netAddresses) {
            let result = this.netAddresses[this.nextRouteIndex]
            this.nextRouteIndex++
            return result
        }
    }
}