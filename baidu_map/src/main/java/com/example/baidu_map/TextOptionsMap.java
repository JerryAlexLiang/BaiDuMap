package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

public class TextOptionsMap extends AppCompatActivity {

    private MapView mTextOptionsMap;
    private BaiduMap BaiDuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_options_map);
        //初始化视图
        mTextOptionsMap = (MapView) findViewById(R.id.text_options_mapView);
        //获取BaiDuMap
        BaiDuMap = mTextOptionsMap.getMap();
        //定义地图的中心
        setCenter();
        //添加自定义的覆盖物marker
        addMarker();
//        //添加自定义覆盖物Marker的点击监听事件(文字覆盖物)
//        BaiDuMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//
//                return false;
//
//
//            }
//        });
    }

    /**
     * 添加自定义的覆盖物marker
     */
    private void addMarker() {
        //自定义一个坐标点(定义文字所显示的坐标点)
        LatLng testPosition = new LatLng(33.826026, 115.791332);
        //添加marker图标
        //构建MarkerOption,用于在地图上添加marker
        OverlayOptions textOptions = new TextOptions()
                .text("我的秘密花园")  //显示的文字
                .position(testPosition)   //文字的位置
                .bgColor(0xAAFFFF00)   //文字背景
                .fontColor(0xFFFF00FF)  //文字颜色
                .fontSize(36)  //文字大小
                .rotate(-60);  //旋转角度
        //在地图上添加改文字对象并显示
        BaiDuMap.addOverlay(textOptions);
    }

    /**
     * 设置地图的中心点
     */
    private void setCenter() {
        //设置中心点坐标
        LatLng centralPoint = new LatLng(33.83146, 115.786975);
        //定义地图的状态
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(15).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图的状态
        BaiDuMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 加载MapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mTextOptionsMap != null) {
            mTextOptionsMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextOptionsMap != null) {
            mTextOptionsMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTextOptionsMap != null) {
            mTextOptionsMap.onDestroy();
        }
    }
}

