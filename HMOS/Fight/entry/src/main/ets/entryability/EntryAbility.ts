import UIAbility from '@ohos.app.ability.UIAbility';
import hilog from '@ohos.hilog';
import window from '@ohos.window';
import Want from '@ohos.app.ability.Want';
import AbilityConstant from '@ohos.app.ability.AbilityConstant';
import Logger from '../util/Logger';
import dataPreferences from '@ohos.data.preferences';

export default class EntryAbility extends UIAbility {
  content: string = 'pages/Function'
  windowStage = null

  onCreate(want, launchParam) {
    this.onCardWant(want, true)
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onCreate');
  }

  onDestroy() {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onDestroy');
  }

  onWindowStageCreate(windowStage: window.WindowStage) {
    this.initData()
    // Main window is created, set main page for this ability
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageCreate');
    this.windowStage = windowStage
    windowStage.loadContent(this.content, (err, data) => {
      if (err.code) {
        hilog.error(0x0000, 'testTag', 'Failed to load the content. Cause: %{public}s', JSON.stringify(err) ?? '');
        return;
      }
      hilog.info(0x0000, 'testTag', 'Succeeded in loading the content. Data: %{public}s', JSON.stringify(data) ?? '');
    });
  }

  onNewWant(want: Want, launchParams: AbilityConstant.LaunchParam) {
    // 服务卡片
    this.onCardWant(want)

    // 悬浮窗口
    this.initData()
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

  onCardWant(want: Want, isCreate?: boolean) {
    this.content = 'pages/Function'
    if (want.parameters && want.parameters.params) {
      let obj = JSON.parse(want.parameters.params.toString())
      this.showLog('params = ' + JSON.stringify(obj))
      switch (obj['message']) {
        case '1': //
        // this.content = 'pages/DataAnalysis'
          break
        case '2': // 操作详情
          this.content = 'pages/OperateResult'
          break
        case '3': // 当前操作
          this.content = 'pages/Operate'
          break
        case '4': // 数据分析
          this.content = 'pages/DataAnalysis'
          break
      }

      this.showLog('content = ' + this.content)
      if (!isCreate && this.windowStage) {

        this.windowStage.loadContent(this.content, (err, data) => {
          if (err.code) {
            hilog.error(0x0000, 'testTag', 'Failed to load the content. Cause: %{public}s', JSON.stringify(err) ?? '');
            return;
          }
          hilog.info(0x0000, 'testTag', 'Succeeded in loading the content. Data: %{public}s', JSON.stringify(data) ?? '');
        });
      }
    }
  }

  floatPosition = {
    'x': 1200, 'y': 220
  }
  windowClass = null;

  async initData() {

    let preference: dataPreferences.Preferences = null
    try {
      preference = await dataPreferences.getPreferences(this.context, 'demon_db');
    } catch (err) {
      Logger.error('tag', `Failed to get preferences, Cause: ${err}`);
    }
    let value = <string> await preference.get('floatPosition', null);
    let position = JSON.parse(value);
    if (position.x > 600) {
      this.floatPosition = position
    }
  }

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

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('EntryAbility', msg)
  }
}
