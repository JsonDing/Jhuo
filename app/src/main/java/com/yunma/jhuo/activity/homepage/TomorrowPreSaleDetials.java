package com.yunma.jhuo.activity.homepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.yunma.R;
import com.yunma.adapter.GoodsSizeAdapter;
import com.yunma.adapter.GoodsTagAdapter;
import com.yunma.bean.LocalImagePathBean;
import com.yunma.bean.PathBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.bean.TomorrowUpDataResultBean;
import com.yunma.jhuo.activity.PicViewerActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.AddTomorrowUpDataRemindPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.FlowTagLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.library.MDDialog;

import static com.yunma.R.id.tag_layout;

public class TomorrowPreSaleDetials extends MyCompatActivity implements
        GoodsSizeAdapter.OnAddBasketCarts, SelfGoodsInterFace.AddSelfGoodsRemindView {

    @BindView(R.id.xbanner) XBanner xbanner;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.imgsFavorable) ImageView imgsFavorable;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsOriPrice) TextView tvGoodsOriPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.tvFavorable) TextView tvFavorable;
    @BindView(tag_layout) FlowTagLayout tagLayout;
    @BindView(R.id.size_layout) FlowTagLayout sizeLayout;
    @BindView(R.id.tvExpressInfo) TextView tvExpressInfo;
    @BindView(R.id.scroll) NestedScrollView scroll;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.btnRemindMe) Button btnRemindMe;
    private AddTomorrowUpDataRemindPre addTomorrowUpDataRemindPre =null;
    private TomorrowUpDataResultBean.SuccessBean.YunmaBean detialsBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow_pre_sale_detials);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        getIntentDatas();
    }

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(TomorrowPreSaleDetials.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        tvTittle.setText("详情");
    }

    private void getIntentDatas() {
        detialsBean = (TomorrowUpDataResultBean.SuccessBean.YunmaBean)
                this.getIntent().getSerializableExtra("TomorrowPreSaleDetials");
        assert detialsBean != null;
        if(detialsBean.getLabel().contains("支持去鞋盒服务")){
            imgsFavorable.setVisibility(View.VISIBLE);
            tvFavorable.setVisibility(View.VISIBLE);
        }
        if(detialsBean.getPic()!=null){
            List<String> imgUrls = Arrays.asList(detialsBean.getPic().split(","));
            setDatas(imgUrls);
        }
        tvGoodsName.setText(detialsBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(detialsBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.b4,s, Typeface.NORMAL,
                17,1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsOriPrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(detialsBean.getSaleprice())));
        int remainNums = 0;
        ArrayList<String> sizeList = new ArrayList<>();
        for(int i=0;i<detialsBean.getStocks().size();i++){
            if(detialsBean.getStocks().get(i).getNum()!=0){
                sizeList.add(detialsBean.getStocks().get(i).getSize());
                remainNums = remainNums + detialsBean.getStocks().get(i).getNum();
            }else{
                detialsBean.getStocks().remove(i);
            }
        }
        tvGoodsRemain.setText(String.valueOf("仅剩" + remainNums + "件"));
        // 设置中划线并加清晰;
        tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvGoodsOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        GoodsSizeAdapter mAdapter = new GoodsSizeAdapter(
                TomorrowPreSaleDetials.this,TomorrowPreSaleDetials.this);
        sizeLayout.setAdapter(mAdapter);
        mAdapter.onlyAddAll(sizeList);
        GoodsTagAdapter tagAdapter = new GoodsTagAdapter(TomorrowPreSaleDetials.this);
        tagLayout.setAdapter(tagAdapter);
        String [] str = detialsBean.getLabel().split(",");
        List<String> tagsList = new ArrayList<>();
        for (String aStr : str) {
            if (!aStr.contains("服务")) {
                tagsList.add(aStr);
            }
        }
        if(detialsBean.getNewRemindId()!=null){
            btnRemindMe.setEnabled(false);
            btnRemindMe.setText("已设置上架提醒");
        }else{
            btnRemindMe.setEnabled(true);
            btnRemindMe.setText("上架提醒我");
        }
        tagAdapter.onlyAddAll(tagsList);
        tvExpressInfo.setText(Html.fromHtml("· 邮费信息：详细见各地区" +
                "<font color = '#1E88E5'>" + "运费模板" + "</font>"));
    }

    private void setDatas(final List<String> imgUrls) {
        assert imgUrls!= null;
        final String url;
        if(detialsBean.getRepoid()==1){
            url = ConUtils.SElF_GOODS_IMAGE_URL;
        }else{
            url = ConUtils.GOODS_IMAGE_URL;
        }
        xbanner.setData(imgUrls,null);
        xbanner.setmAdapter(new XBanner.XBannerAdapter() {
            public void loadBanner(XBanner banner, View view, int pos) {
                GlideUtils.glidBanner(TomorrowPreSaleDetials.this,
                        (ImageView) view, url + imgUrls.get(pos));
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
                    pathBean.setImgsPath(url + imgUrls.get(i));
                    pathBeenList.add(pathBean);
                }
                preBean.setPathBeen(pathBeenList);
                Intent intent = new Intent(TomorrowPreSaleDetials.this,PicViewerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("path",preBean);
                bundle.putInt("pos", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.layoutBack, R.id.btnRemindMe,R.id.tvExpressInfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnRemindMe:
                addTomorrowUpDataRemindPre = new AddTomorrowUpDataRemindPre(TomorrowPreSaleDetials.this);
                addTomorrowUpDataRemindPre.addGoodsRemind(this,detialsBean.getNumber());
                break;
            case R.id.tvExpressInfo:
                showExpressDialog();
                break;
        }
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

    @Override
    public void onAddBasketCarts(String size) {

    }

    @Override
    public void showSelfGoodsRemindInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
        }    }
}
