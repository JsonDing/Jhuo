package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.InfoBean;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.bean.NoticeBean;
import com.yunma.bean.RemindNumbersBean;
import com.yunma.dao.AgentList;
import com.yunma.dao.AppInfo;
import com.yunma.dao.BrowsingHistory;
import com.yunma.dao.ConsigneeAddress;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.SystemNotices;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.AgentListDao;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.BrowsingHistoryDao;
import com.yunma.greendao.ConsigneeAddressDao;
import com.yunma.greendao.SystemNoticesDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.activity.ContactUsActivity;
import com.yunma.jhuo.activity.IntroCodeActivity;
import com.yunma.jhuo.activity.KnowMoreGradeActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.MyGradeActivity;
import com.yunma.jhuo.activity.MyIntegralActivity;
import com.yunma.jhuo.activity.MyReturnActivity;
import com.yunma.jhuo.activity.NearByUserActivity;
import com.yunma.jhuo.activity.VolumeListActivity;
import com.yunma.jhuo.activity.homepage.BrowerRecordActivity;
import com.yunma.jhuo.activity.mine.AddressManagerActivity;
import com.yunma.jhuo.activity.mine.MyFavoriteActivity;
import com.yunma.jhuo.activity.mine.MyOrderManage;
import com.yunma.jhuo.activity.mine.MyPopupWindow;
import com.yunma.jhuo.activity.mine.QuickReplenishActivity;
import com.yunma.jhuo.activity.mine.ReturnGoodsManage;
import com.yunma.jhuo.activity.mine.SystemNoticeActivity;
import com.yunma.jhuo.activity.mine.SystemSettingActivity;
import com.yunma.jhuo.general.DialogStyleLoginActivity;
import com.yunma.jhuo.m.GetNoticeInterFace;
import com.yunma.jhuo.m.LoginInterface;
import com.yunma.jhuo.m.NumberRemindInterface;
import com.yunma.jhuo.p.LoginPre;
import com.yunma.jhuo.p.NoticePre;
import com.yunma.jhuo.p.NumberRemindPre;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.Typefaces;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.CircleImageView;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yunma.dao.GreenDaoUtils.getAgentListDao;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class MineFragment extends Fragment implements NumberRemindInterface.NumberRemindView,
        GetNoticeInterFace.NoticeView ,LoginInterface.LoginView {

    @BindView(R.id.layouStatusBar) View layouStatusBar;
    @BindView(R.id.imgPhotos) CircleImageView imgPhotos;
    @BindView(R.id.layoutLookAll) LinearLayout layoutLookAll;
    @BindView(R.id.layoutPayMent) LinearLayout layoutPayMent;
    @BindView(R.id.layoutSendOut) LinearLayout layoutSendOut;
    @BindView(R.id.layoutReceipt) LinearLayout layoutReceipt;
    @BindView(R.id.layoutQuickReplenish) LinearLayout layoutQuickReplenish;
    @BindView(R.id.layoutFavorites) LinearLayout layoutFavorites;
    @BindView(R.id.layoutReceiverManage) LinearLayout layoutReceiverManage;
    @BindView(R.id.layoutSystemNotice) LinearLayout layoutSystemNotice;
    @BindView(R.id.layoutVolumeList) LinearLayout layoutVolumeList;
    @BindView(R.id.layoutSystemSetting) LinearLayout layoutSystemSetting;
    @BindView(R.id.layouttvFootprint) LinearLayout layouttvFootprint;
    @BindView(R.id.layoutService) LinearLayout layoutService;
    @BindView(R.id.tvNickName) TextView tvNickName;
    @BindView(R.id.tvUnPayNums) TextView tvUnPayNums;
    @BindView(R.id.tvWaitSendNums) TextView tvWaitSendNums;
    @BindView(R.id.tvWaitReciveNums) TextView tvWaitReciveNums;
    @BindView(R.id.tvNoticeNums) TextView tvNoticeNums;
    @BindView(R.id.tvCollectNums) TextView tvCollectNums;
    @BindView(R.id.tvFootprint) TextView tvFootprint;
    @BindView(R.id.tvVolumeList) TextView tvVolumeList;
    @BindView(R.id.layoutMessage) View layoutMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.myIntegral) View myIntegral;
    @BindView(R.id.layoutIntroCode) View layoutIntroCode;
    @BindView(R.id.tvAddrNums) TextView tvAddrNums;
    @BindView(R.id.tvMemberLevel) TextView tvMemberLevel;
    @BindView(R.id.loAskFriends) LinearLayout loAskFriends;
    @BindView(R.id.loMyGrade) LinearLayout loMyGrade;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mActivity = getActivity();
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(mActivity), 0, 0);
        tvMemberLevel.setTypeface(Typefaces.get(mActivity, "BaskerVille.ttf"));
        /* 获取系统公告 */
        NoticePre noticePre = new NoticePre(MineFragment.this);
        noticePre.getNotices(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.mainActivity.isLogin()) {
            if (MainActivity.mainActivity.isLogin()) {
                isShowNumberRemind();
                refreshUserInfo();
                refreshSystemNotice();
                refreshBrowseRecord();
                refreshConsigneeAddress();
                LoginPre loginPre = new LoginPre(MineFragment.this);
                loginPre.login(mActivity,SPUtils.getPhoneNumber(getActivity()),
                        SPUtils.getPassWd(getActivity()));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (MainActivity.mainActivity.isLogin()) {
            isShowNumberRemind();
            refreshUserInfo();
            refreshSystemNotice();
            refreshBrowseRecord();
            LoginPre loginPre = new LoginPre(MineFragment.this);
            loginPre.login(mActivity,SPUtils.getPhoneNumber(getActivity()),
                    SPUtils.getPassWd(getActivity()));
        }
    }

    /**
     * 是否显示数字条目提示
     */
    private void isShowNumberRemind() {
        if (SPUtils.IsShowNumberRemind(getActivity())) {
            if (!SPUtils.getUserId(mActivity).equals("J·货")) {
                NumberRemindPre numberRemindPre = new NumberRemindPre(MineFragment.this);
                numberRemindPre.getRemindNums(mActivity);
            }
        } else {
            tvUnPayNums.setVisibility(View.INVISIBLE);
            tvWaitSendNums.setVisibility(View.INVISIBLE);
            tvWaitReciveNums.setVisibility(View.INVISIBLE);
            tvNoticeNums.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        if (!SPUtils.getUserId(mActivity).equals("J·货")) {
            UserInfos userInfos = getUserDao().load(Long.valueOf(SPUtils.getUserId(mActivity)));
            if (userInfos != null) {
                if (userInfos.getNickName() != null) {
                    tvNickName.setText(userInfos.getNickName());
                } else {
                    tvNickName.setText(userInfos.getPhoneNumber());
                }
                if (userInfos.getImgsPhotos() != null && !userInfos.getImgsPhotos().isEmpty()) {
                    GlideUtils.glidNormle(mActivity, imgPhotos,
                            ConUtils.HEAD_IMAGE_URL + userInfos.getImgsPhotos());
                } else {
                    imgPhotos.setImageDrawable(
                            ContextCompat.getDrawable(mActivity, R.drawable.default_photo));
                }
            }
        }
    }

    /**
     * 刷新未读系统公告
     */
    private void refreshSystemNotice() {
        List<SystemNotices> noticesList = getSystemNoticesDao().queryBuilder()
                .where(SystemNoticesDao.Properties.IsRead.eq("no"))
                .build()
                .list();
        int size = noticesList.size();
        if (size != 0) {
            tvNoticeNums.setVisibility(View.VISIBLE);
            tvNoticeNums.setText(String.valueOf(size));
        } else {
            tvNoticeNums.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新浏览记录
     */
    private void refreshBrowseRecord() {
        List<BrowsingHistory> bhList = getBrowsingHistoryDao()
                .queryBuilder()
                .where(BrowsingHistoryDao.Properties.Tel.eq(
                        SPUtils.getPhoneNumber(getActivity())))
                .list();
        if (bhList != null) {
            tvFootprint.setText(String.valueOf(bhList.size()));
        } else {
            tvFootprint.setText("0");
        }
    }

    /**
     * 刷新收件人条目数
     */
    private void refreshConsigneeAddress() {
        List<ConsigneeAddress> addrList = getAddressDao().loadAll();
        if (addrList.size() != 0) {
            tvAddrNums.setVisibility(View.VISIBLE);
            tvAddrNums.setText(String.valueOf(addrList.size()));
        } else {
            tvAddrNums.setVisibility(View.INVISIBLE);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            String url = data.getStringExtra("path");
            GlideUtils.glidNormle(mActivity, imgPhotos, ConUtils.HEAD_IMAGE_URL + url);
            Query<UserInfos> nQuery = getUserDao().queryBuilder()
                    .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mActivity)))
                    .build();
            List<UserInfos> users = nQuery.list();
            UserInfos userInfos = users.get(0);
            userInfos.setImgsPhotos(ConUtils.HEAD_IMAGE_URL + url);
            getUserDao().insertOrReplace(userInfos);
        } else if (requestCode == 2 && data != null) {
            String path = data.getStringExtra("path");
            GlideUtils.glidNormleFast(mActivity,
                    imgPhotos, ConUtils.HEAD_IMAGE_URL + path);
        }
    }

    @Override
    public void showNumberRemindResult(RemindNumbersBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getActivity(), msg);
        } else {
            RemindNumbersBean.SuccessBean remindNums = resultBean.getSuccess();
            if (remindNums.getUnpay() != 0) {
                tvUnPayNums.setVisibility(View.VISIBLE);
                tvUnPayNums.setText(String.valueOf(remindNums.getUnpay()));
            } else {
                tvUnPayNums.setVisibility(View.INVISIBLE);
            }
            if (remindNums.getUnsend() != 0) {
                tvWaitSendNums.setVisibility(View.VISIBLE);
                tvWaitSendNums.setText(String.valueOf(remindNums.getUnsend()));
            } else {
                tvWaitSendNums.setVisibility(View.INVISIBLE);
            }
            if (remindNums.getSend() != 0) {
                tvWaitReciveNums.setVisibility(View.VISIBLE);
                tvWaitReciveNums.setText(String.valueOf(remindNums.getSend()));
            } else {
                tvWaitReciveNums.setVisibility(View.INVISIBLE);
            }
            if (remindNums.getCoupon() != 0) {
                tvVolumeList.setText(String.valueOf(remindNums.getCoupon()));
            } else {
                tvVolumeList.setText("0");
            }
            if (remindNums.getCollect() != 0) {
                tvCollectNums.setText(String.valueOf(remindNums.getCollect()));
            } else {
                tvCollectNums.setText("0");
            }
        }
    }

    @OnClick({R.id.myIntegral, R.id.layoutNearBy, R.id.layoutIntroCode, R.id.layouttvFootprint,
            R.id.imgPhotos, R.id.layoutLookAll, R.id.layoutPayMent, R.id.layoutSendOut,
            R.id.layoutReceipt, R.id.layoutService, R.id.layoutVolumeList, R.id.layoutQuickReplenish,
            R.id.layoutFavorites, R.id.layoutReceiverManage, R.id.layoutSystemNotice,
            R.id.layoutSystemSetting,R.id.loAskFriends,R.id.loMyGrade,R.id.loPromoteGrade,
            R.id.loMyMoneyReturn})
    public void onViewClicked(View view) {
        if (MainActivity.mainActivity.isLogin()){
            Intent intent;
            Bundle bundle;
            switch (view.getId()) {
                case R.id.myIntegral: /* 积分 */
                    intent = new Intent(mActivity, MyIntegralActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutNearBy: /* 雷达附近的人 */
                    intent = new Intent(mActivity, NearByUserActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutIntroCode: /* 邀请码 */
                    intent = new Intent(mActivity, IntroCodeActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.loAskFriends: /* 邀请好友 */
                    intent = new Intent(mActivity, IntroCodeActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layouttvFootprint: /* 流览足迹 */
                    intent = new Intent(mActivity, BrowerRecordActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.imgPhotos: /* 切换头像 */
                    intent = new Intent(getActivity(), MyPopupWindow.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.in_bottomtotop, 0);
                    break;
                case R.id.layoutLookAll: /* 查看全部订单 */
                    intent = new Intent(mActivity, MyOrderManage.class);
                    bundle = new Bundle();
                    bundle.putInt("position", 0);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutPayMent: /* 待付款订单 */
                    intent = new Intent(mActivity, MyOrderManage.class);
                    bundle = new Bundle();
                    bundle.putInt("position", 0);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSendOut: /* 待发货订单 */
                    intent = new Intent(mActivity, MyOrderManage.class);
                    bundle = new Bundle();
                    bundle.putInt("position", 1);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutReceipt: /* 待收货订单 */
                    intent = new Intent(mActivity, MyOrderManage.class);
                    bundle = new Bundle();
                    bundle.putInt("position", 2);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutService: /* 售后服务 */
                    intent = new Intent(mActivity, ReturnGoodsManage.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutVolumeList: /* 优惠券 */
                    intent = new Intent(mActivity, VolumeListActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutQuickReplenish: /* 快速补货 */
                    intent = new Intent(mActivity, QuickReplenishActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutFavorites: /* 收藏夹 */
                    intent = new Intent(mActivity, MyFavoriteActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutReceiverManage: /*  收件人管理 */
                    intent = new Intent(mActivity, AddressManagerActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSystemNotice: /* 系统公告 */
                    intent = new Intent(mActivity, SystemNoticeActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.layoutSystemSetting: /* 系统设置 */
                    intent = new Intent(mActivity, SystemSettingActivity.class);
                    mActivity.startActivityForResult(intent, 2);
                    break;
                case R.id.loMyGrade: /* 我的等级 */
                    intent = new Intent(mActivity, MyGradeActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.loPromoteGrade:  /* 等级提升 */
                    long myAgent = SPUtils.getAgentId(mActivity);
                    long agentId = getMyNextAgent(myAgent);
                    intent = new Intent(mActivity, KnowMoreGradeActivity.class);
                    bundle = new Bundle();
                    bundle.putLong("agentId",agentId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.loMyMoneyReturn:
                    if(SPUtils.isAgent(mActivity)){
                        intent = new Intent(mActivity, MyReturnActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.showWarning(mActivity,"您当前尚未成为代理");
                    }
                    break;
            }
        } else {
            showRegistOrLogin();
        }
    }

    private long getMyNextAgent(long myAgent) {
        List<AgentList> agentList = getAgentListDao().queryBuilder()
                .orderDesc(AgentListDao.Properties.Discount)
                .build()
                .list();
        int currPos = 0;
        for (int i = 0;i < agentList.size();i++){
            if (myAgent == agentList.get(i).getId()){
                currPos = i;
                break;
            }
        }
        if (currPos != agentList.size() - 1){
            AgentList nextAgent = agentList.get(currPos +1);
            return nextAgent.getId();
        } else {
            return myAgent;
        }
    }


    /**
     * 消息提醒
     */
    @OnClick(R.id.layoutMessage)
    public void onLayoutMessage() {
        Intent intent = new Intent(mActivity, ContactUsActivity.class);
        mActivity.startActivity(intent);
    }
    

    /**
     * 注册登录
     */
    private void showRegistOrLogin() {
        Intent intent = new Intent(MainActivity.mainActivity, DialogStyleLoginActivity.class);
        mActivity.startActivity(intent);
        (MainActivity.mainActivity).overridePendingTransition(0,
                R.anim.fade_out);
    }

    private BrowsingHistoryDao getBrowsingHistoryDao() {
        return GreenDaoManager.getInstance().getSession().getBrowsingHistoryDao();
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    private ConsigneeAddressDao getAddressDao() {
        return GreenDaoManager.getInstance().getSession().getConsigneeAddressDao();
    }

    @Override
    public void showNoticeInfo(NoticeBean noticeBean, String msg) {
        if (noticeBean != null) {
            int size = noticeBean.getSuccess().getList().size();
            if (size != 0) {
                List<NoticeBean.SuccessBean.ListBean> listBean = noticeBean.getSuccess().getList();
                List<SystemNotices> newSystemNotices = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    String tittle = listBean.get(i).getTitle();
                    //查询该条公告是否存在
                    Query<SystemNotices> nQuery = getSystemNoticesDao().queryBuilder()
                            .where(SystemNoticesDao.Properties.Tittle.eq(tittle))
                            .build();
                    List<SystemNotices> notices = nQuery.list();
                    //如果没有，添加
                    if (notices.size() == 0) {
                        NoticeBean.SuccessBean.ListBean bean = listBean.get(i);
                        SystemNotices systemNotices = new SystemNotices(
                                null, bean.getTitle(), bean.getContent(), bean.getDate(), bean.getAuthor(), "no");
                        newSystemNotices.add(systemNotices);
                    } else {
                        SystemNotices data = notices.get(0);
                        SystemNotices systemNotices = new SystemNotices(
                                null, data.getTittle(), data.getContent(), data.getTime(), data.getPublisher(), data.getIsRead());
                        newSystemNotices.add(systemNotices);
                    }
                }
                getSystemNoticesDao().deleteAll();
                saveNLists(newSystemNotices);
                Query<SystemNotices> nQuery = getSystemNoticesDao().queryBuilder()
                        .where(SystemNoticesDao.Properties.IsRead.eq("no"))
                        .build();
                List<SystemNotices> notices = nQuery.list();
                if (notices.size() != 0) {
                    tvNoticeNums.setVisibility(View.VISIBLE);
                    tvNoticeNums.setText(String.valueOf(notices.size()));
                } else {
                    tvNoticeNums.setVisibility(View.INVISIBLE);
                }

            }

        }
    }

    private SystemNoticesDao getSystemNoticesDao() {
        return GreenDaoManager.getInstance().getSession().getSystemNoticesDao();
    }

    /**
     * 批量插入信息
     *
     * @param list 系统公告列表
     */
    public void saveNLists(final List<SystemNotices> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getSystemNoticesDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    SystemNotices systemNotices = list.get(i);
                    getSystemNoticesDao().insertOrReplace(systemNotices);
                }
            }
        });
    }

    @Override
    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
        if (resultBean != null) {
            String token = resultBean.getSuccess().getToken();
            saveSharePreferences(token,resultBean.getSuccess());
            updateAppInfos(token);
            updateUserInfos(resultBean.getSuccess().getInfo());
            int myAgentId = SPUtils.getAgentId(getActivity());
            AgentList myAgent = getAgentListDao().load((long) myAgentId);
            if (myAgent != null) {
                tvMemberLevel.setVisibility(View.VISIBLE);
                tvMemberLevel.setText(
                        String.valueOf("V" + myAgent.getGrade() + " " +myAgent.getName()));
            } else {
                tvMemberLevel.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void saveSharePreferences(String token, LoginSuccessResultBean.SuccessBean useInfo) {
        SPUtils.setToken(mActivity, token);
        InfoBean.LvlBean mLvl = useInfo.getInfo().getLvl();
        if (mLvl != null){
            SPUtils.setRole(mActivity, mLvl.getValue()); // 角色：运营、移动用户、、、
            SPUtils.setRoleId(mActivity,mLvl.getId()); // 角色ID
        }
        SPUtils.setAgentId(mActivity,useInfo.getAgent().getId());
        SPUtils.setAgentDiscount(mActivity,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getDiscount()));
        SPUtils.setAgentName(mActivity,useInfo.getAgent().getName());
        String mNick = useInfo.getAgent().getNick();
        if (mNick != null){
            SPUtils.setAgentNick(mActivity,useInfo.getAgent().getNick());
        }else {
            SPUtils.setAgentNick(mActivity,"");
        }
        SPUtils.setParentDiscount(mActivity,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getParentDiscount()));
        SPUtils.setRootDiscount(mActivity,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getRootDiscount()));
        SPUtils.setAgentPoints(mActivity,useInfo.getAgent().getPoints());
        SPUtils.setUserId(mActivity,String.valueOf(useInfo.getInfo().getId()));
        SPUtils.setIntegral(mActivity,useInfo.getInfo().getPoints());
        int isAgent = useInfo.getInfo().getIsAgent();
        if (isAgent == 0){
            SPUtils.setIsAnget(mActivity,false);
        } else {
            SPUtils.setIsAnget(mActivity,true);
        }
    }

    private void updateAppInfos(String token) {
        LogUtils.test("trace: " + 3);
        AppInfoDao appInfoDao = GreenDaoManager.getInstance().getSession().getAppInfoDao();
        List<AppInfo> appInfos = appInfoDao.loadAll();
        AppInfo infos = new AppInfo(appInfos.get(0).getId(),1,1,
                token,appInfos.get(0).getIsFirstSetting());
        appInfoDao.save(infos);
    }

    private void updateUserInfos(InfoBean useInfo) {
        UserInfos userInfos = getUserDao().load(Long.valueOf(String.valueOf(useInfo.getId())));
        UserInfos ui=  new UserInfos();
        ui.setId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setUserId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setPhoneNumber(useInfo.getTel());
        ui.setPassWd(useInfo.getPass());
        ui.setImgsPhotos(useInfo.getHeadImg());
        ui.setNickName(useInfo.getName());
        ui.setIsAutoLogin(userInfos.getIsAutoLogin());
        ui.setGender(userInfos.getGender());
        ui.setRealName(useInfo.getUser());
        ui.setQq(useInfo.getQq());
        if (useInfo.getLvl() != null){
            ui.setRoleName(useInfo.getLvl().getValue());
            ui.setRoleId(useInfo.getLvl().getId());
        }
        ui.setPoints(useInfo.getPoints());
        getUserDao().save(ui);
    }
}
