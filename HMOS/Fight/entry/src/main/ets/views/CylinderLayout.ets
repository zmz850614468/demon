@Preview
@Component
export struct CylinderLayout {
  max: number = 5
  value: number = 0
  day: number = 1

  build() {
    Stack() {
      if (this.value == 0) {
        Text(this.value.toString())
          .fontSize(11)
          .margin({ left: 0, right: 0, top: 0, bottom: 15 })
      } else {
        Column() {
          Text(this.value.toString())
            .fontSize(11)
        }
        .width('80%')
        .height((this.value * 100 / this.max) + '%')
        .backgroundColor($r('app.color.blue'))
        .borderRadius(7)
      }

      Text()
        .width('100%')
        .height(0.5)
        .backgroundColor($r('app.color.black'))
        .margin({ left: 0, right: 0, top: 0, bottom: 15 })

      Text(this.day.toString())
        .fontSize(11)
        .width('100%')
        .height(15)
        .textAlign(TextAlign.Center)
        .backgroundColor($r('app.color.white'))
    }
    .width('100%')
    .height('100%')
    .align(Alignment.Bottom)
  }
}

@Preview
@Component
export struct Y {
  max: number = 5

  build() {
    Stack() {
      Column() {
        Text()
          .width(0.5)
          .layoutWeight(1)
          .backgroundColor($r('app.color.black'))

        Text()
          .width(0.5)
          .height(15)
          .backgroundColor($r('app.color.white'))
      }

      Column() {
        Column() {
          Text('5')
            .textAlign(TextAlign.Center)
            .fontSize(11)
        }
        .width('100%')
        .layoutWeight(1)

        Column() {
          Text('4')
            .textAlign(TextAlign.Center)
            .fontSize(11)
        }
        .width('100%')
        .layoutWeight(1)

        Column() {
          Text('3')
            .textAlign(TextAlign.Center)
            .fontSize(11)
        }
        .width('100%')
        .layoutWeight(1)

        Column() {
          Text('2')
            .textAlign(TextAlign.Center)
            .fontSize(11)
        }
        .width('100%')
        .layoutWeight(1)

        Column() {
          Text('1')
            .textAlign(TextAlign.Center)
            .fontSize(11)
        }
        .width('100%')
        .layoutWeight(1)
      }
    }
    .align(Alignment.End)
    .width('100%')
    .height('100%')
  }
}