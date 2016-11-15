package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class TrafficMap extends AppCompatActivity {

    private MapView mTrafficMap;
    private BaiduMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_map);
        //初始化map
        mTrafficMap = (MapView) findViewById(R.id.MapTraffic);
        //获取map
        map = mTrafficMap.getMap();
        //实时交通图
        map.setTrafficEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTrafficMap != null){
            mTrafficMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTrafficMap != null){
            mTrafficMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTrafficMap != null){
            mTrafficMap.onDestroy();
        }
    }
}
