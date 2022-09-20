package com.flashlight.demo;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.flashlight.demo.BuildConfig;
//import com.google.android.gms.corebase.lg;
//import com.google.android.gms.corebase.logger;

import okhttp3.OkHttpClient;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;

    public static BaseApplication getInstance(){
        if (null == instance){
            instance = new BaseApplication();
        }
        return instance;
    }

    public static boolean isAppRunning;

    public static boolean isIsAppRunning() {
        return isAppRunning;
    }

    public static void setIsAppRunning(boolean isAppRunning) {
        isAppRunning = isAppRunning;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        MultiDex.install(this);

        setupLifecycleListener1();
        initAndroidNetworking();
//        logger.e(this);

        new Thread(() -> {
//            Fabric.with(getApplicationContext(), new Crashlytics());
//            FirebaseApp.initializeApp(getApplicationContext());
        }).start();

    }

    private void initAndroidNetworking() {
        //# Adding an Network Interceptor for Debugging purpose :
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
    }

    public void setupLifecycleListener1() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            void onMoveToForeground() {
//                lg.logs("HomeApplication", "onMoveToForeground: ");
                isAppRunning = true;
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            void onMoveToBackground() {
//                lg.logs("HomeApplication", "onMoveToBackground: ");
                isAppRunning = false;
//                InterstitialAdUtils.getInstance().lastTimeShownAd = 0;
            }
        });
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}