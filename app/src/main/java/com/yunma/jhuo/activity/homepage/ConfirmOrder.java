package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.yunma.R;
import com.yunma.bean.AddAddressResultBean;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.bean.OrderBean;
import com.yunma.bean.OrderdetailsBean;
import com.yunma.bean.RecipientManageBean;
import com.yunma.bean.SaveOrderBean;
import com.yunma.bean.SaveOrderResultBean;
import com.yunma.jhuo.SelectVolumeActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.ConfirmOrderInterface.ConfirmOrderView;
import com.yunma.jhuo.m.SaveOrderInterface;
import com.yunma.jhuo.p.ConfirmOrderPre;
import com.yunma.jhuo.p.SaveOrderPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.ExpressUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ConfirmOrder extends MyCompatActivity implements
        ConfirmOrderView, SaveOrderInterface.SaveOrderView {
    private static final int VOLIME_RESULT = 3;
    @BindView(R.id.nineGrid) NineGridView nineGrid;
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.tvReceiver) TextView tvReceiver;
    @BindView(R.id.tvNumber) TextView tvNumber;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.layoutFavorableInfos) View layoutFavorableInfos;
    @BindView(R.id.layoutSelectAddress) LinearLayout layoutSelectAddress;
    @BindView(R.id.layoutAddress) RelativeLayout layoutAddress;
    @BindView(R.id.tvAddLocation) TextView tvAddLocation;
    @BindView(R.id.tvFavorableInfos)TextView tvFavorableInfos;
    @BindView(R.id.imgSingleGoods) ImageView imgSingleGoods;
    @BindView(R.id.tvSingleGoodsName) TextView tvSingleGoodsName;
    @BindView(R.id.tvSingleGoodsInfo) TextView tvSingleGoodsInfo;
    @BindView(R.id.tvSingleGoodsPrice) TextView tvSingleGoodsPrice;
    @BindView(R.id.layoutSingleGoods) RelativeLayout layoutSingleGoods;
    @BindView(R.id.layoutMultipleGoods) LinearLayout layoutMultipleGoods;
    @BindView(R.id.layoutLookAll) LinearLayout layoutLookAll;
    @BindView(R.id.layoutInvoice) RelativeLayout layoutInvoice;
    @BindView(R.id.layoutCarriageExplain) RelativeLayout layoutCarriageExplain;
    @BindView(R.id.tvSubmitOrder) TextView tvSubmitOrder;
    @BindView(R.id.tvGoodsNums) TextView tvGoodsNums;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.tvExpressPrice) TextView tvExpressPrice;
    @BindView(R.id.tvNumbers) TextView tvNumbers;
    @BindView(R.id.tvAllTotalPrice) TextView tvAllTotalPrice;
    @BindView(R.id.layoutTotal) TextView layoutTotal;
    @BindView(R.id.etRemark) EditText etRemark;
    @BindView(R.id.tvExpressCost) TextView tvExpressCost;
    private OrderBean orderBean = null;
    private List<OrderdetailsBean> orderBeanList = null;
    private Context mContext;
    private ConfirmOrderPre mPresenter = null;
    private SaveOrderPre saveOrderPre = null;
    private String addressId = null;
    private GetShoppingListBean resultBean = null;
    private String currAddress = null;
    private int currNums = -1;
    private double totalPrice = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }

    private void getDatas() {
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("type");
        if(type==0){//单个商品，多件尺码
            singleTypeGoodsInfos(bundle);
        }else if(type==1){//多个商品，多件尺码
            layoutSingleGoods.setVisibility(View.GONE);
            layoutMultipleGoods.setVisibility(View.VISIBLE);
            totalPrice =  Double.valueOf(bundle.getString("totalPrice"));
            tvTotalPrice.setText(String.valueOf("￥" + totalPrice));
            currNums = Integer.valueOf(bundle.getString("totalNums"));
            tvGoodsNums.setText(String.valueOf(currNums + "件"));
            layoutTotal.setText("共" + bundle.getString("totalNums") + "件" );
            if(currAddress!=null){
                tvExpressPrice.setText(ExpressUtils.getExpressCost(currAddress,currNums));
                tvAllTotalPrice.setText("￥" + ValueUtils.toTwoDecimal(
                        Double.valueOf(bundle.getString("totalPrice")) +
                                ExpressUtils.getExpressCost(currAddress,currNums)));
            }
            resultBean = (GetShoppingListBean) bundle.getSerializable("goodsListBean");
            if(resultBean != null){
                setDatas(resultBean.getSuccess());
                // 合并订单
                orderBeanList = new ArrayList<>();
                for(int i=0;i<resultBean.getSuccess().size();i++){
                    OrderdetailsBean orderdetailsBean = new OrderdetailsBean();
                    orderdetailsBean.setGid(String.valueOf(
                            resultBean.getSuccess().get(i).getGoodsId()));
                    orderdetailsBean.setSize(resultBean.getSuccess().get(i).getCartSize());
                    orderdetailsBean.setNum(String.valueOf(resultBean.getSuccess().get(i).getCartNum()));
                    orderBeanList.add(orderdetailsBean);
                }
            }

        }
    }

    private void singleTypeGoodsInfos(Bundle bundle) {
        layoutSingleGoods.setVisibility(View.VISIBLE);
        layoutMultipleGoods.setVisibility(View.GONE);
        orderBean= (OrderBean) bundle.getSerializable("order");
        if(orderBean!=null){
            orderBeanList = orderBean.getOrderdetails();
        }
        String goodsInfo = "";
        for (int i=0;i<orderBeanList.size();i++){
            goodsInfo = goodsInfo + orderBeanList.get(i).getSize()
                    + "x" + orderBeanList.get(i).getNum() + ";";
        }
        currNums = Integer.valueOf(bundle.getString("totalNums"));
        tvGoodsNums.setText(String.valueOf(currNums + "件"));
        totalPrice = bundle.getDouble("totalPrice");
        tvTotalPrice.setText(String.valueOf(totalPrice + "元"));
        totalPrice = bundle.getDouble("totalPrice");
        if(currAddress!=null){
            tvExpressPrice.setText(String.valueOf((totalPrice +
                    ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
            tvAllTotalPrice.setText(String.valueOf((totalPrice +
                    ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
        }
        tvSingleGoodsName.setText(bundle.getString("goodsName"));
        String s ="￥" +  ValueUtils.toTwoDecimal(bundle.getDouble("singlePrice"));
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,14),1,s.indexOf("."));
        tvSingleGoodsPrice.setText(ss);
        String storageType = bundle.getString("storageType");
        String goodPic =bundle.getString("goodsPic");
        if(storageType!=null&& storageType.equals("自仓")){
            if(goodPic!=null){
                GlideUtils.glidNormle(this,imgSingleGoods,
                        ConUtils.SElF_GOODS_IMAGE_URL + goodPic.split(",")[0]);
            }else{
                imgSingleGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
            }

        }else if(storageType!=null&& storageType.equals("大仓")){
            if(goodPic!=null){
                GlideUtils.glidNormle(this,imgSingleGoods,
                        ConUtils.GOODS_IMAGE_URL + goodPic);
            }else{
                imgSingleGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
            }
        }
        tvSingleGoodsInfo.setText(goodsInfo);
        tvNumbers.setText("x" + bundle.getString("totalNums") + "件");
    }

    private void setDatas(List<GetShoppingListBean.SuccessBean> resultBean) {
        if(resultBean.size()>1){
            layoutSingleGoods.setVisibility(View.GONE);
            layoutMultipleGoods.setVisibility(View.VISIBLE);
            NineGridView.setImageLoader(new GlideImageLoader());
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < resultBean.size(); i++) {
                ImageInfo info = new ImageInfo();
                if (resultBean.get(i).getRepoid() == 1) {
                    info.setThumbnailUrl(ConUtils.SElF_GOODS_IMAGE_URL +
                            resultBean.get(i).getPic().split(",")[0]);
                    info.setBigImageUrl(ConUtils.SElF_GOODS_IMAGE_URL +
                            resultBean.get(i).getPic().split(",")[0]);
                } else {
                    info.setThumbnailUrl(ConUtils.GOODS_IMAGE_URL +
                            resultBean.get(i).getNumber() + ".jpg");
                    info.setBigImageUrl(ConUtils.GOODS_IMAGE_URL +
                            resultBean.get(i).getNumber() + ".jpg");
                }
                imageInfo.add(info);
            }
            nineGrid.setAdapter(new NineGridViewClickAdapter(this, imageInfo));
        }else{
            layoutSingleGoods.setVisibility(View.VISIBLE);
            layoutMultipleGoods.setVisibility(View.GONE);
            if (resultBean.get(0).getRepoid() == 1) {
                GlideUtils.glidNormleFast(mContext,imgSingleGoods,ConUtils.SElF_GOODS_IMAGE_URL +
                        resultBean.get(0).getPic().split(",")[0]);
            }else{
                GlideUtils.glidNormleFast(mContext,imgSingleGoods,ConUtils.GOODS_IMAGE_URL +
                        resultBean.get(0).getNumber() + ".jpg");
            }
            tvSingleGoodsName.setText(resultBean.get(0).getName());
            tvSingleGoodsInfo.setText(resultBean.get(0).getCartSize()+"x"+resultBean.get(0).getCartNum());
            String s ="￥" +  ValueUtils.toTwoDecimal(resultBean.get(0).getYunmaprice());
            SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                    DensityUtils.sp2px(mContext,14),1,s.indexOf("."));
            tvSingleGoodsPrice.setText(ss);
            tvNumbers.setText("x"+resultBean.get(0).getCartNum() +"件");
        }
    }

    private void initStatusBar() {
        mContext = this;
        mPresenter = new ConfirmOrderPre(this);
        mPresenter.getReceiverInfo();
        saveOrderPre = new SaveOrderPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(ConfirmOrder.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams(); //取控件textView当前的布局参数
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44), 0, 0);
        scroll.setLayoutParams(fl); //使设置好的布局参数应用到控件
        scroll.smoothScrollTo(0,0);
    }

    @OnClick({R.id.layoutSelectAddress, R.id.layoutBack,R.id.tvAddLocation,R.id.layoutLookAll,
            R.id.layoutInvoice,R.id.layoutCarriageExplain,R.id.tvSubmitOrder,R.id.layoutSingleGoods,
            R.id.layoutFavorableInfos})
    public void onClick(View view) {
        Intent intent ;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.layoutSelectAddress:
                intent = new Intent(ConfirmOrder.this,ReceiverManage.class);//重新选择收件人
                startActivityForResult(intent,2);
                break;
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);                break;
            case R.id.tvAddLocation:
                intent = new Intent(ConfirmOrder.this,AddRecipient.class);//添加默认收件人
                bundle = new Bundle();
                bundle.putString("mtittle","添加地址");
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
            case R.id.layoutLookAll:
                intent = new Intent(ConfirmOrder.this,GoodsMnifest.class);
                bundle  = new Bundle();
                bundle.putString("totalNums",layoutTotal.getText().toString());
                bundle.putSerializable("goodsListBean",resultBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layoutInvoice:
                showTicketDialog();
                break;
            case R.id.tvSubmitOrder:
                if(getAddressId()==null||getAddressId().isEmpty()){
                    ToastUtils.showError(getApplicationContext(),"请选择或者添加收件信息");
                }else if(orderBeanList==null){
                    ToastUtils.showError(getApplicationContext(),"商品信息有误，请退回重新选择");
                }else {
                    saveOrderPre.toSaveOrder();
                }
                break;
            case R.id.layoutCarriageExplain:
                showExpressDialog();
                break;
            case R.id.layoutFavorableInfos:
                intent = new Intent(ConfirmOrder.this,SelectVolumeActivity.class);
                startActivityForResult(intent,3);
                overridePendingTransition(0,
                        android.R.animator.fade_out);
                break;
        }
    }

    private void showExpressDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext,42);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.express_info)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            AddAddressResultBean addAddressResultBean;
            addAddressResultBean = (AddAddressResultBean) data.getSerializableExtra("result");
            tvAddLocation.setVisibility(View.GONE);
            layoutAddress.setVisibility(View.VISIBLE);
            tvReceiver.setText(addAddressResultBean.getSuccess().getName());
            tvNumber.setText(addAddressResultBean.getSuccess().getTel());
            tvAddress.setText(addAddressResultBean.getSuccess().getRegoin() +
            addAddressResultBean.getSuccess().getAddr());
            addressId = String.valueOf(addAddressResultBean.getSuccess().getId());
            currAddress = addAddressResultBean.getSuccess().getRegoin();
            tvExpressCost.setText("￥" + ExpressUtils.getExpressCost(currAddress,currNums));
            tvExpressPrice.setText(String.valueOf((totalPrice +
                    ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
            tvAllTotalPrice.setText(String.valueOf((totalPrice +
                    ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
        }else if(resultCode == 2){
            if(data.getStringExtra("result").equals("refresh")){
                mPresenter.getReceiverInfo();
            }
        }else if(resultCode == VOLIME_RESULT){
            if(data.getStringExtra("money")!=null&&data.getStringExtra("name")!=null){
                tvFavorableInfos.setText(Html.fromHtml("已使用1张" + data.getStringExtra("name") +
                        "<font color = '#f44141'>" + "￥" + data.getStringExtra("money") + "元</font>"));
            }
        }
    }

    private void showTicketDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext,42);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.invoice_info)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String getAddressId() {
        return addressId;
    }

    @Override
    public SaveOrderBean getSaveOrderBean() {
        SaveOrderBean saveOrderBean = new SaveOrderBean();
        saveOrderBean.setToken(SPUtils.getToken(mContext));
        saveOrderBean.setAddrid(addressId);
        saveOrderBean.setOrderdetails(orderBeanList);
        if(EmptyUtil.isNotEmpty(etRemark.getText().toString().trim())){
            saveOrderBean.setRemark(etRemark.getText().toString().trim());
        }else{
            saveOrderBean.setRemark("");
        }
        return saveOrderBean;
    }

    @Override
    public void showSaveOrderInfos(SaveOrderResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            Intent intent = new Intent(ConfirmOrder.this,SelectedPayWay.class);
            intent.putExtra("orderId",String.valueOf(resultBean.getSuccess().getId()));
            intent.putExtra("totalPrice",resultBean.getSuccess().getTotalcost());
            startActivity(intent);
            AppManager.getAppManager().finishActivity(this);
        }
    }

    @Override
    public void toShowDefaultAddress(RecipientManageBean.SuccessBean.ListBean listBean, String msg) {
        if(listBean==null){
            tvAddLocation.setVisibility(View.VISIBLE);
            layoutAddress.setVisibility(View.GONE);
        }else{
            addressId = String.valueOf(listBean.getId());
            tvReceiver.setText(listBean.getName());
            tvNumber.setText(listBean.getTel());
            tvAddress.setText(listBean.getRegoin() + listBean.getAddr());
            layoutAddress.setVisibility(View.VISIBLE);
            tvAddLocation.setVisibility(View.GONE);
            currAddress = listBean.getRegoin();
            if(currNums!=-1 && totalPrice!=-1){
                tvExpressCost.setText("￥" + ExpressUtils.getExpressCost(currAddress,currNums));
                tvExpressPrice.setText(String.valueOf((totalPrice +
                        ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
                tvAllTotalPrice.setText(String.valueOf((totalPrice +
                        ExpressUtils.getExpressCost(currAddress,currNums)) + "元"));
            }

        }
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.default_pic)//
                    .bitmapTransform(new RoundedCornersTransformation(context, 20, 0,
                            RoundedCornersTransformation.CornerType.ALL))
                    .error(R.drawable.load_error)//
                    .animate(android.R.anim.slide_in_left)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .crossFade()
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
