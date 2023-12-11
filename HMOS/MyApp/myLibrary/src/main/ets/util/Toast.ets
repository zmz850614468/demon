import promptAction from '@ohos.promptAction';

export class Toast {
  /**
   * 提示：最少1500ms
   *
   * @param {Resource | string} content content to show
   */
  static show(content: Resource | string, time?: number): void {
    promptAction.showToast({
      message: content,
      duration: time
    });
  }
}