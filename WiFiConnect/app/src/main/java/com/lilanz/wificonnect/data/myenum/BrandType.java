package com.lilanz.wificonnect.data.myenum;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌类型
 */
public enum BrandType {
    GREE("GREE"),
    OTHER("其他");

    public String name;

    BrandType(String s) {
        this.name = s;
    }

    /**
     * 风扇的品牌类型 名称
     *
     * @return
     */
    public static List<String> getElectricFanBrand() {
        List<String> list = new ArrayList<>();
        list.add(GREE.name);
//        list.add(OTHER);
        return list;
    }

    /**
     * 灯的品牌 名称
     *
     * @return
     */
    public static List<String> getLampBrand() {
        List<String> list = new ArrayList<>();
        list.add(OTHER.name);
        return list;
    }

    /**
     * 通过品牌名称获取品牌类型
     *
     * @param brandName
     * @return
     */
    public static BrandType getBrandType(@NonNull String brandName) {
        for (BrandType value : BrandType.values()) {
            if (brandName.equals(value.name)) {
                return value;
            }
        }
        return null;
    }
}
