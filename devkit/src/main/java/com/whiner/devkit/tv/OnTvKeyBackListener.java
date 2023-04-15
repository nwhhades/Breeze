package com.whiner.devkit.tv;

import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class OnTvKeyBackListener implements View.OnKeyListener, DialogInterface.OnKeyListener {

    private static final String TAG = "OnTvKeyBackListener";
    private static final int INTERVAL = 500;    //响应间隔时间
    private long lastReturnTime;                //上次响应返回事件时间

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastReturnTime > INTERVAL) {
                lastReturnTime = curTime;
                return onBack(v, null);
            } else {
                Log.d(TAG, "onKey: 本次按键无效化,因为您按的太快了");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastReturnTime > INTERVAL) {
                lastReturnTime = curTime;
                return onBack(null, dialog);
            } else {
                Log.d(TAG, "onKey: 本次按键无效化,因为您按的太快了");
                return true;
            }
        }
        return false;
    }

    public abstract boolean onBack(@Nullable View view, @Nullable DialogInterface dialog);

}
