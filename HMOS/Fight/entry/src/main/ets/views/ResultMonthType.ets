import OperateBean from '../database_v2/bean/OperateBean'
import HashMap from '@ohos.util.HashMap'
import { ResultBean } from './ResultType_v2'

@Preview
@Component
export default struct ResultMonthType {
  operateList: Array<OperateBean> = null
  map: HashMap<string, ResultBean> = new HashMap()
  @State resultList: Array<ResultBean> = []
  currentMonthIndex: number = 0
  lastMonth: string = null

  aboutToAppear() {
    this.calculate()
  }

  private async calculate() {
    this.operateList.forEach((item) => {
      if (!this.lastMonth) {
        this.lastMonth = item.getMonth()
      } else if (this.lastMonth !== item.getMonth()) {
        this.currentMonthIndex++
        let result: ResultBean = new ResultBean()
        result.name = this.lastMonth

        this.lastMonth = item.getMonth()

        this.map.forEach((value, key) => {
          result.resultList.push(value.copy())
        })
        result.order()
        this.resultList.push(result)

        this.map.clear()
      }

      if (!this.map.hasKey(item.name)) {
        this.map.set(item.name, new ResultBean())
        this.map.get(item.name).name = item.name
      }

      let bean = this.map.get(item.name)
      bean.result += item.result
        item.result > 0 ? bean.posCount++ : bean.negCount++
    })

  }

  build() {
    Column() {
      List({ space: 5 }) {
        ForEach(this.resultList, (item: ResultBean) => {
          ListItem() {
            Column() {
              Text(item.name)
                .fontSize(17)
                .width('100%')
                .height(30)
                .fontColor($r('app.color.black'))
                .margin({ top: 10, bottom: 5 })
                .padding({ left: 20, right: 20, top: 0, bottom: 0 })

              ForEach(item.resultList, (bean: ResultBean) => {
                this.itemLayout(bean)
              })
            }
            .borderWidth(0.5)
            .borderRadius(20)
            .borderColor($r("app.color.gray"))
            .padding({ left: 0, right: 0, top: 0, bottom: 5 })
          }
        })
      }
    }
    .width('100%')
    .height('100%')
  }

  @Builder
  itemLayout(item: ResultBean) {
    Text()
      .textAlign(TextAlign.Center)
      .width('95%')
      .height(0.5)
      .backgroundColor($r('app.color.gray'))
      .margin({ left: 0, right: 0, top: 5, bottom: 5 })
    Row() {
      Text(item.name)
        .fontSize(17)
        .width('20%')
        .height('100%')
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.black'))

      Text('总/次:' + (item.posCount + item.negCount))
        .fontSize(15)
        .width('20%')
        .height('100%')
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.black'))

      // Column({ space: 1 }) {
      Text('胜率:' + (item.posCount * 100 / (item.posCount + item.negCount)).toFixed(2) + '%')
        .fontSize(15)
        .width('30%')
        .height('100%')
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.black'))
      // .borderWidth(0.5)
      // .borderRadius(15)
      // .borderColor($r("app.color.gray"))

      Text('盈亏:' + item.result)
        .fontSize(15)
        .width('30%')
        .height('100%')
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.white'))
        .backgroundColor(item.result > 0 ? $r('app.color.deepRed') : $r('app.color.deepGreen'))
          // .borderWidth(0.5)
        .borderRadius(15)
      // }
      // .width('35%')
    }.height(35)
    .justifyContent(FlexAlign.Center)

  }
}

@Extend(Text) function textItem() {
  .fontSize(17)
  .width('20%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}

