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

import cryptoFramework from '@ohos.security.cert';
import RealTLSSocket from './RealTLSSocket';
import StringUtil from '../utils/StringUtil';
import ObjectUtil from '../utils/ObjectUtil';
import OkHostnameVerifier from './OkHostnameVerifier';
import { Logger } from '../utils/Logger'

export default class CertificateVerify {
    tlsSocket: RealTLSSocket

    constructor(tlsSocket: RealTLSSocket) {
        this.tlsSocket = tlsSocket
    }

    verifyCertificate(callback: (err, data) => void) {
        let that = this
        this.tlsSocket.getRemoteCertificate((err, result) => {
            Logger.info('getRemoteCertificate:err:' + err)
            Logger.info('getRemoteCertificate:encodingFormat:' + JSON.stringify(result.encodingFormat))
            if (!!result && result != undefined) {
                if (result.encodingFormat != cryptoFramework.EncodingFormat.FORMAT_PEM
                && result.encodingFormat != cryptoFramework.EncodingFormat.FORMAT_DER) {
                    let onlySupportFormat: string = 'certificate encodingFormat only support cryptoFramework.EncodingFormat.FORMAT_PEM and cryptoFramework.EncodingFormat.FORMAT_DER'
                    that.notifyListener('verify Certificate encodingFormat', onlySupportFormat, result.encodingFormat)
                    callback(onlySupportFormat, false)
                    return
                }
                let resultEncodingFormatString: string = (result.encodingFormat == cryptoFramework.EncodingFormat.FORMAT_PEM ?
                    'cryptoFramework.EncodingFormat.FORMAT_PEM' : 'cryptoFramework.EncodingFormat.FORMAT_DER')
                that.notifyListener('verify Certificate encodingFormat', null, resultEncodingFormatString)

                Logger.info('verifyCertificate:encodingFormat:success')
                that.verifyCipherSuite((errCipher, resultCip) => {
                    that.notifyListener('verify Certificate CipherSuite', errCipher, resultCip)
                    if (!StringUtil.isEmpty(errCipher)) {
                        callback(errCipher, false)
                        return
                    }
                    Logger.info('verifyCertificate:verifyCipherSuite:success')
                    that.verifyProtocol((errPro, resultPro) => {
                        that.notifyListener('verify Certificate Protocol', errPro, resultPro)
                        if (!StringUtil.isEmpty(errPro)) {
                            Logger.info('verifyProtocol:err:' + errPro)
                            callback(errPro, false)
                            return
                        }
                        Logger.info('verifyCertificate:verifyProtocol:success')
                        that.verifySignatureAlgorithms((errSAlog, resultSAlog) => {
                            that.notifyListener('verify Certificate SignatureAlgorithms', errSAlog, resultSAlog)
                            if (!StringUtil.isEmpty(errSAlog)) {
                                Logger.info('verifySignatureAlgorithms:err:' + errSAlog)
                                callback(errSAlog, false)
                                return
                            }
                            Logger.info('verifyCertificate:verifySignatureAlgorithms:success')
                            that.verifyTime((errTime, resultTime) => {
                                that.notifyListener('verify Certificate Time', errTime, resultTime)
                                if (!StringUtil.isEmpty(errTime)) {
                                    Logger.info('verifyTime:err:' + errTime)
                                    callback(errTime, false)
                                    return
                                }
                                that.tlsSocket.setHostNameVerifier(new OkHostnameVerifier())
                                Logger.info('verifyIpAddress:' + that.tlsSocket.hostNameVerifier);
                                that.verifyIpAddress((errIpAddress, resultIpAddress) => {
                                    that.notifyListener('verify Certificate IpAddress', errIpAddress, resultIpAddress)
                                    if (!StringUtil.isEmpty(errIpAddress)) {
                                        Logger.info('verifyIpAddress:err:' + errIpAddress)
                                        callback(errTime, false)
                                        return
                                    }
                                })
                            })

                        })

                    })
                })
            } else {
                callback('verifyCertificate getRemoteCertificate err:' + err, false)
            }
        })
    }

    notifyListener(verifyName: string, err: string, data: string) {
        if (!ObjectUtil.isEmpty(this.tlsSocket.tlsSocketLisenter)) {
            this.tlsSocket.tlsSocketLisenter.onVerify(verifyName, err, data)
        }
    }

    verifyCipherSuite(callback: (err, data) => void) {
        this.tlsSocket.getCipherSuites((err, signData) => {
            Logger.info('verifyCipherSuite:' + JSON.stringify(signData))
            if (err != undefined) {
                callback('getCipherSuites err:' + err, signData)
            }
            if (!!!signData || signData.length == 0) {
                if (!!!this.tlsSocket.options.cipherSuite) {
                    callback(null, signData)
                } else {
                    callback('getCipherSuites has no cipherSuite to verify local cipherSuite', signData)
                }
                return
            }
            if (signData.length > 0) {
                for (let signItem of signData) {
                    if (this.tlsSocket.options.cipherSuite.toLowerCase() == signItem.toLowerCase()) {
                        callback(null, signItem)
                        return
                    }
                }
                callback('verifyCipherSuite false ', signData);
            }
        })
    }

    verifyIpAddress(callback: (err, data) => void) {
        this.tlsSocket.getRemoteAddress((err, result) => {
            //               NetAddress
            if (err != undefined) {
                callback('getRemoteAddress err:' + err, result)
                return
            }
            if (!!!result) {
                callback('verifyIpAddress err:' + err, result)
                return
            }
            if (!!this.tlsSocket.hostNameVerifier && this.tlsSocket.hostNameVerifier != undefined) {
                if (this.tlsSocket.hostNameVerifier.verifyIpAddress(this.tlsSocket.address, result.address)) {
                    callback(null, result.address)
                    return
                } else {
                    callback('verifyIpAddress false', result.address)
                    return
                }
            } else {
                callback(null, result.address)
            }
        })
    }

    verifyProtocol(callback: (err, data) => void) {
        this.tlsSocket.getProtocol((err, protocolData) => {
            Logger.info('verifyProtocol:protocolData:' + JSON.stringify(protocolData))
            if (err != undefined) {
                callback('verifyProtocol err:' + err, protocolData)
            }
            if (!!!protocolData || protocolData.length == 0) {
                callback('getProtocol has no Protocol to verify local Protocol', protocolData)
                return
            }
            if (ObjectUtil.isArray(this.tlsSocket.options.protocols)) {
                for (let protocolItem of this.tlsSocket.options.protocols) {
                    if (protocolData.toLowerCase().includes(protocolItem.toString().toLowerCase())) {
                        callback(null, protocolData)
                        return
                    }
                }
                callback('verifyProtocol false:', protocolData)
            } else {
                if (this.tlsSocket.options.protocols.toString().toLowerCase() == protocolData.toLowerCase()) {
                    callback(null, true)
                    return
                }
                callback('verifyProtocol false:', protocolData)
            }

        })
    }

    verifySignatureAlgorithms(callback: (err, data) => void) {
        this.tlsSocket.getSignatureAlgorithms((err, signatureData) => {
            Logger.info('verifyProtocol:signatureData:' + JSON.stringify(signatureData))
            if (err != undefined) {
                callback('verifySignatureAlgorithms err:' + err, signatureData)
            }
            if (!!!signatureData || signatureData.length == 0) {
                callback('getSignatureAlgorithms has no SignatureAlgorithms to verify local SignatureAlgorithms', signatureData)
                return
            }

            for (let sAlogRemoteItem of signatureData) {
                if (this.tlsSocket.options.signatureAlgorithms.toString().toLowerCase().includes(sAlogRemoteItem.toLowerCase())) {
                    callback(null, signatureData)
                    return
                }
            }
            callback('verifySignatureAlgorithms false:', signatureData)

        })
    }

    verifyTime(callback: (err, data) => void) {
        let caArr: cryptoFramework.EncodingBlob[] = this.tlsSocket.getCAencodingBlob()
        if (!!!caArr || caArr.length == 0) {
            callback('verifyCATime need ca', 'caArr time is null')
            return
        }
        cryptoFramework.createX509Cert(caArr[0], function (error, x509Cert) {
            if (error != null) {
                callback('verifyCATime createX509Cert err:' + error, x509Cert)
                return
            } else {
                try {
                    let notAfter: number = StringUtil.getNumberInStr(x509Cert.getNotAfterTime())
                    if (notAfter >= StringUtil.getCurrentDayTime()) {
                        callback(null, notAfter)
                        return
                    }
                    callback('X509Cert time had be overdue', notAfter)
                    return
                } catch (err) {
                    callback('verifyCATime createX509Cert err:' + JSON.stringify(error), false)
                    return
                }
            }
        });
    }
}