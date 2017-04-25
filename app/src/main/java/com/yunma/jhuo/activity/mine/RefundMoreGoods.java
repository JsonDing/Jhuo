package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.*;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.RefundMoreGoodsAdapter;
import com.yunma.adapter.RefundMoreGoodsAdapter.*;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyActivity;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.ServiceInterface.AddServiceView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.*;
import me.nereo.multi_image_selector.MultiImageSelector;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.tencent.open.utils.Global.getContext;

@SuppressWarnings("unchecked")
public class RefundMoreGoods extends MyActivity implements SaveEditListener,SaveSpinnerListener,
        SaveImgsListener, GetQiniuTokenView ,AddServiceView {
    private static final int REQUEST_IMAGE = 2;
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.layoutNews) View layoutNews;
    private Context mContext;
    private RefundMoreGoodsAdapter refundAdapter;
    private UpLoadServiceBean upLoadServiceBean = null;
    private List<UpLoadServiceBean.ServicesBean> servicesBeanList;
    private Map<Integer, String> editMap = new HashMap<>();
    private Map<Integer, String> spinnerMap = new HashMap<>();
    private MyHandler mHandler=new MyHandler(this);
    private int currImgsPosition;
    private List<OrderdetailsBean> listBean;
    private List<String> mStringList;
    private int currentPosition = -1;
    private int endPosition = -1;
    private String qiNiuToken = null;
    private CustomProgressDialog dialog = null;
    private CustomProgressDialog dialog1 = null;
    private GoodsServicePre goodsServicePre;
    private List<PathBean> imgUrlList = new ArrayList<>();
    private ArrayList<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_more_goods);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(RefundMoreGoods.this);
        initStatusBar();
        getDatas();
        initUpLoadBean();

    }

    private void initUpLoadBean() {
        upLoadServiceBean = new UpLoadServiceBean();
        upLoadServiceBean.setOid(listBean.get(0).getOid());
        upLoadServiceBean.setPic("Jhou_image");
        upLoadServiceBean.setRemark("remark");
        upLoadServiceBean.setToken(SPUtils.getToken(mContext));
        servicesBeanList = new ArrayList<>();
        for(int i=0;i<listBean.size();i++){
            UpLoadServiceBean.ServicesBean servicesBean
                    = upLoadServiceBean.new ServicesBean();
            servicesBean.setNum("1");
            servicesBean.setOrderdetailid(listBean.get(i).getId());
            servicesBean.setRemark("");
            servicesBeanList.add(servicesBean);
        }
        upLoadServiceBean.setServices(servicesBeanList);
        goodsServicePre = new GoodsServicePre(this);
    }

    private void getDatas() {
        listBean = (List<OrderdetailsBean>) this.getIntent().getSerializableExtra("refundBean");
        if (EmptyUtil.isNotEmpty(listBean)) {
            refundAdapter = new RefundMoreGoodsAdapter(RefundMoreGoods.this,
                    listBean,RefundMoreGoods.this);
            recyclerview.setAdapter(refundAdapter);
            recyclerview.smoothScrollToPosition(0);
        }
        editMap.clear();
        spinnerMap.clear();
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(RefundMoreGoods.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        LinearLayoutManager layoutManagerCenter
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(layoutManagerCenter);
        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(recyclerview);
    }

    @OnClick({R.id.layoutBack,R.id.btnSubmit,R.id.layoutNews})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSubmit:
                if(getEditTextValue()){
                    currentPosition = 0;
                    endPosition = servicesBeanList.size()-1;
                    progress1Show();
                    GetQiniuTokenPre qiniuTokenPre = new GetQiniuTokenPre(this);
                    qiniuTokenPre.getToken(mContext,"service");
                }
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(RefundMoreGoods.this,ESQ,null);
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void compressPic(String[] strs) {
        mStringList = new ArrayList<>();
        progressShow();
        for (String str : strs) {
            compressWithLs(new File(str), strs.length);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 压缩图片 Listener 方式
     */
    private void compressWithLs(File file, final int picSize) {

        Luban.get(RefundMoreGoods.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File file) {
                        mStringList.add(file.getAbsolutePath());
                        LogUtils.log("压缩后路径：" + file.getAbsolutePath());
                        if(mStringList.size()==picSize){
                            for(int i=0;i<mStringList.size();i++){
                                getUpimg(mStringList.get(i),i);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressDimiss();
                        ToastUtils.showError(mContext,"请稍后重试");
                    }
                }).launch();
    }

    private void getUpimg(final String imagePath, final int position) {
        final String key = "Jhuo_Service_" + DateTimeUtils.getCurrentTimeInString(
                new SimpleDateFormat("MMddHHmmSSS",Locale.CHINA)) + position + ".png";
        if(qiNiuToken!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getUploadManager().put(imagePath, key, qiNiuToken,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if(info.isOK()) {
                                        LogUtils.log("Upload Success : " + key);
                                        PathBean pathBean = new PathBean();
                                        pathBean.setImgsPath(key);
                                        imgUrlList.add(pathBean);
                                        if (imgUrlList.size() == mStringList.size()) {
                                            mHandler.sendEmptyMessage(0x333);
                                        }
                                    }else{
                                        progressDimiss();
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        LogUtils.log("Upload Fail : " + position + "Type" + info.error);
                                    }
                                }
                            }, null);
                }
            }).start();
        }else{
            progressDimiss();
            ToastUtils.showError(getContext(),"上传失败");
        }
    }

   /* private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < imgUrlList.size(); i++){
                        sb.append(imgUrlList.get(i).getImgsPath());
                        sb.append(",");
                    }
                    servicesBeanList.get(currentPosition).setPic(sb.toString());
                    if(currentPosition == endPosition){
                        goodsServicePre.addToService(mContext,upLoadServiceBean);
                     //   LogUtils.log("测试数据：上传服务器前 ---> " + new Gson().toJson(upLoadServiceBean));
                    }else{
                        mStringList.clear();
                        imgUrlList.clear();
                        currentPosition = currentPosition + 1;
                        compressPic(servicesBeanList.get(currentPosition).getPic().split(","));
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

    private static class MyHandler extends Handler{
        WeakReference<RefundMoreGoods> mActivity;

        MyHandler(RefundMoreGoods activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RefundMoreGoods theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < theActivity.imgUrlList.size(); i++){
                        sb.append(theActivity.imgUrlList.get(i).getImgsPath());
                        sb.append(",");
                    }
                    theActivity.servicesBeanList.get(
                            theActivity.currentPosition).setPic(sb.toString());
                    if(theActivity.currentPosition == theActivity.endPosition){
                        theActivity.goodsServicePre.addToService(
                                theActivity,theActivity.upLoadServiceBean);
                    }else{
                        theActivity.mStringList.clear();
                        theActivity.imgUrlList.clear();
                        theActivity.currentPosition = theActivity.currentPosition + 1;
                        theActivity.compressPic(theActivity.servicesBeanList.get(
                                theActivity.currentPosition).getPic().split(","));
                    }
                    break;
            }
        }
    }

    private boolean getEditTextValue() {
        //遍历处理map存储的内容
        for (int i=0;i<listBean.size();i++){
            if(editMap.get(i)!=null && !editMap.get(i).equals("")){
                servicesBeanList.get(i).setRemark(editMap.get(i));
            }

        }
        //遍历处理map存储的内容
        for (int i=0;i<listBean.size();i++){
            if(spinnerMap.get(i)!=null&&!spinnerMap.get(i).equals("")) {
                servicesBeanList.get(i).setReason(spinnerMap.get(i));
            }else{
                Toast.makeText(this,"退货原因不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath.clear();
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    sb.append(",");
                }
                List<PathBean> pathBeanList = new ArrayList<>();
                for(int i=0;i<mSelectPath.size();i++){
                    PathBean pathBean = new PathBean();
                    pathBean.setImgsPath(mSelectPath.get(i));
                    pathBeanList.add(pathBean);
                }
                listBean.get(getCurrImgsPosition()).setImgsPath(pathBeanList);
                servicesBeanList.get(currImgsPosition).setPic(sb.toString());
                refundAdapter.setListBean(listBean);
                refundAdapter.notifyItemChanged(currImgsPosition);
                recyclerview.smoothScrollToPosition(currImgsPosition);
            }
        }
    }

    @Override
    public void SaveEdit(int position, String string) {
        editMap.put(position,string);
    }

    @Override
    public void SaveSpinner(int position, String string) {
        spinnerMap.put(position,string);
    }

    @Override
    public void savaImgs(int position) {
        setCurrImgsPosition(position);
        MultiImageSelector.create(RefundMoreGoods.this)
                .showCamera(true) // 是否显示相机. 默认为显示
                .multi() // 多选模式, 默认模式;
                .count(3)  // 默认已选择图片. 只有在选择模式为多选时有效
                .start(RefundMoreGoods.this, REQUEST_IMAGE);
    }

    public int getCurrImgsPosition() {
        return currImgsPosition;
    }

    public void setCurrImgsPosition(int currImgsPosition) {
        this.currImgsPosition = currImgsPosition;
    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if(resultBean == null){
            progress1Dimiss();
            ToastUtils.showError(mContext,msg);
        }else{
            progress1Dimiss();
            qiNiuToken = resultBean.getSuccess();
            compressPic(servicesBeanList.get(0).getPic().split(","));
        }
    }

    @Override
    public void toShowAddInfos(SuccessResultBean resultBean, String msg) {
        if(EmptyUtil.isEmpty(resultBean)){
            progressDimiss();
            ToastUtils.showError(mContext,msg);
        }else{
            progressDimiss();
            btnSubmit.setEnabled(false);
            btnSubmit.setClickable(false);
            btnSubmit.setBackgroundColor(Color.parseColor("#a0a0a0"));
            ToastUtils.showSuccess(mContext,msg);
        }
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(RefundMoreGoods.this,"图片上传中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void progress1Show() {
        if (dialog1 == null) {
            dialog1 = new CustomProgressDialog(RefundMoreGoods.this,"获取上传权限...", R.drawable.pb_loading_logo_2);
        }
        dialog1.show();
    }

    public void progress1Dimiss() {
        if (dialog1 != null) {
            dialog1.dismiss();
            dialog1 = null;
        }
    }
}
