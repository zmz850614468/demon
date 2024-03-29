import dataPreferences from '@ohos.data.preferences';
import promptAction from '@ohos.promptAction';
import Logger from '../util/Logger';

let context = getContext(this);
let preference: dataPreferences.Preferences = null

export let BASIC: string = 'basic'

export let FU_LI: string = 'fuLi'

export let COUNT: string = 'count'

export let TARGET_LIST: string = 'targetList'

export let OPERATE_LIST: string = 'operateList'

export let HANDS_LIST: string = 'handsList'

export let BEST_COMBINATION: string = 'bestCombination'

export let BEST_COMBINATION_TEST: string = 'bestCombinationTest'

export let FLOAT_POSITION: string = 'floatPosition'

class PreferenceModel {

  /**
   * Read the specified Preferences persistence file and load the data into the Preferences instance.
   */
  async getPreferencesFromStorage() {
    try {
      preference = await dataPreferences.getPreferences(context, 'demon_db');
    } catch (err) {
      Logger.error('tag', `Failed to get preferences, Cause: ${err}`);
    }
  }

  /**
   * Save the data to the Preferences.
   *
   * @param fruit Fruit data.
   */
  async save(key: string, value) {
    if (preference === null) {
      await this.getPreferencesFromStorage();
    }
    // The fruit name and fruit quantity data entered by the user are saved to the cached Preference instance.
    try {
      await preference.put(key, value);
    } catch (err) {
      Logger.error('tag', `Failed to put value, Cause: ${err}`);
    }
    // Store the Preference instance in the preference persistence file
    await preference.flush();
  }

  /**
   * Get preference data.
   */
  async get(key: string) {
    let value;
    if (preference === null) {
      await this.getPreferencesFromStorage();
    }
    try {
      // Logger.error('tag', 'has key : ' + key + ' -- ' + preference.has(key))
      value = <string> await preference.get(key, null);
    } catch (err) {
      Logger.error('tag', `Failed to get value, Cause: ${err}`);
    }

    return value;
  }

  /**
   *  保存 对象 到文件中
   * @param key
   * @param value 对象数据
   */
  async saveObj(key: string, value: object) {
    await this.save(key, JSON.stringify(value))
  }

  /**
   * 获取 对象 数据
   * @param key
   * @returns 对象数据
   */
  async getObj(key: string) {
    let value = await this.get(key)
    return JSON.parse(value);
  }


  /**
   * 提示信息
   */
  showToastMessage(message: Resource) {
    promptAction.showToast({
      message: message,
      duration: 300
    })
  }
}

export default new PreferenceModel()