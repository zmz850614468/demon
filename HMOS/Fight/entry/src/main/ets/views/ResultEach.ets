import OperateBean from '../database_v2/bean/OperateBean'
import Logger from '../common/util/Logger'
import BaseTable_v2 from '../database_v2/BaseTable_v2'

@Component
export default struct ResultEach {
  @State operateList: Array<OperateBean> = null
  table: BaseTable_v2<OperateBean>

  /**
   * 加载前初始化数据
   */
  aboutToAppear() {
    this.table = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
      // Logger.error("tag", "数据表初始化结果：" + b)
      this.table.queryAll(new OperateBean(), (list: Array<OperateBean>) => {
        this.operateList = list.reverse()
      })
    })
  }

  build() {
    // Stack({ alignContent: Alignment.TopStart }) {
    Column() {
      Row() {
        Text('时间')
          .textItem()
        Text('名称')
          .textItem()
        Text('结果')
          .textItem()
      }
      .height(40)
      .width('100%')

      List({ space: 1 }) {
        ForEach(this.operateList, (item: OperateBean) => {
          ListItem() {
            Row() {
              Text(item.getDay() + "")
                .textItem()
              Text(item.name)
                .textItem()
              Text(item.result + "")
                .textItem()
            }
          }
        })
      }
      .width('100%')
      .height('95%')
    }
    .width('100%')
    .height('100%')
  }
}

@Extend(Text) function textItem() {
  .fontSize(17)
  .width('33.3%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}


