package com.gd.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.example.commonlib.base.BaseActivity;
import com.example.main.bean.Address;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Polygon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Route(path = "/map/navigation")
public class MapActivity extends BaseActivity {


    private MapView mMapView = null;
    private AMap aMap = null;
    CameraUpdate cameraUpdate;

    @Autowired
    public Address address;
    @Autowired
    public String polygon;

    @Override
    protected int setContentView() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, String a) {

        ARouter.getInstance().inject(this);
        addBack();
        setTitleText(address.getName());

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        showMy();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(address.getName()));


        cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16, 0, 30));
        gotoQy();

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            View infoWindow = null;

            /**
             * 监听自定义infowindow窗口的infowindow事件回调
             */
            @Override
            public View getInfoWindow(Marker marker) {
                if (infoWindow == null) {
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

        findViewById(R.id.my_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });

        if (!TextUtils.isEmpty(polygon)){
            showGrid();
        }

        setLocation();//设置定位参数

    }

    private void gotoQy() {
        aMap.moveCamera(cameraUpdate);
    }


    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        TextView name = view.findViewById(R.id.name);
        name.setText(marker.getOptions().getTitle());
        TextView button = view.findViewById(R.id.navigation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("导航到" + marker.getOptions().getTitle());
                checkGaodeMap(marker.getOptions().getPosition().latitude, marker.getOptions().getPosition().longitude, marker.getOptions().getTitle());
            }
        });
    }


    /**
     * 显示定位蓝点
     */

    private void showMy() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

//        mAMapLocationManager.requestLocationUpdates( LocationProviderProxy.AMapNetwork 5000 50 this);


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
    private void checkGaodeMap(double latitude, double longtitude, String address) {
        if (isInstallApk(MapActivity.this, "com.autonavi.minimap")) {// 是否安装了高德地图
            try {
                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=&poiname=" + address + "&lat="
                        + latitude
                        + "&lon="
                        + longtitude + "&dev=0");
                startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {// 未安装
            showToast("您尚未安装高德地图");
//            Uri uri = Uri
//                    .parse("market://details?id=com.autonavi.minimap");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }
    }

    /**
     * 判断手机中是否安装指定包名的软件
     */
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

    public void showGrid() {
        Polygon polygon = getPolygon();
        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加 多边形的每个顶点（顺序添加）
        for (Polygon.Point point : polygon.getPath()) {
            polygonOptions.add(new LatLng(point.getLat(), point.getLng()));
        }
        polygonOptions.strokeWidth(15) // 多边形的边框
                .zIndex(polygon.getzIndex())
                .strokeColor(Color.parseColor(polygon.getStrokeColor())) // 边框颜色
                .fillColor(Color.parseColor(polygon.getFillColor()));   // 多边形的填充色

        aMap.addPolygon(polygonOptions);
    }

    private Polygon getPolygon(){
        try {
            JSONObject json = new JSONObject(polygon);
            Polygon polygon = new Gson().fromJson(json.getString("options"),new TypeToken<Polygon>(){}.getType());
            return polygon;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    //取出经纬度
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
//    private boolean isLocation = false;

    private void setLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);


        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void getPermission(){
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
//                            if (isLocation) {
//                                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//                                isLocation = false;
//                            } else {
                                //启动定位
                                mLocationClient.startLocation();
//                                isLocation = true;
//                            }
                        } else {
                            Toast.makeText(MapActivity.this, "您拒绝了权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}