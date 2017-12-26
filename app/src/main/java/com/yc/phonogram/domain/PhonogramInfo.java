package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class PhonogramInfo {
    private int id;
    private String voice;
    private String video;
    private String cover;
    private String img;
    private String name;
    private String desp;
    private String desp_audio;

    public String getDesp_audio() {
        return desp_audio;
    }

    public void setDesp_audio(String desp_audio) {
        this.desp_audio = desp_audio;
    }

    @JSONField(name = "example")
    private List<ExampleInfo> exampleInfos;

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

    public List<ExampleInfo> getExampleInfos() {
        return exampleInfos;
    }

    public void setExampleInfos(List<ExampleInfo> exampleInfos) {
        this.exampleInfos = exampleInfos;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
