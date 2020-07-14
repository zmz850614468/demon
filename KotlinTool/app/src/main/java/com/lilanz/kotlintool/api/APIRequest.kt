package com.lilanz.kotlintool.api

import android.support.annotation.NonNull
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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
        const val PARSE_TYPE_BEAN: Int = 1      // 返回 Bean 对象
        const val PARSE_TYPE_LIST: Int = 2      // 返回 List 对象
        const val PARSE_TYPE_NULL: Int = 3      // 返回 提示 信息
        const val PARSE_TYPE_JSON: Int = 4      // 返回 json 信息
    }

    private var parseListener: ParseListener<T>? = null
    private var clazz: Class<*>
    private var requestBasePath: String? = null // 请求的首地址

    constructor(clazz: Class<T>) {
        this.clazz = clazz
    }

    /**
     * 请求参数格式：Params; Body-FormUrlEncoded;
     */
    fun requestNormal(obj: Any, @NonNull methodName: String, parseType: Int) {
        val apiService: APIService = APIManager.getService(requestBasePath, APIService::class.java) as APIService
        var call: Call<ResponseBody?>? = null
        try {
            val methods: Array<Method> = apiService::class.java.methods
            for (method in methods) {
                val name = method.name
                if (methodName == name) {
                    call = method.invoke(apiService, *arrayOf<Any>(obj)) as Call<ResponseBody?>
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
                        parseListener?.onError(-1, "返回体为空")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (parseListener != null) {
                        parseListener?.onError(-1, "数据解析异常")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                parseListener?.onError(-1, t.toString())
            }
        })
    }

    /**
     * 请求参数格式：Body-raw-json 格式
     */
    fun requestByJson(map: Map<String?, Objects?>, methodName: String, parseType: Int) {
        val entity = Gson().toJson(map)
        val mediaType =
            MediaType.parse("application/json; charset=UTF-8")
        val body = RequestBody.create(mediaType, entity)

        requestNormal(body, methodName, parseType)
    }

    /**
     * 请求参数格式：Body-FormData 格式  @Multipart
     */
    fun requestUploadImage(
        fileKey: String, file: File, map: Map<String, String>, methodName: String,
        parseType: Int
    ) {
        val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key, value) in map) {
            builder.addFormDataPart(key, value)
        }
        builder.addFormDataPart(fileKey, file.name, body)

        val parts = builder.build().parts()
        requestNormal(parts, methodName, parseType)
    }

    /**
     * 解析请求成功后的数据
     */
    @Throws(Exception::class)
    private fun parseJson(jsonStr: String, parseType: Int) {
        // 直接返回json字符串
        if (parseType == PARSE_TYPE_JSON) {
            parseListener?.jsonResult(jsonStr);
            return
        }

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

                if (beanList == null) {
                    beanList = ArrayList()
                }
                parseListener?.jsonParsed(beanList)
                // 解析单个对象
            } else if (parseType == PARSE_TYPE_BEAN) {
                val obj = `object`.getJSONObject("data")
                val bean = Gson().fromJson(obj.toString(), clazz) as T
                parseListener?.jsonParsed(bean)
                // 只有提示信息
            } else if (parseType == PARSE_TYPE_NULL) {
                parseListener?.onTip("请求成功")
            }
        } else if (`object`.has("errcode") && `object`.has("errmsg")) {
            val errCode = `object`.getInt("errcode")
            val errMsg = `object`.getString("errmsg")
            parseListener?.onError(errCode, errMsg)
        } else {
            showErrMsg(-1, "没有任何错误信息")
        }
    }

    /**
     * 返回错误代码，和错误信息
     *
     * @param errCode
     * @param errMsg
     */
    private fun showErrMsg(errCode: Int, errMsg: String) {
        if (parseListener != null) {
            parseListener!!.onError(errCode, errMsg)
        }
    }

    fun setRequestBasePath(requestBasePath: String?) {
        this.requestBasePath = requestBasePath
    }

    fun setParseListener(parseListener: ParseListener<T>?) {
        this.parseListener = parseListener
    }

}