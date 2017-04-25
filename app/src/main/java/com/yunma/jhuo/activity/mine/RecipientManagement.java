package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.AddRecipient;
import com.yunma.adapter.RecipientManagementAdapter;
import com.yunma.bean.ModifyAddressBean;
import com.yunma.bean.RecipientManageBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.RecipientManageInterface.OnModifyRecipient;
import com.yunma.jhuo.m.RecipientManageInterface.RecipientManageView;
import com.yunma.jhuo.p.RecipientManagePre;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;

public class RecipientManagement extends MyCompatActivity implements
        RecipientManageView, OnModifyRecipient {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutAddReceiver) LinearLayout layoutAddReceiver;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvReceiverManage) ListView lvReceiverManage;
    @BindView(R.id.layout) View layout;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    private Context mContext;
    private RecipientManageBean mBean = null;
    private List<RecipientManageBean.SuccessBean.ListBean> addressList = new ArrayList<>();
    private RecipientManagementAdapter mAdapter;
    private RecipientManagePre mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_management);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        initDatas();

    }

    private void initDatas() {
        mPresenter = new RecipientManagePre(RecipientManagement.this);
        mPresenter.queryRecipient();
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(RecipientManagement.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
       /* int navigationBarHeight = ScreenUtils.getNavigationBarHeight(RecipientManagement.this);
        //取控件textView当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        linearParams.height = navigationBarHeight;// 控件的高强制设成
        linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件*/
    }

    @OnClick({R.id.layoutBack, R.id.layoutAddReceiver})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutAddReceiver:
                addLocation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            mPresenter.queryRecipient();
        }else if(resultCode == 2){
            if(data.getStringExtra("type").equals("modify")){
                ModifyAddressBean resultBean = (ModifyAddressBean) data.getSerializableExtra("result");
                int pos = data.getIntExtra("modifyId",-1);
                LogUtils.log("postion : 3 ---> " + pos);
                addressList.get(pos).setName(resultBean.getName());
                addressList.get(pos).setTel(resultBean.getTel());
                addressList.get(pos).setRegoin(resultBean.getRegoin());
                addressList.get(pos).setAddr(resultBean.getAddr());
                if(resultBean.getUsed()==1){
                    clearDefault();
                    addressList.get(pos).setUsed(1);
                }else{
                    addressList.get(pos).setUsed(resultBean.getUsed());
                }
            }else if(data.getStringExtra("type").equals("delete")){
                int pos = data.getIntExtra("modifyId",-1);
                addressList.remove(pos);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void clearDefault() {
        for(int i=0;i<addressList.size();i++){
            addressList.get(i).setUsed(0);
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void toShowRecipientManage(RecipientManageBean bean, String msg) {
        this.mBean = bean;
        if (mBean != null) {
            addressList = mBean.getSuccess().getList();
            if (mAdapter == null) {
                layoutNull.setVisibility(View.GONE);
                lvReceiverManage.setVisibility(View.VISIBLE);
                if(mAdapter==null){
                    mAdapter = new RecipientManagementAdapter(this, addressList,
                            RecipientManagement.this);
                    lvReceiverManage.setAdapter(mAdapter);
                }else{
                    mAdapter.setAddressList(addressList);
                }
            }
        }else {
            layoutNull.setVisibility(View.VISIBLE);
            lvReceiverManage.setVisibility(View.GONE);
            ToastUtils.showInfo(mContext, msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContext != null) {
            mContext = null;
        }
        if (mAdapter != null) {
            mAdapter = null;
        }
        if (mPresenter != null) {
            mPresenter = null;
        }
        mAdapter = null;
        addressList.clear();
    }

    private void addLocation() {
        Intent intent = new Intent(RecipientManagement.this, AddRecipient.class);
        Bundle bundle = new Bundle();
        bundle.putString("mtittle", "添加地址");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onModifyListener(int position, RecipientManageBean.SuccessBean.ListBean  mBean) {
        LogUtils.log("postion : 1 ---> " + position);
        Intent intent = new Intent(mContext, AddRecipient.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("mtittle", "编辑地址");
        bundle.putSerializable("bean", mBean);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

}
