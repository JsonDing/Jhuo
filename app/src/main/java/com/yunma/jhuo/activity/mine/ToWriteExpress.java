package com.yunma.jhuo.activity.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yunma.R;
import com.yunma.bean.WriteExpressResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GoodsRefundInterface;
import com.yunma.jhuo.p.WriteExpressPre;
import com.yunma.utils.*;
import com.yunma.widget.NiceSpinner;

import java.util.*;

import butterknife.*;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class ToWriteExpress extends MyCompatActivity implements PermissionCallbacks, GoodsRefundInterface.WriteExpressView {
    private int REQUEST_CODE_PERMISSION_SCANNER = 0;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.spinner) NiceSpinner spinner;
    @BindView(R.id.etExpressCode) EditText etExpressCode;
    @BindView(R.id.imgScanner) ImageView imgScanner;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    private Context mContext;
    private String expressName = null;
    private String serviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_write_express);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        serviceId = bundle.getString("serviceId");
        LogUtils.log("serviceId ---> " + serviceId);
    }

    private void setDatas() {
        final List<String> dataset = new LinkedList<>(
                Arrays.asList("-请选择-", "顺丰快递", "申通快递", "圆通快递", "韵达快递", "中通快递",
                        "天天快递", "EMS", "百世快递", "邮政平邮"));//1 0008 2434 2255
        spinner.attachDataSource(dataset);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expressName = dataset.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ToWriteExpress.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews,R.id.imgScanner,R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",mContext);
                WPA mWPA = new WPA(ToWriteExpress.this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(ToWriteExpress.this,ESQ,
                        "你好");
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imgScanner:
                etExpressCode.setText("");
                getPermission();
                break;
            case R.id.btnSubmit:
                if(expressName==null){
                    ToastUtils.showWarning(mContext,"请选择物流公司...");
                }else if(EmptyUtil.isEmpty(etExpressCode.getText().toString().trim())){
                    ToastUtils.showWarning(mContext,"请录入快递单号...");
                }else if(serviceId==null){
                    ToastUtils.showWarning(mContext,"系统正忙，请稍候再试，或者联系客服处理");
                }else{
                    WriteExpressPre writeExpressPre = new WriteExpressPre(this);
                    writeExpressPre.toSubmitExpressInfo(mContext,serviceId,expressName,
                            etExpressCode.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etExpressCode.setText(result);
                    assert result != null;
                    etExpressCode.setSelection(result.length());
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ToWriteExpress.this, "解析失败,请手动输入", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getPermission() {
        String[] perms = {
                Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent(ToWriteExpress.this, CaptureActivity.class);
            startActivityForResult(intent, 1);

        } else {
            EasyPermissions.requestPermissions(this, "扫面需要以下权限:" +
                    "\n1.ACCESS_CAMERA", REQUEST_CODE_PERMISSION_SCANNER, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_SCANNER) {
            Toast.makeText(this, "您拒绝了「扫面」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void toShowExpressInfos(WriteExpressResultBean resultBean,String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
        }
    }
}
