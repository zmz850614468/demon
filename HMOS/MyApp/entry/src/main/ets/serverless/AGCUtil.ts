// @ts-nocheck
import json from '../agconnect-services.json'
import { initialize } from '@hw-agconnect/hmcore'
import { setApiKey } from '@hw-agconnect/hmcore';
import { setClientSecret } from '@hw-agconnect/hmcore';
/**
 * agconnect 使用
 */
export class AGCUtil {

  /**
   * 1. 初始化sdk
   */
  static initSDK(context) {
    initialize(context, json)
  }


  /**
   * 2. 初始化apiKey,clientSecret
   * 必须在 initSDK() 之后调用
   * @param apiKey
   * @param clientSecret
   */
  static initApiKey(apiKey: string, clientSecret: string) {
    setApiKey(apiKey); // 设置API密钥（凭据）
    setClientSecret(clientSecret); // 设置Client Secret
  }
}