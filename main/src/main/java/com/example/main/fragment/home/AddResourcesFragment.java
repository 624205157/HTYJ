package com.example.main.fragment.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.activity.PositionFineTuningActivity;
import com.example.main.adapter.GridImageAdapter;
import com.example.main.bean.Grid;
import com.example.main.fragment.BaseFragment;
import com.example.main.utils.FullyGridLayoutManager;
import com.example.main.utils.GlideEngine;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/10 11:30.
 * describe: 新增应急资源
 */
public class AddResourcesFragment extends BaseFragment {


    @BindView(R2.id.map)
    MapView map;
    @BindView(R2.id.name)
    EditText name;
    @BindView(R2.id.type)
    TextView type;
    @BindView(R2.id.address)
    TextView address;
    @BindView(R2.id.total)
    EditText total;
    @BindView(R2.id.remain)
    EditText remain;
    @BindView(R2.id.grid)
    TextView grid;
    @BindView(R2.id.photo_recycler1)
    RecyclerView photoRecycler1;
    @BindView(R2.id.location_text)
    TextView locationText;


    private OptionsPickerView reasonPicker;
    private OptionsPickerView reasonPicker2;
    private List<String> typeNameList = new ArrayList<>();
    private List<String> gridNameList = new ArrayList<>();
    private List<Grid> gridList = new ArrayList<>();
    private List<Grid> typeList = new ArrayList<>();
    private Grid gridSelect = new Grid();
    private Grid typeSelect = new Grid();

    private GridImageAdapter adapter;
    //已经选择图片
    private List<LocalMedia> selectList = new ArrayList<>();
    //照片选择最大值
    private int maxSelectNum = 3;

    AMap aMap = null;

    private boolean isLocation = false;
    private LatLng latLng;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    aMap.clear();
                    latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    locationText.setText("经度:" + aMapLocation.getLongitude() + " 纬度:" + aMapLocation.getLatitude());
                    address.setText(aMapLocation.getAddress());
                    aMap.addMarker(new MarkerOptions().position(latLng));
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 0, 30)));
                    mLocationClient.stopLocation();
                    isLocation = false;
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected int setContentView() {
        return R.layout.fragment_add_resourses;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        map.onCreate(savedInstanceState);// 此方法必须重写

        if (aMap == null) {
            aMap = map.getMap();
        }

        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
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

        getPermission();

        init();



        //初始化网格归属列表
        initGridList();

        buildDialog("");
    }

    @OnClick({R2.id.relocation, R2.id.position_fine_tuning, R2.id.grid, R2.id.submit,R2.id.type})
    public void onViewClicked(View view) {

        int id = view.getId();
        if (id == R.id.relocation) {
            getPermission();
        } else if (id == R.id.position_fine_tuning) {
            startActivityForResult(new Intent(mActivity, PositionFineTuningActivity.class), 0x100);
        } else if (id == R.id.grid) {
            if (reasonPicker2!=null)
            reasonPicker2.show();
        } else if (id == R.id.submit) {
            sendData();
        } else if (id == R.id.type) {
            if (reasonPicker!=null)
            reasonPicker.show();
        }
    }

    private void init() {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        photoRecycler1.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                initSelectImage(adapter, selectList);
            }
        });
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        photoRecycler1.setAdapter(adapter);

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(getActivity())
                        .themeStyle(R.style.picture_default_style)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .openExternalPreview(position, selectList);
            }
        });


    }

    private void initSelectImage(GridImageAdapter adapter, List<LocalMedia> selectList) {
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 图片选择结果回调
                        selectList.clear();
                        selectList.addAll(result);
                        // 例如 LocalMedia 里面返回三种path
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                        for (LocalMedia media : selectList) {
//                            Log.i("图片-----》", new File(media.getPath()).length() + "");
//                            Log.i("压缩图片-----》", new File(media.getCompressPath()).length() + "");

//                        Bitmap bitmap = BitmapFactory.decodeFile(media.getCompressPath());
//                        iv_document.setImageBitmap(bitmap);
                            adapter.setList(selectList);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x100:

                    PoiItem poiItem = data.getExtras().getParcelable("address");

                    aMap.clear();
                    latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    locationText.setText("经度:" + poiItem.getLatLonPoint().getLongitude() + " 纬度:" + poiItem.getLatLonPoint().getLatitude());
                    address.setText(poiItem.getTitle());
                    aMap.addMarker(new MarkerOptions().position(latLng));
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 0, 30)));
                    break;
            }
        }
    }

    private void getPermission() {
        PermissionX.init(getActivity())
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            if (isLocation) {
                                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                                isLocation = false;
                            } else {
                                //启动定位
                                mLocationClient.startLocation();
                                isLocation = true;
                            }
                        } else {
                            showToast("您拒绝了拨打定位权限");
                        }
                    }
                });
    }

    private void sendData() {
        if (TextUtils.isEmpty(name.getText())) {
            showToast("资源名不可为空");
            return;
        }
        RequestParams params = new RequestParams();
        try {
            params.put("name", Utils.getText(name));
            params.put("category_id", typeSelect.getId());
            params.put("address", Utils.getText(address));
            params.put("total", Utils.getText(total));
            params.put("surplus", Utils.getText(remain));
            params.put("longitude", latLng.longitude + "");
            params.put("latitude", latLng.latitude+ "");
            params.put("grid_id", gridSelect.getId());
//            params.put("grid_name", gridSelect.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }



        RequestCenter.addResources(params, Utils.getFileList(selectList), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(result.getString("msg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                showToast(responseObj.getMessage());
            }
        });
    }
    /**
     * 初始化网格归属
     */
    private void initGridList(){

        RequestCenter.getGrid(null, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getInt("code") == 0) {
//                        JSONObject data = result.getJSONObject("data");
                        gridList.clear();
                        gridList.addAll(new Gson().fromJson(result.getString("data"),new TypeToken<List<Grid>>(){}.getType()));
                        typeNameList.clear();
                        for (Grid grid:gridList){
                            typeNameList.add(grid.getName());
                        }
                        reasonPicker2 = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                gridSelect = gridList.get(options1);
                                grid.setText(gridSelect.getName());
                            }
                        }).setTitleText("归属网格").setContentTextSize(22).setTitleSize(22).setSubCalSize(21).build();
                        reasonPicker2.setPicker(typeNameList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });

        RequestParams params = new RequestParams();
        params.put("pid","EMERGENCY_RESOURCE_TYPE");//资源种类
        RequestCenter.getType(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getInt("code") == 0) {
//                        JSONObject data = result.getJSONObject("data");
                        typeList.clear();
                        typeList.addAll(new Gson().fromJson(result.getString("data"),new TypeToken<List<Grid>>(){}.getType()));
                        gridNameList.clear();
                        for (Grid grid: typeList){
                            gridNameList.add(grid.getName());
                        }
                        reasonPicker = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                typeSelect = typeList.get(options1);
                                type.setText(typeSelect.getName());
                            }
                        }).setTitleText("资源种类").setContentTextSize(22).setTitleSize(22).setSubCalSize(21).build();
                        reasonPicker.setPicker(gridNameList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (map != null)
            map.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (map != null)
            map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (map != null)
            map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }
}
