import OperateBean from '../database_v2/bean/OperateBean'
import Logger from '../util/Logger'
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import { MyDataSource } from '../adapter/MyDataSource'

@Component
export default struct ResultEach {
  operateList: Array<OperateBean> = null
  data: MyDataSource

  aboutToAppear() {
    this.data = new MyDataSource(this.operateList)
  }

  build() {
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
        LazyForEach(this.data, (item: OperateBean) => {
          ListItem() {
            Row() {
              Text(item.getDay().toString())
                .textItem()
              Text(item.name)
                .textItem()
              Text(item.result.toString())
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


