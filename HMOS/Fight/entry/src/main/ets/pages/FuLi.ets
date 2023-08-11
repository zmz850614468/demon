import FuLiBean from '../common/bean/FuLiBean';
import PreferenceModel from '../model/PreferenceModel';
import Logger from '../common/util/Logger';
import { BASIC, FU_LI, COUNT } from '../model/PreferenceModel';

@Entry
@Component
struct FuLi {
  @State fuLiBeanList: Array<FuLiBean> = [];
  isInit: boolean = false
  @State basic: number = 1.0
  @State fuLi: number = 1.2;
  @State count: number = 12;

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

  /**
   * 每次退出时，保存数据
   */
  onBackPress() {
    PreferenceModel.save(BASIC, this.basic)
    PreferenceModel.save(FU_LI, this.fuLi)
    PreferenceModel.save(COUNT, this.count)
  }

  initData() {
    this.isInit = true
  }

  /**
   * 计算复利结果信息
   */
  cal() {
    while (this.fuLiBeanList.length > 0) {
      this.fuLiBeanList.pop()
    }

    let fl = this.fuLi - 0.04
    for (let i = 0; i < 7; i++) {
      fl += 0.01
      let result = this.basic * Math.pow(fl, this.count)
      let bean = new FuLiBean((i + 1) + '', this.basic + '', Math.round(fl * 100 - 100) + '%', this.count + '', Math.round(result * 100) / 100 + '')
      this.fuLiBeanList.push(bean)
    }

  }

  build() {
    Column() {
      Text("复利")
        .fontSize(21)
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

      List({ space: 3 }) {
        ForEach(this.fuLiBeanList, (item: FuLiBean) => {
          ListItem() {
            this.fuLiLayout(item)
          }
        })
      }
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
            this.basic = Number.parseFloat(val)
            this.cal()
          })
      } else if (type === FU_LI) {
        TextInput({ text: this.fuLi.toString() })
          .input()
          .onChange((val) => {
            this.fuLi = Number.parseFloat(val)
            this.cal()
          })
      } else {
        TextInput({ text: this.count.toString() })
          .input()
          .onChange((val) => {
            this.count = Number.parseFloat(val)
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

