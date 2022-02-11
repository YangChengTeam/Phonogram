package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/4/7 09:53.
 */
public class UserInfo {

    /**
     * user_info : {"id":"700604","imeil":"3d685df9486bd3f","logo":null,"gender":null,"mobile":"18872922735","app_id":"5","status":"0","username":"18872922735","pwd":"HkXkpnyBOuO1Wi8qWjm1OVbb0+9FUld5Cg0n/RyHmc+dLeUrl7Qhr/kr","salt":"GUrTajdkgM"}
     * user_vip_list : {}
     */
    @JSONField(name = "id")
    private String userId;
    /**
     * id : 700604
     * imeil : 3d685df9486bd3f
     * logo : null
     * gender : null
     * mobile : 18872922735
     * app_id : 5
     * status : 0
     * username : 18872922735
     * pwd : HkXkpnyBOuO1Wi8qWjm1OVbb0+9FUld5Cg0n/RyHmc+dLeUrl7Qhr/kr
     * salt : GUrTajdkgM
     */


    private String imeil;
    private String logo;
    private int gender;
    private String mobile;
    private String app_id;
    private String status;
    private String username;
    private String pwd;
    private String salt;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getImeil() {
        return imeil;
    }

    public void setImeil(String imeil) {
        this.imeil = imeil;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
