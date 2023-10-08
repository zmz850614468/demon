import CostList from "../views/CostList";
// import { CostBean, costDataList } from "../common/bean/CostBean";

@Entry
@Component
struct Cost {

  build() {
    Column() {
      Text("交易费用")
        .fontSize(19)
        .width('100%')
        .height(50)
        .textAlign(TextAlign.Center)
      Scroll() {
        // Column() {
          // Text("交易费用")
          //   .fontSize(21)
          //   .width('100%')
          //   .height(50)
          //   .textAlign(TextAlign.Center)

          CostList()
        // }
        // .width('100%')
      }
    }
  }
}