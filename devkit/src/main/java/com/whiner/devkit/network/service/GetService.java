package com.whiner.devkit.network.service;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetService {

    @GET
    Observable<String> get(@Url @NonNull String url);

}
