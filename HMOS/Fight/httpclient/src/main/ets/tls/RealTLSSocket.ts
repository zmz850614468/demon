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

import StringUtil from '../utils/StringUtil';
import ObjectUtil from '../utils/ObjectUtil';
import { TLSSocketListener } from './TLSSocketListener';
import socket from '@ohos.net.socket';
import resmgr from '@ohos.resourceManager';
import CertificateVerify from './CertificateVerify';
import cryptoFramework from '@ohos.security.cert';
import OkHostnameVerifier from './OkHostnameVerifier';
import { Logger } from '../utils/Logger';

export default class RealTLSSocket {
    address: string;
    hostNameVerifier: OkHostnameVerifier
    encodingFormat: cryptoFramework.EncodingFormat = cryptoFramework.EncodingFormat.FORMAT_PEM
    //    encodingFormat: number = 0
    options: socket.TLSSecureOptions = {
        ca: '',
        cert: '',
        key: '',
        passwd: '',
        protocols: [socket.Protocol.TLSv12],
        useRemoteCipherPrefer: true,
        signatureAlgorithms: '',
        cipherSuite: ''
    }
    extraOptions: socket.TCPExtraOptions = {
        keepAlive: true,
        OOBInline: true,
        TCPNoDelay: true,
        socketLinger: {
            on: true, linger: 65530
        },
        receiveBufferSize: 1000,
        sendBufferSize: 1000,
        reuseAddress: true,
        socketTimeout: 3000,
    }
    tlsSocketLisenter: TLSSocketListener
    private ALPNProtocols: Array<string> = ["spdy/1", "http/1.1"]
    private family: number = 1; // IPv4 = 1; IPv6 = 2, default is IPv4
    private port: number;
    private isNeedVerify: boolean = false;
    private certificate: CertificateVerify
    private tlsSocket //:socket.TLSSocket

    constructor() {
        this.tlsSocket = socket.constructTLSSocketInstance();
        this.certificate = new CertificateVerify(this);
    }

    setLisenter(tlsSocketLisenter: TLSSocketListener) {
        this.tlsSocketLisenter = tlsSocketLisenter
    }

    setAddress(ipAddress: string) {
        this.address = ipAddress
    }

    setPort(port: number) {
        this.port = port
    }

    setALPNProtocols(aLPNProtocols: Array<string>): RealTLSSocket {
        this.ALPNProtocols = aLPNProtocols
        return this
    }

    setKeyData(keyStr: string) {
        this.options.key = keyStr;
    }

    setCertData(cerStr: string) {
        this.options.cert = cerStr;
    }

    setCaData(caStr: string | string[]) {
        this.options.ca = caStr;
    }

    addCaData(caStr: string) {
        if (ObjectUtil.isArray(this.options.ca)) {
            this.options.ca = this.options.ca.concat(caStr)
        } else {
            let caArr: string[] = []
            caArr.push((this.options.ca as string))
            this.options.ca = caArr
        }
    }

    setCaDataByRes(resourceManager: resmgr.ResourceManager, resName: string[], callBack: (err: string, data: string | boolean | string[]) => void): RealTLSSocket {
        let caObjectArr: CaObject[] = []
        for (let i = 0;i < resName.length; i++) {
            caObjectArr.push(new CaObject(resName[i], i))
        }
        let hadFinishSize: number = 0
        let that = this
        for (let caObjectItem of caObjectArr) {
            this.getRawFileData(resourceManager, caObjectItem.name, (error, result) => {
                if (error != null) {
                    callBack(error, false)
                    return
                }
                if (!!!result) {
                    return
                }
                if (resName.length > 1) {
                    caObjectItem.caStr = result
                    hadFinishSize++
                    if (hadFinishSize >= caObjectArr.length) {
                        let caObjectSort: CaObject[] = that.sortCaObject(caObjectArr)
                        this.options.ca = []
                        let callBackData: string[] = []
                        for (let caObjectSortItem of caObjectSort) {
                            callBackData.push(caObjectSortItem.caStr)
                            this.options.ca.push(caObjectSortItem.caStr)
                        }
                        callBack(null, callBackData)
                        return
                    }
                } else {
                    this.options.ca = result;
                    callBack(null, result)
                    return
                }
            })
        }
        return this
    }

    sortCaObject(caObjectArr: CaObject[]): CaObject[] {
        const arr = [...caObjectArr]
        for (let i = 0, len = arr.length; i < len; i++) {
            let noBubble = true
            // 循环走完第 n 轮循环的时候，数组的后 n 个元素就已经是有序的，所以是 len - 1 - i
            for (let j = 0; j < len - 1 - i; j++) {
                if (arr[j].index > arr[j + 1].index) {
                    [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]]
                    noBubble = false
                }
            }
            if (noBubble) {
                // 当前一轮没有进行冒泡，说明数组已然有序
                return arr
            }
        }
        return arr
    }

    setCertDataByRes(resourceManager: resmgr.ResourceManager, resName: string, callBack): RealTLSSocket {
        this.getRawFileData(resourceManager, resName, (error, result) => {
            callBack(error, result)
            if (!!!result) {
                return
            }
            this.options.cert = result;
        })
        return this
    }

    setKeyDataByRes(resourceManager: resmgr.ResourceManager, resName: string, callBack): RealTLSSocket {
        this.getRawFileData(resourceManager, resName, (error, result) => {
            callBack(error, result)
            if (!!!result) {
                return
            }
            this.options.key = result;
        })
        return this
    }

    setUseRemoteCipherPrefer(useRemoteCipherPrefer: boolean): RealTLSSocket {
        this.options.useRemoteCipherPrefer = useRemoteCipherPrefer
        return this
    }

    setSignatureAlgorithms(signatureAlgorithms: string): RealTLSSocket {
        this.options.signatureAlgorithms = signatureAlgorithms
        return this
    }

    setCipherSuites(cipherSuites: string): RealTLSSocket {
        this.options.cipherSuite = cipherSuites;
        return this
    }

    setPasswd(passwd: string): RealTLSSocket {
        this.options.passwd = passwd
        return this
    }

    setProtocols(protocols): RealTLSSocket {
        this.options.protocols = protocols
        return this
    }

    getRawFileData(resourceManager: resmgr.ResourceManager, resName: string, callBack) {
        resourceManager.getRawFileContent(resName, (error, value) => {
            if (error != null) {
                callBack(error, null)
            } else {
                let rawFile = value;
                let data: string = ''
                for (var i = 0;i < rawFile.length; i++) {
                    let todo = rawFile[i]
                    var item = String.fromCharCode(todo);
                    data += item;
                }
                Logger.info("call getRawFilesssss is " + data);
                callBack(null, data)
            }
        });
        return this
    }

    getCAencodingBlob(): cryptoFramework.EncodingBlob[] {
        if (this.encodingFormat != cryptoFramework.EncodingFormat.FORMAT_PEM && this.encodingFormat != cryptoFramework.EncodingFormat.FORMAT_DER) {
            throw new Error('ca only support cryptoFramework.EncodingFormat.FORMAT_PEM and cryptoFramework.EncodingFormat.FORMAT_DER')
            return null
        }
        if (!!!this.options.ca || StringUtil.isEmpty(this.options.ca.toString())) {
            return null
        }
        let encodingBlobArr: cryptoFramework.EncodingBlob[] = []
        if (ObjectUtil.isArray(this.options.ca)) {
            for (let caItem of this.options.ca) {
                let encodingBlob = {
                    data: StringUtil.stringToUint8Array(caItem),
                    // 根据encodingData的格式进行赋值，支持FORMAT_PEM和FORMAT_DER
                    encodingFormat: this.encodingFormat
                }
                encodingBlobArr.push(encodingBlob)
            }
        } else {
            let encodingBlob = {
                data: StringUtil.stringToUint8Array(this.options.ca),
                // 根据encodingData的格式进行赋值，支持FORMAT_PEM和FORMAT_DER
                encodingFormat: this.encodingFormat
            }
            encodingBlobArr.push(encodingBlob)
        }
        return encodingBlobArr
    }

    setOptions(options: socket.TLSSecureOptions): RealTLSSocket {
        this.options = options;
        return this
    }

    setExtraOptions(extraOptions?: socket.TCPExtraOptions, callback?: (err, data) => void) {
        let TCPExtraOptions = ((!!!extraOptions || extraOptions == undefined) ? this.extraOptions : extraOptions)
        let that = this
        this.tlsSocket.setExtraOptions(TCPExtraOptions, (err, data) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.setExtraOptions(err, data)
            }
            if (!!callback && callback != undefined) {
                callback(err, data)
            }
        })
    }

    bind(callback?: (err, data) => void) {
        let Address = {
            address: this.address,
            port: this.port,
            family: this.family
        }
        let that = this
        this.tlsSocket.bind(Address, (err, data) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onBind(err, data)
            }
            if (!!callback && callback != undefined) {
                callback(err, data)
            }
        });
    }

    connect(callback?: (err, data) => void) {
        let options = {
            ALPNProtocols: this.ALPNProtocols,
            address: {
                address: this.address,
                port: this.port,
                family: this.family
            },
            secureOptions: this.options,
        }
        let that = this
        this.tlsSocket.connect(options, (err, data) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onConnect(err, data)
            }

            if (!!callback && callback != undefined) {
                callback(err, data)
            }
        })
    }

    excute(dendData, callback: (err, data) => void) {
        this.getState((errState, data) => {
            Logger.info("tlsSoket: getState err:" + JSON.stringify(errState))
            Logger.info("tlsSoket: getState data:" + JSON.stringify(data))
            if (!!!data || data.isConnected == false) {
                this.bind((errBind, dataBind) => {
                    Logger.info("tlsSoket: tcpBind err :" + JSON.stringify(errBind))
                    Logger.info("tlsSoket: tcpBind data :" + JSON.stringify(dataBind))
                    if (!!!errBind) {
                        this.openListener()
                        this.setExtraOptions(null, (errOptions, dataOptions) => {
                            this.connect((errConnect, dataConnect) => {
                                this.verifyCertificate((errVerify, dataVerify) => {
                                });
                                Logger.info("tlsSoket: connect err:" + JSON.stringify(errConnect))
                                Logger.info("tlsSoket: connect data:" + JSON.stringify(dataConnect))
                                if (!!!errConnect) {
                                    if (!!dendData && dendData != undefined && dendData.length > 0) {
                                        this.send(dendData, (errSend, dataResut) => {
                                            callback(errSend, dataResut)
                                        })
                                    } else {
                                        callback(errConnect, dataConnect)
                                    }

                                } else {
                                    callback(errConnect, dataConnect)
                                    this.close((errConnect, dataConnect) => {

                                    })
                                }

                            });
                        });
                    } else {
                        callback(errBind, dataBind)
                    }
                })
            } else {
                if (!!dendData && dendData != undefined && dendData.length > 0) {
                    this.send(dendData, (errSend, dataResut) => {
                        callback(errSend, dataResut)
                    })
                } else {
                    if (!!this.tlsSocketLisenter && this.tlsSocketLisenter != undefined) {
                        this.tlsSocketLisenter.onConnect(null, 'had already connected')
                    }
                    callback(null, 'had already connected')
                }
            }
        })
    }

    getCertificate(callback: (err, data) => void) {
        this.tlsSocket.getCertificate((err, data) => {
            callback(err, data);
        });
    }

    getRemoteCertificate(callback: (err, data) => void) {
        this.tlsSocket.getRemoteCertificate((err, data) => {
            callback(err, data);
        })
    }

    send(data, callback?: (err, data) => void) {
        let that = this

        this.tlsSocket.send(data, (err, data) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onSend(err, data)
            }
            if (!!callback && callback != undefined) {
                callback(err, data)
            }
        });
    }

    getProtocol(callback: (err, data) => void) {
        this.tlsSocket.getProtocol((err, data) => {
            callback(err, data);
        });
    }

    getCipherSuites(callback: (err, data) => void) {
        this.tlsSocket.getCipherSuite((err, data) => {
            callback(err, data);
        });
    }

    getSignatureAlgorithms(callback: (err, data) => void) {
        this.tlsSocket.getSignatureAlgorithms((err, data) => {
            callback(err, data);
        });
    }

    close(callback) {
        this.offMessage()
        this.offConnect()
        this.offError()
        this.offClose()
        this.tlsSocket.close((err, data) => {
            callback(err, data);
        });
    }

    getState(callback: (err, data) => void) {
        this.tlsSocket.getState((err, data) => {
            callback(err, data);
        });
    }

    setHostNameVerifier(hostNameVerifier: OkHostnameVerifier) {
        this.hostNameVerifier = hostNameVerifier
    }

    setVerify(isVerify: boolean) {
        this.isNeedVerify = isVerify
    }

    openListener() {
        this.onMessage()
        this.onClose()
        this.onError()
    }

    onMessage() {
        let that = this
        this.tlsSocket.on('message', value => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onMessage(null, value)
            }
        })
    }

    onConnect() {
        let that = this
        this.tlsSocket.on('connect', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onConnect(err, value)
            }
        });
    }

    onClose() {
        let that = this
        this.tlsSocket.on('close', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onClose(err, value)
            }
            Logger.info("onclose:" + value);
        });
    }

    onError() {
        let that = this
        this.tlsSocket.on('error', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.onError(err, value)
            }
            Logger.info("onError:" + value);
        });
    }

    offMessage() {
        let that = this
        this.tlsSocket.off('message', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.offMessage(err, value)
            }
            Logger.info("offMessage:" + value);
        });
    }

    offConnect() {
        let that = this
        this.tlsSocket.off('connect', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.offConnect(err, value)
            }
            Logger.info("offConnect:" + value);
        });
    }

    offClose() {
        let that = this
        this.tlsSocket.off('close', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.offClose(err, value)
            }
            Logger.info("offClose:" + value);
        });
    }

    offError() {
        let that = this
        this.tlsSocket.off('error', (err, value) => {
            if (!!that.tlsSocketLisenter && that.tlsSocketLisenter != undefined) {
                that.tlsSocketLisenter.offError(err, value)
            }
            Logger.info("offError:" + value);
        });
    }

    getRemoteAddress(callback: (err, data) => void) {
        this.tlsSocket.getRemoteAddress((err, data) => {
            callback(err, data);
        });
    }

    verifyCertificate(callback: (err, data) => void) {
        this.tlsSocket.getState((err, data) => {
            Logger.info('verifyCertificate:getState:' + JSON.stringify(data))
            if (!!!data || data.isConnected == false) {
                callback('not connect, verifyCertificate must connect', false)
                return
            }
            if (!!this.hostNameVerifier && this.hostNameVerifier != undefined) {
                this.certificate.verifyIpAddress(callback)
                return
            }
            if (this.isNeedVerify == true) {
                this.certificate.verifyCertificate(callback)
            } else {
                callback(null, true)
            }
        });
    }
}

class CaObject {
    index: number
    name: string
    caStr: string;

    constructor(name: string, index: number) {
        this.name = name;
        this.index = index;
    }
}