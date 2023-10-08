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

import { Chain, Interceptor } from '../Interceptor';
import { Response } from '../response/Response';
import { RealInterceptorChain } from './RealInterceptorChain';
import gZipUtil from '../utils/gZipUtil';
import { Logger } from '../utils/Logger';
import buffer from '@ohos.buffer';
import ConstantManager from '../ConstantManager'
import hilog from '@ohos.hilog';

export class BridgeInterceptor implements Interceptor {
    intercept(chain: Chain): Promise<Response> {
        Logger.info("BridgeInterceptor intercept")
        chain = chain as RealInterceptorChain
        const userRequest = chain.requestI()
        var header = userRequest.headers;
        var requestJSON = JSON.parse(JSON.stringify(header));
        Logger.info('BridgeInterceptor:requestJSON:' + JSON.stringify(header))
        var encodingFormat = requestJSON[ConstantManager.ACCEPT_ENCODING];
        encodingFormat = (encodingFormat == undefined) ? requestJSON[ConstantManager.ACCEPT_ENCODING.toLowerCase()] : encodingFormat;
        encodingFormat = (encodingFormat == undefined) ? requestJSON[ConstantManager.ACCEPT_ENCODING.toUpperCase()] : encodingFormat;
        Logger.info('BridgeInterceptor:encodingFormat:' + encodingFormat)
        if (encodingFormat == undefined || encodingFormat == null || encodingFormat == '') {
            const bridgeResponse = chain.proceedI(userRequest)
            return bridgeResponse
        }
        if (encodingFormat.toString().toLowerCase() == 'gzip') {
            try {
                if (userRequest.body != null) {
                    var compressed = gZipUtil.gZipString(userRequest.body.content);
                    let myArray = gZipUtil.stringToUint8Array(compressed)
                    if (userRequest.gzipBodyNeedBuffer == true) {
                        let bufferContent = buffer.from(myArray as Uint8Array)
                        userRequest.body.content = bufferContent
                    } else {
                        userRequest.body.content = myArray
                    }
                }
            } catch (error) {
                hilog.info(0x0001, "gInterceptors: Request error", error.message);
            }
        }
        const bridgeResponse = chain.proceedI(userRequest)
        return bridgeResponse
    }
}
