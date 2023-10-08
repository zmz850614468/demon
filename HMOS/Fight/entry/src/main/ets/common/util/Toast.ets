import promptAction from '@ohos.promptAction';

export default class Toast {
  /**
   * Show toast content.
   *
   * @param {Resource | string} content content to show
   */
  static show(content: Resource | string, time: number = 1500): void {
    promptAction.showToast({
      message: content,
      duration: time
    });
  }
}