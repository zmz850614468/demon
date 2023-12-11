import { WebSocketListener } from './WebSocketModel';
import Logger from '../util/Logger';
import OperateBean from '../database_v2/bean/OperateBean';
import BaseTable_v2 from '../database_v2/BaseTable_v2';
import Toast from '../util/Toast';
import DayOperateBean from '../database_v2/bean/DayOperateBean';

/**
 * webSocket 监听类
 */
export default class DBWebSocketListener implements WebSocketListener {
  isOpened: boolean
  operateTable: BaseTable_v2<OperateBean>
  dayOperateTable: BaseTable_v2<DayOperateBean>
  listener: Function

  constructor(listener: Function) {
    this.listener = listener
    this.operateTable = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
      Logger.error("tag", "operateTable表初始化结果：" + b)
    })

    this.dayOperateTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
      Logger.error("tag", "DayOperateBean表初始化结果：" + b)
    })
  }

  onError(msg: string) {
    this.isOpened = false
    Logger.error("tag", "DBWebSocketListener-onError(): " + msg)
  }

  onClose() {
    this.isOpened = false
    Logger.error("tag", "DBWebSocketListener-onClose()")
  }

  /**
   * @param msg
   * {"createTime":1655526012607,"id":3303,"inPrice":4762,"inType":"买入","isBadOperate":false,"isFollow":true,"name":"螺纹钢","outPrice":4770,"price":10,"result":80}
   */
  onMessage(msg: string | ArrayBuffer) {
    Logger.error("tag", "DBWebSocketListener-onMessage():" + msg)

    if (typeof msg === "string") {
      let bean = JSON.parse(msg)

      if (msg.includes("poundage")) { // 天数据
        this.dayOperateTable.insertData(bean, (b: boolean) => {
          if (!b) {
            Toast.show("dayOperateTable数据库插入失败")
            Logger.error("dayOperateTable数据库插入失败")
          } else {
            this.listener(2)
          }
        })
      } else { // 次数据
        let b = new OperateBean()
        b.timeStamp = bean.createTime
        b.name = bean.name
        b.type = bean.inType
        b.result = bean.result

        this.operateTable.insertData(b, (b: boolean) => {
          if (!b) {
            Toast.show("operateTable数据库插入失败")
            Logger.error("operateTable数据库插入失败")
          } else {
            this.listener(1)
          }
        })
      }
    }
  }

  onOpen() {
    this.isOpened = true
    Logger.error("tag", "DBWebSocketListener-onOpen()")
  }

  isWebSocketOpened(): boolean {
    return this.isOpened
  }
}