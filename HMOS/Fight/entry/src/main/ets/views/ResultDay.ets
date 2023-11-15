import BaseTable_v2 from '../database_v2/BaseTable_v2'
import DayOperateBean from '../database_v2/bean/DayOperateBean'

@Preview
@Component
export default struct ResultDay {
  dayOperateList: Array<DayOperateBean> = null

  build() {
    Column() {
      Row() {
        Text('时间')
          .textItem()
        Text('盈亏')
          .textItem()
        Text('手续费')
          .textItem()
        Text('赢-次')
          .textItem()
        Text('亏-次')
          .textItem()
      }
      .height(40)
      .width('100%')

      List() {
        ForEach(this.dayOperateList, (item: DayOperateBean) => {
          ListItem() {
            Row() {
              Text(item.getDay())
                .textItem()
              Text(item.result + "")
                .textItem()
              Text(item.poundage + "")
                .textItem()
              Text(item.posCount + "")
                .textItem()
              Text(item.negCount + "")
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
  .width('20%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}
