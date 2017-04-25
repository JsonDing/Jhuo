package com.yunma.jhuo.activity.mine;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.ApplyGoodsAftermarketAdapter;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.general.*;
import com.yunma.jhuo.m.LoginInterface;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;

import java.io.Serializable;
import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

public class ApplyMoreGoodsAftermarket extends MyCompatActivity implements LoginInterface.LoginDialogView {
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.imgMessage)
    ImageView imgMessage;
    @BindView(R.id.imgRemind)
    ImageView imgRemind;
    @BindView(R.id.layoutNews)
    RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.lvGoodsAftermarketList)
    ListView lvGoodsAftermarketList;
    @BindView(R.id.btnSure)
    com.rey.material.widget.Button btnSure;
    private Context mContext;
    private ApplyGoodsAftermarketAdapter mAdapter;
    private List<ListBean.OrderdetailsBean> orderdetailsBeanList;
    private ListBean listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_more_goods_aftermarket);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }


    private void getDatas() {
        listBean = (ListBean) getIntent().getSerializableExtra("orderdetails");
        if (EmptyUtil.isNotEmpty(listBean)) {
            tvOrderId.setText("订单号：" + listBean.getId());
            tvOrderTime.setText(DateTimeUtils.getTime(listBean.getDate(),
                    DateTimeUtils.DEFAULT_DATE_FORMAT));
            orderdetailsBeanList = new ArrayList<>();
            for (int i = 0; i < listBean.getOrderdetails().size(); i++) {
                for (int j = 0; j < listBean.getOrderdetails().get(i).getNum(); j++) {
                    orderdetailsBeanList.add(listBean.getOrderdetails().get(i));
                }
            }
            for (int i = 0; i < orderdetailsBeanList.size(); i++) {
                orderdetailsBeanList.get(i).setNum(1);
            }
            mAdapter = new ApplyGoodsAftermarketAdapter(this, orderdetailsBeanList);
            lvGoodsAftermarketList.setAdapter(mAdapter);
        }
    }

    @OnClick({R.id.layoutBack, R.id.btnSure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSure:
                ToastWarnning();
                //showLoginDialog();
                break;
        }
    }

    private void ToastWarnning() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("同笔订单，在售后审核期不得再次申请售后，是否确定此次的售后商品？");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mAdapter.getSelectGoods()!=null&&mAdapter.getSelectGoods().size()!=0){
                            Intent intent = new Intent(ApplyMoreGoodsAftermarket.this, RefundMoreGoods.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("refundBean", (Serializable) mAdapter.getSelectGoods());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            ToastUtils.showError(mContext,"请选择退货商品...");
                        }
                        ///    LogUtils.log("选择的退货：" +  new Gson().toJson(mAdapter.getSelectGoods()));
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ApplyMoreGoodsAftermarket.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    private void showLoginDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 64);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.item_user_login)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        final ViewHolder holer = new ViewHolder(contentView);
                        holer.btnLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (EmptyUtil.isEmpty(holer.etLoginName.getText().toString().trim())) {
                                    ToastUtils.showError(mContext, "请输入帐号");
                                } else if (EmptyUtil.isEmpty(holer.etPasswd.getText().toString().trim())) {
                                    ToastUtils.showError(mContext, "请输入密码");
                                } else {
                                    LoginPre loginPre = new LoginPre(ApplyMoreGoodsAftermarket.this);
                                    loginPre.loginDialog(mContext, holer.etLoginName.getText().toString().trim(),
                                            holer.etPasswd.getText().toString().trim());
                                }
                            }
                        });
                        holer.tvForgetPasswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ForgetPassWd.class);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @Override
    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(mContext, msg);
       //     progressDimiss();
        } else{
        //    SPUtils.setPhoneNumber(mContext,etLoginName.getText().toString().trim());
        //    SPUtils.setPassWd(mContext,etPasswd.getText().toString().trim());
            SPUtils.setToken(mContext,resultBean.getSuccess().getToken());
          /*  updateAppInfos(resultBean.getSuccess().getToken());
            updateUserInfos();*/
            ToastUtils.showSuccess(mContext,"登录成功");
            /*//判断角色，进入相应界面
            UserInfosPre userInfosPre = new UserInfosPre(this);
            userInfosPre.getUserInfos();*/
        }
    }

    static class ViewHolder {
        @BindView(R.id.etLoginName)
        EditText etLoginName;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.etPasswd)
        EditText etPasswd;
        @BindView(R.id.view1)
        View view1;
        @BindView(R.id.btnLogin)
        Button btnLogin;
        @BindView(R.id.ckIsAutoLogin)
        CheckBox ckIsAutoLogin;
        @BindView(R.id.tvForgetPasswd)
        TextView tvForgetPasswd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
