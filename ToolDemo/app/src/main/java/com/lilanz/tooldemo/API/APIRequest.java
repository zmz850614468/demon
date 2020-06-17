package com.lilanz.tooldemo.API;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * json数据格式的网络请求和解析
 *
 * @param <T>
 */
public class APIRequest<T> {

    // 解析数据的类型
    public static final int PARSE_TYPE_BEAN = 1;
    public static final int PARSE_TYPE_LIST = 2;
    public static final int PARSE_TYPE_NULL = 3;

    private ParseListener<T> parseListener;
    private Class clazz;

    public APIRequest(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @param map        网络请求所需要的参数
     * @param methodName 要调用的方法名
     * @param parseType  数据解析的类型
     */
    public void requestFor(@NonNull Map<String, String> map, @NonNull String methodName, final int parseType) {
        String entity = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, entity);
        APIService apiService = APIManager.getService(APIService.class);

        Call<ResponseBody> call = null;
        try {
            Method[] methods = apiService.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (methodName.equals(name)) {
                    call = (Call<ResponseBody>) method.invoke(apiService, new Object[]{body});
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (call != null) {
            // 异步请求：call.enqueue();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            parseJson(response.body().string(), parseType);
                        } else if (parseListener != null) {
                            parseListener.onError("返回体为空");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (parseListener != null) {
                            parseListener.onError("数据解析异常");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (parseListener != null) {
                        parseListener.onError(t.toString());
                    }
                }
            });
        }
    }

    /**
     * 解析请求成功后的数据
     *
     * @param jsonStr
     * @param parseType
     * @throws Exception
     */
    private void parseJson(String jsonStr, int parseType) throws Exception {
        JSONObject object = new JSONObject(jsonStr);
        if (object.has("errcode") && object.getInt("errcode") == 0) {

            // 解析数据对象
            if (parseType == PARSE_TYPE_LIST) {
                JSONArray jsonArray = object.getJSONArray("data");
                ArrayList<T> beanList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String tempObj = jsonArray.getJSONObject(i).toString();
                    T t = (T) new Gson().fromJson(tempObj, clazz);
                    beanList.add(t);
                }
//                ArrayList<T> beanList = new Gson().fromJson(jsonArray.toString(),
//                        new TypeToken<ArrayList<T>>() {}.getType());
                if (parseListener != null) {
                    if (beanList == null) {
                        beanList = new ArrayList<>();
                    }
                    parseListener.jsonParsed(beanList);
                }
                // 解析单个对象
            } else if (parseType == PARSE_TYPE_BEAN) {
                JSONObject obj = object.getJSONObject("data");
                T bean = (T) new Gson().fromJson(obj.toString(), clazz);
                if (parseListener != null) {
                    parseListener.jsonParsed(bean);
                }
                // 只有提示信息
            } else if (parseType == PARSE_TYPE_NULL) {
                if (parseListener != null) {
                    parseListener.onTip("请求成功");
                }
            }
        } else {
            if (object.has("errmsg") && parseListener != null) {
                String errMsg = object.getString("errmsg");
                parseListener.onError(errMsg);
            }
        }
    }

    public void setParseListener(ParseListener<T> parseListener) {
        this.parseListener = parseListener;
    }
}
