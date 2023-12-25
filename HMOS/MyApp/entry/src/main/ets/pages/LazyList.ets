import { MyDataSource } from 'myLibrary'

@Entry
@Component
struct LazyList {
  data: MyDataSource = new MyDataSource()
  count = 0
  delete = 0

  build() {
    Column() {
      List({ space: 1 }) {
        LazyForEach(this.data, (item) => {
          ListItem() {
            Text(item)
              .width('100%')
              .height(40)
          }
        }, (item: string) => item.toString())
      }.width('100%')
      .height('94%')

      Row() {
        Text('添加')
          .width('20%')
          .height('100%')
          .onClick(() => {
            // if (this.count % 2 == 0) {
            // this.data.pushData('value: ' + this.count)
            // } else {
            this.data.addData(0, 'value: ' + this.count)
            // this.data.addData(this.data.totalCount(), 'value: ' + this.count)
            // }
            this.count++
          })
        Text('删除')
          .width('20%')
          .height('100%')
          .onClick(() => {
            // if (this.delete % 2 == 0) {
            //   this.data.deleteData(this.data.totalCount() - 1)
            // } else {
            // this.data.deleteData(this.data.totalCount() - 1)
            this.data.deleteData(0)
            // }
            this.delete++
          })

        Text('全部替换')
          .width('20%')
          .height('100%')
          .onClick(() => {
            let size = this.data.totalCount()
            // for (let i = size - 1; i >= 0; i--) {
            //   this.data.deleteData(i)
            // }
            this.data.deleteAll()

            for (let i = 0; i < size / 2; i++) {
              // this.data.pushData('value: ' + this.count++)
              // this.data.addTest('value: ' + this.count++)
            }
            this.data.notifyDataReload()

            // setTimeout(() => {
            //   this.data.notifyDataReload()
            // }, 30)
          })
      }.width('100%')
      .height('6%')
    }
  }
}