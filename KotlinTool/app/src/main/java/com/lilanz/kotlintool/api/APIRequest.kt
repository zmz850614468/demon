package com.lilanz.kotlintool.api

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Method
import java.util.*

/**
 * json数据格式的网络请求和解析
 *
 * @param <T>
 */
class APIRequest<T> {
    companion object {
        // 解析数据的类型
        const val PARSE_TYPE_BEAN: Int = 1
        const val PARSE_TYPE_LIST: Int = 2
        const val PARSE_TYPE_NULL: Int = 3
    }

    private var parseListener: ParseListener<T>? = null
    private var clazz: Class<*>

    constructor(clazz: Class<T>) {
        this.clazz = clazz
    }

    /**
     * @param map        网络请求所需要的参数
     * @param methodName 要调用的方法名
     * @param parseType  数据解析的类型
     */
    fun requestFor(
        map: Map<String?, Objects?>, methodName: String, parseType: Int
    ) {
        val entity = Gson().toJson(map)
        val mediaType =
            MediaType.parse("application/json; charset=UTF-8")
        val body = RequestBody.create(mediaType, entity)
        val apiService: APIService = APIManager.getService(APIService::class.java) as APIService
        var call: Call<ResponseBody?>? = null
        try {
            val methods: Array<Method> = apiService::class.java.methods
            for (method in methods) {
                val name = method.name
                if (methodName == name) {
                    call = method.invoke(
                        apiService,
                        *arrayOf<Any>(body)
                    ) as Call<ResponseBody?>
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                try {
                    if (response.body() != null) {
                        parseJson(response.body()!!.string(), parseType)
                    } else if (parseListener != null) {
                        parseListener?.onError("返回体为空")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (parseListener != null) {
                        parseListener?.onError("数据解析异常")
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>, t: Throwable
            ) {
                if (parseListener != null) {
                    parseListener?.onError(t.toString())
                }
            }
        })
    }

    /**
     * 解析请求成功后的数据
     *
     * @param jsonStr
     * @param parseType
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun parseJson(jsonStr: String, parseType: Int) {
        val `object` = JSONObject(jsonStr)
        if (`object`.has("errcode") && `object`.getInt("errcode") == 0) { // 解析数据对象
            if (parseType == PARSE_TYPE_LIST) {
                val jsonArray = `object`.getJSONArray("data")
                var beanList = ArrayList<T>()
                for (i in 0 until jsonArray.length()) {
                    val tempObj = jsonArray.getJSONObject(i).toString()
                    val t = Gson().fromJson(tempObj, clazz) as T
                    beanList.add(t)
                }
                //                ArrayList<T> beanList = new Gson().fromJson(jsonArray.toString(),
//                        new TypeToken<ArrayList<T>>() {}.getType());
                if (parseListener != null) {
                    if (beanList == null) {
                        beanList = ArrayList()
                    }
                    parseListener?.jsonParsed(beanList)
                }
                // 解析单个对象
            } else if (parseType == PARSE_TYPE_BEAN) {
                val obj = `object`.getJSONObject("data")
                val bean = Gson().fromJson(obj.toString(), clazz) as T
                if (parseListener != null) {
                    parseListener?.jsonParsed(bean)
                }
                // 只有提示信息
            } else if (parseType == PARSE_TYPE_NULL) {
                if (parseListener != null) {
                    parseListener?.onTip("请求成功")
                }
            }
        } else {
            if (`object`.has("errmsg") && parseListener != null) {
                val errMsg = `object`.getString("errmsg")
                parseListener?.onError(errMsg)
            }
        }
    }

    fun setParseListener(parseListener: ParseListener<T>?) {
        this.parseListener = parseListener
    }
//
//    fun <T> setParseListener(p0: Any) {
//
//    }
}