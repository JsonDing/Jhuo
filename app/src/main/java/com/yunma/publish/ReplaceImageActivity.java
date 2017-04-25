package com.yunma.publish;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yunma.R;
import com.yunma.adapter.ReplaceImageAdapter;
import com.yunma.adapter.ReplaceImageAdapter.ImagePickClick;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.bean.*;
import com.yunma.jhuo.general.CheckPermissionsActivity;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.m.GetQiniuTokenInterface.GetQiniuTokenView;
import com.yunma.jhuo.m.SelfGoodsInterFace.ModSelfGoodsView;
import com.yunma.jhuo.p.GetQiniuTokenPre;
import com.yunma.jhuo.p.ModSelfGoodsPre;
import com.yunma.utils.*;
import com.yunma.widget.MySpacesItemDecoration;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ReplaceImageActivity extends CheckPermissionsActivity implements
        ImagePickClick, GetQiniuTokenView, ModSelfGoodsView, MyItemTouchCallback.OnDragListener {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    private ItemTouchHelper itemTouchHelper;
    private Context mContext;
    private int clickItem = -1;
    private ListBean listBean = null;
    private ReplaceImageAdapter mAdapter = null;
    private List<String> oldImgs;
    private int selectedPosition = -1;
    private GalleryConfig galleryConfig = null;
    private String qiNiuToken = null;
    private ModSelfGoodsPre modSelfGoodsPre = null;
    private ModSelfYunmasBean yunmasBean = null;
    private List<ModSelfYunmasBean> yunmasBeanList = null;
    private NewModSelfGoodsBean newModSelfGoodsBean = null;
    private boolean isReady = false;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_image);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        initRecyclerView();
        initGallery();
        getDatas();
    }

    private void initBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ReplaceImageActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        recyclerview.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new ReplaceImageAdapter(mContext,ReplaceImageActivity.this);
        recyclerview.setAdapter(mAdapter);
        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerview);
        recyclerview.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerview) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition()!=oldImgs.size()) {
                    itemTouchHelper.startDrag(vh);
                    MobileUtils.Vibrate(ReplaceImageActivity.this, 70);   //震动70ms
                }
            }
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }
        });

    }

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        clickItem = bundle.getInt("ClickItem");
        listBean = (ListBean) bundle.getSerializable("SelfGoodsDetials");
        assert listBean != null;
        if(listBean.getPic()!=null&&!listBean.getPic().equals("")){
           oldImgs = new ArrayList<>();
           oldImgs.addAll(Arrays.asList(listBean.getPic().split(",")));
           mAdapter.setImgsUrl(oldImgs);
        }
        final List<StocksBean> stocksBeenList = listBean.getStocks();
        new Thread(new Runnable() {
            @Override
            public void run() {
                modSelfGoodsPre = new ModSelfGoodsPre(ReplaceImageActivity.this);
                newModSelfGoodsBean = new NewModSelfGoodsBean();
                yunmasBean = new ModSelfYunmasBean();
                yunmasBeanList = new ArrayList<>();
                newModSelfGoodsBean.setToken(SPUtils.getToken(mContext));
                newModSelfGoodsBean.setList("1");
                yunmasBean.setNumber(listBean.getNumber());
                List<ModSelfStocksBean> modSelfStocksBeanList = new ArrayList<>();
                for(int i=0;i<stocksBeenList.size();i++){
                    ModSelfStocksBean bean = new ModSelfStocksBean();
                    bean.setNum(String.valueOf(stocksBeenList.get(i).getNum()));
                    bean.setSize(stocksBeenList.get(i).getSize());
                    modSelfStocksBeanList.add(bean);
                }
                yunmasBean.setStocks(modSelfStocksBeanList);
                yunmasBeanList.add(yunmasBean);
                newModSelfGoodsBean.setYunmas(yunmasBeanList);
                mHandler.sendEmptyMessage(0x333);
            }
        }).start();
        GetQiniuTokenPre tokenPre = new GetQiniuTokenPre(ReplaceImageActivity.this);
        tokenPre.getToken(mContext,"yunma");
    }

    @OnClick({R.id.layoutBack, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSubmit:
                if(isReady){
                    yunmasBean.setPic(ValueUtils.listToString(oldImgs));
                    modSelfGoodsPre.modifyRepertory(mContext,newModSelfGoodsBean);
                }else{
                    ToastUtils.showInfo(mContext,"数据打包处理中，请稍候");
                }
                break;
        }
    }

    @Override
    public void addImageClick(int position) {
        if(oldImgs.size()!=9){
            selectedPosition = position;
            galleryConfig.getBuilder().multiSelect(false).isOpenCamera(false).build();
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ReplaceImageActivity.this);
        }else{
         ToastUtils.showError(mContext,"最多支持上传9张图片");
        }
    }

    @Override
    public void removeImageClick(int position, List<String> imgsUrl) {
        imgsUrl.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void initGallery() {
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                LogUtils.log("onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                LogUtils.log("onSuccess: ---> " + photoList.size() + " -- " + photoList.get(0));
                compressWithLs(new File(photoList.get(0)));
            }

            @Override
            public void onCancel() {
                LogUtils.log("onCancel: 取消");
            }

            @Override
            public void onFinish() {
                LogUtils.log("onFinish: 结束");
            }

            @Override
            public void onError() {
                LogUtils.log("onError: 出错");
            }
        };
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.yunma.fileprovider")   // provider(必填)
                .multiSelect(false)// 是否多选   默认：false
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .isShowCamera(true)// 是否现实相机按钮  默认：false
                .filePath(Environment.getExternalStorageDirectory().toString() + "/YunMa/Jhuo")          // 图片存放路径
                .build();
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File file) {
        Luban.get(ReplaceImageActivity.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }
                    @Override
                    public void onSuccess(File file) {
                        //    LogUtils.log("压缩后路径--> " + file.getAbsolutePath());
                        getUpimg(file.getAbsolutePath());
                    }
                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError(mContext,"图压缩失败"
                                + "\n" + "请稍后重试");
                    }
                }).launch();
    }

    @Override
    public void showQiniuToken(QiniuResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else {
            qiNiuToken = resultBean.getSuccess();
        }
    }

    /**
     * 上传七牛
     * @param path
     */
    private void getUpimg(final String path) {
        final String key = "Jhuo_YunMa_" + DateTimeUtils.getCurrentTimeInString(
                new SimpleDateFormat("MMddHHmmSSS", Locale.CHINA)) + ".jpg";
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
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        LogUtils.log("Upload Fail : " );
                                    }
                                }
                            }, null);
                }
            }).start();
        }else{
            ToastUtils.showError(getApplicationContext(),"上传失败");
        }
    }

    @Override
    public void modSelfGoodsView(ModifySelfResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,"更新成功");
            new MyHandler(this).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    AppManager.getAppManager().finishActivity(ReplaceImageActivity.this);
                }
            },1500);
        }
    }

    @Override
    public void onFinishDrag() {
        LogUtils.log("当前图片值：" + ValueUtils.listToString(oldImgs));
    }

    private static class MyHandler extends Handler {
        WeakReference<ReplaceImageActivity> mActivity;

        MyHandler(ReplaceImageActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ReplaceImageActivity theActivity = mActivity.get();
            switch (msg.what) {
                case 0x333:
                    theActivity.isReady = true;
                    break;
            }
        }
    }

    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics());
    }
}
