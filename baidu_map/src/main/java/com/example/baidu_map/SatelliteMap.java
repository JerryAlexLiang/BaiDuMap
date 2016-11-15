package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class SatelliteMap extends AppCompatActivity {

    private MapView satelliteMap;
    private BaiduMap mTypeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_map);
        //初始化map
        satelliteMap = (MapView) findViewById(R.id.satellite_map);
        //获取map
        mTypeMap = satelliteMap.getMap();
        //卫星地图
        mTypeMap.setMapType(mTypeMap.MAP_TYPE_SATELLITE);
    }

    /**
     * 加入地图的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (satelliteMap != null){
            satelliteMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (satelliteMap != null){
            satelliteMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (satelliteMap != null){
            satelliteMap.onDestroy();
        }
    }
}
