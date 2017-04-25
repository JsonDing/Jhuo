package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.BookmarksAdapter;
import com.yunma.bean.GetCollectResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GoodsCollectInterFace;
import com.yunma.jhuo.p.DelGoodsCollectPre;
import com.yunma.jhuo.p.GetGoodsCollectPre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class Bookmarks extends MyCompatActivity implements GoodsCollectInterFace.GetGoodsCollectView, BookmarksAdapter.OnItemClick, GoodsCollectInterFace.DelGoodsCollectView {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvBookmarks) RecyclerView lvBookmarks;
    private Context mContext;
    private DelGoodsCollectPre delGoodsCollectPre = null;
    private BookmarksAdapter mAdapter;
    private String goodIds;
    private int goodsPos;
    private List<GetCollectResultBean.SuccessBean> listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }


    private void getDatas() {
        GetGoodsCollectPre getGoodsCollectPre = new GetGoodsCollectPre(this);
        delGoodsCollectPre = new DelGoodsCollectPre(this);
        getGoodsCollectPre.getGoodsCollect(mContext);
    }
    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(Bookmarks.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        lvBookmarks.setHasFixedSize(true);
        lvBookmarks.setItemAnimator(new FadeInLeftAnimator());
        lvBookmarks.getItemAnimator().setAddDuration(700);
        lvBookmarks.getItemAnimator().setMoveDuration(500);
        lvBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BookmarksAdapter(this,Bookmarks.this);
        lvBookmarks.setAdapter(mAdapter);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(Bookmarks.this,ESQ,null);
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String getGoodId() {
        return getGoodIds();
    }

    @Override
    public void onDelCollectShow(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            mAdapter.remove(getGoodsPos());
            ToastUtils.showSuccess(mContext,"删除成功");
        }
    }

    @Override
    public void onGetCollectShow(GetCollectResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            if(resultBean.getSuccess()!=null&&
                    resultBean.getSuccess().size()!=0){
                listBean = resultBean.getSuccess();
                mAdapter.AddListBean(listBean);
            }else {
                ToastUtils.showInfo(mContext,"暂未收藏商品");
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, GetCollectResultBean.SuccessBean successBean) {
        Intent intent = new Intent(mContext, QuickReplenishDetial.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type",successBean.getRepoid());
        bundle.putString("goodsNumber",successBean.getNumber());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDelClick(int position, String goodId) {
        setGoodIds(goodId);
        setGoodsPos(position);
        delGoodsCollectPre.onDelCollect();
    }

    public String getGoodIds() {
        return goodIds;
    }

    public void setGoodIds(String goodIds) {
        this.goodIds = goodIds;
    }

    public int getGoodsPos() {
        return goodsPos;
    }

    public void setGoodsPos(int goodsPos) {
        this.goodsPos = goodsPos;
    }

}
