package com.yunma.jhuo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.EntrepotGoodsAdapter;
import com.yunma.bean.*;
import com.yunma.bean.GoodsInfoResultBean.SuccessBean.ListBean;
import com.yunma.emchat.ui.EMMainActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddShoppingCartsInterface.AddShoppingCartsView;
import com.yunma.jhuo.m.GoodsCollectInterFace.AddGoodsCollectView;
import com.yunma.jhuo.m.GoodsCollectInterFace.IfcollectView;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchGoodsByTypeView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.NestListView;

import java.util.List;

import butterknife.*;

public class EntrepotGoodsDetial extends MyCompatActivity implements AddShoppingCartsView,
        SearchGoodsByTypeView, AddGoodsCollectView, IfcollectView {
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsOriPrice) TextView tvGoodsOriPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.nestlistview) NestListView nestlistview;
    @BindView(R.id.ImgCollection) ImageView ImgCollection;
    @BindView(R.id.layoutCollection) LinearLayout layoutCollection;
    @BindView(R.id.layoutCustomer) LinearLayout layoutCustomer;
    @BindView(R.id.layoutAddBasket) LinearLayout layoutAddBasket;
    @BindView(R.id.layoutRightBuy) LinearLayout layoutRightBuy;
    @BindView(R.id.tvExpressInfo) TextView tvExpressInfo;
    @BindView(R.id.imgsGoods) ImageView imgsGoods;
    @BindView(R.id.layoutLookMore) View layoutLookMore;
    @BindView(R.id.imgLookDetials) ImageView imgLookDetials;
    @BindView(R.id.tlGoodsDetials) TableLayout tlGoodsDetials;
    @BindView(R.id.tvBrand) TextView tvBrand;
    @BindView(R.id.tvGoodsCode) TextView tvGoodsCode;
    @BindView(R.id.tvFitPerson) TextView tvFitPerson;
    @BindView(R.id.tvGoodsType) TextView tvGoodsType;
    @BindView(R.id.layoutSimilar) View layoutSimilar;
    private EntrepotGoodsAdapter eAdapter;
    private List<ListBean> list;
    private SearchGoodsByTypePre searchGoodsByTypePre = null;
    private Context mContext;
    private boolean lookDetials = false;
    private String currGoodsId ;
    private AddGoodsCollectPre addGoodsCollectPre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrepot_goods_detial);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        initDatas();
    }

    private void initView() {
        mContext = this;
        searchGoodsByTypePre = new SearchGoodsByTypePre(this);
        addGoodsCollectPre = new AddGoodsCollectPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(EntrepotGoodsDetial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        int navigationBarHeight = ScreenUtils.getNavigationBarHeight(EntrepotGoodsDetial.this);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44), 0, navigationBarHeight+48);
        scroll.setLayoutParams(fl); //使设置好的布局参数应用到控件
        int width =ScreenUtils.getScreenWidth(EntrepotGoodsDetial.this);
        int height = 2*width/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        imgsGoods.setLayoutParams(params);
    }

    private void initDatas() {
        ListBean listBean = (ListBean) getIntent().getSerializableExtra("goodsBean");
        String isEnd = getIntent().getStringExtra("isToEnd");
        assert  listBean !=null;
        IfcollectPre ifcollectPre = new IfcollectPre(this);
        ifcollectPre.ifCollect(listBean.getId());
        if(listBean.getPic()!=null){
            GlideUtils.glidNormle(this,imgsGoods,ConUtils.GOODS_IMAGE_URL + listBean.getPic());
        }
        if(isEnd.equals("yes")){
            layoutSimilar.setVisibility(View.GONE);
        }else if(isEnd.equals("no")){
            layoutSimilar.setVisibility(View.VISIBLE);
            String searchType = listBean.getType();
            if(searchType !=null&&!searchType.isEmpty()){
                searchGoodsByTypePre.searchGoodsByType(searchType,"6",1);
            }
        }
        tvGoodsName.setText(listBean.getBrand()+ "_" + listBean.getYear()+ "_" + listBean.getNumber()+ "_"
                + listBean.getSeason()+ "_" + listBean.getName()+ "_" + listBean.getType());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);

        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(listBean.getSaleprice())));
        if(listBean.getSales()==null){
            tvGoodsRemain.setText(String.valueOf("销量" + 0));
        }else {
            tvGoodsRemain.setText(String.valueOf("销量" + listBean.getSales()));
        }
        // 设置中划线并加清晰;
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvBrand.setText(listBean.getBrand());
        tvGoodsCode.setText(listBean.getNumber());
        tvFitPerson.setText(listBean.getSex());
        tvGoodsType.setText(listBean.getType());
        tvExpressInfo.setText(Html.fromHtml("· 邮费信息：详细见各地区" +
                "<font color = '#1E88E5'>" + "运费模板" + "</font>"));
        setCurrGoodsId(listBean.getId());
    }

    @OnClick({R.id.layoutCollection, R.id.layoutCustomer, R.id.layoutAddBasket,R.id.layoutLookMore,
            R.id.layoutRightBuy,R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                if(isLogin()){
                    QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                    WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                    String ESQ = "2252162352";  //客服QQ号
                    int ret = mWPA.startWPAConversation(EntrepotGoodsDetial.this,ESQ,
                            "你好，我正在J货看" + tvGoodsName.getText().toString());
                    if (ret != 0) {
                        Toast.makeText(getApplicationContext(),
                                "抱歉，联系客服出现了错误~. error:" + ret,
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    ToastUtils.showInfo(getApplicationContext(),"请登录后操作");
                }
                break;
            case R.id.layoutCollection:
                if(isLogin()){
                    if(getContext()!=null&&getGoodId()!=null){
                        addGoodsCollectPre.onAddCollect();
                    }else{
                        ToastUtils.showError(mContext,"系统错误，请稍候重试");
                    }
                }else{
                    ToastUtils.showInfo(getApplicationContext(),"请登录后操作");
                }
                break;
            case R.id.layoutCustomer:
                if(isLogin()){
                    intent = new Intent(EntrepotGoodsDetial.this,EMMainActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtils.showInfo(getApplicationContext(),"请登录后操作");
                }
                break;
            case R.id.layoutAddBasket:
                if(isLogin()){
                    ToastUtils.showInfo(getApplicationContext(),"大仓商品暂未开放" + "\n" +
                            "您可以先加入收藏");
                    return;
                    /*intent = new Intent(EntrepotGoodsDetial.this,SelectedSizeToShoppingCart.class);
                    bundle = new Bundle();
                    bundle.putSerializable("goodsBean",listBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_in,0);*/
                }else{
                    ToastUtils.showInfo(getApplicationContext(),"请登录后操作");
                }
                break;
            case R.id.layoutRightBuy:
                if(isLogin()){
                    ToastUtils.showInfo(getApplicationContext(),"大仓商品暂未开放" + "\n" +
                            "您可以先加入收藏");
                    return;
                   /* intent = new Intent(EntrepotGoodsDetial.this,SelectedGoodsSizeAndNums.class);
                    bundle = new Bundle();
                    bundle.putSerializable("goodsBean",listBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_in,0);*/
                }else{
                    ToastUtils.showInfo(getApplicationContext(),"请登录后操作");
                }
                break;
            case R.id.layoutLookMore:
                if(lookDetials){
                    tlGoodsDetials.setVisibility(View.GONE);
                    imgLookDetials.setImageDrawable(getResources().getDrawable(R.drawable.look_more));
                    lookDetials = false;
                }else {
                    tlGoodsDetials.setVisibility(View.VISIBLE);
                    imgLookDetials.setImageDrawable(getResources().getDrawable(R.drawable.un_look_more));
                    lookDetials = true;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(EntrepotGoodsDetial.this);
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    @Override
    public void showCollectInfos(AddGoodsCollectBean resultBean, String msg) {
        if(resultBean!=null){
            GlideUtils.glidLocalDrawable(mContext,ImgCollection,R.drawable.star_yellow);
        }
    }

    @Override
    public String getGoodId() {
        return getCurrGoodsId();
    }

    @Override
    public void onAddCollectShow(AddGoodsCollectBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            GlideUtils.glidLocalDrawable(mContext,ImgCollection,R.drawable.star_yellow);
            ToastUtils.showSuccess(mContext,msg);
        }
    }

    @Override
    public void showSearchInfosByType(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
            layoutSimilar.setVisibility(View.GONE);
        }else{
            list = resultBean.getSuccess().getList();
            boolean isLastPage = resultBean.getSuccess().isIsLastPage();
            if(list.size()==0){
                layoutSimilar.setVisibility(View.GONE);
            }else{
                layoutSimilar.setVisibility(View.VISIBLE);
                if(eAdapter==null){
                    eAdapter = new EntrepotGoodsAdapter(EntrepotGoodsDetial.this,
                            list,isLastPage);
                    nestlistview.setAdapter(eAdapter);
                }else{
                    eAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public ShoppingCartsBean getShoppingCartsBean() {
        return null;
    }

    @Override
    public void showAddShoppingCartsInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
        }
    }

    public String getCurrGoodsId() {
        return currGoodsId;
    }

    public void setCurrGoodsId(String currGoodsId) {
        this.currGoodsId = currGoodsId;
    }

    @Override
    public boolean isLogin() {
        return super.isLogin();
    }
}
