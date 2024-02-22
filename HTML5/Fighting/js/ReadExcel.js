
// import "https://cdn.bootcss.com/jquery/3.2.1/jquery.js"
// import "https://cdn.bootcss.com/xlsx/0.11.5/xlsx.core.min.js"

document.write("<script  src='https://cdn.bootcss.com/jquery/3.2.1/jquery.js'></script>")
document.write("<script src='https://cdn.bootcss.com/xlsx/0.11.5/xlsx.core.min.js'></script>")

function readFile(file,callback){

    let fileReader = new FileReader();
    fileReader.onload = function(ev) {
        let data = ev.target.result
        let workbook = XLSX.read(data, {
            type: 'binary'
        })

         // 以二进制流方式读取得到整份excel表格对象
         // 读取每一页的数据
         let index =1
         for (const SheetName of workbook.SheetNames) {
            let worksheet = workbook.Sheets[SheetName];
            let json = XLSX.utils.sheet_to_json(worksheet,{header:1})
            callback(file.name,SheetName, json)
         }

        // let worksheet = workbook.Sheets[workbook.SheetNames[0]];
        // let json = XLSX.utils.sheet_to_json(worksheet,{header:1})
        // callback(file.name, json)
    };
    // 以二进制方式打开文件
    fileReader.readAsBinaryString(file);

}
