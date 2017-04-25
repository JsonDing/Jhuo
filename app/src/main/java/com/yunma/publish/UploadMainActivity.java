package com.yunma.publish;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.jhuo.general.CheckPermissionsActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.GlideImageLoader;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadMainActivity extends CheckPermissionsActivity
        implements SelfGoodsInterFace.GetSelfGoodsView {
    @BindView(R.id.repertoryManage) LinearLayout repertoryManage;
    @BindView(R.id.tvSaleOut) TextView tvSaleOut;
    @BindView(R.id.layoutSaleOut) LinearLayout layoutSaleOut;
    private String TAG = "---Jhuo---";
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private Activity mActivity;
    @BindView(R.id.imgPhotos) CircleImageView imgPhotos;
    @BindView(R.id.layoutPhoto) LinearLayout layoutPhoto;
    @BindView(R.id.tvNickName) TextView tvNickName;
    @BindView(R.id.layoutPublishManage) LinearLayout layoutPublishManage;
    @BindView(R.id.layoutOffShelf) LinearLayout layoutOffShelf;
    @BindView(R.id.imgBtnPublish) ImageButton imgBtnPublish;
    @BindView(R.id.imgBtnTakePhotos) ImageButton imgBtnTakePhotos;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private SelfGoodsPre mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_main);
        ButterKnife.bind(this);
        mActivity = this;
        initGallery();
    }


    @OnClick({R.id.imgPhotos, R.id.layoutPublishManage, R.id.layoutOffShelf, R.id.imgBtnPublish
            , R.id.imgBtnTakePhotos,R.id.repertoryManage,R.id.layoutSaleOut})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.imgPhotos:

                break;
            case R.id.layoutPublishManage:
                intent = new Intent(UploadMainActivity.this, PublishManageActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutOffShelf:
                intent = new Intent(this, OffTheShelfActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtnPublish:
                intent = new Intent(UploadMainActivity.this, UploadList.class);
                startActivity(intent);
                break;
            case R.id.imgBtnTakePhotos:
                initPermissions();
                break;
            case R.id.repertoryManage:
                intent = new Intent(this, RepertoryManageActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutSaleOut:
                intent = new Intent(this, SaleOutActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                Toast.makeText(getApplicationContext(), "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                Log.i(TAG, "拒绝过了");
                Toast.makeText(getApplicationContext(), "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }

        } else {
            Log.i(TAG, "不需要授权 ");
            ToastUtils.showSuccess(this, "success");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).openCamera(mActivity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "同意授权");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).openCamera(mActivity);
            } else {
                Log.i(TAG, "拒绝授权");
            }
        }
    }

    private void initGallery() {
        mPresenter = new SelfGoodsPre(this);
        mPresenter.getSpecialOfferGoods(mActivity,"nogoods","12",1);
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.yunma.fileprovider")
                .multiSelect(false)
                //   .crop(true, 1, 1, 400, 400)
                .isOpenCamera(true)
                .filePath(Environment.getExternalStorageDirectory().toString() + "/YunMa/Jhuo")
                .build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean != null){
            tvSaleOut.setVisibility(View.VISIBLE);
            tvSaleOut.setText(String.valueOf(resultBean.getSuccess().getTotal()));
        }
    }

}
