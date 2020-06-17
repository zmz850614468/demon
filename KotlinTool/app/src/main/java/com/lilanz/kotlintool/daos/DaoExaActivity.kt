package com.lilanz.kotlintool.daos

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lilanz.kotlintool.R
import com.lilanz.kotlintool.beans.StudentBean
import com.lilanz.kotlintool.utils.NumberUtil
import com.lilanz.kotlintool.utils.StringUtil
import kotlin.collections.HashMap

class DaoExaActivity : Activity() {

    private lateinit var tvAll: TextView
    private lateinit var tvSearch: TextView
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etIsBoy: EditText
    private lateinit var etValue1: EditText
    private lateinit var etValue2: EditText
    private lateinit var btInsert: Button
    private lateinit var btSearch: Button


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dao_test)
        initUI()
        initInsert()
        initSearch()
        searchAll()
    }


    private fun initSearch() {
        etValue1 = findViewById<EditText>(R.id.et_value_1)
        etValue2 = findViewById<EditText>(R.id.et_value_2)
        btSearch = findViewById<Button>(R.id.bt_search)
        btSearch!!.setOnClickListener {
            val value1 = etValue1!!.text.toString()
            val value2 = etValue2!!.text.toString()
            // 字符串查询
            val map: HashMap<String, Any> = HashMap()
            if (!StringUtil.isEmpty(value1)) {
                map["name"] = value1
            }
            if (!StringUtil.isEmpty(value2)) {
                val i = value2.toInt()
                map["isboy"] = i != 0
            }
            /**
             * 查询数据
             */
            val dao: BeanDao<*>? =
                BeanDao.getDaoOperate(this, StudentBean::class.java)
            val bean = dao?.queryWhereForOne(map)
            if (bean == null) {
                tvSearch?.text = "search null"
            } else {
                tvSearch?.text = bean.toString()
            }
        }
    }

    private fun initInsert() {
        etName = findViewById<EditText>(R.id.et_name)
        etAge = findViewById<EditText>(R.id.et_age)
        etIsBoy = findViewById<EditText>(R.id.et_isboy)
        btInsert = findViewById<Button>(R.id.bt_insert)
        btInsert!!.setOnClickListener {
            val bean = StudentBean()
            bean.name = etName!!.text.toString()
            bean.age = 0
            bean.isBoy = false
            val ageStr = etAge!!.text.toString()
            val isBoyStr = etIsBoy!!.text.toString()
            if (!StringUtil.isEmpty(ageStr) && NumberUtil.isInt(ageStr)) {
                bean.age = ageStr.toInt()
            }
            if (!StringUtil.isEmpty(isBoyStr) && NumberUtil.isInt(isBoyStr)) {
                if (isBoyStr.toInt() != 0) {
                    bean.isBoy = true
                }
            }
            /**
             * 插入数据
             *
             */
            val dao =
                BeanDao.getDaoOperate(this, StudentBean::class.java) as BeanDao<StudentBean>?
            if (dao != null) {
                dao.insert(bean)
            }
            searchAll()
        }
    }

    private fun initUI() {
        tvAll = findViewById<TextView>(R.id.tv_all)
        tvSearch = findViewById<TextView>(R.id.tv_reault)
    }

    /**
     * 查询所有数据
     */
    private fun searchAll() {
        tvAll!!.text = ""
        val dao: BeanDao<*>? = BeanDao.getDaoOperate(this, StudentBean::class.java)
        val list: List<StudentBean>? = dao?.queryAll() as List<StudentBean>?
        if (list != null) {
            for (bean in list) {
                tvAll!!.append(bean.toString().toString() + "\n")
            }
        }
    }
}