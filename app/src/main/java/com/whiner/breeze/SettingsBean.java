package com.whiner.breeze;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class SettingsBean implements Serializable {

    private static final long serialVersionUID = 5039078745337183094L;

    private long apk_version;//apk版本
    private String apk_file;//apk更新文件
    private String apk_bg_image;//apk主背景
    private String apk_home_skin;//主页风格类型 skin_1 风格一 ...
    private String apk_home_logo_image;//主页logo
    private String apk_home_msg;//主页游字
    private String mqtt_url;//订阅地址
    private String mqtt_taps;//订阅标签
    private String shop_qrcode_image;//商店二维码
    private String shop_qrcode_tip;//二维码提示
    private String home_data_version;//主页数据的版本号

    public long getApk_version() {
        return apk_version;
    }

    public void setApk_version(long apk_version) {
        this.apk_version = apk_version;
    }

    public String getApk_file() {
        return apk_file;
    }

    public void setApk_file(String apk_file) {
        this.apk_file = apk_file;
    }

    public String getApk_bg_image() {
        return apk_bg_image;
    }

    public void setApk_bg_image(String apk_bg_image) {
        this.apk_bg_image = apk_bg_image;
    }

    public String getApk_home_skin() {
        return apk_home_skin;
    }

    public void setApk_home_skin(String apk_home_skin) {
        this.apk_home_skin = apk_home_skin;
    }

    public String getApk_home_logo_image() {
        return apk_home_logo_image;
    }

    public void setApk_home_logo_image(String apk_home_logo_image) {
        this.apk_home_logo_image = apk_home_logo_image;
    }

    public String getApk_home_msg() {
        return apk_home_msg;
    }

    public void setApk_home_msg(String apk_home_msg) {
        this.apk_home_msg = apk_home_msg;
    }

    public String getMqtt_url() {
        return mqtt_url;
    }

    public void setMqtt_url(String mqtt_url) {
        this.mqtt_url = mqtt_url;
    }

    public String getMqtt_taps() {
        return mqtt_taps;
    }

    public void setMqtt_taps(String mqtt_taps) {
        this.mqtt_taps = mqtt_taps;
    }

    public String getShop_qrcode_image() {
        return shop_qrcode_image;
    }

    public void setShop_qrcode_image(String shop_qrcode_image) {
        this.shop_qrcode_image = shop_qrcode_image;
    }

    public String getShop_qrcode_tip() {
        return shop_qrcode_tip;
    }

    public void setShop_qrcode_tip(String shop_qrcode_tip) {
        this.shop_qrcode_tip = shop_qrcode_tip;
    }

    public String getHome_data_version() {
        return home_data_version;
    }

    public void setHome_data_version(String home_data_version) {
        this.home_data_version = home_data_version;
    }

    @NonNull
    @Override
    public String toString() {
        return "SettingsBean{" +
                "apk_version=" + apk_version +
                ", apk_file='" + apk_file + '\'' +
                ", apk_bg_image='" + apk_bg_image + '\'' +
                ", apk_home_skin='" + apk_home_skin + '\'' +
                ", apk_home_logo_image='" + apk_home_logo_image + '\'' +
                ", apk_home_msg='" + apk_home_msg + '\'' +
                ", mqtt_url='" + mqtt_url + '\'' +
                ", mqtt_taps='" + mqtt_taps + '\'' +
                ", shop_qrcode_image='" + shop_qrcode_image + '\'' +
                ", shop_qrcode_tip='" + shop_qrcode_tip + '\'' +
                ", home_data_version='" + home_data_version + '\'' +
                '}';
    }

}
