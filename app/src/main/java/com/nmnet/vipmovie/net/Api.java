package com.nmnet.vipmovie.net;

import com.nmnet.parentlib.net.retrofit.Response;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by NMNET on 2017/3/7 0007.
 */

public interface Api {

    @GET("user/regist")
    Observable<Response<String>> test();

}
