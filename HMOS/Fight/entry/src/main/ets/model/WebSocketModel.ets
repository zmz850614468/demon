import webSocket from '@ohos.net.webSocket';
import Toast from '../common/util/Toast';

export default class WebSocketModel {
  private ws = webSocket.createWebSocket();
  listener: WebSocketListener

  constructor(listener: WebSocketListener) {
    this.listener = listener
  }

  /**
   * 连接webSocket服务器
   * @param address  ws://
   */
  connect(address: string) {
    this.onOpen()
    this.onMessage()
    this.onError()
    this.onClose()

    this.ws.connect(address, (err, value) => {
      if (err) {
        Toast.show('webSocket connect 错误：' + value)
      }
    });
  }

  /**
   * 发送数据
   * @param msg
   */
  send(msg: string) {
    this.ws.send(msg, (err, value) => {
      if (err) {
        Toast.show('webSocket send 错误：' + value)
      }
    });
  }

  /**
   * 打开监听
   */
  private onOpen() {
    this.ws.on('open', (err, value) => {
      if (err) {
        Toast.show('webSocket onOpen 错误：' + value)
      } else {
        this.listener.onOpen()
      }
    });
  }

  /**
   * 消息接收监听
   */
  private onMessage() {
    this.ws.on('message', (err, value) => {
      if (err) {
        Toast.show('webSocket onMessage 错误：' + value)
      } else {
        this.listener.onMessage(value)
      }
    });
  }

  /**
   * 关闭监听
   */
  private onClose() {
    this.ws.on('close', (err, value) => {
      if (err) {
        Toast.show('webSocket onClose 错误：' + value)
      } else {
        this.listener.onClose()
      }
    });
  }

  /**
   * 出错监听
   */
  private onError() {
    this.ws.on('error', (err) => {
      this.listener.onError(err.code + ' - ' + err.data)
    });
  }

  /**
   * 关闭
   */
  close() {
    this.ws.close((err, value) => {
      if (!err) {
        console.log("close success");
      } else {
        console.log("close fail, err is " + JSON.stringify(err));
      }
    });
  }

  isOpened(): boolean {
    return this.listener.isWebSocketOpened()
  }
}


export interface WebSocketListener {
  onOpen()

  onMessage(msg: string | ArrayBuffer)

  onClose()

  onError(msg: string)

  isWebSocketOpened(): boolean
}
