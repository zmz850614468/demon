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

class Protocol {
    static HTTP_1_0 = "http/1.0"
    static HTTP_1_1 = "http/1.1"
    static SPDY_3 = "spdy/3.1"
    static HTTP_2 = "h2"
    static H2_PRIOR_KNOWLEDGE = "h2_prior_knowledge"
    static QUIC = "quic";
    private protocol: string

    constructor(protocol: string) {
        this.protocol = protocol;
    }

    public toString(): string {
        return this.protocol;
    }
}

export default Protocol