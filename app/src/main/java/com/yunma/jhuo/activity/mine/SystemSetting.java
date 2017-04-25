package com.yunma.jhuo.activity.mine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.yunma.AddMoreUser;
import com.yunma.R;
import com.yunma.dao.*;
import com.yunma.emchat.Constant;
import com.yunma.emchat.DemoHelper;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.general.*;
import com.yunma.utils.*;
import com.yunma.widget.CircleImageView;
import com.yunma.widget.CustomProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import me.nereo.multi_image_selector.MultiImageSelector;

public class SystemSetting extends MyCompatActivity {
    private static final int REQUEST_IMAGE = 2;
    @BindView(R.id.layoutBack) View layoutBack;
    @BindView(R.id.layouStatusBar) View layouStatusBar;
    @BindView(R.id.imgPhoto) CircleImageView imgPhoto;
    @BindView(R.id.layoutMyInfo) View layoutMyInfo;
    @BindView(R.id.layoutAccountManage) View layoutAccountManage;
    @BindView(R.id.layoutSetInvoice) View layoutSetInvoice;
    @BindView(R.id.layoutEmSetting) View layoutEmSetting;
    @BindView(R.id.layoutClearCache) View layoutClearCache;
    @BindView(R.id.layoutPopWindowSetting) View layoutPopWindowSetting;
    @BindView(R.id.layoutAboutUs) View layoutAboutUs;
    @BindView(R.id.btnExit) TextView btnExit;
    @BindView(R.id.tvContactUs) TextView tvContactUs;
    @BindView(R.id.layoutMoreRegister) View layoutMoreRegister;
    @BindView(R.id.layout) View layout;
    private Context mContext;
    private CustomProgressDialog dialog = null;
    private long startDate ;
    private String path = null;
    private ArrayList<String> mSelectPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        AppManager.getAppManager().addActivity(SystemSetting.this);
        getDatas();
        setDatas();
    }

    private void getDatas() {
        List<AppInfo> info = getAppDao().loadAll();
        Log.i("tag", "当前AppInfo数量：" + info.size());
        if(info.get(0).getIsFirstSetting()==0){
            progressShow();
            AppInfo infos = new AppInfo(info.get(0).getId(),1,info.get(0).getIsLogin(),
                    info.get(0).getToken(),1);
            getAppDao().save(infos);
        }
        //查询数据
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                .build();
        List<UserInfos> users = nQuery.list();
        if(users.get(0).getImgsPhotos()!=null&&
                !users.get(0).getImgsPhotos().isEmpty()){
            GlideUtils.glidNormleFast(getApplicationContext(),
                    imgPhoto,users.get(0).getImgsPhotos());
        }
    }

    private void setDatas() {
        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
        if (tvContactUs.getText() instanceof Spannable) {
            Spannable s = (Spannable) tvContactUs.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
        long endDate = DateTimeUtils.getCurrentTimeInLong();
        long cost = endDate - startDate;
        if(cost>1500){
            progressDimiss();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDimiss();
                }
            }, 1500 - cost);
        }
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(SystemSetting.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        if(SPUtils.getRole(mContext).contains("运营")){
            layoutMoreRegister.setVisibility(View.VISIBLE);
        }else{
            layoutMoreRegister.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.layoutBack, R.id.btnExit,R.id.layoutMyInfo, R.id.layoutAccountManage,R.id.layoutEmSetting,
            R.id.layoutSetInvoice, R.id.layoutClearCache, R.id.layoutAboutUs,R.id.layoutMoreRegister,
            R.id.layoutPopWindowSetting})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutMoreRegister:
                intent = new Intent(mContext,AddMoreUser.class);
                mContext.startActivity(intent);
                break;
            case R.id.layoutBack:
                if(getPath()!=null){
                    intent = new Intent();
                    intent.putExtra("path",getPath());
                    SystemSetting.this.setResult(2, intent);
                }
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnExit://退出登录
                SPUtils.clear(mContext);
                logout();
                break;
            case R.id.layoutMyInfo://个人资料
                intent = new Intent(mContext,PersonInfos.class);
                startActivityForResult(intent,1);
                break;
            case R.id.layoutAccountManage://帐号管理
                intent = new Intent(mContext,AccountManage.class);
                startActivity(intent);
                break;
            case R.id.layoutSetInvoice://设置个人增值税发票
                intent = new Intent(mContext,InvoiceListActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutClearCache://清楚缓存
                showCacheSize();
                break;
            case R.id.layoutAboutUs://关于我们
                intent = new Intent(SystemSetting.this,AboutUs.class);
                startActivity(intent);
                break;
            case R.id.layoutEmSetting://环信设置
                intent = new Intent(SystemSetting.this,EmSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutPopWindowSetting:
                intent = new Intent(SystemSetting.this,PopWindowsActivity.class);
                startActivity(intent);
                break;
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        startActivity(new Intent(SystemSetting.this, LoginActivity.class));
                        AppManager.getAppManager().finishActivity(SystemSetting.this);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        LogUtils.log("退出聊天系统失败");
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    //    Toast.makeText(getApplicationContext(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showCacheSize() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        try {
                            tvShow.setText(Html.fromHtml("当前应用缓存：" +
                                    "<font color = '#019e1b'><big>" +
                                            SPUtils.getTotalCacheSize(mContext) + "</big></font>"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtils.clearAllCache(mContext);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
            String path = data.getStringExtra("path");
            setPath(path);
            GlideUtils.glidNormleFast(getApplicationContext(),
                    imgPhoto,path);
        }else if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    sb.append("\n");
                }
                LogUtils.log("---->  " + sb.toString());
            }
        }

          //  LogUtils.log("---->  " + sb.toString());

    }

    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
        if (dialog == null) {
            dialog = new CustomProgressDialog(SystemSetting.this,"获取配置中...", R.drawable.loading_setting);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private AppInfoDao getAppDao(){
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(MainActivity.mainContext.isConflict){
            outState.putBoolean("isConflict", true);
        }else if(MainActivity.mainContext.getCurrentAccountRemoved()){
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

}
