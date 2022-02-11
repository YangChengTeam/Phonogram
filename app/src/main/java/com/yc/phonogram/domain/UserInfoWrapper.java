package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by suns  on 2020/4/8 09:14.
 */
public class UserInfoWrapper {
    @JSONField(name = "user_info")
    private UserInfo userInfo;

    @JSONField(name = "user_vip_list")
    private List<VipInfo> vipList;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<VipInfo> getVipList() {
        return vipList;
    }

    public void setVipList(List<VipInfo> vipList) {
        this.vipList = vipList;
    }
}
