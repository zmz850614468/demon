import FuLiBean from '../common/bean/FuLiBean';
import PreferenceModel, { TARGET_LIST } from '../model/PreferenceModel';
import Logger from '../common/util/Logger';
import { BASIC, FU_LI, COUNT } from '../model/PreferenceModel';
import StringUtil from '../common/util/StringUtil';
import Toast from '../common/util/Toast';

@Entry
@Component
struct FuLi {
  @State fuLiBeanList: Array<FuLiBean> = [];
  @State basic: number = 1.0
  @State fuLi: number = 1.2;
  @State count: number = 12;
  @State targetList: Array<FuLiBean> = []

  /**
   * 每次显示页面时，加载数据
   */
  onPageShow() {
    PreferenceModel.get(BASIC).then(value => {
      if (!(value === undefined)) {
        this.basic = value
        Logger.error('tag', 'basic = ' + this.basic)
      } else {
        Logger.error('tag', 'basic undefined ' + value)
      }
    })
    PreferenceModel.get(FU_LI).then(value => {
      if (!(value === undefined)) {
        this.fuLi = value
      }
    })
    PreferenceModel.get(COUNT).then(value => {
      if (!(value === undefined)) {
        this.count = value
      }
    })
  }

  aboutToAppear() {
    this.initData()
  }

  /**
   * 每次退出时，保存数据
   */
  onBackPress() {
    PreferenceModel.save(BASIC, this.basic)
    PreferenceModel.save(FU_LI, this.fuLi)
    PreferenceModel.save(COUNT, this.count)
  }

  private async initData() {
    let listValue = await PreferenceModel.get(TARGET_LIST)
    Object.assign(this.targetList, JSON.parse(listValue))
  }

  /**
   * 计算复利结果信息
   */
  cal() {
    while (this.fuLiBeanList.length > 0) {
      this.fuLiBeanList.pop()
    }

    let fl = this.fuLi - 0.02
    for (let i = 0; i < 3; i++) {
      fl += 0.01
      let result = this.basic * Math.pow(fl, this.count)
      let bean = new FuLiBean((i + 1).toString(), this.basic.toString(), Math.round(fl * 100 - 100) + '%', this.count.toString(), result.toFixed(2))
      this.fuLiBeanList.push(bean)
    }
  }

  private lastTime: number = 0

  build() {
    Column() {
      Text("复利")
        .fontSize(19)
        .width('100%')
        .height(50)
        .textAlign(TextAlign.Center)

      Blank()
        .width('100%')
        .height(1)
        .color($r('app.color.gray'))

      this.inputItem('基数', BASIC)
      this.inputItem('复利', FU_LI)
      this.inputItem('次数', COUNT)

      Blank()
        .width('100%')
        .height(1)
        .color($r('app.color.gray'))
        .margin({ top: 5 })

      this.fuLiLayout(new FuLiBean('序号', '基数', '利率', '次数', '结果'))

      List({ space: 2 }) {
        ForEach(this.fuLiBeanList, (item: FuLiBean) => {
          ListItem() {
            this.fuLiLayout(item)
          }.onClick(async (e) => {
            let time = e.timestamp
            if (time - this.lastTime < 300000000) {
              let bean = new FuLiBean((await StringUtil.getDayTimeAsync()).slice(2), item.basic, item.fuLi, item.count, item.result)
              this.targetList.push(bean)
              if (this.targetList.length > 5) {
                this.targetList.shift()
              }

              PreferenceModel.save(TARGET_LIST, JSON.stringify(this.targetList))
              Toast.show('数据添加成功')
            }
            this.lastTime = time
          })
        })
      }

      Stack() {
        List({ space: 1 }) {
          ForEach(this.targetList, (item) => {
            ListItem() {
              this.fuLiLayout(item)
            }.onClick((e) => {
              let time = e.timestamp
              if (time - this.lastTime < 300000000) {
                AlertDialog.show({
                  title: "注意",
                  message: "确定删除改数据吗？",
                  confirm: {
                    value: "确定",
                    action: () => {
                      let index = this.targetList.indexOf(item)
                      this.targetList.splice(index, 1)
                      PreferenceModel.save(TARGET_LIST, JSON.stringify(this.targetList))
                    },
                  }
                })
              }
              this.lastTime = time
            })
          })
        }
      }.width('100%')
      .height('51%')
      .align(Alignment.Bottom)
    }
  }

  /**
   * 复利单行数据显示
   * @param item
   */
  @Builder
  fuLiLayout(item: FuLiBean) {
    Row() {
      Text(item?.index)
        .fuLiItem()
      Text(item?.basic)
        .fuLiItem()
      Text(item?.fuLi)
        .fuLiItem()
      Text(item?.count)
        .fuLiItem()
      Text(item?.result)
        .fuLiItem()
    }
  }

  @Builder
  inputItem(title: string, type: string) {
    Row() {
      Text(title)
        .fontSize(17)
        .width('40%')
        .height('100%')
        .textAlign(TextAlign.Center)

      if (type === BASIC) {
        TextInput({ text: this.basic.toString() })
          .input()
          .onChange((val) => {
            if (val === 'NaN' || val === 'Na' || val === 'N' || val === '') {
              this.basic = 0
            } else {
              this.basic = Number.parseFloat(val)
            }
            this.cal()
          })
      } else if (type === FU_LI) {
        TextInput({ text: this.fuLi.toString() })
          .input()
          .onChange((val) => {
            if (val === 'NaN' || val === 'Na' || val === 'N' || val === '') {
              this.fuLi = 0
            } else {
              this.fuLi = Number.parseFloat(val)
            }
            this.cal()
          })
      } else {
        TextInput({ text: this.count.toString() })
          .input()
          .onChange((val) => {
            if (val === 'NaN' || val === 'Na' || val === 'N' || val === '') {
              this.count = 0
            } else {
              this.count = Number.parseFloat(val)
            }
            this.cal()
          })
      }
    }
    .width('100%')
    .height(40)
    .margin({ top: 5 })
  }
}

/**
 * 复利显示属性
 */
@Extend(Text) function fuLiItem() {
  .fontSize(17)
  .width('20%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}

/**
 * 输入框属性
 */
@Extend(TextInput) function input() {
  .fontSize(17)
  .width('60%')
  .height('100%')
  .type(InputType.Number)
  .inputFilter('[0-9]*.[0-9]*')
  .textAlign(TextAlign.Center)
}

