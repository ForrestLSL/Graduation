
package com.lsl.graduation.json;

import android.content.Context;

import com.lsl.graduation.bean.VideoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViedoListJson extends JsonPacket {

    public static ViedoListJson newListJson;

    public List<VideoModel> videoModles;

    public ViedoListJson(Context context) {
        super(context);
    }

    public static ViedoListJson instance(Context context) {
        if (newListJson == null) {
            newListJson = new ViedoListJson(context);
        }
        return newListJson;
    }

    public List<VideoModel> readJsonVideoModles(String res, String value) {
        videoModles = new ArrayList<VideoModel>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            VideoModel videoModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                videoModle = readVideoModle(jsonArray.getJSONObject(i));
                videoModles.add(videoModle);
            }
        } catch (Exception e) {

        } finally {
            System.gc();
        }
        return videoModles;
    }

    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public VideoModel readVideoModle(JSONObject jsonObject) throws Exception {
        VideoModel videoModle = null;

        String vid = "";
        String title = "";
        int length = 0;
        String cover = "";
        String mp4Hd_url = "";

        vid = getString("vid", jsonObject);
        title = getString("title", jsonObject);
        length = getInt("length", jsonObject);
        cover = getString("cover", jsonObject);
        mp4Hd_url = getString("mp4_url", jsonObject);

        videoModle = new VideoModel();

        videoModle.setCover(cover);
        if (length == -1) {
            videoModle.setTitle(getString("sectiontitle", jsonObject));
            videoModle.setLength(getTitle(title));
        } else {
            videoModle.setLength(getTime(length));
            videoModle.setTitle(getTitle(title));
        }
        videoModle.setMp4Hd_url(mp4Hd_url);
        videoModle.setVid(vid);

        return videoModle;
    }

    public String getTitle(String title) {
        if (title.contains("&quot;")) {
            title = title.replace("&quot;", "\"");
        }
        return title;
    }

    public String getTime(int length) {
        int fen = length / 60;
        int miao = length % 60;
        String fenString = fen + "";
        String miaoString = miao + "";
        fenString = fenString.length() == 1 ? "0" + fenString : fenString;
        miaoString = miaoString.length() == 1 ? miaoString + "0" : miaoString;
        return fenString + ":" + miaoString;
    }

}
