package com.example.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.bean.AddCustomValues;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.commonlib.utils.DynamicAddCompoundHelper;
import com.example.commonlib.view.NoScrollRecyclerView;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.adapter.GridImageAdapter;
import com.example.main.bean.Control;
import com.example.main.bean.Dynamic;
import com.example.main.bean.MyFiles;
import com.example.main.bean.Option;
import com.example.main.bean.Task;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    LinearLayout controlsLayout;
    @BindView(R2.id.v_5)
    View v5;
    @BindView(R2.id.stop_time_tv)
    TextView stopTimeTv;
    @BindView(R2.id.submit)
    TextView submit;
    @BindView(R2.id.save)
    TextView save;
    @BindView(R2.id.view)
    View view;

//    @BindView(R2.id.state_recycler)
//    RecyclerView stateRecycler;

//    private EventDetailsAdapter mAdapter;
//    private List<Processes> mData = new ArrayList<>();

    private Task task;
    private TimePickerView pvTime;
    private OptionsPickerView  pvOption;

    private List<Dynamic> forms = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("任务详情");

        getData();

    }

    private void getData() {
        Gson gson = new Gson();

        task = gson.fromJson(getIntent().getStringExtra("task"), new TypeToken<Task>() {
        }.getType());

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
            submit.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }
        if (TextUtils.equals(task.getState(), "2")) {
            state.setText("进行中");
        }
        if (TextUtils.equals(task.getState(), "3")) {
            state.setText("未开始");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        count.setText(task.getCount());

        List<Control> controlList = new ArrayList<>();
        controlList.addAll(gson.fromJson(task.getControls(), new TypeToken<List<Control>>() {
        }.getType()));
        Map<String, String> map = null;
        if (!TextUtils.isEmpty(task.getValues())) {
            map = gson.fromJson(task.getValues(), HashMap.class);
        }
        showControl(controlList, controlsLayout, map);

        if (task.getAttachments().size() > 0) {
            for (MyFiles imageList : task.getAttachments()) {
                selectList.add(new LocalMedia(imageList.getUid(), imageList.getUrl()));
            }

            adapter.setList(selectList);
            adapter.notifyDataSetChanged();
        }
    }


    @OnClick({R2.id.submit, R2.id.save})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.submit) {
            sendData("1");
        } else if (id == R.id.save) {
            sendData("0");
        }
    }

    private void sendData(String state) {
        RequestParams params = new RequestParams();
        params.put("id", task.getId());
        params.put("state", state);
        Map<String, String> map = new HashMap<>();
        for (Dynamic dynamic : forms) {
            if (dynamic.getControl().isRequired() && TextUtils.isEmpty(Utils.getText(dynamic.getView()))) {
                dynamic.getView().setTextColor(getResources().getColor(R.color.red, null));
                switch (dynamic.getControl().getType()) {
                    case "input":
                    case "textarea":
                    case "date":
                    case "upload":
                        showToast(dynamic.getControl().getPlaceholder());
                    case "radio":
                    case "checkbox":
                    case "select":
                        showToast(dynamic.getControl().getPlaceholder() + dynamic.getControl().getLabel());
                }
                return;
            } else {
                dynamic.getView().setTextColor(getResources().getColor(R.color.text_color, null));
            }
            map.put(dynamic.getControl().getId(), Utils.getText(dynamic.getView()));
        }


        Gson gson = new Gson();

        params.put("values", gson.toJson(map));

        String exist = "";

        if (selectList.size() > 0) {

            Iterator<LocalMedia> it_b = selectList.iterator();
            while (it_b.hasNext()) {
                LocalMedia localMedia = it_b.next();
                if (!TextUtils.isEmpty(localMedia.getUid())) {
                    exist += localMedia.getUid() + ",";
                    it_b.remove();
                }
            }
            if (!TextUtils.isEmpty(exist))
                params.put("exists",  exist.substring(0, exist.length() - 1));
        }

        buildDialog("提交中");

        RequestCenter.addUpdateData(UrlService.TASK, params, Utils.getFileList(selectList), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (TextUtils.equals(result.getString("code"), "0")) {
                        showToast("保存成功");
                        finish();
                    } else {
                        showToast(result.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                showToast(responseObj.getMessage());
                cancelDialog();
            }
        });
    }


    private void showControl(List<Control> controlList, LinearLayout controls, Map<String, String> map) {
        for (Control control : controlList) {
            switch (control.getType()) {
                case "input":
                    controls.addView(addInput(control, map));
                    break;

                case "textarea":
                    controls.addView(addTextArea(control, map));
                    break;

                case "radio":
                    controls.addView(addRadio(control, map));
                    break;

                case "checkbox":
                    controls.addView(addCheckBox(control, map));
                    break;

                case "date":
                    controls.addView(addDate(control, map));
                    break;

                case "upload":
                    controls.addView(addUpload(control));
                    break;

                case "select":
                    controls.addView(addSelect(control, map));
                    break;
            }
            if (controlList.indexOf(control) != controlList.size() - 1) {
                controls.addView(addLine());
            }


        }
    }

    private View addInput(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_input, null);
        TextView tv = view.findViewById(R.id.tv);
        EditText edit = view.findViewById(R.id.edit);
        tv.setText(control.getLabel());
        edit.setHint(control.getPlaceholder());

        if (map != null) {
            edit.setText(map.get(control.getId()));
        }

        forms.add(new Dynamic(control, tv, edit));
        return view;

    }

    private View addTextArea(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_textarea, null);
        TextView tv = view.findViewById(R.id.tv);
        EditText edit = view.findViewById(R.id.edit);
        tv.setText(control.getLabel());
        edit.setHint(control.getPlaceholder());

        if (map != null) {
            edit.setText(map.get(control.getId()));
        }

        forms.add(new Dynamic(control, tv, edit));
        return view;

    }

    private View addRadio(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_grid, null);
        TextView tv = view.findViewById(R.id.tv);
        TextView chose = view.findViewById(R.id.chose);
        GridLayout gl = view.findViewById(R.id.gl);
        tv.setText(control.getLabel());
        chose.setHint(control.getPlaceholder());

        List<AddCustomValues> valueList = new ArrayList<>();
        for (Option option : control.getOptions()) {
            AddCustomValues values = new AddCustomValues(option.getLabel(), option.getValue());
            valueList.add(values);
        }

        DynamicAddCompoundHelper helper = new DynamicAddCompoundHelper<RadioButton>(this, valueList, gl)
                .manageGridLayoutView(RadioButton.class).addCheckedChangeListener(new DynamicAddCompoundHelper.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(int tag, int parentID, String values) {
                        chose.setText(values);
                    }
                });
        if (map != null) {
            helper.radioBoxFromInfo(map.get(control.getId()));
        }


        forms.add(new Dynamic(control, tv, chose));
        return view;

    }


    private View addCheckBox(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_grid, null);
        TextView tv = view.findViewById(R.id.tv);
        TextView chose = view.findViewById(R.id.chose);
        GridLayout gl = view.findViewById(R.id.gl);
        tv.setText(control.getLabel());
        chose.setHint(control.getPlaceholder());

        List<AddCustomValues> valueList = new ArrayList<>();
        for (Option option : control.getOptions()) {
            AddCustomValues values = new AddCustomValues(option.getLabel(), option.getValue());
            valueList.add(values);
        }

        DynamicAddCompoundHelper helper = new DynamicAddCompoundHelper<CheckBox>(this, valueList, gl)
                .manageGridLayoutView(CheckBox.class, 3).addCheckedChangeListener(new DynamicAddCompoundHelper.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(int tag, int parentID, String values) {
                        chose.setText("[" + values + "]");
                    }
                });

        if (map != null) {
            helper.checkedBoxFromInfo(map.get(control.getId()).replace("[", "").replace("]", ""));//去除字符串的 []
        }

        forms.add(new Dynamic(control, tv, chose));
        return view;

    }

    private View addDate(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_date, null);
        TextView tv = view.findViewById(R.id.tv);
        TextView time = view.findViewById(R.id.time);
        tv.setText(control.getLabel());
        time.setHint(control.getPlaceholder());

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        //时间选择器
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                time.setText(Utils.getDateStr(date, "yyyy-MM-dd"));
            }
        }).build();
        if (map != null) {
            time.setText(map.get(control.getId()));
        }
        forms.add(new Dynamic(control, tv, time));
        return view;
    }

    private View addSelect(Control control, Map<String, String> map) {
        View view = getLayoutInflater().inflate(R.layout.layout_date, null);
        TextView tv = view.findViewById(R.id.tv);
        TextView time = view.findViewById(R.id.time);
        TextView save = view.findViewById(R.id.save);
        tv.setText(control.getLabel());
        time.setHint(control.getPlaceholder());

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOption.show();
            }
        });

        List<String> valueList = new ArrayList<>();
        for (Option option : control.getOptions()) {
            valueList.add(option.getLabel());
        }

        pvOption =  new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                time.setText(valueList.get(options1));
                save.setText(control.getOptions().get(options1).getValue());
            }
        }).setTitleText("选择人员").setContentTextSize(22).setTitleSize(22).setSubCalSize(21)
                .build();
        pvOption.setPicker(valueList);

        if (map != null) {
            save.setText(map.get(control.getId()));
            for (Option option : control.getOptions()) {
                if (TextUtils.equals(option.getValue(),map.get(control.getId()))){
                    time.setText(option.getLabel());
                }
            }


        }
        forms.add(new Dynamic(control, tv, save));
        return view;
    }
    private View addUpload(Control control) {
        View view = getLayoutInflater().inflate(R.layout.layout_upload, null);
        TextView tv = view.findViewById(R.id.tv);
        NoScrollRecyclerView rv = view.findViewById(R.id.photo_recycler);
        tv.setText(control.getLabel());

        initPhotoRecycler(rv);
        return view;
    }

    private View addLine() {
        return getLayoutInflater().inflate(R.layout.layout_line, null);
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
        adapter.setSelectMax(9);
        adapter.setList(selectList);
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
//                        for (LocalMedia media : selectList) {
//                            Log.i("图片-----》", new File(media.getPath()).length() + "");
//                            Log.i("压缩图片-----》", new File(media.getCompressPath()).length() + "");

//                        Bitmap bitmap = BitmapFactory.decodeFile(media.getCompressPath());
//                        iv_document.setImageBitmap(bitmap);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
//                        }
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

}
