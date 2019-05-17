package com.yc.phonogram.domain;

import java.util.List;

/**
 * Created by wanglin  on 2019/5/16 11:26.
 */
public class CategoryInfoWrapper {

    private String typename;
    private String desp;

    private String id;
    private List<CategoryInfo> detail;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CategoryInfo> getDetail() {
        return detail;
    }

    public void setDetail(List<CategoryInfo> detail) {
        this.detail = detail;
    }
}
