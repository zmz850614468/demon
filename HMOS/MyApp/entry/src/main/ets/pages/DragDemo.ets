import Logger from 'myLibrary/src/main/ets/util/Logger'

@Entry
@Component
struct DragDemo {
  build() {
    Column() {
      Type_1().height('40%')

      Type_2().height('60%')
    }
  }
}

@Preview
@Component
struct Type_1 {
  @State list: Array<string> = ['item_1', 'item_2', 'item_3', 'item_4']
  @State dragIndex: number = 0
  @State dragStarted: boolean = false

  /**
   * 移动位置
   * @param from
   * @param to
   */
  move(from: number, to: number) {
    let item = this.list.splice(from, 1)[0]
    this.list.splice(to, 0, item)
  }

  build() {
    List({ space: 1 }) {
      ForEach(this.list, (item, index) => {
        ListItem() {
          Text(this.dragStarted && this.dragIndex == index ? '' : item)
            .fontSize(17)
            .padding({ left: 10, right: 10, top: 10, bottom: 10 })
            .borderWidth(1)
            .borderRadius(10)
            .borderColor(this.dragStarted && this.dragIndex == index ? $r('app.color.deepRed') : $r("app.color.gray"))
        }
        .width('100%')
        .height(40)
        .onDragStart(() => {
          this.dragStarted = true
          this.showLog('onDragStart() -> ')
          return this.drawItem(item)
        })
        .onTouch((e) => {
          if (e.type === TouchType.Down) {
            this.dragIndex = index // 获取当前拖拽子组件的索引
            this.showLog('onTouch:' + this.dragIndex)
          } else if (e.type === TouchType.Up) {
            this.dragStarted = false
          }
        })

      })
    }
    .onDrop((e: DragEvent, params: string) => {
      let p = JSON.parse(params)
      this.showLog('onDrop:' + params)
      this.move(this.dragIndex, p.insertIndex)
    })
  }

  @Builder drawItem(name: string) {
    Column() {
      Text(name)
        .padding({ left: 10, right: 10, top: 10, bottom: 10 })
        .height(40)
        .fontSize(17)
        .borderRadius(10)
        .textAlign(TextAlign.Center)
        .backgroundColor($r('app.color.deepRed'))
    }
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('DrawDemo', msg)
  }
}

@Preview
@Component
struct Type_2 {
  @State list: Array<string> = ['item_1', 'item_2', 'item_3', 'item_4']
  @State list_2: Array<string> = ['item_7', 'item_8', 'item_9', 'item_10']
  @State dragIndex: number = 0
  @State dragStarted: boolean = false

  /**
   * 移动位置
   * @param from
   * @param to
   */
  move(from: number, to: number) {
    let item = this.list.splice(from, 1)[0]
    this.list_2.splice(to, 0, item)
  }

  build() {
    Column() {
      List({ space: 1 }) {
        ForEach(this.list, (item, index) => {
          ListItem() {
            Text(this.dragStarted && this.dragIndex == index ? '' : item)
              .textAlign(TextAlign.Center)
              .width('25%')
              .fontSize(17)
              .padding({ left: 0, right: 0, top: 10, bottom: 10 })
              .borderWidth(1)
              .borderRadius(10)
              .borderColor(this.dragStarted && this.dragIndex == index ? $r('app.color.deepRed') : $r("app.color.gray"))
          }
          .onDragStart(() => {
            this.dragStarted = true
            this.showLog('onDragStart() -> ')
            return this.drawItem(item)
          })
          .onTouch((e) => {
            if (e.type === TouchType.Down) {
              this.dragIndex = index // 获取当前拖拽子组件的索引
              this.showLog('onTouch:' + this.dragIndex)
            } else if (e.type === TouchType.Up) {
              this.dragStarted = false
            }
          })
        })
      }.height(40)
      .listDirection(Axis.Horizontal)

      List({ space: 1 }) {
        ForEach(this.list_2, (item, index) => {
          ListItem() {
            Text(item)
              .textAlign(TextAlign.Center)
              .width('25%')
              .fontSize(17)
              .padding({ left: 0, right: 0, top: 10, bottom: 10 })
              .borderWidth(1)
              .borderRadius(10)
              .borderColor($r("app.color.gray"))
          }.width('100%')
          .height(40)
        })
      }
      .margin({ left: 0, right: 0, top: 20, bottom: 0 })
      .onDrop((e: DragEvent, params: string) => {
        let p = JSON.parse(params)
        this.showLog('onDrop:' + params)
        this.move(this.dragIndex, p.insertIndex)
      })
    }
  }

  @Builder drawItem(name: string) {
    Column() {
      Text(name)
        .width('25%')
        .padding({ left: 0, right: 0, top: 10, bottom: 10 })
        .height(40)
        .fontSize(17)
        .borderRadius(10)
        .textAlign(TextAlign.Center)
        .backgroundColor($r('app.color.deepRed'))
    }
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('DrawDemo', msg)
  }
}
