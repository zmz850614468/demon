
const groubleColor=[    // 全局颜色值
    '#73a373',
    '#f49f42',
    '#73b9bc',
    '#eedd78',
    '#91ca8c',
    '#8dc1a9',
    '#7289ab',
    '#ea7e53',
    '#dd6b66',
    '#759aa0',
    '#e69d87',
]

/**
 * 柱状图 类
 */
class BarOption{
    
    // color=groubleColor   // 全局颜色设置
    legend={data:[]}    // 图例显示
    xAxis={}    // x轴标签
    yAxis={}    // y轴标签
    series=[]   // 数据集

    /**
     * 设置这个为 纵向柱状图
     * @param {Array} data 
     */
    setXAxis(data){
        this.xAxis.data = data
    }

    /**
     * 设置这个为 横向柱状图
     * @param {Array} data 
     */
    setYAxis(data){
        this.yAxis.data=data
    }

    /**
     * 添加数据集
     * @param {Array} data 
     */
    addSeriesData(data){
        this.series.push({type:'bar',data:data})
    }

    /**
     * 添加图片提示标签
     * @param {*} name 
     */
    addTipLable(name){
        this.legend.data.push({name:name,icon:'rect'})
        let size = this.legend.data.length
        if(this.series.length >= size){
            this.series[size-1].name = name
        }
    }

    /**
     * 设置数据标签显示位置
     * @param {*} position 
     */
    setLabelShow(position='top'){
        for (let i = 0; i < this.series.length; i++) {
            this.series[i].label={
                position: position,
                distance: 5,
                // rotate:90,   // 选择角度

                show: true,
                formatter: '{c}',
            }    
        }
    }
}

/**
 * 折线图 类
 */
class LineOption{

    legend={data:[]}    // 图例显示
    xAxis={}    // x轴标签
    yAxis={}    // y轴标签
    series=[]   // 数据集

    // 是否显示点对应数据,也可以放series数组对象里面
    label={
        show: true,
        position: 'top',
        textStyle: {
          fontSize: 20
        }
    }
    // 是否平滑处理
    isSmooth=true

    setXAxis(data){
        this.xAxis.data = data
    }

    setYAxis(data){
        this.yAxis.data=data
    }

    addSeriesData(data){
        this.series.push({type:'line',data:data,
        smooth:this.isSmooth})
    }

    setShowValue(isShow){
        this.label.show=isShow
        // this.isShowValue=isShow
        // if(this.isShowValue){
        //     for (const item of this.series.values) {
        //         item.label=this.label
        //     }
        // }else{
        //     for (const item of this.series) {
        //         item.label=null
        //     }
        // }
    }

    setSmooth(isSmooth){
        this.isSmooth=isSmooth
        for (const item of this.series) {
            item.smooth=this.isSmooth
        }
    }

    /**
     * 添加图片提示标签
     * @param {*} name 
     */
    addTipLable(name){
        this.legend.data.push({name:name,icon:'rect'})
        let size = this.legend.data.length
        if(this.series.length >= size){
            this.series[size-1].name = name
        }
    }
}

/**
 * 饼状图 类
 */
class PieOption{
    
    series=[{
        type:'pie',
        label: {
            show: true,  // 是否显示标签
            // position:''  // 'center' 是否改成选中中间显示
        },
        data:[]}]   // 数据集

    /**
     * {value:number ,name:string}
     * @param {*} data 
     */
    addSeriesData(data){
        this.series[0].data.push(data)
    }

    /**
     * [{value:number ,name:string}]
     * @param {*} data 
     */
    setSeriesData(data){
        this.series[0].data=data
    }

    /**
     * 填充部分，通过设置可以变成圆环
     * '0%'~'100%'
     * @param {*} min 
     * @param {*} max 
     */
    setRadius(min, max){
        this.series[0].radius = [min, max]
    }

    /**
     * 外围固定显示标签
     * @param {*} isShow 
     */
    setLabelShow(isShow){
        this.series[0].label.show=isShow
    }

    /**
     * 选中 中间高亮显示标签
     * @param {*} isShow 
     */
    setCenterLabelShow(isShow){
        if(isShow){
            this.series[0].label.show=false
            this.series[0].label.position='center'
            this.series[0].emphasis={
                label: {
                  show: true,
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
        }else{
            this.series[0].label={show: true}
            this.series[0].emphasis = null
        }
    }
}
