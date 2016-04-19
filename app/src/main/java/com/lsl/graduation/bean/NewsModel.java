
package com.lsl.graduation.bean;

import java.util.List;

public class NewsModel extends BaseModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * docid
     */
    private String docid;
    /**
     * 标题
     */
    private String title;
    /**
     *
     */
    private String digest;
    /**
     *
     */
    private String imgsrc;
    /**
     * 来源
     */
    private String source;
    /**
     *
     */
    private String ptime;
    /**
     * TAG
     */
    private String tag;
    /**
     *
     */
    private ImagesModel imagesModle;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ImagesModel getImagesModle() {
        return imagesModle;
    }

    public void setImagesModle(ImagesModel imagesModle) {
        this.imagesModle = imagesModle;
    }

    /**
     * 图片列表
     */
    private List<ImagesModel> imgHeadLists;

    public List<ImagesModel> getImgHeadLists() {
        return imgHeadLists;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public void setImgHeadLists(List<ImagesModel> imgHeadLists) {
        this.imgHeadLists = imgHeadLists;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
