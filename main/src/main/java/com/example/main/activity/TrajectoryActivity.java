package com.example.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/16 13:46.
 * describe: 轨迹
 */
public class TrajectoryActivity extends BaseActivity {
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.map)
    MapView map;
    @BindView(R2.id.is_open)
    Switch isOpen;

    @Override
    protected int setContentView() {
        return R.layout.activity_trajectory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {


        isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showToast("开启位置上报");
                } else {
                    showToast("关闭位置上报");
                }
            }
        });
    }


    @OnClick({R2.id.time})
    public void onViewClicked(View view) {
        int id = view.getId();

        if (id == R.id.time) {

        }
    }

//
//    private void clearTracksOnMap() {
//        for (Polyline polyline : polylines) {
//            polyline.remove();
//        }
//        for (Marker marker : endMarkers) {
//            marker.remove();
//        }
//        endMarkers.clear();
//        polylines.clear();
//    }
}
