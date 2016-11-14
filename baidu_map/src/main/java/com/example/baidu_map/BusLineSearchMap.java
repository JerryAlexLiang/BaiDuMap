package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
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
    private Button mNextPageButton;
    private MapView mBusLineMap;
    private BaiduMap mBaiDuMap;
    //声明搜索对象
    private PoiSearch mPoiSearch;
    //声明公交搜索对象
    private BusLineSearch mBusLineSearch;
    //定义一个结果集存放公交路线的结果
    private List<PoiInfo> busLineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line_search_map);
        //初始化控件
        mBusLineCity = (EditText) findViewById(R.id.busLine_city_et);
        mBusLineNumber = (EditText) findViewById(R.id.busLine_num_et);
        mBusLineSearchButton = (Button) findViewById(R.id.busLine_search_button);
        mNextPageButton = (Button) findViewById(R.id.busLine_next_page);
        //初始化mapView
        mBusLineMap = (MapView) findViewById(R.id.BusLine_search_mapView);
        //得到BaiDuMap
        mBaiDuMap = mBusLineMap.getMap();
        //设置中心点
        setCenter();
        //设置Button的点击监听事件
        mBusLineSearchButton.setOnClickListener(this);
        mNextPageButton.setOnClickListener(this);
        //实例化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //给实例对象添加结果集的接口回调对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        //实例化公交搜索对象
        mBusLineSearch = BusLineSearch.newInstance();
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
        MapStatus mapStatus = new MapStatus.Builder().target(centrolPoint).zoom(12).build();
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
        String cityName = mBusLineCity.getText().toString();
        String busLineNum = mBusLineNumber.getText().toString();
        switch (view.getId()) {
            case R.id.busLine_search_button:
                //开始搜索
                searchInCity(cityName,busLineNum);
                break;

            case R.id.busLine_next_page:
                //下一页
                index ++;
                searchInCity(cityName,busLineNum);
                break;
        }
    }

    /**
     * 开始搜索功能
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
     * 公交搜索对象的监听事件 ---> 公交搜索结束之后回调的方法
     * @param busLineResult
     */
    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {

    }


    /**
     * 1、第一步：构造自定义的PoiOverlay 类；
     */
    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            //查询详情 ---->在onGetPoiDetailResult中进行方法回调
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
            return true;
        }
    }

    /**
     * 给实例对象添加结果集的接口回调对象
     *
     * @param result
     */

    @Override
    public void onGetPoiResult(PoiResult result) {

//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            //如果搜索结果没有错误，先清空地图
//            mBaiDuMap.clear();
//            //创建PoiOverlay
//            PoiOverlay overlay = new MyPoiOverlay(mBaiDuMap);
//            //设置overlay可以处理标注点击事件
//            mBaiDuMap.setOnMarkerClickListener(overlay);
//            //设置PoiOverlay数据
//            overlay.setData(result);
//            //添加PoiOverlay到地图中
//            overlay.addToMap();
//            //设置地图可以缩放，使搜索结果都可以显示在地图上
//            overlay.zoomToSpan();
//            return;
//        }


        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BusLineSearchMap.this, "没有相应搜索结果", Toast.LENGTH_SHORT).show();
            return;
        }

        //查询出所有的公交路线信息，并放入busLineList中
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo poiInfo : allPoi){
            //判断搜索结果是否是公交路线
            if (poiInfo.type == PoiInfo.POITYPE.BUS_LINE || poiInfo.type == PoiInfo.POITYPE.SUBWAY_LINE){
                //添加公交路线
                busLineList.add(poiInfo);
                System.out.println("=======-> " + poiInfo.name + "  " + poiInfo.address);
            }
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }else {
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
