package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.mine.RefundMoreGoods;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.utils.*;
import com.yunma.widget.NiceSpinner;

import java.util.*;

/**
 * Created by Json on 2017/3/16.
 */

public class RefundMoreGoodsAdapter extends RecyclerView.Adapter<RefundMoreGoodsAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private SaveImgsListener saveImgsListener;
    private List<OrderdetailsBean> listBean;
    private List<String> dataset = new LinkedList<>(
            Arrays.asList("-请选择-", "大小鞋", "顺鞋", "有色差", "尺码不合适", "质量问题",
                    "其他问题"));
    public RefundMoreGoodsAdapter(RefundMoreGoods mContext, List<OrderdetailsBean> listBean, RefundMoreGoods saveImgsListener) {
        this.mContext = mContext;
        this.saveImgsListener = saveImgsListener;
        this.listBean = listBean;
        inflater = LayoutInflater.from(mContext);
        initNewPic();
    }

    private void initNewPic() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_page, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,30))/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,width);
        holder.layoutImgs.setLayoutParams(params);
        holder.etDetials.addTextChangedListener(new TextSwitcher(holder,position));
        holder.spinner.attachDataSource(dataset);
        holder.spinner.addOnItemClickListener(new OnMyItemClickListener(holder,position));
        //通过设置tag，防止position紊乱
        holder.etDetials.setTag(position);
        holder.spinner.setTag(position);
        holder.spinner.setSelectedIndex(listBean.get(position).getSelectedPos());
        holder.tvGoodsName.setText(listBean.get(position).getInfo());
        holder.tvGoodsInfo.setText("颜色：如图  " + "尺码：" +
                listBean.get(position).getSize());
        holder.tvPrice.setText("￥" + listBean.get(position).getUserprice());
        holder.tvGoodsNum.setText("X" + listBean.get(position).getNum());
        if(listBean.get(position).getRemark()==null||listBean.get(position).getRemark().equals("")){
            holder.etDetials.setHint("(选填) 请您描述详细问题");
        }else{
            holder.etDetials.setText(listBean.get(position).getRemark());
        }
        if(listBean.get(position).getRepoid()==1){
            if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        listBean.get(position).getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }else{
            if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        listBean.get(position).getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }
        holder.imgsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=saveImgsListener){
                    saveImgsListener.savaImgs(holder.getAdapterPosition());
                }
            }
        });
        if(listBean.get(position).getImgsPath()!=null){
            if(listBean.get(position).getImgsPath().size()==1){
                holder.imgsOne.setVisibility(View.VISIBLE);
                holder.imgsTwo.setVisibility(View.GONE);
                holder.imgsThree.setVisibility(View.GONE);
                holder.imgsAdd.setVisibility(View.VISIBLE);
                GlideUtils.loadSDImgs(mContext,holder.imgsOne,
                        listBean.get(position).getImgsPath().get(0).getImgsPath());

            }else if(listBean.get(position).getImgsPath().size()==2){
                holder.imgsOne.setVisibility(View.VISIBLE);
                holder.imgsTwo.setVisibility(View.VISIBLE);
                holder.imgsThree.setVisibility(View.GONE);
                holder.imgsAdd.setVisibility(View.VISIBLE);
                GlideUtils.loadSDImgs(mContext,holder.imgsOne,
                        listBean.get(position).getImgsPath().get(0).getImgsPath());
                GlideUtils.loadSDImgs(mContext,holder.imgsTwo,
                        listBean.get(position).getImgsPath().get(1).getImgsPath());
            }else if(listBean.get(position).getImgsPath().size()==3){
                holder.imgsOne.setVisibility(View.VISIBLE);
                holder.imgsTwo.setVisibility(View.VISIBLE);
                holder.imgsThree.setVisibility(View.VISIBLE);
                holder.imgsAdd.setVisibility(View.GONE);
                GlideUtils.loadSDImgs(mContext,holder.imgsOne,
                        listBean.get(position).getImgsPath().get(0).getImgsPath());
                GlideUtils.loadSDImgs(mContext,holder.imgsTwo,
                        listBean.get(position).getImgsPath().get(1).getImgsPath());
                GlideUtils.loadSDImgs(mContext,holder.imgsThree,
                        listBean.get(position).getImgsPath().get(2).getImgsPath());
            }
        }else{
            holder.imgsOne.setVisibility(View.GONE);
            holder.imgsTwo.setVisibility(View.GONE);
            holder.imgsThree.setVisibility(View.GONE);
            holder.imgsAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGoods;
        ImageView imgsAdd,imgsOne,imgsTwo,imgsThree;
        TextView tvGoodsName;
        TextView tvGoodsInfo;
        TextView tvPrice;
        TextView tvGoodsNum;
        NiceSpinner spinner;
        EditText etDetials;
        TextView tvCanInput;
        LinearLayout layoutImgs;
        ViewHolder(View view) {
            super(view);
            imgGoods = view.findViewById(R.id.imgGoods);
            tvGoodsName = view.findViewById(R.id.tvGoodsName);
            tvGoodsInfo = view.findViewById(R.id.tvGoodsInfo);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvGoodsNum = view.findViewById(R.id.tvGoodsNum);
            spinner = view.findViewById(R.id.spinner);
            etDetials = view.findViewById(R.id.etDetials);
            tvCanInput = view.findViewById(R.id.tvCanInput);
            imgsAdd = view.findViewById(R.id.imgsAdd);
            imgsOne = view.findViewById(R.id.imgsOne);
            imgsTwo = view.findViewById(R.id.imgsTwo);
            imgsThree = view.findViewById(R.id.imgsThree);
            layoutImgs = view.findViewById(R.id.layoutImgs);
        }
    }


    public interface SaveImgsListener{
        void savaImgs(int position);
    }
    public interface SaveEditListener{

        void SaveEdit(int position, String string);
    }
    public interface SaveSpinnerListener{

        void SaveSpinner(int position, String string);
    }

    private class TextSwitcher implements TextWatcher{
        private ViewHolder mHolder;
        private int clickPos;

        TextSwitcher(ViewHolder mHolder, int position) {
            this.mHolder = mHolder;
            this.clickPos = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //用户输入完毕后，处理输入数据，回调给主界面处理
            SaveEditListener listener= (SaveEditListener) mContext;
            if(s!=null){
                listener.SaveEdit(Integer.parseInt(mHolder.etDetials.getTag().toString()),s.toString());
                listBean.get(clickPos).setRemark(s.toString());
            }
        }
    }

    private class OnMyItemClickListener implements AdapterView.OnItemClickListener {
        private ViewHolder mHolder;
        private int clickPos;

        OnMyItemClickListener(ViewHolder holder, int position) {
            this.mHolder = holder;
            this.clickPos = position;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SaveSpinnerListener listener = (SaveSpinnerListener) mContext;
            listener.SaveSpinner(Integer.parseInt(mHolder.spinner.getTag().toString()),dataset.get(position));
            listBean.get(clickPos).setSelectedPos(position);
        }
    }

    public List<OrderdetailsBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<OrderdetailsBean> listBean) {
        this.listBean = listBean;
    }
}
