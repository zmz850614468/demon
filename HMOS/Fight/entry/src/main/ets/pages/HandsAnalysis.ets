import { CylinderLayout, Y } from '../views/CylinderLayout'

@Entry
@Component
struct HandsAnalysis {
  build() {
    List() {
      ListItem() {
        Y({ max: 5 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 1, day: 1 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 2, day: 3 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 6, day: 4 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 0, day: 5 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 5, day: 6 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 1, day: 1 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 2, day: 3 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 3, day: 4 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 4, day: 5 })
      }.width(20)
      .height('100%')

      ListItem() {
        CylinderLayout({ max: 5, value: 5, day: 6 })
      }.width(20)
      .height('100%')

    }
    .listDirection(Axis.Horizontal)
    .height(200)
    .divider({ strokeWidth: 0 })
    .padding({ left: 0, right: 0, top: 0, bottom: 0 })
    .margin({ left: 0, right: 0, top: 0, bottom: 0 })
  }
}