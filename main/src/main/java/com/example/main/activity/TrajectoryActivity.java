package com.example.main.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.TrackPoint;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.Constants;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.utils.SimpleOnTrackLifecycleListener;
import com.example.main.utils.SimpleOnTrackListener;
import com.example.main.utils.Utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/16 13:46.
 * describe: 轨迹
 */
public class TrajectoryActivity extends RightTitleActivity {
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.map)
    TextureMapView map;
    @BindView(R2.id.is_open)
    Switch isOpen;

    private String TAG = "TrajectoryActivity";

    private AMapTrackClient aMapTrackClient;
    private String username;
    private long terminalId;
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

    private List<Polyline> polylines = new LinkedList<>();
    private List<Marker> endMarkers = new LinkedList<>();

    private TimePickerView pvTime;

    @Override
    protected int setContentView() {
        return R.layout.activity_trajectory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {

        addBack();
        setTitleText("轨迹管理");

        rightTitle("查看他人轨迹", new RightClickListener() {
            @Override
            public void callBack() {
                startActivity(new Intent(TrajectoryActivity.this, CheckOtherTraActivity.class));
            }
        });

        isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showToast("位置上报已开启");
                    handler.postDelayed(runnable, 10000);
                    startTrack();
                } else {
                    stopTrack();
                    showToast("位置上报已关闭");
                }
            }
        });

        username = (String) shareHelper.query("username", "");//本人账号
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(2, 10);


        map.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
        map.onCreate(savedInstanceState);
        // 启用地图内置定位
        map.getMap().setMyLocationEnabled(true);
        map.getMap().setMyLocationStyle(
                new MyLocationStyle()
                        .interval(2000)
                        .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
                        .strokeColor(Color.argb(0, 0, 0, 0))// 设置圆形的边框颜色
                        .radiusFillColor(Color.argb(0, 0, 0, 0))// 设置圆形的填充颜色
        );

        queryTerminal();

        initView();

//        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                test();
//            }
//        });

    }

    private void initView() {
        //时间选择器
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                clearTracksOnMap();
                queryHistoryTrack(date);

                if (Utils.isToday(date)) {
                    handler.postDelayed(runnable, 10000);
                } else {
                    handler.removeCallbacks(runnable);
                }

            }
        }).build();
    }

    Handler handler = new Handler();

    /**
     * 10秒更新一次当天数据
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            getTodayTrack();
            handler.postDelayed(this, 10000);
        }
    };


    private void getTodayTrack() {
        Date date = new Date();
        queryHistoryTrack(date);
    }


    @OnClick({R2.id.time})
    public void onViewClicked(View view) {
        int id = view.getId();

        if (id == R.id.time) {
            pvTime.show();
        }
    }

    /**
     * 轨迹服务监听回调
     */
    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                    status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                    status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 服务启动成功，继续开启收集上报
                aMapTrackClient.startGather(this);
                Log.e(TAG, "轨迹上报服务启动成功");
            } else {
                showToast("轨迹上报服务服务启动异常，" + msg);
            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                shareHelper.save("tra", false).commit();
                Log.e(TAG, "轨迹上报服务停止");
                // 成功停止
//                Toast.makeText(TrajectoryActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrajectoryActivity.this,
                        "error onStopTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE
                    || status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
//                Toast.makeText(TrajectoryActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                shareHelper.save("tra", true).commit();
                Log.e(TAG, "定位采集已经开启");
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrajectoryActivity.this,
                        "error onStartGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                Log.e(TAG, "定位采集停止成功");
//                Toast.makeText(TrajectoryActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(TrajectoryActivity.this,
                        "error onStopGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * 查询Terminal ID
     */
    private void queryTerminal() {
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, username), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        // 当前终端已经创建过，直接使用查询到的terminal id
                        terminalId = queryTerminalResponse.getTid();
//                        if (uploadToTrack) {
//                            aMapTrackClient.addTrack(new AddTrackRequest(Constants.SERVICE_ID, terminalId), new SimpleOnTrackListener() {
//                                @Override
//                                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//                                    if (addTrackResponse.isSuccess()) {
//                                        // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
//                                        trackId = addTrackResponse.getTrid();
//                                        TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
//                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            trackParam.setNotification(createNotification());
//                                        }
//                                        aMapTrackClient.startTrack(trackParam, onTrackListener);
//                                    } else {
//                                        Toast.makeText(TrajectoryActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
                    } else {
                        // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
                        aMapTrackClient.addTerminal(new AddTerminalRequest(username, Constants.SERVICE_ID), new SimpleOnTrackListener() {
                            @Override
                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                                if (addTerminalResponse.isSuccess()) {
                                    terminalId = addTerminalResponse.getTid();
                                } else {
                                    Toast.makeText(TrajectoryActivity.this, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if ((boolean) shareHelper.query("tra", false)) {
                        //查询储存的轨迹开关
                        isOpen.setChecked(true);
                        handler.postDelayed(runnable, 10000);
                    } else {
                        isOpen.setChecked(false);
                    }
                    getTodayTrack();
                } else {
                    Toast.makeText(TrajectoryActivity.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 开始轨迹
     */
    private void startTrack() {
        // 不指定track id，上报的轨迹点是该终端的散点轨迹
        TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            trackParam.setNotification(showMessage(this, "猎鹰轨迹运行中", "猎鹰轨迹运行中"));
        }
        aMapTrackClient.startTrack(trackParam, onTrackListener);
    }
    /**
     * 结束轨迹
     */
    private void stopTrack(){
        aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, terminalId), onTrackListener);
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "trajectory service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        Intent nfIntent = new Intent(TrajectoryActivity.this, TrajectoryActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(TrajectoryActivity.this, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.logo_img)
                .setContentTitle("猎鹰轨迹运行中")
                .setContentText("猎鹰轨迹运行中");
        Notification notification = builder.build();
        return notification;
    }


    /**
     * 消息通知栏
     *
     * @param context 上下文
     * @param tittle  通知栏标题
     * @param content 通知栏内容
     */
    public static Notification showMessage(Context context, String tittle, String content) {
//        Intent intent = new Intent(context, cl);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String id = context.getPackageName();//频道的ID。每个包必须是唯一的
        //渠道名字
        String name = context.getString(R.string.app_name);//频道的用户可见名称
        //创建一个通知管理器
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setContentTitle(tittle)//设置通知标题
                    .setContentText(content)//设置通知内容
                    .setSmallIcon(R.mipmap.login_img)//设置小图标
                    .setLargeIcon(BitmapFactory.decodeResource
                            (context.getResources(), R.mipmap.login_img))//设置大图标
//                    .setContentIntent(pendingIntent)//打开消息跳转到这儿
                    .setAutoCancel(false)// 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                    .setOngoing(true)//将Ongoing设为true 那么notification将不能滑动删除
                    // 从Android4.1开始，可以通过以下方法，设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                    //.setPriority(NotificationCompat.PRIORITY_MAX)

                    // Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
                    // Notification.DEFAULT_SOUND：系统默认铃声。
                    // Notification.DEFAULT_VIBRATE：系统默认震动。
                    // Notification.DEFAULT_LIGHTS：系统默认闪光。
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .build();
        } else {
            @SuppressLint("WrongConstant") NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context)
                            .setContentTitle(tittle)
                            .setContentText(content)
                            .setSmallIcon(R.mipmap.login_img)
                            .setLargeIcon(BitmapFactory.decodeResource
                                    (context.getResources(), R.mipmap.login_img))//设置大图标

//                            .setContentIntent(pendingIntent)//打开消息跳转到这儿
                            .setAutoCancel(false)
                            .setOngoing(true)
                            //.setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)//在锁屏上的显示

                            .setOngoing(true)
//                            .setChannel(id)
                    ;//无效
            notification = notificationBuilder.build();
        }
        return notification;
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
                terminalId,
                startTime,
                startTime + 24 * 60 * 60 * 1000,
                1,      // 不绑路
                0,      // 不做距离补偿
                5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                0,  // 由旧到新排序
                1,  // 返回第1页数据
                999,    // 一页不超过999条
                ""  // 暂未实现，该参数无意义，请留空
        );
        aMapTrackClient.queryHistoryTrack(historyTrackRequest, new SimpleOnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                if (historyTrackResponse.isSuccess()) {
                    HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
                    if (historyTrack == null || historyTrack.getCount() == 0) {
                        Toast.makeText(TrajectoryActivity.this, "未获取到轨迹点", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Point> points = historyTrack.getPoints();
                    drawTrackOnMap(points, historyTrack.getStartPoint(), historyTrack.getEndPoint());
                } else {
                    showToast(historyTrackResponse.getErrorDetail());
                    Toast.makeText(TrajectoryActivity.this, "查询历史轨迹点失败，" + historyTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 在地图上绘制轨迹
     *
     * @param points
     * @param startPoint
     * @param endPoint
     */
    private void drawTrackOnMap(List<Point> points, TrackPoint startPoint, TrackPoint endPoint) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);

        if (startPoint != null && startPoint.getLocation() != null) {
            LatLng latLng = new LatLng(startPoint.getLocation().getLat(), startPoint.getLocation().getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            endMarkers.add(map.getMap().addMarker(markerOptions));
        }

        if (endPoint != null && endPoint.getLocation() != null) {
            for (int i = endMarkers.size() - 1; i > 0; i--) {
                endMarkers.get(i).remove();
            }
            LatLng latLng = new LatLng(endPoint.getLocation().getLat(), endPoint.getLocation().getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            endMarkers.add(map.getMap().addMarker(markerOptions));
        }

        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        Polyline polyline = map.getMap().addPolyline(polylineOptions);
        polylines.add(polyline);
        map.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
    }

    /**
     * 清空地图
     */
    private void clearTracksOnMap() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        for (Marker marker : endMarkers) {
            marker.remove();
        }
        endMarkers.clear();
        polylines.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        handler.removeCallbacks(runnable);
//        if (isServiceRunning) {
//            aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, terminalId), new SimpleOnTrackLifecycleListener());
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

//    private void test() {
//        aMapTrackClient.queryLatestPoint(new LatestPointRequest(Constants.SERVICE_ID, terminalId), new OnTrackListener() {
//
//            @Override
//            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//
//            }
//
//            @Override
//            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
//
//            }
//
//            @Override
//            public void onDistanceCallback(DistanceResponse distanceResponse) {
//
//            }
//
//            @Override
//            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
//                if (latestPointResponse.isSuccess()) {
//                    Point point = latestPointResponse.getLatestPoint().getPoint();
//                    // 查询实时位置成功，point为实时位置信息
//                    showToast("经度:" + point.getLat() + "纬度:" + point.getLng() + "时间:" + point.getTime());
//                } else {
//                    // 查询实时位置失败
//                }
//            }
//
//            @Override
//            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
//
//            }
//
//            @Override
//            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
//
//            }
//
//            @Override
//            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//
//            }
//
//            @Override
//            public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
//
//            }
//        });
//    }
}
