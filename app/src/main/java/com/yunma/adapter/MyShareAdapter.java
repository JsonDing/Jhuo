package com.yunma.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.ShareListBean;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.UserInfosDao;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.SPUtils;
import com.yunma.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2018-07-24
 *
 * @author Json.
 */
public class MyShareAdapter extends RecyclerView.Adapter<MyShareAdapter.ViewHolder> {

    private Activity mActivity;
    private OnItemClick onItemClick;
    private LayoutInflater inflater;
    private List<ShareListBean> shareList;

    public MyShareAdapter(Activity activity, OnItemClick onItemClick) {
        this.mActivity = activity;
        this.onItemClick = onItemClick;
        this.shareList = new ArrayList<>();
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                inflater.inflate(R.layout.item_my_share, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    public void setData(List<ShareListBean> shareList) {
        this.shareList = shareList;
        notifyItemInserted(this.shareList.size());
    }

    public void addData(List<ShareListBean> shareList) {
        this.shareList.addAll(shareList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.shareList.clear();
        notifyDataSetChanged();
    }

    public void removePos(int pos) {
        this.shareList.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgUserPhoto) CircleImageView imgUserPhoto;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvNumber) TextView tvNumber;
        @BindView(R.id.tvStocks) TextView tvStocks;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.loPrice) LinearLayout loPrice;
        @BindView(R.id.tvShareCount) TextView tvShareCount;
        @BindView(R.id.recyclerView) RecyclerView recyclerView;
        @BindView(R.id.tvEdit) TextView tvEdit;
        @BindView(R.id.tvDelete) TextView tvDelete;
        @BindView(R.id.btnShare) Button btnShare;
        @BindView(R.id.btnBuy) Button btnBuy;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            UserInfos userInfos = getUserDao().load(Long.valueOf(SPUtils.getUserId(mActivity)));
            if (userInfos != null) {
                if (userInfos.getNickName() != null) {
                    tvUserName.setText(userInfos.getNickName());
                } else {
                    tvUserName.setText(userInfos.getPhoneNumber());
                }
                if (userInfos.getImgsPhotos() != null && !userInfos.getImgsPhotos().isEmpty()) {
                    GlideUtils.glidNormle(mActivity, imgUserPhoto,
                            ConUtils.HEAD_IMAGE_URL + userInfos.getImgsPhotos());
                } else {
                    imgUserPhoto.setImageDrawable(
                            ContextCompat.getDrawable(mActivity, R.drawable.head_portrait));
                }
            }
        }
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    public interface OnItemClick {
        void onShareClick(ShareListBean shareBean);

        void onGoBuyclick(String number);

        void onEditClick(ShareListBean shareBean);

        void onDeleteClick(int position, String ids);
    }


}
