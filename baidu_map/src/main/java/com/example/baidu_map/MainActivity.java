package com.example.baidu_map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 百度地图：在应用程序创建时初始化 SDK引用的Context 全局变量
 * 1.初始化地图
 * 2.加入地图的生命周期
 * 注意：在SDK各功能组件使用之前都需要调用
 * SDKInitializer.initialize(getApplicationContext());，因此我们建议该方法放在Application的初始化方法中
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button commonMap;
    private Button satelliteMap;
    private Button emptyMap;
    private Button trafficMap;
    private Button centralMap;
    private Button markerMap;
    private Button hotMapBtn;
    private Button mTextOptionsBtn;
    private Button mCitySearchBtn;
    private Button mRangeSearchBtn;
    private Button mNearbySearchBtn;
    private Button mBusLineSearchBtn;
    private Button mRoutePlanBtn;
    private Button mLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        commonMap = (Button) findViewById(R.id.common_map);
        satelliteMap = (Button) findViewById(R.id.satellite_map);
        emptyMap = (Button) findViewById(R.id.empty_map);
        trafficMap = (Button) findViewById(R.id.traffic_map);
        hotMapBtn = (Button) findViewById(R.id.hot_map_btn);
        centralMap = (Button) findViewById(R.id.central_map_btn);
        markerMap = (Button) findViewById(R.id.marker_map_btn);
        mTextOptionsBtn = (Button) findViewById(R.id.text_options);
        mCitySearchBtn = (Button) findViewById(R.id.city_search_btn);
        mRangeSearchBtn = (Button) findViewById(R.id.range_search_btn);
        mNearbySearchBtn = (Button) findViewById(R.id.nearby_search_btn);
        mBusLineSearchBtn = (Button) findViewById(R.id.busLine_search_btn);
        mRoutePlanBtn = (Button) findViewById(R.id.route_plan_btn);
        mLocationBtn = (Button) findViewById(R.id.location_btn);
        //设置点击监听事件
        commonMap.setOnClickListener(this);
        satelliteMap.setOnClickListener(this);
        emptyMap.setOnClickListener(this);
        trafficMap.setOnClickListener(this);
        hotMapBtn.setOnClickListener(this);
        centralMap.setOnClickListener(this);
        markerMap.setOnClickListener(this);
        mTextOptionsBtn.setOnClickListener(this);
        mCitySearchBtn.setOnClickListener(this);
        mRangeSearchBtn.setOnClickListener(this);
        mNearbySearchBtn.setOnClickListener(this);
        mBusLineSearchBtn.setOnClickListener(this);
        mRoutePlanBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
    }


    /**
     * 设置点击监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.common_map:
//                Intent intent = new Intent(MainActivity.this, CommonMap.class);
//                startActivity(intent);
                intent.setClass(MainActivity.this,CommonMap.class);
                break;

            case R.id.satellite_map:
//                Intent intent1 = new Intent(MainActivity.this, SatelliteMap.class);
//                startActivity(intent1);
                intent.setClass(MainActivity.this,SatelliteMap.class);
                break;

            case R.id.empty_map:
//                Intent intent2 = new Intent(MainActivity.this, EmptyMap.class);
//                startActivity(intent2);
                intent.setClass(MainActivity.this,EmptyMap.class);
                break;

            case R.id.traffic_map:
//                Intent intent3 = new Intent(MainActivity.this, TrafficMap.class);
//                startActivity(intent3);
                intent.setClass(MainActivity.this,TrafficMap.class);
                break;

            case R.id.hot_map_btn:
//                Intent intent4 = new Intent(MainActivity.this,HotMap.class);
//                startActivity(intent4);
                intent.setClass(MainActivity.this,HotMap.class);
                break;

            case R.id.central_map_btn:
//                Intent intent5 = new Intent(MainActivity.this,CentralMap.class);
//                startActivity(intent5);
                intent.setClass(MainActivity.this,CentralMap.class);
                break;

            case R.id.marker_map_btn:
//                Intent intent6 = new Intent(MainActivity.this,MarkerMap.class);
//                startActivity(intent6);
                intent.setClass(MainActivity.this,MarkerMap.class);
                break;

            case R.id.text_options:
//                Intent intent7 = new Intent(MainActivity.this,TextOptionsMap.class);
//                startActivity(intent7);
                intent.setClass(MainActivity.this,TextOptionsMap.class);
                break;

            case R.id.city_search_btn:
//                Intent intent8 = new Intent(MainActivity.this,CitySearchMap.class);
//                startActivity(intent8);
                intent.setClass(MainActivity.this,CitySearchMap.class);
                break;

            case R.id.range_search_btn:
//                Intent intent9 = new Intent(MainActivity.this,RangeSearchMap.class);
//                startActivity(intent9);
                intent.setClass(MainActivity.this,RangeSearchMap.class);
                break;

            case R.id.nearby_search_btn:
//                Intent intent10 = new Intent(MainActivity.this,NearbySearchMap.class);
//                startActivity(intent10);
                intent.setClass(MainActivity.this,NearbySearchMap.class);
                break;

            case R.id.busLine_search_btn:
//                Intent intent11 = new Intent(MainActivity.this,BusLineSearchMap.class);
//                startActivity(intent11);
                intent.setClass(MainActivity.this,BusLineSearchMap.class);
                break;

            case R.id.route_plan_btn:
//                Intent intent12 = new Intent(MainActivity.this,RoutePlanMap.class);
//                startActivity(intent12);
                intent.setClass(MainActivity.this,RoutePlanMap.class);
                break;

            case R.id.location_btn:
                intent.setClass(MainActivity.this,LocationMap.class);
                break;
        }
        startActivity(intent);
    }
}
