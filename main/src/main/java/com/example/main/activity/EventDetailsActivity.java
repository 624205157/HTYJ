package com.example.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.commonlib.view.BottomDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.adapter.EventDetailsAdapter;
import com.example.main.adapter.ShowGridImageAdapter;
import com.example.main.bean.Event;
import com.example.main.bean.MyFiles;
import com.example.main.bean.People;
import com.example.main.bean.Processes;
import com.example.main.utils.FullyGridLayoutManager;
import com.example.main.utils.GlideEngine;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

/**
 * Created by czy on 2020/8/14 13:59.
 * describe: 突发事件详情
 */
public class EventDetailsActivity extends BaseActivity {
    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.address)
    TextView address;
    @BindView(R2.id.type)
    TextView type;
    @BindView(R2.id.level)
    TextView level;
    @BindView(R2.id.count)
    TextView count;
    @BindView(R2.id.photo_recycler1)
    RecyclerView photoRecycler1;
    @BindView(R2.id.state)
    TextView state;
    @BindView(R2.id.state_recycler)
    RecyclerView stateRecycler;
    @BindView(R2.id.submit)
    TextView submit;

    private EventDetailsAdapter mAdapter;
    private List<Processes> mData = new ArrayList<>();
    private List<People> peopleList = new ArrayList<>();
    private List<String> peopleName = new ArrayList<>();

    private Event event;

    private ShowGridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();


    @Override
    protected int setContentView() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("突发事件处理");

        mAdapter = new EventDetailsAdapter(mData);

        mAdapter.setAnimationEnable(false);
        stateRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        stateRecycler.setAdapter(mAdapter);
        if (TextUtils.equals(getIntent().getStringExtra("state"), "1")) {
            submit.setVisibility(View.GONE);
            state.setText("已办结");
        }else {
            state.setText("处理中");
        }
        init();

        getData();
    }


    private void init() {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        photoRecycler1.setLayoutManager(manager);
        adapter = new ShowGridImageAdapter(this);
        adapter.setList(selectList);
        photoRecycler1.setAdapter(adapter);

        adapter.setOnItemClickListener(new ShowGridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(EventDetailsActivity.this)
                        .themeStyle(R.style.picture_default_style)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .openExternalPreview(position, selectList);
            }
        });


    }


    private void getData() {
        RequestParams params = new RequestParams();
        params.put("id", getIntent().getStringExtra("id"));

        RequestCenter.getDataList(UrlService.EVENT, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = (JSONObject) result.getJSONObject("data").getJSONArray("list").get(0);

                    if (TextUtils.equals(result.getString("code"), "0")) {

                        event = new Gson().fromJson(data.toString(), new TypeToken<Event>() {
                        }.getType());

                        name.setText("事件名称: " + event.getName());
                        address.setText("发生地址: " + event.getAddress());
                        type.setText("事件类型: " + event.getType());
                        level.setText("紧急程度: " + event.getLevel());
                        count.setText(event.getContent());

                        for (MyFiles imageList : event.getAttachments()) {
                            selectList.add(new LocalMedia(imageList.getUid(), imageList.getUrl()));
                        }
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();

                        mData.addAll(event.getProcesses());
                        mAdapter.setList(mData);

                        mAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });

        RequestParams params2 = new RequestParams();
        params2.put("eventId", getIntent().getStringExtra("id"));
        RequestCenter.getDataList(UrlService.EVENTUSER, params2, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        Gson gson = new Gson();
                        peopleList.addAll(gson.fromJson(result.getString("data"),new TypeToken<List<People>>(){}.getType()));
                        for (People people : peopleList){
                            peopleName.add(people.getName());
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });
    }

    @OnClick({R2.id.navigation, R2.id.submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.navigation) {
            ARouter.getInstance().build("/map/navigation")
                    .withParcelable("address", event.getPoint())
                    .navigation();
        } else if (id == R.id.submit) {
            showDialog();
        }
    }

    private OptionsPickerView reasonPicker;

    private void showDialog() {
        final String[] stateStr = new String[1];
        View view = getLayoutInflater().inflate(R.layout.dialog_processing_opinion, null);
        ImageView close = view.findViewById(R.id.close);
        EditText count = view.findViewById(R.id.count);
        LinearLayout ll = view.findViewById(R.id.ll);
        TextView chose = view.findViewById(R.id.chose_next);
        TextView submit = view.findViewById(R.id.submit);
        ((RadioGroup) view.findViewById(R.id.deal_with_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.over) {//办结
                    stateStr[0] = "1";
                } else if (checkedId == R.id.next) {//转发
                    stateStr[0] = "0";
                    ll.setVisibility(View.VISIBLE);

                }
            }
        });
        BottomDialog dialog = new BottomDialog(this)
                .setAddView(view);

        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonPicker.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog("");
                sendData(Utils.getText(count), stateStr[0], Utils.getText(chose), dialog);
            }
        });
        initPicker(chose);
    }

    private void sendData(String content, String state, String users, BottomDialog dialog) {
        if (TextUtils.isEmpty(content)) {
            showToast("处理意见不可为空");
            return;
        }
        if (TextUtils.isEmpty(state)) {
            showToast("处理状态未选择");
            return;
        }
        if (TextUtils.equals(state, "0") && TextUtils.isEmpty(users)) {
            showToast("未选择转发对象");
            return;
        }
        List<String> userList = new ArrayList<>();
        userList.add(users);
        Processes processes = new Processes();
        processes.setEventId(event.getId());
        processes.setContent(content);
        processes.setState(state);
        processes.setUsers(userList);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(processes);

        RequestCenter.patchData(UrlService.EVENT, jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (TextUtils.equals(result.getString("code"),"0")) {
                        showToast("处理成功");
                        dialog.dismiss();
                    }else {
                        showToast(result.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                showToast(responseObj.getMessage());
            }
        });

    }

    private void initPicker(TextView tv) {
        reasonPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tv.setText(peopleList.get(options1).getName());
            }
        }).setTitleText("选择人员").setContentTextSize(22).setTitleSize(22).setSubCalSize(21)
                .isDialog(true)
                .build();
        reasonPicker.setPicker(peopleName);
    }

}
