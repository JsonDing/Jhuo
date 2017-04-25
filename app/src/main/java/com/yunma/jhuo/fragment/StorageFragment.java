package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.*;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.emchat.ui.EMMainActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.SearchGoodsByCode;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.m.GetGoodsInterface.GetGoodsRecommendView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.NestListView;

import butterknife.*;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class StorageFragment extends Fragment implements GetGoodsRecommendView, GetGoodsInterface.GetGoodsHotView, GetGoodsInterface.GetGoodsNewView {
    @BindView(R.id.lvEveryBoutique) NestListView lvEveryBoutique;
    @BindView(R.id.lvNewProduct) NestListView lvNewProduct;
    @BindView(R.id.lvHotGoods) NestListView lvHotGoods;
    @BindView(R.id.layoutEveryDayBoutique) LinearLayout layoutEveryDayBoutique;
    @BindView(R.id.layoutNewProduct) LinearLayout layoutNewProduct;
    @BindView(R.id.layoutHotGoods) LinearLayout layoutHotGoods;
    @BindView(R.id.layoutSearch) LinearLayout layoutSearch;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutNoBoutique) View layoutNoBoutique;
    @BindView(R.id.layoutNoNew) View layoutNoNew;
    @BindView(R.id.layoutNoHot) View layoutNoHot;
    private EverydayBoutiqueAdapter recommendAdapter;
    private NewProductAdapter newAdapter;
    private HotGoodsAdapter hotAdapter;
    private GetGoodsRecommendPre recommendPre = null;
    private GetGoodsHotPre hotPre = null;
    private GetGoodsNewPre newPre = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment, container, false);
        ButterKnife.bind(this, view);
        int statusHeight = ScreenUtils.getStatusHeight(getActivity());
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        recommendPre = new GetGoodsRecommendPre(StorageFragment.this);
        hotPre = new  GetGoodsHotPre(StorageFragment.this);
        newPre = new GetGoodsNewPre(StorageFragment.this);
        getDatas();
        return view;
    }

    private void getDatas() {
        recommendPre.getRecommendGoods("6",1);
        hotPre.getHotGoods("6",1);
        newPre.getNewGoods("6",1);
        recommendAdapter = new EverydayBoutiqueAdapter(getActivity());
        lvEveryBoutique.setAdapter(recommendAdapter);
        newAdapter = new NewProductAdapter(getActivity());
        lvNewProduct.setAdapter(newAdapter);
        hotAdapter = new HotGoodsAdapter(getActivity());
        lvHotGoods.setAdapter(hotAdapter);
    }

    @OnClick({R.id.layoutSearch, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutSearch:
                Intent intent = new Intent(getActivity(), SearchGoodsByCode.class);
                startActivity(intent);
                break;
            case R.id.layoutNews:
                if(MainActivity.mainContext.isLogin()){
                    Intent intent1 = new Intent(MainActivity.mainContext, EMMainActivity.class);
                    MainActivity.mainContext.startActivity(intent1);
                }else{
                    QQAuth mqqAuth = QQAuth.createInstance("1106058796",MainActivity.mainContext);
                    WPA mWPA = new WPA(MainActivity.mainContext, mqqAuth.getQQToken());
                    String ESQ = "2252162352";  //客服QQ号
                    int ret = mWPA.startWPAConversation(getActivity(),ESQ,null);
                    if (ret != 0) {
                        Toast.makeText(MainActivity.mainContext,
                                "抱歉，联系客服出现了错误~. error:" + ret,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return super.getActivity();
    }

    @Override
    public void showNewGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getActivity(),msg);
            layoutNoNew.setVisibility(View.VISIBLE);
            lvNewProduct.setVisibility(View.GONE);
        }else {
            if(resultBean.getSuccess().getList()!=null&&
                    resultBean.getSuccess().getList().size()!=0){
                layoutNoNew.setVisibility(View.GONE);
                lvNewProduct.setVisibility(View.VISIBLE);
                newAdapter.setListBean(resultBean.getSuccess().getList());
            }else{
                layoutNoNew.setVisibility(View.VISIBLE);
                lvNewProduct.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showHotGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getActivity(),msg);
            layoutNoHot.setVisibility(View.VISIBLE);
            lvHotGoods.setVisibility(View.GONE);
        }else {
            if(resultBean.getSuccess().getList()!=null&&
                    resultBean.getSuccess().getList().size()!=0){
                layoutNoHot.setVisibility(View.GONE);
                lvHotGoods.setVisibility(View.VISIBLE);
                hotAdapter.setListBean(resultBean.getSuccess().getList());
            }else{
                layoutNoHot.setVisibility(View.VISIBLE);
                lvHotGoods.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showRecommendGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getActivity(),msg);
            layoutNoBoutique.setVisibility(View.VISIBLE);
            lvEveryBoutique.setVisibility(View.GONE);
        }else {
            if(resultBean.getSuccess().getList()!=null&&
                    resultBean.getSuccess().getList().size()!=0){
                layoutNoBoutique.setVisibility(View.GONE);
                lvEveryBoutique.setVisibility(View.VISIBLE);
                recommendAdapter.setList(resultBean.getSuccess().getList());
            }else{
                layoutNoBoutique.setVisibility(View.VISIBLE);
                lvEveryBoutique.setVisibility(View.GONE);
            }
        }
    }

    public void refresh() {
        imgRemind.setVisibility(View.VISIBLE);
    }
}
