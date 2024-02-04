import { CostBean, costDataList } from "../bean/CostBean";

@Entry
@Component
struct Cost {
  private listData: CostBean[] = costDataList

  build() {
    Column() {
      Text('交易费用')
        .fontSize(19)
        .width('100%')
        .height(50)
        .textAlign(TextAlign.Center)

      List() {
        ForEach(this.listData, (item: CostBean) => {
          ListItem() {
            Row() {
              Text(item.name)
                .fontSize(17)
                .width('50%')
                .height(50)
                .textAlign(TextAlign.Center)

              Text(item.cost + '元')
                .fontSize(17)
                .width('50%')
                .height(50)
                .textAlign(TextAlign.Center)
            }
            .margin({ left: 0, right: 0, top: 0, bottom: 3 })
            .borderWidth(0.5)
            .borderRadius(15)
            .borderColor($r("app.color.gray"))
          }
        })
      }
    }
  }
}