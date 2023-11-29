import http from '@ohos.net.http';

/**
 * 网络请求类
 */
export default class HttpModel {
  option = {
    method: http.RequestMethod.GET,
    header: {
      'Content-Type': 'application/json' // 默认
    },
    extraData: {},
    expectDataType: http.HttpDataType.STRING, // 可选，指定返回数据的类型
    usingCache: true, // 可选，默认为true
    priority: 1, // 可选，默认为1
    connectTimeout: 20000, // 可选，默认为60000ms
    readTimeout: 20000, // 可选，默认为60000ms
    usingProtocol: http.HttpProtocol.HTTP1_1, // 可选，协议类型默认值由系统自动指定
  }

  /**
   * get 请求方式
   * @param url
   * @param errBack
   * @param dataBack
   */
  getRequest(url: string, errBack: (number, string) => void, dataBack: (data: any) => void) {
    this.option.method = http.RequestMethod.GET

    let request = http.createHttp()

    request.request(
      url,
      this.option,
      this.getResultFunction(request, errBack, dataBack))
  }

  /**
   * post 请求方式
   * @param url
   */
  postRequest(url: string, errBack: (number, string) => void, dataBack: (data: any) => void) {
    this.option.method = http.RequestMethod.POST

    let request = http.createHttp()
    request.request(
      url,
      this.option,
      this.getResultFunction(request, errBack, dataBack))
  }

  /**
   * 请求返回值的处理
   * @param request
   * @param errBack
   * @param dataBack
   * @returns
   */
  private getResultFunction(request: http.HttpRequest, errBack: (number, string) => void, dataBack: (data: any) => void) {
    return (err, data) => {
      if (err) {
        errBack(err.code, err.message)
      } else {
        let res = JSON.parse(data.result)
        if (res.errcode == 0) {
          dataBack(res.data)
        } else {
          errBack(res.errcode, res.errmsg)
        }
      }
      request.destroy() // 当该请求使用完毕时，调用destroy方法主动销毁。
    }
  }

  /**
   * 设置头类型
   * @param header
   */
  setHeader(header) {
    this.option.header = header
  }
}