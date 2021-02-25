package com.lilanz.wificonnect.beans;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "song_table")
public class SongBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;//歌曲id
    @DatabaseField(columnName = "name")
    public String name;//歌曲名
    @DatabaseField(columnName = "singer")
    public String singer;//歌手
    @DatabaseField(columnName = "size")
    public long size;//歌曲所占空间大小
    @DatabaseField(columnName = "duration")
    public int duration;//歌曲时间长度
    @DatabaseField(columnName = "path")
    public String path;//歌曲地址
    @DatabaseField(columnName = "album_id")
    public long albumId;//图片id

    public boolean isLastOne; // 是否是最后一首歌曲,用于网络通信用

    public boolean isServiceExit;   // 服务器是否已存在
    public boolean isSelect;        // 是否选择

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
