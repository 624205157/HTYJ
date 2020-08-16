package com.example.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.EventDetailsAdapter;
import com.example.main.bean.Event;

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

    private EventDetailsAdapter mAdapter;
    private List<Event> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        mAdapter = new EventDetailsAdapter(mData);

        mAdapter.setAnimationEnable(false);
        stateRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        stateRecycler.setAdapter(mAdapter);
    }

    @OnClick({R2.id.navigation, R2.id.submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.navigation) {
            ARouter.getInstance().build("/map/navigation")
                    .withParcelable("address",null)
                    .navigation();
        } else if (id == R.id.submit) {

        }
    }

    private void showDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_processing_opinion,null);

    }

}
