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

import Authenticator from './Authenticator';
import Request from '../Request';
import ArrayList from '@ohos.util.ArrayList';
import HttpHeaders from '../core/HttpHeaders';
import Route from '../core/Route';
import { Response } from '../response/Response';

export default class RecordingOkAuthenticator implements Authenticator {
    public responses: ArrayList<Response> = new ArrayList<Response>();
    public requests: ArrayList<Request> = new ArrayList<Request>();
    public routes: ArrayList<typeof Route> = new ArrayList<typeof Route>();
    public credential: string;
    public scheme: string;
    basicStr: string = 'Basic'

    constructor(credential: string, scheme: string) {
        this.credential = credential;
        this.scheme = scheme;
    }

    public onlyResponse(): Response {
        if (this.responses.length != 1) throw new Error('onlyResponse llegalStateException');
        return this.responses[0];
    }

    public onlyRequest() {
        if (this.requests.length != 1) throw new Error('onlyRequest llegalStateException');
        return this.requests[0];
    }

    public onlyRoute(): typeof Route {
        if (this.routes.length != 1) throw new Error('onlyRoute llegalStateException');
        return this.routes[0];
    }

    public authenticate(request: Request, response: Response): Request {
        if (response == null) throw new ReferenceError("response == null");

        this.responses.add(response);
        this.requests.add(request);

        if (!this.schemeMatches(response) || this.credential == null) return null;

        switch (response.responseCode) {
            case 401:
                if (request.headers.hasOwnProperty('Authorization')) {
                    request.headers['Authorization'] = this.credential
                } else {
                    request.addHeader('Authorization', this.credential)
                }
                break;
            case 407:
                if (request.headers.hasOwnProperty('Proxy-Authorization')) {
                    request.headers['Proxy-Authorization'] = this.credential
                } else {
                    request.addHeader('Proxy-Authorization', this.credential)
                }
                break;
        }
        return request;
    }

    private schemeMatches(response: Response): boolean {
        if (this.scheme == null) return true;

        for (let challenge of HttpHeaders.challenges(response)) {
            if (challenge.getScheme() == this.scheme) return true;
        }

        return false;
    }
}
