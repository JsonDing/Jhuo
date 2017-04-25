package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.SaleRemindAdapter;
import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.bean.GoodsRemindResultBean.SuccessBean.NewRemindsBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.m.SelfGoodsInterFace.GoodsRemindView;
import com.yunma.jhuo.p.DelGoodsRemindPre;
import com.yunma.jhuo.p.GoodsRemindPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.Typefaces;
import com.yunma.widget.MySpacesItemDecoration;
import com.yunma.widget.Titanic;
import com.yunma.widget.TitanicTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static org.xutils.common.util.DensityUtil.dip2px;

public class SaleRemindActivity extends MyCompatActivity implements GoodsRemindView,
        SelfGoodsInterFace.DelGoodsRemindView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SHSwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;
    private GoodsRemindPre goodsRemindPre;
    private DelGoodsRemindPre delGoodsRemindPre = null;
    private SaleRemindAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_remind);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        initRecyclerCiew();
        initSwipeRefreshLayout();
    }

    private void initRecyclerCiew() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        recyclerview.setItemAnimator(new SlideInLeftAnimator());
        mAdapter =  new SaleRemindAdapter(mContext,SaleRemindActivity.this);
        recyclerview.setAdapter(mAdapter);
        goodsRemindPre = new GoodsRemindPre(SaleRemindActivity.this);
        goodsRemindPre.getRemindGoods(mContext);
    }

    private void initStatusBar() {
        mContext = this;
        tvTittle.setText("上新提醒");
        imgsRight.setVisibility(View.GONE);
        layoutRight.setClickable(false);
        GlideUtils.glidLocalDrawable(mContext,imgsRight,R.drawable.re_delete);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);

    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }

    @OnClick({R.id.layoutBack, R.id.layoutRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutRight:
                LogUtils.log("list: " + mAdapter.getList().toString());
                if(delGoodsRemindPre == null){
                    delGoodsRemindPre = new DelGoodsRemindPre(this);
                }
                delGoodsRemindPre.addGoodsRemind(mContext,mAdapter.getList());
                break;
        }
    }

    @Override
    public void showGoodsRemind(GoodsRemindResultBean resultBean,
                                List<NewRemindsBean> newRemindsBeanList, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            if(newRemindsBeanList.size()!=0){
                mAdapter.setNewRemindsBeanList(newRemindsBeanList);
            }else{
                ToastUtils.showInfo(mContext,"您尚未添加上新提醒");
            }
        }
    }

    public void test(){
       if(mAdapter.getList().size()!=0){
           imgsRight.setVisibility(View.VISIBLE);
           layoutRight.setClickable(true);
       } else{
           layoutRight.setClickable(false);
           imgsRight.setVisibility(View.GONE);
       }
    }

    @Override
    public void showDelGoodsRemindInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
            goodsRemindPre.getRemindGoods(mContext);
        }
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.head_view, null);
        final TitanicTextView tv = (TitanicTextView) view.findViewById(R.id.textview);
        tv.setTypeface(Typefaces.get(this, "Delicious.ttf"));
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(true);
        final Titanic titanic = new Titanic();
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                goodsRemindPre.getRemindGoods(mContext);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        titanic.cancel();
                        swipeRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoading() {
                // TODO: 2017-04-21
            }

            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        titanic.start(tv);
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        break;
                }
            }
        });
    }
}
