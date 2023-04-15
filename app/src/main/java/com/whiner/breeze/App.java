package com.whiner.breeze;

import android.util.Log;

import com.whiner.devkit.base.BaseApplication;

public class App extends BaseApplication {

    private static final String TAG = "App";

    @Override
    protected void init() {
        Log.d(TAG, "onCreate: " + this);
    }

}
