import RecordResult from './bean/RecordResult'
import ArrayList from '@ohos.util.ArrayList'
import AnalysisBean from './bean/AnalysisBean'
import MonthBasicDataBean from './bean/MonthBasicDataBean'
import DataParse from './DataParse'
import Data_L_10_2023 from './data/2023/Data_L_10_2023'
import Data_L_10_2024 from './data/2024/Data_L_10_2024'

/**
 * RU-橡胶 数据解析
 */
export default class DataParse_L_10 {
  /**
   * 需要去解析的基础数据
   */
  static basicList: Array<MonthBasicDataBean> = []

  static record: RecordResult = new RecordResult()

  static analysiclist: ArrayList<AnalysisBean> = new ArrayList()

  constructor() {
    DataParse_L_10.basicList = DataParse_L_10.basicList.concat(new Data_L_10_2024().getBasicList())
    DataParse_L_10.basicList = DataParse_L_10.basicList.concat(new Data_L_10_2023().getBasicList())
  }

  /**
   * 获取所有分析结果
   */
  getAnalysis(): ArrayList<AnalysisBean> {
    if (DataParse_L_10.analysiclist.isEmpty()) {
      DataParse_L_10.basicList.forEach((item) => {
        let dataList = DataParse.getStringToBean(item.data)
        DataParse_L_10.analysiclist.add(DataParse.getMonthAnalysis(item.year, item.month, dataList))
        DataParse_L_10.record.dealOperateData(item.year + '/' + item.month, dataList)
      })

      DataParse_L_10.record.dealMonthData(DataParse_L_10.analysiclist)

      // 1手 对应 0.5
      DataParse_L_10.analysiclist.forEach((item) => {
        item.update(0.5)
      })
    }
    return DataParse_L_10.analysiclist
  }
}