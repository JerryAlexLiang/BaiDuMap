package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

public class CentralMap extends AppCompatActivity {

    private MapView mCentralMap;
    private BaiduMap mBaiDuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_map);
        //初始化mapView
        mCentralMap = (MapView) findViewById(R.id.central_map);
        //获取BaiDuMap
        mBaiDuMap = mCentralMap.getMap();
        //设置百度地图的中心点
        setCenter();
        //添加BaiDuMap的地图默认覆盖物点击监听事件
        mBaiDuMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 点击地图上任意一个点的回调方法
             * @param latLng  被点的地图坐标
             */
            @Override
            public void onMapClick(LatLng latLng) {
                int describeContents = latLng.describeContents();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Toast.makeText(CentralMap.this, "坐标信息： " + describeContents + "   " +
                        latitude + "   " + longitude, Toast.LENGTH_SHORT).show();

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                /**
                 * 点击地图上默认的marker的回调方法
                 * @param mapPoi  返回marker的相关信息
                 * @return
                 */
                String name = mapPoi.getName();
                LatLng position = mapPoi.getPosition();
                Toast.makeText(CentralMap.this, "坐标信息：  " +
                        name + "   " + position.latitude + "  " + position.longitude, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    /**
     * 设置百度地图的中心点
     */
    private void setCenter() {
        //设置中心点的坐标
        LatLng centralPoint = new LatLng(33.83146, 115.786975);//纬度，经度（注意：和中文坐标拾取系统格式相反）
        //定义地图的状态
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(18).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图状态
        mBaiDuMap.setMapStatus(mapStatusUpdate);

    }

    /**
     * 加入mapView的生命周期
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mCentralMap != null) {
            mCentralMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCentralMap != null) {
            mCentralMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCentralMap != null) {
            mCentralMap.onDestroy();
        }
    }
}
