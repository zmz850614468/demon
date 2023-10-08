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

var _onComplete = Symbol();
var _onError = Symbol();

var _id = Symbol();
var _priorityKey = Symbol();

var _isRunning = Symbol();

class BaseWorker {
    constructor($id, $priorityKey) {
        this[_id] = $id;
        this[_priorityKey] = $priorityKey;
    }

    get isRunning() {
        return this[_isRunning];
    }

    get id() {
        return this[_id];
    }

    get priorityKey() {
        return this[_priorityKey];
    }

    set priorityKey(value) {
        this[_priorityKey] = value;
    }

    process(onComplete, onError) {
        this[_onComplete] = onComplete;
        this[_onError] = onError;
        this[_isRunning] = true;
    }

    stop() {
        this[_isRunning] = false;
    }

    notifyComplete(res) {
        var res = res ? res : this;

        if (typeof this[_onComplete] === 'function') this[_onComplete](res);

        this[_isRunning] = false;
    }

    notifyError(...arg: any[]) {
        var res = arg.length > 0 && arg[0] !== undefined ? arg[0] : null;

        var res = res ? res : this;

        if (typeof this[_onError] === 'function') this[_onError](res);

        this[_isRunning] = false;
    }

    dispose() {
        this.stop();

        this[_onComplete] = false;
        this[_onError] = false;
        this[_isRunning] = false;
    }
}

export default BaseWorker;