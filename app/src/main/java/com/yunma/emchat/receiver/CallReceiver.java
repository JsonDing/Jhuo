package com.yunma.emchat.receiver;

import android.content.*;

import com.hyphenate.util.EMLog;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.ui.VideoCallActivity;
import com.yunma.emchat.ui.VoiceCallActivity;

public class CallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(!DemoHelper.getInstance().isLoggedIn())
		    return;
		//username
		String from = intent.getStringExtra("from");
		//call type
		String type = intent.getStringExtra("type");
		if("video".equals(type)){ //video call
		    context.startActivity(new Intent(context, VideoCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}else{ //voice call
		    context.startActivity(new Intent(context, VoiceCallActivity.class).
		            putExtra("username", from).putExtra("isComingCall", true).
		            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		EMLog.d("CallReceiver", "app received a incoming call");
	}

}
