import UIAbility from '@ohos.app.ability.UIAbility';
import hilog from '@ohos.hilog';
import window from '@ohos.window';
import abilityAccessCtrl, { Permissions } from '@ohos.abilityAccessCtrl';

export default class EntryAbility extends UIAbility {
  onCreate(want, launchParam) {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onCreate');
  }

  onDestroy() {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onDestroy');
  }

  requestPermission(permission: Array<Permissions>, callBack?: (result: Array<number>) => void) {
    let atManager = abilityAccessCtrl.createAtManager();
    try {
      atManager.requestPermissionsFromUser(this.context, permission, (err, data) => {
        console.info("tag" + JSON.stringify(data));
        // console.info("data permissions:" + data.permissions);
        // console.info("data authResults:" + data.authResults);
        if (callBack) {
          callBack(data.authResults)
        }
      });
    } catch (err) {
      console.log(`catch err->${JSON.stringify(err)}`);
    }
  }

  onWindowStageCreate(windowStage: window.WindowStage) {
    // Main window is created, set main page for this ability
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageCreate');

    windowStage.loadContent('pages/Index', (err, data) => {
      if (err.code) {
        hilog.error(0x0000, 'testTag', 'Failed to load the content. Cause: %{public}s', JSON.stringify(err) ?? '');
        return;
      }
      hilog.info(0x0000, 'testTag', 'Succeeded in loading the content. Data: %{public}s', JSON.stringify(data) ?? '');
    });

    // let windowClass = null;
    // let config = {
    //   name: "floatWindow", windowType: window.WindowType.TYPE_FLOAT, ctx: this.context
    // };
    // window.createWindow(config, (err, data) => {
    //   if (err.code) {
    //     console.error('Failed to create the floatWindow. Cause: ' + JSON.stringify(err));
    //     return;
    //   }
    //   console.info('Succeeded in creating the floatWindow. Data: ' + JSON.stringify(data));
    //   windowClass = data;
    //   // 2.悬浮窗窗口创建成功后，设置悬浮窗的位置、大小及相关属性等。
    //   windowClass.moveWindowTo(300, 300, (err) => {
    //     if (err.code) {
    //       console.error('Failed to move the window. Cause:' + JSON.stringify(err));
    //       return;
    //     }
    //     console.info('Succeeded in moving the window.');
    //   });
    //   windowClass.resize(500, 500, (err) => {
    //     if (err.code) {
    //       console.error('Failed to change the window size. Cause:' + JSON.stringify(err));
    //       return;
    //     }
    //     console.info('Succeeded in changing the window size.');
    //   });
    //   // 3.为悬浮窗加载对应的目标页面。
    //   windowClass.setUIContent("pages/Index", (err) => {
    //     if (err.code) {
    //       console.error('Failed to load the content. Cause:' + JSON.stringify(err));
    //       return;
    //     }
    //     console.info('Succeeded in loading the content.');
    //     // 3.显示悬浮窗。
    //     windowClass.showWindow((err) => {
    //       if (err.code) {
    //         console.error('Failed to show the window. Cause: ' + JSON.stringify(err));
    //         return;
    //       }
    //       console.info('Succeeded in showing the window.');
    //     });
    //   });
    //   // 4.销毁悬浮窗。当不再需要悬浮窗时，可根据具体实现逻辑，使用destroy对其进行销毁。
    //   windowClass.destroyWindow((err) => {
    //     if (err.code) {
    //       console.error('Failed to destroy the window. Cause: ' + JSON.stringify(err));
    //       return;
    //     }
    //     console.info('Succeeded in destroying the window.');
    //   });
    // });

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
