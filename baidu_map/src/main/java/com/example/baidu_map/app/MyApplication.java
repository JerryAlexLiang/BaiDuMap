package com.example.baidu_map.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * 百度地图：在应用程序创建时初始化 SDK引用的Context 全局变量
 * 注意：在SDK各功能组件使用之前都需要调用
 * SDKInitializer.initialize(getApplicationContext());，因此我们建议该方法放在Application的初始化方法中
 *
 * 注意：一定要在清单文件中加上application的name
 * Created by iflytek on 2016/11/1.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化地图
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

    }
}
