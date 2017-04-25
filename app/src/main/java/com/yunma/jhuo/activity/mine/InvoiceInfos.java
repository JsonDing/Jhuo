package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.ReceiverManage;
import com.yunma.jhuo.activity.homepage.SelectTickets;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AskForInvoiceInterFace;
import com.yunma.jhuo.m.ConfirmOrderInterface;
import com.yunma.jhuo.p.AskForInvoicePre;
import com.yunma.jhuo.p.ConfirmOrderPre;
import com.yunma.utils.*;
import com.yunma.widget.ContainsEmojiEditText;

import butterknife.*;

public class InvoiceInfos extends MyCompatActivity implements
        ConfirmOrderInterface.ConfirmOrderView, AskForInvoiceInterFace.AskForInvoiceView {
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.tvReceiver)
    TextView tvReceiver;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.layoutSelectAddress)
    LinearLayout layoutSelectAddress;
    @BindView(R.id.layoutAddress)
    RelativeLayout layoutAddress;
    @BindView(R.id.tvAddLocation)
    TextView tvAddLocation;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.layoutPerson)
    View layoutPerson;
    @BindView(R.id.layoutCompany)
    View layoutCompany;
    @BindView(R.id.radioGroupType)
    RadioGroup radioGroupType;
    @BindView(R.id.radioGroupContent)
    RadioGroup radioGroupContent;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvRatepayer)
    TextView tvRatepayer;
    @BindView(R.id.tvRegisterAddress)
    TextView tvRegisterAddress;
    @BindView(R.id.tvRegisterPhone)
    TextView tvRegisterPhone;
    @BindView(R.id.tvRegisterBank)
    TextView tvRegisterBank;
    @BindView(R.id.tvRegisterAccount)
    TextView tvRegisterAccount;
    @BindView(R.id.etContent)
    ContainsEmojiEditText etContent;

    private Context mContext;
    private String ticketType = "0";
    private String goodType = "衣服";
    private String addressId = null;
    private String orderId = null;
    private String invoiceId = null;
    private InvoiceBean.SuccessBean.ListBean tickBean = null;
    private ConfirmOrderPre mPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_infos);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }


    private void getDatas() {
        mPresenter = new ConfirmOrderPre(this);
        mPresenter.getReceiverInfo();
        Bundle bundle = this.getIntent().getExtras();
        orderId = bundle.getString("orderid");
        tvPrice.setText(bundle.getString("totalPrice"));

    }

    private void setDatas() {
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.cbPersonal:
                        ticketType = "0";
                        layoutPerson.setVisibility(View.VISIBLE);
                        layoutCompany.setVisibility(View.GONE);
                        break;
                    case R.id.cbCompany:
                        ticketType = "1";
                        layoutCompany.setVisibility(View.VISIBLE);
                        layoutPerson.setVisibility(View.GONE);
                        break;
                }
            }
        });

        radioGroupContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.cbClothes:
                        goodType = "衣服";
                        break;
                    case R.id.cbShoes:
                        goodType = "鞋子";
                        break;
                    case R.id.cbOther:
                        goodType = "配件";
                        break;
                }
            }
        });
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(InvoiceInfos.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.btnConfirm, R.id.tvAddLocation,
            R.id.layoutSelectAddress, R.id.layoutCompany})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnConfirm:
                if(addressId == null){
                    ToastUtils.showError(mContext,"请选择发票邮递地址");
                }else if(ticketType.equals("0") &&
                        EmptyUtil.isEmpty(etContent.getText().toString().trim())){
                    ToastUtils.showError(mContext,"请输入发票内容");
                }else if(orderId == null){
                    ToastUtils.showError(mContext,"服务起异常，请稍后重试");
                }else{
                    getBeans();
                }
                break;
            case R.id.tvAddLocation:
                intent = new Intent(InvoiceInfos.this, ReceiverManage.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.layoutSelectAddress:
                intent = new Intent(InvoiceInfos.this, ReceiverManage.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.layoutCompany:
                intent = new Intent(InvoiceInfos.this, SelectTickets.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void getBeans() {
        AskForInvoicePre askPre = new AskForInvoicePre(this);
        AskForInvoiceBean mBeans = new AskForInvoiceBean();
        mBeans.setToken(SPUtils.getToken(mContext));
        mBeans.setAddrid(addressId);
        mBeans.setName(goodType + "  " +etContent.getText().toString().trim());
        mBeans.setOrderid(orderId);
        mBeans.setType(ticketType);
        if(invoiceId!=null){
            mBeans.setVatid(invoiceId);
        }
        LogUtils.log("----> " + mBeans.toString());
        askPre.askForInvoice(mContext,mBeans);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (data.getStringExtra("result").equals("refresh")) {
                mPresenter.getReceiverInfo();
            }
        } else if (resultCode == 1) {
            tickBean = (InvoiceBean.SuccessBean.ListBean) data.getSerializableExtra("tickBean");
            assert tickBean != null;
            tvCompanyName.setText(tickBean.getName());
            tvRatepayer.setText(tickBean.getNumber());
            tvRegisterAddress.setText(tickBean.getAddr());
            tvRegisterPhone.setText(tickBean.getTel());
            tvRegisterBank.setText(tickBean.getBank());
            tvRegisterAccount.setText(tickBean.getAccount());
            invoiceId = tickBean.getId();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void toShowDefaultAddress(RecipientManageBean.SuccessBean.ListBean listBean, String msg) {
        if (listBean == null) {
            tvAddLocation.setVisibility(View.VISIBLE);
            layoutAddress.setVisibility(View.GONE);
        } else {
            addressId = String.valueOf(listBean.getId());
            tvReceiver.setText(listBean.getName());
            tvNumber.setText(listBean.getTel());
            tvAddress.setText(listBean.getRegoin() + listBean.getAddr());
            layoutAddress.setVisibility(View.VISIBLE);
            tvAddLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAskInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
        }
    }

}
