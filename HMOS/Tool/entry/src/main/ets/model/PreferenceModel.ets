import dataPreferences from '@ohos.data.preferences';
import Toast from '../util/Toast'
import Logger from '../util/Logger'

export const Test: string = 'test'

class PreferenceModel {
  private context = getContext(this);
  private preference: dataPreferences.Preferences = null

  /**
   * 获取文件保存对象
   */
  async getPreferencesFromStorage() {
    try {
      this.preference = await dataPreferences.getPreferences(this.context, 'demon_db');
    } catch (err) {
      Logger.error('tag', `Failed to get preferences, Cause: ${err}`);
      Toast.show('获取 preferences 失败：' + err)
    }
  }

  /**
   *  保存基础 key-value 到文件中
   * @param key
   * @param value  number | string | boolean | Array<number> | Array<string> | Array<boolean>;
   */
  async save(key: string, value) {
    if (this.preference === null) {
      await this.getPreferencesFromStorage();
    }

    try {
      await this.preference.put(key, value);
    } catch (err) {
      Logger.error('tag', `Failed to put value, Cause: ${err}`);
      Toast.show('保存轻量级数据失败：' + err)
    }

    await this.preference.flush();
  }

  /**
   * 获取基础 key-value 数据
   * @param key
   * @returns number | string | boolean | Array<number> | Array<string> | Array<boolean>;
   */
  async get(key: string) {
    if (this.preference === null) {
      await this.getPreferencesFromStorage();
    }

    let value;
    try {
      // Logger.error('tag', 'has key : ' + key + ' -- ' + preference.has(key))
      value = <string> await this.preference.get(key, null);
    } catch (err) {
      Logger.error('tag', `Failed to get value, Cause: ${err}`);
      Toast.show('获取轻量级数据失败：' + err)
    }

    return value;
  }

  /**
   *  保存 对象 到文件中
   * @param key
   * @param value 对象数据
   */
  async saveObj(key: string, value) {
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
   *  保存 Array[] 到文件中
   * @param key
   * @param value 对象数据 - 不是基础数据对象
   */
  async saveObjArray(key: string, value: Array<Object>) {
    await this.save(key, JSON.stringify(value))
  }

  /**
   * 获取 Array[] 数据
   * @param key
   * @returns 对象数据 - 不是基础数据对象
   */
  async getObjArray(key: string, array: Array<Object>) {
    let value = await this.get(key)
    Object.assign(array, value);
    return array
  }
}

let myPreference = new PreferenceModel()

export default myPreference