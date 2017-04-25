package com.yunma.emchat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.yunma.R;
import com.yunma.emchat.Constant;
import com.yunma.emchat.adapter.GroupAdapter;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import java.util.List;

import butterknife.*;


public class GroupsActivity extends BaseActivity {
    public static final String TAG = "GroupsActivity";
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupsActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(GroupsActivity.this,
                            R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_fragment_groups);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        instance = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupListView = (ListView) findViewById(R.id.list);
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
                } else {
                    Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 2).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }

        });
        groupListView.setOnTouchListener(new OnTouchListener() {

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

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(GroupsActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void back(View view) {
        AppManager.getAppManager().finishActivity(this);
    }

    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }
}
