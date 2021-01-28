package com.demon.dream_realizer_car.bean;

import com.demon.dream_realizer_car.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 线路图对象
 */
@DatabaseTable(tableName = "route_table")
public class RouteBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;
    @DatabaseField(columnName = "name")
    public String name;
    @DatabaseField(columnName = "travel_str")
    public String travelStr;

    private List<TravelBean> travelList;

    public void setTravelList(List<TravelBean> travelList) {
        this.travelList = travelList;
        travelStr = new Gson().toJson(travelList);
    }

    public List<TravelBean> getTravelList() {
        if (!StringUtil.isEmpty(travelStr)) {
            this.travelList = new Gson().fromJson(travelStr, new TypeToken<List<TravelBean>>() {
            }.getType());
        }
        return travelList;
    }
}
