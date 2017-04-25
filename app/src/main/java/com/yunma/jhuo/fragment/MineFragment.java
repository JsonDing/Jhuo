package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.homepage.ReceiverManage;
import com.yunma.jhuo.activity.mine.*;
import com.yunma.bean.UserInfosResultBean;
import com.yunma.dao.*;
import com.yunma.jhuo.general.RegisterAccount;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.MineFragmentInterface.UserInfosView;
import com.yunma.jhuo.p.UserInfosPre;
import com.yunma.utils.*;
import com.yunma.widget.CircleImageView;
import com.yunma.publish.UploadMainActivity;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.*;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class MineFragment extends Fragment implements UserInfosView {

    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imgPhotos) CircleImageView imgPhotos;
    @BindView(R.id.layoutLookAll) LinearLayout layoutLookAll;
    @BindView(R.id.layoutPayMent) LinearLayout layoutPayMent;
    @BindView(R.id.layoutSendOut) LinearLayout layoutSendOut;
    @BindView(R.id.layoutReceipt) LinearLayout layoutReceipt;
    @BindView(R.id.layoutMyWallet) LinearLayout layoutMyWallet;
    @BindView(R.id.layoutQuickReplenish) LinearLayout layoutQuickReplenish;
    @BindView(R.id.layoutFavorites) LinearLayout layoutFavorites;
    @BindView(R.id.layoutReceiverManage) LinearLayout layoutReceiverManage;
    @BindView(R.id.layoutSaleRemind) LinearLayout layoutSaleRemind;
    @BindView(R.id.layoutSystemNotice) LinearLayout layoutSystemNotice;
    @BindView(R.id.layoutFeedback) LinearLayout layoutFeedback;
    @BindView(R.id.layoutVolumeList) LinearLayout layoutVolumeList;
    @BindView(R.id.layoutSystemSetting) LinearLayout layoutSystemSetting;
    @BindView(R.id.layoutPhoto) LinearLayout layoutPhoto;
    @BindView(R.id.layoutService) LinearLayout layoutService;
    @BindView(R.id.tvNickName) TextView tvNickName;
    @BindView(R.id.layoutGoPublish) View layoutGoPublish;
    private Context mContext;
    private Activity mActivity;
    private UserInfosPre userInfosPre;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        getDatas();
        return view;
    }

    private void initView() {
        mActivity = getActivity();
        mContext = getActivity();
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(MainActivity.mainContext), 0, 0);
    }

    private void getDatas(){
        if(isLogin()){
            if(SPUtils.getRole(mContext).equals("移动上传者")||
                    SPUtils.getRole(mContext).equals("库房")){
                layoutGoPublish.setVisibility(View.VISIBLE);
                layoutSaleRemind.setVisibility(View.GONE);//
            }else{
                layoutSaleRemind.setVisibility(View.VISIBLE);
                layoutGoPublish.setVisibility(View.GONE);
            }
            userInfosPre = new UserInfosPre(this);
            userInfosPre.getUserInfos(mActivity);
        }
    }

    @OnClick({R.id.layoutNews, R.id.layoutLookAll, R.id.layoutPayMent, R.id.layoutSendOut,
            R.id.layoutReceipt, R.id.layoutService, R.id.layoutMyWallet, R.id.layoutQuickReplenish,
            R.id.layoutFavorites, R.id.layoutReceiverManage, R.id.layoutSaleRemind,
            R.id.layoutSystemNotice, R.id.layoutFeedback,R.id.layoutGoPublish,R.id.layoutVolumeList,
            R.id.layoutSystemSetting,R.id.layoutPhoto})
    public void onClick(View view) {
        Intent intent ;
        if(isLogin()){
            switch (view.getId()) {
                case R.id.layoutGoPublish://上传者、库房帐号
                    intent = new Intent(mActivity, UploadMainActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutNews://消息提示
                    QQAuth mqqAuth = QQAuth.createInstance("1106058796",mActivity);
                    WPA mWPA = new WPA(getActivity(), mqqAuth.getQQToken());
                    String ESQ = "2252162352";  //客服QQ号
                    int ret = mWPA.startWPAConversation(getActivity(),ESQ,null);
                    if (ret != 0) {
                        Toast.makeText(getActivity(),
                                "抱歉，联系客服出现了错误~. error:" + ret,
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.layoutLookAll://查看所有订单
                    intent = new Intent(mActivity, MyOrderManage.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("position", 0);
                    intent.putExtras(bundle3);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutPayMent://待支付
                    intent = new Intent(mActivity, MyOrderManage.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 0);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSendOut://代发货
                    intent = new Intent(mActivity, MyOrderManage.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("position", 1);
                    intent.putExtras(bundle1);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutReceipt://待收货
                    intent = new Intent(mActivity, MyOrderManage.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("position", 2);
                    intent.putExtras(bundle2);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutService://退货
                    intent = new Intent(mActivity, ReturnGoodsManage.class);
                    mActivity.startActivity(intent);
                    break;
               case R.id.layoutMyWallet://我的钱包
                    ToastUtils.showInfo(mContext,"当前版本暂不支持钱包功能");
                    /*intent = new Intent(mContext, MyWallet.class);
                    mContext.startActivity(intent);*/
                    break;
                case R.id.layoutQuickReplenish://快速补货
                    intent = new Intent(mActivity, QuickReplenish.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutFavorites://收藏夹
                    intent = new Intent(mActivity, Bookmarks.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutReceiverManage://收件人管理
                    intent = new Intent(mActivity, ReceiverManage.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSaleRemind://开卖提醒
                    intent = new Intent(mActivity, SaleRemindActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSystemNotice://系统公告
                    intent = new Intent(mActivity, SystemNotice.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutFeedback://意见反馈
                    intent = new Intent(mActivity, Feedback.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSystemSetting://系统设置
                    intent = new Intent(mActivity, SystemSetting.class);
                    getActivity().startActivityForResult(intent,2);
                    break;
                case R.id.layoutPhoto:
                    selectUploadFile();
                    break;
                case R.id.layoutVolumeList:
                    intent = new Intent(mActivity,VolumeListActivity.class);
                    mActivity.startActivity(intent);
                    break;
            }
        }else{
            intent = new Intent(MainActivity.mainContext,RegisterAccount.class);
            mActivity.startActivity(intent);
            (MainActivity.mainContext).overridePendingTransition(0,
                    android.R.animator.fade_out);
        }
    }

    private void selectUploadFile() {
        Intent intent = new Intent(getActivity(),MyPopupWindow.class);
        startActivityForResult(intent,1);
        getActivity().overridePendingTransition(R.anim.in_bottomtotop,0);
    }

    private boolean isLogin() {
        List<AppInfo> appInfos = getDao().loadAll();
        return appInfos.size() != 0 && appInfos.get(0).getIsLogin() != 0;
    }

    private AppInfoDao getDao(){
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    @Override
    public void showUserInfos(UserInfosResultBean resultBean,String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            saveUserInfos(resultBean.getSuccess());
            SPUtils.setRole(mContext,resultBean.getSuccess().getLvl().getValue());
            if(SPUtils.getRole(mContext).equals("移动上传者")||
                    SPUtils.getRole(mContext).equals("库房")){
                layoutGoPublish.setVisibility(View.VISIBLE);
                layoutSaleRemind.setVisibility(View.GONE);
            }else{
                layoutSaleRemind.setVisibility(View.VISIBLE);
                layoutGoPublish.setVisibility(View.GONE);
            }
            if(resultBean.getSuccess().getName()!= null && !resultBean.getSuccess().getName().isEmpty()){
                tvNickName.setText(resultBean.getSuccess().getName());
            }else{
                tvNickName.setText(resultBean.getSuccess().getTel());
            }
            if(resultBean.getSuccess().getHeadImg()!=null){
                GlideUtils.glidNormle(mContext,imgPhotos,resultBean.getSuccess().getHeadImg());
            }else{
                imgPhotos.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_photo));
            }
        }
    }

    private void saveUserInfos(UserInfosResultBean.SuccessBean successBean) {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                .build();
        List<UserInfos> users = nQuery.list();
        UserInfos userInfos =  new UserInfos(users.get(0).getId(),successBean.getId(),
                users.get(0).getPhoneNumber(), users.get(0).getPassWd(),successBean.getHeadImg(),
                users.get(0).getIsAutoLogin(),successBean.getName(),users.get(0).getGender(),
                users.get(0).getRealName());
        getUserDao().insertOrReplace(userInfos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
            String url = data.getStringExtra("path");
            GlideUtils.glidNormle(mContext,imgPhotos,url);
            Query<UserInfos> nQuery = getUserDao().queryBuilder()
                    .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                    .build();
            List<UserInfos> users = nQuery.list();
            UserInfos userInfos =  new UserInfos(users.get(0).getId(),users.get(0).getUserId(),
                    users.get(0).getPhoneNumber(), users.get(0).getPassWd(), url,
                    users.get(0).getIsAutoLogin(),users.get(0).getNickName(),users.get(0).getGender(),
                    users.get(0).getRealName());
            getUserDao().insertOrReplace(userInfos);
        }else if(requestCode==2&&data!=null){
            String path = data.getStringExtra("path");
            GlideUtils.glidNormleFast(mActivity,
                    imgPhotos,path);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLogin()) {
            if (userInfosPre != null) {
                userInfosPre.getUserInfos(getActivity());
            } else {
                userInfosPre = new UserInfosPre(this);
                userInfosPre.getUserInfos(getActivity());
            }
        }
    }
}
