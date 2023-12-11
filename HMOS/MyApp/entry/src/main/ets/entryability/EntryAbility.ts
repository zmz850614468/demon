import UIAbility from '@ohos.app.ability.UIAbility';
import hilog from '@ohos.hilog';
import window from '@ohos.window';
import Want from '@ohos.app.ability.Want';
import { PreferenceModel } from 'myLibrary/src/main/ets/model/PreferenceModel'
import { PreferenceConst } from '../constance/PreferenceConst';

export default class EntryAbility extends UIAbility {
  onCreate(want, launchParam) {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onCreate');
  }

  onDestroy() {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onDestroy');
  }

  onWindowStageCreate(windowStage: window.WindowStage) {
    // Main window is created, set main page for this ability
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageCreate');

    this.initData()
    windowStage.loadContent('pages/Index', (err, data) => {
      if (err.code) {
        hilog.error(0x0000, 'testTag', 'Failed to load the content. Cause: %{public}s', JSON.stringify(err) ?? '');
        return;
      }
      hilog.info(0x0000, 'testTag', 'Succeeded in loading the content. Data: %{public}s', JSON.stringify(data) ?? '');
    });
  }

  onNewWant(want: Want, launchParam) {
    this.initData()
    // console.error("onNewWant want:" + JSON.stringify(want));
    // console.error("onNewWant launchParam:" + JSON.stringify(launchParam));
    if (want.parameters['action']) {
      switch (want.parameters['action']) {
        case 'open':
          this.createSubWindow()
          break
        case 'close':
          this.closeSubWindow()
          break
      }
    }
  }

  async initData() {
    let position = await PreferenceModel.getInstance(this.context).getObj(PreferenceConst.FloatPosition)
    if (position) {
      this.floatPosition = position
    }
  }

  floatPosition = {
    'x': 1200, 'y': 220
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
        // this.windowClass.resize(800, 800, (err) => {
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

  closeSubWindow() {
    // 4.销毁悬浮窗。当不再需要悬浮窗时，可根据具体实现逻辑，使用destroy对其进行销毁。
    this.windowClass.destroyWindow((err) => {
      if (err.code) {
        console.error('Failed to destroy the window. Cause: ' + JSON.stringify(err));
        return;
      }
      console.info('Succeeded in destroying the window.');
    });
    this.windowClass = null
  }

  onWindowStageDestroy() {
    // Main window is destroyed, release UI related resources
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageDestroy');
  }

  onForeground() {
    // Ability has brought to foreground
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onForeground');
  }

  onBackground() {
    // Ability has back to background
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onBackground');
  }
}
