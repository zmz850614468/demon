import { MyDataSource } from '../adapter/MyDataSource'
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import DayOperateBean from '../database_v2/bean/DayOperateBean'

@Preview
@Component
export default struct ResultDay {
  dayOperateList: Array<DayOperateBean> = null
  @State data: MyDataSource = null
  dayTable: BaseTable_v2<DayOperateBean> = null

  aboutToAppear() {
    this.data = new MyDataSource(this.dayOperateList)
    this.dayTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
    })
  }

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
        LazyForEach(this.data, (item: DayOperateBean, index) => {
          ListItem() {
            Row() {
              Text(item.getDay())
                .textItem()
              Text(item.result.toFixed(0))
                .textItem()
              Text(item.poundage.toFixed(1))
                .textItem()
              Text(item.posCount + "")
                .textItem()
              Text(item.negCount + "")
                .textItem()
            }
          }.gesture(LongPressGesture().onAction(() => {
            AlertDialog.show({
              title: '注意',
              message: '确定要删除当前数据吗？',
              confirm: {
                value: '确定',
                action: () => {
                  this.data.deleteData(index)
                  this.dayTable.deleteData(item, () => {
                  })
                },
              }
            })
          }))
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
