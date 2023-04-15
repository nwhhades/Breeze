package com.whiner.breeze;

import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.PathUtils;
import com.google.gson.reflect.TypeToken;
import com.hjq.permissions.Permission;
import com.whiner.breeze.databinding.ActivityMainBinding;
import com.whiner.devkit.base.BaseActivity;
import com.whiner.devkit.network.NetUtils;
import com.whiner.devkit.network.OnNetListener;
import com.whiner.devkit.network.base.GetConfig;
import com.whiner.devkit.network.base.NetResult;
import com.whiner.devkit.utils.MacUtils;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected boolean enableAppBackground() {
        return false;
    }

    @Override
    protected boolean checkPermissionInit() {
        return true;
    }

    @Override
    protected List<String> getPermissionList() {
        return Arrays.asList(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void preInit() {
        super.preInit();
        setBackgroundSrc("ss");
    }

    Disposable disposable;

    @Override
    protected void init() {
        boolean b = FileIOUtils.writeFileFromString(PathUtils.getExternalStoragePath() + "/sss/aa.txt", "ssssss");
        Log.d(TAG, "init: 写入状态" + b);
        viewBinding.tvTitle.setText(MacUtils.getMac());

        viewBinding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });

        viewBinding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposable.dispose();
            }
        });

    }


    private OnNetListener<NetResult<SettingsBean>> onNetListener = new OnNetListener<NetResult<SettingsBean>>() {
        @Override
        public TypeToken<NetResult<SettingsBean>> getTypeToken() {
            return new TypeToken<NetResult<SettingsBean>>() {
            };
        }

        @Override
        public void onStart() {
            Log.d(TAG, "onStart: 请求开始了");
        }

        @Override
        public void onDisposable(Disposable d) {
            disposable = d;
        }

        @Override
        public void onSucceeded(NetResult<SettingsBean> data, boolean isCache) {
            Log.d(TAG, "onSucceeded: 请求成功了" + data + " - " + isCache);
        }

        @Override
        public void onFailed(Exception e) {
            Log.d(TAG, "onFailed: 请求异常了" + e);
        }

        @Override
        public void onEnd() {
            Log.d(TAG, "onEnd: 请求结束了");
            viewBinding.tvTitle.setText("sss");
        }
    };


    private void get() {
        GetConfig getConfig = new GetConfig();
        getConfig.setMainUrl("http://www.baidu.com/api/settings/getSettings");
        getConfig.setBackUrl("http://101.133.235.5:8686/api/settings/getSettings");
        getConfig.setCacheKey("getSettings");
        getConfig.setCacheTime(2000);
        getConfig.setCacheType(GetConfig.CacheType.FIRST_CACHE);

        NetUtils.ONE.get(getConfig, onNetListener);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onNetListener = null;
        finish();
    }

}