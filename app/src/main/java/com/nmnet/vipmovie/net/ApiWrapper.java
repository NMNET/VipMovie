package com.nmnet.vipmovie.net;

import android.util.Log;
import android.widget.Toast;

import com.nmnet.parentlib.net.retrofit.Response;
import com.nmnet.parentlib.net.retrofit.RetrofitUtils;
import com.nmnet.parentlib.net.retrofit.WrapRequest;

import rx.Observable;


/**
 * Created by NMNET on 2017/3/7 0007.
 */

public class ApiWrapper {
    Api mApis = RetrofitUtils.getApis(Api.class);

    public void test() {
        Observable<Response<String>> test = mApis.test();
        RetrofitUtils.getResponse(test, new WrapRequest<String>() {
            @Override
            public void onNext(String s) {
                Log.d("onNext", "onNext: " + s);
            }
        });
    }

}
