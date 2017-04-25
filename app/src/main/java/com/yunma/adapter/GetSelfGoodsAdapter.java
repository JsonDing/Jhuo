package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yunma.R;
import com.yunma.jhuo.activity.AdInfosDetialsActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.homepage.TomorrowPreSaleActivity;
import com.yunma.jhuo.activity.mine.SystemNotice;
import com.yunma.bean.AdInfoResultBean;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.bean.StocksBean;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.utils.*;
import com.yunma.widget.FlowTagLayout;
import com.yunma.widget.NestListView;

import java.util.*;

/**
 * Created by Json on 2017/3/1.
 */
public class GetSelfGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private static final int ITEM_TYPE_AD = 3;
    private static final int ITEM_TYPE_UPDATE = 4;
    private static final int mHeaderCount = 1;//头部View个数
    private static final int mBottomCount = 1;//底部View个数
    private static final int mAdCount = 1;
    private int mTomorrow = 0;
    private int adPosition = 7;
    private int tomorrowPosition = 16;
    private Context mContext;
    private LayoutInflater inflater;
    private List<ListBean> goodsListBeen;
    private List<ListBean> tomorrowListBean;
    private List<String> adList;
    private List<String> bannerList;
    private AdInfoResultBean adInfoResultBean;
    private ArrayList<String> sizeList;
    private OnBuyClick onBuyClick;
    private boolean isEnd = false;
    private XBanner view;
    public GetSelfGoodsAdapter(Context mContext,OnBuyClick onBuyClick) {
        // TODO: 2017-04-13
        this.mContext = mContext;
        this.onBuyClick = onBuyClick;
        this.adList = new ArrayList<>();
        this.bannerList = new ArrayList<>();
        this.goodsListBeen = new ArrayList<>();
        this.tomorrowListBean = new ArrayList<>();
        this.adInfoResultBean = new AdInfoResultBean();
        inflater = LayoutInflater.from(mContext);
        adPosition = new Random().nextInt(15)%(8) + 5;
        if(adPosition%2==0){
            adPosition = adPosition + 1;
        }
        tomorrowPosition = adPosition + 7;
    }

    @Override
    public int getItemCount() {
        if(goodsListBeen.size()==0){
            return mHeaderCount;
        }else{
            return mHeaderCount + getBodyItemCount() + mBottomCount + mAdCount + mTomorrow;
        }
    }

    //内容长度
    private int getBodyItemCount(){
        return goodsListBeen.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
       // return mHeaderCount != 0 && position < mHeaderCount;
        return position < mHeaderCount;
    }
    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return position >= (mHeaderCount + getBodyItemCount() + mAdCount + mTomorrow);
    }
    //判断当前item是否是AdView
    public boolean isAdView(int position) {
        return position == adPosition;
    }
    //判断当前item是否是AdView
    public boolean isTomorrowUpdate(int position) {
       if(position==tomorrowPosition){
           mTomorrow = 1;
       }
        return position == tomorrowPosition;
    }
    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
        if (position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (position >= (mHeaderCount + dataItemCount + mAdCount + mTomorrow)){
            //底部View
            return ITEM_TYPE_BOTTOM;
        }else if (position == adPosition){
            //广告
            return ITEM_TYPE_AD;
        }else if (position == tomorrowPosition){
            //明日更新
            return ITEM_TYPE_UPDATE;
        }else {
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
            return new HeaderViewHolder(inflater.inflate(R.layout.mainitem_head, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return  new BodyViewHolder(inflater.inflate(R.layout.item_self_goods, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(inflater.inflate(R.layout.mainitem_foot, parent, false));
        } else if (viewType == ITEM_TYPE_AD){
            return new AdViewHolder(inflater.inflate(R.layout.mainitem_ad,parent,false));
        } else if (viewType == ITEM_TYPE_UPDATE){
            return new TomorrowHolder(inflater.inflate(R.layout.item_tomorrow_update,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        // TODO: 2017-04-15 Banner页
        if(holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).marqueeView.startWithList(adList);
            ((HeaderViewHolder) holder).marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, TextView textView) {
                    Intent intent = new Intent(mContext, SystemNotice.class);
                    mContext.startActivity(intent);
                }
            });
            if(bannerList!=null){
                ((HeaderViewHolder) holder).xbanner.setData(bannerList, null);
                ((HeaderViewHolder) holder).xbanner.setmAdapter(new XBanner.XBannerAdapter() {
                    public void loadBanner(XBanner banner, View view, int pos) {
                       GlideUtils.glidBanner(mContext,(ImageView) view,
                               ConUtils.BANNER_IMAGE + bannerList.get(pos));
                    }
                });
                ((HeaderViewHolder) holder).xbanner.setPageTransformer(Transformer.Alpha);
            }
            ((HeaderViewHolder) holder).layoutLookAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }else if(holder instanceof BodyViewHolder) {
            // TODO: 2017-04-15 货品List
            int contentPostion = 1;
            if(0<position&&position<adPosition){
                contentPostion = position - 1;
            }else if(position>adPosition&&position<tomorrowPosition){
                contentPostion = position - 2;
            }else if(position>tomorrowPosition){
                contentPostion = position - 3;
            }
            int remainNums = 0;
            for(int i=0;i<goodsListBeen.get(contentPostion).getStocks().size();i++){
                remainNums = remainNums + goodsListBeen.get(contentPostion).getStocks().get(i).getNum();
            }
            if(EmptyUtil.isNotEmpty(goodsListBeen.get(contentPostion).getLabel())){
                ((BodyViewHolder) holder).tvTags.setText(goodsListBeen.get(contentPostion)
                        .getLabel().substring(0,2));
            }
            ((BodyViewHolder) holder).tvRemainNums.setText(String.valueOf(remainNums));
            if(remainNums==0){
                ((BodyViewHolder) holder).imgSoldOut.setVisibility(View.VISIBLE);//shopping_button
            }else{
                ((BodyViewHolder) holder).imgSoldOut.setVisibility(View.GONE);
            }
            GlideUtils.glidNormle(mContext,((BodyViewHolder) holder).imgGoods,ConUtils.SElF_GOODS_IMAGE_URL
                    + goodsListBeen.get(contentPostion).getPic().split(",")[0]);
            ((BodyViewHolder) holder).tvGoodsName.setText(goodsListBeen.get(contentPostion).getName());
            ((BodyViewHolder) holder).tvSalePrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                    goodsListBeen.get(contentPostion).getSaleprice())));
            ((BodyViewHolder) holder).tvSalePrice.getPaint().setFlags(
                    Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
            ((BodyViewHolder) holder).tvYunmaPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                    goodsListBeen.get(contentPostion).getYmprice())));
            if(goodsListBeen.get(contentPostion).getStocks()!=null){
                List<StocksBean> stocksList = goodsListBeen.get(contentPostion).getStocks();
                int lenth = goodsListBeen.get(contentPostion).getStocks().size();
                sizeList = new ArrayList<>();
                for(int i =0;i<lenth;i++){
                    if(stocksList.get(i).getNum()!=0){
                        sizeList.add(stocksList.get(i).getSize());
                    }
                }
                SizeAdapter mAdapter = new SizeAdapter(mContext);
                ((BodyViewHolder) holder).tagSize.setAdapter(mAdapter);
                if(sizeList.size()>5){
                    mAdapter.onlyAddAll(sizeList.subList(0,5));
                }else{
                    mAdapter.onlyAddAll(sizeList);
                }
            }
            if(contentPostion %2 ==0){
                ((BodyViewHolder) holder).view2.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).view1.setVisibility(View.GONE);
            }else{
                ((BodyViewHolder) holder).view1.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).view2.setVisibility(View.GONE);
            }
            final int finalContentPostion = contentPostion;
            ((BodyViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int goodsId = goodsListBeen.get(finalContentPostion).getId();
                    if(null!=onBuyClick){
                        onBuyClick.onLookGoodDetial(goodsId, finalContentPostion,goodsListBeen.get(finalContentPostion));
                    }
                }
            });

        } else if (holder instanceof BottomViewHolder) {
            // TODO: 2017-04-15 底部
            ((BottomViewHolder)holder).layoutFoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                    mContext.startActivity(intent);
                }
            });
            if(isEnd){
                ((BottomViewHolder)holder).tvBottom.setText("这次真的到底了");
            }else{
                ((BottomViewHolder)holder).tvBottom.setText("我们是有底线的");
            }

        }else if(holder instanceof AdViewHolder){
            // TODO: 2017-04-15  广告
            if(adInfoResultBean.getSuccess()!=null&&adInfoResultBean.getSuccess().get(0)!=null){
                ((AdViewHolder)holder).tvAdTittle.setText(
                        adInfoResultBean.getSuccess().get(0).getTitle());

                GlideUtils.glidNormleFast(mContext,((AdViewHolder)holder).adView,
                        ConUtils.BANNER_IMAGE + adInfoResultBean.getSuccess().get(0).getPic());
                ((AdViewHolder)holder).layoutAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.mainContext,AdInfosDetialsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("adInfos",adInfoResultBean.getSuccess().get(0));
                        intent.putExtras(bundle);
                        MainActivity.mainContext.startActivity(intent);
                    }
                });
            }
        }else if(holder instanceof TomorrowHolder){
            // TODO: 2017-04-15 明日更新
            ((TomorrowHolder)holder).layoutLookAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showSuccess(mContext,"点击了查看全部");
                    Intent intent = new Intent(MainActivity.mainContext,TomorrowPreSaleActivity.class);
                    MainActivity.mainContext.startActivity(intent);
                }
            });
            TomorrowUpdateAdapter updateAdapter = new TomorrowUpdateAdapter(mContext,tomorrowListBean);
            ((TomorrowHolder)holder).nestListView.setAdapter(updateAdapter);
        }
    }

    //内容列表
    private class BodyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvTags;
        TextView tvYunmaPrice;
        TextView tvSalePrice;
        TextView tvRemainNums;
        ImageView imgSoldOut;
        FlowTagLayout tagSize;
        View layout,view1,view2;

        BodyViewHolder(View view) {
            super(view);
            imgGoods = (ImageView) view.findViewById(R.id.imgGoods);
            tvTags = (TextView)view.findViewById(R.id.tvTags);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvYunmaPrice = (TextView) view.findViewById(R.id.tvYunmaPrice);
            tvSalePrice = (TextView) view.findViewById(R.id.tvSalePrice);
            tvRemainNums = (TextView) view.findViewById(R.id.tvRemainNums);
            imgSoldOut = (ImageView)view.findViewById(R.id.imgSoldOut);
            tagSize = (FlowTagLayout)view.findViewById(R.id.tagSize);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            layout = view.findViewById(R.id.layout);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,5))/2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

    //头部 ViewHolder
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private XBanner xbanner;
        private View layoutLookAll;
        private MarqueeView marqueeView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            xbanner = (XBanner) itemView.findViewById(R.id.xbanner);
            layoutLookAll = itemView.findViewById(R.id.layoutLookAll);
            marqueeView = (MarqueeView)itemView.findViewById(R.id.marqueeView);
            setView(xbanner);
        }
    }

    //底部 ViewHolder
    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private View layoutFoot;
        private TextView tvBottom;
        BottomViewHolder(View itemView) {
            super(itemView);
            layoutFoot = itemView.findViewById(R.id.layoutFoot);
            tvBottom = (TextView) itemView.findViewById(R.id.tvBottom);
        }
    }

    //广告栏
    private static class AdViewHolder extends RecyclerView.ViewHolder {
        private View layoutAd;
        private ImageView adView;
        private TextView tvAdTittle;
        AdViewHolder(View itemView) {
            super(itemView);
            layoutAd = itemView.findViewById(R.id.layoutAd);
            adView = (ImageView) itemView.findViewById(R.id.adView);
            tvAdTittle = (TextView) itemView.findViewById(R.id.tvAdTittle);
        }
    }

    //明日更新
    private static class TomorrowHolder extends RecyclerView.ViewHolder{
        private NestListView nestListView;
        private LinearLayout layoutLookAll;
        TomorrowHolder(View itemView) {
            super(itemView);
            nestListView = (NestListView) itemView.findViewById(R.id.nestlistview);
            layoutLookAll = (LinearLayout) itemView.findViewById(R.id.layoutLookAll);
        }
    }


    public void setGoodsListBeen(List<ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyDataSetChanged();
    }

    public void setDatas(List<String> bannerList,List<String> adList,
                         AdInfoResultBean adInfoResultBean,List<ListBean> tomorrowListBean,
                         List<ListBean> goodsListBeen){
        this.adList = adList;
        this.bannerList = bannerList;
        if(tomorrowListBean!=null){
            this.tomorrowListBean = tomorrowListBean;
        }
        if(adInfoResultBean!=null){
            this.adInfoResultBean = adInfoResultBean;
        }
        this.goodsListBeen = goodsListBeen;
        notifyDataSetChanged();
    }

    public void setAdList(List<String> adList) {
        this.adList = adList;
        notifyDataSetChanged();
    }

    public void setBannerList(List<String> bannerList) {
        this.bannerList = bannerList;
        notifyDataSetChanged();
    }

    public void setAdInfoResultBean(AdInfoResultBean adInfoResultBean) {
        this.adInfoResultBean = adInfoResultBean;
        notifyDataSetChanged();
    }

    public void setEnd(boolean end) {
        this.isEnd = end;
        notifyDataSetChanged();
    }

    public void setTomorrowListBean(List<ListBean> tomorrowListBean) {
        this.tomorrowListBean = tomorrowListBean;
        notifyDataSetChanged();
    }

    private class SizeAdapter extends BaseAdapter {
        private List<String> mSize = new ArrayList<>();
        private LayoutInflater layoutInflater;

        SizeAdapter(Context mContext) {
            layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mSize.size();
        }

        @Override
        public Object getItem(int position) {
            return mSize.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.size_home_item, parent, false);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_tag);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvName.setText(String.valueOf(mSize.get(position)));
            return convertView;
        }
        void onlyAddAll(List<String> list) {
            this.mSize = list;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView tvName;
        }
    }
}
