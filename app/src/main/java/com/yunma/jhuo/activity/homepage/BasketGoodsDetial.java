package com.yunma.jhuo.activity.homepage;

import android.animation.ObjectAnimator;
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
import com.yunma.emchat.ui.EMMainActivity;
import com.yunma.adapter.GoodsSizeAdapter;
import com.yunma.adapter.GoodsTagAdapter;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddShoppingCartsInterface;
import com.yunma.jhuo.m.GoodsCollectInterFace.AddGoodsCollectView;
import com.yunma.jhuo.m.GoodsCollectInterFace.IfcollectView;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchGoodsView;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchSelfGoodsView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.FlowTagLayout;
import com.yunma.publish.PicViewerActivity;

import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

public class BasketGoodsDetial extends MyCompatActivity implements AddGoodsCollectView,SearchSelfGoodsView,
        IfcollectView, GoodsSizeAdapter.OnAddBasketCarts, AddShoppingCartsInterface.AddShoppingCartsView, SearchGoodsView {

    @BindView(R.id.xbanner) XBanner xbanner;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsOriPrice) TextView tvGoodsOriPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.tag_layout) FlowTagLayout tagLayout;
    @BindView(R.id.size_layout) FlowTagLayout sizeLayout;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.ImgCollection) ImageView ImgCollection;
    @BindView(R.id.layoutCollection) LinearLayout layoutCollection;
    @BindView(R.id.layoutCustomer) LinearLayout layoutCustomer;
    @BindView(R.id.layoutRightBuy) LinearLayout layoutRightBuy;
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.tvExpressInfo) TextView tvExpressInfo;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.imgsFavorable) ImageView imgsFavorable;
    @BindView(R.id.tvFavorable) TextView tvFavorable;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    private Context mContext;
    private String goodId = null;
    private AddGoodsCollectPre addGoodsCollectPre = null;
    private SearchSelfGoodsByCodePre searchPre = null;
    private SearchGoodsByCodePre searchGoodsByCodePre = null;
    private GetSelfGoodsResultBean.SuccessBean.ListBean listBean = null;
    private AddShoppingCartsPre addShoppingCartsPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_goods_detial);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mContext = this;
        addGoodsCollectPre = new AddGoodsCollectPre(this);
        addShoppingCartsPre = new AddShoppingCartsPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(BasketGoodsDetial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        int navigationBarHeight = ScreenUtils.getNavigationBarHeight(BasketGoodsDetial.this);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44), 0, navigationBarHeight + 48);
        scroll.setLayoutParams(fl); //使设置好的布局参数应用到控件
        searchPre = new SearchSelfGoodsByCodePre(this);
        searchGoodsByCodePre =  new SearchGoodsByCodePre(this);
        if(getIntent().getStringExtra("type").equals("自仓")){
            try {
                searchPre.searchSelfGoodsByCode(this,getIntent().getStringExtra("goodsNumber"),"7");
            }catch (Exception e){
                e.getMessage();
            }
        } else if(getIntent().getStringExtra("type").equals("大仓")){
            try {
                searchGoodsByCodePre.searchGoodsByCode(this,getIntent().getStringExtra("goodsNumber"));
            }catch (Exception e){
                e.getMessage();
            }
        }
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews,R.id.layoutCustomer,R.id.layoutCollection,
            R.id.layoutRightBuy,R.id.tvExpressInfo})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(BasketGoodsDetial.this,ESQ,
                        "你好，我正在J货看" + tvGoodsName.getText().toString());
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.layoutCollection:
                if(getContext()!=null&&getGoodId()!=null){
                    addGoodsCollectPre.onAddCollect();
                }else{
                    ToastUtils.showError(getApplicationContext(),"系统错误，请稍候重试");
                }
                break;
            case R.id.layoutCustomer:
                intent = new Intent(BasketGoodsDetial.this,EMMainActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutRightBuy:
                intent = new Intent(BasketGoodsDetial.this,SelfGoodsSizeNumsChoose.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selfGoods",listBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tvExpressInfo:
                showExpressDialog();
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
    public Context getContext() {
        return mContext;
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

    @Override
    public void showCollectInfos(AddGoodsCollectBean resultBean, String msg) {
        if(resultBean!=null){
            GlideUtils.glidLocalDrawable(getApplicationContext(),ImgCollection,R.drawable.star_yellow);
        }
    }

    @Override
    public String getGoodId() {
        return goodId;
    }

    @Override
    public void onAddCollectShow(AddGoodsCollectBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ImgCollection.setImageDrawable(getResources().getDrawable(R.drawable.star_yellow));
            ToastUtils.showSuccess(getApplicationContext(),msg);
        }
    }

    @Override
    public void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showSuccess(mContext,msg);
        }else{
            if(resultBean.getSuccess().getList()==null){
                ToastUtils.showInfo(mContext,"该商品已下架");
            }else if(resultBean.getSuccess().getList().size()==0){
                ToastUtils.showInfo(mContext,"该商品已售空");
            }else{
                listBean = resultBean.getSuccess().getList().get(0);
                assert listBean != null;
                initDatas(listBean);
            }
        }
    }

    private void initDatas(GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        List<String> imgUrls = Arrays.asList(listBean.getPic().split(","));
        setDatas(imgUrls);
        goodId = String.valueOf(listBean.getId());
        IfcollectPre ifcollectPre = new IfcollectPre(this);
        ifcollectPre.ifCollect(goodId);
        tvGoodsName.setText(listBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(listBean.getSaleprice())));
        if(listBean.getLabel().contains("支持去鞋盒服务")){
            imgsFavorable.setVisibility(View.VISIBLE);
            tvFavorable.setVisibility(View.VISIBLE);
        }
        int remainNums = 0;
        for(int i=0;i<listBean.getStocks().size();i++){
            remainNums = remainNums + listBean.getStocks().get(i).getNum();
        }
        tvGoodsRemain.setText(String.valueOf("仅剩" + remainNums + "件"));
        // 设置中划线并加清晰;
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        ArrayList<String> sizeList = new ArrayList<>();
        if(listBean.getStocks()!=null){
            for(int i =0;i<listBean.getStocks().size();i++){
                if(listBean.getStocks().get(i).getNum()!=0){
                    sizeList.add(listBean.getStocks().get(i).getSize());
                }
            }
            GoodsSizeAdapter mAdapter = new GoodsSizeAdapter(BasketGoodsDetial.this,
                    BasketGoodsDetial.this);
            sizeLayout.setAdapter(mAdapter);
            mAdapter.onlyAddAll(sizeList);

        }
        GoodsTagAdapter tagAdapter = new GoodsTagAdapter(BasketGoodsDetial.this);
        tagLayout.setAdapter(tagAdapter);
        String [] str = listBean.getLabel().split(",");
        List<String> tagsList = new ArrayList<>();
        for (String aStr : str) {
            if (!aStr.contains("服务")) {
                tagsList.add(aStr);
            }
        }
        tagAdapter.onlyAddAll(tagsList);
        tvExpressInfo.setText(Html.fromHtml("· 邮费信息：详细见各地区" +
                "<font color = '#1E88E5'>" + "运费模板" + "</font>"));
    }

    private void setDatas(final List<String> imgUrls) {
        assert imgUrls!= null;
        xbanner.setData(imgUrls,null);
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
                        .load(ConUtils.SElF_GOODS_IMAGE_URL + imgUrls.get(pos))
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
                for (int i=0;i<imgUrls.size();i++){
                    PathBean pathBean = new PathBean();
                    pathBean.setImgsPath(ConUtils.SElF_GOODS_IMAGE_URL + imgUrls.get(i));
                    pathBeenList.add(pathBean);
                }
                preBean.setPathBeen(pathBeenList);
                Intent intent = new Intent(getContext(),PicViewerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("path",preBean);
                bundle.putInt("pos", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(BasketGoodsDetial.this);
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
    public void showSearchInfos(GoodsInfoResultBean resultBean, String msg) {

    }
}
