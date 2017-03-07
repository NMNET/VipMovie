package com.nmnet.vipmovie.app;

import android.app.Application;

import com.nmnet.parentlib.net.retrofit.NetParams;

/**
 * Created by NMNET on 2017/3/7 0007.
 */

public class MovieApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetParams.init("http://192.168.1.7:8080/","200");
    }
}
