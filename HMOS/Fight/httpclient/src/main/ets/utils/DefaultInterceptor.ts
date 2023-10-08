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

class DefaultInterceptor {
    interceptorList = [];

    constructor() {
    }

    use(iFunction) {
        this.interceptorList.push(iFunction);
        return iFunction;
    }

    eject(iFunction) {
        let index = this.interceptorList.indexOf(iFunction);
        if (index != -1) {
            this.interceptorList.splice(index, 1);
        }
    }

    getSize() {
        return this.interceptorList.length;
    }
}

const gInterceptors = {
    request: new DefaultInterceptor(),
    response: new DefaultInterceptor()
};

export { DefaultInterceptor, gInterceptors };