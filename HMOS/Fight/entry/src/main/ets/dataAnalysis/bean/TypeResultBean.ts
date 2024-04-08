export default class TypeResultBean {
  type: string
  count: number = 0
  posCount: number = 0
  result: number = 0

  constructor(type: string) {
    this.type = type
  }
}