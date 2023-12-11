import OperateBean from '../database_v2/bean/OperateBean'
import HashMap from '@ohos.util.HashMap'

@Component
export default struct ResultType {
  operateList: Array<OperateBean> = null
  map: HashMap<string, ResultBean> = new HashMap()
  @State resultList: Array<ResultBean> = []

  aboutToAppear() {
    this.calculate()
  }

  private async calculate() {
    this.operateList.forEach((item) => {
      if (!this.map.hasKey(item.name)) {
        this.map.set(item.name, new ResultBean())
        this.map.get(item.name).name = item.name
      }

      let bean = this.map.get(item.name)
      bean.result += item.result
        item.result > 0 ? bean.posCount++ : bean.negCount++
    })

    this.resultList.push(this.map.get('菜油'))
    this.resultList.push(this.map.get('棕榈油'))
  }

  build() {
    Column() {
      List({ space: 1 }) {
        ForEach(this.resultList, (item: ResultBean) => {
          ListItem() {
            Column() {
              Text(item.name)
                .fontSize(17)
                .width('100%')
                .height(30)
                .align(Alignment.TopStart)
                .padding({ left: 8, right: 8, top: 3, bottom: 3 })
                .fontColor($r('app.color.black'))
              Row() {
                Text('盈/次:' + item.posCount)
                  .fontSize(17)
                  .width('33%')
                  .height('100%')
                  .textAlign(TextAlign.Center)
                  .fontColor($r('app.color.black'))

                Text('亏/次:' + item.negCount)
                  .fontSize(17)
                  .width('33%')
                  .height('100%')
                  .textAlign(TextAlign.Center)
                  .fontColor($r('app.color.black'))

                Text('总/次:' + (item.posCount + item.negCount))
                  .fontSize(17)
                  .width('40%')
                  .height('100%')
                  .textAlign(TextAlign.Center)
                  .fontColor($r('app.color.black'))
              }
              .width('100%')
              .height(35)

              Row() {
                Text('胜率:' + (item.posCount * 100 / (item.posCount + item.negCount)).toFixed(2) + '%')
                  .fontSize(17)
                  .width('50%')
                  .height('100%')
                  .textAlign(TextAlign.Center)
                  .fontColor($r('app.color.white'))

                Text('盈亏:' + item.result)
                  .fontSize(17)
                  .width('50%')
                  .height('100%')
                  .textAlign(TextAlign.Center)
                  .fontColor($r('app.color.white'))
              }
              .width('100%')
              .height(35)
              .borderWidth(1)
              .borderRadius(10)
              .backgroundColor($r("app.color.red"))
              .borderColor($r("app.color.red"))
            }
            .margin({ left: 0, right: 0, top: 3, bottom: 3 })
            .borderWidth(1)
            .borderRadius(10)
            .borderColor($r("app.color.gray"))
          }
        })
      }
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

class ResultBean {
  name: string
  posCount: number = 0
  negCount: number = 0
  result: number = 0
}