package com.yunma.emchat.ui;

import android.os.Bundle;
import android.widget.*;

import com.yunma.R;
import com.yunma.emchat.adapter.NewFriendsMsgAdapter;
import com.yunma.emchat.db.InviteMessgeDao;
import com.yunma.emchat.domain.InviteMessage;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;

import java.util.List;

import butterknife.*;

/**
 * Application and notification
 */
public class NewFriendsMsgActivity extends BaseActivity {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.list) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_friends_msg);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        init();
    }

    private void init() {
        tvTittle.setText("申请与通知");
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this),0,0);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);
    }


    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }
}
