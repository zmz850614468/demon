import { CostListSource } from "./CostListSource";
import { CostBean, costDataList } from "../common/bean/CostBean";

@Component
export default struct CostList {
  @Provide public costList: CostListSource = new CostListSource();


  // @Builder initData(costList: CostListSource<CostBean> ) {
  //   costList.setListData(costDataList);
  // }

  // this.costList.setListData(costDataList)
  build() {
    Row() {
      List() {
        LazyForEach(this.costList, (item) => {
          ListItem() {
            Row() {
              Text(item?.name)
                .fontSize(17)
                .width('50%')
                .height(50)
                .textAlign(TextAlign.Center)

              Text(item?.cost)
                .fontSize(17)
                .width('50%')
                .height(50)
                .textAlign(TextAlign.Center)
            }
            .borderWidth(1)
            .borderRadius(15)
            .borderColor($r("app.color.gray"))
          }
        })
      }
    }
  }
}