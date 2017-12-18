package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/2/21.
 */

public class LoginDataInfo {

    @JSONField(name = "update")
    private UpdateInfo updateInfo;

    @JSONField(name = "user_info")
    private StatusInfo statusInfo;

    @JSONField(name = "share_info")
    private ShareInfo shareInfo;

    @JSONField(name = "pub_key")
    private String publicKey;

    @JSONField(name = "contact_info")
    private ContactInfo contactInfo;

    @JSONField(name = "user_vip_list")
    private List<VipInfo> vipInfoList;

    public List<VipInfo> getVipInfoList() {
        return vipInfoList;
    }

    public void setVipInfoList(List<VipInfo> vipInfoList) {
        this.vipInfoList = vipInfoList;
    }


    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }



}
