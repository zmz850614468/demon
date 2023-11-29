@Preview
@Component
export default struct CircleView {
  values: Array<number> = null // 数值
  disArr: Array<string> = null // 描述
  colors: Array<Resource> = [$r("app.color.circle_1"), $r("app.color.circle_2"), $r("app.color.circle_3")]

  build() {
    Column() {
      Stack() {
        DataPanel({ values: this.values, max: this.getTotal(this.values), type: DataPanelType.Circle })
          .width('100%')
          .height('100%')
        Column() {
          List({ space: 2 }) {
            ForEach(this.disArr, (item, index) => {
              ListItem() {
                Text(item)
                  .fontSize(13)
                  .fontColor(this.colors[index])
              }.width('100%')
            })
          }
        }
      }.align(Alignment.Center)
    }
  }

  /**
   * 获取总值
   * @param values
   * @returns
   */
  private getTotal(values: Array<number>): number {
    let total = 0;
    if (values) {
      values.forEach((item) => {
        total += item
      })
    }
    return total
  }
}

