export interface CostBean {
  name: string;
  cost: number;
}

export const costDataList: CostBean[] = [
  { name: "菜油-OI", cost: 4.4 },
  { name: "棕榈油-P", cost: 5.5 },
  { name: "豆油-Y", cost: 5.5 },
  { name: "菜粕-RM", cost: 3.3 }
]

export function getCost(name: string): number {
  let cost = 0
  costDataList.forEach((bean) => {
    if (bean.name.match(name) !== null) {
      cost = bean.cost
    }
  })
  return cost
}
