package com.yc.phonogram.domain;

/**
 * Created by zhangkai on 2017/10/17.
 */

public class SkillBoxInfo {

    private String title;
    private String iconName;
    private boolean ispay;
    private String previewName;

    public String getPreviewName() {
        return previewName;
    }

    public void setPreviewName(String previewName) {
        this.previewName = previewName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean ispay() {
        return ispay;
    }

    public void setIspay(boolean ispay) {
        this.ispay = ispay;
    }
}
