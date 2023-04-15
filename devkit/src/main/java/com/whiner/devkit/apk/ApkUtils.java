package com.whiner.devkit.apk;

import android.app.Activity;

import com.blankj.utilcode.util.AppUtils;
import com.whiner.devkit.utils.SysUtils;

import java.io.File;

public class ApkUtils {

    public static String getApkPackageName(final File file) {
        String packageName = null;
        AppUtils.AppInfo appInfo = AppUtils.getApkInfo(file);
        if (appInfo != null) {
            packageName = appInfo.getPackageName();
        }
        return packageName;
    }

    /**
     * 需要在Activity订阅ApkInstallEvent
     *
     * @param activity 目标Act
     * @param file     应用文件
     */
    public static String installApk(final Activity activity, final File file) {
        if (activity == null || file == null) {
            return null;
        }
        AppUtils.AppInfo appInfo = AppUtils.getApkInfo(file);
        if (appInfo == null) {
            return null;
        }
        //应用有系统权限,安装app需要这样操作
        if (SysUtils.isSysApp()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                SilentInstallUtils.silentInstall(activity, file);
                return appInfo.getPackageName();
            }
        }
        AppUtils.installApp(file);
        return appInfo.getPackageName();
    }

}
