import { CostBean, costDataList } from "../common/bean/CostBean";

export class CostListSource implements IDataSource {
  private listData: CostBean[] = costDataList;

  // public CostListSource(listData: T[]) {
  //   this.listData = listData;
  // }

  public setListData(listData: CostBean[]) {
    this.listData = listData;
  }

  /**
   * Total data count.
   * @since 7
   */
  public totalCount(): number {
    return this.listData.length;
  }
  /**
   * Return the data of index.
   * @since 7
   */
  public getData(index: number): any {
    return this.listData[index];
  }
  /**
   * Register data change listener.
   * @since 7
   */
  registerDataChangeListener(listener: DataChangeListener): void {
  }
  /**
   * Unregister data change listener.
   * @since 7
   */
  unregisterDataChangeListener(listener: DataChangeListener): void {
  }
}


