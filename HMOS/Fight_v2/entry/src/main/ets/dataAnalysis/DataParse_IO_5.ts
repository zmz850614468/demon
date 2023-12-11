import RecordResult from './bean/RecordResult'
import ArrayList from '@ohos.util.ArrayList'
import AnalysisBean from './bean/AnalysisBean'
import Data_IO_10_2023 from './data/Data_IO_10_2023'
import MonthBasicDataBean from './bean/MonthBasicDataBean'
import DataParse from './DataParse'
import Data_IO_5_2023 from './data/Data_IO_5_2023'

/**
 * IO 数据解析
 */
export default class DataParse_IO_5 {
  /**
   * 需要去解析的基础数据
   */
  static basicList: Array<MonthBasicDataBean> = []

  static record: RecordResult = new RecordResult()

  static analysiclist: ArrayList<AnalysisBean> = new ArrayList()

  constructor() {
    DataParse_IO_5.basicList = DataParse_IO_5.basicList.concat(DataParse_IO_5.basicList, new Data_IO_5_2023().getBasicList())
  }

  /**
   * 获取所有分析结果
   */
  getAnalysis(): ArrayList<AnalysisBean> {

    if (DataParse_IO_5.analysiclist.isEmpty()) {
      DataParse_IO_5.basicList.forEach((item) => {
        let dataList = DataParse.getStringToBean_5(item.data)
        DataParse_IO_5.analysiclist.add(DataParse.getMonthAnalysis(item.year, item.month, dataList))
        DataParse_IO_5.record.dealOperateData(item.year + '/' + item.month, dataList)
      })

      DataParse_IO_5.record.dealMonthData(DataParse_IO_5.analysiclist)
    }
    return DataParse_IO_5.analysiclist
  }
}