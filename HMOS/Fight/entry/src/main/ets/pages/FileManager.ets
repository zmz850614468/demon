import fs from '@ohos.file.fs';
import Logger from '../util/Logger';
import BaseTable_v2 from '../database_v2/BaseTable_v2';
import OperateBean from '../database_v2/bean/OperateBean';
import DayOperateBean from '../database_v2/bean/DayOperateBean';
import { FileModel_v2 } from '../model/FileModel_v2';
import { FileUtil } from '../util/FileUtil';
import { PickerUtil } from '../util/PickerUtil';
import Toast from '../util/Toast';
import MoneyBean from '../database_v2/bean/MoneyBean';
import StringUtil from '../util/StringUtil';

@Entry
@Component
struct FileManager {
  private split = '$#$'
  path = getContext(this).tempDir + '/test2.txt'
  operateList: Array<OperateBean> = null
  dayOperateList: Array<DayOperateBean> = null
  moneyList: Array<MoneyBean> = null
  operateTable: BaseTable_v2<OperateBean> = null
  dayTable: BaseTable_v2<DayOperateBean> = null
  moneyTable: BaseTable_v2<MoneyBean> = null

// month_9=


  aboutToAppear() {
    this.operateTable = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
      this.operateTable.queryAll(new OperateBean(), (list: Array<OperateBean>) => {
        this.operateList = list
      })
    })

    this.dayTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
      this.dayTable.queryAll(new DayOperateBean(), (list: Array<DayOperateBean>) => {
        this.dayOperateList = list
      })
    })

    this.moneyTable = new BaseTable_v2(MoneyBean.TABLE_NAME, MoneyBean.SQL, MoneyBean.COLUMNS, (b: boolean) => {
      this.moneyTable.queryAll(new MoneyBean(), (list: Array<MoneyBean>) => {
        this.moneyList = list
      })
    })
  }

  /**
   * 列出所有子文件
   * @param files
   */
  async listFile(files: Array<string>) {
    let size = files.length
    let path = null
    for (let i = 0; i < size; i++) {
      path = files[i]
      let arr = await fs.listFile(path)
      this.showLog(path + ' -- ' + JSON.stringify(arr))
    }
  }

  /**
   * 1. 保存、转移数据
   * 2. 读取、保存数据
   */
  build() {
    Column() {

      Text('导出数据：数据库-->文件')
        .textAlign(TextAlign.Center)
        .fontSize(19)
        .width('100%')
        .height(50)
        .onClick(() => {
          this.export()
        })

      Text('导入数据：文件-->数据库')
        .textAlign(TextAlign.Center)
        .fontSize(19)
        .fontColor($r('app.color.deepRed'))
        .width('100%')
        .height(50)
        .margin({ left: 0, right: 0, top: 30, bottom: 0 })
        .onClick(() => {
          AlertDialog.show({
            title: "注意",
            message: "确定要导入新数据吗？",
            confirm: {
              value: "确定",
              action: () => {
                this.import()
              },
            }
          })
        })

      Text('清空 cache和temp 缓存文件')
        .textAlign(TextAlign.Center)
        .fontSize(19)
        .width('100%')
        .height(50)
        .margin({ left: 0, right: 0, top: 80, bottom: 0 })
        .onClick(() => {
          FileUtil.clearCacheAndTempFiles(getContext(this))

          FileUtil.listFiles(FileUtil.getAppDirs(getContext(this)))
        })
    }
    .height('100%')
    .justifyContent(FlexAlign.Center)
  }

  /**
   * 数据导出
   */
  private export() {
    let time = StringUtil.getTime()
    let fileNames = []
    fileNames.push('operate_' + time + '.txt')
    fileNames.push('day_' + time + '.txt')
    fileNames.push('money_' + time + '.txt')

    let basePath = getContext(this).cacheDir + '/'
    PickerUtil.documentSavePicker(fileNames, (err, result) => {
      if (err) {
        Toast.show('文件选择出错:' + JSON.stringify(err))
      }
      if (result) {
        this.writeToFile(this.operateList, result[0])
        this.writeToFile(this.dayOperateList, result[1])
        this.writeToFile(this.moneyList, result[2])

        // 1.导出数据到文件中
        // this.writeToFile(this.operateList, basePath + fileNames[0])
        // this.writeToFile(this.dayOperateList, basePath + fileNames[1])
        // this.writeToFile(this.moneyList, basePath + fileNames[2])
        //
        // this.showLog(JSON.stringify(result))
        // FileUtil.moveFile(basePath + fileNames[0], result[0])
        // FileUtil.moveFile(basePath + fileNames[1], result[1])
        // FileUtil.moveFile(basePath + fileNames[2], result[2])

        Toast.show('数据导出完成')
      }
    })
  }

  /**
   * 数据写入文件
   * @param path
   */
  private writeToFile(list: Array<object>, path: string) {
    let fileModel = new FileModel_v2()
    fileModel.open(path, fs.OpenMode.CREATE | fs.OpenMode.WRITE_ONLY)
    list.forEach((obj) => {
      fileModel.write(JSON.stringify(obj) + this.split)
    })
    fileModel.close()
  }

  /**
   * 数据导入
   */
  private import() {
    PickerUtil.documentSelectPicker(async (err, result) => {
      if (err) {
        Toast.show('文件选择出错:' + JSON.stringify(err))
      }
      if (result) {
        for (let i = 0; i < result.length; i++) {
          let disPath = getContext(this).cacheDir + '/' + FileUtil.getFileName(result[0])

          FileUtil.moveFile(result[0], disPath) // 移动文件到沙盒中

          Toast.show('读文件数据存在问题')

          let lastIndex = 0
          let index = 0
          let count = 0
          let data = fs.readTextSync(disPath)
          if (data) {
            while ((index = data.indexOf(this.split, index)) > 0) {
              let content = data.substring(lastIndex, index)

              // 保存数据到数据库中
              let bean = JSON.parse(content)
              if (content.indexOf('hands') > 0) { // 笔数据
                this.operateTable.insertData(bean, () => {
                })
              } else if (content.indexOf('poundage') > 0) { // 天数据
                this.dayTable.insertData(bean, () => {
                })
              } else if (content.indexOf('memo') > 0) { // 出入金
                this.moneyTable.insertData(bean, () => {
                })
              }
              index += 3
              lastIndex = index
            }
            // this.showLog('size :' + data.length)
            // this.showLog('length:' + length)
          }
        }
      }
    }

    )
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('FileManager', msg)
  }
}