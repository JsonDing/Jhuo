package com.yunma.jhuo.activity;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.hyphenate.*;
import com.hyphenate.chat.*;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.yunma.R;
import com.yunma.emchat.Constant;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.db.InviteMessgeDao;
import com.yunma.emchat.runtimepermissions.PermissionsManager;
import com.yunma.emchat.ui.ChatActivity;
import com.yunma.emchat.ui.GroupsActivity;
import com.yunma.jhuo.fragment.*;
import com.yunma.jhuo.general.LoginActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends MyCompatActivity {
    @BindView(R.id.layNavigationBar) LinearLayout layNavigationBar;
    @BindView(R.id.content) FrameLayout content;
    @BindView(R.id.imgHome) ImageView imgHome;
    @BindView(R.id.tvHome) TextView tvHome;
    @BindView(R.id.layoutHome) RelativeLayout layoutHome;
    @BindView(R.id.imgStorage) ImageView imgStorage;
    @BindView(R.id.tvStorage) TextView tvStorage;
    @BindView(R.id.layoutStorage) RelativeLayout layoutStorage;
    @BindView(R.id.imgBasket) ImageView imgBasket;
    @BindView(R.id.tvBasket) TextView tvBasket;
    @BindView(R.id.layoutBasket) RelativeLayout layoutBasket;
    @BindView(R.id.imgMine) ImageView imgMine;
    @BindView(R.id.tvMine) TextView tvMine;
    @BindView(R.id.layoutMine) RelativeLayout layoutMine;
    @BindView(R.id.mLyout) RelativeLayout mLyout;
    private HomepageFragment homePageFragment = null;
    private StorageFragment storageFragment = null;
    private BasketFragment basketFragment = null;
    private MineFragment mineFragment = null;
    private FragmentManager fragmentManager = null;
    private int currentPosition;
    private InviteMessgeDao inviteMessgeDao;
    public static MainActivity mainContext;
    public boolean isConflict = false;
    private boolean isCurrentAccountRemoved = false;
    private AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private static int[] colorId = new int[]{R.color.color_b2,R.color.color_b3};
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBar();
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        showExceptionDialogFromIntent(getIntent());
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        registerInternalDebugReceiver();
    }

    private void initBar() {
        mainContext = this;
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) mLyout.getLayoutParams();
        fl.setMargins(0, SPUtils.getStatusHeight(this), 0,0);
        mLyout.setLayoutParams(fl);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        inviteMessgeDao = new InviteMessgeDao(this);
        boolean netConnect = this.isNetConnect();
        if (!netConnect){
            NiftyNotificationView.build(this,"当前网络不可用",
                    Effects.slideIn,R.id.mLyout,NetUtil.initConf())
                    .setIcon(R.mipmap.logo)
                    .show();
        }
    }


    private void setTabSelection(int type) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (type) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment = new HomepageFragment();
                    transaction.add(R.id.content, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                currentPosition = 0;
                tvHome.setTextColor(getResources().getColor(colorId[1]));
                imgHome.setImageDrawable(getResources().getDrawable(R.drawable.tab_home_s));
                break;
            case 1:
                if (storageFragment == null) {
                    storageFragment = new StorageFragment();
                    transaction.add(R.id.content, storageFragment);
                } else {
                    transaction.show(storageFragment);
                }
                currentPosition = 1;
                tvStorage.setTextColor(getResources().getColor(colorId[1]));
                imgStorage.setImageDrawable(getResources().getDrawable(R.drawable.tab_entrepot_s));
                break;
            case 2:
                if (basketFragment == null) {
                    basketFragment = new BasketFragment();
                    transaction.add(R.id.content, basketFragment);
                } else {
                    transaction.show(basketFragment);
                }
                currentPosition = 2;
                tvBasket.setTextColor(getResources().getColor(colorId[1]));
                imgBasket.setImageDrawable(getResources().getDrawable(R.drawable.tab_shopping_s));
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                currentPosition = 3;
                tvMine.setTextColor(getResources().getColor(colorId[1]));
                imgMine.setImageDrawable(getResources().getDrawable(R.drawable.tab_me_s));
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (storageFragment != null) {
            transaction.hide(storageFragment);
        }
        if (basketFragment != null) {
            transaction.hide(basketFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    private void clearSelection() {
        tvHome.setTextColor(getResources().getColor(colorId[0]));
        tvStorage.setTextColor(getResources().getColor(colorId[0]));
        tvBasket.setTextColor(getResources().getColor(colorId[0]));
        tvMine.setTextColor(getResources().getColor(colorId[0]));
        imgHome.setImageDrawable(getResources().getDrawable(R.drawable.tab_home));
        imgStorage.setImageDrawable(getResources().getDrawable(R.drawable.tab_entrepot));
        imgBasket.setImageDrawable(getResources().getDrawable(R.drawable.tab_shopping));
        imgMine.setImageDrawable(getResources().getDrawable(R.drawable.tab_me));
    }

    @OnClick({R.id.layoutHome, R.id.layoutStorage,
            R.id.layoutBasket, R.id.layoutMine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutHome:
                if(currentPosition!=0)
                setTabSelection(0);
                break;
            case R.id.layoutStorage:
                if(currentPosition!=1)
                setTabSelection(1);
                break;
            case R.id.layoutBasket:
                if(currentPosition!=2)
                setTabSelection(2);
                break;
            case R.id.layoutMine:
                if(currentPosition!=3)
                setTabSelection(3);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定退出？")
                    .setConfirmText("退出")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            EMClient.getInstance().logout(true, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.log("EMchat 退出成功！");
                                    GlideUtils.glidClearDisk(MainActivity.this);
                                    AppManager.getAppManager().finishAllActivity();
                                    finish();
                                    System.exit(0);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }

                                @Override
                                public void onError(int i, String s) {
                                    LogUtils.log("EMchat 退出失败！");
                                    GlideUtils.glidClearDisk(MainActivity.this);
                                    AppManager.getAppManager().finishAllActivity();
                                    finish();
                                    System.exit(0);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return false;
    }



    @Override
    public void onNetChange(int netMobile) {
        LogUtils.log("status ----> " + netMobile);
        if (netMobile == NetUtil.NETWORK_WIFI) { //WIFI网络
            NiftyNotificationView.build(this,"WIFI已连接",
                    Effects.slideOnTop,R.id.mLyout,NetUtil.initConf())
                    .setIcon(R.drawable.logo_sm)
                    .show();
        } else if (netMobile == NetUtil.NETWORK_MOBILE) { // 移动网络
            NiftyNotificationView.build(this,"网络已连接",
                    Effects.thumbSlider,R.id.mLyout,NetUtil.initConf())
                    .setIcon(R.drawable.logo_sm)
                    .show();
        } else if (netMobile == NetUtil.NETWORK_NONE) { // 没有连接网络
            NiftyNotificationView.build(this,"当前网络不可用",
                    Effects.slideIn,R.id.mLyout,NetUtil.initConf())
                    .setIcon(R.drawable.logo_sm)
                    .show();
        }
        super.onNetChange(netMobile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(MainActivity.this);
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
            LogUtils.log("MainActivity: " + e.getMessage());
        }
    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                updateUnreadLabel();
            }
        });
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            if (currentPosition == 0) {
                if (homePageFragment != null) {
                    homePageFragment.refresh();
                }
            }else if (currentPosition == 1) {
                if (storageFragment != null) {
                    storageFragment.refresh();
                }
            }
        }
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    if (currentPosition == 0) {
                        if (homePageFragment != null) {
                            homePageFragment.refresh();
                        }
                    }else if (currentPosition == 1) {
                        if (storageFragment != null) {
                            storageFragment.refresh();
                        }
                    }
                }
            }
        });
    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }

    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                LogUtils.log("---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentPosition == 0) {
                    if (homePageFragment != null) {
                        homePageFragment.refresh();
                    }
                } else if (currentPosition == 1) {
                    if (storageFragment != null) {
                        storageFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {

        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

}
