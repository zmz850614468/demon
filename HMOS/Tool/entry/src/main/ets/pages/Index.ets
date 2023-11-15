import preferences from '@ohos.data.preferences'
// import PreferenceModel from '../model/PreferenceModel'
import myPreference, { Test } from '../model/PreferenceModel'
import Toast from '../util/Toast'

@Entry
@Component
struct Index {
  @State message: string = 'Hello World'

  build() {
    Row() {
      Column() {
        Text(this.message)
          .fontSize(50)
          .fontWeight(FontWeight.Bold)
          .onClick((e) => {
            let arr: Array<string> = []
            arr.push('key0')
            arr.push('key1')
            arr.push('key2')
            myPreference.save(Test, arr)
            setTimeout(async () => {
              let array: Array<string> = []
              let content = await myPreference.get(Test)
              Toast.show(array[2])
            }, 2000)
          })
      }
      .width('100%')
    }
    .height('100%')
  }
}