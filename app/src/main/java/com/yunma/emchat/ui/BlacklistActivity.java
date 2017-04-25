package com.yunma.emchat.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.*;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.yunma.R;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import java.util.Collections;
import java.util.List;

import butterknife.*;

/**
 * Blacklist screen
 */
public class BlacklistActivity extends BaseActivity {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.list) ListView listView;
    private BlacklistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_black_list);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        List<String> blacklist = EMClient.getInstance().contactManager().getBlackListUsernames();
        if (blacklist != null) {
            Collections.sort(blacklist);
            adapter = new BlacklistAdapter(this, blacklist);
            listView.setAdapter(adapter);
        }

        registerForContextMenu(listView);

    }

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(BlacklistActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.em_remove_from_blacklist, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove) {
            final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            removeOutBlacklist(tobeRemoveUser);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * remove user out from blacklist
     *
     * @param tobeRemoveUser
     */
    void removeOutBlacklist(final String tobeRemoveUser) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.be_removing));
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(tobeRemoveUser);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            adapter.remove(tobeRemoveUser);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.Removed_from_the_failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void toGroupDetails(View view) {

    }

    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * adapter
     */
    private class BlacklistAdapter extends ArrayAdapter<String> {

        BlacklistAdapter(Context context, List<String> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.ease_row_contact, null);
            }
            String username = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);

            EaseUserUtils.setUserAvatar(getContext(), username, avatar);
            EaseUserUtils.setUserNick(username, name);

            return convertView;
        }

    }
}
