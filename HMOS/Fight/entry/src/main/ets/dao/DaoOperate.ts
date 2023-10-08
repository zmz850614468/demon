import { DaoSession } from '@ohos/dataorm/src/main/ets/core/DaoSession';
import { GlobalContext } from '@ohos/dataorm/src/main/ets/core/GlobalContext';
import MoneyBean from './MoneyBean';
import Logger from '../common/util/Logger';
import { Select } from '@ohos/dataorm/src/main/ets/core/dbflow/base/Select';
/**
 * 数据库操作类
 */
export default class DaoOperate {
  private static instance: DaoOperate

  private daoSession: DaoSession | null = null;

  private constructor() {
    this.daoSession = GlobalContext.getContext().getValue("daoSession") as DaoSession;
  }

  /**
   * 单例
   * @returns
   */
  static getInstance() {
    if (!DaoOperate.instance) {
      DaoOperate.instance = new DaoOperate()
    }

    return DaoOperate.instance
  }

  /**
   * 添加新的数据 api-9 不支持插入接口
   * @param obj
   */
  index = 1

  async addSave(entity: any, obj: object) {
    let dao = this.daoSession.getBaseDao(MoneyBean)
    if (dao) {
      let bean = new MoneyBean()
      bean.id = 0
      bean.date = "2023-10-" + this.index
      bean.munch = this.index
      bean.memo = "memo-" + this.index

      this.index++
      let count = await dao.insertWithoutSettingPk(bean)
      Logger.error('tag', '插入数据：' + count)
    }
  }

  /**
   * 查询数据
   */
  async query() {
    let dao = this.daoSession.getBaseDao(MoneyBean)
    if (dao) {
      // let ar = await dao.query(new Select().from(MoneyBean))
      let ar = await dao.queryBuilder().distinct().list()
      if (ar) {
        Logger.error('tag', "数据：" + JSON.stringify(ar))
      }
    }
  }
}