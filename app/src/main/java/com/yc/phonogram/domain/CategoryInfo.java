package com.yc.phonogram.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/5/15 09:49.
 */
public class CategoryInfo implements Parcelable {

    private int id;

    @JSONField(name = "is_vip")
    private int is_free;//0是免费 1是付费

    private String title;
    private String img;
    private String type_id;

    private String icon;

    public CategoryInfo() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.is_free);
        dest.writeString(this.title);
        dest.writeString(this.img);
        dest.writeString(this.type_id);
        dest.writeString(this.icon);
    }

    protected CategoryInfo(Parcel in) {
        this.id = in.readInt();
        this.is_free = in.readInt();
        this.title = in.readString();
        this.img = in.readString();
        this.type_id = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<CategoryInfo> CREATOR = new Parcelable.Creator<CategoryInfo>() {
        @Override
        public CategoryInfo createFromParcel(Parcel source) {
            return new CategoryInfo(source);
        }

        @Override
        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };
}
