package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

/**
 * 百度地图：在应用程序创建时初始化 SDK引用的Context 全局变量
 * 1.初始化地图
 * 2.加入地图的生命周期
 * 注意：在SDK各功能组件使用之前都需要调用
 * SDKInitializer.initialize(getApplicationContext());，因此我们建议该方法放在Application的初始化方法中
 */
public class CommonMap extends AppCompatActivity {

    //声明地图
    private MapView mMapView;
    private BaiduMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //SDKInitializer.initialize(getApplicationContext());   //已在app中初始化
        setContentView(R.layout.activity_common_map);
        //初始化MapView
        mMapView = (MapView) findViewById(R.id.map_view);
    }

    /**
     * 加入地图的生命周期
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
}
