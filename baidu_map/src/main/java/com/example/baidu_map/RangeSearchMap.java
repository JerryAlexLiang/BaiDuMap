package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

/**
 * 范围搜索
 */
public class RangeSearchMap extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener {

    private EditText mRangeEt;
    private EditText mRangeRestaurantEt;
    private Button mRangeSearchBtn;
    private Button mRangeNextBtn;
    private MapView mRangeMap;
    private BaiduMap mBaiDuMap;
    //声明搜索对象
    private PoiSearch mPoiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_search_map);
        //初始化视图
        mRangeEt = (EditText) findViewById(R.id.range_search_et);//城市搜索名称
        mRangeRestaurantEt = (EditText) findViewById(R.id.restaurant_range_search_et);//搜索内容
        mRangeSearchBtn = (Button) findViewById(R.id.range_search_button);  //开始搜索
        mRangeNextBtn = (Button) findViewById(R.id.range_next_page);//下一页
        //初始化mapView
        mRangeMap = (MapView) findViewById(R.id.range_search_mapView);
        //获取BaiDuMap
        mBaiDuMap = mRangeMap.getMap();
        //设置中心点
        setCenter();
        //设置Button的点击监听事件
        mRangeSearchBtn.setOnClickListener(this);
        mRangeNextBtn.setOnClickListener(this);
        //添加搜索
        //实例化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //给实例对象添加结果集的接口回调对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    /**
     * 设置中心点
     */
    private void setCenter() {
        LatLng centralPoint = new LatLng(33.83146, 115.786975);
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(12).build();
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
        String range = mRangeEt.getText().toString();
        String restaurant = mRangeRestaurantEt.getText().toString();
        switch (view.getId()) {
            case R.id.range_search_button:
                //开始搜索
                searchInRange(range, restaurant);
                break;


            case R.id.range_next_page:
                //下一页
                index++;
                searchInRange(range, restaurant);
                break;
        }

    }

    /**
     * 开始范围搜索功能
     *
     * @param range
     * @param restaurant
     */
    private void searchInRange(String range, String restaurant) {
        LatLng pointOne = new LatLng(33.863006, 115.816525);
        LatLng pointTwo = new LatLng(33.786709, 115.736037);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(pointOne)   //北部坐标
                .include(pointTwo)   //南部坐标
                .build();
        PoiBoundSearchOption poiBoundSearchOption = new PoiBoundSearchOption()
                .keyword(restaurant)
                .pageCapacity(10)
                .pageNum(index)
                .bound(bounds);
        mPoiSearch.searchInBound(poiBoundSearchOption);
    }

    /**
     * 加载MapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mRangeMap != null) {
            mRangeMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRangeMap != null) {
            mRangeMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRangeMap != null) {
            mRangeMap.onDestroy();
        }
    }


    /**
     * 检索结果覆盖物
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
     * @param poiResult
     */
    @Override
    public void onGetPoiResult(PoiResult result) {
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo p : allPoi) {
            System.out.println("======>" + "餐厅名称： " + p.name + "餐厅地址： " + p.address + "订餐电话： " + p.phoneNum);
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(RangeSearchMap.this, "没有相应搜索结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //如果搜索结果没有错误，先清空地图
            mBaiDuMap.clear();
            //创建PoiOverlay
            PoiOverlay overlay = new MyPoiOverlay(mBaiDuMap);
            //设置overlay可以处理标注点击事件
            mBaiDuMap.setOnMarkerClickListener(overlay);
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
     *
     * @param poiDetailResult
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RangeSearchMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            String name = poiDetailResult.getName();
            String address = poiDetailResult.getAddress();
            String telephone = poiDetailResult.getTelephone();
            Toast.makeText(RangeSearchMap
                    .this, name + "\n" + address + "\n" + telephone
                    + poiDetailResult.getDetailUrl(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

}

