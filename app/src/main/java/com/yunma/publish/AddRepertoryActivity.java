package com.yunma.publish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.SelectSizeNumsAdapter;
import com.yunma.bean.*;
import com.yunma.domain.GoodsInfos;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.ModSelfGoodsPre;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;
import com.yunma.widget.WheelView;

import java.lang.ref.WeakReference;
import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

public class AddRepertoryActivity extends MyCompatActivity implements SelectSizeNumsAdapter.OnClickListener, SelfGoodsInterFace.ModSelfGoodsView {

    @BindView(R.id.view) View view;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.rvSelectSizeNums) RecyclerView rvSelectSizeNums;
    @BindView(R.id.btnConfirm) Button btnConfirm;
    private SelectSizeNumsAdapter selectSizeNumsAdapter = null;
    private GetSelfGoodsResultBean.SuccessBean.ListBean listBean = null;
    private ModSelfGoodsPre modSelfGoodsPre = null;
    private List<StocksBean> stocksBeenList = null;
    private CustomProgressDialog dialog = null;
    private ModifyYunmasBean modifyBean = null;
    private List<ModSelfYunmasBean> yunmasBeanList = null;
    private ModSelfYunmasBean yunmasBean = null;
    private NewModSelfGoodsBean newModSelfGoodsBean = null;
    private String mSize = null;
    private int mNums = -1;
    private int clickItem = -1;
    private boolean isReady = false;
    private String goodsType ;
    private Context mContext;
    private MyHandler mHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repertory);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initRecycleView();
        getBundleDatas();
    }

    private void getBundleDatas() {
        Bundle bundle = this.getIntent().getExtras();
        listBean = (GetSelfGoodsResultBean.SuccessBean.ListBean)
                bundle.getSerializable("SelfGoodsDetials");
        assert listBean!=null;
        clickItem = bundle.getInt("ClickItem");
        new Thread(new Runnable() {
            @Override
            public void run() {
                newModSelfGoodsBean = new NewModSelfGoodsBean();
                yunmasBean = new ModSelfYunmasBean();
                yunmasBeanList = new ArrayList<>();
                newModSelfGoodsBean.setToken(SPUtils.getToken(mContext));
                newModSelfGoodsBean.setList("1");
                yunmasBean.setNumber(listBean.getNumber());
                yunmasBean.setPic(listBean.getPic());
                mHandler.sendEmptyMessage(0x333);
            }
        }).start();

        tvGoodsName.setText(listBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        GlideUtils.glidNormleFast(this,imgGoods,
                ConUtils.SElF_GOODS_IMAGE_URL + listBean.getPic().split(",")[0]);
        stocksBeenList = new ArrayList<>();
        int remainNums = 0;
        for(int i=0;i<listBean.getStocks().size();i++){
            if(listBean.getStocks().get(i).getNum()!=0){
                remainNums = remainNums + listBean.getStocks().get(i).getNum();
            }else{
                listBean.getStocks().remove(i);
            }
        }
        goodsType = String .valueOf(listBean.getType());
        if(goodsType.equals("1")){
            if(listBean.getStocks().get(0).getSize().equals("A")){
                goodsType = "1.1";
            }else {
                goodsType = "1.2";
            }
        }
        stocksBeenList.addAll(listBean.getStocks());
        tvGoodsRemain.setText("库存"+ remainNums +"件");
        listBean.setRemain(remainNums);
        selectSizeNumsAdapter.setStocksBeanList(stocksBeenList);
    }

    private void initRecycleView() {
        mContext = this;
        modSelfGoodsPre = new ModSelfGoodsPre(this);
        rvSelectSizeNums.setHasFixedSize(true);
        rvSelectSizeNums.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        selectSizeNumsAdapter = new SelectSizeNumsAdapter(AddRepertoryActivity.this,AddRepertoryActivity.this);
        rvSelectSizeNums.setAdapter(selectSizeNumsAdapter);
    }

    @OnClick({R.id.view, R.id.imgClose, R.id.btnConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case R.id.imgClose:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case R.id.btnConfirm:
                List<ModSelfStocksBean> modSelfStocksBeanList = new ArrayList<>();
                for(int i=0;i<stocksBeenList.size();i++){
                    ModSelfStocksBean bean = new ModSelfStocksBean();
                    bean.setNum(String.valueOf(stocksBeenList.get(i).getNum()));
                    bean.setSize(stocksBeenList.get(i).getSize());
                    modSelfStocksBeanList.add(bean);
                }
                yunmasBean.setStocks(modSelfStocksBeanList);
                yunmasBeanList.add(yunmasBean);
                newModSelfGoodsBean.setYunmas(yunmasBeanList);
                if(isReady){
                    modSelfGoodsPre.modifyRepertory(mContext,newModSelfGoodsBean);
                }else{
                    ToastUtils.showInfo(mContext,"数据打包处理中，请稍候");
                }
                break;
        }
    }

    @Override
    public void onAddNewSize() {
        switch (goodsType) {
            case "1.1":
                showDialog(GoodsInfos.PANTS);
                break;
            case "1.2":
                showDialog(GoodsInfos.CLOTHES);
                break;
            case "2":
                showDialog(GoodsInfos.SHOES);
                break;
            case "3":
                showDialog(GoodsInfos.OTHERS);
                break;
            case "4":
                showDialog(GoodsInfos.CHILD);
                break;
            case "5":
                showDialog(GoodsInfos.CHILD);
                break;
        }
    }

    private void showDialog(final String[] value) {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.wheelview_add_size)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        WheelView _size = (WheelView) contentView.findViewById(R.id.wheel_view_size);
                        WheelView _nums = (WheelView) contentView.findViewById(R.id.wheel_view_nums);
                        _size.setOffset(1);
                        _size.setItems(Arrays.asList(value));
                        _size.setSeletion(0);
                        _nums.setOffset(1);
                        _nums.setItems(Arrays.asList(GoodsInfos.NUMS));
                        _nums.setSeletion(0);
                        setmSize(_size.getSeletedItem());
                        setmNums(Integer.valueOf(_nums.getSeletedItem()));
                        _size.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                            @Override
                            public void onSelected(int selectedIndex, String item) {
                                super.onSelected(selectedIndex, item);
                                if (!item.isEmpty()) {
                                    setmSize(item);
                                }
                            }
                        });
                        _nums.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                            @Override
                            public void onSelected(int selectedIndex, String item) {
                                super.onSelected(selectedIndex, item);
                                if (!item.isEmpty()) {
                                    setmNums(Integer.valueOf(item));
                                }

                            }
                        });
                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                        if(isSizeExist()){
                            StocksBean stocksBean = new StocksBean();
                            stocksBean.setSize(getmSize());
                            stocksBean.setNum(getmNums());
                            stocksBeenList.add(stocksBean);
                            selectSizeNumsAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showError(mContext,"添加失败，改尺码已存在");
                        }
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(true)
                .create()
                .show();
    }

    public boolean isSizeExist(){
        for(int i=0;i<stocksBeenList.size();i++){
            if(stocksBeenList.get(i).getSize().equals(getmSize()) ){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRemoveSize(int position) {
        stocksBeenList.remove(position);
        selectSizeNumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNumber(int position, int currNums) {
        stocksBeenList.get(position).setNum((currNums + 1));
        selectSizeNumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusNumbers(int position, int currNums) {
        if (currNums == 0) {
            stocksBeenList.remove(position);
        } else {
            stocksBeenList.get(position).setNum((currNums - 1));
        }
        selectSizeNumsAdapter.notifyDataSetChanged();
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(AddRepertoryActivity.this,"更新中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public int getmNums() {
        return mNums;
    }

    public void setmNums(int mNums) {
        this.mNums = mNums;
    }

    @Override
    public void modSelfGoodsView(ModifySelfResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,/*"库存修改成功"*/msg);
            listBean.setStocks(stocksBeenList);
            new MyHandler(this).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ClickItem",clickItem);
                    bundle.putSerializable("GoodsInfos",listBean);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                    AppManager.getAppManager().finishActivity(AddRepertoryActivity.this);
                }
            },2000);
        }
    }

    private static class MyHandler extends Handler{
        WeakReference<AddRepertoryActivity> mActivity;

        MyHandler(AddRepertoryActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddRepertoryActivity theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    theActivity.isReady = true;
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }

}
