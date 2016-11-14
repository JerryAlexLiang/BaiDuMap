package com.example.baidu_map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class CitySearchMap extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener {

    private MapView mCitySearchMap;
    private BaiduMap BaiDuMap;
    private EditText mCityEt;
    private EditText mFoodEt;
    private Button mSrerachBtn;
    private PoiSearch mPoiSearch;
    private Button mNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search_map);
        //初始化控件
        mCityEt = (EditText) findViewById(R.id.city_search_et);
        mFoodEt = (EditText) findViewById(R.id.restaurant_search_et);
        mSrerachBtn = (Button) findViewById(R.id.search_btn);
        mNextPage = (Button) findViewById(R.id.next_page);
        //初始化mapView
        mCitySearchMap = (MapView) findViewById(R.id.city_search_mapView);
        //获取BaiDuMap
        BaiDuMap = mCitySearchMap.getMap();
        //设置中心点
        setCenter();
        //设置button的点击监听事件
        mSrerachBtn.setOnClickListener(this);
        mNextPage.setOnClickListener(this);
        //添加搜索
        //实例化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //给实例对象添加结果集的接口回调对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);

    }


    /**
     * 设置地图中心点
     */
    private void setCenter() {
        LatLng centralPoint = new LatLng(33.83146, 115.786975);
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(12).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        BaiDuMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * Button的点击监听事件
     *
     * @param view
     */
    private int index = 0;

    @Override
    public void onClick(View view) {
        String cityName = mCityEt.getText().toString();
        String restaurantKey = mFoodEt.getText().toString();
        switch (view.getId()) {
            case R.id.search_btn:
                //开始搜索
                searchInCity(cityName, restaurantKey);
                break;

            case R.id.next_page:
                //下一页
                index++;
                searchInCity(cityName, restaurantKey);
                break;
        }
    }

    /**
     * 开始搜索功能
     *
     * @param cityName
     * @param restaurantKey
     */
    private void searchInCity(String cityName, String restaurantKey) {
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption()
                .city(cityName)
                .keyword(restaurantKey)
                .pageCapacity(5)
                .pageNum(index);
        mPoiSearch.searchInCity(poiCitySearchOption);
    }


    /**
     * 加载mapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mCitySearchMap != null) {
            mCitySearchMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCitySearchMap != null) {
            mCitySearchMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCitySearchMap != null) {
            mCitySearchMap.onDestroy();
        }
    }

    /**
     * 检索结果覆盖物
     */

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
     * 实例对象结果集的接口回调对象
     * 2、第二步：在POI检索回调接口中添加自定义的PoiOverlay；
     *
     * @param result
     */

    @Override
    public void onGetPoiResult(PoiResult result) {
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo p : allPoi) {
            System.out.println("======>" + "餐厅名称： " + p.name + "餐厅地址： " + p.address + "订餐电话： " + p.phoneNum);
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(CitySearchMap.this, "没有相应搜索结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //如果搜索结果没有错误，先清空地图
            BaiDuMap.clear();
            //创建PoiOverlay
            PoiOverlay overlay = new MyPoiOverlay(BaiDuMap);
            //设置overlay可以处理标注点击事件
            BaiDuMap.setOnMarkerClickListener(overlay);
            //设置PoiOverlay数据
            overlay.setData(result);
            //添加PoiOverlay到地图中
            overlay.addToMap();
            //设置地图可以缩放，使搜索结果都可以显示在地图上
            overlay.zoomToSpan();
            return;
        }


    }

    /**
     * 调用searchPoiDetail回调的方法
     * @param poiDetailResult
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(CitySearchMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }else {
            String name = poiDetailResult.getName();
            String address = poiDetailResult.getAddress();
            String telephone = poiDetailResult.getTelephone();
            Toast.makeText(CitySearchMap.this, name + "\n" + address + "\n" + telephone
                    + poiDetailResult.getDetailUrl(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }


}
