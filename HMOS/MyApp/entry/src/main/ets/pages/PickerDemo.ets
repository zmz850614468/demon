import fs from '@ohos.file.fs';
import picker from '@ohos.file.picker'
import { FileModel_v2, FileUtil } from 'myLibrary'
import uri from '@ohos.uri';

@Entry
@Component
struct PickerDemo {
  build() {
    Column() {

      Text('导出数据')
        .textAlign(TextAlign.Center)
        .fontSize(19)
        .width('100%')
        .height(50)
        .onClick(() => {
          // this.export()
          FileUtil.listFiles(FileUtil.getAppDirs(getContext(this)))
        })

      Text('导入数据')
        .textAlign(TextAlign.Center)
        .fontSize(19)
        .fontColor($r('app.color.deepRed'))
        .width('100%')
        .height(50)
        .margin({ left: 0, right: 0, top: 30, bottom: 0 })
        .onClick(() => {
          FileUtil.clearCacheAndTempFiles(getContext(this))
          // AlertDialog.show({
          //   title: "注意",
          //   message: "确定要导入新数据吗？",
          //   confirm: {
          //     value: "确定",
          //     action: () => {
          //       this.import()
          //     },
          //   }
          // })
        })

    }
    .height('100%')
    .justifyContent(FlexAlign.Center)
  }

  /**
   * 数据导出
   */
  private export() {
    let options = new picker.DocumentSaveOptions()
    options.newFileNames = ['test.txt']

    new picker.DocumentViewPicker().save(options).then((result) => {
      this.showLog('uri :' + result[0])
      let fileModel = new FileModel_v2()
      fileModel.open(result[0], fs.OpenMode.CREATE | fs.OpenMode.WRITE_ONLY)
      fileModel.write('DocumentViewPicker测试')
      fileModel.close()
    })
  }

  /**
   * 数据导入
   */
  private import() {
    let options = new picker.DocumentSelectOptions()
    new picker.DocumentViewPicker().select(options).then((result) => {
      this.showLog('uri :' + result[0])
      let fileModel = new FileModel_v2()
      fileModel.open(result[0], fs.OpenMode.READ_ONLY)

      // let stat = fileModel.getStat()
      // this.showLog('stat:' + JSON.stringify(stat))


      // FileUtil.moveFile(result[0], getContext(this).cacheDir + '/exam.txt')
      // this.showLog('file exit:' + fileModel.isExit())
      // fileModel.readText((data) => {
      //   this.showLog('文件数据：' + data)
      // })

      setTimeout(() => {
        FileUtil.listFiles(FileUtil.getAppDirs(getContext(this)))
      }, 1000)

    })


    FileUtil.listFiles(FileUtil.getAppDirs(getContext(this)))
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    console.error('PickerDemo', msg)
  }
}