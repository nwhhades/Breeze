package com.whiner.devkit.mmkv;

import android.content.Context;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public enum MMKVUtils implements IMMKV {
    ONE;

    private static final String TAG = "MMKVUtils";

    public static void init(final Context context) {
        String root = MMKV.initialize(context);
        Log.d(TAG, "initMMKV: MMKV root is " + root);
    }

    @Override
    public String getKeyTimeSuffix() {
        return "_TIME";
    }

    @Override
    public int getNotExpiredTime() {
        return -1;
    }

    @Override
    public MMKV getMMKV() {
        return MMKV.defaultMMKV();
    }

    @Override
    public String get(String key, String def) {
        if (key == null) {
            return def;
        }
        long outTime = getMMKV().getLong(key + getKeyTimeSuffix(), 0);
        if (outTime > System.currentTimeMillis() || outTime == getNotExpiredTime()) {
            return getMMKV().getString(key, def);
        } else {
            getMMKV().removeValueForKey(key);
            getMMKV().removeValueForKey(key + getKeyTimeSuffix());
        }
        return def;
    }

    @Override
    public void put(String key, String s, long keep_time) {
        if (key == null || s == null) {
            return;
        }
        getMMKV().putString(key, s);
        if (keep_time > 0) {
            getMMKV().putLong(key + getKeyTimeSuffix(), System.currentTimeMillis() + keep_time);
        } else {
            getMMKV().putLong(key + getKeyTimeSuffix(), getNotExpiredTime());
        }
    }

}
