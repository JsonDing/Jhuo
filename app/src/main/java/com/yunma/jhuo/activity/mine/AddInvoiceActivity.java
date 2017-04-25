package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.InvoiceInterface.AddInvoiceView;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.jhuo.p.InvoicesPre;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import me.nereo.multi_image_selector.MultiImageSelector;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddInvoiceActivity extends MyCompatActivity implements GetQiniuTokenView,AddInvoiceView {
    private static final int REQUEST_IMAGE_ONE = 1;
    private static final int REQUEST_IMAGE_TWO = 2;
    private static final int REQUEST_IMAGE_THREE = 3;
    private static final int REQUEST_IMAGE_FOUR = 4;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imgsFirst) ImageView imgsFirst;
    @BindView(R.id.imgsSecond) ImageView imgsSecond;
    @BindView(R.id.imgsThird) ImageView imgsThird;
    @BindView(R.id.tvFillInfos) TextView tvFillInfos;
    @BindView(R.id.tvUploadFile) TextView tvUploadFile;
    @BindView(R.id.tvComplete) TextView tvComplete;
    @BindView(R.id.tvCompanyName) EditText etCompanyName;
    @BindView(R.id.tvCompanyCode) EditText etCompanyCode;
    @BindView(R.id.tvRegistAddress) EditText etRegistAddress;
    @BindView(R.id.tvRegistPhone) EditText etRegistPhone;
    @BindView(R.id.tvBank) EditText etBank;
    @BindView(R.id.tvBankCardNumber) EditText etBankCardNumber;
    @BindView(R.id.btnBextStap) Button btnBextStap;
    @BindView(R.id.layoutFirst) ScrollView layoutFirst;
    @BindView(R.id.lineFirstRight) View lineFirstRight;
    @BindView(R.id.lineSecondLeft) View lineSecondLeft;
    @BindView(R.id.lineSecondRight) View lineSecondRight;
    @BindView(R.id.lineThirdLeft) View lineThirdLeft;
    @BindView(R.id.imgOne) ImageView imgOne;
    @BindView(R.id.imgTwo) ImageView imgTwo;
    @BindView(R.id.imgThree) ImageView imgThree;
    @BindView(R.id.imgFour) ImageView imgFour;
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    @BindView(R.id.layoutSecond) ScrollView layoutSecond;
    @BindView(R.id.imgColseOne) ImageView imgColseOne;
    @BindView(R.id.imgColseTwo) ImageView imgColseTwo;
    @BindView(R.id.imgColseThree) ImageView imgColseThree;
    @BindView(R.id.imgColseFour) ImageView imgColseFour;
    private Context mContext;
    private AddInvoicesBean addBean = null;
    private String qiNiuToken = null;
    private String imgUrl = null;
    private CharSequence tempCode = null;
    private int codeStart;
    private int codeEnd;
    private CharSequence tempCard = null;
    private int cardStart;
    private int cardEnd;
    private CustomProgressDialog dialog = null;
    private ArrayList<String> imgsOneUrl = new ArrayList<>();
    private ArrayList<String> imgsTwoUrl = new ArrayList<>();
    private ArrayList<String> imgsThreeUrl = new ArrayList<>();
    private ArrayList<String> imgsFourUrl = new ArrayList<>();
    private ArrayList<String> imgCompress = null;
    private List<PathBean> imgUrlList = null;
    private MyHandler mHandler=new MyHandler(this);
    private ArrayList<String> mSelectPath = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBar();
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(AddInvoiceActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        etCompanyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempCode = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                codeStart = etCompanyCode.getSelectionStart();
                codeEnd = etCompanyCode.getSelectionEnd();
                if (tempCode.length() > 18) {
                    ToastUtils.showError(getApplicationContext(),"纳税人识别号为18位");
                    s.delete(codeStart - 1, codeEnd);
                    int tempSelection = codeStart;
                    etCompanyCode.setText(s);
                    etCompanyCode.setSelection(tempSelection);
                }
            }
        });

        etBankCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempCard = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                cardStart = etBankCardNumber.getSelectionStart();
                cardEnd = etBankCardNumber.getSelectionEnd();
                if (tempCard.length() > 19) {
                    ToastUtils.showError(getApplicationContext(),"银行卡号为16-19位");
                    s.delete(cardStart - 1, cardEnd);
                    int tempSelection = cardStart;
                    etCompanyCode.setText(s);
                    etCompanyCode.setSelection(tempSelection);
                }
            }
        });
    }

    @OnClick({R.id.layoutBack, R.id.btnBextStap, R.id.imgOne, R.id.imgTwo, R.id.imgThree,
            R.id.imgFour, R.id.btnBack, R.id.btnSubmit,R.id.imgColseOne, R.id.imgColseTwo,
            R.id.imgColseThree, R.id.imgColseFour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnBextStap:
                if (EmptyUtil.isEmpty(etCompanyName.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入单位名称");
                } else if (EmptyUtil.isEmpty(etCompanyCode.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入纳税人识别号");
                }else if(etCompanyCode.getText().toString().trim().length()<18){
                    ToastUtils.showWarning(mContext, "纳税人识别号为18位，当前" +
                            etCompanyCode.getText().toString().trim().length() + "位");
                } else if (EmptyUtil.isEmpty(etRegistPhone.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入注册电话");
                } else if (EmptyUtil.isEmpty(etRegistAddress.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入注册地址");
                } else if (EmptyUtil.isEmpty(etBank.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入开户银行");
                } else if (EmptyUtil.isEmpty(etBankCardNumber.getText().toString().trim())) {
                    ToastUtils.showWarning(mContext, "请输入注册账户");
                } else if(etBankCardNumber.getText().toString().trim().length()<16){
                    ToastUtils.showWarning(mContext, "银行卡号为16-19位，当前" +
                            etBankCardNumber.getText().toString().trim().length() + "位");
                }else {
                    addBean = new AddInvoicesBean();
                    addBean.setToken(SPUtils.getToken(mContext));
                    addBean.setName(etCompanyName.getText().toString().trim());
                    addBean.setAddr(etRegistAddress.getText().toString().trim());
                    addBean.setNumber(etCompanyCode.getText().toString().trim());
                    addBean.setTel(etRegistPhone.getText().toString().trim());
                    addBean.setBank(etBank.getText().toString().trim());
                    addBean.setAccount(etBankCardNumber.getText().toString().trim());
                    layoutFirst.setVisibility(View.GONE);
                    layoutSecond.setVisibility(View.VISIBLE);
                    lineFirstRight.setBackgroundColor(Color.parseColor("#f4bd39"));
                    lineSecondLeft.setBackgroundColor(Color.parseColor("#f4bd39"));
                    imgsSecond.setImageDrawable(mContext.getResources()
                            .getDrawable(R.drawable.circle_orange));
                }
                break;
            case R.id.imgOne:
                choosePic(REQUEST_IMAGE_ONE);
                break;
            case R.id.imgTwo:
                choosePic(REQUEST_IMAGE_TWO);
                break;
            case R.id.imgThree:
                choosePic(REQUEST_IMAGE_THREE);
                break;
            case R.id.imgFour:
                choosePic(REQUEST_IMAGE_FOUR);
                break;
            case R.id.btnBack:
                layoutFirst.setVisibility(View.VISIBLE);
                layoutSecond.setVisibility(View.GONE);
                lineFirstRight.setBackgroundColor(Color.parseColor("#a0a0a0"));
                lineSecondLeft.setBackgroundColor(Color.parseColor("#a0a0a0"));
                imgsSecond.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.circle_gray));
                break;
            case R.id.btnSubmit://提交
                    btnBack.setClickable(false);
                    btnBack.setEnabled(false);
                    lineSecondRight.setBackgroundColor(Color.parseColor("#f4bd39"));
                    lineThirdLeft.setBackgroundColor(Color.parseColor("#f4bd39"));
                    imgsThird.setImageDrawable(mContext.getResources()
                            .getDrawable(R.drawable.circle_orange));
                    if(imgsFourUrl.size()>0){
                        progressShow();
                        mSelectPath.add(imgsFourUrl.get(0));
                        GetQiniuTokenPre mPresent = new GetQiniuTokenPre(this);
                        mPresent.getToken(mContext,"invoice");
                    }else if(imgsOneUrl.size()>0&&imgsTwoUrl.size()>0&&imgsThreeUrl.size()>0){
                        progressShow();
                        mSelectPath.add(imgsOneUrl.get(0));
                        mSelectPath.add(imgsTwoUrl.get(0));
                        mSelectPath.add(imgsThreeUrl.get(0));
                        GetQiniuTokenPre mPresent = new GetQiniuTokenPre(this);
                        mPresent.getToken(mContext,"invoice");
                    }else{
                     ToastUtils.showWarning(mContext,"请上传完整附件");
                    }
                break;
            case R.id.imgColseOne:
                clearView(imgColseOne,imgOne);
                imgsOneUrl.clear();
                if((imgsOneUrl==null||imgsOneUrl.size()==0)&&(imgsTwoUrl==null||
                        imgsTwoUrl.size()==0)&&(imgsThreeUrl==null||imgsThreeUrl.size()==0)){
                    imgFour.setEnabled(true);
                    imgFour.setClickable(true);
                }
                break;
            case R.id.imgColseTwo:
                clearView(imgColseTwo,imgTwo);
                imgsTwoUrl.clear();
                if((imgsOneUrl==null||imgsOneUrl.size()==0)&&(imgsTwoUrl==null||
                        imgsTwoUrl.size()==0)&&(imgsThreeUrl==null||imgsThreeUrl.size()==0)){
                    imgFour.setEnabled(true);
                    imgFour.setClickable(true);
                }
                break;
            case R.id.imgColseThree:
                clearView(imgColseThree,imgThree);
                imgsThreeUrl.clear();
                if((imgsOneUrl==null||imgsOneUrl.size()==0)&&(imgsTwoUrl==null||
                        imgsTwoUrl.size()==0)&&(imgsThreeUrl==null||imgsThreeUrl.size()==0)){
                    imgFour.setEnabled(true);
                    imgFour.setClickable(true);
                }
                break;
            case R.id.imgColseFour:
                imgsFourUrl.clear();
                initClickable(true);
                clearView(imgColseFour,imgFour);
                break;
        }
    }

    private void initClickable(boolean b) {
        imgOne.setEnabled(b);
        imgOne.setClickable(b);
        imgTwo.setEnabled(b);
        imgTwo.setClickable(b);
        imgThree.setEnabled(b);
        imgThree.setClickable(b);
        imgFour.setEnabled(b);
        imgFour.setClickable(b);
    }

    private void clearView(ImageView view1, ImageView view2) {
        view1.setVisibility(View.INVISIBLE);
        view2.setImageDrawable(getResources().getDrawable(R.drawable.add_image));
        view2.setClickable(true);
        view2.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_ONE){
            if(resultCode == RESULT_OK){
                imgsOneUrl.clear();
                imgsOneUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imgOne, imgsOneUrl.get(0));
                imgOne.setClickable(false);
                imgOne.setEnabled(false);
                imgFour.setEnabled(false);
                imgFour.setClickable(false);
                imgColseOne.setVisibility(View.VISIBLE);
            }
        }else if(requestCode == REQUEST_IMAGE_TWO){
            if(resultCode == RESULT_OK){
                imgsTwoUrl.clear();
                imgsTwoUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imgTwo, imgsTwoUrl.get(0));
                imgTwo.setClickable(false);
                imgTwo.setEnabled(false);
                imgFour.setEnabled(false);
                imgFour.setClickable(false);
                imgColseTwo.setVisibility(View.VISIBLE);
            }
        }else if(requestCode == REQUEST_IMAGE_THREE){
            if(resultCode == RESULT_OK){
                imgsThreeUrl.clear();
                imgsThreeUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imgThree, imgsThreeUrl.get(0));
                imgThree.setClickable(false);
                imgThree.setEnabled(false);
                imgFour.setEnabled(false);
                imgFour.setClickable(false);
                imgColseThree.setVisibility(View.VISIBLE);
            }
        }else if(requestCode == REQUEST_IMAGE_FOUR){
            if(resultCode == RESULT_OK){
                imgsFourUrl.clear();
                imgsFourUrl = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                GlideUtils.loadSDImgs(mContext,imgFour, imgsFourUrl.get(0));
                initClickable(false);
                imgColseFour.setVisibility(View.VISIBLE);
            }
        }
    }

    public void choosePic(int type) {
        MultiImageSelector.create(AddInvoiceActivity.this)
                .showCamera(true) // 是否显示相机. 默认为显示
                .single() //单选模式
                .start(AddInvoiceActivity.this, type);
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file) {

        Luban.get(AddInvoiceActivity.this)
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

   /* private Handler mHandler=new Handler(){
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
    };*/

    private static class MyHandler extends Handler{
        WeakReference<AddInvoiceActivity> mActivity;

        MyHandler(AddInvoiceActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddInvoiceActivity theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < theActivity.imgUrlList.size(); i++){
                        sb.append(theActivity.imgUrlList.get(i));
                        LogUtils.log("上传服务器前：" + theActivity.imgUrlList.get(i));
                    }
                    theActivity.addBean.setPic(sb.toString());
                    InvoicesPre addpre = new InvoicesPre(theActivity);
                    addpre.addInvoice(theActivity,theActivity.addBean);
                //    submitToOurService();
                    break;
            }
        }
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
                compressWithLs(new File(mSelectPath.get(i)));
                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(AddInvoiceActivity.this,"图片上传中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    @Override
    public void toShowInvoice(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else{
            imgsOneUrl.clear();
            imgsTwoUrl.clear();
            imgsThreeUrl.clear();
            imgsFourUrl.clear();
            imgCompress.clear();
            imgUrlList.clear();
            mSelectPath.clear();
            progressDimiss();
            ToastUtils.showSuccess(mContext,msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mIntent = new Intent();
                    AddInvoiceActivity.this.setResult(RESULT_OK, mIntent);
                    AppManager.getAppManager().finishActivity(AddInvoiceActivity.this);
                }
            },1500);

        }
    }
}
