package com.whiner.devkit.toaster;

import android.content.Context;
import android.view.Gravity;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.hjq.toast.style.BlackToastStyle;

public class BigBlackToastStyle extends BlackToastStyle {

    @Override
    protected float getTextSize(Context context) {
        return AdaptScreenUtils.pt2Px(90);
    }

    @Override
    public int getGravity() {
        return Gravity.TOP;
    }

    @Override
    public int getYOffset() {
        return AdaptScreenUtils.pt2Px(90);
    }

}
