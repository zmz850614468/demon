package com.lilanz.wificonnect.data.myenum;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景类型
 */
public enum SceneType {
    DOOR("门口"),
    BALCONY("阳台"),
    LIVING_ROOM("客厅"),
    MASTER_BEDROOM("主卧"),
    SECOND_BEDROOM("次卧"),
    GUEST_BEDROOM("客卧"),
    ;

    public String name;

    SceneType(String name) {
        this.name = name;
    }

    /**
     * 获取所有场景名称
     *
     * @return
     */
    public static List<String> getSceneName() {
        List<String> list = new ArrayList<>();
        for (SceneType value : SceneType.values()) {
            list.add(value.name);
        }

        return list;
    }

    /**
     * 通过场景名称获取场景类型
     *
     * @param sceneName
     * @return
     */
    public static SceneType getSceneType(@NonNull String sceneName) {
        for (SceneType value : SceneType.values()) {
            if (sceneName.equals(value.name)) {
                return value;
            }
        }
        return null;
    }
}
