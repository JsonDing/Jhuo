package com.yunma.jhuo.activity.homepage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
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
import com.yunma.adapter.GoodsSizeAdapter;
import com.yunma.adapter.GoodsTagAdapter;
import com.yunma.adapter.KindredGoodsAdapter;
import com.yunma.bean.AddGoodsCollectBean;
import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.bean.LocalImagePathBean;
import com.yunma.bean.PathBean;
import com.yunma.bean.ShoppingCartsBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.emchat.ui.EMMainActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.general.AddCartsRemindActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.general.RegisterAccount;
import com.yunma.jhuo.m.AddShoppingCartsInterface;
import com.yunma.jhuo.m.GoodsCollectInterFace;
import com.yunma.jhuo.m.GoodsCollectInterFace.AddGoodsCollectView;
import com.yunma.jhuo.m.OnKindredGoodsClick;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchSelfGoodsByTypeView;
import com.yunma.jhuo.p.AddGoodsCollectPre;
import com.yunma.jhuo.p.AddShoppingCartsPre;
import com.yunma.jhuo.p.IfcollectPre;
import com.yunma.jhuo.p.SearchSelfGoodsByTypePre;
import com.yunma.publish.PicViewerActivity;
import com.yunma.utils.*;
import com.yunma.widget.FlowTagLayout;
import com.yunma.widget.NestListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.library.MDDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qzone.QZone;

public class SelfGoodsDetials extends MyCompatActivity implements
        OnKindredGoodsClick , AddGoodsCollectView, SearchSelfGoodsByTypeView,
        GoodsCollectInterFace.IfcollectView, GoodsSizeAdapter.OnAddBasketCarts,
        AddShoppingCartsInterface.AddShoppingCartsView{
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.layoutNews) View layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsOriPrice) TextView tvGoodsOriPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.size_layout) FlowTagLayout sizeLayout;
    @BindView(R.id.tag_layout) FlowTagLayout tag_layout;
    @BindView(R.id.nestlistview) NestListView nestlistview;
    @BindView(R.id.ImgCollection) ImageView ImgCollection;
    @BindView(R.id.layoutCollection) LinearLayout layoutCollection;
    @BindView(R.id.layoutCustomer) LinearLayout layoutCustomer;
    @BindView(R.id.layoutAddBasket) LinearLayout layoutAddBasket;
    @BindView(R.id.layoutRightBuy) LinearLayout layoutRightBuy;
    @BindView(R.id.layoutSimilar) View layoutSimilar;
    @BindView(R.id.xbanner) XBanner xbanner;
    @BindView(R.id.tvHasSelected) TextView tvHasSelected;
    @BindView(R.id.tvExpressInfo) TextView tvExpressInfo;
    @BindView(R.id.imgsFavorable) ImageView imgsFavorable;
    @BindView(R.id.imgShare) ImageView imgShare;
    @BindView(R.id.tvFavorable) TextView tvFavorable;
    @BindView(R.id.layoutSelectSize) View layoutSelectSize;
    @BindView(R.id.layoutLookMore) View layoutLookMore;
    @BindView(R.id.imgLookDetials) ImageView imgLookDetials;
    @BindView(R.id.tlGoodsDetials) TableLayout tlGoodsDetials;
    @BindView(R.id.tvBrand) TextView tvBrand;
    @BindView(R.id.tvGoodsCode) TextView tvGoodsCode;
    @BindView(R.id.tvFitPerson) TextView tvFitPerson;
    @BindView(R.id.tvGoodsType) TextView tvGoodsType;
    private KindredGoodsAdapter nAdapter;//同类商品
    private GetSelfGoodsResultBean.SuccessBean.ListBean mBean = null;
    private SearchSelfGoodsByTypePre searchSelfGoodsByTypePre = null;
    private AddGoodsCollectPre addGoodsCollectPre = null;
    private AddShoppingCartsPre addShoppingCartsPre = null;
    private String goodId = null;
    private boolean lookDetials = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detials);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        initDatas();

    }

    /**
     * 初始化UI
     */
    private void initView() {
        searchSelfGoodsByTypePre = new SearchSelfGoodsByTypePre(this);
        addGoodsCollectPre = new AddGoodsCollectPre(this);
        addShoppingCartsPre = new AddShoppingCartsPre(this);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        int navigationBarHeight = ScreenUtils.getNavigationBarHeight(SelfGoodsDetials.this);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, SPUtils.getStatusHeight(this) + DensityUtils.dp2px(this, 44), 0, navigationBarHeight + 48);
        scroll.setLayoutParams(fl);
    }

    /**
     * 初始化Intent数据
     */
    private void initDatas() {
        goodId = String.valueOf(getIntent().getExtras().getInt("goodId"));
        IfcollectPre ifcollectPre = new IfcollectPre(this);
        ifcollectPre.ifCollect(goodId);
        String isEnd = getIntent().getStringExtra("isToEnd");
        mBean = (GetSelfGoodsResultBean.SuccessBean.ListBean)
                this.getIntent().getSerializableExtra("goodsDetials");
        assert mBean!=null;
        if(mBean.getLabel().contains("支持去鞋盒服务")){
            imgsFavorable.setVisibility(View.VISIBLE);
            tvFavorable.setVisibility(View.VISIBLE);
        }
        if(mBean.getPic()!=null){
            List<String> imgUrls = Arrays.asList(mBean.getPic().split(","));
            setDatas(imgUrls);
        }
        String searchType;
        if(mBean.getType()==1){
            searchType = "服装";
        }else if(mBean.getType()==2){
            searchType = "鞋子";
        }else if(mBean.getType()==3){
            searchType = "配件";
        }else if(mBean.getType()==4){
            searchType = "童款";
        }else if(mBean.getType()==5){
            searchType = "裤子";
        }else {
            searchType = null;
        }
        tvGoodsType.setText(searchType);
        if(mBean.getSex()==1){
            tvFitPerson.setText("男性");
        }else if(mBean.getSex()==2){
            tvFitPerson.setText("女性");
        }else{
            tvFitPerson.setText("中性");
        }
        if(isEnd.equals("yes")){
            layoutSimilar.setVisibility(View.GONE);
        }else{
            layoutSimilar.setVisibility(View.GONE);
            if(searchType !=null&&!searchType.isEmpty()){
                searchSelfGoodsByTypePre.searchSelfGoodsByType(String.valueOf(mBean.getType()),"6",1);
            }
        }
        tvGoodsName.setText(mBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(mBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(mBean.getSaleprice())));
        int remainNums = 0;
        for(int i=0;i<mBean.getStocks().size();i++){
            remainNums = remainNums + mBean.getStocks().get(i).getNum();
        }
        tvGoodsRemain.setText(String.valueOf(remainNums));
        // 设置中划线并加清晰;
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvBrand.setText(mBean.getBrand());
        tvGoodsCode.setText(mBean.getNumber());
       /* ArrayList<String> sizeList = new ArrayList<>();
        if(mBean.getStocks()!=null){
            for(int i =0;i<mBean.getStocks().size();i++){
                if(mBean.getStocks().get(i).getNum()!=0){
                    sizeList.add(mBean.getStocks().get(i).getSize());
                }
            }
            GoodsSizeAdapter mAdapter = new GoodsSizeAdapter(SelfGoodsDetials.this,SelfGoodsDetials.this);
            sizeLayout.setAdapter(mAdapter);
            mAdapter.onlyAddAll(sizeList);

        }*/
        GoodsTagAdapter tagAdapter = new GoodsTagAdapter(SelfGoodsDetials.this);
        tag_layout.setAdapter(tagAdapter);
        String [] str = mBean.getLabel().split(",");
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

    /**
     * 显示商品图Banner
     * @param imgUrls
     */
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
    public void onSeeGoodsDatialListener(int pos, GoodsInfoResultBean.SuccessBean.ListBean listBean) {
        // TODO Auto-generated method stub
        // 大仓 无需操作
    }

    @Override
    public void onSeeMoreListener(int pos,String type) {
        // TODO Auto-generated method stub
        // 大仓 无需操作
    }

    /**
     * 查看推荐热销
     * @param position
     * @param listBean
     */
    @Override
    public void onSeeSelfGoodsDatialListener(int position, ListBean listBean) {
        if(isLogin()){
            Intent intent = new Intent(SelfGoodsDetials.this,SelfGoodsDetials.class);
            Bundle bundle = new Bundle();
            bundle.putString("isToEnd","yes");
            bundle.putString("goodId", String.valueOf(listBean.getId()));
            bundle.putSerializable("goodsDetials", listBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            loginOrRegister();
        }
    }

    /**
     * 查看推荐更多
     * @param position
     * @param type
     */
    @Override
    public void onSeeSelfMoreListener(int position, String type) {
        if(isLogin()){
            Intent intent = new Intent(SelfGoodsDetials.this,SelfKindredGoods.class);
            Bundle bundle = new Bundle();
            bundle.putString("type",type);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            loginOrRegister();
        }

    }

    @OnClick({R.id.layoutCollection, R.id.layoutCustomer, R.id.layoutAddBasket, R.id.imgShare,
            R.id.layoutRightBuy, R.id.layoutBack, R.id.layoutNews, R.id.tvExpressInfo,
            R.id.layoutSelectSize,R.id.layoutLookMore})
    public void onClick(View view) {
        if (view.getId() == R.id.layoutBack) {
            AppManager.getAppManager().finishActivity(this);
            return;
        }
        switch (view.getId()) {
            case R.id.layoutNews:
                /*if(isLogin()){
                    Intent intent2 = new Intent(SelfGoodsDetials.this, ShoppingCartBySelfGoods.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selfGoods", mBean);
                    intent2.putExtras(bundle);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.bottom_in, 0);
                }else{
                    loginOrRegister();
                }*/
                Intent intent0= new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",2);
                intent0.putExtras(bundle);
                startActivity(intent0);
                AppManager.getAppManager().finishAllActivity();
                break;
            case R.id.layoutCollection:
                if(isLogin()){
                    if (getContext() != null && getGoodId() != null) {
                        addGoodsCollectPre.onAddCollect();
                    } else {
                        ToastUtils.showError(getApplicationContext(), "系统错误，请稍候重试");
                    }
                }else{
                    loginOrRegister();
                }

                break;
            case R.id.layoutCustomer:
                if (isLogin()) {
                    Intent intent = new Intent(this, EMMainActivity.class);
                    startActivity(intent);
                } else {
                    QQAuth mqqAuth = QQAuth.createInstance("1106058796", this);
                    WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                    String ESQ = "2252162352";  //客服QQ号
                    int ret = mWPA.startWPAConversation(this, ESQ, null);
                    if (ret != 0) {
                        Toast.makeText(this,
                                "抱歉，联系客服出现了错误~. error:" + ret,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.layoutAddBasket://添加购物车
                if(isLogin()){
                    Intent intent1 = new Intent(SelfGoodsDetials.this, ShoppingCartBySelfGoods.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("selfGoods", mBean);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.bottom_in, 0);
                }else{
                    loginOrRegister();
                }
                break;
            case R.id.layoutRightBuy://立即购买
                if(isLogin()){
                    Intent intent3 = new Intent(SelfGoodsDetials.this, SelfGoodsSizeNumsChoose.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("selfGoods", mBean);
                    intent3.putExtras(bundle3);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.fade_in, 0);
                }else{
                    loginOrRegister();
                }

                break;
            case R.id.tvExpressInfo:
                showExpressDialog();
                break;
            case R.id.imgShare:
                if(isLogin()){
                    showShare();
                }else{
                    loginOrRegister();
                }
                break;
            case R.id.layoutSelectSize://选择尺码
                Intent intent3 = new Intent(SelfGoodsDetials.this,ChooseSizeNumsActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("selfGoods", mBean);
                intent3.putExtras(bundle3);
                startActivityForResult(intent3,0);
                overridePendingTransition(R.anim.bottom_in, 0);
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

    private void loginOrRegister() {
        Intent intent = new Intent(SelfGoodsDetials.this,RegisterAccount.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.animator.fade_out);
    }


    /**
     * 显示运费模版
     */
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

    private void shareByByQQ() {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(mBean.getName());
        //    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
        sp.setText(mBean.getBrand()+" " + mBean.getNumber() + " " +
                mBean.getLabel() + "\n" + "￥ " + mBean.getYmprice() );
        sp.setSite("http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");
        sp.setSiteUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");
        Platform qzone = ShareSDK.getPlatform (QZone.NAME);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener (new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }
            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
        // 执行图文分享
        qzone.share(sp);
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        /* oks.setTitle(mBean.getName()+"\n"+
                "标签：" + mBean.getLabel() + "\n" +
                "价格：￥" + mBean.getYmprice() + "\n" +
                "尺码：" + mBean.getStocks().toString() + "\n" +
                "细节实拍图请app查看（应用市场搜“J货”，下载安装）" );*/  //最多30个字符
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        /*oks.setComment( "标签：" + mBean.getLabel() + "\n" +
                "价格：￥" + mBean.getYmprice() + "\n" +
                "尺码：" + mBean.getStocks().toString() + "\n" +
                "细节实拍图请app查看（应用市场搜“J货”，下载安装）");*/
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mBean.getName()+"\n" +
                "标签：" + mBean.getLabel() + "\n" +
                "价格：￥" + mBean.getYmprice() + "\n" +
                "尺码：" + mBean.getStocks().toString() + "\n" +
                "细节实拍图请app查看（应用市场搜“J货”，下载安装）" + "\n" +
                "http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");  //最多40个字符

        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(ConUtils.SElF_GOODS_IMAGE_URL + mBean.getPic().split(",")[0]);
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");   //网友点进链接后，可以看到分享的详情
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.yunma");  //网友点进链接后，可以看到分享的详情
        oks.show(this);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public ShoppingCartsBean getShoppingCartsBean() {
        return null;
    }

    /**
     * 添加购物车返回
     * @param resultBean
     * @param msg
     */
    @Override
    public void showAddShoppingCartsInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
        }
    }

    /**
     * 查询收藏
     * @param resultBean
     * @param msg
     */
    @Override
    public void showCollectInfos(AddGoodsCollectBean resultBean, String msg) {
        if(resultBean!=null){
            GlideUtils.glidLocalDrawable(getApplicationContext(),ImgCollection,R.drawable.star_yellow);
        }
    }

    /**
     * 查找同类货品返回
     * @param resultBean
     * @param msg
     */
    @Override
    public void showSearchInfosByType(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
            layoutSimilar.setVisibility(View.GONE);
        }else{
            List<ListBean> listBean = resultBean.getSuccess().getList();
            boolean isLastPage = resultBean.getSuccess().isIsLastPage();
            if(listBean ==null|| listBean.size()==0){
                layoutSimilar.setVisibility(View.GONE);
            }else{
                layoutSimilar.setVisibility(View.VISIBLE);
                if(nAdapter==null){
                    nAdapter = new KindredGoodsAdapter(SelfGoodsDetials.this,
                            listBean, SelfGoodsDetials.this,isLastPage);
                    nestlistview.setAdapter(nAdapter);
                }else{
                    nAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public String getGoodId() {
        return goodId;
    }

    /**
     * 添加收藏夹返回
     * @param resultBean
     * @param msg
     */
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
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(SelfGoodsDetials.this);
        ShareSDK.stopSDK(this);
    }

    /**
     * 判断是否登录
     * @return
     */
    @Override
    public boolean isLogin() {
        return super.isLogin();
    }

    /**
     * 添加购物车请求
     * @param size
     */
    @Override
    public void onAddBasketCarts(String size) {
        ShoppingCartsBean shoppingCartsBean = new ShoppingCartsBean();
        shoppingCartsBean.setToken(SPUtils.getToken(getContext()));
        shoppingCartsBean.setNum(1);
        shoppingCartsBean.setSize(size);
        shoppingCartsBean.setGid(getGoodId());
        addShoppingCartsPre.addToBasket(shoppingCartsBean);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(xbanner!=null){
            xbanner.stopAutoPlay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(xbanner!=null){
            xbanner.startAutoPlay();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            if(SPUtils.isAddCartRemind(getApplicationContext())){
                AddToCartsRequestBean cartsRequestBean = (AddToCartsRequestBean)
                        data.getSerializableExtra("cartsRequestBean");
                assert cartsRequestBean != null;
                tvHasSelected.setText(String.valueOf("已选：" +
                        cartsRequestBean.getCarts().toString().substring(1,
                        cartsRequestBean.getCarts().toString().length()-1)));
                tvHasSelected.setTextColor(Color.parseColor("#323232"));
                if(data.getStringExtra("totalNums")!=null&&
                        data.getStringExtra("tvTotalPrice")!=null){
                    showAddCartsDialog(data.getStringExtra("totalNums"),
                            data.getStringExtra("tvTotalPrice"));
                }
            }else{
                ToastUtils.showError(getApplicationContext(),"弹窗关闭");
            }
        }
    }

    private void showAddCartsDialog(final String totalNums, final String tvTotalPrice) {
        Intent intent = new Intent(SelfGoodsDetials.this,AddCartsRemindActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("totalNums",totalNums);
        bundle.putString("tvTotalPrice",tvTotalPrice);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
