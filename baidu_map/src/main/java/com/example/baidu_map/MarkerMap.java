package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MarkerMap extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap BaiDuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_map);
        //初始化mapView
        mMapView = (MapView) findViewById(R.id.marker_map);
        //获取BaiDuMap
        BaiDuMap = mMapView.getMap();
        //定义地图中心点
        setCenter();
        //添加自定义的覆盖物marker
        addMarker();
        //添加BaiDuMap的地图默认覆盖物点击监听事件
        BaiDuMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 点击地图上任意一个点的回调方法
             * @param latLng  被点的地图坐标
             */
            @Override
            public void onMapClick(LatLng latLng) {
                int describeContents = latLng.describeContents();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Toast.makeText(MarkerMap.this, "坐标信息： " + describeContents + "   " +
                        latitude + "   " + longitude, Toast.LENGTH_SHORT).show();

            }

            /**
             * 点击地图上默认的marker的回调方法
             * @param mapPoi  返回marker的相关信息
             * @return
             */
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                String name = mapPoi.getName();
                LatLng position = mapPoi.getPosition();
                Toast.makeText(MarkerMap.this, "坐标信息：  " +
                        name + "   " + position.latitude + "  " + position.longitude, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //添加自定义覆盖物Marker的点击监听事件
        BaiDuMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                String title = marker.getTitle();
                Toast.makeText(MarkerMap.this, "坐标信息：  " + title + " " + "百度地图坐标： " + position, Toast.LENGTH_SHORT).show();
                //为自定义的覆盖物marker添加弹出框
                TextView view = new TextView(MarkerMap.this);
                view.setBackgroundResource(R.drawable.popup);
                view.setText(title);
                /**
                 * 参数1：要弹出的view
                 * 参数2：弹出的位置
                 * 参数3：相对于屏幕上的marker点(圆点)在Y轴方向上的偏移量
                 */
                InfoWindow infoWindow = new InfoWindow(view,position,-100);//"-50":在Y轴方向的偏移量
                //显示InfoWindow
                BaiDuMap.showInfoWindow(infoWindow);
                return false;
            }
        });

        //添加自定义覆盖物marker的拖动事件(针对已经添加在地图上的标注，可以采用如下方式进行手势拖拽)
        //调用 BaiduMap 对象的 setOnMarkerDragListener 方法设置 marker 拖拽的监听
        BaiDuMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            /**
             * 拖拽中
             * @param marker
             */
            @Override
            public void onMarkerDrag(Marker marker) {
                System.out.println("======> 纬度： " + marker.getPosition().latitude +
                        "====>经度： " + marker.getPosition().longitude);
            }

            /**
             * 拖拽结束
             * @param marker
             */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MarkerMap.this, "纬度： " + marker.getPosition().latitude +
                        "经度： " + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            /**
             * 开始拖拽
             * @param marker
             */
            @Override
            public void onMarkerDragStart(Marker marker) {
                System.out.println("======> 纬度： " + marker.getPosition().latitude +
                        "====>经度： " + marker.getPosition().longitude);

            }
        });

    }

    /**
     * 定义地图中心点
     */
    private void setCenter() {
        //设置中心点的坐标
        LatLng centralPoint = new LatLng(33.83146, 115.786975);
        //定义地图的状态
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(17).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图的状态
        BaiDuMap.setMapStatus(mapStatusUpdate);

    }

    /**
     * 添加自定义的覆盖物marker
     */
    private void addMarker() {
        //定义一个自定义的坐标点
        LatLng marker = new LatLng(33.826026, 115.791332);
        //构建marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        //构建MarkerOption,用于在地图上添加marker
        OverlayOptions option = new MarkerOptions()
                .title("My Home : 龙凤家园") //设置标记物的标题
                .icon(bitmap)     //设置标记物的图标
                .position(marker) //设置位置
                .draggable(true);  //设置是否可以拖动
        //把图标放到地图上(在地图上添加Marker，并显示)
        BaiDuMap.addOverlay(option);
    }

    /**
     * 加载mapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
}
