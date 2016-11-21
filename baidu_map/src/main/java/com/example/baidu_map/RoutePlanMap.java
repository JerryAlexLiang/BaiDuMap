package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

public class RoutePlanMap extends AppCompatActivity implements View.OnClickListener, OnGetRoutePlanResultListener {

    private MapView mRouteMap;
    private BaiduMap mBaiDuMap;
    private EditText mEtStart;
    private EditText mEtEnd;
    private Button mDriverRouteBtn;
    private String mStartPoint;
    private String mEndPoint;

    //路线规划搜索相关
    private RoutePlanSearch mRoutePlanSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_map);
        //初始化控件
        mEtStart = (EditText) findViewById(R.id.start_point_name);
        mEtEnd = (EditText) findViewById(R.id.end_point_name);
        mDriverRouteBtn = (Button) findViewById(R.id.route_driver_btn);
        //初始化mapView
        mRouteMap = (MapView) findViewById(R.id.route_map_view);
        //得到BaiDuMap
        mBaiDuMap = mRouteMap.getMap();
        //设置中心点
        setCenter();
        //实例化路径规划对象mRoutePlanSearch
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        //设置点击监听事件
        mDriverRouteBtn.setOnClickListener(this);
        //监听结果返回接口对象
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);


    }

    /**
     * 设置点击监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        mStartPoint = mEtStart.getText().toString();
        mEndPoint = mEtEnd.getText().toString();

        switch (view.getId()) {
            case R.id.route_driver_btn:
                driver();
                break;
        }

    }

    /**
     * 驾车
     */
    private void driver() {
        PlanNode startNode = PlanNode.withCityNameAndPlaceName("武汉", mStartPoint);//开始节点
        PlanNode endNode = PlanNode.withCityNameAndPlaceName("武汉", mEndPoint);//结束节点
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption()
                .from(startNode)
                .to(endNode);
        mRoutePlanSearch.drivingSearch(drivingRoutePlanOption);

    }

    /**
     * 设置地图中心点
     */
    private void setCenter() {
        LatLng centrolPoint = new LatLng(33.83146, 115.786975);
        MapStatus mapStatus = new MapStatus.Builder().target(centrolPoint).zoom(15).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiDuMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 接口回调事件
     * @param walkingRouteResult
     */

    /**
     * 步行路线
     *
     * @param walkingRouteResult
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    /**
     * 公交路线
     *
     * @param transitRouteResult
     */
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    /**
     * @param massTransitRouteResult
     */
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    /**
     * 自驾
     *
     * @param drivingRouteResult
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();
        for (DrivingRouteLine routeLine : routeLines){
            System.out.println("====> " + routeLine.getDistance() + " " + routeLine.getDuration() + " " + routeLine.getStarting().getTitle()
                    + " " + routeLine.getTerminal().getTitle());
        }



    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    /**
     * 自行车骑行
     *
     * @param bikingRouteResult
     */
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    /**
     * 加载MapView的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (mRouteMap != null) {
            mRouteMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRouteMap != null) {
            mRouteMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRouteMap != null) {
            mRouteMap.onDestroy();
        }
    }

}
