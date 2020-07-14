package com.lilanz.tooldemo.multiplex.API;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.bleModel.BleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    public static final int PARSE_TYPE_BEAN = 1;    // 返回 Bean 对象
    public static final int PARSE_TYPE_LIST = 2;    // 返回 List 对象
    public static final int PARSE_TYPE_NULL = 3;    // 返回 提示 信息
    public static final int PARSE_TYPE_JSON = 4;    // 返回 json 信息

    private ParseListener<T> parseListener;
    private Class clazz;
    private String requestBasePath;     // 请求的首地址

    public APIRequest(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * 请求参数格式：Params; Body-FormUrlEncoded;
     *
     * @param object
     * @param methodName
     * @param parseType
     */
    public void requestNormal(@NonNull Object object, @NonNull String methodName, final int parseType) {
        APIService apiService = APIManager.getService(requestBasePath, APIService.class);

        Call<ResponseBody> call = null;
        try {
            Method[] methods = apiService.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (methodName.equals(name)) {
                    call = (Call<ResponseBody>) method.invoke(apiService, new Object[]{object});
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrMsg(-1, "获取call方法异常");
        }
        if (call != null) {
            // 异步请求：call.enqueue();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            parseJson(response.body().string(), parseType);
                        } else {
                            showErrMsg(-1, "返回体为空");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrMsg(-1, "数据解析异常");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (parseListener != null) {
                        parseListener.onError(-1, t.toString());
                    }
                }
            });
        } else {
            showErrMsg(-1, "call为空");
        }
    }

    /**
     * 请求参数格式：Body-raw-json 格式
     *
     * @param map        网络请求所需要的参数
     * @param methodName 要调用的方法名
     * @param parseType  数据解析的类型
     */
    public void requestByJson(@NonNull Map<String, Object> map, @NonNull String methodName, final int parseType) {
        String entity = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, entity);

        requestNormal(body, methodName, parseType);
    }

    /**
     *
     *
     * @param fileKey    文件参数名
     * @param file       文件
     * @param map        其他参数
     * @param methodName 方法名
     * @param parseType  请求结果参数
     */
    public void requestUploadImage(String fileKey, File file, @NonNull Map<String, String> map, @NonNull String methodName, final int parseType) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        builder.addFormDataPart(fileKey, file.getName(), body);

        List<MultipartBody.Part> parts = builder.build().parts();
        requestNormal(parts, methodName, parseType);
    }

    /**
     * 解析请求成功后的数据
     *
     * @param jsonStr
     * @param parseType
     * @throws Exception
     */
    private void parseJson(String jsonStr, int parseType) throws Exception {
        // 直接返回json字符串
        if (parseType == PARSE_TYPE_JSON) {
            if (parseListener != null) {
                parseListener.jsonResult(jsonStr);
            }
            return;
        }

        // 解析json字符串，返回对应的对象
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
        } else if (object.has("errcode") && object.has("errmsg")) {
            int errCode = object.getInt("errcode");
            String errMsg = object.getString("errmsg");
            showErrMsg(errCode, errMsg);
        } else {
            showErrMsg(-1, "没有任何错误信息");
        }
    }

    /**
     * 返回错误代码，和错误信息
     *
     * @param errCode
     * @param errMsg
     */
    private void showErrMsg(int errCode, String errMsg) {
        if (parseListener != null) {
            parseListener.onError(errCode, errMsg);
        }
    }

    public void setRequestBasePath(String requestBasePath) {
        this.requestBasePath = requestBasePath;
    }

    public void setParseListener(ParseListener<T> parseListener) {
        this.parseListener = parseListener;
    }
}
