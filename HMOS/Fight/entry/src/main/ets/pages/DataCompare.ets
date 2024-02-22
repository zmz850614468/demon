import { GetTypeMonthData, HandsMap, Months, Types } from '../constant/TypeConstant'
import AnalysisBean from '../dataAnalysis/bean/AnalysisBean'
import HashMap from '@ohos.util.HashMap'
import pasteboard from '@ohos.pasteboard'
import Toast from '../util/Toast'

@Entry
@Component
struct DataCompare {
  @State selectedMonthIndex: Array<number> = [0, 1]
  @State selectedTypeIndex: Array<number> = [0, 1, 2, 3, 4]
  @State isSelectionLayoutShow: boolean = false
  @State selectionDatas: Array<AnalysisBean> = []
  updateCount: number = 1

  // 月数据
  monthAnalysisMap: HashMap<string, Array<AnalysisBean>> = new HashMap()

  aboutToAppear() {
    this.refreshData()
  }

  /**
   * 刷新数据
   */
  private refreshData() {
    this.selectionDatas.splice(0)
    Months.forEach((months, monthIndex) => {
      if (this.selectedMonthIndex.indexOf(monthIndex) >= 0) {
        let b = new AnalysisBean()
        b.year = -1
        b.month = months.toString()
        this.selectionDatas.push(b)
        Types.forEach((type, index) => {
          if (this.selectedTypeIndex.indexOf(index) >= 0) { // 需要对比的数据
            let list = null
            // 1.数据不存在，则添加
            if (!this.monthAnalysisMap.hasKey(type)) {
              list = GetTypeMonthData(type)
              this.monthAnalysisMap.set(type, list)
            }
            // 2.数据不存在，则获取
            if (!list) {
              list = this.monthAnalysisMap.get(type)
            }
            // 3.计算数据结果
            let bean = this.cal(list, months)
            bean.type = type
            bean.month = months.toString()
            // if (HandsMap.hasKey(bean.type)) {
            //   bean.update(HandsMap.get(bean.type))
            // }
            this.selectionDatas.push(bean)
          }
        })
      }
    })

    this.updateCount++
    // let list = this.selectionDatas
    // let size = this.selectionDatas.length
    // size++
  }

  /**
   * 计算时长内的数据
   * @param list
   * @param months
   */
  private cal(list: Array<AnalysisBean>, months: number): AnalysisBean {
    let bean = new AnalysisBean()
    let size = Math.min(months, list.length)
    for (let i = 0; i < size; i++) {
      bean.add(list[i])
    }

    return bean
  }

  /**
   * 复制结果到剪切板中
   */
  private copyData() {
    let split = ';'
    let result = ''
    result += '类型' + split
    result += '负操作数' + split
    result += '正操作数' + split
    result += '总操作数' + split
    result += '交易费用' + split
    result += '操作数(/)' + split
    result += '结果(/)' + split
    result += '操作数(-1)' + split
    result += '结果(-1)' + split
    result += '胜率' + split
    result += '收益' + '\n'
    this.selectionDatas.forEach((item, index) => {
      if (item.year === -1) {
        result += item.month + '\n'
      } else {
        result += item.type + split
        result += item.negCount + split
        result += item.posCount + split
        result += item.count + split
        result += item.pay + split
        result += item.fxCount + split
        result += item.fxResult + split
        result += item.fxCount_1 + split
        result += item.fxResult_1 + split
        result += (item.posCount * 100 / item.count).toFixed(1) + '%' + split
        result += item.result + '\n'
      }
    })

    let pasteData = pasteboard.createData(pasteboard.MIMETYPE_TEXT_PLAIN, result)
    pasteboard.getSystemPasteboard().setData(pasteData)
    Toast.show('复制数据成功')
  }

  build() {
    Column() {
      this.selectionItem()
      List({ space: 1 }) {
        ForEach(this.selectionDatas, (item, index) => {
          ListItem() {
            if (item.year !== -1) {
              this.selectionItem(item)
            } else {
              Text(item.month)
                .textAlign(TextAlign.Center)
                .width('100%')
                .height(35)
                .margin({ left: 0, right: 0, top: 15, bottom: 0 })
            }
          }
        }, item => item.toString() + this.updateCount)
      }.layoutWeight(1)

      Column() {
        if (this.isSelectionLayoutShow) {
          this.selectionLayout()
        }
        Row() {
          Text('筛选').item().width('30%')
            .onClick(() => {
              this.isSelectionLayoutShow = !this.isSelectionLayoutShow
            })
          Text('复制到剪切板').item().width('70%')
            .onClick(() => {
              this.copyData()
            })
        }.height(40)
      }.padding({ left: 0, right: 0, top: 0, bottom: 3 })
    }
  }

  @Builder
  selectionItem(item?: AnalysisBean) {
    Row() {
      Text(item ? item.type : '类型').listItem()
      Text(item ? item.count.toFixed(0) : '操作数').listItem()
      Text(item ? item.fxResult.toFixed(1) : '收益(/)').listItem()
      Text(item ? (item.posCount * 100 / item.count).toFixed(1) + '%' : '胜率(%)').listItem()
      Text(item ? item.result.toFixed(1) : '总收益').listItem()
    }.width('100%')
    .height(35)
  }

  @Builder
  selectionLayout() {

    Column() {
      Text('1.选择对比品种')
        .fontSize(15)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(35)
        .padding({ left: 20, right: 20, top: 0, bottom: 0 })

      Grid() {
        ForEach(Types, (item, index) => {
          GridItem() {
            Text(item)
              .fontSize(15)
              .textAlign(TextAlign.Center)
              .width('45%')
              .height(35)
              .borderWidth(0.5)
              .borderRadius(10)
              .fontColor(this.selectedTypeIndex.indexOf(index) >= 0 ? $r('app.color.deepRed') : $r("app.color.black"))
              .borderColor(this.selectedTypeIndex.indexOf(index) >= 0 ? $r('app.color.deepRed') : $r("app.color.gray"))
          }.onClick(() => {
            let i = this.selectedTypeIndex.indexOf(index)
            if (i < 0) {
              this.selectedTypeIndex.push(index)
            } else {
              this.selectedTypeIndex.splice(i, 1)
            }
            this.refreshData()
          })
        })
      }
      .align(Alignment.Center)
      .layoutDirection(GridDirection.Row)
      .maxCount(2)
      .minCount(2)
      .rowsGap(5)
      .columnsGap(5)

    }.borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .padding({ left: 0, right: 0, top: 0, bottom: 3 })

    Column() {
      Text('2.选择对比时长(月)')
        .fontSize(15)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(35)
        .padding({ left: 20, right: 20, top: 0, bottom: 0 })

      Grid() {
        ForEach(Months, (item: number, index) => {
          GridItem() {
            Text(item.toString())
              .fontSize(15)
              .textAlign(TextAlign.Center)
              .width('30%')
              .height(35)
              .borderWidth(0.5)
              .borderRadius(10)
              .fontColor(this.selectedMonthIndex.indexOf(index) >= 0 ? $r('app.color.deepRed') : $r("app.color.black"))
              .borderColor(this.selectedMonthIndex.indexOf(index) >= 0 ? $r('app.color.deepRed') : $r("app.color.gray"))
          }.onClick(() => {
            let i = this.selectedMonthIndex.indexOf(index)
            if (i < 0) {
              this.selectedMonthIndex.push(index)
            } else {
              this.selectedMonthIndex.splice(i, 1)
            }
            this.refreshData()
          })
        })
      }
      .align(Alignment.Center)
      .layoutDirection(GridDirection.Row)
      .maxCount(3)
      .minCount(3)
      .rowsGap(5)
      .columnsGap(5)

    }
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .margin({ left: 0, right: 0, top: 3, bottom: 3 })
    .padding({ left: 0, right: 0, top: 0, bottom: 3 })
  }
}

@Extend(Text) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Center)
  .width('100%')
  .height('100%')
  .borderWidth(0.5)
  .borderRadius(10)
  .borderColor($r("app.color.gray"))
}

@Extend(Text) function listItem() {
  .fontSize(15)
  .textAlign(TextAlign.Center)
  .width('20%')
  .height('100%')
}