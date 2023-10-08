import { Columns, ColumnType, Id, Entity } from '@ohos/dataorm'

/**
 * 出入金 类
 */
@Entity('MONEY')
export default class MoneyBean {
  @Id()
  @Columns({ columnName: 'ID', types: ColumnType.num })
  id: number

  @Columns({ columnName: 'DATE', types: ColumnType.str })
  date: string // 日期

  @Columns({ columnName: 'MUNCH', types: ColumnType.num })
  munch: number // 出入金

  @Columns({ columnName: 'MEMO', types: ColumnType.str })
  memo: string // 备注

  // 类中必须在constructor中声明所有非静态变量，用于反射生成列
  constructor(id?: number, date?: string, munch?: number, memo?: string) {
    this.id = id
    this.date = date
    this.munch = munch
    this.memo = memo
  }

  setId(id: number) {
    this.id = id;
  }

  getId(): number {
    return this.id
  }

  setDate(date: string) {
    this.date = date
  }

  getDate(): string {
    return this.date
  }

  setMunch(munch: number) {
    this.munch = munch
  }

  getMunch(): number {
    return this.munch
  }

  setMemo(memo: string) {
    this.memo = memo
  }

  getMemo(): string {
    return this.memo
  }

}