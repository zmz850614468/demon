import display from '@ohos.display'

@Entry
@Component
struct DisplayInfo {
  @State displayList: Array<string> = []

  aboutToAppear() {
    let displayIndo = display.getDefaultDisplaySync()
    this.displayList.push('设备的id:' + displayIndo.id)
    this.displayList.push('设备的名称:' + displayIndo.name)
    this.displayList.push('设备是否启用:' + displayIndo.alive)
    this.displayList.push('设备的状态:' + displayIndo.state)
    this.displayList.push('设备的刷新率:' + displayIndo.refreshRate)
    this.displayList.push('设备的屏幕旋转角度:' + displayIndo.rotation)
    this.displayList.push('设备的宽度-像素:' + displayIndo.width)
    this.displayList.push('设备的高度-像素:' + displayIndo.height)
    this.displayList.push('设备的屏幕密度:' + displayIndo.densityDPI) // 160,480等
    this.displayList.push('设备的逻辑密度:' + displayIndo.densityPixels) // 1,3等
    this.displayList.push('备的显示字体的缩放因子:' + displayIndo.scaledDensity) // 与上一个属性相同
    this.displayList.push('x方向每英寸屏幕物理像素值:' + displayIndo.xDPI)
    this.displayList.push('y方向每英寸屏幕物理像素值:' + displayIndo.yDPI)
  }

  build() {
    Column() {
      List({ space: 3 }) {
        ForEach(this.displayList, (item, index) => {
          ListItem() {
            this.item((index + 1) + '.' + item)
          }
        })
      }
    }
  }

  @Builder
  item(text: string) {
    Text(text)
      .fontSize(15)
      .textAlign(TextAlign.Start)
      .width('100%')
      .height(40)
      .padding({ left: 20, right: 20, top: 0, bottom: 0 })
  }
}