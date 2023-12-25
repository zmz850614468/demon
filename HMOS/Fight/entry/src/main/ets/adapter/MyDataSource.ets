import { BasicDataSource } from './BasicDataSource';

export class MyDataSource extends BasicDataSource {
  dataArray: Array<any> = null

  constructor(dataArray?: Array<any>) {
    super();
    if (dataArray) {
      this.dataArray = dataArray
    } else {
      this.dataArray = []
    }
  }

  totalCount(): number {
    return this.dataArray.length
  }

  getData(index: number): any {
    return this.dataArray[index]
  }

  addData(index: number, data: any): void {
    this.dataArray.splice(index, 0, data)
    this.notifyDataAdd(index)
  }

  pushData(data: any): void {
    this.dataArray.push(data)
    this.notifyDataAdd(this.dataArray.length - 1)
  }

  deleteData(index: number) {
    this.dataArray.splice(index, 1)
    this.notifyDataDelete(index)
  }

  /**
   * 删除所有数据
   */
  deleteAll() {
    this.dataArray.splice(0)
    this.notifyDataReload()
  }
}