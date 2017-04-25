package com.yunma.jhuo.activity.mine;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.jhuo.general.MyActivity;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.PersonInfosMoifyInterface.PersonInfosMoifyView;
import com.yunma.jhuo.m.UploaderToServiceInterface.UploaderToServiceView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.*;

import org.greenrobot.greendao.query.Query;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.*;
import top.zibin.luban.*;

public class PersonInfos extends MyActivity implements PersonInfosMoifyView,GetQiniuTokenView,
        UploaderToServiceView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvSave) TextView tvSave;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imgPhoto) CircleImageView imgPhoto;
    @BindView(R.id.layoutEditPhoto) LinearLayout layoutEditPhoto;
    @BindView(R.id.tvName) EditText tvName;
    @BindView(R.id.layoutName) LinearLayout layoutName;
    @BindView(R.id.tvGender) EditText tvGender;
    @BindView(R.id.layoutGender) LinearLayout layoutGender;
    @BindView(R.id.tvRealName) EditText tvRealName;
    @BindView(R.id.layoutRealName) LinearLayout layoutRealName;
    @BindView(R.id.layout) View layout;
    private Context mContext;
    private String qiNiuToken = null;
    private PersonInfosMoifyPre moifyPre = null;
    private CustomProgressDialog dialog = null;
    private GetQiniuTokenPre mPresent = null;
    private UpLoaderToServicePre nPresenter = null;
    private String imgUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_infos);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }

    private void getDatas() {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                .build();
        List<UserInfos> users = nQuery.list();
        LogUtils.log("users.size() -------->  " + " " + users.size());
        tvName.setText(users.get(0).getNickName());
        tvGender.setText(users.get(0).getGender());
        tvRealName.setText(users.get(0).getRealName());
        if(users.get(0).getImgsPhotos()!=null&&
                !users.get(0).getImgsPhotos().isEmpty()){
            GlideUtils.glidNormleFast(getApplicationContext(),
                    imgPhoto,users.get(0).getImgsPhotos());
        }
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        moifyPre = new PersonInfosMoifyPre(this);
        nPresenter = new UpLoaderToServicePre(this);
        int statusHeight = ScreenUtils.getStatusHeight(PersonInfos.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.tvSave,R.id.layoutEditPhoto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                if(imgUrl!=null){
                    Intent intent = new Intent();
                    intent.putExtra("path",imgUrl);
                    PersonInfos.this.setResult(1, intent);
                }
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.tvSave:
                if(tvName.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"昵称不能为空");
                }else if(tvGender.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext," 性别不能为空");
                }else if(isTrueGender()){
                    ToastUtils.showWarning(mContext," 请输入正确的性别");
                }else if(tvRealName.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext," 真实姓名不能为空");
                }else{
                    moifyPre.modifyPersonalInfos();
                }
                break;
            case R.id.layoutEditPhoto:
                mPresent = new GetQiniuTokenPre(this);
                mPresent.getToken(mContext,"user");
                break;
        }
    }

    private boolean isTrueGender() {
        if(!tvGender.getText().toString().contains("男")){
            return false;
        }else if(!tvGender.getText().toString().contains("女")){
            return false;
        }
        return true;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(getContext(),msg);
        }else {
            setQiNiuToken(resultBean.getSuccess());
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        }
    }

    @Override
    public String getNickname() {
        return tvName.getText().toString().trim();
    }

    @Override
    public String getGender() {
        return tvGender.getText().toString().trim();
    }

    @Override
    public String getRealName() {
        return tvRealName.getText().toString().trim();
    }

    @Override
    public void showPersonInfosMoifyResult(SuccessResultBean successResultBean, String msg) {
        if(successResultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
            Query<UserInfos> nQuery = getUserDao().queryBuilder()
                    .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                    .build();
            List<UserInfos> users = nQuery.list();
            LogUtils.log("users.size() -------->  " + " " + users.size());
            UserInfos userInfos =  new UserInfos(users.get(0).getId(),users.get(0).getUserId(),
                    users.get(0).getPhoneNumber(), users.get(0).getPassWd(),
                    users.get(0).getImgsPhotos(), users.get(0).getIsAutoLogin(),
                    tvName.getText().toString(),tvGender.getText().toString(),
                    tvRealName.getText().toString());
            getUserDao().insertOrReplace(userInfos);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
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
            cursor1.close();
            compressWithLs(new File(path));
           // upLoadToQiniu(path);
        }
    }

    private void compressWithLs(File file) {
        Luban.get(PersonInfos.this)
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
                        ToastUtils.showError(PersonInfos.this,"压缩失败,请稍后重试");
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
            dialog = new CustomProgressDialog(PersonInfos.this,"配置中...", R.drawable.loading_setting);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    @Override
    public Context getConText() {
        return getContext();
    }

    @Override
    public String getImageUrl() {
        return imgUrl;
    }

    @Override
    public void showUploaderInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getContext(),msg);
        }else{
            ToastUtils.showSuccess(getContext(),msg);
            GlideUtils.glidNormleFast(mContext,imgPhoto,imgUrl);
            Query<UserInfos> nQuery = getUserDao().queryBuilder()
                    .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                    .build();
            List<UserInfos> users = nQuery.list();
            LogUtils.log("users.size() -------->  1" + " " + users.size());
            UserInfos userInfos =  new UserInfos(users.get(0).getId(),users.get(0).getUserId(),
                    users.get(0).getPhoneNumber(), users.get(0).getPassWd(), imgUrl,
                    users.get(0).getIsAutoLogin(),users.get(0).getNickName(),users.get(0).getGender(),
                    users.get(0).getRealName());
            getUserDao().insertOrReplace(userInfos);
            progressDimiss();
        }
    }

    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDimiss();
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
