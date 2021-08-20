package com.demon.tool.API;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Gson解析不支持泛型，利用ParameterizedType获取泛型参数类型
 * author: JayGengi 60167
 * email:  gengy@chinaraising.com
 * time:  2018/04/13 10:01
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    Class clazz;

    public ParameterizedTypeImpl(Class clz) {
        clazz = clz;
    }
    @Override
    public Type[] getActualTypeArguments() {
        //返回实际类型组成的数据
        return new Type[]{clazz};
    }
    @Override
    public Type getRawType() {
        //返回原生类型，即HashMap
        return List.class;
    }
    @Override
    public Type getOwnerType() {
        //返回Type对象
        return null;
    }
}
