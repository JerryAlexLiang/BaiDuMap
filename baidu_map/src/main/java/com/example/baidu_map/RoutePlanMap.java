package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.PlaneInfo;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
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

    //声明驾车的覆盖物
    private DrivingRouteOverlay mDrivingRouteOverlay;
    private EditText mRouteCityName;
    private String mCity;

    //所有节点信息
    private List<DrivingRouteLine.DrivingStep> allStep;
    private Button mUP;
    private Button mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_map);
        //初始化控件
        mRouteCityName = (EditText) findViewById(R.id.city_name);
        mEtStart = (EditText) findViewById(R.id.start_point_name);
        mEtEnd = (EditText) findViewById(R.id.end_point_name);
        mDriverRouteBtn = (Button) findViewById(R.id.route_driver_btn);
        mUP = (Button) findViewById(R.id.route_driver_btn_up);//上一节点
        mNext = (Button) findViewById(R.id.route_driver_btn_next);//下一节点
        //初始化mapView
        mRouteMap = (MapView) findViewById(R.id.route_map_view);
        //得到BaiDuMap
        mBaiDuMap = mRouteMap.getMap();
        //设置中心点
        setCenter();
        //实例化路径规划对象mRoutePlanSearch
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        //监听结果返回接口对象
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);

        //创建公交路线规划线路覆盖物   实例化覆盖物
        mDrivingRouteOverlay = new DrivingRouteOverlay(mBaiDuMap);
        //设置点击监听事件
        mDriverRouteBtn.setOnClickListener(this);
        mUP.setOnClickListener(this);
        mNext.setOnClickListener(this);

    }

    /**
     * 设置点击监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        mCity = mRouteCityName.getText().toString();
        mStartPoint = mEtStart.getText().toString();
        mEndPoint = mEtEnd.getText().toString();

        switch (view.getId()) {
            case R.id.route_driver_btn:
                mBaiDuMap.clear();//清空所有Marker
                currentStep = -1;
                driver();

            case R.id.route_driver_btn_up:
                //上一节点
                up();
                break;

            case R.id.route_driver_btn_next:
                //下一节点
                next();
                break;
        }

    }

    private int currentStep = -1;

    /**
     * 下一个节点
     */
    private void next() {
        if (allStep != null) {
            if (currentStep == allStep.size() - 1) {
                Toast.makeText(RoutePlanMap.this, "已到达目标地点", Toast.LENGTH_SHORT).show();
            } else {
                DrivingRouteLine.DrivingStep drivingStep = allStep.get(currentStep + 1);
//                LatLng location = drivingStep.getEntrance().getLocation();
                LatLng location = drivingStep.getExit().getLocation();
                TextView popupTextView = new TextView(this);
                popupTextView.setBackgroundResource(R.drawable.popup);
                popupTextView.setText(drivingStep.getInstructions());
                mBaiDuMap.showInfoWindow(new InfoWindow(popupTextView, location, 0));
                //mDrivingRouteOverlay.zoomToSpan();//自动缩放地图
                //移动节点至中心
                mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));
                currentStep++;
            }

        }

    }

    /**
     * 上一个节点
     */
    private void up() {

    }

    /**
     * 驾车
     */
    private void driver() {

        PlanNode startNode = PlanNode.withCityNameAndPlaceName(mCity, mStartPoint);//开始节点
        PlanNode endNode = PlanNode.withCityNameAndPlaceName(mCity, mEndPoint);//结束节点

//        PlanNode startNode = PlanNode.withLocation(new LatLng(33.83145, 115.786992));//万达广场
//        PlanNode endNode = PlanNode.withLocation(new LatLng(33.894828, 115.779495));//风华中学
        if (mStartPoint.isEmpty() || mEndPoint.isEmpty() ||mCity.isEmpty()){
            Toast.makeText(RoutePlanMap.this, "请输入查询信息", Toast.LENGTH_SHORT).show();
        }else {
            DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption()
                    .from(startNode)
                    .to(endNode);

            mRoutePlanSearch.drivingSearch(drivingRoutePlanOption);// 发起驾车路线规划
        }

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
     * 接口回调事件
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
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //未找到结果
            Toast.makeText(RoutePlanMap.this, "未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            Toast.makeText(RoutePlanMap.this, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            return;
        }
        if (drivingRouteResult != null && drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();
            if (routeLines.size() > 0) {
//                mBaiDuMap.clear();//清空所有Marker
                //设置公交路线规划数据
                mDrivingRouteOverlay.setData(routeLines.get(0));
                //将公交路线规划覆盖物添加到地图中
                mDrivingRouteOverlay.addToMap();
                mDrivingRouteOverlay.zoomToSpan();//自动缩放地图

                //测试：万达广场---亳州一中
                DrivingRouteLine line = routeLines.get(0);
                //返回路径上所有的折点
                allStep = line.getAllStep();
                if (allStep != null) {
                    for (DrivingRouteLine.DrivingStep drivingStep : allStep) {
                        System.out.println("====>  " + drivingStep.getInstructions());
                    }
                }


            }
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
