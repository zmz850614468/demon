import StringUtil from '../util/StringUtil'
import DayOperateBean from '../database_v2/bean/DayOperateBean'
import Logger from '../util/Logger';
import OperateBean from '../database_v2/bean/OperateBean';
import { MyDataSource } from '../adapter/MyDataSource';

@Component
export default struct ResultMonth {
  operateResult: any
  dayOperateList: Array<DayOperateBean> = null
  monthList: Array<ResultBean> = []
  @State resultList: Array<ResultBean> = []
  @State type: string = '月'
  @State data: MyDataSource = new MyDataSource(this.resultList)

  aboutToAppear() {
    this.calculate()
    this.operateResult.listener = (type: string) => {
      this.type = type
      this.switchType(type)
    }
  }

  /**
   * 异步处理数据
   */
  private async calculate() {
    let lastMonth = -1
    let bean: ResultBean = null
    this.dayOperateList.forEach((item: DayOperateBean) => {
      let month = StringUtil.getMonth(item.timeStamp)
      if (lastMonth != month) {
        lastMonth = month
        bean = new ResultBean()
        bean.year = StringUtil.getYear(item.timeStamp)
        bean.month = month
        bean.result -= bean.poundage
        this.monthList.push(bean)
      }

      bean.result += item.result
      bean.poundage += item.poundage
      bean.posCount += item.posCount
      bean.negCount += item.negCount
    })

    this.switchType(this.type)
  }

  private async switchType(type: string) {
    switch (type) {
      case '月':
        this.data.deleteAll()
        this.monthList.forEach((item) => {
          this.data.pushData(item)
        })
        this.data.pushData(this.calTotal(this.resultList))
        break;
      case '季':
        this.seasonData()
        break;
      case '年':
        this.yearData()
        if (this.resultList.length == 1) {
          this.resultList[0].month = 13 // 为了解决只有一个月数据时，年切换月，数据不变的情况
        }
        break;
    }
  }

  /**
   * 统计  季  数据
   */
  private seasonData() {
    this.data.deleteAll()
    let size = this.monthList.length
    let bean = new ResultBean()
    for (let i = 0; i < size; i++) {
      let tempBean = this.monthList[i]
      bean.year = tempBean.year
      bean.month = tempBean.month
      bean.posCount += tempBean.posCount
      bean.negCount += tempBean.negCount
      bean.poundage += tempBean.poundage
      bean.result += tempBean.result

      if (bean.month == 1 || bean.month == 4 || bean.month == 7 || bean.month == 10) {
        bean.month = (bean.month + 2) / 3
        this.data.pushData(bean)
        bean = new ResultBean()
      }
    }
    if (bean.poundage > 0) {
      bean.month = Number.parseInt(((bean.month + 2) / 3).toString())
      this.data.pushData(bean)
    }
    this.data.pushData(this.calTotal(this.resultList))
  }

  /**
   * 统计  年  数据
   */
  private yearData() {
    this.data.deleteAll()
    let size = this.monthList.length
    let bean = new ResultBean()
    for (let i = 0; i < size; i++) {
      let tempBean = this.monthList[i]
      bean.year = tempBean.year
      bean.month = tempBean.month
      bean.posCount += tempBean.posCount
      bean.negCount += tempBean.negCount
      bean.poundage += tempBean.poundage
      bean.result += tempBean.result

      if (bean.month == 1) {
        this.resultList.push(bean)
        bean = new ResultBean()
      }
    }
    if (bean.month > 0) {
      this.resultList.push(bean)
    }
    this.data.pushData(this.calTotal(this.resultList))
  }

  private calTotal(list: Array<ResultBean>): ResultBean {
    let bean = new ResultBean()
    list.forEach((item) => {
      bean.posCount += item.posCount
      bean.negCount += item.negCount
      bean.poundage += item.poundage
      bean.result += item.result
    })

    return bean
  }

  build() {
    Column() {
      List({ space: 1 }) {
        LazyForEach(this.data, (item: ResultBean) => {
          ListItem() {
            ItemView({ item: item, type: this.type })
              .padding({ left: 0, right: 0, top: 0, bottom: 3 })
          }
        }, (item: ResultBean) => item.toString())
      }
    }.width('100%')
    .height('100%')
  }
}

@Preview
@Component
struct ItemView {
  private item: ResultBean
  private type: string

  build() {
    Stack() {
      Column() {
        Row() {
          Text()
            .fontSize(17)
            .width('20%')
            .height('100%')
          Text('盈亏:' + this.item.result)
            .fontSize(17)
            .width('40%')
            .height('100%')
          Text('手续费:' + this.item.poundage.toFixed(2))
            .fontSize(17)
            .width('40%')
            .height('100%')
          Text(this.item.result.toString())
            .textAlign(TextAlign.Start)
        }
        .width('100%')
        .height(35)

        Row() {
          Text('盈/次:' + this.item.posCount)
            .fontSize(17)
            .width('33.3%')
            .height('100%')
          Text('亏/次:' + this.item.negCount)
            .fontSize(17)
            .width('33.3%')
            .height('100%')
          Text('总/次:' + (this.item.posCount + this.item.negCount))
            .fontSize(17)
            .width('33.3%')
            .height('100%')
        }.width('100%')
        .height(35)
        .padding({ left: 8, right: 0, top: 0, bottom: 0 })

        Row() {
          Text('胜率:' + (this.item.posCount == 0 ? 100 : (this.item.posCount * 100 / (this.item.posCount + this.item.negCount))).toFixed(2) + '%')
            .fontSize(17)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)
            .fontColor($r('app.color.white'))

          Text('盈亏:' + (this.item.result - this.item.poundage).toFixed(2))
            .fontSize(17)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)
            .fontColor($r('app.color.white'))
        }
        .width('100%')
        .height(35)
        .borderWidth(1)
        .borderRadius(15)
        .backgroundColor($r("app.color.red"))
        .borderColor($r("app.color.red"))
      }
      .width('100%')
      .height('100%')
      .borderWidth(1)
      .borderRadius(15)
      .borderColor($r("app.color.gray"))

      Text(this.item.month == 0 ? '总' : (this.type === '年' ? this.item.year + this.type : this.item.month + this.type))
        .fontSize(19)
        .fontColor($r("app.color.red"))
        .align(Alignment.TopStart)
        .width('100%')
        .height('100%')

      if (this.item.year > 0 && this.type !== '年') {
        Text(this.item.year + '年')
          .fontSize(13)
          .fontColor($r("app.color.black"))
          .align(Alignment.Bottom)
          .textAlign(TextAlign.End)
          .width('100%')
          .height('100%')
          .padding({ left: 0, right: 3, top: 0, bottom: 40 })
      }
    }.width('100%')
    .height(110)
  }
}

class ResultBean {
  year: number
  month: number = 0
  posCount: number = 0
  negCount: number = 0
  result: number = 0
  poundage: number = 0 // 费用

  toString(): string {
    return this.year + '-' + this.month + '-' + this.result + '-' + this.poundage
  }
}

@Extend(Text) function textItem() {
  .fontSize(17)
  .width('20%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}




