package com.yunma.emchat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContact;
import com.hyphenate.exceptions.HyphenateException;
import com.yunma.R;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.db.UserDao;
import com.yunma.emchat.domain.RobotUser;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;

import java.util.*;

import butterknife.*;

public class RobotsActivity extends BaseActivity {

    public static final String TAG = RobotsActivity.class.getSimpleName();
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.list) ListView mListView;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar) LinearLayout progressBar;
    private List<RobotUser> robotList = new ArrayList<>();
    private RobotAdapter adapter;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_fragment_robots);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initView();
        setListener();
    }

    private void setListener() {
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RobotUser user = (RobotUser) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(RobotsActivity.this, ChatActivity.class);
                intent.putExtra("userId", user.getUsername());
                startActivity(intent);
            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    private void initView() {
        tvTittle.setText("J货小助手");
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this),0,0);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                getRobotNamesFromServer();
            }
        });
        Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
        if (robotMap != null) {
            robotList.addAll(robotMap.values());
        } else {
            progressBar.setVisibility(View.VISIBLE);
            getRobotNamesFromServer();
        }
        adapter = new RobotAdapter(this, 1, robotList);
        mListView.setAdapter(adapter);

    }

    private void getRobotNamesFromServer() {
        asyncGetRobotNamesFromServer(new EMValueCallBack<List<EMContact>>() {

            @Override
            public void onSuccess(final List<EMContact> value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Map<String, RobotUser> mMap = new HashMap<>();
                        for (EMContact item : value) {
                            RobotUser user = new RobotUser(item.getUsername());
                            user.setNick(item.getNick());
                            user.setInitialLetter("#");
                            mMap.put(item.getUsername(), user);
                        }
                        robotList.clear();
                        robotList.addAll(mMap.values());
                        // save it in cache
                        DemoHelper.getInstance().setRobotList(mMap);
                        // save it in database
                        UserDao dao = new UserDao(RobotsActivity.this);
                        dao.saveRobotUser(robotList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void asyncGetRobotNamesFromServer(final EMValueCallBack<List<EMContact>> callback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    List<EMContact> mList = EMClient.getInstance().getRobotsFromServer();
                    callback.onSuccess(mList);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    callback.onError(e.getErrorCode(), e.toString());
                }
            }
        }).start();
    }

    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }

    private class RobotAdapter extends ArrayAdapter<RobotUser> {

        private LayoutInflater inflater;

        RobotAdapter(Context context, int res, List<RobotUser> robots) {
            super(context, res, robots);
            this.inflater = LayoutInflater.from(context);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_robots, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getNick());
            return convertView;
        }

    }
}
