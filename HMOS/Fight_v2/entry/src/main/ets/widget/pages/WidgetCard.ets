@Entry
@Component
struct WidgetCard {


  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    console.error('WidgetCard', msg)
  }

  build() {
    Column() {
      Row() {
        Image($r('app.media.fl')).itemImage()
          .onClick(() => {
            this.to('1')
          })
        Image($r('app.media.ic_record')).itemImage()
          .onClick(() => {
            this.to('2')
          })
      }
      .width('100%')
      .height('50%')
      .justifyContent(FlexAlign.SpaceAround)
      .padding({ left: 0, right: 0, top: 10, bottom: 5 })

      Row() {
        Image($r('app.media.ic_memo')).itemImage()
          .onClick(() => {
            this.to('3')
          })
        Image($r('app.media.fxsj')).itemImage()
          .onClick(() => {
            this.to('4')
          })
      }
      .height('50%')
      .width('100%')
      .justifyContent(FlexAlign.SpaceAround)
      .padding({ left: 0, right: 0, top: 5, bottom: 10 })

    }
  }

  to(msg: string) {
    postCardAction(this, {
      'action': 'router',
      'abilityName': 'EntryAbility',
      'params': {
        'message': msg
      }
    })
  }
}

@Extend(Image) function itemImage() {
  .height('85%')
  .borderWidth(1)
  .borderRadius(10)
  .borderColor($r("app.color.gray"))
}

