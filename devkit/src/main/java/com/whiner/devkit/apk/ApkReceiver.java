package com.whiner.devkit.apk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.hjq.toast.Toaster;

import org.greenrobot.eventbus.EventBus;

public class ApkReceiver extends BroadcastReceiver implements LifecycleEventObserver {

    private static final String TAG = "ApkReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (intent != null) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            String packageName = intent.getData().getSchemeSpecificPart();
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    Toaster.show(packageName + " - 安装成功");
                    sendApkInstallEvent(packageName);
                    break;
                case Intent.ACTION_PACKAGE_REPLACED:
                    Toaster.show(packageName + " - 替换成功");
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    Toaster.show(packageName + " - 卸载成功");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (source instanceof Activity) {
            Activity activity = (Activity) source;
            if (event == Lifecycle.Event.ON_CREATE) {
                Log.d(TAG, "onStateChanged: 开始注册");
                register(activity);
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                Log.d(TAG, "onStateChanged: 注销注册");
                unregister(activity);
            }
        }
    }

    private void register(Activity activity) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        activity.registerReceiver(this, intentFilter);
    }

    private void unregister(Activity activity) {
        activity.unregisterReceiver(this);
    }

    //发送安装成功的Event
    private static void sendApkInstallEvent(String packageName) {
        EventBus.getDefault().post(new ApkInstallEvent(packageName, true));
    }

}
