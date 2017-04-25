package com.yunma.publish;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yunma.R;
import com.yunma.adapter.*;
import com.yunma.adapter.GoodsTagsAdapter.TagsOnClick;
import com.yunma.adapter.PublishGoodsDetialsAdapter.OnAddPicClick;
import com.yunma.adapter.SelectSizeNumsAdapter.OnClickListener;
import com.yunma.bean.*;
import com.yunma.domain.GoodsInfos;
import com.yunma.jhuo.general.CheckPermissionsActivity;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.OnTagSelectListener;
import com.yunma.jhuo.m.SelfGoodsInterFace.ModSelfGoodsByIdView;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.jhuo.p.ModSelfGoodsPre;
import com.yunma.utils.*;
import com.yunma.widget.*;
import com.yunma.publish.MyItemTouchCallback.OnDragListener;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class PublishGoodsDetials extends CheckPermissionsActivity implements
        OnAddPicClick, OnClickListener, GetQiniuTokenView,ModSelfGoodsByIdView,
        TagsOnClick, OnDragListener, PublishSelectSizeNumsAdapter.OnClickListener {
    // type 鞋
    @BindView(R.id.tvShoes) TextView tvShoes;
    @BindView(R.id.lineShoes) View lineShoes;
    @BindView(R.id.layoutShoes) LinearLayout layoutShoes;
    // type 衣服
    @BindView(R.id.tvClothes) TextView tvClothes;
    @BindView(R.id.lineClothes) View lineClothes;
    @BindView(R.id.layoutCloches) LinearLayout layoutCloches;
    // type 裤子
    @BindView(R.id.tvPants) TextView tvPants;
    @BindView(R.id.linePants) View linePants;
    @BindView(R.id.layoutPants) LinearLayout layoutPants;
    // type 男童
    @BindView(R.id.tvChildBoy) TextView tvChildBoy;
    @BindView(R.id.lineChildBoy) View lineChildBoy;
    @BindView(R.id.layoutChildBoy) LinearLayout layoutChildBoy;
    // type 女童
    @BindView(R.id.tvChildGirl) TextView tvChildGirl;
    @BindView(R.id.lineChildGirl) View lineChildGirl;
    @BindView(R.id.layoutChildGirl) LinearLayout layoutChildGirl;
    // type 配件
    @BindView(R.id.tvOther) TextView tvOther;
    @BindView(R.id.lineOther) View lineOther;
    @BindView(R.id.layoutOther) LinearLayout layoutOther;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvGenderMan) TextView tvGenderMan;
    @BindView(R.id.tvGenderWoman) TextView tvGenderWoman;
    @BindView(R.id.rvSelectSizeNums) RecyclerView rvSelectSizeNums;
    @BindView(R.id.etGoodsNumber) EditText etGoodsNumber;
    @BindView(R.id.etSeries) EditText etSeries;
    @BindView(R.id.etGoodsDescribe) EditText etGoodsDescribe;
    @BindView(R.id.etDiscountPrice) EditText etDiscountPrice;
    @BindView(R.id.etSalePrice) EditText etSalePrice;
    @BindView(R.id.tvOutDiscount) TextView tvOutDiscount;
    @BindView(R.id.etInDiscount) EditText etInDiscount;
    private String TAG = "---Jhuo---";
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tag_layout) FlowTagLayout tag_layout;
    @BindView(R.id.brands_layout) FlowTagLayout brands_layout;
    @BindView(R.id.etGoodsRemark) EditText etGoodsRemark;
    private PublishGoodsDetialsAdapter mAdapter = null;
    private PublishSelectSizeNumsAdapter selectSizeNumsAdapter = null;
    private ModifyYunmasBean modifyBean = null;
    private List<ModStocksBean> stocksBeenList = null;
    private GalleryConfig galleryConfig = null;
    private List<String> oldImgs = new ArrayList<>();
    private int selectedPosition = -1;
    private List<String> selectedTags;
    private ItemTouchHelper itemTouchHelper;
    private GoodsTagsAdapter tagAdapter;
    private GetSelfGoodsResultBean.SuccessBean.ListBean successBean;
    private CustomProgressDialog dialog = null;
    private Context mContext;
    private String goodsPic;
    private String goodsBrands;
    private String goodsTags;
    private String goodsType;//类型(1-服装,2-鞋子,3-配件,4-童男，5-童女)
    private String fitPerson;
    private String goodsColor;
    private String mSize = null;
    private int mNums ;
    private String qiNiuToken;
    private MyHandler mHandler = new MyHandler(this);

    // TODO: 2017-04-07
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_goods_detials);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        initRecyclerview();
        initGallery();
        initView();

    }

    private void initRecyclerview() {
        mContext = this;
        stocksBeenList = new ArrayList<>();
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        recyclerview.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new PublishGoodsDetialsAdapter(this, PublishGoodsDetials.this);
        recyclerview.setAdapter(mAdapter);
        itemTouchHelper = new ItemTouchHelper(
                new MyItemTouchCallback(mAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerview);
        recyclerview.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerview) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition()!=oldImgs.size()) {
                    itemTouchHelper.startDrag(vh);
                    MobileUtils.Vibrate(PublishGoodsDetials.this, 70);   //震动70ms
                }
            }
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }
        });

        rvSelectSizeNums.setHasFixedSize(true);
        rvSelectSizeNums.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        etDiscountPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSalePrice.getText().toString().trim().length() > 0 &&
                        EmptyUtil.isNotEmpty(etSalePrice.getText().toString().trim())&&
                        EmptyUtil.isNotEmpty(etDiscountPrice.getText().toString().trim())) {
                    if(Double.valueOf(etSalePrice.getText().toString().trim())!= 0){
                        tvOutDiscount.setText(ValueUtils.toTwoDecimal((Double.valueOf(etDiscountPrice.getText().toString().trim())
                                /Double.valueOf(etSalePrice.getText().toString().trim()))*100) + "%");
                    }
                }else if(EmptyUtil.isEmpty(etDiscountPrice.getText().toString())){
                    tvOutDiscount.setText("");
                }
            }
        });
        etSalePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (EmptyUtil.isNotEmpty(etDiscountPrice.getText().toString().trim())&&
                        EmptyUtil.isNotEmpty(etSalePrice.getText().toString().trim())) {
                    if(Double.valueOf(etSalePrice.getText().toString().trim())!= 0){
                        tvOutDiscount.setText(ValueUtils.toThreeDecimal(Double.valueOf(etDiscountPrice.getText().toString().trim())
                                /Double.valueOf(etSalePrice.getText().toString().trim())));
                    }
                }else if(EmptyUtil.isEmpty(etSalePrice.getText().toString().trim())){
                    tvOutDiscount.setText("");
                }
            }
        });
    }

    private void initGallery() {
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess");
                compressWithLs(new File(photoList.get(0)));

               /* for (String s : photoList) {
                    Log.i(TAG, s);
                    if(addToPosition<path.size()){
                        path.set(addToPosition,s);
                    }else{
                        path.add(addToPosition,s);
                    }
                }
                goodsPic = ValueUtils.listToString(photoList);*/
               /* localImagePathBean = new LocalImagePathBean();
                for (int i = 0; i < photoList.size(); i++) {
                    PathBean pathBean = new PathBean();
                    pathBean.setImgsPath(photoList.get(i));
                    pathBeenList.add(pathBean);
                }
                localImagePathBean.setPathBeen(pathBeenList);*/
              //  mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.yunma.fileprovider")   // provider(必填)
                .multiSelect(false)// 是否多选   默认：false
                .crop(false, 1, 1, 800, 800)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)// 是否现实相机按钮  默认：false
                .filePath(Environment.getExternalStorageDirectory().toString() + "/YunMa/Jhuo")          // 图片存放路径
                .build();
    }

    private void initView() {
        //// TODO: 2017-04-14
        successBean = (GetSelfGoodsResultBean.SuccessBean.ListBean)
                this.getIntent().getSerializableExtra("PublishGoodsDetials");
        assert successBean!=null;
        setGoodsColor("如图所示");
        goodsBrands = successBean.getBrand();
        setGoodsType(String.valueOf(successBean.getType()));
        goodsType = String .valueOf(successBean.getType());
        if(goodsType.equals("1")){
            if(successBean.getStocks().get(0).getSize().equals("A")){
                goodsType = "1.1";
            }else {
                goodsType = "1.2";
            }
        }
        if(successBean.getPic()!=null&&!successBean.getPic().equals("")){
            oldImgs = new ArrayList<>();
            oldImgs.addAll(Arrays.asList(successBean.getPic().split(",")));
            mAdapter.setImgsUrl(oldImgs);
        }
        for(int i=0;i<successBean.getStocks().size();i++){
            ModStocksBean stocksBean = new ModStocksBean();
            stocksBean.setNum(String.valueOf(successBean.getStocks().get(i).getNum()));
            stocksBean.setSize(String.valueOf(successBean.getStocks().get(i).getSize()));
            stocksBeenList.add(stocksBean);
        }
        selectSizeNumsAdapter = new PublishSelectSizeNumsAdapter(PublishGoodsDetials.this,PublishGoodsDetials.this);
        rvSelectSizeNums.setAdapter(selectSizeNumsAdapter);
        selectSizeNumsAdapter.setStocksBeenList(stocksBeenList);
        GoodsBrandsAdapter brandAdapter = new GoodsBrandsAdapter(PublishGoodsDetials.this);
        brands_layout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        brands_layout.setAdapter(brandAdapter);
        int index = Arrays.asList(GoodsInfos.BRANDS).indexOf(successBean.getBrand());
        brandAdapter.isSelectedPosition(index);
        brandAdapter.onlyAddAll(Arrays.asList(GoodsInfos.BRANDS));
        brands_layout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout flowTagLayout, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(flowTagLayout.getAdapter().getItem(i));
                    }
                    goodsBrands=sb.toString();
                    successBean.setBrand(goodsBrands);
                }
            }
        });
        selectedTags = new ArrayList<>();
        setGoodsTags(successBean.getLabel());
        selectedTags.addAll(Arrays.asList(successBean.getLabel().split(",")));
        tag_layout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        tagAdapter = new GoodsTagsAdapter(PublishGoodsDetials.this, selectedTags,
                PublishGoodsDetials.this, 0);
        tag_layout.setAdapter(tagAdapter);
        tagAdapter.onlyAddAll(selectedTags);
        LogUtils.log("tag---> " + selectedTags.get(0));
        etGoodsDescribe.setText(successBean.getName());
        if(successBean.getInfo()!=null){
            etGoodsRemark.setText(successBean.getInfo());
        }
        etSalePrice.setText(ValueUtils.toTwoDecimal(successBean.getSaleprice()));
        etDiscountPrice.setText(ValueUtils.toTwoDecimal(successBean.getYmprice()));
        etInDiscount.setText(ValueUtils.toThreeDecimal(successBean.getDiscount()));
        tvOutDiscount.setText(ValueUtils.toThreeDecimal(successBean.getYmprice()/successBean.getSaleprice()));
        etGoodsNumber.setText(successBean.getNumber());
        setFitPerson(String.valueOf(successBean.getSex()));
        if(successBean.getType()==1){// "服装"
            clearStatus();
            if(successBean.getStocks().get(0).getSize().contains("A")){
                tvPants.setTextColor(getResources().getColor(R.color.color_b3));
                linePants.setVisibility(View.VISIBLE);
            }else{
                tvClothes.setTextColor(getResources().getColor(R.color.color_b3));
                lineClothes.setVisibility(View.VISIBLE);
            }
        }else if(successBean.getType()==2){// "鞋子"
            clearStatus();
            tvShoes.setTextColor(getResources().getColor(R.color.color_b3));
            lineShoes.setVisibility(View.VISIBLE);
        }else if(successBean.getType()==3){// "配件"
            clearStatus();
            tvOther.setTextColor(getResources().getColor(R.color.color_b3));
            lineOther.setVisibility(View.VISIBLE);
        }else if(successBean.getType()==4){// "男童"
            clearStatus();
            tvChildBoy.setTextColor(getResources().getColor(R.color.color_b3));
            lineChildBoy.setVisibility(View.VISIBLE);
        }else if(successBean.getType()==5){// "女童"
            clearStatus();
            tvChildGirl.setTextColor(getResources().getColor(R.color.color_b3));
            lineChildGirl.setVisibility(View.VISIBLE);
        }else{// 默认 "鞋子"
            clearStatus();
            tvClothes.setTextColor(getResources().getColor(R.color.color_b3));
            lineClothes.setVisibility(View.VISIBLE);
        }
        if(successBean.getSex()==1){
            selectSex(1);
        }else if(successBean.getSex()==2){
            selectSex(2);
        }
        GetQiniuTokenPre tokenPre = new GetQiniuTokenPre(PublishGoodsDetials.this);
        tokenPre.getToken(mContext,"yunma");
    }

    private void selectSex(int type) {
        switch (type){
            case 1:// 女
                tvGenderMan.setTextColor(getResources()
                        .getColor(R.color.white));
                tvGenderMan.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_light_orange));
                tvGenderWoman.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvGenderWoman.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_gray_light));
                break;
            case 2:// 男
                tvGenderMan.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvGenderMan.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_gray_light));
                tvGenderWoman.setTextColor(getResources()
                        .getColor(R.color.white));
                tvGenderWoman.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_light_orange));
                break;
        }
    }

    /**
     * 清楚选中状态
     */
    private void clearStatus() {
        tvShoes.setTextColor(getResources().getColor(R.color.color_b1));
        tvClothes.setTextColor(getResources().getColor(R.color.color_b1));
        tvChildBoy.setTextColor(getResources().getColor(R.color.color_b1));
        tvChildGirl.setTextColor(getResources().getColor(R.color.color_b1));
        tvOther.setTextColor(getResources().getColor(R.color.color_b1));
        tvPants.setTextColor(getResources().getColor(R.color.color_b1));
        linePants.setVisibility(View.INVISIBLE);
        lineShoes.setVisibility(View.INVISIBLE);
        lineClothes.setVisibility(View.INVISIBLE);
        lineOther.setVisibility(View.INVISIBLE);
        lineChildBoy.setVisibility(View.INVISIBLE);
        lineChildGirl.setVisibility(View.INVISIBLE);
    }

    private void initBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(PublishGoodsDetials.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }


    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    @Override
    public void addPicClick(int position) {
        if(oldImgs.size()!=9){
            selectedPosition = position;
            galleryConfig.getBuilder().multiSelect(false).isOpenCamera(false).build();
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PublishGoodsDetials.this);
        }else{
            ToastUtils.showError(mContext,"最多支持上传9张图片");
        }
    }

    @Override
    public void prePicClick(int position) {
        if(oldImgs.size()!=9){
            selectedPosition = position;
            galleryConfig.getBuilder().multiSelect(false).isOpenCamera(false).build();
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PublishGoodsDetials.this);
        }else{
            ToastUtils.showError(mContext,"最多支持上传9张图片");
        }
    }

    /**
     * 更换图片
     * @param position
     */
    @Override
    public void removePicClick(int position) {
        oldImgs.remove(position);
        mAdapter.notifyItemRemoved(position);
       // mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNewSize() {
        switch (goodsType) {
            case "2":
                showDialog(GoodsInfos.SHOES);
                break;
            case "1_1":
                showDialog(GoodsInfos.PANTS);
                break;
            case "1_0":
                showDialog(GoodsInfos.CLOTHES);
                break;
            case "4":
                showDialog(GoodsInfos.CHILD);
                break;
            case "5":
                showDialog(GoodsInfos.CHILD);
                break;
            case "3":
                showDialog(GoodsInfos.OTHERS);
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
                            ModStocksBean stocksBean = new ModStocksBean();
                            stocksBean.setSize(getmSize());
                            stocksBean.setNum(String.valueOf(getmNums()));
                            stocksBeenList.add(stocksBean);
                            selectSizeNumsAdapter.notifyDataSetChanged();
                            LogUtils.log("stocksBeenList.size  ---> " + stocksBeenList.toString());
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
        LogUtils.log("stocksBeenList.size  ---> " + stocksBeenList.toString());
    }

    @Override
    public void onAddNumber(int position, int currNums) {
        stocksBeenList.get(position).setNum(String.valueOf(currNums + 1));
        selectSizeNumsAdapter.notifyDataSetChanged();
        LogUtils.log("stocksBeenList.size  ---> " + stocksBeenList.toString());
    }

    @Override
    public void onMinusNumbers(int position, int currNums) {
        if (currNums == 0) {
            stocksBeenList.remove(position);
        } else {
            stocksBeenList.get(position).setNum(String.valueOf(currNums - 1));
        }
        selectSizeNumsAdapter.notifyDataSetChanged();
        LogUtils.log("stocksBeenList.size  ---> " + stocksBeenList.toString());
    }

    @Override
    public void addTags() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.item_goods_add_tags)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        FlowTagLayout addTags = (FlowTagLayout) contentView.findViewById(R.id.addTags);
                        addTags.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
                        GoodsTagsAdapter tagAdapter = new GoodsTagsAdapter(PublishGoodsDetials.this,
                                Arrays.asList(GoodsInfos.TAGS), PublishGoodsDetials.this, 1);
                        addTags.setAdapter(tagAdapter);
                        tagAdapter.onlyAddAll(Arrays.asList(GoodsInfos.TAGS));
                        addTags.setOnTagSelectListener(new OnTagSelectListener() {
                            @Override
                            public void onItemSelect(FlowTagLayout flowTagLayout, List<Integer> selectedList) {
                                StringBuilder sb = new StringBuilder();
                                for (int i : selectedList) {
                                    sb.append(flowTagLayout.getAdapter().getItem(i));
                                    sb.append(",");
                                }
                                setSelectedTags(Arrays.asList(sb.toString().split(",")));
                                setGoodsTags(sb.toString().substring(0,sb.toString().length()-1));
                                successBean.setLabel(sb.toString());
                            }
                        });
                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                        tagAdapter.onlyAddAll(getSelectedTags());
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

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(PublishGoodsDetials.this,"图片上传中...",
                    R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @OnClick({R.id.layoutShoes, R.id.layoutCloches,R.id.layoutPants, R.id.layoutChildBoy,R.id.layoutChildGirl,
            R.id.layoutOther,R.id.layoutBack, R.id.btnSave, R.id.tvGenderMan,
            R.id.tvGenderWoman, R.id.rvSelectSizeNums})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSave:
                if (getGoodsInfos()) {
                    progressShow();
                    toPackageGoodsInfo();
                }
                break;
            case R.id.layoutShoes://类型(1-服装,2-鞋子,3-配件,4-童)
                setGoodsType("2");
                goodsType = "2";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvShoes.setTextColor(getResources().getColor(R.color.color_b3));
                lineShoes.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutCloches://类型(1-服装,2-鞋子,3-配件,4-童)
                setGoodsType("1");
                goodsType = "1_0";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvClothes.setTextColor(getResources().getColor(R.color.color_b3));
                lineClothes.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutPants://类型(1-服装,2-鞋子,3-配件,4-童)
                setGoodsType("1");
                goodsType = "1_1";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvPants.setTextColor(getResources().getColor(R.color.color_b3));
                linePants.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutChildBoy://类型(1-服装,2-鞋子,3-配件,4-童(男)，5-童(女))
                setGoodsType("4");
                goodsType = "4";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvChildBoy.setTextColor(getResources().getColor(R.color.color_b3));
                lineChildBoy.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutChildGirl://类型(1-服装,2-鞋子,3-配件,4-童(男)，5-童(女))
                setGoodsType("5");
                goodsType = "5";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvChildGirl.setTextColor(getResources().getColor(R.color.color_b3));
                lineChildGirl.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutOther://类型(1-服装,2-鞋子,3-配件,4-童)
                setGoodsType("3");
                goodsType = "3";
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvOther.setTextColor(getResources().getColor(R.color.color_b3));
                lineOther.setVisibility(View.VISIBLE);
                break;
            case R.id.tvGenderMan:
                setFitPerson("1");
                successBean.setSex(1);
                selectSex(1);
                break;
            case R.id.tvGenderWoman:
                setFitPerson("2");
                successBean.setSex(2);
                selectSex(2);
                break;
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file) {
        Luban.get(PublishGoodsDetials.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }
                    @Override
                    public void onSuccess(File file) {

                        getUpimg(file.getAbsolutePath());
                    }
                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError(mContext,"图压缩失败"
                                + "\n" + "请稍后重试");
                        progressDimiss();
                    }
                }).launch();
    }

    /**
     * 上传七牛
     * @param path
     */
    private void getUpimg(final String path) {
        final String key = "Jhuo_YunMa_" + DateTimeUtils.getCurrentTimeInString(
                new SimpleDateFormat("MMddHHmmSSS", Locale.CHINA))+ ".jpg";
        if(qiNiuToken!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getUploadManager().put(path, key,qiNiuToken,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if(info.isOK()) {
                                        LogUtils.log("Upload Success : " + key);
                                        if(selectedPosition==oldImgs.size()){
                                            oldImgs.add(selectedPosition,key);
                                        }else{
                                            oldImgs.set(selectedPosition,key);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }else{
                                        LogUtils.log("Upload Fail");
                                        progressDimiss();
                                    }
                                }
                            }, null);
                }
            }).start();
        }else{
            progressDimiss();
            ToastUtils.showError(getApplicationContext(),"上传失败");
        }
    }

    @Override
    public void modSelfGoodsByIdView(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else{
            progressDimiss();
            ToastUtils.showSuccess(mContext,msg);
            new MyHandler(this).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    AppManager.getAppManager().finishActivity(PublishGoodsDetials.this);
                }
            },2000);
        }
    }

    private static class MyHandler extends Handler{
        WeakReference<PublishGoodsDetials> mActivity;

        MyHandler(PublishGoodsDetials activity) {
            mActivity = new WeakReference<>(activity);

        }

       @Override
        public void handleMessage(Message msg) {
            PublishGoodsDetials theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    ModSelfGoodsPre modSelfGoodsPre= new ModSelfGoodsPre(theActivity);
                    modSelfGoodsPre.modifyRepertoryById(theActivity,theActivity.modifyBean);
                    break;
            }
        }
    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else {
            qiNiuToken = resultBean.getSuccess();
        }
    }

    private void toPackageGoodsInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                modifyBean = new ModifyYunmasBean();
                modifyBean.setToken(SPUtils.getToken(mContext));
                modifyBean.setId(String.valueOf(successBean.getId()));
                modifyBean.setName(getGoodsDescribe());
                modifyBean.setNumber(getGoodsNumber());
                modifyBean.setInfo(getGoodsRemark());
                modifyBean.setBrand(goodsBrands);
                modifyBean.setSaleprice(getGoodsSalePrice());
                modifyBean.setDiscount(getGoodsDiscount());
                modifyBean.setYmprice(getGoodsDiscountPrice());
                modifyBean.setType(getGoodsType());
                modifyBean.setLabel(getGoodsTags());
                modifyBean.setStocks(stocksBeenList);
                LogUtils.log("stocksBeenList.size  ---> " + stocksBeenList.toString());
                modifyBean.setPic(ValueUtils.listToString(oldImgs));
                mHandler.sendEmptyMessage(0x333);
            }
        }).start();

    }

    private boolean getGoodsInfos() {
        // TODO: 2017-04-14
        setGoodsPic(ValueUtils.listToString(oldImgs));
        if (EmptyUtil.isEmpty(getGoodsPic()) || getGoodsPic().split(",").length < 3) {
            ToastUtils.showWarning(mContext, "请添加商品图片，并且不少于3张");
            return false;
        } else if (EmptyUtil.isEmpty(goodsBrands)) {
            ToastUtils.showWarning(mContext, "请选择商品品牌");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsNumber())) {
            ToastUtils.showWarning(mContext, "请添加商品货号");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsType())) {
            ToastUtils.showWarning(mContext, "请选择商品类型");
            return false;
        } else if (EmptyUtil.isEmpty(getFitPerson())) {
            ToastUtils.showWarning(mContext, "请选择商品适用人群");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsColor())) {
            ToastUtils.showWarning(mContext, "请选择商品颜色");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsTags())) {
            ToastUtils.showWarning(mContext, "请添加商品标签");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsDescribe())) {
            ToastUtils.showWarning(mContext, "请添加商品描述");
            return false;
        } else if (EmptyUtil.isEmpty(stocksBeenList)) {
            ToastUtils.showWarning(mContext, "请添加商品尺码");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsDiscount())) {
            ToastUtils.showWarning(mContext, "请输入商品折扣");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsSalePrice())) {
            ToastUtils.showWarning(mContext, "请输入商品吊牌价");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsDiscountPrice())) {
            ToastUtils.showWarning(mContext, "请输入商品零售价");
            return false;
        }else if(EmptyUtil.isEmpty(getGoodsInDisCount())){
            ToastUtils.showWarning(mContext, "请输入商品入库折扣");
            return false;
        } else {
            return true;
        }
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public String getGoodsTags() {
        return goodsTags;
    }

    public void setGoodsTags(String goodsTags) {
        this.goodsTags = goodsTags;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsNumber() {
        return etGoodsNumber.getText().toString().trim();
    }
    public String getGoodsType() {
        return goodsType;
    }
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
    public String getFitPerson() {
        return fitPerson;
    }
    public void setFitPerson(String fitPerson) {
        this.fitPerson = fitPerson;
    }
    public String getGoodsColor() {
        return goodsColor;
    }
    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }
    public String getGoodsDescribe() {
        return etGoodsDescribe.getText().toString().trim();
    }
    public String getGoodsDiscount() {
        return tvOutDiscount.getText().toString().trim();
    }
    public String getGoodsSalePrice() {
        return etSalePrice.getText().toString().trim();
    }
    public String getGoodsDiscountPrice() {
        return etDiscountPrice.getText().toString().trim();
    }
    public String getGoodsInDisCount(){
        return etInDiscount.getText().toString().trim();
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

    private String getGoodsRemark() {
        if (EmptyUtil.isEmpty(etGoodsRemark.getText().toString().trim()))
            return "";
        else
            return etGoodsRemark.getText().toString().trim();
    }

    @Override
    public void onFinishDrag() {
        LogUtils.log("当前图片值：" + ValueUtils.listToString(oldImgs));
    }
}
