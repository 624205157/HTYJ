package com.example.main.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.commonlib.Constants;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.adapter.GridImageAdapter;
import com.example.main.bean.Subject;
import com.example.main.utils.GlideEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * Created by 陈泽宇 on 2020/8/31
 * Describe: 更新个人信息
 */
public class UpdateMyActivity extends BaseActivity {
    @BindView(R2.id.head_img)
    ImageView headImg;
    @BindView(R2.id.name)
    EditText name;
    @BindView(R2.id.tel)
    EditText tel;

    private String path = "";

    private Subject personInfo;


    @Override
    protected int setContentView() {
        return R.layout.activity_update_my;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {

        addBack();
        setTitleText("更新个人信息");

        String subject = (String) shareHelper.query("subject", "");
        if (TextUtils.isEmpty(subject)) {
            return;
        }
        Gson gson = new Gson();
        personInfo = gson.fromJson(subject, new TypeToken<Subject>() {
        }.getType());
        name.setText(personInfo.getName());
        tel.setText(personInfo.getMobile());
        Glide.with(this).load(personInfo.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(headImg);


    }

    @OnClick({R2.id.head_img, R2.id.submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_img) {
            initSelectImage();
        } else if (id == R.id.submit) {
            sendData();
        }
    }

    private void initSelectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 图片选择结果回调
                        // 例如 LocalMedia 里面返回三种path
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                        LocalMedia media = result.get(0);
                        if (media.isCompressed()) {
                            path = media.getCompressPath();
                        } else {
                            path = media.getPath();
                        }
                        Glide.with(UpdateMyActivity.this).load(path).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                                .into(headImg);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

    //    avatar
    private void sendData() {
        File file = null;
        RequestParams params = new RequestParams();
        params.put("id", personInfo.getId());
        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.equals(personInfo.getName(), name.getText())) {
            params.put("name", name.getText() + "");
            personInfo.setName(name.getText() + "");
        }

        if (!TextUtils.isEmpty(tel.getText()) && !TextUtils.equals(personInfo.getMobile(), tel.getText())) {
            params.put("mobile", tel.getText() + "");
            personInfo.setMobile(tel.getText() + "");
        }

        if (!TextUtils.isEmpty(path)) {
            file = new File(path);
            personInfo.setAvatar(path);
        }
        buildDialog("提交中");

        RequestCenter.addUpdateData(UrlService.USER, params, "avatar", file, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("msg");
                    showToast(message);
                    if (TextUtils.equals("0", code)) {
                        shareHelper.save("subject", jsonObject.getString("data")).commit();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException e) {
                showToast("网络连接失败,请稍后重试");
                e.printStackTrace();
                cancelDialog();
            }
        });

    }

}
