package com.example.main.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.view.TextHintDialog;
import com.example.commonlib.view.TwoChangeDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.GridDetailsAdapter;
import com.example.main.bean.Address;
import com.example.main.bean.Grid;
import com.example.main.bean.Polygon;
import com.example.main.bean.User;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.main.utils.Utils.isMultiSim;

/**
 * Created by 陈泽宇 on 2020/8/24
 * Describe: 网格详情
 */
public class GridDetailsActivity extends RightTitleActivity {
    @BindView(R2.id.list)
    RecyclerView list;

    //    private Grid grid;
    private String gridName;
    private GridDetailsAdapter adapter;
    private List<User> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_grid_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
//        grid = getIntent().getParcelableExtra("grid");
        gridName = getIntent().getStringExtra("name");


        addBack();
        setTitleText(gridName);
        rightTitle("中心点", new RightClickListener() {
            @Override
            public void callBack() {
                ARouter.getInstance().build("/map/navigation")
                        .withParcelable("address", getAddress())
                        .withString("polygon", getIntent().getStringExtra("polygon"))
                        .navigation();
            }
        });

//        mData.addAll(grid.getUsers());

        adapter = new GridDetailsAdapter(mData);
        adapter.setAnimationEnable(true);

        adapter.addChildClickViewIds(R.id.tel);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.tel) {
                    callPhone(mData.get(position).getTel());
                }
            }
        });

        list.setLayoutManager(new GridLayoutManager(this, 2));

        list.setAdapter(adapter);

        getData();

    }

    private void getData() {
        for (int i = 0; i < 9; i++) {
            User user = new User();
            user.setName("张飞" + i);
            user.setTel("1358888" + i+ i+ i+ i);
            mData.add(user);
        }
        adapter.setList(mData);

        adapter.notifyDataSetChanged();
    }

    private Address getAddress() {
        try {
            JSONObject polygon = new JSONObject(getIntent().getStringExtra("polygon"));

            return new Address(polygon.getJSONObject("center").getDouble("lat"), polygon.getJSONObject("center").getDouble("lng"), gridName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void callPhone(String callNumber){
        if (!isMultiSim(this)) {
            new TextHintDialog(this).setMessageStr("拨打电话：" + callNumber)
                    .setClickListener(isConfirm -> {

                    })
                    .show();
        } else {
            new TwoChangeDialog(this).setMessageStr("拨打电话：" + callNumber)
                    .setButtonText("电话卡1", "电话卡2")
                    .setClickListener(tag -> {
                        getPermission(tag,callNumber);
                    }).show();
        }
    }

    private void getPermission(int id ,String callNumber) {
        PermissionX.init(this)
                .permissions(Manifest.permission.CALL_PHONE
                      )
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Utils.call(GridDetailsActivity.this,id,callNumber);
                        } else {
                            String text = "";
                            for (String a : deniedList) {
                                text = text + a + ";";
                            }
                            showToast("您拒绝了" +text);
//                            Toast.makeText(MainActivity.this, "您拒绝了定位权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
