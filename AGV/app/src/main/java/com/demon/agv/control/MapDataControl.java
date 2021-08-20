package com.demon.agv.control;

import com.demon.agv.bean.MapBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图数据控制类
 */
public class MapDataControl {

    public static final int DISTANCE = 5;   // 每格对应的大小：cm

    private Map<String, MapBean> dataMap = new HashMap<>();

    public MapDataControl() {
    }

    private MapBean mapBean;
    private String key;

    /**
     * 添加新的标准点
     *
     * @param oriX
     * @param oriY
     * @param disX
     * @param disY
     */
    public void addMark(int oriX, int oriY, int disX, int disY) {
        oriX -= oriX % DISTANCE;
        oriY -= oriY % DISTANCE;
        disX -= disX % DISTANCE;
        disY -= disY % DISTANCE;

        key = String.format("%d-%d", disX, disY);
        if (dataMap.containsKey(key)) {
            mapBean = dataMap.get(key);
            mapBean.addMarkCount();
        } else {
            mapBean = new MapBean(key, oriX, oriY, disX, disY);
            dataMap.put(key, mapBean);
        }
    }

    public void reSet() {
        dataMap.clear();
    }

    /**
     * 获取所有不可走的点信息
     *
     * @return
     */
    public List<MapBean> getMarkData() {
        List<MapBean> list = new ArrayList<>();
        for (Map.Entry<String, MapBean> entry : dataMap.entrySet()) {
            if (entry.getValue().type == MapBean.TYPE_CANNOT) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

}
