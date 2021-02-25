package com.lilanz.wificonnect.data.myenum;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景类型
 */
public enum SceneType {
    MASTER_BEDROOM("主卧"),
    SECOND_BEDROOM("次卧"),
    GUEST_BEDROOM("客卧"),
    LIVING_ROOM("客厅"),
    BALCONY("阳台"),
    DOOR("门口"),
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
