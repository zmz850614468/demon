import http from '@ohos.net.http';

/**
 * 网络请求类
 */
export class HttpModel {
  option = {
    method: http.RequestMethod.GET,
    header: {
      'Content-Type': 'application/json' // 默认
    },
    extraData: {},
    expectDataType: http.HttpDataType.OBJECT, // 可选，指定返回数据的类型
    usingCache: true, // 可选，默认为true
    priority: 1, // 可选，默认为1
    connectTimeout: 30000, // 可选，默认为60000ms
    readTimeout: 30000, // 可选，默认为60000ms
    usingProtocol: http.HttpProtocol.HTTP1_1, // 可选，协议类型默认值由系统自动指定
  }
  
  /**
   * 设置头类型
   * @param header {'Content-Type': 'application/json' // 默认
   }
   */
  setHeader(header) {
    this.option.header = header
  }

  /**
   * get 请求方式
   * @param url
   * @param errBack
   * @param dataBack
   */
  getRequest(url: string, extraData: {}, errBack: (number: number, string: string) => void, dataBack: (data: any) => void) {
    this.option.method = http.RequestMethod.GET
    if (extraData) {
      this.option.extraData = extraData
    }

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
  postRequest(url: string, extraData: {}, errBack: (number: number, string: string) => void, dataBack: (data: any) => void) {
    this.option.method = http.RequestMethod.POST
    if (extraData) {
      this.option.extraData = extraData
    }

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
  private getResultFunction(request: http.HttpRequest, errBack: (number: number, string: string) => void, dataBack: (data: any) => void) {
    return (err, data) => {
      if (err) {
        errBack(err.code, err.message)
      } else {
        let res = data.result
        if (res.errcode && res.errcode == 0) {
          dataBack(res.data)
        } else if (!res.errcode) {
          dataBack(res)
        } else {
          errBack(res.errcode, res.errmsg)
        }
      }
      request.destroy() // 当该请求使用完毕时，调用destroy方法主动销毁。
    }
  }
}