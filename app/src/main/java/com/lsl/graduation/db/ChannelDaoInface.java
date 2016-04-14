package com.lsl.graduation.db;

import android.content.ContentValues;

import com.lsl.graduation.bean.ChannelItem;

import java.util.List;
import java.util.Map;

/**
 * Created by Forrest on 16/4/13.
 */
public  interface ChannelDaoInface {
    /**
     * 添加channelItem
     * @param item
     * @return
     */
    boolean addCache(ChannelItem item);

    /**
     * 删除数据
     * @param whereClause
     * @param whereArgs
     * @return
     */
    boolean deleteCache(String whereClause, String[] whereArgs);

    /**
     * 更新数据库
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    boolean updateCache(ContentValues values, String whereClause,
                        String[] whereArgs);

    Map<String, String> viewCache(String selection,
                                  String[] selectionArgs);

    List<Map<String, String>> listCache(String selection,
                                        String[] selectionArgs);

    void clearFeedTable();
}
