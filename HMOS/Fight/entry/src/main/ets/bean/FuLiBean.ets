export default class FuLiBean {
  index: string;
  // 基数
  basic: string;
  // 复利
  fuLi: string;
  // 次数
  count: string;
  // 结果
  result: string;

  constructor(index: string, basic: string, fuLi: string, count: string, result: string) {
    this.index = index;
    this.basic = basic;
    this.fuLi = fuLi;
    this.count = count;
    this.result = result;
  }
}