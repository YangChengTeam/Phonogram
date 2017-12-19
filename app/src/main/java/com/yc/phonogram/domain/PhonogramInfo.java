package com.yc.phonogram.domain;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class PhonogramInfo {
    private int id;
    private String video;
    private String cover;
    private String img;
    private String name;
    private String desp;
    private ExampleInfo exampleInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public ExampleInfo getExampleInfo() {
        return exampleInfo;
    }

    public void setExampleInfo(ExampleInfo exampleInfo) {
        this.exampleInfo = exampleInfo;
    }
}
