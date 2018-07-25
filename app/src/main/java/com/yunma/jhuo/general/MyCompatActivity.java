package com.yunma.jhuo.general;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.dao.AppInfo;
import com.yunma.dao.GreenDaoManager;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.activity.ShareGoodsInfo;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.SearchGoodsPre;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.MD5Utils;
import com.yunma.utils.ValueUtils;

import java.util.List;

/**
 * Created on 2016-11-24.
 * @author Json
 */

public abstract class MyCompatActivity extends AppCompatActivity implements
        SelfGoodsInterFace.SearchGoodsDetialView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .build());
        }*/
    }

    public void hideSoftInput(){
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
    }

    public boolean isLogin() {
        List<AppInfo> appInfos = getAppInfoDao().loadAll();
        return appInfos.size() != 0 && appInfos.get(0).getIsLogin() != 0;
    }

    private AppInfoDao getAppInfoDao() {
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;
            if(clipboard.hasPrimaryClip()){
                assert clipboard.getPrimaryClip().getItemAt(0).getText() != null;
                String info = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                if ( !TextUtils.isEmpty(info)) {
                    int startIndex = info.indexOf("【");
                    int endIndex =  info.indexOf("】");
                    if(startIndex != -1 && endIndex != -1 && endIndex != startIndex+1){
                        String str = info.substring(startIndex + 1,endIndex);
                        showDialog(str);
                    }
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    private void showDialog(String str) {
        SearchGoodsPre mPre = new SearchGoodsPre(this,"detial");
        mPre.getGoodsDetials(this,MD5Utils.getIdFromBase64(str));
    }

    @Override
    public void showGoodsDetials(SelfGoodsListBean resultBean, String msg) {
        if (resultBean != null){
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;
            clipboard.setPrimaryClip(ClipData.newPlainText(null,""));
            Intent intent = new Intent(this,ShareGoodsInfo.class);
            String strPic = resultBean.getPic();
            String goodsPic = null;
            String strUrl = "";
            if (resultBean.getRepoid() == 1) {
                strUrl = ConUtils.SElF_GOODS_IMAGE_URL;
            } else {
                strUrl = ConUtils.GOODS_IMAGE_URL;
            }
            if(strPic != null && !strPic.isEmpty()){
                goodsPic = strPic.split(",")[0];
            }
            String goodsId = String.valueOf(resultBean.getId());
            String goodsName = resultBean.getName();
            String goodsPrice = ValueUtils.toTwoDecimal(resultBean.getYmprice());
            Bundle bundle = new Bundle();
            bundle.putString("goodsId",goodsId);
            bundle.putString("goodsPic",strUrl + goodsPic);
            bundle.putString("goodsName",goodsName);
            bundle.putString("goodsPrice",goodsPrice);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
    }


    public  void showSuccessDialog(Context context,String msg,int imgId,int colorId){
        final Dialog mDialog = new Dialog(context, R.style.CenterDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.hint_dialog, null);
        //初始化视图
        ImageView imgLogo = root.findViewById(R.id.imgLogo);
        TextView tvMgs = root.findViewById(R.id.tvMgs);
        imgLogo.setBackground(ContextCompat.getDrawable(context,imgId));
        tvMgs.setTextColor(ContextCompat.getColor(context, colorId));
        tvMgs.setText(msg);
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        /*lp.width = context.getResources().getDisplayMetrics().widthPixels
                - DensityUtils.dp2px(context,64);*/ // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.width = lp.height;
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
            }
        },1500);
    }

    public UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    public void myIntent(Activity mActivity, Class<?> activityClass){
        Intent intent = new Intent(mActivity,activityClass);
        startActivity(intent);
    }
}
