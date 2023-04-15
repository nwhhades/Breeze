package com.whiner.devkit.apk;

public class ApkInstallEvent {

    private String packageName;//安装应用的包名
    private boolean status;//安装是否成功了

    public ApkInstallEvent(String packageName, boolean status) {
        this.packageName = packageName;
        this.status = status;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
