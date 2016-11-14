package com.example.baidu_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class EmptyMap extends AppCompatActivity {

    private MapView emptyMap;
    private BaiduMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_map);
        //初始化map
        emptyMap = (MapView) findViewById(R.id.empty_map);
        //获取map
        mMap = emptyMap.getMap();
        //空白地图，基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
        mMap.setMapType(mMap.MAP_TYPE_NONE);
    }

    /**
     * 加入地图的生命周期
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (emptyMap != null){
            emptyMap.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (emptyMap != null){
            emptyMap.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emptyMap != null){
            emptyMap.onDestroy();
        }
    }
}
