package com.whiner.devkit.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.hjq.toast.Toaster;
import com.whiner.devkit.tv.OnTvKeyBackListener;

public abstract class BaseDialogFragment<V extends ViewBinding> extends AppCompatDialogFragment {

    private static final String TAG = "BaseDialogFragment";

    protected V viewBinding;

    protected abstract V getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract String getBackTip();

    protected abstract void initView();

    //动态设置是否能取消
    protected volatile boolean cancel = true;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnTvKeyBackListener() {
            @Override
            public boolean onBack(View view, DialogInterface dialog) {
                if (isCancel()) {
                    return false;
                } else {
                    Toaster.show(getBackTip());
                    return true;
                }
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        viewBinding = getViewBinding(inflater, container);
        initView();
        return viewBinding.getRoot();
    }

    protected volatile boolean show = false;

    protected void showDialog(@NonNull FragmentManager fragmentManager) {
        if (show) {
            return;
        }
        show = true;
        show(fragmentManager, TAG + hashCode());
    }

    protected void hideDialog() {
        show = false;
        dismiss();
    }

}
