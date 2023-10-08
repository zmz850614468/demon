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

import CacheStrategy from './CacheStrategy';
import Request from '../Request';
import { Response } from '../response/Response';
import CacheRequest from './CacheRequest'

interface InternalCache {

    get(request: Request): Response

    put(response: Response): CacheRequest

    remove(request: Request): void

    update(cached: Response, network: Response): void

    trackConditionalCacheHit(): void

    trackResponse(cacheStrategy: CacheStrategy.CacheStrategy): void
}

export default InternalCache