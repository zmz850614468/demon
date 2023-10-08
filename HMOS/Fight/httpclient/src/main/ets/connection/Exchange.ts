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

import { RealConnection } from './RealConnection';
import { ExchangeCodec } from './ExchangeCodec';

export class Exchange {
    codec: ExchangeCodec
    /** True if there was an exception on the connection to the peer. */
    hasFailure = false

    public connection(): RealConnection {
        return this.codec.carrier as RealConnection
    }

    isCoalescedConnection(): boolean {
        return false
    }
}
