import deviceInfo from '@ohos.deviceInfo'
import router from '@ohos.router'
import { Toast } from 'myLibrary/src/main/ets/util/Toast'

@Entry
@Component
struct DeviceInfo {
  aboutToAppear() {

  }

  build() {
    Scroll() {
      Column({ space: 3 }) {
        this.item('1.设备类型:' + deviceInfo.deviceType)
        this.item('2.设备厂家名称:' + deviceInfo.manufacture)
        this.item('3.设备品牌名称:' + deviceInfo.brand)
        this.item('4.外部产品系列:' + deviceInfo.marketName)
        this.item('5.产品系列:' + deviceInfo.productSeries)
        this.item('6.认证型号:' + deviceInfo.productModel)
        this.item('7.内部软件子型号:' + deviceInfo.softwareModel)
        this.item('8.硬件版本号:' + deviceInfo.hardwareModel)
        this.item('9.硬件Profile:' + deviceInfo.hardwareProfile)
        this.item('10.设备序列号:' + deviceInfo.serial)
        this.item('11.Bootloader版本号:' + deviceInfo.bootloaderVersion)
        this.item('12.应用二进制接口列表:' + deviceInfo.abiList)
        this.item('13.安全补丁级别:' + deviceInfo.securityPatchTag)
        this.item('14.产品版本:' + deviceInfo.displayVersion)
        this.item('15.差异版本号:' + deviceInfo.incrementalVersion)
        this.item('16.系统发布类型:' + deviceInfo.osReleaseType)
        this.item('17.系统版本:' + deviceInfo.osFullName)
        this.item('18.Major版本号:' + deviceInfo.majorVersion)
        this.item('19.Senior版本号:' + deviceInfo.seniorVersion)
        this.item('20.Feature版本号:' + deviceInfo.featureVersion)
        this.item('21.Build版本号:' + deviceInfo.buildVersion)
        this.item('22.系统软件API版本:' + deviceInfo.sdkApiVersion)
        this.item('23.首个版本系统软件API版本:' + deviceInfo.firstApiVersion)
        this.item('24.版本ID:' + deviceInfo.versionId)
        this.item('25.构建类型:' + deviceInfo.buildType)
        this.item('26.构建用户:' + deviceInfo.buildUser)
        this.item('27.构建主机:' + deviceInfo.buildHost)
        this.item('28.构建时间:' + deviceInfo.buildTime)
        this.item('29.构建版本Hash:' + deviceInfo.buildRootHash)
        this.item('30.设备Udid:' + deviceInfo.udid)
      }
    }
  }

  @Builder
  item(text: string) {
    Text(text)
      .fontSize(15)
      .textAlign(TextAlign.Start)
      .width('100%')
      .height(40)
      .padding({ left: 20, right: 20, top: 0, bottom: 0 })
  }
}

@Extend(Text)
function textItem() {

}