package com.example.main.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.TrackPoint;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.Constants;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.utils.SimpleOnTrackListener;
import com.example.main.utils.TraceRePlay;
import com.example.main.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈泽宇 on 2020/8/28
 * Describe: 查询他人轨迹
 */
public class CheckOtherTraActivity extends BaseActivity {


    @BindView(R2.id.check_other)
    TextView checkOther;
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.map)
    TextureMapView mMapView;

    private final static int AMAP_LOADED = 2;


    private AMap mAMap;
    private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;
    private Marker mGraspStartMarker, mGraspEndMarker, mGraspRoleMarker;
    private Polyline polylines, mGraspPolyline;

    private int mRecordItemId;
    private List<LatLng> mOriginLatLngList = new ArrayList<>();
    private List<LatLng> mGraspLatLngList = new ArrayList<>();
    private ExecutorService mThreadPool;
    private TraceRePlay mRePlay;

    private AMapTrackClient aMapTrackClient;

    @Override
    protected int setContentView() {
        return R.layout.activity_other_trajectory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        initMap();
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
    }

    @OnClick({R2.id.check_other, R2.id.time})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.check_other) {

        } else if (id == R.id.time) {
            Date date = new Date();
            queryHistoryTrack(date);
        }
    }


    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
    }


    private void startMove() {
        if (mRePlay != null) {
            mRePlay.stopTrace();
        }
        mRePlay = rePlayTrace(mOriginLatLngList, mOriginRoleMarker);
    }


    /**
     * 查询轨迹点
     */
    private void queryHistoryTrack(Date date) {
        time.setText(Utils.getDateStr(date, "yyyy-MM-dd"));
        long startTime;
        startTime = Utils.date2TimeStamp(Utils.getDateStr(date, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");

        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
                Constants.SERVICE_ID,
//                terminalId,
                0,
                startTime,
                startTime + 24 * 60 * 60 * 1000
//                System.currentTimeMillis() - 24 * 60 * 60 * 1000,
//                System.currentTimeMillis()
//                0,      // 不绑路
//                0,      // 不做距离补偿
//                5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
//                0,  // 由旧到新排序
//                1,  // 返回第1页数据
//                100,    // 一页不超过100条
//                ""  // 暂未实现，该参数无意义，请留空
        );
        aMapTrackClient.queryHistoryTrack(historyTrackRequest, new SimpleOnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                if (historyTrackResponse.isSuccess()) {
                    HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
                    if (historyTrack == null || historyTrack.getCount() == 0) {
                        Toast.makeText(CheckOtherTraActivity.this, "未获取到轨迹点", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Point> points = historyTrack.getPoints();
                    setupRecord(points, historyTrack.getStartPoint(), historyTrack.getEndPoint());
                    startMove();//开始移动
                } else {
                    showToast(historyTrackResponse.getErrorDetail());
                    Toast.makeText(CheckOtherTraActivity.this, "查询历史轨迹点失败，" + historyTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 轨迹回放方法
     */
    private TraceRePlay rePlayTrace(List<LatLng> list, final Marker updateMarker) {
        TraceRePlay replay = new TraceRePlay(list, 100,
                new TraceRePlay.TraceRePlayListener() {

                    @Override
                    public void onTraceUpdating(LatLng latLng) {
                        if (updateMarker != null) {
                            updateMarker.setPosition(latLng); // 更新小人实现轨迹回放
                        }
                    }

                    @Override
                    public void onTraceUpdateFinish() {
//                        mDisplaybtn.setChecked(false);
//                        mDisplaybtn.setClickable(true);
                    }
                });
        mThreadPool.execute(replay);
        return replay;
    }

//    /**
//     * 将纠偏后轨迹小人设置到起点
//     */
//    private void resetGraspRole() {
//        if (mGraspLatLngList == null) {
//            return;
//        }
//        LatLng startLatLng = mGraspLatLngList.get(0);
//        if (mGraspRoleMarker != null) {
//            mGraspRoleMarker.setPosition(startLatLng);
//        }
//    }

//    /**
//     * 将原始轨迹小人设置到起点
//     */
//    private void resetOriginRole() {
//        if (mOriginLatLngList == null) {
//            return;
//        }
//        LatLng startLatLng = mOriginLatLngList.get(0);
//        if (mOriginRoleMarker != null) {
//            mOriginRoleMarker.setPosition(startLatLng);
//        }
//    }


//    public void onBackClick(View view) {
//        this.finish();
//        if (mThreadPool != null) {
//            mThreadPool.shutdownNow();
//        }
//    }

    public void onDestroy() {
        super.onDestroy();
        if (mThreadPool != null) {
            mThreadPool.shutdownNow();
        }
    }

    private LatLngBounds getBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (mOriginLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mOriginLatLngList.size(); i++) {
            b.include(mOriginLatLngList.get(i));
        }
        return b.build();

    }

    /**
     * 轨迹数据初始化
     */
    private void setupRecord(List<Point> points, TrackPoint startPoint, TrackPoint endPoint) {
        // 轨迹纠偏初始化
        LBSTraceClient mTraceClient = new LBSTraceClient(
                getApplicationContext());
        List<TraceLocation> mGraspTraceLocationList = new ArrayList<>();
        LatLng startLatLng = new LatLng(startPoint.getLocation().getLat(), startPoint.getLocation().getLng());
        LatLng endLatLng = new LatLng(endPoint.getLocation().getLat(), endPoint.getLocation().getLng());
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            mOriginLatLngList.add(latLng);
            TraceLocation location = new TraceLocation();
            location.setLatitude(p.getLat());
            location.setLongitude(p.getLng());
            location.setSpeed((float) p.getSpeed());
            location.setTime(p.getTime());
            mGraspTraceLocationList.add(location);
//                boundsBuilder.include(latLng);
        }
        addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);

        setOriginEnable();
        // 调用轨迹纠偏，将mGraspTraceLocationList进行轨迹纠偏处理
//            mTraceClient.queryProcessedTrace(1, mGraspTraceLocationList,
//                    LBSTraceClient.TYPE_AMAP, this);

    }

    /**
     * 地图上添加原始轨迹线路及起终点、轨迹动画小人
     *
     * @param startPoint
     * @param endPoint
     * @param originList
     */
    private void addOriginTrace(LatLng startPoint, LatLng endPoint,
                                List<LatLng> originList) {
        polylines = mAMap.addPolyline(new PolylineOptions().color(
                Color.BLUE).addAll(originList));
        mOriginStartMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start)));
        mOriginEndMarker = mAMap.addMarker(new MarkerOptions().position(
                endPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.end)));

        try {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(),
                    50));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mOriginRoleMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.walk))));
    }

    /**
     * 设置显示原始轨迹
     */
    private void setOriginEnable() {
        if (polylines == null || mOriginStartMarker == null
                || mOriginEndMarker == null || mOriginRoleMarker == null) {
            return;
        }
        polylines.setVisible(true);
        mOriginStartMarker.setVisible(true);
        mOriginEndMarker.setVisible(true);
        mOriginRoleMarker.setVisible(true);
    }

    /**
     * 地图上添加纠偏后轨迹线路及起终点、轨迹动画小人
     */
//    private void addGraspTrace(List<LatLng> graspList, boolean mGraspChecked) {
//        if (graspList == null || graspList.size() < 2) {
//            return;
//        }
//        LatLng startPoint = graspList.get(0);
//        LatLng endPoint = graspList.get(graspList.size() - 1);
//        mGraspPolyline = mAMap.addPolyline(new PolylineOptions()
//                .setCustomTexture(
//                        BitmapDescriptorFactory
//                                .fromResource(R.drawable.grasp_trace_line))
//                .width(40).addAll(graspList));
//        mGraspStartMarker = mAMap.addMarker(new MarkerOptions().position(
//                startPoint).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.start)));
//        mGraspEndMarker = mAMap.addMarker(new MarkerOptions()
//                .position(endPoint).icon(
//                        BitmapDescriptorFactory.fromResource(R.drawable.end)));
//        mGraspRoleMarker = mAMap.addMarker(new MarkerOptions().position(
//                startPoint).icon(
//                BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(), R.drawable.walk))));
//        if (!mGraspChecked) {
//            mGraspPolyline.setVisible(false);
//            mGraspStartMarker.setVisible(false);
//            mGraspEndMarker.setVisible(false);
//            mGraspRoleMarker.setVisible(false);
//        }
//    }

    /**
     * 设置显示纠偏后轨迹
     */
//    private void setGraspEnable() {
//        if (mGraspPolyline == null || mGraspStartMarker == null
//                || mGraspEndMarker == null || mGraspRoleMarker == null) {
//            return;
//        }
//        mGraspPolyline.setVisible(false);
//        mGraspStartMarker.setVisible(false);
//        mGraspEndMarker.setVisible(false);
//        mGraspRoleMarker.setVisible(false);
//    }

//    @Override
//    public void onMapLoaded() {
//        Message msg = handler.obtainMessage();
//        msg.what = AMAP_LOADED;
//        handler.sendMessage(msg);
//    }

//    /**
//     * 轨迹纠偏完成数据回调
//     */
//    @Override
//    public void onFinished(int arg0, List<LatLng> list, int arg2, int arg3) {
//        addGraspTrace(list, mGraspChecked);
//        mGraspLatLngList = list;
//    }

//    @Override
//    public void onRequestFailed(int arg0, String arg1) {
//        Toast.makeText(this.getApplicationContext(), "轨迹纠偏失败:" + arg1,
//                Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onTraceProcessing(int arg0, int arg1, List<LatLng> arg2) {
//
//    }

    /**
     * 清空地图
     */
//    private void clearTracksOnMap() {
//        for (Polyline polyline : polylines) {
//            polyline.remove();
//        }
//        polylines.remove();
//        mOriginStartMarker.remove();
//        endMarkers.clear();
//        polylines.clear();
//    }

}
