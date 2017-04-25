package com.yunma.publish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.*;
import android.view.animation.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.RepertoryManageAdapter;
import com.yunma.adapter.RepertoryManageAdapter.OnItemClick;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchSelfGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSelfGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.ShelveSelfGoodsView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class RepertoryManageActivity extends MyCompatActivity implements
        GetSelfGoodsView, SearchSelfGoodsView, ShelveSelfGoodsView, OnItemClick {
    private static final int  REQUEST_REPERTORY_CODE = 1;
    private static final int  REQUEST_IMAGE_CODE = 2;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvShelve) TextView tvShelve;
    @BindView(R.id.layoutShelve) LinearLayout layoutShelve;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutSearch) LinearLayout layoutSearch;
    @BindView(R.id.etHint) LinearLayout etHint;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.tvMaind) TextView tvMaind;
    @BindView(R.id.rcPublishManage) RecyclerView rcPublishManage;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen = null;
    private RepertoryManageAdapter mAdapter = null;
    private SelfGoodsPre mPresenter = null;
    private SearchSelfGoodsByCodePre searchPre = null;
    private ShelveSelfGoodsPre shelveGoodsPre = null;
    private int nextPage = 2;
    private int totalGoodsNums = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertory_manage);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        initView();
        initSwipeRefreshLayout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvMaind.setVisibility(View.GONE);
            }
        },4000);
    }

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(RepertoryManageActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        rcPublishManage.setHasFixedSize(true);
        rcPublishManage.setItemAnimator(new FadeInLeftAnimator());
        rcPublishManage.getItemAnimator().setAddDuration(700);
        rcPublishManage.getItemAnimator().setMoveDuration(500);
        rcPublishManage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new RepertoryManageAdapter(RepertoryManageActivity.this, RepertoryManageActivity.this,rcPublishManage);
        rcPublishManage.setAdapter(mAdapter);
    }

    private void initView() {
        mPresenter = new SelfGoodsPre(RepertoryManageActivity.this);
        searchPre = new SearchSelfGoodsByCodePre(RepertoryManageActivity.this);
        shelveGoodsPre = new ShelveSelfGoodsPre(RepertoryManageActivity.this);
        mPresenter.getSpecialOfferGoods(getApplicationContext(),"sale","12", 1);
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // hasFocus 为false时表示点击了别的控件，离开当前editText控件
                if (!hasFocus) {
                    if ("".equals(etSearch.getText().toString())) {
                        etHint.setVisibility(View.VISIBLE);
                        layoutSearch.setVisibility(View.GONE);
                    }
                }
                else {
                    etHint.setVisibility(View.GONE);
                    layoutSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if(etSearch.getText().toString().trim().isEmpty()){
                        ToastUtils.showWarning(getApplicationContext(),"请输入商品货号");
                        return true;
                    }
                    searchPre.searchSelfGoodsByCode(getApplicationContext(),
                            etSearch.getText().toString().trim(),"12");

                    return true;
                }
                return false;
            }
        });

    }

    @OnClick({R.id.layoutBack, R.id.layoutShelve,R.id.layoutSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutShelve:
                if(EmptyUtil.isNotEmpty(mAdapter.getShelveIds())){
                    IssueBean issueBean = new IssueBean();
                    issueBean.setToken(SPUtils.getToken(this));
                    issueBean.setIssue("0");
                    issueBean.setId(mAdapter.getShelveIds());
                    LogUtils.log("---ShelveID: " + mAdapter.getShelveIds());
                    showWarning(issueBean);
                }else{
                    ToastUtils.showWarning(getApplicationContext(),"请选择下架商品");
                }
                break;
            case R.id.layoutSearch:
                if(etSearch.getText().toString().trim().isEmpty()){
                    ToastUtils.showWarning(getApplicationContext(),"请输入商品货号");
                }else{
                    searchPre.searchSelfGoodsByCode(getApplicationContext(),
                            etSearch.getText().toString().trim(),"12");
                }
                break;
        }
    }

    private void showWarning(final IssueBean issueBean) {
        int dialogWith = ScreenUtils.getScreenWidth(this) -
                DensityUtils.dp2px(this, 42);
        new MDDialog.Builder(RepertoryManageActivity.this)
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("商品下架后，可在已下架商品栏目查看，并且可以重新发布上架");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shelveGoodsPre.shelveGoods(getApplicationContext(),issueBean);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(getApplicationContext(), dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    @Override
    public void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        if(listBean!=null){
            Intent intent = new Intent(this,AddRepertoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ClickItem",position);
            bundle.putSerializable("SelfGoodsDetials",listBean);
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_REPERTORY_CODE);
            overridePendingTransition(0,R.anim.bottom_out);
        }else{
            if(EmptyUtil.isNotEmpty(mAdapter.getShelveIds())) {
                totalGoodsNums = totalGoodsNums - mAdapter.getShelveIds().split(",").length;
            }
            tvShelve.setText(Html.fromHtml("在售" + "<font color = '#f44141'>" +
                    totalGoodsNums + "</font>" + "款"));
        }

    }

    @Override
    public void OnReplaceImage(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(this,ReplaceImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ClickItem",position);
        bundle.putSerializable("SelfGoodsDetials",listBean);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_IMAGE_CODE);
        overridePendingTransition(0,R.anim.bottom_out);
    }

    @Override
    public void onItemLongClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean) {
        MobileUtils.Vibrate(RepertoryManageActivity.this,70);
        mAdapter.isShowBox(true);
        tvShelve.setText(Html.fromHtml("<font color = '#f4bd39'>下架</font>"));
        layoutShelve.setClickable(true);
        layoutShelve.setEnabled(true);
    }

    @Override
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
            if("服务器未响应".equals(msg)||"网络出错".equals(msg)){
                swipeRefreshLayout.setLoadmoreEnable(true);
            }
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                nextPage = resultBean.getSuccess().getNextPage();
                if (resultBean.getSuccess().isHasNextPage()) {
                    swipeRefreshLayout.setLoadmoreEnable(true);
                } else {
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                totalGoodsNums = resultBean.getSuccess().getTotal();
                // Html.fromHtml("· 邮费信息：" +"<font color = '#1E88E5'>" + "参考运费模版" + "</font>")
                tvShelve.setText(Html.fromHtml("在售" + "<font color = '#f44141'>"
                        + totalGoodsNums + "</font>" + "件",null,null));
                if (resultBean.getSuccess().getPageNum() == 1) {
                    if (goodsListBeen != null && goodsListBeen.size() != 0) {
                        goodsListBeen.clear();
                    }
                    goodsListBeen = resultBean.getSuccess().getList();
                    mAdapter.setGoodsListBeen(goodsListBeen);
                } else if (resultBean.getSuccess().getPageNum() <= resultBean.getSuccess().getPages()) {
                    goodsListBeen.addAll(resultBean.getSuccess().getList());
                    swipeRefreshLayout.finishLoadmore();
                    mAdapter.setGoodsListBeen(goodsListBeen);
                }
            }
        }
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Animation operatingAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = (ImageView)view.findViewById(R.id.imgSun);
        final TextView textView2 = (TextView) view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(true);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSpecialOfferGoods(getApplicationContext(),"sale","12",1);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        imgSun.clearAnimation();
                    }
                }, 1500);
            }

            @Override
            public void onLoading() {
                mPresenter.getSpecialOfferGoods(getApplicationContext(),"sale","12",nextPage);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                    }
                }, 1600);
            }

            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        imgSun.startAnimation(operatingAnim);
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
                        textView2.setText("正在加载...");
                        JumpingBeans.with(textView2)
                                .appendJumpingDots()
                                .build();
                        break;
                }
            }
        });
    }

    @Override
    public void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            goodsListBeen = resultBean.getSuccess().getList();
            mAdapter.setGoodsListBeen(goodsListBeen);
        }
    }

    @Override
    public void shelveSelfGoods(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            totalGoodsNums = totalGoodsNums -1;
            tvShelve.setText(Html.fromHtml("在售" + "<font color = '#f44141'>" +
                    totalGoodsNums + "</font>" + "款"));
            mAdapter.remove();
            etSearch.clearFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_REPERTORY_CODE:
                if(resultCode==RESULT_OK){
                    GetSelfGoodsResultBean.SuccessBean.ListBean goodsBeen =
                            (GetSelfGoodsResultBean.SuccessBean.ListBean)
                                    data.getSerializableExtra("GoodsInfos"); //data为B中回传的Intent
                    int clickItem = data.getIntExtra("ClickItem",-1);
                    if(clickItem!=-1){
                        goodsListBeen.set(clickItem,goodsBeen);
                    }
                    mAdapter.notifyItemChanged(clickItem);
                }
                break;
            case REQUEST_IMAGE_CODE:
                if(resultCode==RESULT_OK){
                    mPresenter.getSpecialOfferGoods(getApplicationContext(),"sale","10", 1);
                }
                break;
        }
    }
}
