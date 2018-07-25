package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GetCollectResultBean;
import com.yunma.jhuo.activity.mine.SimilarGoodsActivity;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.PriceUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-16
 *
 * @author Json.
 */
public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<GetCollectResultBean.SuccessBean> success;

    public BookmarksAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.success = new ArrayList<>();
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_book_marks, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (success.get(position).getRepoid() == 1) {
            if (success.get(position).getPic() != null) {
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        success.get(position).getPic().split(",")[0] + "/min");
            } else {
                holder.imgGoods.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.default_pic));
            }
        } else {
            if (success.get(position).getPic() != null) {
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        success.get(position).getPic().split(",")[0] + "/min");
            } else {
                holder.imgGoods.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.default_pic));
            }
        }
        double yunPrice = success.get(position).getYunmaprice();
        double specialPrice = success.get(position).getSpecialprice();
        if (PriceUtils.isShowSpecialPrice(yunPrice,specialPrice)) {
            holder.loSpecialOffers.setVisibility(View.VISIBLE);
            holder.imgSpecialOffer.setVisibility(View.VISIBLE);
            holder.textview2.setText("原价：");
            holder.textview2.setTextColor(ContextCompat.getColor(mContext, R.color.b2));
            holder.textview3.setTextColor(ContextCompat.getColor(mContext, R.color.b2));
            SpannableStringBuilder span = new SpannableStringBuilder(
                    "缩进 " + success.get(position).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGoodsName.setText(span);
            String s = PriceUtils.getPrice(yunPrice,specialPrice);
            SpannableStringBuilder ss = ValueUtils.changeTextSize(
                    mContext, s, 15, 0, s.indexOf("."));
            holder.tvGoodsPrice.setText(ss);
            holder.tvPrimePrice.setText(
                    ValueUtils.toTwoDecimal(success.get(position).getYunmaprice()));
            holder.tvPrimePrice.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
        } else {
            holder.loSpecialOffers.setVisibility(View.INVISIBLE);
            holder.imgSpecialOffer.setVisibility(View.GONE);
            holder.textview2.setText("售价：");
            holder.textview2.setTextColor(ContextCompat.getColor(mContext, R.color.b2));
            holder.textview3.setTextColor(ContextCompat.getColor(mContext, R.color.b4));
            holder.tvPrimePrice.setTextColor(ContextCompat.getColor(mContext, R.color.b4));
            holder.tvGoodsName.setText(success.get(position).getName());
            String s = PriceUtils.getPrice(yunPrice,specialPrice);
            SpannableStringBuilder ss = ValueUtils.changeTextSize(
                    mContext, s, 15, 0, s.indexOf("."));
            holder.tvPrimePrice.setText(ss);
        }
        if (success.get(position).getIssue() != 1 ){
            holder.mOffSaleImg.setVisibility(View.VISIBLE);
        } else {
            holder.mOffSaleImg.setVisibility(View.INVISIBLE);
        }
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onDelClick(holder.getLayoutPosition(),
                            success.get(holder.getLayoutPosition()).getId());
                }
            }
        });

        holder.btnLooksimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SimilarGoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goodsInfo", success.get(holder.getLayoutPosition()));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.layoutSeeDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(success.get(holder.getLayoutPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return success.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
        @BindView(R.id.btnLooksimilar) Button btnLooksimilar;
        @BindView(R.id.layoutDelete) LinearLayout layoutDelete;
        @BindView(R.id.layoutSeeDetials) View layoutSeeDetials;
        @BindView(R.id.tvPrimePrice) TextView tvPrimePrice;
        @BindView(R.id.imgSpecialOffer) ImageView imgSpecialOffer;
        @BindView(R.id.loSpecialOffers) View loSpecialOffers;
        @BindView(R.id.textview2) TextView textview2;
        @BindView(R.id.textview3) TextView textview3;
        @BindView(R.id.mOffSaleImg) ImageView mOffSaleImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClick {
        void onItemClick(GetCollectResultBean.SuccessBean successBean);

        void onDelClick(int position, int goodId);
    }

    public void AddListBean(List<GetCollectResultBean.SuccessBean> success) {
        this.success = success;
        notifyItemInserted(success.size());
    }

    public void remove(int position) {
        success.remove(position);
        notifyItemRemoved(position);
    }
}
