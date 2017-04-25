package com.yunma.jhuo.activity.mine;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.*;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.jhuo.activity.SelectedGoodsSizeAndNums;
import com.yunma.jhuo.activity.homepage.SelfGoodsSizeNumsChoose;
import com.yunma.adapter.GoodsSizeAdapter;
import com.yunma.adapter.GoodsSizeAdapter.OnAddBasketCarts;
import com.yunma.adapter.GoodsTagAdapter;
import com.yunma.bean.*;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddShoppingCartsInterface.AddShoppingCartsView;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchGoodsView;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchSelfGoodsView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.FlowTagLayout;
import com.yunma.publish.PicViewerActivity;

import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;


public class QuickReplenishDetial extends MyCompatActivity implements SearchSelfGoodsView,
        OnAddBasketCarts, AddShoppingCartsView, SearchGoodsView {
    @BindView(R.id.xbanner) XBanner xbanner;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsOriPrice) TextView tvGoodsOriPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.tag_layout) FlowTagLayout tagLayout;
    @BindView(R.id.size_layout) FlowTagLayout sizeLayout;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.layoutRightBuy) LinearLayout layoutRightBuy;
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvExpressInfo) TextView tvExpressInfo;
    @BindView(R.id.tvGoodsCode) TextView tvGoodsCode;
    @BindView(R.id.tvFitPerson) TextView tvFitPerson;
    @BindView(R.id.tvGoodsType) TextView tvGoodsType;
    @BindView(R.id.tlGoodsDetials) TableLayout tlGoodsDetials;
    @BindView(R.id.imgLookDetials) ImageView imgLookDetials;
    @BindView(R.id.layoutLookMore) LinearLayout layoutLookMore;
    @BindView(R.id.tvBrand) TextView tvBrand;
    private ListBean listBean = null;
    private Activity mActivity;
    private boolean lookDetials = false;
    private AddShoppingCartsPre addShoppingCartsPre = null;
    private String goodId = null;
    private int type = -1;
    private int remainNums = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_replenish_detial);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initTittleBar();
        getIntentDatas();
    }

    private void getIntentDatas() {
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getInt("type");
        if (type == 1) {
            SearchSelfGoodsByCodePre searchPre =
                    new SearchSelfGoodsByCodePre(this);
            searchPre.searchSelfGoodsByCode(this,
                    bundle.getString("goodsNumber"),"12");
        } else {
            // TODO: 2017-03-24
            SearchGoodsByCodePre searchGoodsByCodePre = new SearchGoodsByCodePre(this);
            searchGoodsByCodePre.searchGoodsByCode(this, bundle.getString("goodsNumber"));
        }
    }

    private void initTittleBar() {
        mActivity = this;
        int statusHeight = ScreenUtils.getStatusHeight(mActivity);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(mActivity, 44), 0, DensityUtils.dp2px(mActivity, 44));
        addShoppingCartsPre = new AddShoppingCartsPre(QuickReplenishDetial.this);
        scroll.setLayoutParams(fl); //使设置好的布局参数应用到控件
    }

    /**
     * 自仓
     * @param resultBean
     * @param msg
     */
    @Override
    public void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showSuccess(getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                listBean = resultBean.getSuccess().getList().get(0);
                assert listBean != null;
                initDatas(listBean);
            } else {
                ToastUtils.showInfo(getApplicationContext(), "该商品已售空");
            }
        }
    }

    /**
     * 大仓
     * @param resultBean
     * @param msg
     */
    @Override
    public void showSearchInfos(GoodsInfoResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showSuccess(getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                assert resultBean.getSuccess().getList().get(0) != null;
                initDatas(resultBean.getSuccess().getList().get(0));
            } else {
                ToastUtils.showInfo(getApplicationContext(), "该商品已售空");
            }
        }
    }

    private void initDatas(final GoodsInfoResultBean.SuccessBean.ListBean listBean) {
        List<String> imgUrls = Arrays.asList(listBean.getPic().split(","));
        setDatas(imgUrls, ConUtils.GOODS_IMAGE_URL);
        goodId = String.valueOf(listBean.getId());
        tvGoodsName.setText(listBean.getBrand() + listBean.getType() + listBean.getSex() + listBean.getNumber());
        String s = "￥" + ValueUtils.toTwoDecimal(listBean.getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(mActivity, R.color.color_b4, s,
                Typeface.NORMAL, DensityUtils.sp2px(mActivity, 17), 1, s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(listBean.getSaleprice())));
        int remainNums = 0;
        for (int i = 0; i < listBean.getStocks().size(); i++) {
            remainNums = remainNums + listBean.getStocks().get(i).getNum();
        }
        tvGoodsRemain.setText(String.valueOf("仅剩" + remainNums + "件"));
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if(listBean.getBrand()!=null){
            tvBrand.setText(listBean.getBrand());
        }
        if(listBean.getNumber()!=null){
            tvGoodsCode.setText(listBean.getNumber());
        }
        if(listBean.getSex()!=null){
            tvFitPerson.setText(listBean.getSex());
        }
        if(listBean.getType()!=null){
            tvGoodsType.setText(listBean.getType());
        }
        ArrayList<String> sizeList = new ArrayList<>();
        if (listBean.getStocks() != null) {
            for (int i = 0; i < listBean.getStocks().size(); i++) {
                if (listBean.getStocks().get(i).getNum() != 0) {
                    sizeList.add(listBean.getStocks().get(i).getSize());
                }
            }
            GoodsSizeAdapter mAdapter = new GoodsSizeAdapter(QuickReplenishDetial.this,
                    QuickReplenishDetial.this);
            sizeLayout.setAdapter(mAdapter);
            mAdapter.onlyAddAll(sizeList);
        }
    }

    private void initDatas(ListBean listBean) {
        List<String> imgUrls = Arrays.asList(listBean.getPic().split(","));
        setDatas(imgUrls, ConUtils.SElF_GOODS_IMAGE_URL);
        goodId = String.valueOf(listBean.getId());
        tvGoodsName.setText(listBean.getName());
        String s = "￥" + ValueUtils.toTwoDecimal(listBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s, Typeface.NORMAL,
                DensityUtils.sp2px(this, 17), 1, s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(listBean.getSaleprice())));

        for (int i = 0; i < listBean.getStocks().size(); i++) {
            remainNums = remainNums + listBean.getStocks().get(i).getNum();
            listBean.getStocks().get(i).setBuyNum(0);
            if(listBean.getStocks().get(i).getNum()==0){
                listBean.getStocks().remove(i);
            }
        }
        tvGoodsRemain.setText(String.valueOf("仅剩" + remainNums + "件"));
        // 设置中划线并加清晰;
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (listBean.getBrand() != null) {
            tvBrand.setText(listBean.getBrand());
        }
        if (listBean.getNumber() != null) {
            tvGoodsCode.setText(listBean.getNumber());
        }
        if (listBean.getSex() == 1) {
            tvFitPerson.setText("男士");
        } else if (listBean.getSex() == 2){
            tvFitPerson.setText("女士");
        } else{
            tvFitPerson.setText("男女");
        }
        if (listBean.getType() == 1){
            tvGoodsType.setText("服装");
        } else if(listBean.getType() == 2) {
            tvGoodsType.setText("鞋子");
        } else if(listBean.getType() == 3) {
            tvGoodsType.setText("配件");
        }else if(listBean.getType() == 4){
            tvGoodsType.setText("男童");
        } if(listBean.getType() == 5){
            tvGoodsType.setText("女童");
        }else{

        }
        ArrayList<String> sizeList = new ArrayList<>();
        if (listBean.getStocks() != null) {
            for (int i = 0; i < listBean.getStocks().size(); i++) {
                if (listBean.getStocks().get(i).getNum() != 0) {
                    sizeList.add(listBean.getStocks().get(i).getSize());
                }
            }
            GoodsSizeAdapter mAdapter = new GoodsSizeAdapter(QuickReplenishDetial.this, QuickReplenishDetial.this);
            sizeLayout.setAdapter(mAdapter);
            mAdapter.onlyAddAll(sizeList);

        }
        GoodsTagAdapter tagAdapter = new GoodsTagAdapter(QuickReplenishDetial.this);
        tagLayout.setAdapter(tagAdapter);
        String [] str = listBean.getLabel().split(",");
        List<String> tagsList = new ArrayList<>();
        for (String aStr : str) {
            if (!aStr.contains("服务")) {
                tagsList.add(aStr);
            }
        }
        tagAdapter.onlyAddAll(tagsList);
    }

    private void setDatas(final List<String> imgUrls, final String url) {
        tvExpressInfo.setText(Html.fromHtml("· 邮费信息：详细见各地区" +
                "<font color = '#1E88E5'>" + "运费模板" + "</font>"));
        assert imgUrls != null;
        xbanner.setData(imgUrls, null);
        xbanner.setmAdapter(new XBanner.XBannerAdapter() {
            public void loadBanner(XBanner banner, View view, int pos) {
                ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
                    @Override
                    public void animate(View view) {
                        view.setAlpha(0f);
                        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                        fadeAnim.setDuration(2000);
                        fadeAnim.start();
                    }
                };
                Glide.with(getApplicationContext())
                        .load(url + imgUrls.get(pos))
                        .asBitmap()
                        .placeholder(R.drawable.banner_blank)//
                        .error(R.drawable.banner_blank)//
                        .animate(animationObject)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//
                        .into((ImageView) view);
            }
        });
        xbanner.setPageTransformer(Transformer.Alpha);
        xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                final LocalImagePathBean preBean = new LocalImagePathBean();
                List<PathBean> pathBeenList = new ArrayList<>();
                for (int i = 0; i < imgUrls.size(); i++) {
                    PathBean pathBean = new PathBean();
                    pathBean.setImgsPath(ConUtils.SElF_GOODS_IMAGE_URL + imgUrls.get(i));
                    pathBeenList.add(pathBean);
                }
                preBean.setPathBeen(pathBeenList);
                Intent intent = new Intent(QuickReplenishDetial.this, PicViewerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("path", preBean);
                bundle.putInt("pos", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews, R.id.tvExpressInfo,R.id.layoutLookMore,
            R.id.layoutRightBuy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(QuickReplenishDetial.this,ESQ,
                        "你好，我正在看J货");
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tvExpressInfo:
                showExpressDialog();
                break;
            case R.id.layoutLookMore:
                if (lookDetials) {
                    tlGoodsDetials.setVisibility(View.GONE);
                    imgLookDetials.setImageDrawable(getResources().getDrawable(R.drawable.look_more));
                    lookDetials = false;
                } else {
                    tlGoodsDetials.setVisibility(View.VISIBLE);
                    imgLookDetials.setImageDrawable(getResources().getDrawable(R.drawable.un_look_more));
                    lookDetials = true;
                }
                break;
            case R.id.layoutRightBuy:
                if (remainNums != 0){
                    if(type == 1){
                        Intent intent = new Intent(QuickReplenishDetial.this,SelfGoodsSizeNumsChoose.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("selfGoods",listBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.bottom_in,0);
                    }else if(type != -1){
                        Intent intent = new Intent(QuickReplenishDetial.this,SelectedGoodsSizeAndNums.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("goodsBean",listBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.bottom_in,0);
                    }
                }else {
                    ToastUtils.showWarning(mActivity,"库存为零，无法购买");
                }
                break;
        }
    }

    private void showExpressDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 48);
        new MDDialog.Builder(this)
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
                .setWidthMaxDp((int) DensityUtils.px2dp(this, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @Override
    public void onAddBasketCarts(String size) {
        ShoppingCartsBean shoppingCartsBean = new ShoppingCartsBean();
        shoppingCartsBean.setToken(SPUtils.getToken(getContext()));
        shoppingCartsBean.setNum(1);
        shoppingCartsBean.setSize(size);
        shoppingCartsBean.setGid(goodId);
        addShoppingCartsPre.addToBasket(shoppingCartsBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(QuickReplenishDetial.this);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public ShoppingCartsBean getShoppingCartsBean() {
        return null;
    }

    @Override
    public void showAddShoppingCartsInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        } else {
            ToastUtils.showSuccess(getApplicationContext(), msg);
        }
    }

}
