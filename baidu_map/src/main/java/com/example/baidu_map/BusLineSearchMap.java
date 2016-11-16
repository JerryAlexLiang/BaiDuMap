package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

public class BusLineSearchMap extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener {

    private EditText mBusLineCity;
    private EditText mBusLineNumber;
    private Button mBusLineSearchButton;
    private Button mPreviousButton;
    private MapView mBusLineMap;
    private BaiduMap mBaiDuMap;
    //声明搜索对象
    private PoiSearch mPoiSearch;
    //声明公交搜索对象
    private BusLineSearch mBusLineSearch;
    //定义一个结果集存放公交路线的结果
    private List<PoiInfo> busLineList;
    private String cityName;
    private String busLineNum;
    //声明公交站的覆盖物
    private BusLineOverlay busLineOverlay;
    //当前显示的第一条公交线路
    private int currentBusLine = 0;
    private Button mNextButton;
    private BusLineResult busLineResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line_search_map);
        //初始化控件
        mBusLineCity = (EditText) findViewById(R.id.busLine_city_et);
        mBusLineNumber = (EditText) findViewById(R.id.busLine_num_et);
        mBusLineSearchButton = (Button) findViewById(R.id.busLine_search_button);
        mPreviousButton = (Button) findViewById(R.id.busLine_next_page);//上一站
        mNextButton = (Button) findViewById(R.id.busLine_next_page_two);//下一站
        //初始化mapView
        mBusLineMap = (MapView) findViewById(R.id.BusLine_search_mapView);
        //得到BaiDuMap
        mBaiDuMap = mBusLineMap.getMap();
        //设置中心点
        setCenter();
        //设置Button的点击监听事件
        mBusLineSearchButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        //实例化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //给实例对象添加结果集的接口回调对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        //实例化公交搜索对象
        mBusLineSearch = BusLineSearch.newInstance();
        //实例化公交站覆盖物busLineOverlay
        busLineOverlay = new BusLineOverlay(mBaiDuMap);
        //监听公交搜索回调方法
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        //初始化busLineList
        busLineList = new ArrayList<PoiInfo>();

    }


    /**
     * 设置地图中心点
     */
    private void setCenter() {
        LatLng centrolPoint = new LatLng(33.83146, 115.786975);
        MapStatus mapStatus = new MapStatus.Builder().target(centrolPoint).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiDuMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 设置Button的点击监听事件
     *
     * @param view
     */
    private int index = 0;

    @Override
    public void onClick(View view) {
        //获取EditText
        cityName = mBusLineCity.getText().toString();
        busLineNum = mBusLineNumber.getText().toString();
        switch (view.getId()) {
            case R.id.busLine_search_button:
                //开始搜索
                searchInCity(cityName, busLineNum);
                currentStation = 0;
                break;

            case R.id.busLine_next_page:
                //上一站
                upStation();
                break;

            case R.id.busLine_next_page_two:
                //下一站
                nextStation();
                break;
        }
    }

    /**
     * 上一站
     */
    private int currentStation = 0;

    private void upStation() {
        int size = busLineResult.getStations().size();
//        if (currentStation == size) {
//            Toast.makeText(BusLineSearchMap.this, "已到达终点站", Toast.LENGTH_SHORT).show();
//        }
        if (currentStation == 0) {
            Toast.makeText(BusLineSearchMap.this, "始发站", Toast.LENGTH_SHORT).show();
            currentStation = size;
//            return;
        } else {
            currentStation--;
            BusLineResult.BusStation busStation = busLineResult.getStations().get(currentStation);
            //移动到指定索引的坐标
            mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLng(busStation.getLocation()));
            //设置弹出框
            TextView popupText = new TextView(this);
            popupText.setBackgroundResource(R.drawable.popup);
            //弹出泡泡
            popupText.setText(busStation.getTitle());
            mBaiDuMap.showInfoWindow(new InfoWindow(popupText, busStation.getLocation(), 0));


        }

    }


    /**
     * 下一站
     */
    private void nextStation() {
        int size = busLineResult.getStations().size();
        if (currentStation == 0) {
            Toast.makeText(BusLineSearchMap.this, "始发站", Toast.LENGTH_SHORT).show();

        }
        if (currentStation == size-1) {
            Toast.makeText(BusLineSearchMap.this, "已到达终点站", Toast.LENGTH_SHORT).show();
            currentStation = 0;
            //return;
        } else {
            currentStation++;
            BusLineResult.BusStation busStation = busLineResult.getStations().get(currentStation);
            //移动到指定索引的坐标
            mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLng(busStation.getLocation()));
            //设置弹出框
            TextView popupText = new TextView(this);
            popupText.setBackgroundResource(R.drawable.popup);
            //弹出泡泡
            popupText.setText(busStation.getTitle());
            mBaiDuMap.showInfoWindow(new InfoWindow(popupText, busStation.getLocation(), 0));


        }

    }

    /**
     * 开始搜索功能
     *
     * @param cityName
     * @param busLineNum
     */
    private void searchInCity(String cityName, String busLineNum) {
        //先找出所有的结果集，再从这些结果集中找到公交信息的结果  再用公交信息的结果去查询公交信息详情
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption()
                .city(cityName)
                .keyword(busLineNum)
                .pageCapacity(5)
                .pageNum(index);
        mPoiSearch.searchInCity(poiCitySearchOption);
    }

    /**
     * 获取公交路线图(站点)
     */
    private void getBusLine() {
        if (currentBusLine > busLineList.size()) {
            currentBusLine = 0;
        } else {
            BusLineSearchOption busLineSearchOption = new BusLineSearchOption()
                    .city(cityName)
                    .uid(busLineList.get(currentBusLine).uid);
            mBusLineSearch.searchBusLine(busLineSearchOption);
            currentBusLine++;
        }

    }

    /**
     * 公交搜索对象的监听事件 ---> 公交搜索结束之后回调的方法(站点)
     *
     * @param busLineResult
     */
    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {
        this.busLineResult = busLineResult;
        List<BusLineResult.BusStation> stations = busLineResult.getStations();
        for (BusLineResult.BusStation busStation : stations) {
            System.out.println("=======-> " + busStation.getTitle() + " " + busStation.getLocation());
            //在地图上展示公交站线路
            busLineOverlay.removeFromMap();
            busLineOverlay.setData(busLineResult);
            busLineOverlay.addToMap();
            busLineOverlay.zoomToSpan();
        }


    }


    /**
     * 给实例对象添加结果集的接口回调对象
     *
     * @param result
     */

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BusLineSearchMap.this, "没有相应搜索结果", Toast.LENGTH_SHORT).show();
            return;
        }

        //查询出所有的公交路线信息，并放入busLineList中
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo poiInfo : allPoi) {
            //判断搜索结果是否是公交路线
            if (poiInfo.type == PoiInfo.POITYPE.BUS_LINE || poiInfo.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                //添加公交路线
                busLineList.add(poiInfo);
                System.out.println("=======-> " + poiInfo.name + "  " + poiInfo.address);
            }
        }
        //获取公交路线图(站点)
        getBusLine();

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            String name = poiDetailResult.getName();
            String address = poiDetailResult.getAddress();
            String telephone = poiDetailResult.getTelephone();
            Toast.makeText(BusLineSearchMap.this, name + "\n" + address + "\n" + telephone
                    + poiDetailResult.getDetailUrl(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 加载地图的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mBusLineMap != null) {
            mBusLineMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBusLineMap != null) {
            mBusLineMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBusLineMap != null) {
            mBusLineMap.onDestroy();
        }
    }
}
