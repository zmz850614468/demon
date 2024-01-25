import { Rdb_v4 } from 'myLibrary'
import Logger from 'myLibrary/src/main/ets/util/Logger'
import V4Bean from '../bean/data/V4Bean'

@Entry
@Component
struct DbDemo {
  @State msg: string = ''
  rdb: Rdb_v4<V4Bean> = null
  list: Array<V4Bean> = null

  aboutToAppear() {
    this.rdb = new Rdb_v4(getContext(this), V4Bean, (err) => {
      if (err) {
        this.msg = '数据库初始化结果:' + JSON.stringify(err)
      } else {
        this.msg = '数据库初始化结果:' + true
      }
    })
  }

  build() {
    Column() {
      Text(this.msg).item()

      Button('曾').width('60%')
        .margin({ left: 0, right: 0, top: 10, bottom: 0 })
        .onClick(() => {
          let bean = new V4Bean()
          bean.result = Math.round(Math.random() * 1000)
          bean.date = new Date().getMilliseconds().toString()
          bean.memo = '默认备注'
          this.rdb.insertBean(bean, (err, count) => {
            this.msg = '数据插入数据id结果：' + count
          })
        })

      Button('查').width('60%')
        .margin({ left: 0, right: 0, top: 10, bottom: 0 })
        .onClick(() => {
          this.rdb.queryAll((err, list) => {
            this.list = list
            this.msg = '数据查询总条数：' + this.list.length
            this.showLog('查询结果：' + JSON.stringify(this.list))
          })
        })

      Button('删').width('60%')
        .margin({ left: 0, right: 0, top: 10, bottom: 0 })
        .onClick(() => {
          if (!this.list || this.list.length == 0) {
            this.msg = '查询结果为空，不能删除'
          } else {
            this.rdb.deleteById(this.list.pop().id, (err, rows) => {
              this.msg = '删除数据条数:' + rows
            })
          }
        })

    }.justifyContent(FlexAlign.Center)
    .padding({ left: 5, right: 5, top: 10, bottom: 10 })
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('DbDemo', msg)
  }
}

@Extend(Text) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Center)
  .width('100%')
  .height(40)
}
