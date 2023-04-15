package com.whiner.devkit.ui.fragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.whiner.devkit.R;
import com.whiner.devkit.apk.ApkUtils;
import com.whiner.devkit.base.BaseDialogFragment;
import com.whiner.devkit.databinding.FragmentDownloadDialogBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public abstract class DownloadDialogFragment extends BaseDialogFragment<FragmentDownloadDialogBinding> implements View.OnClickListener {

    private static final String TAG = "DownloadDialogFragment";
    protected final String url;
    protected final String fileMD5;
    protected final String filePath;
    protected String title;
    protected String context;
    protected String apkPackageName;

    public DownloadDialogFragment(String url, String fileMD5, String filePath) {
        this.url = url;
        this.fileMD5 = fileMD5;
        this.filePath = filePath;
    }

    @Override
    protected FragmentDownloadDialogBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDownloadDialogBinding.inflate(inflater, container, false);
    }

    @Override
    protected String getBackTip() {
        return "任务执行中，请耐心等待";
    }

    @Override
    protected void initView() {
        viewBinding.tvTitle.setText(title);
        viewBinding.tvContext.setText(context);
        viewBinding.btn1.setOnClickListener(this);
        viewBinding.btn2.setOnClickListener(this);
    }

    public void showDialog(String title, String context, FragmentManager fragmentManager) {
        this.title = title;
        this.context = context;
        showDialog(fragmentManager);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "onDismiss: 窗口已关闭，终止任务");
        stopDownload();
    }

    protected void setProgress(int progress) {
        if (viewBinding != null) {
            viewBinding.progressBar.setProgress(progress);
            String s = getString(R.string.tv_progress, progress);
            viewBinding.tvProgress.setText(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            hideDialog();
        } else {
            startDownload();
            installApk(null);
        }
    }

    protected abstract void startDownload();

    protected abstract void stopDownload();

    protected abstract void downloadStart();

    protected abstract void downloadFailed();

    protected abstract void downloadEnd();

    protected void installApk(final File file) {
        if (viewBinding != null) {
            viewBinding.tvContext.setText("安装中...");
            viewBinding.progressBar.setIndeterminate(true);
            viewBinding.tvProgress.setVisibility(View.INVISIBLE);
            viewBinding.btn1.setEnabled(false);
            viewBinding.btn2.setEnabled(false);
        }
        setCancel(false);
        //启动一个异步的线程，如果1分钟后还是没有收到安装完成的消息就自动关闭
        //startAutoCancel();
        //安装
        apkPackageName = ApkUtils.installApk(getActivity(), file);
    }

    //监听安装结果的回调

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
