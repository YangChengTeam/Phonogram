package com.kk.pay;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/4/14.
 */

public class PayInfo {
    //基本信息
    private String partnerid; //商户id
    private String appid;    //应用id
    private String notify_url; //回调地址

    //支付方式相关信息
    private String payway_account_name;  //支付方式帐号名
    private String payway;    //支付方式

    //原生支付宝支付信息
    private String email;  //邮箱
    private String privatekey; //密钥

    //现在支付渠道信息
    @JSONField(name = "rsmd5")
    private String wx_sign;  //现在支付 md5校验值
    private String starttime; //现在支付 订单创建时间

    //香蕉互娱渠道信息
    private String channelId; //银行微信支付 渠道id

    //原生微信渠道信息
    private String mch_id;
    private String nonce_str;
    private String prepay_id;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String trade_type;

    //微信h5支付渠道信息
    @JSONField(name = "pay_url")
    private String payurl;
    @JSONField(name = "front_notify_url")
    private String frontnotifyurl;
    private String timestamp;

    //小小支付
    private String ip;
    private String merchantID;
    private String key;
    private String return_url;

    @JSONField(name = "is_h5_pay")
    private int isH5Pay;

    @JSONField(name = "is_override_url")
    private int isOverrideUrl = 1;

    public int getIsOverrideUrl() {
        return isOverrideUrl;
    }

    public void setIsOverrideUrl(int isOverrideUrl) {
        this.isOverrideUrl = isOverrideUrl;
    }

    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public int getIsH5Pay() {
        return isH5Pay;
    }

    public void setIsH5Pay(int isH5Pay) {
        this.isH5Pay = isH5Pay;
    }


    //扬扬助手
    private String callback_url;

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getPayurl() {
        return payurl;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl;
    }

    public String getFrontnotifyurl() {
        return frontnotifyurl;
    }

    public void setFrontnotifyurl(String frontnotifyurl) {
        this.frontnotifyurl = frontnotifyurl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getPayway_account_name() {
        return payway_account_name;
    }

    public void setPayway_account_name(String payway_account_name) {
        this.payway_account_name = payway_account_name;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getWx_sign() {
        return wx_sign;
    }

    public void setWx_sign(String wx_sign) {
        this.wx_sign = wx_sign;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
