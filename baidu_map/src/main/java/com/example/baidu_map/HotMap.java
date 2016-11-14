package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class HotMap extends AppCompatActivity {

    private MapView mHotMapView;
    private BaiduMap mBaiDuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_map);
        //初始化MapView
        mHotMapView = (MapView) findViewById(R.id.hot_mapView);
        //获取BaiDuMap
        mBaiDuMap = mHotMapView.getMap();
        //开启城市热力图用
        mBaiDuMap.setBaiduHeatMapEnabled(true);

    }

    /**
     * 加载MapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mHotMapView != null) {
            mHotMapView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHotMapView != null) {
            mHotMapView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHotMapView != null) {
            mHotMapView.onDestroy();
        }
    }
}
