package com.yunma.publish;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yunma.R;
import com.yunma.adapter.*;
import com.yunma.bean.*;
import com.yunma.domain.GoodsInfos;
import com.yunma.jhuo.general.CheckPermissionsActivity;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.m.*;
import com.yunma.jhuo.p.AddSelfGoodsPre;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.utils.*;
import com.yunma.widget.*;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.yunma.domain.GoodsInfos.TAGS;


public class ToAddGoods extends CheckPermissionsActivity implements
        ToAddGoodsAdapter.OnAddPicClick, GoodsTagsAdapter.TagsOnClick,
        GetQiniuTokenInterface.GetQiniuTokenView,
        SelfGoodsInterFace.AddSelfGoodsView, SizeNumsAdapter.OnClickListener {

    @BindView(R.id.myScrowView) MyScrollView myScrowView;
    // type 鞋 ---》 2
    @BindView(R.id.tvShoes) TextView tvShoes;
    @BindView(R.id.lineShoes) View lineShoes;
    @BindView(R.id.layoutShoes) LinearLayout layoutShoes;
    // type 衣服 ---》 1
    @BindView(R.id.tvClothes) TextView tvClothes;
    @BindView(R.id.lineClothes) View lineClothes;
    @BindView(R.id.layoutCloches) LinearLayout layoutCloches;
    // type 裤子 ---》 5
    @BindView(R.id.tvPants) TextView tvPants;
    @BindView(R.id.linePants) View linePants;
    @BindView(R.id.layoutPants) LinearLayout layoutPants;
    // type 童款 ---》 4
    @BindView(R.id.tvChild) TextView tvChild;
    @BindView(R.id.lineChild) View lineChild;
    @BindView(R.id.layoutChild) LinearLayout layoutChild;
    // type 配件 ---》 3
    @BindView(R.id.tvOther) TextView tvOther;
    @BindView(R.id.lineOther) View lineOther;
    @BindView(R.id.layoutOther) LinearLayout layoutOther;
    // 性别
    @BindView(R.id.tvGenderMan) TextView tvGenderMan;
    @BindView(R.id.tvGenderWoman) TextView tvGenderWoman;
    @BindView(R.id.tvMiddlesex) TextView tvMiddlesex;

    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.layoutChildType) LinearLayout layoutChildType;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.rvSelectSizeNums) RecyclerView rvSelectSizeNums;
    @BindView(R.id.etGoodsNumber) EditText etGoodsNumber;
    @BindView(R.id.etSeries) EditText etSeries;
    @BindView(R.id.etGoodsDescribe) EditText etGoodsDescribe;
    @BindView(R.id.etDiscountPrice) EditText etDiscountPrice;
    @BindView(R.id.etSalePrice) EditText etSalePrice;
    @BindView(R.id.tvOutDiscount) TextView tvOutDiscount;
    @BindView(R.id.etInDiscount) EditText etInDiscount;
    @BindView(R.id.layoutPublish) View layoutPublish;
    private String TAG = "---Jhuo---";
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tag_layout) FlowTagLayout tag_layout;
    @BindView(R.id.brands_layout) FlowTagLayout brands_layout;
    @BindView(R.id.etGoodsRemark) EditText etGoodsRemark;
    private ToAddGoodsAdapter mAdapter = null;
    private SizeNumsAdapter selectSizeNumsAdapter = null;
    private YunmasBeanList yunmasBean = null;
    private List<StocksBeanList> stocksBeenList = null;
    private Activity mActivity;
    private Context mContext;
    private GalleryConfig galleryConfig = null;
    private List<String> path = new ArrayList<>();
    private LocalImagePathBean localImagePathBean = null;
    private List<PathBean> pathBeenList = new ArrayList<>();
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private List<String> selectedTags;
    private GoodsTagsAdapter tagAdapter;
    private String goodsBrands;
    private String goodsNumber;
    private String goodsSeries;
    private String goodsType;//类型(1-服装,2-鞋子,3-配件,4-童)
    private String fitPerson;
    private String goodsColor;
    private String goodsTags;
    private String goodsDescribe;
    private String goodsPic;
    private String goodsDiscount;
    private String goodsSalePrice;
    private String goodsDiscountPrice;
    private String mSize = null;
    private int mNums ;
    private List<String> imgCompress;
    private String qiNiuToken;
    private List<PathBean> imgUrlList = new ArrayList<>();
    private CustomProgressDialog dialog = null;
    private String showType = "2";
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_to_add_goods);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        int statusHeight = ScreenUtils.getStatusHeight(ToAddGoods.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        myScrowView.setPadding(0, statusHeight + DensityUtils.dp2px(this, 44),
                0, DensityUtils.dp2px(this, 44));
        initRecyclerview();
        initGallery();
        initView();
    }

    private void initRecyclerview() {
        mActivity = this;
        mContext = this;
        stocksBeenList = new ArrayList<>();
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    // TODO: 2017-04-19
                    case R.id.rbShose:
                        showType = "4";
                        break;
                    case R.id.rbClothes:
                        showType = "1";
                        break;
                    case R.id.rbPants:
                        showType = "5";
                        break;
                }
            }
        });
    }

    private void initGallery() {
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                path.clear();
                pathBeenList.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                setGoodsPic(ValueUtils.listToString(photoList));
                localImagePathBean = new LocalImagePathBean();
                for (int i = 0; i < photoList.size(); i++) {
                    PathBean pathBean = new PathBean();
                    pathBean.setImgsPath(photoList.get(i));
                    pathBeenList.add(pathBean);
                }
                localImagePathBean.setPathBeen(pathBeenList);
                mAdapter.notifyDataSetChanged();
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
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)// 是否多选   默认：false
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 800, 800)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)// 是否现实相机按钮  默认：false
                .filePath(Environment.getExternalStorageDirectory().toString() + "/YunMa/Jhuo")          // 图片存放路径
                .build();
    }

    private void initView() {
        setGoodsType("2");
        setFitPerson("1");
        setGoodsColor("如图所示");
        setGoodsBrands("阿迪达斯");
        selectedTags = new ArrayList<>();
        mAdapter = new ToAddGoodsAdapter(this, path, ToAddGoods.this);
        recyclerview.setAdapter(mAdapter);
        selectSizeNumsAdapter = new SizeNumsAdapter(ToAddGoods.this, ToAddGoods.this);
        rvSelectSizeNums.setAdapter(selectSizeNumsAdapter);
        selectSizeNumsAdapter.setStocksBeanList(stocksBeenList);
        GoodsBrandsAdapter brandAdapter = new GoodsBrandsAdapter(ToAddGoods.this);
        brands_layout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        brands_layout.setAdapter(brandAdapter);
        brandAdapter.onlyAddAll(Arrays.asList(GoodsInfos.BRANDS));
        brands_layout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout flowTagLayout, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(flowTagLayout.getAdapter().getItem(i));
                    }
                    setGoodsBrands(sb.toString());
                }
            }
        });
        tag_layout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        tagAdapter = new GoodsTagsAdapter(ToAddGoods.this, selectedTags,
                ToAddGoods.this, 0);
        tag_layout.setAdapter(tagAdapter);
        tagAdapter.onlyAddAll(selectedTags);
    }

    /**
     * 选取图片
     */
    @Override
    public void addPicClick() {
        galleryConfig.getBuilder().maxSize(9).build();
        galleryConfig.getBuilder().isOpenCamera(false).build();
        initPermissions();
    }

    @Override
    public void removePicClick(int position) {
        path.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 预览图片
     * @param position
     */
    @Override
    public void prePicClick(int position) {
        Intent intent = new Intent(ToAddGoods.this, PicViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", getLocalImagePathBean());
        bundle.putInt("pos", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    /**
     * 读取相册授权
     */
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                Toast.makeText(getApplicationContext(), "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }

        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ToAddGoods.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ToAddGoods.this);
            }
        }
    }

    public LocalImagePathBean getLocalImagePathBean() {
        return localImagePathBean;
    }

    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    @OnClick({R.id.layoutShoes, R.id.layoutCloches,R.id.layoutPants, R.id.layoutChild,
            R.id.layoutOther,R.id.layoutPublish, R.id.layoutBack, R.id.btnSave, R.id.tvGenderMan,
            R.id.tvGenderWoman,R.id.tvMiddlesex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSave:
                if (getGoodsInfos()) {
                    toPackageGoodsInfo();
                    imgCompress = new ArrayList<>();
                    compressWithLs(new File(yunmasBean.getPic().split(",")[0]),0);
                    progressShow();
                }
                break;
            case R.id.layoutShoes://类型(1-服装,2-鞋子,3-配件,4-童，5-裤子)
                setGoodsType("2");
                showType = "2";
                layoutChildType.setVisibility(View.GONE);
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvShoes.setTextColor(getResources().getColor(R.color.color_b3));
                lineShoes.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutPants://类型(1-服装,2-鞋子,3-配件,4-童，5-裤子)
                setGoodsType("5");
                showType = "5";
                layoutChildType.setVisibility(View.GONE);
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvPants.setTextColor(getResources().getColor(R.color.color_b3));
                linePants.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutCloches://类型(1-服装,2-鞋子,3-配件,4-童，5-裤子)
                setGoodsType("1");
                showType = "1";
                layoutChildType.setVisibility(View.GONE);
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvClothes.setTextColor(getResources().getColor(R.color.color_b3));
                lineClothes.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutChild://类型(1-服装,2-鞋子,3-配件,4-童，5-裤子)
                setGoodsType("4");
                radioGroup.check(R.id.rbShose);
                showType = "4";
                layoutChildType.setVisibility(View.VISIBLE);
                if (stocksBeenList != null) {
                    stocksBeenList.clear();
                    selectSizeNumsAdapter.notifyDataSetChanged();
                }
                clearStatus();
                tvChild.setTextColor(getResources().getColor(R.color.color_b3));
                lineChild.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutOther://类型(1-服装,2-鞋子,3-配件,4-童，5-裤子)
                setGoodsType("3");
                showType = "3";
                layoutChildType.setVisibility(View.GONE);
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
                selectSex(1);
                break;
            case R.id.tvGenderWoman:
                setFitPerson("2");
                selectSex(2);
                break;
            case R.id.tvMiddlesex:
                setFitPerson("3");
                selectSex(3);
                break;
            case R.id.layoutPublish:

                break;
        }
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
                tvMiddlesex.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvMiddlesex.setBackground(getResources()
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
                tvMiddlesex.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvMiddlesex.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_gray_light));
                break;
            case 3:// 中性
                tvGenderMan.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvGenderMan.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_gray_light));
                tvGenderWoman.setTextColor(getResources()
                        .getColor(R.color.color_b2));
                tvGenderWoman.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_gray_light));
                tvMiddlesex.setTextColor(getResources()
                        .getColor(R.color.white));
                tvMiddlesex.setBackground(getResources()
                        .getDrawable(R.drawable.fillet_light_orange));
                break;
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file, final int curPic) {
        Luban.get(ToAddGoods.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }
                    @Override
                    public void onSuccess(File file) {
                    //    LogUtils.log("压缩后路径--> " + file.getAbsolutePath());
                        imgCompress.add(file.getAbsolutePath());
                        if(imgCompress.size() == yunmasBean.getPic().split(",").length){
                            GetQiniuTokenPre tokenPre = new GetQiniuTokenPre(ToAddGoods.this);
                            tokenPre.getToken(mContext,"yunma");
                        }else{
                            runAgain(curPic);
                        }
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
     * @param position
     */
    private void getUpimg(final String path,final int position,final int curPic) {
        final String key;
        if(position == 0){
            key = getGoodsNumber()  +  ".jpg";
        }else{
            key = getGoodsNumber()  + "_yunma_" +position + ".jpg";
        }
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
                                        PathBean pathBean = new PathBean();
                                        pathBean.setImgsPath(key);
                                        imgUrlList.add(pathBean);
                                        if (imgUrlList.size() == imgCompress.size()) {
                                            mHandler.sendEmptyMessage(0x333);
                                        }else{
                                            upLoadAgan(curPic);
                                        }
                                    }else{
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        LogUtils.log("Upload Fail : " + position + "Type" + info.error);
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

    private void upLoadAgan(int curPic) {
        getUpimg(imgCompress.get(curPic +1),curPic +1,curPic +1);
    }

    private void runAgain(int curPic) {
        compressWithLs(new File(yunmasBean.getPic().split(",")[curPic +1]),curPic +1);
    }

    private static class MyHandler extends Handler{
        WeakReference<ToAddGoods> mActivity;
        MyHandler(ToAddGoods activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            ToAddGoods theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < theActivity.imgUrlList.size(); i++){
                        sb.append(theActivity.imgUrlList.get(i));
                    }
                    theActivity.yunmasBean.setPic(sb.toString());
                    PublishGoodsBean publishGoodsBean = new PublishGoodsBean();
                    publishGoodsBean.setList("1");
                    publishGoodsBean.setToken(SPUtils.getToken(theActivity));
                    List<YunmasBeanList> beanList = new ArrayList<>();
                    beanList.add(theActivity.yunmasBean);
                    publishGoodsBean.setYunmas(beanList);
                    AddSelfGoodsPre addSelfGoodsPre = new AddSelfGoodsPre(theActivity);
                    addSelfGoodsPre.publishGoods(theActivity.getApplicationContext(),
                            publishGoodsBean);
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
            getUpimg(imgCompress.get(0),0,0);
        }
    }

    /**
     * 清楚选中状态
     */
    private void clearStatus() {
        tvShoes.setTextColor(getResources().getColor(R.color.color_b1));
        tvClothes.setTextColor(getResources().getColor(R.color.color_b1));
        tvPants.setTextColor(getResources().getColor(R.color.color_b1));
        tvChild.setTextColor(getResources().getColor(R.color.color_b1));
        tvOther.setTextColor(getResources().getColor(R.color.color_b1));
        lineShoes.setVisibility(View.INVISIBLE);
        lineClothes.setVisibility(View.INVISIBLE);
        linePants.setVisibility(View.INVISIBLE);
        lineChild.setVisibility(View.INVISIBLE);
        lineOther.setVisibility(View.INVISIBLE);
    }
    /**
     * 打包待发布货品Bean
     */
    private void toPackageGoodsInfo() {
        yunmasBean = new YunmasBeanList();
        yunmasBean.setDiscount(getGoodsDiscount());
        yunmasBean.setInfo(getGoodsRemark());
        yunmasBean.setLabel(getGoodsTags());
        yunmasBean.setNumber(getGoodsNumber());
        yunmasBean.setName(getGoodsDescribe());
        yunmasBean.setPic(getGoodsPic());
        yunmasBean.setSaleprice(getGoodsSalePrice());
        yunmasBean.setType(getGoodsType());
        yunmasBean.setYmprice(getGoodsDiscountPrice());
        yunmasBean.setBrand(getGoodsBrands());
        yunmasBean.setSex(getFitPerson());
        yunmasBean.setStocks(stocksBeenList);
        yunmasBean.setToken(SPUtils.getToken(mContext));
    }

    private String getGoodsRemark() {
        if (EmptyUtil.isEmpty(etGoodsRemark.getText().toString().trim()))
            return "";
        else
            return etGoodsRemark.getText().toString().trim();
    }

    private boolean getGoodsInfos() {
        if (EmptyUtil.isEmpty(getGoodsPic()) || getGoodsPic().split(",").length < 3) {
            ToastUtils.showWarning(mContext, "请添加商品图片，并且不少于3张");
            return false;
        } else if (EmptyUtil.isEmpty(getGoodsBrands())) {
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

    @Override
    public void onAddNewSize() {
        // TODO: 2017-04-19
        switch (showType) {
            case "2":
                showDialog(GoodsInfos.SHOES);
                break;
            case "5":
                showDialog(GoodsInfos.PANTS);
                break;
            case "1":
                showDialog(GoodsInfos.CLOTHES);
                break;
            case "4":
                showDialog(GoodsInfos.CHILD);
                break;
            case "3":
                showDialog(GoodsInfos.OTHERS);
                break;
            case "-1":
                ToastUtils.showError(mContext,"请选择童款类型");
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
                        StocksBeanList stocksBean = new StocksBeanList();
                        stocksBean.setSize(getmSize());
                        stocksBean.setNum(String.valueOf(getmNums()));
                        stocksBeenList.add(stocksBean);
                        selectSizeNumsAdapter.notifyDataSetChanged();
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

    @Override
    public void onRemoveSize(int position) {
        stocksBeenList.remove(position);
        selectSizeNumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNumber(int position, int currNums) {
        stocksBeenList.get(position).setNum(String.valueOf(currNums + 1));
        selectSizeNumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusNumbers(int position, int currNums) {
        if (currNums == 0) {
            stocksBeenList.remove(position);
        } else {
            stocksBeenList.get(position).setNum(String.valueOf(currNums - 1));
        }
        selectSizeNumsAdapter.notifyDataSetChanged();
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
                        GoodsTagsAdapter tagAdapter = new GoodsTagsAdapter(ToAddGoods.this,
                                Arrays.asList(TAGS), ToAddGoods.this, 1);
                        addTags.setAdapter(tagAdapter);
                        tagAdapter.onlyAddAll(Arrays.asList(TAGS));
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
            dialog = new CustomProgressDialog(ToAddGoods.this,"图片上传中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /***********************************************************************************************/
    public String getGoodsBrands() {
        return goodsBrands;
    }

    public void setGoodsBrands(String goodsBrands) {
        this.goodsBrands = goodsBrands;
    }

    public String getGoodsNumber() {
        return etGoodsNumber.getText().toString().trim();
    }

    public String getGoodsSeries() {
        return etSeries.getText().toString().trim();
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

    public String getGoodsTags() {
        return goodsTags;
    }

    public void setGoodsTags(String goodsTags) {
        this.goodsTags = goodsTags;
    }

    public String getGoodsDescribe() {
        return etGoodsDescribe.getText().toString().trim();
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
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

    public String getGoodsColor() {
        return goodsColor;
    }

    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
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

    @Override
    public void showAddSelfGoods(YunmasBeanResult resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
            progressDimiss();
        }else{
            ToastUtils.showSuccess(mContext,msg);
            progressDimiss();
            AppManager.getAppManager().finishActivity(this);
        }
    }

}
