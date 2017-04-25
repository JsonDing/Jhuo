package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.InvoiceInterface.DelInvoiceView;
import com.yunma.jhuo.m.InvoiceInterface.EditInvoiceView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;
import com.yunma.publish.PicViewerActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import me.nereo.multi_image_selector.MultiImageSelector;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class InvoiceDetials extends MyCompatActivity implements DelInvoiceView, View.OnLongClickListener,
        GetQiniuTokenView,EditInvoiceView {

    private static final int EDIT_SUCCESS = 1;
    private static final int DEL_SUCCESS = 2;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutDel) View layoutDel;
    @BindView(R.id.imgStatusLogo) ImageView imgStatusLogo;
    @BindView(R.id.etCompany) EditText etCompany;
    @BindView(R.id.etRatepayer) EditText etRatepayer;
    @BindView(R.id.etRegisterAddress) EditText etRegisterAddress;
    @BindView(R.id.etRegisterPhone) EditText etRegisterPhone;
    @BindView(R.id.etRegisterBank) EditText etRegisterBank;
    @BindView(R.id.etRegisterAccount) EditText etRegisterAccount;
    @BindView(R.id.imageOne) ImageView imageOne;
    @BindView(R.id.imageTwo) ImageView imageTwo;
    @BindView(R.id.imageThree) ImageView imageThree;
    @BindView(R.id.btnUpdata) Button btnUpdata;
    @BindView(R.id.tvStatus) TextView tvStatus;
    private Context mContext;
    private String id = null;
    private int position = -1;
    private InvoicesPre invoicesPre = null;
    private EditInvoicePre editinvoicePre = null;
    private InvoiceBean.SuccessBean.ListBean invoiceDetials;
    private ArrayList<String> imgsOneUrl;
    private ArrayList<String> imgsTwoUrl;
    private ArrayList<String> imgsThreeUrl;
    private CustomProgressDialog dialog = null;
    private List<String> oldPicUrl = null;
    private ArrayList<String> imgCompress = null;
    private List<String> mSelectPic = null;
    private String qiNiuToken = null;
    private List<PathBean> imgUrlList;
    private AddInvoicesBean addBean = null;
    private MyHandler mHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detials);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(InvoiceDetials.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        invoicesPre = new InvoicesPre(this);
        editinvoicePre =  new EditInvoicePre(this);
        imageOne.setOnLongClickListener(this);
        imageTwo.setOnLongClickListener(this);
        imageThree.setOnLongClickListener(this);
    }

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        invoiceDetials = (InvoiceBean.SuccessBean.ListBean) bundle.getSerializable("invoiceDetials");
        if (EmptyUtil.isNotEmpty(invoiceDetials)) {
            if(invoiceDetials.getAuth().equals("0")){
                GlideUtils.glidLocalDrawable(mContext,imgStatusLogo,R.drawable.being_processed);
                tvStatus.setText("个人增票审核中");
            }else if(invoiceDetials.getAuth().equals("1")){
                GlideUtils.glidLocalDrawable(mContext,imgStatusLogo,R.drawable.succeed);
                tvStatus.setText("已通过审核");
            }else {
                GlideUtils.glidLocalDrawable(mContext,imgStatusLogo,R.drawable.be_defeated);
                tvStatus.setText("未通过审核");
            }
            position = bundle.getInt("position");
            id = invoiceDetials.getId();
            etCompany.setText(invoiceDetials.getName());
            etRatepayer.setText(invoiceDetials.getNumber());
            etRegisterAddress.setText(invoiceDetials.getAddr());
            etRegisterPhone.setText(invoiceDetials.getTel());
            etRegisterBank.setText(invoiceDetials.getBank());
            etRegisterAccount.setText(invoiceDetials.getAccount());
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            if (invoiceDetials.getPic()!=null){
                oldPicUrl = new ArrayList<>();
                for(int i=0;i<invoiceDetials.getPic().split(",").length;i++){
                    oldPicUrl.add(invoiceDetials.getPic().split(",")[i]);
                }
                if (invoiceDetials.getPic().split(",").length==1){
                    imageOne.setLayoutParams(params);
                    imageOne.setVisibility(View.VISIBLE);
                    imageTwo.setVisibility(View.GONE);
                    imageThree.setVisibility(View.GONE);
                    GlideUtils.glidNormleFast(mContext,imageOne,ConUtils.GET_INVIOCE_IMAGE +
                    invoiceDetials.getPic().split(",")[0]);
                }else{
                    imageOne.setLayoutParams(params);
                    imageTwo.setLayoutParams(params);
                    imageThree.setLayoutParams(params);
                    imageOne.setVisibility(View.VISIBLE);
                    imageTwo.setVisibility(View.VISIBLE);
                    imageThree.setVisibility(View.VISIBLE);
                    GlideUtils.glidNormleFast(mContext,imageOne,ConUtils.GET_INVIOCE_IMAGE +
                            invoiceDetials.getPic().split(",")[0]);
                    GlideUtils.glidNormleFast(mContext,imageTwo,ConUtils.GET_INVIOCE_IMAGE +
                            invoiceDetials.getPic().split(",")[1]);
                    GlideUtils.glidNormleFast(mContext,imageThree,ConUtils.GET_INVIOCE_IMAGE +
                            invoiceDetials.getPic().split(",")[2]);
                }
            }
        }
    }

    @OnClick({R.id.imageOne, R.id.imageTwo, R.id.imageThree, R.id.btnUpdata,R.id.layoutBack,
            R.id.layoutDel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageOne:
                previewPhotos(0);
                break;
            case R.id.imageTwo:
                previewPhotos(1);
                break;
            case R.id.imageThree:
                previewPhotos(2);
                break;
            case R.id.btnUpdata:
                toUpDatasNewInvoice();
                break;
            case R.id.layoutBack:
               AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutDel:
                if(id!=null){
                     showDialog();
                }else{
                    ToastUtils.showError(mContext,"服务器异常，请稍后重试");
                }
                break;
        }
    }

    private void showDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("赠票一经删除后，无法恢复，必须重新提交申请" + "\n"  +"是否确认删除？");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteBean deleteBean = new DeleteBean();
                        deleteBean.setIds(id);
                        deleteBean.setToken(SPUtils.getToken(mContext));
                        invoicesPre.delInvoice(mContext,deleteBean);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    private void toUpDatasNewInvoice() {
        if (EmptyUtil.isEmpty(etCompany.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入单位名称");
        } else if (EmptyUtil.isEmpty(etRatepayer.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入纳税人识别号");
        } else if (EmptyUtil.isEmpty(etRegisterPhone.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入注册电话");
        } else if (EmptyUtil.isEmpty(etRegisterAddress.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入注册地址");
        } else if (EmptyUtil.isEmpty(etRegisterBank.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入开户银行");
        } else if (EmptyUtil.isEmpty(etRegisterAccount.getText().toString().trim())) {
            ToastUtils.showWarning(mContext, "请输入注册账户");
        } else {
            addBean = new AddInvoicesBean();
            addBean.setId(id);
            addBean.setToken(SPUtils.getToken(mContext));
            addBean.setName(etCompany.getText().toString().trim());
            addBean.setNumber(etRatepayer.getText().toString().trim());
            addBean.setTel(etRegisterPhone.getText().toString().trim());
            addBean.setAddr(etRegisterAddress.getText().toString().trim());
            addBean.setBank(etRegisterBank.getText().toString().trim());
            addBean.setAccount(etRegisterAccount.getText().toString().trim());
            mSelectPic = new ArrayList<>();
            LogUtils.log("mSelectPic " + mSelectPic.size());
            if(imgsOneUrl != null && imgsOneUrl.size()!=0){
                mSelectPic.add(imgsOneUrl.get(0));
                LogUtils.log("imgsOneUrl " + imgsOneUrl.size());
            }
            if(imgsTwoUrl != null && imgsTwoUrl.size()!=0){
                mSelectPic.add(imgsTwoUrl.get(0));
                LogUtils.log("imgsTwoUrl " + imgsTwoUrl.size());
            }
            if(imgsThreeUrl != null && imgsThreeUrl.size()!=0){
                mSelectPic.add(imgsThreeUrl.get(0));
                LogUtils.log("imgsThreeUrl " + imgsThreeUrl.size());
            }
            if(mSelectPic.size()==0){
                addBean.setPic(invoiceDetials.getPic());
                editinvoicePre.editInvoice(mContext,addBean);
            }else{
                progressShow();
                imgCompress = new ArrayList<>();
                imgUrlList = new ArrayList<>();
                for(int i = 0;i<mSelectPic.size();i++){
                    compressWithLs(new File(mSelectPic.get(i)));
                    try {
                        Thread.sleep(120);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    private void previewPhotos(int position) {
        String s[] = invoiceDetials.getPic().split(",");
        LocalImagePathBean preBean = new LocalImagePathBean();
        List<PathBean> pathBeenList = new ArrayList<>();
        for (String value : s) {
            PathBean pathBean = new PathBean();
            pathBean.setImgsPath(ConUtils.GET_INVIOCE_IMAGE + value);
            pathBeenList.add(pathBean);
        }
        preBean.setPathBeen(pathBeenList);
        Intent intent = new Intent(mContext,PicViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path",preBean);
        bundle.putInt("pos", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void toShowDelInvoice(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("position",position);
                    InvoiceDetials.this.setResult(DEL_SUCCESS, mIntent);
                    AppManager.getAppManager().finishActivity(InvoiceDetials.this);
                }
            },1500);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        GetQiniuTokenPre qiniuToken = new GetQiniuTokenPre(this);
        qiniuToken.getToken(mContext,"invoice");
        switch (v.getId()){
            case R.id.imageOne:
                imgsOneUrl = new ArrayList<>();
                choosePic(1);
                break;
            case R.id.imageTwo:
                imgsTwoUrl = new ArrayList<>();
                choosePic(2);
                break;
            case R.id.imageThree:
                imgsThreeUrl = new ArrayList<>();
                choosePic(3);
                break;
        }
        return false;
    }

    public void choosePic(int type) {
        MultiImageSelector.create(InvoiceDetials.this)
                .showCamera(true) // 是否显示相机. 默认为显示
                .single() //单选模式
                .start(InvoiceDetials.this, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                imgsOneUrl.clear();
                imgsOneUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imageOne, imgsOneUrl.get(0));
            }
        }else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                imgsTwoUrl.clear();
                imgsTwoUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imageTwo, imgsTwoUrl.get(0));
            }
        }else if(requestCode == 3){
            if(resultCode == RESULT_OK){
                imgsThreeUrl.clear();
                imgsThreeUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imageThree, imgsThreeUrl.get(0));
            }
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file) {

        Luban.get(InvoiceDetials.this)
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
                        if(imgCompress.size() == mSelectPic.size()){
                            for(int i = 0; i < imgCompress.size(); i++){
                                LogUtils.log("上传七牛前：" + imgCompress.get(i));
                                getUpimg(imgCompress.get(i) ,i);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError(mContext,"压缩失败"
                                + "\n" + "请稍后重试");
                    }
                }).launch();
    }

    private void getUpimg(final String imagePath, final int position) {
        final String key = "Jhuo_Invoice_" + MD5Utils.getMD5(DateTimeUtils.getCurrentTimeInString()) + position + ".png";
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
                                        LogUtils.log("Upload Success : " + key);
                                        PathBean pathBean = new PathBean();
                                        pathBean.setImgsPath(key);
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

   /* private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0x333:
                    submitToOurService();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

    private static class MyHandler extends Handler{
        WeakReference<InvoiceDetials> mActivity;
        MyHandler(InvoiceDetials activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            InvoiceDetials theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    if(theActivity.imgsOneUrl!=null &&
                            theActivity.imgsOneUrl.size()!=0){
                        theActivity.oldPicUrl.remove(0);
                    }
                    if(theActivity.imgsTwoUrl!=null &&
                            theActivity.imgsTwoUrl.size()!=0){
                        theActivity.oldPicUrl.remove(1);
                    }
                    if(theActivity.imgsThreeUrl!=null&&
                            theActivity.imgsThreeUrl.size()!=0){
                        theActivity.oldPicUrl.remove(2);
                    }
                    for(int i=0;i<theActivity.imgUrlList.size();i++){
                        theActivity.oldPicUrl.add(theActivity.imgUrlList.get(i).getImgsPath());
                    }
                    LogUtils.log(" --- > " + ValueUtils.listToString(theActivity.oldPicUrl));
                    theActivity.addBean.setPic(ValueUtils.listToString(theActivity.oldPicUrl));
                    theActivity.editinvoicePre.editInvoice(theActivity,theActivity.addBean);
                    break;
            }
        }
    }

/*
    private void submitToOurService() {
        if(imgsOneUrl!=null&&imgsOneUrl.size()!=0){
            oldPicUrl.remove(0);
        }
        if(imgsTwoUrl!=null&&imgsTwoUrl.size()!=0){
            oldPicUrl.remove(1);
        }
        if(imgsThreeUrl!=null&&imgsThreeUrl.size()!=0){
            oldPicUrl.remove(2);
        }
        for(int i=0;i<imgUrlList.size();i++){
            oldPicUrl.add(imgUrlList.get(i).getImgsPath());
        }
        LogUtils.log(" --- > " + ValueUtils.listToString(oldPicUrl));
        addBean.setPic(ValueUtils.listToString(oldPicUrl));
        editinvoicePre.editInvoice(mContext,addBean);
    }*/


    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(InvoiceDetials.this,"图片上传中...", R.drawable.pb_loading_logo_1);
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
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else {
            setQiNiuToken(resultBean.getSuccess());
        }
    }

    @Override
    public void toShowEditInvoice(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else{
            progressDimiss();
            ToastUtils.showSuccess(mContext,msg);
        }
    }
}
