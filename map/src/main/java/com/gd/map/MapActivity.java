package com.gd.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.commonlib.base.BaseActivity;
import com.example.main.bean.Address;
import com.example.main.bean.Enterprise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.util.List;

@Route(path="/map/navigation")
public class MapActivity extends BaseActivity {


    private MapView mMapView = null;
    private AMap aMap = null;
    CameraUpdate cameraUpdate;

    @Autowired
    public Address address;

    @Override
    protected int setContentView() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState,String a) {
        ARouter.getInstance().inject(this);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        showMy();
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(address.getName()));


        cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,0,30));
        gotoQy();

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            View infoWindow = null;
            /**
             * 监听自定义infowindow窗口的infowindow事件回调
             */
            @Override
            public View getInfoWindow(Marker marker) {
                if(infoWindow == null) {
                    infoWindow = LayoutInflater.from(MapActivity.this).inflate(
                            R.layout.infowindow_layout, null);
                }
                render(marker, infoWindow);
                return infoWindow;
            }

            /**
             * 监听自定义infowindow窗口的infocontents事件回调
             */
            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

//        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                showToast("导航到XXX企业");
//            }
//        });

        marker.showInfoWindow();

        findViewById(R.id.go_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoQy();
            }
        });

    }

    private void gotoQy(){
        aMap.moveCamera(cameraUpdate);
    }


    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        TextView name = view.findViewById(R.id.name);
        name.setText(marker.getOptions().getTitle());
        Button button =  view.findViewById(R.id.navigation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("导航到" + marker.getOptions().getTitle());
                checkGaodeMap(marker.getOptions().getPosition().latitude,marker.getOptions().getPosition().longitude,marker.getOptions().getTitle());
            }
        });
    }



    /**
     * 显示定位蓝点
     */

    private void showMy(){
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER) ;//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。



//        myLocationStyle.showMyLocation(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //跳转到高德地图
    private void checkGaodeMap(double latitude,double longtitude,String address)
    {
        if (isInstallApk(MapActivity.this, "com.autonavi.minimap")) {// 是否安装了高德地图
            try {
                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=&poiname="+address+"&lat="
                        + latitude
                        + "&lon="
                        + longtitude + "&dev=0");
                startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        }else {// 未安装
            showToast("您尚未安装高德地图");
//            Uri uri = Uri
//                    .parse("market://details?id=com.autonavi.minimap");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }
    }

    /** 判断手机中是否安装指定包名的软件 */
    public static boolean isInstallApk(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}