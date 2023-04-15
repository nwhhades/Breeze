package com.whiner.devkit.network;

import com.google.gson.reflect.TypeToken;

import io.reactivex.disposables.Disposable;

public interface OnNetListener<T> {

    TypeToken<T> getTypeToken();

    void onStart();

    void onDisposable(Disposable d);

    void onSucceeded(T data, boolean isCache);

    void onFailed(Exception e);

    void onEnd();

}
