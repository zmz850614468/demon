import { Toast } from 'myLibrary/src/main/ets/util/Toast';
import { AccountBean } from '../bean/data/AccountBean';

@Preview
@CustomDialog
export struct AddAccountDialog {
  callback: (bean: AccountBean) => void = null // 回调监听

  @State group: string = ''
  @State name: string = ''
  @State account: string = ''
  @State pwd: string = ''
  private controller: CustomDialogController = new CustomDialogController({
    builder: AddAccountDialog()
  });

  /**
   * 点击确认
   */
  private onOkClick() {
    if (this.group === '') {
      Toast.show('组别不能为空')
      return
    }
    if (this.name === '') {
      Toast.show('名称不能为空')
      return
    }
    if (this.account === '') {
      Toast.show('账号不能为空')
      return
    }
    if (this.pwd === '') {
      Toast.show('密码不能为空')
      return
    }

    let bean = new AccountBean()
    bean.typeGroup = this.group
    bean.name = this.name
    bean.account = this.account
    bean.pwd = this.pwd
    this.callback(bean)
    // this.controller.close()
  }

  build() {
    Column({ space: 1 }) {
      Row() {
        Text('添加账号信息')
          .textAlign(TextAlign.Center)
          .fontSize(17)
          .height(40)
      }.width('100%')
      .justifyContent(FlexAlign.Center)

      Row() {
        this.textItem('组别：')
        TextInput().item().key('group').tabIndex(1)
          .onChange((value) => {
            this.group = value
          })
      }

      Row() {
        this.textItem('名称：')
        TextInput().item().key('name').tabIndex(2)
          .onChange((value) => {
            this.name = value
          })
      }

      Row() {
        this.textItem('账号：')
        TextInput().item().key('account').tabIndex(3)
          .onChange((value) => {
            this.account = value
          })
      }

      Row() {
        this.textItem('密码：')
        TextInput().item().key('pwd').tabIndex(4)
          .onChange((value) => {
            this.pwd = value
          })
      }

      Row() {
        Text('确认添加')
          .textAlign(TextAlign.Center)
          .fontSize(17)
          .width('100%')
          .height(40)
          .fontColor($r('app.color.white'))
          .backgroundColor($r('app.color.deepRed'))
          .onClick(() => {
            this.onOkClick()
          })
          .borderRadius(20)
      }.width('80%')
      .margin({ left: 0, right: 0, top: 20, bottom: 10 })
      .justifyContent(FlexAlign.Center)

    }
    .padding({ left: 0, right: 0, top: 15, bottom: 15 })
  }

  @Builder
  textItem(name: string) {
    Text(name)
      .fontSize(17)
      .textAlign(TextAlign.End)
      .width('30%')
      .height(40)
  }
}

@Extend(TextInput) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Start)
  .width('60%')
  .height(35)
  .padding({ left: 10, right: 3, top: 0, bottom: 0 })
}