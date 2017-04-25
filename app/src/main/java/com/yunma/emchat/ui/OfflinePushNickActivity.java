package com.yunma.emchat.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.hyphenate.chat.EMClient;
import com.yunma.R;
import com.yunma.emchat.DemoHelper;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.*;

public class OfflinePushNickActivity extends BaseActivity {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.et_input_nickname) EditText inputNickName;
    @BindView(R.id.btn_save) Button btnSave;
    @BindView(R.id.tv_nickname_description) TextView nicknameDescription;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_offline_push);
        ButterKnife.bind(this);
        initBar();
        inputNickName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    nicknameDescription.setTextColor(Color.RED);
                } else {
                    nicknameDescription.setTextColor(Color.parseColor("#cccccc"));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(OfflinePushNickActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void save() {
        dialog = ProgressDialog.show(OfflinePushNickActivity.this, "update nickname...", "waiting...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        inputNickName.getText().toString());
                if (!updatenick) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(OfflinePushNickActivity.this, "update nickname failed!",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(inputNickName.getText().toString());
                            if (!updatenick) {
                                Toast.makeText(OfflinePushNickActivity.this, "update nickname failed!",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(OfflinePushNickActivity.this, "update nickname success!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();
                }
            }
        }).start();
    }
}
