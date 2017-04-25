package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.yunma.R;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.*;

public class Feedback extends MyCompatActivity {

    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.etSuggestion)
    EditText etSuggestion;
    private Context mContext;
    private SuccessResultBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(Feedback.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }


    @OnClick({R.id.layoutBack, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnSubmit:
                if(etSuggestion.getText().toString().trim().isEmpty()){
                    ToastUtils.showInfo(getApplicationContext(),"请输入您的宝贵意见"
                            + "\n" + "然后再提交");
                }else{
                    subitmitInfos();
                }
                break;
        }
    }

    private void subitmitInfos() {
        RequestParams params = new RequestParams(ConUtils.USER_FEEDBACK);
        FeedBackBean paramsBean = new FeedBackBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setType("1");
        paramsBean.setContent(etSuggestion.getText().toString().trim());
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        params.setBodyContent(strParams);
        params.setConnectTimeout(1000*5);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(getApplicationContext(),"数据解析出错");
                            return;
                        }
                        ToastUtils.showSuccess(getApplicationContext(),"反馈成功");
                        etSuggestion.setText("");
                        etSuggestion.setHint("非常感谢您的宝贵意见，如您的建议被采纳，我们将赠送精美礼品一份！");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(getApplicationContext(),"数据解析出错");
                            return;
                        }
                        ToastUtils.showError(getApplicationContext(),
                                failedResultBean.getFailed().getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtils.log("responseCode: " + responseCode + "\n" + "responseMsg: " +
                            responseMsg + "\n" + "errorResult: " + errorResult);
                    ToastUtils.showError(getApplicationContext(),"网络错误");
                }else{
                    ToastUtils.showError(getApplicationContext(),"服务器错误");
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("添加收藏夹 result: " + result);
                }
            }
        });
    }

    public class FeedBackBean{
        private String token;
        private String type;
        private String content;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
