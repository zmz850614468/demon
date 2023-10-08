/**
 * dataorm 初始化
 * 非加密库在AbilityStage.ts进行初始化
 */
import { DaoMaster } from '@ohos/dataorm/src/main/ets/core/DaoMaster';
import { Database } from '@ohos/dataorm/src/main/ets/core/database/Database';
import { GlobalContext } from '@ohos/dataorm/src/main/ets/core/GlobalContext';
import { ExampleOpenHelper } from './ExampleOpenHelper';
import MoneyBean from './MoneyBean';
import Logger from '../common/util/Logger';

export default class DaoInit {
  private static DB_NAME: string = "demon.db"
  private static DB_VERSION: number = 1;

  /**
   * 数据库初始化类
   * @param context
   */
  static async init(context) {
    GlobalContext.getContext().setValue("contt", context)
    //entity 属性集合，通过类名获取
    GlobalContext.getContext().setValue("entityCls", {});
    //记录要做处理的实体类，通过类名获取
    GlobalContext.getContext().setValue("entityClsArr", {});
    //全局临时变量，用于特殊情况存储数据
    GlobalContext.getContext().setValue("entityClsRelationshipArr", {});
    //entity 属性toMany、 toOne关系集合
    GlobalContext.getContext().setValue("entityClsRelationship", {});

    let helper: ExampleOpenHelper = new ExampleOpenHelper(context, DaoInit.DB_NAME);
    // helper.setEncrypt(true);
    let db: Database = await helper.getWritableDb();
    helper.setEntities(MoneyBean);
    //调用创建表方法,将新增表创建,若无新增则不创建表
    helper.onCreate_D(db);

    // todo 需要可以更新数据库表结构
    // DaoInit.updateDB(helper)

    GlobalContext.getContext().setValue("daoSession", new DaoMaster(db).newSession());

    Logger.error('tag', 'dao-数据库初始化完成')
  }

  /**
   * 更新表结构
   * @param helper
   */
  static updateDB(helper: ExampleOpenHelper) {
    //todo 数据库新增列示例
    //Migration为表更新实例,也可调用Migration.backupDB对当前数据库进行备份 todo
    // let migration = new Migration(DaoInit.DB_NAME, "tableName", DaoInit.DB_VERSION).addColumn("MONEYS", ColumnType.realValue);
    // //将所有表更新实例放到ExampleOpenHelper的父级中
    // helper.setMigration(migration);

    // //设置新的数据库版本,如果新版本中包含表更新实例将在这调用onUpgrade_D()进行表更新
    helper.setVersion(DaoInit.DB_VERSION)
  }
}