package com.yunma.emchat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;
import com.yunma.jhuo.general.LoginActivity;
import com.yunma.R;
import com.yunma.emchat.*;

/**
 * settings screen
 * 
 * 
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class SettingsFragment extends Fragment implements OnClickListener {

	private RelativeLayout rl_switch_notification;
	private RelativeLayout rl_switch_sound;
	private RelativeLayout rl_switch_vibrate;
	private RelativeLayout rl_switch_speaker;
	private TextView textview1, textview2;
	private LinearLayout blacklistContainer;
	private LinearLayout userProfileContainer;
	private Button logoutBtn;
    private RelativeLayout rl_switch_delete_msg_when_exit_group;
    private RelativeLayout rl_switch_auto_accept_group_invitation;
    private RelativeLayout rl_switch_adaptive_video_encode;
	RelativeLayout rl_push_settings;
	private LinearLayout   ll_call_option;
	private LinearLayout pushNick;
    private EaseSwitchButton notifySwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private EaseSwitchButton switch_delete_msg_when_exit_group;
    private EaseSwitchButton switch_auto_accept_group_invitation;
    private EaseSwitchButton switch_adaptive_video_encode;
    private DemoModel settingsModel;
    private EMOptions chatOptions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.em_fragment_conversation_settings, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		rl_switch_notification = (RelativeLayout) getView().findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) getView().findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) getView().findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) getView().findViewById(R.id.rl_switch_speaker);
		rl_switch_delete_msg_when_exit_group = (RelativeLayout) getView().findViewById(R.id.rl_switch_delete_msg_when_exit_group);
		rl_switch_auto_accept_group_invitation = (RelativeLayout) getView().findViewById(R.id.rl_switch_auto_accept_group_invitation);
		rl_switch_adaptive_video_encode = (RelativeLayout) getView().findViewById(R.id.rl_switch_adaptive_video_encode);
		rl_push_settings = (RelativeLayout) getView().findViewById(R.id.rl_push_settings);
		ll_call_option = (LinearLayout) getView().findViewById(R.id.ll_call_option);
		notifySwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_notification);
		soundSwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_sound);
		vibrateSwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_vibrate);
		speakerSwitch = (EaseSwitchButton) getView().findViewById(R.id.switch_speaker);
		switch_delete_msg_when_exit_group = (EaseSwitchButton) getView().findViewById(R.id.switch_delete_msg_when_exit_group);
		switch_auto_accept_group_invitation = (EaseSwitchButton) getView().findViewById(R.id.switch_auto_accept_group_invitation);
		switch_adaptive_video_encode = (EaseSwitchButton) getView().findViewById(R.id.switch_adaptive_video_encode);
		logoutBtn = (Button) getView().findViewById(R.id.btn_logout);

		if(!TextUtils.isEmpty(EMClient.getInstance().getCurrentUser())){
			logoutBtn.setText(getString(R.string.button_logout) + "(" + EMClient.getInstance().getCurrentUser() + ")");
		}
		textview1 = (TextView) getView().findViewById(R.id.textview1);
		textview2 = (TextView) getView().findViewById(R.id.textview2);
		blacklistContainer = (LinearLayout) getView().findViewById(R.id.ll_black_list);
		userProfileContainer = (LinearLayout) getView().findViewById(R.id.ll_user_profile);
		pushNick=(LinearLayout) getView().findViewById(R.id.ll_set_push_nick);

		settingsModel = DemoHelper.getInstance().getModel();
		chatOptions = EMClient.getInstance().getOptions();
		
		blacklistContainer.setOnClickListener(this);
		userProfileContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		pushNick.setOnClickListener(this);
		rl_switch_delete_msg_when_exit_group.setOnClickListener(this);
		rl_switch_auto_accept_group_invitation.setOnClickListener(this);
		rl_switch_adaptive_video_encode.setOnClickListener(this);
		rl_push_settings.setOnClickListener(this);
		ll_call_option.setOnClickListener(this);

		if (settingsModel.getSettingMsgNotification()) {
			notifySwitch.openSwitch();
		} else {
		    notifySwitch.closeSwitch();
		}

		if (settingsModel.getSettingMsgSound()) {
		    soundSwitch.openSwitch();
		} else {
		    soundSwitch.closeSwitch();
		}

		// vibrate notification is switched on or not?
		if (settingsModel.getSettingMsgVibrate()) {
		    vibrateSwitch.openSwitch();
		} else {
		    vibrateSwitch.closeSwitch();
		}

		// the speaker is switched on or not?
		if (settingsModel.getSettingMsgSpeaker()) {
		    speakerSwitch.openSwitch();
		} else {
		    speakerSwitch.closeSwitch();
		}
		// delete messages when exit group?
		if(settingsModel.isDeleteMessagesAsExitGroup()){
		    switch_delete_msg_when_exit_group.openSwitch();
		} else {
		    switch_delete_msg_when_exit_group.closeSwitch();
		}
		
		if (settingsModel.isAutoAcceptGroupInvitation()) {
		    switch_auto_accept_group_invitation.openSwitch();
		} else {
		    switch_auto_accept_group_invitation.closeSwitch();
		}
		
		if (settingsModel.isAdaptiveVideoEncode()) {
            switch_adaptive_video_encode.openSwitch();
			EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
        } else {
            switch_adaptive_video_encode.closeSwitch();
			EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
        }

		/*if (settingsModel.isCustomAppkeyEnabled()) {
		} else {
		}*/
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_switch_notification:
				if (notifySwitch.isSwitchOpen()) {
					notifySwitch.closeSwitch();
					rl_switch_sound.setVisibility(View.GONE);
					rl_switch_vibrate.setVisibility(View.GONE);
					textview1.setVisibility(View.GONE);
					textview2.setVisibility(View.GONE);
					settingsModel.setSettingMsgNotification(false);
				} else {
					notifySwitch.openSwitch();
					rl_switch_sound.setVisibility(View.VISIBLE);
					rl_switch_vibrate.setVisibility(View.VISIBLE);
					textview1.setVisibility(View.VISIBLE);
					textview2.setVisibility(View.VISIBLE);
					settingsModel.setSettingMsgNotification(true);
				}
				break;
			case R.id.rl_switch_sound:
				if (soundSwitch.isSwitchOpen()) {
					soundSwitch.closeSwitch();
					settingsModel.setSettingMsgSound(false);
				} else {
					soundSwitch.openSwitch();
					settingsModel.setSettingMsgSound(true);
				}
				break;
			case R.id.rl_switch_vibrate:
				if (vibrateSwitch.isSwitchOpen()) {
					vibrateSwitch.closeSwitch();
					settingsModel.setSettingMsgVibrate(false);
				} else {
					vibrateSwitch.openSwitch();
					settingsModel.setSettingMsgVibrate(true);
				}
				break;
			case R.id.rl_switch_speaker:
				if (speakerSwitch.isSwitchOpen()) {
					speakerSwitch.closeSwitch();
					settingsModel.setSettingMsgSpeaker(false);
				} else {
					speakerSwitch.openSwitch();
					settingsModel.setSettingMsgVibrate(true);
				}
				break;
			case R.id.rl_switch_delete_msg_when_exit_group:
				if(switch_delete_msg_when_exit_group.isSwitchOpen()){
					switch_delete_msg_when_exit_group.closeSwitch();
					settingsModel.setDeleteMessagesAsExitGroup(false);
					chatOptions.setDeleteMessagesAsExitGroup(false);
				}else{
					switch_delete_msg_when_exit_group.openSwitch();
					settingsModel.setDeleteMessagesAsExitGroup(true);
					chatOptions.setDeleteMessagesAsExitGroup(true);
				}
				break;
			case R.id.rl_switch_auto_accept_group_invitation:
				if(switch_auto_accept_group_invitation.isSwitchOpen()){
					switch_auto_accept_group_invitation.closeSwitch();
					settingsModel.setAutoAcceptGroupInvitation(false);
					chatOptions.setAutoAcceptGroupInvitation(false);
				}else{
					switch_auto_accept_group_invitation.openSwitch();
					settingsModel.setAutoAcceptGroupInvitation(true);
					chatOptions.setAutoAcceptGroupInvitation(true);
				}
				break;
			case R.id.rl_switch_adaptive_video_encode:
				EMLog.d("switch", "" + !switch_adaptive_video_encode.isSwitchOpen());
				if (switch_adaptive_video_encode.isSwitchOpen()){
					switch_adaptive_video_encode.closeSwitch();
					settingsModel.setAdaptiveVideoEncode(false);
					EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);

				}else{
					switch_adaptive_video_encode.openSwitch();
					settingsModel.setAdaptiveVideoEncode(true);
					EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
				}
				break;
			case R.id.btn_logout:
			//	logout();
				break;
			case R.id.ll_black_list:
				startActivity(new Intent(getActivity(), BlacklistActivity.class));
				break;
			case R.id.ll_set_push_nick:
				startActivity(new Intent(getActivity(), OfflinePushNickActivity.class));
				break;
			case R.id.ll_call_option:
				startActivity(new Intent(getActivity(), CallOptionActivity.class));
				break;
			case R.id.ll_user_profile:
				startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("setting", true)
						.putExtra("username", EMClient.getInstance().getCurrentUser()));
				break;
			case R.id.rl_push_settings:
				startActivity(new Intent(getActivity(), OfflinePushSettingsActivity.class));
				break;
			default:
				break;
		}
	}

	void logout() {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		String st = getResources().getString(R.string.Are_logged_out);
		pd.setMessage(st);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		DemoHelper.getInstance().logout(false,new EMCallBack() {
			
			@Override
			public void onSuccess() {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						getActivity().finish();
						startActivity(new Intent(getActivity(), LoginActivity.class));
						
					}
				});
			}
			
			@Override
			public void onProgress(int progress, String status) {
				
			}
			
			@Override
			public void onError(int code, String message) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						pd.dismiss();
						Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        if(((EMMainActivity)getActivity()).isConflict){
			outState.putBoolean("isConflict", true);
		}else if(((EMMainActivity)getActivity()).getCurrentAccountRemoved()){
			outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
		}
    }

}
