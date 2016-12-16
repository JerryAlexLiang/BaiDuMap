package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 百度定位功能
 */
public class LocationMap extends AppCompatActivity {

    //声明定位客户端
    private LocationClient locationClient;
    //创建一个接口回调类
    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String country = bdLocation.getCountry();
            String city = bdLocation.getCity();
            Address address = bdLocation.getAddress();
            mCountry.setText(country);
            mCity.setText(city);
            mAddress.setText(String.valueOf(address));

        }
    };
    private TextView mCountry;
    private TextView mCity;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        //第一步，实例化定位客户端
        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(locationListener);
        //第二步，配置定位SDK参数
        initLocation();
        //第三步，开始定位
        locationClient.start();
        //初始化视图
        initView();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        mCountry = (TextView) findViewById(R.id.country_tv);
        mCity = (TextView) findViewById(R.id.city_tv);
        mAddress = (TextView) findViewById(R.id.address_tv);
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption locationClientOption = new LocationClientOption();
        //设置坐标类型
        locationClientOption.setCoorType("bd09ll");//设置返回的定位结果坐标系
        locationClientOption.setScanSpan(1000);//定位精度，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        locationClientOption.setIsNeedAddress(true);//设置是否需要地址信息
        //把当前设置信息添加到客户端
        locationClient.setLocOption(locationClientOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位
        locationClient.stop();
        locationClient = null;
    }
}
