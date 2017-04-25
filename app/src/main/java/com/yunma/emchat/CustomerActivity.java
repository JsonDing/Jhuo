package com.yunma.emchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.yunma.R;
import com.yunma.adapter.CustomerAdapter;
import com.yunma.jhuo.general.LoginActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.library.MDDialog;

public class CustomerActivity extends MyCompatActivity {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.list) EaseConversationList conversationListView ;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private Context mContext;
    private CustomerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();

        loadCustomer();
    }


    private void loadCustomer() {

        conversationList.addAll(loadConversationWithRecentChat());
        //初始化，参数为会话列表集合
        conversationListView.init(conversationList);
        //刷新列表
        conversationListView.refresh();
 /*
       /* try {
            List<String> usernames = EMClient.getInstance()
                    .contactManager().getAllContactsFromServer();
            LogUtils.log("客服：" + usernames.size());
            if(mAdapter==null){
                mAdapter = new CustomerAdapter(this,usernames);
                lvCustomer.setAdapter(mAdapter);
            }else{
                mAdapter.notifyDataSetChanged();
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }*/
    }

    private Collection<? extends EMConversation> loadConversationWithRecentChat() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized(conversations){
            for(EMConversation conversation : conversations.values()){
                if(conversation.getAllMessages().size() != 0){
                    sortList.add(new Pair<>
                            (conversation.getLastMessage().getMsgTime(), conversation)
                    );
                }
            }
        }

        try{
            sortConversationByLastChatTime(sortList);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<>();
        for(Pair<Long, EMConversation> sortItem : sortList){
            list.add(sortItem.second);
        }

        return list;

    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> sortList) {
            Collections.sort(sortList, new Comparator<Pair<Long, EMConversation>>(){

                @Override
                public int compare(Pair<Long, EMConversation> con1,
                                   Pair<Long, EMConversation> con2) {
                    if(con1.first == con2.first){
                        return 0;
                    }else if(con2.first > con1.first){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(CustomerActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    private void shouWornning() {
        int dialogWith = ScreenUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dp2px(getApplicationContext(), 42);
        new MDDialog.Builder(getApplicationContext())
                .setIcon(R.drawable.logo_sm)
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("当前帐号已在其他设备上登录，如若并非本人操作，" +
                                "请立即登录并修改密码，或者联系客服冻结帐号");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(getApplicationContext(), dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

}
