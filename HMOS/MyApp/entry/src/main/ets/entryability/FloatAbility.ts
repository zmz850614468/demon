import ExtensionAbility from '@ohos.app.ability.ExtensionAbility';
import UIAbility from '@ohos.app.ability.UIAbility';
import window from '@ohos.window';

export default class FloatAbility extends UIAbility {
  onWindowStageCreate(windowStage) {
    this.createSubWindow()
  }

  floatPosition = {
    'x': 300, 'y': 300
  }
  windowClass = null;
  /**
   * 创建子串口
   * @param windowStage
   */
  createSubWindow() {
    // 1.创建悬浮窗。
    let config = {
      name: "floatWindow", windowType: window.WindowType.TYPE_FLOAT, ctx: this.context
    };
    window.createWindow(config, (err, data) => {
      if (err.code) {
        console.error('Failed to create the floatWindow. Cause: ' + JSON.stringify(err));
        return;
      }
      console.info('Succeeded in creating the floatWindow. Data: ' + JSON.stringify(data));
      this.windowClass = data;
      // 2.悬浮窗窗口创建成功后，设置悬浮窗的位置、大小及相关属性等。
      this.windowClass.moveWindowTo(this.floatPosition.x, this.floatPosition.y, (err) => {
        if (err.code) {
          console.error('Failed to move the window. Cause:' + JSON.stringify(err));
          return;
        }
        console.info('Succeeded in moving the window.');
      });
      this.windowClass.resize(200, 70, (err) => {
        if (err.code) {
          console.error('Failed to change the window size. Cause:' + JSON.stringify(err));
          return;
        }
        console.info('Succeeded in changing the window size.');
      });
      // 3.为悬浮窗加载对应的目标页面。
      this.windowClass.setUIContent("pages/Float", (err) => {
        if (err.code) {
          console.error('Failed to load the content. Cause:' + JSON.stringify(err));
          return;
        }
        console.info('Succeeded in loading the content.');
        // 3.显示悬浮窗。
        this.windowClass.showWindow((err) => {
          if (err.code) {
            console.error('Failed to show the window. Cause: ' + JSON.stringify(err));
            return;
          }
          console.info('Succeeded in showing the window.');
        });
      });
    });
  }
};