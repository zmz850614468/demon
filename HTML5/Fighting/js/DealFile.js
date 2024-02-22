
document.write("<script src='js/ReadExcel.js'></script>")
document.write("<script src='js/TableUtil.js'></script>")

/**
 * 文件处理的入口
 * @param {*} files 
 */
function dealFiles(files){
    for (const file of files) {
        readFile(file, callback)
    }
}

/**
 * 文件回调
 * @param {*} fileName 
 * @param {*} sheetName 
 * @param {*} json 
 */
function callback(fileName, sheetName, json){
    console.log(fileName +" - "+sheetName+" - size:"+ json.length)
    let table = createTable()
    for (const items of json) {
        createRow(table, items)
    }
    // let table = getTable()
    let div = document.getElementById("testTable")
    div.appendChild(table)
}



