package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.yunma.R;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.DemoModel;
import com.yunma.emchat.ui.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import butterknife.*;

import static com.yunma.R.id.*;

public class EmSettingActivity extends MyCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(com.yunma.R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(switch_notification) Switch switchNotification;
    @BindView(R.id.rl_switch_notification) RelativeLayout rlSwitchNotification;
    @BindView(R.id.switch_sound) Switch switchSound;
    @BindView(R.id.rl_switch_sound) RelativeLayout rlSwitchSound;
    @BindView(R.id.switch_vibrate) Switch switchVibrate;
    @BindView(R.id.switch_speaker) Switch switchSpeaker;
    @BindView(switch_delete_msg_when_exit_group) Switch switchDeleteMsgWhenExitGroup;
    @BindView(switch_auto_accept_group_invitation) Switch switchAutoAcceptGroupInvitation;
    @BindView(switch_adaptive_video_encode) Switch switchAdaptiveVideoEncode;
    @BindView(R.id.rl_push_settings) View rl_push_settings;
    @BindView(R.id.rl_switch_vibrate) RelativeLayout rlSwitchVibrate;
    @BindView(R.id.rl_switch_speaker) RelativeLayout rlSwitchSpeaker;
    @BindView(R.id.ll_user_profile) LinearLayout llUserProfile;
    @BindView(R.id.ll_black_list) LinearLayout llBlackList;
    @BindView(R.id.ll_set_push_nick) LinearLayout llSetPushNick;
    @BindView(R.id.ll_call_option) LinearLayout llCallOption;
    @BindView(R.id.rl_switch_delete_msg_when_exit_group) RelativeLayout rlSwitchDeleteMsgWhenExitGroup;
    @BindView(R.id.rl_switch_auto_accept_group_invitation) RelativeLayout rlSwitchAutoAcceptGroupInvitation;
    @BindView(R.id.rl_switch_adaptive_video_encode) RelativeLayout rlSwitchAdaptiveVideoEncode;
    @BindView(R.id.view1) View view1;
    @BindView(R.id.view2) View view2;
    private Context mContext;
    private DemoModel settingsModel;
    private EMOptions chatOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_setting);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        initView();
    }

    private void initView() {
        settingsModel = DemoHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();
        if (settingsModel.getSettingMsgNotification()) {
            switchNotification.setChecked(true);
            rlSwitchSound.setVisibility(View.VISIBLE);
            rlSwitchVibrate.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            switchNotification.setChecked(false);
            rlSwitchSound.setVisibility(View.GONE);
            rlSwitchVibrate.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
        if (settingsModel.getSettingMsgSound()) {
            switchSound.setChecked(true);
        } else {
            switchSound.setChecked(false);
        }
        if (settingsModel.getSettingMsgVibrate()) {
            switchVibrate.setChecked(true);
        } else {
            switchVibrate.setChecked(false);
        }
        if (settingsModel.getSettingMsgSpeaker()) {
            switchSpeaker.setChecked(true);
        } else {
            switchSpeaker.setChecked(false);
        }
        if(settingsModel.isDeleteMessagesAsExitGroup()){
            switchDeleteMsgWhenExitGroup.setChecked(true);
        } else {
            switchDeleteMsgWhenExitGroup.setChecked(false);
        }
        if (settingsModel.isAutoAcceptGroupInvitation()) {
            switchAutoAcceptGroupInvitation.setChecked(true);
        } else {
            switchAutoAcceptGroupInvitation.setChecked(false);
        }
        if (settingsModel.isAdaptiveVideoEncode()) {
            switchAdaptiveVideoEncode.setChecked(true);
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
        } else {
            switchAdaptiveVideoEncode.setChecked(false);
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
        }
        switchNotification.setOnCheckedChangeListener(this);
        switchSound.setOnCheckedChangeListener(this);
        switchVibrate.setOnCheckedChangeListener(this);
        switchSpeaker.setOnCheckedChangeListener(this);
        switchDeleteMsgWhenExitGroup.setOnCheckedChangeListener(this);
        switchAutoAcceptGroupInvitation.setOnCheckedChangeListener(this);
        switchAdaptiveVideoEncode.setOnCheckedChangeListener(this);
    }

    private void initBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(EmSettingActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.rl_push_settings, R.id.ll_user_profile,
            R.id.ll_black_list, R.id.ll_set_push_nick, R.id.ll_call_option})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.rl_push_settings:
                startActivity(new Intent(EmSettingActivity.this,
                        OfflinePushSettingsActivity.class));
                break;
            case R.id.ll_user_profile:
                startActivity(new Intent(EmSettingActivity.this,
                        UserProfileActivity.class).putExtra("setting", true)
                        .putExtra("username", EMClient.getInstance().getCurrentUser()));
                break;
            case R.id.ll_black_list:
                startActivity(new Intent(EmSettingActivity.this,
                        BlacklistActivity.class));
                break;
            case R.id.ll_set_push_nick:
                startActivity(new Intent(EmSettingActivity.this,
                        OfflinePushNickActivity.class));
                break;
            case R.id.ll_call_option:
                startActivity(new Intent(EmSettingActivity.this,
                        CallOptionActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_notification:
                if(isChecked){
                    ToastUtils.showInfo(mContext,"打开");
                    rlSwitchSound.setVisibility(View.VISIBLE);
                    rlSwitchVibrate.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }else{
                    ToastUtils.showInfo(mContext,"关闭");
                    rlSwitchSound.setVisibility(View.GONE);
                    rlSwitchVibrate.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                }
                break;
            case R.id.switch_sound:
                if(isChecked){
                    ToastUtils.showInfo(mContext,"打开");
                    settingsModel.setSettingMsgSound(true);
                }else{
                    ToastUtils.showInfo(mContext,"关闭");
                    settingsModel.setSettingMsgSound(false);
                }
                break;
            case R.id.switch_vibrate:
                if(isChecked){
                    ToastUtils.showInfo(mContext,"打开");
                    settingsModel.setSettingMsgVibrate(true);
                }else{
                    ToastUtils.showInfo(mContext,"关闭");
                    settingsModel.setSettingMsgVibrate(false);
                }
                break;
            case R.id.switch_speaker:
                if (isChecked) {
                    settingsModel.setSettingMsgSpeaker(true);
                } else {
                    settingsModel.setSettingMsgSpeaker(false);
                }
                break;
            case R.id.switch_delete_msg_when_exit_group:
                if (isChecked) {
                    settingsModel.setDeleteMessagesAsExitGroup(true);
                    chatOptions.setDeleteMessagesAsExitGroup(true);
                } else {
                    settingsModel.setDeleteMessagesAsExitGroup(false);
                    chatOptions.setDeleteMessagesAsExitGroup(false);
                }
                break;
            case R.id.switch_auto_accept_group_invitation:
                if (isChecked) {
                    settingsModel.setAutoAcceptGroupInvitation(true);
                    chatOptions.setAutoAcceptGroupInvitation(true);
                } else {
                    settingsModel.setAutoAcceptGroupInvitation(false);
                    chatOptions.setAutoAcceptGroupInvitation(false);
                }
                break;
            case R.id.switch_adaptive_video_encode:
                if (isChecked) {
                    settingsModel.setAdaptiveVideoEncode(true);
                    EMClient.getInstance().callManager()
                            .getCallOptions().enableFixedVideoResolution(false);
                } else {
                    settingsModel.setAdaptiveVideoEncode(false);
                    EMClient.getInstance().callManager()
                            .getCallOptions().enableFixedVideoResolution(true);
                }
                break;

        }
    }

}
