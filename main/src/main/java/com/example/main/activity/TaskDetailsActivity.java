package com.example.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.GridImageAdapter;
import com.example.main.bean.Control;
import com.example.main.bean.Task;
import com.example.main.utils.FullyGridLayoutManager;
import com.example.main.utils.GlideEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/23 13:19.
 * describe: 任务详情
 */
public class TaskDetailsActivity extends BaseActivity {
    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.start_time)
    TextView startTime;
    @BindView(R2.id.stop_time)
    TextView stopTime;
    @BindView(R2.id.time_limit)
    TextView timeLimit;
    @BindView(R2.id.state)
    TextView state;
    @BindView(R2.id.count)
    TextView count;
    @BindView(R2.id.controls)
    LinearLayout controls;
    @BindView(R2.id.v_5)
    View v5;
    @BindView(R2.id.stop_time_tv)
    TextView stopTimeTv;
//    @BindView(R2.id.state_recycler)
//    RecyclerView stateRecycler;

//    private EventDetailsAdapter mAdapter;
//    private List<Processes> mData = new ArrayList<>();

    private Task task;

    @Override
    protected int setContentView() {
        return R.layout.activity_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("下发任务");

//        mAdapter = new EventDetailsAdapter(mData);
//
//        mAdapter.setAnimationEnable(false);
//        stateRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        stateRecycler.setAdapter(mAdapter);

        getData();

    }

    private void getData() {
        task = getIntent().getParcelableExtra("task");

        name.setText(task.getName());
        startTime.setText(task.getStartTime());
        if (!TextUtils.isEmpty(task.getStopTime())) {
            stopTime.setText(task.getStopTime());
            stopTimeTv.setVisibility(View.VISIBLE);
            stopTime.setVisibility(View.VISIBLE);
            v5.setVisibility(View.VISIBLE);
        }
        timeLimit.setText(task.getTimeLimit());
        if (TextUtils.equals(task.getState(), "1")) {
            state.setText("已完成");
        }
        if (TextUtils.equals(task.getState(), "2")) {
            state.setText("进行中");
        }
        if (TextUtils.equals(task.getState(), "3")) {
            state.setText("未开始");
        }

        count.setText(task.getCount());

        Gson gson = new Gson();
        List<Control> controlList = new ArrayList<>();
        controlList.addAll(gson.fromJson(task.getControls(),new TypeToken<List<Control>>(){}.getType()));

//        controls.addView();
    }


    @OnClick({R2.id.submit, R2.id.save})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.submit) {

        } else if (id == R.id.save) {

        }
    }

    private void showControl(List<Control> controlList){
        for (Control control : controlList){
            switch (control.getType()){
                case "input":

                    break;

                case "textarea":

                    break;

                case "radio":

                    break;

                case "checkbox":

                    break;

                case "date":

                    break;

                case "upload":

                    break;
            }


        }
    }

    private void addInput(){
        View view = getLayoutInflater().inflate(R.layout.layout_input, null);
    }

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();

    private void initPhotoRecycler(RecyclerView photoRecycler) {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        photoRecycler.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                initSelectImage(adapter, selectList);
            }
        });
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        photoRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(TaskDetailsActivity.this)
                        .themeStyle(R.style.picture_default_style)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .openExternalPreview(position, selectList);
            }
        });

    }

    /**
     * 选择图片
     *
     * @param adapter
     * @param selectList
     */
    private void initSelectImage(GridImageAdapter adapter, List<LocalMedia> selectList) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(9)// 最大图片选择数量
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
}
