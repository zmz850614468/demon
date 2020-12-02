package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.lilanz.wificonnect.beans.SongBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲信息控制类
 */
public class SongControl {

    /**
     * 获取本地歌曲信息
     *
     * @param context
     * @return
     */
    public static List<SongBean> getSongList(Context context) {
        List<SongBean> songList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SongBean song = new SongBean();
                song.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//                song.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                song.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                if (song.duration > 60000) {
                    songList.add(song);
                }
            }
        }
        return songList;
    }

}
