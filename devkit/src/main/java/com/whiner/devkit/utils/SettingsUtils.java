package com.whiner.devkit.utils;

import android.content.Intent;
import android.provider.Settings;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

public class SettingsUtils {

    public static void openSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getApp().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.launchApp("com.android.settings");
        }
    }

    public static void openNetSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getApp().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            openSetting();
        }
    }

}
