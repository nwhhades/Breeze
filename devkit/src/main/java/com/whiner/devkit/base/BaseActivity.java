package com.whiner.devkit.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.hjq.permissions.XXPermissions;
import com.whiner.devkit.R;
import com.whiner.devkit.ui.fragment.LoadingDialogFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected V viewBinding;

    protected abstract V getViewBinding();

    protected abstract boolean enableAppBackground();

    protected abstract boolean checkPermissionInit();

    protected abstract List<String> getPermissionList();

    protected void preInit() {
    }

    protected abstract void init();

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 3840);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: " + this);
        super.onCreate(savedInstanceState);
        preInit();
        viewBinding = getViewBinding();
        //添加背景View
        if (enableAppBackground()) {
            addAppBackground();
        }
        setContentView(viewBinding.getRoot());
        initPermission();
        if (checkPermissionInit()) {
            return;
        }
        init();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: " + this);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: " + this);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: " + this);
        super.onResume();
        loadAppBackground();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: " + this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: " + this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + this);
        hideLoading();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + this);
        super.onBackPressed();
    }

    protected void initPermission() {
        if (getPermissionList() == null) {
            return;
        }
        //默认需要网络权限
        List<String> list = new ArrayList<>();
        list.add("android.permission.INTERNET");
        list.addAll(getPermissionList());
        XXPermissions.with(this)
                .permission(list)
                .request((permissions, allGranted) -> {
                    Log.d(TAG, "initPermission: " + permissions);
                    if (allGranted) {
                        permissionSuccess();
                        if (checkPermissionInit()) {
                            init();
                        }
                    } else {
                        permissionFail();
                    }
                });
    }

    protected void permissionSuccess() {
        Log.d(TAG, "permissionSuccess: 权限正常");
    }

    protected void permissionFail() {
        Log.d(TAG, "permissionFail: 权限异常");
    }

    protected volatile LoadingDialogFragment loadingDialogFragment;

    protected synchronized void showLoading(String msg) {
        if (loadingDialogFragment == null) {
            loadingDialogFragment = new LoadingDialogFragment();
        }
        loadingDialogFragment.showLoading(getSupportFragmentManager(), msg);
    }

    protected synchronized void hideLoading() {
        if (loadingDialogFragment != null) {
            loadingDialogFragment.hideLoading();
            loadingDialogFragment = null;
        }
    }

    protected ImageView ivAppBackground;

    protected void addAppBackground() {
        if (viewBinding != null) {
            if (viewBinding.getRoot() instanceof ViewGroup) {
                ivAppBackground = new AppCompatImageView(this);
                ivAppBackground.setId(R.id.iv_app_background);
                ivAppBackground.setScaleType(ImageView.ScaleType.FIT_XY);
                ivAppBackground.setBackgroundResource(R.color.window_bg);
                ivAppBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ViewGroup viewGroup = (ViewGroup) viewBinding.getRoot();
                viewGroup.addView(ivAppBackground, 0);
            }
        }
    }

    protected void loadAppBackground() {
        if (ivAppBackground != null && backgroundSrc != null) {
            Object srcTag = backgroundSrc.hashCode();
            Object tag = ivAppBackground.getTag();
            Log.d(TAG, "loadAppBackground: " + srcTag + " - " + tag);
            if (srcTag.equals(tag)) {
                Log.d(TAG, "loadAppBackground: 图片源的hashCode相同，跳过重新加载");
            } else {
                Glide.with(this)
                        .load(backgroundSrc)
                        .dontAnimate()
                        .into(ivAppBackground);
                ivAppBackground.setTag(backgroundSrc.hashCode());
            }
        }
    }

    //APP 背景用全局对象存储
    protected static volatile Object backgroundSrc;

    public static void setBackgroundSrc(Object backgroundSrc) {
        BaseActivity.backgroundSrc = backgroundSrc;
    }

}
