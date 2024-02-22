
/**
 * 柱状图 数据类
 */
class BarDataSet{
    data=[]         // 必填：柱状图数据

    label=''        // 提示数据的前缀
    indexAxis=''    // 柱状图显示方向  -- x:默认   y:  
    backgroundColor=[]  // 柱状图背景色,交替
    borderColor=[]
    borderWidth=0
}

/**
 * 折线图 数据量
 */
class LineDataSet{
    data=[]         // 必填：折线图数据

    label=''        // 提示数据的前缀
    fill = false    // 是否填充中间层
    borderColor=''  // 折线颜色
    tension=0.1     // 处理拐点弧度
} 

/**
 * 饼状图/环形图 数据量
 */
class CircleDataSet{
    data=[]         // 必填：饼状图数据

    label=''        // 提示数据的前缀
    backgroundColor=[]  // 饼状图背景色
    hoverOffset = 10
}
// 饼状图 随窗口大小缩放
// .options = {
//     responsive: true, // 设置图表为响应式，根据屏幕窗口变化而变化
//     maintainAspectRatio: false, // 保持图表原有比例
// };

/**
 * 各种图标 类
 */
class ChartConfig{
    constructor(type){
        this.type = type
    }

    setXLable(labels){
        this.data.labels = labels
    }

    addDataSet(dataset){
        this.data.datasets.push(dataset)    
    }

    /**
     * 'bar'        :柱状图
     * 'line'       :折线图
     * 'pie'        :饼状图
     * 'doughnut'   :环形图
     */
    type=''         // 必填:图标类型
    data={
        labels:[],  // 必填：x轴标签
        datasets:[] // 必填：具体数据 - DataSet
    }
    options={}
    plugins=[]
}

