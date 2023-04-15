package com.whiner.devkit.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.whiner.devkit.mmkv.NetMMKVUtils;
import com.whiner.devkit.network.base.GetConfig;
import com.whiner.devkit.network.base.RetrofitUtils;
import com.whiner.devkit.network.base.RetryWhen;
import com.whiner.devkit.network.service.GetService;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public enum NetUtils {
    ONE;

    private static final String TAG = "NetUtils";

    private final GetService getService;

    NetUtils() {
        this.getService = RetrofitUtils.INSTANCE.createStringService(GetService.class);
    }

    public <T> void get(@NonNull final GetConfig getConfig, @NonNull OnNetListener<T> onNetListener) {
        boolean returnData = true;
        final TypeToken<T> typeToken = onNetListener.getTypeToken();
        T data;
        switch (getConfig.getCacheType()) {
            case NO_CACHE:
                Log.d(TAG, "get: 不使用缓存");
                break;
            case ONLY_CACHE:
                Log.d(TAG, "get: 有缓存就不请求了");
                data = readData(getConfig.getCacheKey(), typeToken);
                if (data != null) {
                    onNetListener.onStart();
                    onNetListener.onSucceeded(data, true);
                    onNetListener.onEnd();
                    return;
                }
                break;
            case FIRST_CACHE:
                Log.d(TAG, "get: 优先使用缓存，再请求更新缓存");
                data = readData(getConfig.getCacheKey(), typeToken);
                if (data != null) {
                    onNetListener.onStart();
                    onNetListener.onSucceeded(data, true);
                    onNetListener.onEnd();
                    //继续请求，但是不返回数据
                    returnData = false;
                }
                break;
        }
        //开始请求
        final OnNetListener<T> listener = returnData ? onNetListener : null;
        //把订阅者final化
        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + d);
                if (listener != null) {
                    listener.onStart();
                    listener.onDisposable(d);
                } else {
                    onNetListener.onDisposable(d);
                }
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
                //处理结果
                T data = parseStr(typeToken.getType(), s);
                if (data != null) {
                    //保存数据
                    saveData(getConfig, s);
                    //发送数据
                    if (listener != null) {
                        listener.onSucceeded(data, false);
                        listener.onEnd();
                    }
                } else {
                    if (listener != null) {
                        listener.onFailed(new Exception("parse data failed"));
                        listener.onEnd();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
                if (listener != null) {
                    listener.onFailed(new Exception(e));
                    listener.onEnd();
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        //创建发布者

        final Observable<String> observable1 = getService.get(getConfig.getMainUrl())
                .retryWhen(new RetryWhen())
                .onTerminateDetach();
        final Observable<String> observable2 = getService.get(getConfig.getBackUrl())
                .retryWhen(new RetryWhen())
                .onTerminateDetach();
        Observable<Observable<String>> source = Observable.just(observable1);
        source.concatMap(innerObservable -> {
                    // 在 Observable1 出现错误时转换为 Observable2
                    return innerObservable.onErrorResumeNext(observable2);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(listener == null ? Schedulers.io() : AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .doOnDispose(() -> {
                    if (listener != null) {
                        listener.onFailed(new Exception("主动取消了请求"));
                        listener.onEnd();
                    }
                }).subscribe(observer);
    }

    private void saveData(@NonNull final GetConfig getConfig, final String value) {
        if (getConfig.getCacheType() == GetConfig.CacheType.NO_CACHE || value == null) {
            return;
        }
        NetMMKVUtils.ONE.put(getConfig.getCacheKey(), value, getConfig.getCacheTime());
    }

    private <T> T readData(@NonNull final String key, @NonNull TypeToken<T> typeToken) {
        String s = NetMMKVUtils.ONE.get(key, null);
        return parseStr(typeToken.getType(), s);
    }

    @SuppressWarnings("unchecked")
    private <T> T parseStr(@NonNull final Type type, final String s) {
        if (s == null) {
            return null;
        }
        T data = null;
        try {
            if (type == String.class) {
                data = (T) s;
            } else {
                data = GsonUtils.fromJson(s, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
