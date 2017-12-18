package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/2/21.
 */

public class StatusInfo {

    @JSONField(name = "is_reg")
    private boolean isRegister;

    @JSONField(name = "vip_test_hour")
    private int tryHour;

    @JSONField(name = "user_id")
    private String uid;

    private int status;

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public int getTryHour() {
        return tryHour;
    }

    public void setTryHour(int tryHour) {
        this.tryHour = tryHour;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
