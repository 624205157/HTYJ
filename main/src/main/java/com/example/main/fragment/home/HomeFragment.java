package com.example.main.fragment.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @BindView(R2.id.event_num)
    TextView eventNum;
    @BindView(R2.id.task_num)
    TextView taskNum;
    @BindView(R2.id.map)
    MapView mMapView;

    private AMap aMap = null;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        showMy();
    }

    @OnClick({R2.id.event, R2.id.task, R2.id.enterprise, R2.id.resources, R2.id.plan, R2.id.trajectory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.event:
                break;
            case R.id.task:
                break;
            case R.id.enterprise:
                break;
            case R.id.resources:
                break;
            case R.id.plan:
                break;
            case R.id.trajectory:
                break;
        }
    }

    private void showMy() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        myLocationStyle.strokeColor(0x00000000);//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(0x00000000);//设置定位蓝点精度圆圈的填充颜色的方法。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(18);

        aMap.moveCamera(mCameraUpdate);


//        myLocationStyle.showMyLocation(true);
    }


}
