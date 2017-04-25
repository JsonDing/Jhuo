package com.yunma.jhuo.activity.mine;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yunma.R;
import com.yunma.bean.QiniuResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.UploaderToServiceInterface;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.jhuo.p.UpLoaderToServicePre;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;

import butterknife.*;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MyPopupWindow extends MyCompatActivity implements GetQiniuTokenView, UploaderToServiceInterface.UploaderToServiceView {

    @BindView(R.id.openPhotos) Button openPhotos;
    @BindView(R.id.cancle) Button cancle;
    private String qiNiuToken = null;
    private CustomProgressDialog dialog = null;
    private GetQiniuTokenPre mPresent = null;
    private UpLoaderToServicePre nPresenter = null;
    private String imgUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        getQiniuToken();
    }

    private void getQiniuToken() {
        mPresent = new GetQiniuTokenPre(this);
        mPresent.getToken(getApplicationContext(),"user");
        nPresenter = new UpLoaderToServicePre(this);
    }

    @OnClick({ R.id.openPhotos, R.id.cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openPhotos:
                selectPic();
                break;
            case R.id.cancle:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0,R.anim.out_toptobottom);
                break;
        }
    }

    private void selectPic() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    /**
     * 获取选择的图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;//当data为空的时候，不做任何处理
        }
        if (requestCode == 0) {
            progressShow();
            Uri originalUri;
            originalUri = geturi(data);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
            Cursor cursor1 = getContentResolver().query(originalUri,proj,null,null,null);
            assert cursor1 != null;
            int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            compressWithLs(new File(path));
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file) {

        Luban.get(MyPopupWindow.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.log("压缩后路径--> " + file.getAbsolutePath());
                        upLoadToQiniu(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError(MyPopupWindow.this,"压缩失败,请稍后重试");
                    }
                }).launch();
    }


    private void upLoadToQiniu(final String path) {
        final String key = "Jhuo_HeadImage_" + MD5Utils.getMD5(DateTimeUtils.getCurrentTimeInString()) + ".png";
        LogUtils.log("key: " + key);
        if(getQiNiuToken()!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getUploadManager().put(path, key, getQiNiuToken(),
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if(info.isOK()) {
                                        imgUrl = ConUtils.HEAD_IMAGE_URL + key;
                                        LogUtils.log("Upload Success : " + key);
                                        nPresenter.uPLoaderToService();
                                    }else{
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        LogUtils.log("Upload Fail : " + info.error);
                                        progressDimiss();
                                    }
                                }
                            }, null);
                }
            }).start();
        }else{
            progressDimiss();
            ToastUtils.showError(getApplicationContext(),"上传失败");
        }
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(MyPopupWindow.this,"上传中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDimiss();
    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else {
            setQiNiuToken(resultBean.getSuccess());
        }
    }

    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    @Override
    public Context getConText() {
        return getApplicationContext();
    }

    @Override
    public String getImageUrl() {
        return imgUrl;
    }

    @Override
    public void showUploaderInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            progressDimiss();
            Intent intent = new Intent();
            intent.putExtra("path",imgUrl);
            MyPopupWindow.this.setResult(1, intent);
            AppManager.getAppManager().finishActivity(this);
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        "(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")", null, null);
                int index = 0;
                assert cur != null;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
}
