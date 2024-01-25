import Logger from 'myLibrary/src/main/ets/util/Logger'
import ArrayList from '@ohos.util.ArrayList'

@Entry
@Component
struct ReflectDemo {
  msgList: Array<string> = new Array()

  aboutToAppear() {
    // let test: DemoBean = JSON.parse('{}')
    // test.id = 626
    // test.name = 'testing'
    // this.showLog('test:' + JSON.stringify(test) + ' - ' + test.getName())

    // this.showLog('Reflect.defineProperty():' + Reflect.defineProperty(DemoBean,'type',{"value":'dog',"writable":true,"enumerable":true,"configurable":true}))
    // this.showLog('Reflect.deleteProperty():' + Reflect.deleteProperty(DemoBean,'id'))
    let bean = new DemoBean()
    bean.id = 307
    bean.name = '计算机学院'


    this.msgList.push('-初始数据：')
    this.msgList.push('初始类：like=' + DemoBean.like)
    this.msgList.push('初始对象：' + JSON.stringify(bean))

    this.msgList.push('')
    this.msgList.push('-属性判定：')
    this.msgList.push('Reflect.has(class, \'like\'):' + Reflect.has(DemoBean, 'like'))
    this.msgList.push('Reflect.has(obj, \'like\'):' + Reflect.has(bean, 'like'))

    this.msgList.push('')
    this.msgList.push('-判定是否可以扩展：')
    this.msgList.push('Reflect.isExtensible(class):' + Reflect.isExtensible(DemoBean))
    this.msgList.push('Reflect.isExtensible(obj):' + Reflect.isExtensible(bean))

    this.msgList.push('')
    this.msgList.push('-获取属性：')
    this.msgList.push('Reflect.get(class, \'like\'):' + Reflect.get(DemoBean, 'like'))
    this.msgList.push('Reflect.get(obj, \'name\'):' + Reflect.get(bean, 'name'))

    this.msgList.push('')
    this.msgList.push('-添加属性：')
    this.msgList.push('Reflect.set(class, \'dColor\', \'black\'):' + Reflect.set(DemoBean, 'dColor', 'black'))
    this.msgList.push('Reflect.set(obj, \'color\', \'red\'):' + Reflect.set(bean, 'color', 'red'))

    this.msgList.push('')
    this.msgList.push('-阻止添加新属性：')
    this.msgList.push('Reflect.setPrototypeOf(class):' + Reflect.preventExtensions(DemoBean))
    this.msgList.push('Reflect.setPrototypeOf(obj):' + Reflect.preventExtensions(bean))

    this.msgList.push('')
    this.msgList.push('-调用函数：')
    this.msgList.push('Reflect.apply(class.getId,class, [])：' + Reflect.apply(DemoBean.getId, DemoBean, []))
    this.msgList.push('Reflect.apply(class.prototype.getName,obj, [])：' + Reflect.apply(DemoBean.prototype.getName, bean, []))

    this.msgList.push('')
    this.msgList.push('-新建一个对象：')
    this.msgList.push('Reflect.construct(class,[]):' + JSON.stringify(Reflect.construct(DemoBean, [])))

    this.msgList.push('')
    this.msgList.push('-获取属性描述 ：')
    this.msgList.push('Reflect.getOwnPropertyDescriptor(class.\'dColor\'):' + JSON.stringify(Reflect.getOwnPropertyDescriptor(DemoBean, 'dColor')))
    this.msgList.push('Reflect.getOwnPropertyDescriptor(obj,\'color\'):' + JSON.stringify(Reflect.getOwnPropertyDescriptor(bean, 'color')))

    this.msgList.push('')
    this.msgList.push('-获取所有属性：')
    this.msgList.push('Reflect.ownKeys(class)：' + JSON.stringify(Reflect.ownKeys(DemoBean)))
    this.msgList.push('Reflect.ownKeys(obj)：' + JSON.stringify(Reflect.ownKeys(bean)))

    this.msgList.push('')
    this.msgList.push('-结束数据：')
    this.msgList.push('结束类：like=' + DemoBean.like + ' - dColor=' + Reflect.get(DemoBean, 'dColor'))
    this.msgList.push('结束对象：' + JSON.stringify(bean))

    // 获取对象属性描述 - {"value":"red","writable":true,"enumerable":true,"configurable":true}
    // this.showLog('Reflect.getOwnPropertyDescriptor():' + JSON.stringify(Reflect.getOwnPropertyDescriptor(bean, 'color')))

    // 从指定对象中获取属性值
    // this.showLog('类-Reflect.get():' + Reflect.get(DemoBean, 'like'))
    // this.showLog('对象-Reflect.get():' + Reflect.get(bean, 'name'))

    // 对象是否可以被扩展
    // this.showLog('Reflect.isExtensible():' + Reflect.isExtensible(bean))

    // 为对象添加新的属性
    // this.showLog('Reflect.set():' + Reflect.set(bean, 'color', 'red')) // 添加属性成功
    // 阻止对象再添加新的属性
    // this.showLog('Reflect.setPrototypeOf():' + Reflect.preventExtensions(bean))
    // this.showLog('Reflect.set():' + Reflect.set(bean, 'high', '70cm')) // 无法再添加新属性
    // 获取对象的所有属性名称
    // this.showLog('Reflect.ownKeys():' + JSON.stringify(Reflect.ownKeys(bean)))

    // 调用对象执行函数
    // this.showLog('类-Reflect.apply():' + Reflect.apply(DemoBean.getId, DemoBean, []))
    // this.showLog('对象-Reflect.apply():' + Reflect.apply(DemoBean.prototype.getName, bean, []))
    // 新建一个对象
    // this.showLog('Reflect.construct():' + JSON.stringify(Reflect.construct(DemoBean, [])))

    // this.showLog('Reflect.getPrototypeOf():' + JSON.stringify(DemoBean.prototype.id))
    // let b: DemoBean = JSON.parse(this.get())
    // this.showLog('对象：' + b.getName())

    // let list:ArrayList<string> = new ArrayList()

    this.showLog("bean:" + JSON.stringify(bean))
  }

  get(): string {
    let b = {}
    b['id'] = 205
    b['name'] = '{}对象'
    return JSON.stringify(b)
  }

  count: number = 1

  build() {
    List({ space: 1 }) {
      ForEach(this.msgList, (item: string, index) => {
        ListItem() {
          // Text((index + 1) + '.' + item).item()
          Text((item.startsWith('-') ? this.count++ : '') + item).item()
        }
      })
    }.padding({ left: 5, right: 5, top: 10, bottom: 10 })

  }

  /** import Logger from 'myLibrary/src/main/ets/util/Logger'
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('ReflectDemo', msg)
  }
}

@Extend(Text) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Start)
  .width('100%')
  .height(35)
}

export class DemoBean {
  id: number = 404
  name: string = '默认'

  // constructor() {
  // }
  static like: string = 'game'

  getName(): string {
    return this.name
  }

  static getId(): number {
    return 999
  }
}