package com.whiner.devkit.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

public class SysUtils {

    private static final String sysSharedUserId = "android.uid.system";

    public static boolean isSysApp() {
        try {
            PackageManager packageManager = Utils.getApp().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(AppUtils.getAppPackageName(), 0);
            return sysSharedUserId.equalsIgnoreCase(packageInfo.sharedUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getValue(String key) {
        return Settings.Global.getString(Utils.getApp().getContentResolver(), key);
    }

    public static void setValue(String key, String value) {
        Settings.Global.putString(Utils.getApp().getContentResolver(), key, value);
    }

}
