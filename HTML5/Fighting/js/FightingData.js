
// 总-盈亏
let totalProfitAndLoss = 
"23-9,23-10,23-11,23-12,24-1,24-2\n"+
"液化气,2.5,2.7,2.2,1.5,5.4,-0.8\n"+
"塑料,-0.6,0.7,2.7,0.2,2.9,1.7\n"+
"棕榈油,-2.9,2.7,3.2,1.4,1.9,0.6\n"+
"橡胶,1.9,0.4,2.1,-0.6,2.6,0.6\n"+
"菜油,-0.2,0.6,1.2,-1.1,1.6,-0.3\n"+
"菜粕,1,2.4,4.6,-2.6,-0.3,-2"

// 总-近三月盈亏
let threeMonthProfitAndLoss=
"23-11,23-12,24-1,24-2\n"+
"液化气,7.5,6.4,9.2,6.1\n"+
"塑料,2.8,3.6,5.8,4.8\n"+
"棕榈油,3,7.3,6.5,3.9\n"+
"橡胶,4.4,1.9,4.1,2.6\n"+
"菜油,1.6,0.7,1.7,0.2\n"+
"菜粕,8,4.4,1.6,-4.9"

// /-盈亏
let reProfitAndLoss=
"23-9,23-10,23-11,23-12,24-1,24-2\n"+
"液化气,0.1,0.3,0.6,1.1,-1.0,1.4\n"+
"塑料,0.2,0.4,0.3,0.6,0.1,0\n"+
"棕榈油,-0.1,-0.1,1.4,-0.1,0.1,0\n"+
"橡胶,1.1,0.1,0.7,-0.3,0.2,0.3\n"+
"菜油,0.3,0.1,1.3,0.4,0.1,0.6\n"+
"菜粕,0.1,1.8,-0.9,0.8,0.5,0"

// /-近三月盈亏
let reThreeMonthProfitAndLoss=
"23-11,23-12,24-1,24-2\n"+
"液化气,0.9,2,0.7,1.5\n"+
"塑料,0.9,1.3,1.0,0.7\n"+
"棕榈油,1.1,1.1,1.3,0\n"+
"橡胶,1.9,0.6,0.7,0.2\n"+
"菜油,1.8,1.9,1.9,1.1\n"+
"菜粕,1,1.7,0.3,1.3"

// 交易费用
let costData=
"液化气,棕榈油,菜油,橡胶,菜粕,豆油,豆粕,塑料\n"+
"交易费用,13.2,5.5,4.4,6.6,3.3,5.5,3.3,2.2"

// 复利数据
let fuLiData=
"10,15,20,25\n"+
"盈利倍数,2.1,4.4,7.9,13.6"


/**
 * 柱状图，折线图 数据对象
 */
class OptionData{
    axis=[] ;// 坐标系上标签
    series=[] ;// 数据集 
    tipLable=[] ;// 图例提示
}

/**
 * 数据转换
 * @param {string} data 
 * @returns 
 */
function getOptionData(data){
    let optionData = new OptionData()

    let strArr = data.split("\n")
    let size = strArr.length
    for (let i = 0; i< size;i++){
        if(i==0){
            optionData.axis = strArr[i].split(',')
        }else{
            let arr = strArr[i].split(',')
            let tipLable = arr[0]
            let total = 0
            let length = 0
            for (let j = 1; j < arr.length; j++) {
                arr[j-1] = arr[j]
                total += parseFloat(arr[j])
                length++
            }
            arr.pop()
            optionData.tipLable.push(tipLable+' : '+(total/length).toFixed(1))
            optionData.series.push(arr)
        }
    }

    return optionData
}




