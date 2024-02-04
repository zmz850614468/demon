import DataParse from '../dataAnalysis/DataParse'
import ArrayList from '@ohos.util.ArrayList';
import AnalysisBean from '../dataAnalysis/bean/AnalysisBean';
import Toast from '../util/Toast';
import RecordResult from '../dataAnalysis/bean/RecordResult';
import RecordBean from '../dataAnalysis/bean/RecordBean';
import DataParse_IO_10 from '../dataAnalysis/DataParse_IO_10';
import DataParse_P_10 from '../dataAnalysis/DataParse_P_10';
import DataParse_IO_5 from '../dataAnalysis/DataParse_IO_5';
import { SingleSelectDialog } from '../dialog/SingleSelectDialog';
import CircleView from '../component/CircleView';
import DataParse_RM_10 from '../dataAnalysis/DataParse_RM_10';
import DataParse_PG_10 from '../dataAnalysis/DataParse_PG_10';
import pasteboard from '@ohos.pasteboard';
import { Types } from '../constant/TypeConstant'
import DataParse_RU_10 from '../dataAnalysis/DataParse_RU_10';

@Entry
@Component
struct DataAnalysis {
  // private static selectItems: string[] = ['OI-10', 'P-10', 'PG-10', 'RM-10']
  private static selectItems: string[] = Types
  @State recordResult: RecordResult = null
  @State private title: string = DataAnalysis.selectItems[0]
  @State private analysisList: ArrayList<AnalysisBean> = null
  @State totalBean: AnalysisBean = null
  @State recordShow: boolean = false

  aboutToAppear() {
    this.refreshData(DataAnalysis.selectItems[0])
  }

  selectDialogController: CustomDialogController = new CustomDialogController({
    builder: SingleSelectDialog({
      title: '请选择类型',
      itemList: DataAnalysis.selectItems,
      selected: this.title,
      callBack: (selected: string) => {
        this.refreshData(selected)
      }
    }),
    autoCancel: true,
    alignment: DialogAlignment.Bottom,
    offset: { dx: 0, dy: -20 }
  })

  refreshData(type: string) {
    this.title = type
    switch (type) {
      case 'OI-10':
        this.analysisList = new DataParse_IO_10().getAnalysis()
        this.recordResult = DataParse_IO_10.record
        break
      case 'P-10':
        this.analysisList = new DataParse_P_10().getAnalysis()
        this.recordResult = DataParse_P_10.record
        break
      case 'OI-5':
        this.analysisList = new DataParse_IO_5().getAnalysis()
        this.recordResult = DataParse_IO_5.record
        break
      case 'RM-10':
        this.analysisList = new DataParse_RM_10().getAnalysis()
        this.recordResult = DataParse_RM_10.record
        break
      case 'PG-10':
        this.analysisList = new DataParse_PG_10().getAnalysis()
        this.recordResult = DataParse_PG_10.record
        break
      case 'RU-10':
        this.analysisList = new DataParse_RU_10().getAnalysis()
        this.recordResult = DataParse_RU_10.record
        break

      default:
        Toast.show('不存在的类型')
        return
    }

    // 总计
    this.totalBean = new AnalysisBean()
    this.totalBean.month = '总'
    this.analysisList.forEach((item) => {
      this.totalBean.count += item.count
      this.totalBean.pay += item.pay
      this.totalBean.result += item.result

      this.totalBean.posCount += item.posCount
      this.totalBean.negCount += item.negCount
      this.totalBean.pos_60_count += item.pos_60_count
      this.totalBean.neg_60_count += item.neg_60_count

      // /类型  反向操作结果
      this.totalBean.fxCount += item.fxCount
      this.totalBean.fxResult += item.fxResult

      // -1类型  反向操作结果
      this.totalBean.fxCount_1 += item.fxCount_1
      this.totalBean.fxResult_1 += item.fxResult_1
    })
  }

  build() {
    Stack() {
      Column() {
        Stack() {
          Text(this.title + " - 数据分析")
            .fontSize(19)
            .width('100%')
            .height(50)
            .textAlign(TextAlign.Center)
            .onClick(() => {
              this.selectDialogController.open()
            })
          // .gesture(LongPressGesture().onAction(() => {
          //   let pasteData = pasteboard.createData(pasteboard.MIMETYPE_TEXT_PLAIN,
          //     DataParse.copyMonthAnalysis(this.title, this.analysisList))
          //   pasteboard.getSystemPasteboard().setData(pasteData)
          //   Toast.show('复制数据完成')
          // }))

          Text("榜单")
            .fontSize(17)
            .width(60)
            .height(50)
            .textAlign(TextAlign.Center)
            .alignSelf(ItemAlign.End)
            .onClick(() => {
              this.recordShow = true
            })
        }
        .alignContent(Alignment.End)
        .width('100%')
        .height(50)


        Column() {

          // 分结果
          Column() {
            List({ space: 1 }) {
              ForEach(Array.from(this.analysisList), (item: AnalysisBean) => {
                ListItem() {
                  ItemView({ item: item })
                }
              })
            }
          }
          .width('100%')
          .layoutWeight(1)
          // .padding({ left: 0, right: 0, top: 0, bottom: 125 })

          // 总结果
          Column() {
            ForEach([this.totalBean], (item: AnalysisBean) => {
              ItemView({ item: item })
                .padding({ left: 0, right: 0, top: 0, bottom: 3 })
            })
          }

        }.layoutWeight(1)

      }

      Panel(this.recordShow) {
        Column() {
          Stack() {
            Text('榜单')
              .fontSize(17)
              .fontWeight(FontWeight.Bold)
              .textAlign(TextAlign.Center)
              .width('100%')
              .height('100%')
            Text('X')
              .fontSize(17)
              .textAlign(TextAlign.Center)
              .fontColor($r('app.color.black'))
              .width(45)
              .height(45)
              .onClick((e) => {
                this.recordShow = false
              })
          }
          .align(Alignment.End)
          .width('100%')
          .height(45)

          if (this.recordResult !== null) {
            ForEach([this.recordResult], (item: RecordResult) => {
              Row() {
                RecordView({ name: '月低点榜', list: item.monthLowList })
              }

              Row() {
                RecordView({ name: '月收益榜', list: item.profitMonthList })
                RecordView({ name: '月亏损榜', list: item.lossMonthList })
                  .margin({ left: 3, right: 0, top: 0, bottom: 0 })
              }.margin({ left: 0, right: 0, top: 3, bottom: 0 })

              Row() {
                RecordView({ name: '单笔收益榜', list: item.profitOperateList })
                RecordView({ name: '单笔亏损榜', list: item.lossOperateList })
                  .margin({ left: 3, right: 0, top: 0, bottom: 0 })
              }.margin({ left: 0, right: 0, top: 3, bottom: 0 })
            })
          }
        }
      }.type(PanelType.Temporary).mode(PanelMode.Full)
    }
  }
}

@Preview
@Component
struct RecordView {
  private name: string
  private list: Array<RecordBean>

  build() {
    Column() {
      Text(this.name)
        .fontSize(17)
        .textAlign(TextAlign.Center)
        .width('100%')
        .height(35)

      // CircleView({ values: RecordResult.getValues(this.list), disArr: RecordResult.getDisArr(this.list) })
      //   .width('100%')
      //   .height(190)
      //   .margin({ left: 0, right: 0, top: 5, bottom: 0 })

      List({ space: 1 }) {
        ForEach(this.list, (item: RecordBean, index) => {
          ListItem() {
            Row() {
              Text((index + 1).toString())
                .fontSize(17)
                .textAlign(TextAlign.Center)
                .width('15%')
                .height('100%')

              Text(item.time)
                .fontSize(17)
                .textAlign(TextAlign.Center)
                .width('55%')
                .height('100%')

              Text(item.record == 0 ? '' : item.record.toString())
                .fontSize(17)
                .textAlign(TextAlign.Center)
                .width('30%')
                .height('100%')
            }.width('100%')
            .height(40)
          }
        })
      }
    }
    .align(Alignment.Top)
    .borderWidth(1)
    .borderRadius(15)
    .borderColor($r("app.color.gray"))
    .width('50%')
    .height(160)
  }
}

@Preview
@Component
struct ItemView {
  private item: AnalysisBean

  build() {
    Stack() {
      Column() {
        Row() {
          Text('次数:' + this.item.count)
            .fontSize(16)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)
          Text('费用:' + this.item.pay)
            .fontSize(16)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)

        }.width('100%')
        // .margin({ left: 0, right: 0, top: 3, bottom: 0 })
        .height(30)

        // Row() {
        //   Text('正-负:' + this.item.posCount + "-" + this.item.negCount)
        //     .fontSize(16)
        //     .width('50%')
        //     .height('100%')
        //     .textAlign(TextAlign.Center)
        //   Text('60(正-负):')
        //     .fontSize(17)
        //     .width('25%')
        //     .height('100%')
        //     .textAlign(TextAlign.End)
        //   Text(this.item.pos_60_count + "-" + this.item.neg_60_count)
        //     .text(this.getTipColor(this.item.pos_60_count - this.item.neg_60_count, 100))
        //     .textAlign(TextAlign.Start)
        // }.width('100%')
        // .height('33%')
        Row() {
          Text('数量(/):' + this.item.fxCount)
            .fontSize(16)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)

          Row() {
            Text('结果(/):')
              .fontSize(17)
              .height('100%')
            Text(this.item.fxResult.toFixed(0))
              .fontSize(17)
              .height('100%')
              .padding({ left: 0, right: 0, top: 0, bottom: 0 })
              .fontColor(this.getTipColor(this.item.fxResult, 100000))
          }
          .justifyContent(FlexAlign.Center)
          .width('50%')
          .height('100%')

        }.width('100%')
        .height(30)

        if (this.item.fxCount_1 > 0) {
          Row() {
            Text('数量(-1):' + this.item.fxCount_1)
              .fontSize(16)
              .width('50%')
              .height('100%')
              .textAlign(TextAlign.Center)

            Row() {
              Text('结果(-1):')
                .fontSize(17)
                .height('100%')
              Text(this.item.fxResult_1.toFixed(0))
                .fontSize(17)
                .height('100%')
                .padding({ left: 0, right: 0, top: 0, bottom: 0 })
                .fontColor(this.getTipColor(this.item.fxResult_1, 100000))
            }
            .justifyContent(FlexAlign.Center)
            .width('50%')
            .height('100%')
          }.width('100%')
          .height(30)
        }

        Row() {
          Text('胜率:' + (this.item.posCount * 100 / this.item.count).toFixed(2) + '%')
            .fontSize(16)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)

          Row() {
            Text('总结果:')
              .fontSize(16)
              .fontWeight(FontWeight.Bold)
              .height('100%')
            Text(this.item.result.toString())
              .fontSize(17)
              .textAlign(TextAlign.Start)
              .fontColor(this.getTipColor(this.item.result, 1))
              .height('100%')
          }
          .justifyContent(FlexAlign.Center)
          .width('50%')
          .height('100%')

        }.width('100%')
        .height(33)

      }
      // .margin({ left: 0, right: 0, top: 5, bottom: 0 })

      Text(this.item.month + (this.item.year !== undefined ? '/' + this.item.year : ''))
        .fontSize(18)
        .fontColor($r("app.color.red"))
        .padding({ left: 5, right: 5, top: 5, bottom: 5 })
    }
    .width('100%')
    .borderWidth(1)
    .alignContent(Alignment.TopStart)
    .borderRadius(15)
    .borderColor($r("app.color.gray"))
    .margin({ left: 0, right: 0, top: 0, bottom: 5 })
  }

  /**
   * 获取提醒颜色
   * @param result
   * @param count2
   * @param base
   * @returns
   */
  getTipColor(result: number, base: number): ResourceColor {
    if (result >= base) {
      return $r("app.color.red")
    } else if (result > 0) {
      return $r("app.color.red_01")
    } else if (result <= -base) {
      return $r("app.color.green")
    } else {
      return $r("app.color.deepGreen")
    }
  }
}

@Extend(Text) function text(value: ResourceColor) {
  .fontSize(17)
  .width('25%')
  .height('100%')
  .textAlign(TextAlign.Start)
  .fontColor(value)
}

