package com.yunma.jhuo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SelfGoodsDetials;
import com.yunma.adapter.*;
import com.yunma.adapter.SearchGoodsByCodeAdapter.OnClickListener;
import com.yunma.bean.*;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.HistroySearch;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.greendao.HistroySearchDao;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchSelfGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.SearchSelfGoodsByCodePre;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.*;
import com.yunma.widget.*;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class SearchGoodsByCode extends MyCompatActivity implements SearchSelfGoodsView,
        OnClickListener, HistroySearchAdapter.OnClickListener, SelfGoodsInterFace.GetSelfGoodsView {
    @BindView(R.id.layoutTittleBar) View layoutTittleBar;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.layoutCancel) LinearLayout layoutCancel;
    @BindView(R.id.layoutSearchGoods) LinearLayout layoutSearchGoods;
    @BindView(R.id.layoutClear) View layoutClear;
    @BindView(R.id.layoutDetele) View layoutDetele;
    @BindView(R.id.layoutHistorySearch) View layoutHistorySearch;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.flowTagLayout) FlowTagLayout flowTagLayout;
    @BindView(R.id.layout1) LinearLayout layout1;
    @BindView(R.id.tvRemain) TextView tvRemain;
    @BindView(R.id.gridViewIntro) MyGirdView gridViewIntro;
    @BindView(R.id.layout2) ScrollView layout2;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsBean;
    private SearchGoodsByCodeAdapter mAdapter = null;
    private SearchNullRecommendAdapter recommendAdapter = null;
    private HistroySearchAdapter searchAdapter = null;
    private SearchSelfGoodsByCodePre searchByCode = null;
    private CharSequence tempNums = null;
    private int numsStart;
    private int numsEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods_by_code);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        int statusHeight = ScreenUtils.getStatusHeight(SearchGoodsByCode.this);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layoutTittleBar.getLayoutParams();
        lp.width = ActionBar.LayoutParams.MATCH_PARENT;
        lp.height = statusHeight;
        layoutTittleBar.setLayoutParams(lp);
        layoutTittleBar.setLayoutParams(lp);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new ScaleInAnimator());
        recyclerview.getItemAnimator().setAddDuration(600);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchGoodsByCodeAdapter(SearchGoodsByCode.this, SearchGoodsByCode.this);
        recyclerview.setAdapter(mAdapter);
        searchAdapter = new HistroySearchAdapter(SearchGoodsByCode.this, SearchGoodsByCode.this);
        flowTagLayout.setAdapter(searchAdapter);
        searchByCode = new SearchSelfGoodsByCodePre(SearchGoodsByCode.this);
        getHistorySearch();
        etSearch.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (etSearch.getText().toString().trim().isEmpty()) {
                        ToastUtils.showWarning(getApplicationContext(), "请输入商品货号");
                        return true;
                    }
                    mAdapter.clear();
                    searchByCode.searchSelfGoodsByCode(
                            SearchGoodsByCode.this, etSearch.getText().toString().trim(), "18");
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempNums = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                numsStart = etSearch.getSelectionStart();
                numsEnd = etSearch.getSelectionEnd();
                if (tempNums.length() > 30) {
                    ToastUtils.showError(getApplicationContext(), "你输入的字数已经超过了限制");
                    s.delete(numsStart - 1, numsEnd);
                    int tempSelection = numsStart;
                    etSearch.setText(s);
                    etSearch.setSelection(tempSelection);
                }
                if (tempNums.length() != 0) {
                    layoutClear.setVisibility(View.VISIBLE);
                } else {
                    layoutClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void getHistorySearch() {
        Query<HistroySearch> nQuery = getSearchDao().queryBuilder()
                .limit(9)
                .orderDesc(HistroySearchDao.Properties.SearchTimes)
                .build();
        List<HistroySearch> list = nQuery.list();
        if (list != null && list.size() != 0) {
            layoutHistorySearch.setVisibility(View.VISIBLE);
            searchAdapter.setSearchHistory(list);
        } else {
            layoutHistorySearch.setVisibility(View.GONE);
        }
    }

    private void saveToDataBase(String number) {
        Query<HistroySearch> nQuery = getSearchDao().queryBuilder()
                .where(HistroySearchDao.Properties.SearchNumber.eq(number))
                .build();
        List<HistroySearch> list = nQuery.list();
        if (list.size() != 0) { // 查询过
            HistroySearch search = new HistroySearch(list.get(0).getId(), number, list.get(0).getSearchTime(),
                    System.currentTimeMillis(), (list.get(0).getSearchTimes() + 1));
            getSearchDao().update(search);
        } else {
            HistroySearch search = new HistroySearch(null, number, System.currentTimeMillis(),
                    System.currentTimeMillis(), 1);
            getSearchDao().save(search);
        }
    }

    @OnClick({R.id.layoutCancel, R.id.layoutClear, R.id.layoutDetele})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutCancel:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.layoutClear:
                etSearch.setText("");
                etSearch.setHint("请输入货号查询");
                mAdapter.clear();
                layoutClear.setVisibility(View.INVISIBLE);
                getHistorySearch();
                break;
            case R.id.layoutDetele:
                getSearchDao().deleteAll();
                getHistorySearch();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(this);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        return false;
    }

    @Override
    public void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layoutHistorySearch.setVisibility(View.GONE);
                hideSoftInput();
                goodsBean = resultBean.getSuccess().getList();
                mAdapter.setNums(etSearch.getText().toString().trim());
                mAdapter.setGoodsBean(goodsBean);
            }else {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                tvRemain.setText("抱歉，没有找到“" + etSearch.getText().toString().trim()+"”相关商品");
                getGoodsRecommend();
            }
        }
    }

    private void getGoodsRecommend() {
        recommendAdapter = new SearchNullRecommendAdapter(SearchGoodsByCode.this);
        gridViewIntro.setAdapter(recommendAdapter);
        SelfGoodsPre mPresenter = new SelfGoodsPre(SearchGoodsByCode.this);
        mPresenter.getSpecialOfferGoods(getApplicationContext(),"recommend","4",4);
    }

    private HistroySearchDao getSearchDao() {
        return GreenDaoManager.getInstance().getSession().getHistroySearchDao();
    }

    @Override
    public void onSearchClick(GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        saveToDataBase(listBean.getNumber());
        Intent intent = new Intent(this, SelfGoodsDetials.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd", "no");
        bundle.putString("goodId", String.valueOf(listBean.getId()));
        bundle.putSerializable("goodsDetials", listBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSearchClick(String searchNumber) {
        saveToDataBase(searchNumber);
        mAdapter.clear();
        searchByCode.searchSelfGoodsByCode(
                SearchGoodsByCode.this, searchNumber, "18");
        etSearch.setText(searchNumber);
    }

    @Override
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            if(resultBean.getSuccess().getList().size()!=0){
                recommendAdapter.setGoodsListBeen(resultBean.getSuccess().getList());
            }else{
                recommendAdapter.notifyDataSetChanged();
            }
        }
    }

}
