package com.whiner.devkit.mmkv;

import com.tencent.mmkv.MMKV;

public interface IMMKV {

    String getKeyTimeSuffix();

    int getNotExpiredTime();

    MMKV getMMKV();

    String get(String key, String def);

    void put(String key, String s, long keep_time);

}
