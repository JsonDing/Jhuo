package com.yunma.emchat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;
import com.yunma.R;

/**
 * Diagnose activity；user can upload log for debug purpose
 * 
 * @author lyuzhao
 * 
 */
public class DiagnoseActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_diagnose);

		TextView currentVersion = (TextView) findViewById(R.id.tv_version);
		Button uploadLog = (Button) findViewById(R.id.button_uploadlog);
		uploadLog.setOnClickListener(this);
		String strVersion = "";
		try {
			strVersion = getVersionName();
		} catch (Exception e) {
		}
		if (!TextUtils.isEmpty(strVersion))
			currentVersion.setText("V" + strVersion);
		else{
			String st = getResources().getString(R.string.Not_Set);
			currentVersion.setText(st);}
	}

	public void back(View view) {
		finish();
	}

	private String getVersionName() throws Exception {
		return EMClient.getInstance().VERSION;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_uploadlog:
			uploadlog();
			break;

		default:
			break;
		}

	}

	private ProgressDialog progressDialog;

	public void uploadlog() {

		if (progressDialog == null)
			progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Upload_the_log);
		progressDialog.setMessage(stri);
		progressDialog.setCancelable(false);
		progressDialog.show();
		final String st = getResources().getString(R.string.Log_uploaded_successfully);
		EMClient.getInstance().uploadLog(new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(DiagnoseActivity.this, st,
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onProgress(final int progress, String status) {
				// getActivity().runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// progressDialog.setMessage("上传中 "+progress+"%");
				//
				// }
				// });

			}
			String st3 = getResources().getString(R.string.Log_Upload_failed);
			@Override
			public void onError(int code, String message) {
				EMLog.e("###", message);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(DiagnoseActivity.this, st3,
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

	}

}
