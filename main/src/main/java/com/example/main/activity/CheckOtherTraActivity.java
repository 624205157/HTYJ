package com.example.main.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.CorrectMode;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.RecoupMode;
import com.amap.api.track.query.entity.TrackPoint;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.Constants;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.bean.User;
import com.example.main.utils.SimpleOnTrackListener;
import com.example.main.utils.TraceRePlay;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

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
public class CheckOtherTraActivity extends RightTitleActivity implements TraceListener {


    @BindView(R2.id.check_other)
    TextView checkOther;
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.map)
    TextureMapView mMapView;

    private final static int AMAP_LOADED = 2;


    private AMap mAMap;
    private Marker mGraspStartMarker, mGraspEndMarker, mGraspRoleMarker;
    private Polyline polylines, mGraspPolyline;

    private int mRecordItemId;
    private List<LatLng> mGraspLatLngList = new ArrayList<>();
    private ExecutorService mThreadPool;
    private TraceRePlay mRePlay;

    private long terminalId;

    private AMapTrackClient aMapTrackClient;
    private TimePickerView pvTime;
    private Date selectData = null;

    private List<User> userList = new ArrayList<>();
    private List<String> userName = new ArrayList<>();
    private OptionsPickerView reasonPicker;


    @Override
    protected int setContentView() {
        return R.layout.activity_other_trajectory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("查询他人轨迹");
        rightTitle("查询", new RightClickListener() {
            @Override
            public void callBack() {
                if (terminalId == 0) {
                    showToast("请选择人员");
                    return;
                }
                if (selectData == null) {
                    showToast("请选择时间");
                    return;
                }
//                if (Utils.isToday(selectData)) {
//                    handler.postDelayed(runnable, 60000);
//                }
                queryHistoryTrack(selectData);
            }
        });

        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        initMap();
        aMapTrackClient = new AMapTrackClient(getApplicationContext());

        initView();

        getUserList();

    }


    private void getUserList() {
        buildDialog("");
        RequestParams params = new RequestParams();
        params.put("pageable","n");
        RequestCenter.getDataList(UrlService.USERLIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    cancelDialog();
                    JSONObject data = new JSONObject(responseObj.toString());
                    String code = data.getString("code");
                    if (TextUtils.equals(code, "0")) {
                        Gson gson = new Gson();
                        userList.addAll(gson.fromJson(data.getString("data"), new TypeToken<List<User>>() {
                        }.getType()));
                        for (User user : userList) {
                            userName.add(user.getName());
                        }

                        reasonPicker = new OptionsPickerBuilder(CheckOtherTraActivity.this, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                queryTerminal(userList.get(options1).getUsername());
                                checkOther.setText(userList.get(options1).getName());
                            }
                        }).setTitleText("选择人员").setContentTextSize(22).setTitleSize(22).setSubCalSize(21).build();
                        reasonPicker.setPicker(userName);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                showToast(responseObj.getMessage());
                cancelDialog();
            }
        });

    }

    @OnClick({R2.id.check_other, R2.id.time})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.check_other) {
            if (reasonPicker != null)
                reasonPicker.show();
        } else if (id == R.id.time) {
            pvTime.show();
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
        mRePlay = rePlayTrace(mGraspLatLngList, mGraspRoleMarker);
    }


    private void initView() {
        //时间选择器
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectData = date;
                time.setText(Utils.getDateStr(date, "yyyy-MM-dd"));
            }
        }).build();
    }

//    Handler handler = new Handler();

//    /**
//     * 60秒更新一次当天数据
//     */
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            getTodayTrack();
//            handler.postDelayed(this, 60000);
//        }
//    };

//    private void getTodayTrack() {
//        Date date = new Date();
//        queryHistoryTrack(date);
//    }


    /**
     * 查询轨迹点
     */
    private void queryHistoryTrack(Date date) {
        long startTime;
        startTime = Utils.date2TimeStamp(Utils.getDateStr(date, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        buildDialog("查询中");
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
                Constants.SERVICE_ID,
                terminalId,
                startTime,
                startTime + 24 * 60 * 60 * 1000,
//                System.currentTimeMillis() - 24 * 60 * 60 * 1000,
//                System.currentTimeMillis()
                0,      // 不绑路
                0,      // 不做距离补偿
                5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                0,  // 由旧到新排序
                1,  // 返回第1页数据
                999,    // 一页不超过100条
                ""  // 暂未实现，该参数无意义，请留空
        );
        aMapTrackClient.queryHistoryTrack(historyTrackRequest, new SimpleOnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                cancelDialog();
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

    /**
     * 查询Terminal ID
     */
    private void queryTerminal(String username) {
        buildDialog("查询人员中");
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, username), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                cancelDialog();
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        // 当前终端已经创建过，直接使用查询到的terminal id
                        terminalId = queryTerminalResponse.getTid();
                    } else {
                        showToast("该终端未创建");
                    }
                } else {
                    Toast.makeText(CheckOtherTraActivity.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        if (mGraspLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mGraspLatLngList.size(); i++) {
            b.include(mGraspLatLngList.get(i));
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
        for (Point p : points) {
            TraceLocation location = new TraceLocation();
            location.setLatitude(p.getLat());
            location.setLongitude(p.getLng());
            location.setSpeed((float) p.getSpeed());
            location.setTime(p.getTime());
            mGraspTraceLocationList.add(location);
        }
        setGraspEnable();
        // 调用轨迹纠偏，将mGraspTraceLocationList进行轨迹纠偏处理
        mTraceClient.queryProcessedTrace(1, mGraspTraceLocationList,
                LBSTraceClient.TYPE_AMAP, this);

    }

    /**
     * 地图上添加原始轨迹线路及起终点、轨迹动画小人
     *
     * @param startPoint
     * @param endPoint
     * @param originList
     */
//    private void addOriginTrace(LatLng startPoint, LatLng endPoint,
//                                List<LatLng> originList) {
//        polylines = mAMap.addPolyline(new PolylineOptions().color(
//                Color.BLUE).addAll(originList));
//        mOriginStartMarker = mAMap.addMarker(new MarkerOptions().position(
//                startPoint).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.start)));
//        mOriginEndMarker = mAMap.addMarker(new MarkerOptions().position(
//                endPoint).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.end)));


//        mOriginRoleMarker = mAMap.addMarker(new MarkerOptions().position(
//                startPoint).icon(
//                BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(), R.drawable.walk))));
//    }

    /**
     * 设置显示原始轨迹
     */
//    private void setOriginEnable() {
//        if (polylines == null || mOriginStartMarker == null
//                || mOriginEndMarker == null || mOriginRoleMarker == null) {
//            return;
//        }
//        polylines.setVisible(true);
//        mOriginStartMarker.setVisible(true);
//        mOriginEndMarker.setVisible(true);
//        mOriginRoleMarker.setVisible(true);
//    }

    /**
     * 地图上添加纠偏后轨迹线路及起终点、轨迹动画小人
     */
    private void addGraspTrace(List<LatLng> graspList) {
        if (graspList == null || graspList.size() < 2) {
            return;
        }
        LatLng startPoint = graspList.get(0);
        LatLng endPoint = graspList.get(graspList.size() - 1);
        mGraspPolyline = mAMap.addPolyline(new PolylineOptions()
                .setCustomTexture(
                        BitmapDescriptorFactory
                                .fromResource(R.drawable.grasp_trace_line))
                .width(40).addAll(graspList));
        mGraspStartMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start)));
        mGraspEndMarker = mAMap.addMarker(new MarkerOptions()
                .position(endPoint).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.end)));
        mGraspRoleMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.walk))));

        try {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(),
                    50));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置显示纠偏后轨迹
     */
    private void setGraspEnable() {
        if (mGraspPolyline == null || mGraspStartMarker == null
                || mGraspEndMarker == null || mGraspRoleMarker == null) {
            return;
        }
        mGraspPolyline.setVisible(true);
        mGraspStartMarker.setVisible(true);
        mGraspEndMarker.setVisible(true);
        mGraspRoleMarker.setVisible(true);
    }

//    @Override
//    public void onMapLoaded() {
//        Message msg = handler.obtainMessage();
//        msg.what = AMAP_LOADED;
//        handler.sendMessage(msg);
//    }

    /**
     * 轨迹纠偏完成数据回调
     */
    @Override
    public void onFinished(int arg0, List<LatLng> list, int arg2, int arg3) {
        clearTracksOnMap();
        mGraspLatLngList = list;
        addGraspTrace(list);

    }

    @Override
    public void onRequestFailed(int arg0, String arg1) {
        Toast.makeText(this.getApplicationContext(), "轨迹纠偏失败:" + arg1,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTraceProcessing(int arg0, int arg1, List<LatLng> arg2) {

    }

    /**
     * 清空地图
     */
    private void clearTracksOnMap() {
        if (mGraspPolyline != null)
            mGraspPolyline.remove();
        if (mGraspStartMarker != null)
            mGraspStartMarker.remove();
        if (mGraspEndMarker != null)
            mGraspEndMarker.remove();
        if (mGraspRoleMarker != null)
            mGraspRoleMarker.remove();
    }

}
