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
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

/**
 * 附近搜索
 */
public class NearbySearchMap extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener {

    private EditText mNearbySearchEt;
    private EditText mResultNearbySearchEt;
    private Button mNearbySearchButton;
    private Button mNearbyNextPageButton;
    private MapView mNearbyMap;
    private BaiduMap mBaiDuMap;
    //声明搜索对象
    private PoiSearch mPoiSearch;
    private LatLng centralPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_search_map);
        //初始化控件
        mNearbySearchEt = (EditText) findViewById(R.id.nearby_search_et);//附近搜索名称(key)
        mResultNearbySearchEt = (EditText) findViewById(R.id.restaurant_nearby_search_et); //搜索结果(keyResult)
        mNearbySearchButton = (Button) findViewById(R.id.nearby_search_button);  // 搜索按钮
        mNearbyNextPageButton = (Button) findViewById(R.id.nearby_next_page_button); // 下一页按钮
        //初始化mapView试图
        mNearbyMap = (MapView) findViewById(R.id.nearby_search_mapView);
        //获取百度地图
        mBaiDuMap = mNearbyMap.getMap();
        //设置中心点
        setCenter();
        //设置Button的点击监听事件
        mNearbySearchButton.setOnClickListener(this);
        mNearbyNextPageButton.setOnClickListener(this);
        //实例化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //给实例对象添加结果集的接口回调对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    /**
     * 加载map的生命周期
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mNearbyMap != null) {
            mNearbyMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNearbyMap != null) {
            mNearbyMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNearbyMap != null) {
            mNearbyMap.onDestroy();
        }
    }

    /**
     * 设置中心点
     */
    private void setCenter() {
        centralPoint = new LatLng(33.83146, 115.786975);
        MapStatus mapStatus = new MapStatus.Builder().target(centralPoint).zoom(12).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiDuMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * Button的点击监听事件
     *
     * @param view
     */
    private int index = 0;

    @Override
    public void onClick(View view) {
        String key = mNearbySearchEt.getText().toString();
        String keyResult = mResultNearbySearchEt.getText().toString();

        switch (view.getId()) {
            case R.id.nearby_search_button:
                //开始搜索
                searchInNearby(keyResult);

                break;

            case R.id.nearby_next_page_button:
                index++;
                searchInNearby(keyResult);
                //下一页
                break;
        }


    }

    private void searchInNearby(String keyResult) {
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption()
                .keyword(keyResult)  //搜索关键字
                .pageCapacity(10)
                .pageNum(index)
                .location(centralPoint)  //搜索的中心点
                .radius(500);  //搜索的半径(单位：米)
        mPoiSearch.searchNearby(poiNearbySearchOption);
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
     * @param result
     */
    @Override
    public void onGetPoiResult(PoiResult result) {
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo p : allPoi) {
            System.out.println("======>" + "餐厅名称： " + p.name + "餐厅地址： " + p.address + "订餐电话： " + p.phoneNum);
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(NearbySearchMap.this, "没有相应搜索结果", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NearbySearchMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            String name = poiDetailResult.getName();
            String address = poiDetailResult.getAddress();
            String telephone = poiDetailResult.getTelephone();
            Toast.makeText(NearbySearchMap
                    .this, name + "\n" + address + "\n" + telephone
                    + poiDetailResult.getDetailUrl(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
