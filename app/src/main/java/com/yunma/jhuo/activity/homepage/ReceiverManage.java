package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.adapter.ReceiverManageAdapter;
import com.yunma.adapter.ReceiverManageAdapter.OnSelected;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.ModifyRecipientInterface;
import com.yunma.jhuo.m.RecipientManageInterface.OnModifyRecipient;
import com.yunma.jhuo.m.RecipientManageInterface.RecipientManageView;
import com.yunma.jhuo.p.ModifyRecipientPre;
import com.yunma.jhuo.p.RecipientManagePre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

public class ReceiverManage extends MyCompatActivity implements RecipientManageView,OnModifyRecipient,
        OnSelected, ModifyRecipientInterface.ModifyRecipientView {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutAddReceiver) LinearLayout layoutAddReceiver;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvVontacts) ListView lvVontacts;
    @BindView(R.id.layout) View layout;
    private RecipientManagePre mPresenter;
    private ModifyRecipientPre modifyRecipientPre;
    private ReceiverManageAdapter mAdapter = null;
    private RecipientManageBean recipientManageBean = null;
    private RecipientManageBean.SuccessBean.ListBean modifyBean;
    private List<RecipientManageBean.SuccessBean.ListBean> listBeen;
    private Context mContext;
    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_manage);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }

    private void getDatas() {
        mPresenter = new RecipientManagePre(this);
        mPresenter.queryRecipient();
        modifyRecipientPre =  new ModifyRecipientPre(this);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ReceiverManage.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack,R.id.layoutAddReceiver})
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.layoutBack:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("result","refresh");
                intent.putExtras(bundle);
                this.setResult(2, intent);
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutAddReceiver:
                intent = new Intent(ReceiverManage.this,AddRecipient.class);
                bundle = new Bundle();
                bundle.putString("mtittle","添加地址");
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                // 做刷新操作
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            mPresenter.queryRecipient();
        }else if (resultCode == 2) {
            if(data.getStringExtra("type").equals("modify")){
                ModifyAddressBean resultBean = (ModifyAddressBean) data.getSerializableExtra("result");
                int pos = data.getIntExtra("modifyId",-1);
                LogUtils.log("postion : 3 ---> " + pos);
                listBeen.get(pos).setName(resultBean.getName());
                listBeen.get(pos).setTel(resultBean.getTel());
                listBeen.get(pos).setRegoin(resultBean.getRegoin());
                listBeen.get(pos).setAddr(resultBean.getAddr());
                if(resultBean.getUsed()==1){
                    clearDefault();
                    listBeen.get(pos).setUsed(1);
                }else{
                    listBeen.get(pos).setUsed(resultBean.getUsed());
                }
            }else if(data.getStringExtra("type").equals("delete")){
                int pos = data.getIntExtra("modifyId",-1);
                listBeen.remove(pos);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void clearDefault() {
        for(int i=0;i<listBeen.size();i++){
            listBeen.get(i).setUsed(0);
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public ModifyAddressBean getModifyAddressBean() {
        ModifyAddressBean modifyAddressBean = new ModifyAddressBean();
        modifyAddressBean.setToken(SPUtils.getToken(mContext));
        modifyAddressBean.setAddr(getModifyBean().getAddr());
        modifyAddressBean.setName(getModifyBean().getName());
        modifyAddressBean.setRegoin(getModifyBean().getRegoin());
        modifyAddressBean.setTel(getModifyBean().getTel());
        modifyAddressBean.setUsed(1);
        modifyAddressBean.setId(getModifyBean().getId());
        return modifyAddressBean;
    }

    @Override
    public void toShowModifyResult(SuccessResultBean successResultBean, String msg) {
        if(successResultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            //修改默认成功
            if(getListBeen().get(getSelectedId()).getUsed()!=1){
                listBeen.get(reChangeDefaultAddr()).setUsed(0);
                listBeen.get(getSelectedId()).setUsed(1);
            }else{
                listBeen.get(getSelectedId()).setUsed(getModifyAddressBean().getUsed());
            }
            mAdapter.setRecipientManageBean(listBeen);
           // mPresenter.queryRecipient();
        }
    }

    private int reChangeDefaultAddr() {
        for(int i=0;i<listBeen.size();i++){
            if(listBeen.get(i).getUsed()==1){
                return i;
            }
          //  listBeen.get(i).setUsed(0);
        }
        return -1;
    }

    @Override
    public void toShowRecipientManage(RecipientManageBean bean, String msg) {
        if(bean ==null){
            ToastUtils.showError(mContext,msg);
        }else{
            this.recipientManageBean = bean;
            listBeen = recipientManageBean.getSuccess().getList();
            if (listBeen != null) {
                if (mAdapter == null) {
                    lvVontacts.setVisibility(View.VISIBLE);
                    mAdapter = new ReceiverManageAdapter(ReceiverManage.this,listBeen,
                            ReceiverManage.this,ReceiverManage.this);
                    lvVontacts.setAdapter(mAdapter);
                }else{
                    mAdapter.setRecipientManageBean(listBeen);
                }
            }
        }
    }

    @Override
    public void onModifyListener(int position, RecipientManageBean.SuccessBean.ListBean mBean) {
        Intent intent = new Intent(mContext, AddRecipient.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("mtittle", "编辑地址");
        bundle.putSerializable("bean", mBean);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter= null;
        if(mPresenter!=null){
            mPresenter = null;
        }
    }

    @Override
    public void selected(int position, List<RecipientManageBean.SuccessBean.ListBean> addressList) {
        setSelectedId(position);
        setListBeen(addressList);
        setModifyBean(addressList.get(position));
        modifyRecipientPre.modifyLocation();
    }

    public RecipientManageBean.SuccessBean.ListBean getModifyBean() {
        return modifyBean;
    }

    public void setModifyBean(RecipientManageBean.SuccessBean.ListBean modifyBean) {
        this.modifyBean = modifyBean;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public List<RecipientManageBean.SuccessBean.ListBean> getListBeen() {
        return listBeen;
    }

    public void setListBeen(List<RecipientManageBean.SuccessBean.ListBean> listBeen) {
        this.listBeen = listBeen;
    }
}
