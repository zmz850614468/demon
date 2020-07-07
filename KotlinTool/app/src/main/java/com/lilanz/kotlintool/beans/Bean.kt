package com.lilanz.kotlintool.beans

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * 要与服务器数据进行对接:进行json解析
 */
@DatabaseTable(tableName = "student_table")
class Bean {

    @DatabaseField(columnName = "id", generatedId = true)
     var id = 0

    @DatabaseField(columnName = "name")
    var name: String? = null

    @DatabaseField(columnName = "age")
    var age = 0

    @DatabaseField(columnName = "isboy") // true: male  ; false:female
    var isBoy = false

    override fun toString(): String {
        return "{id:$id,name:$name,isboy:$isBoy}"
    }

}