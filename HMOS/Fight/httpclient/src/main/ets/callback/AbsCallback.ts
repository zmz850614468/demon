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
import hilog from '@ohos.hilog';

class AbsCallback {
    constructor() {
    }

    onError(response) {
        hilog.info(0x0001, "httpclient- AbsCallback  onError", JSON.stringify(response));
    }

    onSuccess(response) {
        hilog.info(0x0001, "httpclient- AbsCallback  onSuccess", JSON.stringify(response));
    }
}

export default AbsCallback;