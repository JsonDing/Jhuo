package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.ToUpLoderPicAdapter;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetQiniuTokenInterface;
import com.yunma.jhuo.m.ServiceInterface;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.jhuo.p.GoodsServicePre;
import com.yunma.utils.*;
import com.yunma.widget.*;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;

import butterknife.*;
import me.nereo.multi_image_selector.MultiImageSelector;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class ApplySingleGoodsAftermarket extends MyCompatActivity implements TextWatcher,
        ToUpLoderPicAdapter.OnAddPicClick, GetQiniuTokenInterface.GetQiniuTokenView,
        ServiceInterface.AddServiceView {

    private static final int REQUEST_IMAGE = 2;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsInfo) TextView tvGoodsInfo;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.tvGoodsNum) TextView tvGoodsNum;
    @BindView(R.id.spinner) NiceSpinner spinner;
    @BindView(R.id.tvCanInput) TextView tvCanInput;
    @BindView(R.id.etDetials) EditText etDetials;
    @BindView(R.id.view5) View view5;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Context mContext;
    private int editStart ;
    private int editEnd ;
    private CharSequence temp;
    private OrderdetailsBean orderdetails;
    private String path = null;
    private String qiNiuToken = null;
    private String returnReason;
    private ToUpLoderPicAdapter mAdapter;
    private String imgUrl;
    private ArrayList<String> mSelectPath;
    private List<String> imgCompress;
    private List<PathBean> imgUrlList;
    private GoodsServicePre goodsServicePre;
    private CustomProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_goods_aftermarket);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }


    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        orderdetails = (OrderdetailsBean) bundle.getSerializable("orderdetails");
        if(EmptyUtil.isNotEmpty(orderdetails)){
            if(EmptyUtil.isNotEmpty(orderdetails.getPic())){
                GlideUtils.glidNormleFast(mContext,imgGoods,ConUtils.SElF_GOODS_IMAGE_URL +
                orderdetails.getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,imgGoods,R.drawable.default_pic);
            }
        }
        tvGoodsName.setText(orderdetails.getInfo());
        tvPrice.setText("￥" + orderdetails.getMoney());
        tvGoodsNum.setText("x" + orderdetails.getNum());
        tvGoodsInfo.setText("颜色: 如图 " + " 尺码: " + orderdetails.getSize());

    }

    private void setDatas() {
        final List<String> dataset = new LinkedList<>(
                Arrays.asList("-请选择-", "大小鞋", "顺鞋", "有色差", "尺码不合适", "质量问题",
                        "其他问题"));
        spinner.attachDataSource(dataset);
        etDetials.addTextChangedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                returnReason = dataset.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ApplySingleGoodsAftermarket.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        mSelectPath = new ArrayList<>();
        mAdapter = new ToUpLoderPicAdapter(this,mSelectPath, ApplySingleGoodsAftermarket.this);
        recyclerview.setAdapter(mAdapter);
        goodsServicePre = new GoodsServicePre(this);
    }

    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews,R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getApplicationContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(ApplySingleGoodsAftermarket.this,ESQ,
                        "你好，我正在J货看" + tvGoodsName.getText().toString());
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnSubmit:
               if (EmptyUtil.isNotEmpty(returnReason)&&EmptyUtil.isNotEmpty(mSelectPath)&&
                       mSelectPath.size()!=0){
                   progressShow();
                   GetQiniuTokenPre mPresent = new GetQiniuTokenPre(this);
                   mPresent.getToken(mContext,"service");
               }else{
                   ToastUtils.showWarning(mContext,"请选退货原因及上传图片凭证");
               }
                break;
        }
    }

    private void getUpimg(final String imagePath, final int position) {
        final String key = "Jhuo_Service_" + MD5Utils.getMD5(DateTimeUtils.getCurrentTimeInString()) + position + ".png";
        if(getQiNiuToken()!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getUploadManager().put(imagePath, key, getQiNiuToken(),
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if(info.isOK()) {
                                        imgUrl = key;
                                        LogUtils.log("Upload Success : " + key);
                                        PathBean pathBean = new PathBean();
                                        pathBean.setImgsPath(imgUrl);
                                        imgUrlList.add(pathBean);
                                        if (imgUrlList.size() == imgCompress.size()) {
                                            mHandler.sendEmptyMessage(0x333);
                                        }
                                    }else{
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        LogUtils.log("Upload Fail : " + position + "Type" + info.error);
                                    }
                                }
                            }, null);
                }
            }).start();
        }else{
            progressDimiss();
            ToastUtils.showError(mContext,"上传失败");
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < imgUrlList.size(); i++){
                        sb.append(imgUrlList.get(i));
                        LogUtils.log("上传服务器前：" + imgUrlList.get(i));
                    }
                    submitToOurService(sb.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private static class MyHandler extends Handler{
        WeakReference<ApplySingleGoodsAftermarket> mActivity;
        MyHandler(ApplySingleGoodsAftermarket activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            ApplySingleGoodsAftermarket theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < theActivity.imgUrlList.size(); i++){
                        sb.append(theActivity.imgUrlList.get(i));
                        LogUtils.log("上传服务器前：" + theActivity.imgUrlList.get(i));
                    }
                    theActivity.submitToOurService(sb.toString());
                    break;
            }
        }
    }



    /**
     * 提交服务器
     * @param pic
     */
    private void submitToOurService(String pic) {

        UpLoadServiceBean upLoadServiceBean = new UpLoadServiceBean();

        List<UpLoadServiceBean.ServicesBean> servicesBeanList = new ArrayList<>();

        UpLoadServiceBean.ServicesBean servicesBean = upLoadServiceBean.new ServicesBean();

        servicesBean.setNum(String.valueOf(orderdetails.getNum()));
        servicesBean.setOrderdetailid(String.valueOf(orderdetails.getId()));
        servicesBean.setPic(pic);
        if(EmptyUtil.isEmpty(etDetials.getText().toString().trim())){
            servicesBean.setRemark("");
        }else{
            servicesBean.setRemark(etDetials.getText().toString().trim());
        }
        servicesBean.setReason(returnReason);
        servicesBeanList.add(servicesBean);
        upLoadServiceBean.setOid(String.valueOf(orderdetails.getOid()));
        upLoadServiceBean.setRemark("J货");
        upLoadServiceBean.setPic(pic);
        upLoadServiceBean.setServices(servicesBeanList);
        upLoadServiceBean.setToken(SPUtils.getToken(mContext));
        goodsServicePre.addToService(mContext,upLoadServiceBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    sb.append("\n");
                }
                mAdapter.setImgsUrl(mSelectPath);
                mAdapter.notifyDataSetChanged();
                LogUtils.log("相册拿到的路径---->  " + sb.toString());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        editStart = etDetials.getSelectionStart();
        editEnd = etDetials.getSelectionEnd();
        tvCanInput.setText(temp.length() + "/60");
        if(temp.length()>60){
            ToastUtils.showError(getApplicationContext(),"你输入的字数已经超过了限制");
            s.delete(editStart-1,editEnd);
            int tempSelection = editStart;
            etDetials.setText(s);
            etDetials.setSelection(tempSelection);
        }

    }

    @Override
    public void addPicClick() {
        MultiImageSelector.create(ApplySingleGoodsAftermarket.this)
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                //   .single() 单选模式
                .multi() // 多选模式, 默认模式;
                .count(3)
                .origin(mSelectPath)  // 默认已选择图片. 只有在选择模式为多选时有效
                .start(ApplySingleGoodsAftermarket.this, REQUEST_IMAGE);
    }

    @Override
    public void prePicClick(int position) {

    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else {
            setQiNiuToken(resultBean.getSuccess());
            imgCompress = new ArrayList<>();
            imgUrlList = new ArrayList<>();
            for (int i = 0; i < mSelectPath.size(); i++) {
                LogUtils.log("压缩前路径：" + mSelectPath.get(i));
                compressWithLs(new File(mSelectPath.get(i)),i);
                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(ApplySingleGoodsAftermarket.this,"图片上传中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void toShowAddInfos(SuccessResultBean resultBean,String msg) {
        progressDimiss();
        if(EmptyUtil.isEmpty(resultBean)){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
        }
    }

    /*public void luBanCompressWithRx(File file, final int position){
        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                    @Override
                    public Observable<? extends File> call(Throwable throwable) {
                        return Observable.empty();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(file);
                        intent.setData(uri);
                    }
                });
    }*/

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file, final int position) {

                Luban.get(ApplySingleGoodsAftermarket.this)
                        .load(file)
                        .putGear(Luban.THIRD_GEAR)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                LogUtils.log("压缩后路径--> " + file.getAbsolutePath());
                                imgCompress.add(file.getAbsolutePath());

                                if(imgCompress.size() == mSelectPath.size()){
                                    for(int i = 0; i < imgCompress.size(); i++){
                                        LogUtils.log("上传七牛前：" + imgCompress.get(i));
                                        getUpimg(imgCompress.get(i) , i);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showError(mContext,"第" + position + "张图压缩失败"
                                        + "\n" + "请稍后重试");
                            }
                        }).launch();
            }

}
