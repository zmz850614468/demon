// 类型相关的常数
import AnalysisBean from '../dataAnalysis/bean/AnalysisBean'
import DataParse_IO_10 from '../dataAnalysis/DataParse_IO_10'
import DataParse_IO_5 from '../dataAnalysis/DataParse_IO_5'
import DataParse_PG_10 from '../dataAnalysis/DataParse_PG_10'
import DataParse_P_10 from '../dataAnalysis/DataParse_P_10'
import DataParse_RM_10 from '../dataAnalysis/DataParse_RM_10'
import HashMap from '@ohos.util.HashMap'

// 有数据的期货类型
export const Types: string[] = ['OI-10', 'P-10', 'PG-10', 'RM-10']

// 每手对应的数量，默认数量：10，记作 1
export const HandsMap: HashMap<string, number> = new HashMap()

HandsMap.set('PG-10', 2)

// 对比 月份 类型
export const Months: number[] = [3, 6, 12]

export function GetTypeMonthData(type: string): Array<AnalysisBean> {
  let analysisList = null
  switch (type) {
    case 'OI-10':
      analysisList = new DataParse_IO_10().getAnalysis()
      break
    case 'P-10':
      analysisList = new DataParse_P_10().getAnalysis()
      break
    case 'OI-5':
      analysisList = new DataParse_IO_5().getAnalysis()
      break
    case 'RM-10':
      analysisList = new DataParse_RM_10().getAnalysis()
      break
    case 'PG-10':
      analysisList = new DataParse_PG_10().getAnalysis()
      break
  }
  return analysisList
}








