package com.whiner.devkit.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.hjq.toast.Toaster;
import com.whiner.devkit.mmkv.MMKVUtils;
import com.whiner.devkit.toaster.BigBlackToastStyle;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        MMKVUtils.init(this);
        initToaster();
        init();
    }

    protected void initToaster() {
        Toaster.init(this, new BigBlackToastStyle());
    }

    protected abstract void init();

}
