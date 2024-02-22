
// let table = null

/**
 * 创建表
 * @param {*} title 
 */
function createTable(title){
    table = document.createElement("table")
    if(title){
        let thead = document.createElement("thead")
        thead.innerText = title
        table.appendChild(thead)
    }
    return table
}

/**
 * 创建表项目
 * @param {*} items 
 */
function createRow(table, items){
    let row = document.createElement("tr")
    for (const item of items) {
        let th = document.createElement("th")
        th.innerText = item
        row.appendChild(th)
    }
    table.appendChild(row)

    return table
}

/**
 * 获取表结构
 */
// function getTable(){
//     return table
// }


