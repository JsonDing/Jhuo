package com.yunma.emchat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.yunma.R;
import com.yunma.emchat.DemoHelper;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;

import butterknife.*;

public class AddContactActivity extends BaseActivity {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.tvRight) TextView tvRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.edit_note) EditText editText;
    @BindView(R.id.name) TextView nameText;
    @BindView(R.id.ll_user) RelativeLayout searchedUserLayout;
    private String toAddUsername;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
    }

    private void initBar() {
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this),0,0);
        tvTittle.setText( getResources().getString(R.string.add_friend));
        tvRight.setText("查找");
        String strUserName = getResources().getString(R.string.user_name);
        editText.setHint(strUserName);
    }


    /**
     * search contact
     *
     * @param v
     */
    /*public void searchContact(View v) {
        final String name = editText.getText().toString();
     //   String saveText = searchBtn.getText().toString();

        if (getString(R.string.button_search).equals(saveText)) {
            toAddUsername = name;
            if (TextUtils.isEmpty(name)) {
                new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
                return;
            }

            // TODO you can search the user from your app server here.

            searchedUserLayout.setVisibility(View.VISIBLE);
            nameText.setText(toAddUsername);

        }
    }*/

    /**
     * add contact
     *
     * @param view
     */
    public void addContact(View view) {
        if (EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (DemoHelper.getInstance().getContactList().containsKey(nameText.getText().toString())) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View v) {
        finish();
    }

    @OnClick({R.id.layoutBack, R.id.layoutRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutRight:
                break;
        }
    }
}
