package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yunma.R;
import com.yunma.bean.BannerBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.jhuo.activity.AdInfosDetialsActivity;
import com.yunma.jhuo.activity.FeatureGoodsActivity;
import com.yunma.jhuo.activity.GoodsSortsActivity;
import com.yunma.jhuo.activity.HomePageModelActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.TodayOnSaleActivity;
import com.yunma.jhuo.activity.TomorrowPreviewActivity;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.SystemNoticeActivity;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ModelValue;
import com.yunma.utils.PriceUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.MyListView;
import com.yunma.widget.NestListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Json on 2017/3/1.
 */
public class GetSelfGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int mHeaderCount = 1;//头部View个数
    private static final int mFooterCount = 1;//底部View个数
    private static final int ITEM_TYPE_FOOTER = 2;
    private int mTomorrow = 0;
    private Context mContext;
    private LayoutInflater inflater;
    private List<SelfGoodsListBean> goodsListBeen;
    private List<String> adTextList;
    private List<BannerBean> tittleBannerList;
    private OnBuyClick onBuyClick;
    private XBanner view;
    private String todayPic = null,tomorrowPic = null,yesterdayPic = null;
    private String tvModelOne,tvModelTwo,tvModelThree,tvModelFour,tvModelFive;
    private List<List<BannerBean>> dataOne;
    private List<List<BannerBean>> dataTwo;
    private List<List<BannerBean>> dataThree;
    private List<List<BannerBean>> dataFour;
    private List<BannerBean> dataFive;
    private Animation mAnimation;
    private int totalGoods;
    public GetSelfGoodsAdapter(Context mContext,OnBuyClick onBuyClick) {
        this.mContext = mContext;
        this.onBuyClick = onBuyClick;
        this.adTextList = new ArrayList<>();
        this.tittleBannerList = new ArrayList<>();
        this.goodsListBeen = new ArrayList<>();
        this.inflater = LayoutInflater.from(mContext);
        this.tvModelOne = "";
        this.dataOne = new ArrayList<>();
        this.tvModelTwo = "";
        this.dataTwo = new ArrayList<>();
        this.tvModelThree = "";
        this.dataThree = new ArrayList<>();
        this.tvModelFour = "";
        this.dataFour = new ArrayList<>();
        this.tvModelFive = "";
        this.dataFive = new ArrayList<>();
        this.totalGoods = 0;
    }

    @Override
    public int getItemCount() {
        int size = goodsListBeen.size();
        if(size == 0){
            return mHeaderCount;
        } else if(size == this.totalGoods){
            return mHeaderCount + getBodyItemCount() +  mTomorrow + mFooterCount;
        } else{
            return mHeaderCount + getBodyItemCount() +  mTomorrow;
        }
    }

    //内容长度
    private int getBodyItemCount(){
        return goodsListBeen.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return position < mHeaderCount;
    }
    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return position == (mHeaderCount + this.totalGoods + mTomorrow + mFooterCount - 1);
    }
    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if(position == mHeaderCount + mTomorrow + mFooterCount + this.totalGoods - 1){
            // 底部View
            return ITEM_TYPE_FOOTER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    public void setView(XBanner view) {
        this.view = view;
    }

    public XBanner getView(){
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(
                    inflater.inflate(R.layout.mainitem_head, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return  new BodyViewHolder(
                    inflater.inflate(R.layout.item_self_goods, parent, false));
        } else if (viewType == ITEM_TYPE_FOOTER){
            return  new FooterViewHolder(
                    inflater.inflate(R.layout.recycler_bottom_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
       // holder.setIsRecyclable(false);
        //Banner页
        if(holder instanceof HeaderViewHolder) {
            if(todayPic != null){
                     GlideUtils.glidNormleFast(mContext,((HeaderViewHolder) holder).imgToday,todayPic);
            }else{
                GlideUtils.glidLocalDrawable(mContext,((HeaderViewHolder) holder).imgToday,R.drawable.yunma101);
            }
            if(tomorrowPic != null){
                GlideUtils.glidNormleFast(mContext,((HeaderViewHolder) holder).imgPreView,tomorrowPic);
            }else{
                GlideUtils.glidLocalDrawable(mContext,((HeaderViewHolder) holder).imgPreView,R.drawable.yunma85);
            }
            if(yesterdayPic != null){
                GlideUtils.glidNormleFast(mContext,((HeaderViewHolder) holder).imgBefore,yesterdayPic);
            }else{
                GlideUtils.glidLocalDrawable(mContext,((HeaderViewHolder) holder).imgBefore,R.drawable.yunma11);
            }
            ((HeaderViewHolder) holder).marqueeView.startWithList(adTextList);
            if(tittleBannerList != null){
                ((HeaderViewHolder) holder).xbanner.setData(tittleBannerList, null);
                ((HeaderViewHolder) holder).xbanner.setmAdapter(new XBanner.XBannerAdapter() {
                    public void loadBanner(XBanner banner, View view, int pos) {
                       GlideUtils.glidBanner(mContext,(ImageView) view,
                               ConUtils.BANNER_IMAGE + tittleBannerList.get(pos).getPic());
                    }
                });
                ((HeaderViewHolder) holder).xbanner.setPageTransformer(Transformer.Alpha);
                ((HeaderViewHolder) holder).xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                    @Override
                    public void onItemClick(XBanner banner, int position) {
                        if(tittleBannerList.get(position).getTitle().equals("品牌墙")){
                            Intent intent = new Intent(MainActivity.mainActivity,AdInfosDetialsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("adInfos",tittleBannerList.get(position));
                            intent.putExtras(bundle);
                            MainActivity.mainActivity.startActivity(intent);
                        }else{
                            String ids = tittleBannerList.get(position).getNumbers();
                            if(ids != null && !ids.equals("")){
                                Intent intent = new Intent(MainActivity.mainActivity, HomePageModelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id",tittleBannerList.get(position).getNumbers());
                                bundle.putString("title", tittleBannerList.get(position).getTitle());
                                intent.putExtras(bundle);
                                MainActivity.mainActivity.startActivity(intent);
                            }
                        }
                    }
                });
            }
            ((HeaderViewHolder) holder).layoutLookAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                    MainActivity.mainActivity.startActivity(intent);
                }
            });
            ((HeaderViewHolder) holder).marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, TextView textView) {
                    Intent intent = new Intent(MainActivity.mainActivity,SystemNoticeActivity.class);
                    MainActivity.mainActivity.startActivity(intent);
                }
            });
            //昨天
            ((HeaderViewHolder) holder).layoutBefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.mainActivity,FeatureGoodsActivity.class);
                    MainActivity.mainActivity.startActivity(intent);
                }
            });
            //今天
            ((HeaderViewHolder) holder).layoutToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.mainActivity,TodayOnSaleActivity.class);
                    MainActivity.mainActivity.startActivity(intent);
                }
            });
            //明天
            ((HeaderViewHolder) holder).layoutTomorrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.mainActivity,TomorrowPreviewActivity.class);
                    MainActivity.mainActivity.startActivity(intent);
                }
            });
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.img_scale);
            ((HeaderViewHolder) holder).imgGoBargainPrice.setAnimation(mAnimation);
            mAnimation.start();
            ((HeaderViewHolder) holder).imgGoBargainPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,GoodsSortsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("brand",null);
                    bundle.putString("size",null);
                    bundle.putString("type",null);
                    bundle.putString("gender",null);
                    bundle.putString("stocks",null);
                    bundle.putString("sortBy",null);
                    bundle.putString("desc",null);
                    bundle.putString("name",null);// 名称
                    bundle.putString("isBargainPrice","0");
                    bundle.putString("sPrice",null);
                    bundle.putString("ePrice",null);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

            /**
             *  模块一
             */
            if(!"".equals(this.tvModelOne)){
                ((HeaderViewHolder) holder).layoutModelOne.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).tvModelOne.setText(tvModelOne);
                ModelOneAdapter oneAdapter =new ModelOneAdapter(mContext);
                ((HeaderViewHolder) holder).nlModeOne.setAdapter(oneAdapter);
                oneAdapter.setData(dataOne);
            }else{
                ((HeaderViewHolder) holder).layoutModelOne.setVisibility(View.GONE);
            }
            /**
             *  模块二
             */
            if(!"".equals(this.tvModelTwo)){
                ((HeaderViewHolder) holder).layoutModelTwo.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).tvModelTwo.setText(tvModelTwo);
                ModelTwoAdapter twoAdapter =new ModelTwoAdapter(mContext);
                ((HeaderViewHolder) holder).nlModeTwo.setAdapter(twoAdapter);
                twoAdapter.setData(dataTwo);
            }else{
                ((HeaderViewHolder) holder).layoutModelTwo.setVisibility(View.GONE);
            }
            /**
             *  模块三
             */
            if(!"".equals(this.tvModelThree)){
                ((HeaderViewHolder) holder).layoutModelThree.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).tvModelThree.setText(tvModelThree);
                ModelThreeAdapter threeAdapter =new ModelThreeAdapter(mContext);
                ((HeaderViewHolder) holder).nlModeThree.setAdapter(threeAdapter);
                threeAdapter.setData(dataThree);
            }else{
                ((HeaderViewHolder) holder).layoutModelThree.setVisibility(View.GONE);
            }
            /**
             *  模块四
             */
            if(!"".equals(this.tvModelFour)){
                ((HeaderViewHolder) holder).layoutModelFour.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).tvModelFour.setText(tvModelFour);
                ModelFourAdapter fourAdapter =new ModelFourAdapter(mContext);
                ((HeaderViewHolder) holder).nlModeFour.setAdapter(fourAdapter);
                fourAdapter.setData(dataFour);
            }else{
                ((HeaderViewHolder) holder).layoutModelFour.setVisibility(View.GONE);
            }
            /**
             *  模块五
             */
            if(!"".equals(this.tvModelFive)){
                ((HeaderViewHolder) holder).layoutModelFive.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).tvModelFive.setText(tvModelFive);
                ModelFiveAdapter fiveAdapter =new ModelFiveAdapter(mContext);
                ((HeaderViewHolder) holder).lvModelFive.setAdapter(fiveAdapter);
                fiveAdapter.setData(dataFive);
            }else{
                ((HeaderViewHolder) holder).layoutModelFive.setVisibility(View.GONE);
            }
        }else if(holder instanceof BodyViewHolder) {
            int contentPostion = position - 1;
            int remainNums = goodsListBeen.get(contentPostion).getStock();
            if(EmptyUtil.isNotEmpty(goodsListBeen.get(contentPostion).getLabel())){
                ((BodyViewHolder) holder).tvTags.setText(goodsListBeen.get(contentPostion)
                        .getLabel().substring(0,2));
            }
            if(remainNums==0){
                ((BodyViewHolder) holder).imgSoldOut.setVisibility(View.VISIBLE);//shopping_button
            }else{
                ((BodyViewHolder) holder).imgSoldOut.setVisibility(View.GONE);
            }
            String url ;
            if(goodsListBeen.get(contentPostion).getRepoid()==1){
                url = ConUtils.SElF_GOODS_IMAGE_URL;
            }else{
                url = ConUtils.GOODS_IMAGE_URL;
            }
            GlideUtils.glidNormleFast(mContext,((BodyViewHolder) holder).imgGoods,url
                    + goodsListBeen.get(contentPostion).getPic().split(",")[0]);
            ((BodyViewHolder) holder).tvSalePrice.setText(ValueUtils.toTwoDecimal(
                    goodsListBeen.get(contentPostion).getSaleprice()));
            ((BodyViewHolder) holder).tvSalePrice.getPaint().setFlags(
                    Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
            String s;
            double yunPrice = goodsListBeen.get(contentPostion).getYmprice();
            double specialPrice = goodsListBeen.get(contentPostion).getSpecialprice();
            if (PriceUtils.isShowSpecialPrice(yunPrice, specialPrice)) {
                ((BodyViewHolder) holder).imgSpecialOffer.setVisibility(View.VISIBLE);
                SpannableStringBuilder span = new SpannableStringBuilder(
                        "缩进 " + goodsListBeen.get(contentPostion).getName());
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                ((BodyViewHolder) holder).tvGoodsName.setText(span);
                s = PriceUtils.getPrice(yunPrice,specialPrice);
            } else {
                ((BodyViewHolder) holder).imgSpecialOffer.setVisibility(View.GONE);
                ((BodyViewHolder) holder).tvGoodsName.setText(goodsListBeen.get(contentPostion).getName());
                s = PriceUtils.getPrice(yunPrice,specialPrice);
            }
            SpannableStringBuilder ss = ValueUtils.changeTextSize(mContext,s,14,0,s.indexOf("."));
            ((BodyViewHolder) holder).tvYunmaPrice.setText(ss);
            if(contentPostion %2 ==0){
                ((BodyViewHolder) holder).view2.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).view1.setVisibility(View.GONE);
            }else{
                ((BodyViewHolder) holder).view1.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).view2.setVisibility(View.GONE);
            }
            final int pos = contentPostion;
            ((BodyViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=onBuyClick){
                        onBuyClick.onLookGoodDetial(goodsListBeen.get(pos).getId(), pos,
                                goodsListBeen.get(pos));
                    }
                }
            });
        }
    }

    public void setTodayPic(String url) {
        this.todayPic = url;
        notifyItemChanged(0);
    }

    public void setTomorrowPic(String url) {
        this.tomorrowPic = url;
        notifyItemChanged(0);
    }

    public void setBeforePic(String url) {
        this.yesterdayPic = url;
        notifyItemChanged(0);
    }

    public void setTopBanner(List<BannerBean> tittleBannerList){
        this.tittleBannerList = tittleBannerList;
        notifyItemChanged(0);
    }

    public void setAdText(List<String> adTextList){
        this.adTextList = adTextList;
        notifyItemChanged(0);
    }

    public void setModelData(List<BannerBean> success) {
        int size = success.size();
         List<BannerBean> modelOneList = new ArrayList<>();
         List<BannerBean> modelTwoList = new ArrayList<>();
         List<BannerBean> modelThreeList = new ArrayList<>();
         List<BannerBean> modelFourList = new ArrayList<>();
         List<BannerBean> modelFiveList = new ArrayList<>();
        for(int i=0;i<size;i++){
            int sortId = success.get(i).getSort();
            if(sortId<2000&&sortId>1000){
                modelOneList.add(success.get(i));
            }else if(sortId<3000&&sortId>2000){
                modelTwoList.add(success.get(i));
            }else if(sortId<4000&&sortId>3000){
                modelThreeList.add(success.get(i));
            }else if(sortId<5000&&sortId>4000){
                modelFourList.add(success.get(i));
            }else if(5000<sortId&&sortId<6000){
                modelFiveList.add(success.get(i));
            }
            if (sortId==1000){
                this.tvModelOne = success.get(i).getTitle();
            } else if(sortId==2000){
                this.tvModelTwo = success.get(i).getTitle();
            } else if(sortId==3000){
                this.tvModelThree = success.get(i).getTitle();
            } else if(sortId==4000){
                this.tvModelFour = success.get(i).getTitle();
            } else if(sortId==5000){
                this.tvModelFive = success.get(i).getTitle();
            }
        }
        int mOneSize = modelOneList.size();
        if(mOneSize !=0 && mOneSize >=5){
            this.dataOne = ModelValue.getModelData(modelOneList, 5);
        }
        int mTwoSize = modelTwoList.size();
        if(mTwoSize!=0 && mTwoSize >= 6){
            this.dataTwo = ModelValue.getModelData(modelTwoList, 6);
        }
        int mThreeSize = modelThreeList.size();
        if(mThreeSize!=0 && mThreeSize >= 4){
            this.dataThree = ModelValue.getModelData(modelThreeList, 4);
        }
        int mFourSize = modelFourList.size();
        if(mFourSize != 0 && mFourSize >= 3){
            this.dataFour = ModelValue.getModelData(modelFourList, 3);
        }
        if(modelFiveList.size()!=0){
            this.dataFive = modelFiveList;
        }
        notifyDataSetChanged();
    }

    // 货品列表总数
    public void setTotalGoodsNums(int totalGoods) {
        this.totalGoods = totalGoods;
        notifyDataSetChanged();
    }

    //内容列表
    public class BodyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.imgSoldOut) ImageView imgSoldOut;
        @BindView(R.id.imgSpecialOffer) ImageView imgSpecialOffer;
        @BindView(R.id.tvTags) TextView tvTags;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvSalePrice) TextView tvSalePrice;
        @BindView(R.id.tvYunmaPrice) TextView tvYunmaPrice;
        @BindView(R.id.layout) View layout;
        @BindView(R.id.view1) View view1;
        @BindView(R.id.view2) View view2;
        public BodyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,5))/2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

    //头部 ViewHolder
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.xbanner) XBanner xbanner;
        @BindView(R.id.layoutLookAll) View layoutLookAll;
        @BindView(R.id.layoutBefore) View layoutBefore;
        @BindView(R.id.layoutToday) View layoutToday;
        @BindView(R.id.layoutTomorrow) View layoutTomorrow;
        @BindView(R.id.layoutModelOne) View layoutModelOne;
        @BindView(R.id.layoutModelTwo) View layoutModelTwo;
        @BindView(R.id.layoutModelThree) View layoutModelThree;
        @BindView(R.id.layoutModelFour) View layoutModelFour;
        @BindView(R.id.layoutModelFive) View layoutModelFive;
        @BindView(R.id.marqueeView) MarqueeView marqueeView;
        @BindView(R.id.imgToday) ImageView imgToday;
        @BindView(R.id.imgPreView) ImageView imgPreView;
        @BindView(R.id.imgBefore) ImageView imgBefore;
        @BindView(R.id.imgGoBargainPrice) ImageView imgGoBargainPrice;
        @BindView(R.id.imgsBargainPriceBg) ImageView imgsBargainPriceBg;
        @BindView(R.id.tvModelOne) TextView tvModelOne;
        @BindView(R.id.tvModelTwo) TextView tvModelTwo;
        @BindView(R.id.tvModelThree) TextView tvModelThree;
        @BindView(R.id.tvModelFour) TextView tvModelFour;
        @BindView(R.id.tvModelFive) TextView tvModelFive;
        @BindView(R.id.nlModeOne) NestListView nlModeOne;
        @BindView(R.id.nlModeTwo) NestListView nlModeTwo;
        @BindView(R.id.nlModeThree) NestListView nlModeThree;
        @BindView(R.id.nlModeFour) NestListView nlModeFour;
        @BindView(R.id.lvModelFive) MyListView lvModelFive;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int sWith = ScreenUtils.getScreenWidth(mContext);//屏幕宽度
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    sWith,(11*sWith)/36);
            params.setMargins(0,sWith/72,0,0);
            imgsBargainPriceBg.setLayoutParams(params);
            int marginEnd = (22*sWith)/360;
            int marginBottom = (int) (sWith/22.5);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    sWith/5, sWith/5, Gravity.END|Gravity.BOTTOM);
            layoutParams.setMargins(0,0,marginEnd,marginBottom);
            imgGoBargainPrice.setLayoutParams(layoutParams);
            setView(xbanner);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setGoodsListBeen(List<SelfGoodsListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyDataSetChanged();
    }

    public void setDatas(List<SelfGoodsListBean> goodsListBeen){
        if(goodsListBeen != null){
            this.goodsListBeen = goodsListBeen;
        }
        notifyDataSetChanged();
    }

    public void clearData(){
        this.tvModelOne = "";
        this.dataOne.clear();
        this.tvModelTwo = "";
        this.dataTwo.clear();
        this.tvModelThree = "";
        this.dataThree.clear();
        this.tvModelFour = "";
        this.dataFour.clear();
        this.tvModelFive = "";
        this.dataFive.clear();
    }

    public void animStart(){
        mAnimation.start();
    }
}
