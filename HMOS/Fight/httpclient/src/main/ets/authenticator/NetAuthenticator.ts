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

import ArrayList from '@ohos.util.ArrayList';
import Authenticator from './Authenticator'
import Challenge from './Challenge'
import Request from '../Request';
import Credentials from './Credentials'
import StringUtil from '../utils/StringUtil'
import HttpHeaders from '../core/HttpHeaders';
import { Response } from '../response/Response';

export default class NetAuthenticator implements Authenticator {
    userName: string
    password: string
    basicStr: string = 'Basic'
    credentials: string

    constructor(userName: string, password: string) {
        if (StringUtil.isEmpty(userName)) {
            throw Error('Authorization need userName')
        }
        if (StringUtil.isEmpty(password)) {
            throw Error('Authorization need password')
        }
        this.userName = userName
        this.password = password
    }

    setCredentials(credentials): NetAuthenticator {
        this.credentials = credentials
        return this
    }

    authenticate(request: Request, response: Response): Request {
        if (StringUtil.isEmpty(this.userName) || StringUtil.isEmpty(this.password)) {
            return request;
        }
        let challenges: ArrayList<Challenge> = HttpHeaders.challenges(response);
        let credentials: string = (StringUtil.isEmpty(this.credentials) ? Credentials.basic(this.userName, this.password) : this.credentials)
        for (let challenge of challenges) {
            if (this.basicStr.toLowerCase() != (challenge.getScheme().toLowerCase())) continue;
            switch (response.responseCode) {
                case 401:
                    if (request.headers.hasOwnProperty('Authorization')) {
                        request.headers['Authorization'] = credentials
                    } else {
                        request.addHeader('Authorization', credentials)
                    }
                    break;
                case 407:
                    if (request.headers.hasOwnProperty('Proxy-Authorization')) {
                        request.headers['Proxy-Authorization'] = credentials
                    } else {
                        request.addHeader('Proxy-Authorization', credentials)
                    }
                    break;
            }
            break
        }
        return request;
    }
}