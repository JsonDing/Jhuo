package com.yunma.jhuo.activity.homepage;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.amap.api.services.core.PoiItem;
import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.bean.RecipientManageBean.SuccessBean.ListBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddRecipientface.AddLocationView;
import com.yunma.jhuo.m.DeleteRecipientInteface.DeleteRecipientView;
import com.yunma.jhuo.m.LocationInterface.LocationView;
import com.yunma.jhuo.m.ModifyRecipientInterface.ModifyRecipientView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.ContainsEmojiEditText;
import com.yunma.widget.CustomProgressDialog;

import java.util.List;

import butterknife.*;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class AddRecipient extends MyCompatActivity implements PermissionCallbacks,LocationView,
        AddLocationView, ModifyRecipientView, DeleteRecipientView {
    private int REQUEST_CODE_PERMISSION_LOCATION = 0;
    private int REQUEST_CODE_PERMISSION_ADDRESS_BOOK = 1;
    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layout) View layout;
    @BindView(R.id.etReceiverName) EditText etReceiverName;
    @BindView(R.id.tvClear) LinearLayout tvClear;
    @BindView(R.id.layoutselectReceive) LinearLayout layoutselectReceive;
    @BindView(R.id.etPhoneNum) ContainsEmojiEditText etPhoneNum;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.layoutSelectAddress) LinearLayout layoutSelectAddress;
    @BindView(R.id.etDetialAddress) ContainsEmojiEditText etDetialAddress;
    @BindView(R.id.cbSelect) CheckBox cbSelect;
    @BindView(R.id.layoutSelected) LinearLayout layoutSelected;
    @BindView(R.id.btnSaveAndUse) Button btnSaveAndUse;
    @BindView(R.id.layoutDel) View layoutDel;
    private CharSequence tempName = null;
    private int nameStart;
    private int nameEnd;
    private Context mContext;
    private AddRecipientPre addPre;
    private ModifyRecipientPre modifyPre;
    private DeleteRecipientPre deletePre;
    private LocationPre nPresenter;
    private String username, usernumber;
    private boolean isDefault = false;
    private int requestType;
    private int addressId;
    private int position;
    private CustomProgressDialog dialog = null;
    private long startDate ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        String mTittle = bundle.getString("mtittle");
        if(mTittle!=null){
            tvTittle.setText(mTittle);
            if(mTittle.equals("添加地址")){
                requestType = 0;
                getLocationPermission();
            }else if(mTittle.equals("编辑地址")){
                requestType = 1;
                layoutDel.setVisibility(View.VISIBLE);
                position = bundle.getInt("position");
                ListBean mBean = (ListBean) bundle.getSerializable("bean");
                if(mBean!=null){
                    addressId = mBean.getId();
                    etReceiverName.setText(mBean.getName());
                    etPhoneNum.setText(mBean.getTel());
                    tvAddress.setText(mBean.getRegoin());
                    etDetialAddress.setText(mBean.getAddr());
                    if(mBean.getUsed()==0){
                        cbSelect.setChecked(false);
                    }else{
                        cbSelect.setChecked(true);
                    }
                }
            }
        }
        etReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempName = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                nameStart = etReceiverName.getSelectionStart();
                nameEnd = etReceiverName.getSelectionEnd();
                if (tempName.length() > 7) {
                    ToastUtils.showError(getApplicationContext(),"你输入的字数已经超过了限制");
                    s.delete(nameStart - 1, nameEnd);
                    int tempSelection = nameStart;
                    etReceiverName.setText(s);
                    etReceiverName.setSelection(tempSelection);
                }
                if(tempName.length()!=0){
                    tvClear.setVisibility(View.VISIBLE);
                }else{
                    tvClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        nPresenter = new LocationPre(this);
        addPre = new AddRecipientPre(this);
        modifyPre = new ModifyRecipientPre(this);
        deletePre = new DeleteRecipientPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(AddRecipient.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public AddAddressBean getAddAddressBean() {
        AddAddressBean addAddressBean = new AddAddressBean();
        addAddressBean.setToken(SPUtils.getToken(mContext));
        addAddressBean.setAddr(etDetialAddress.getText().toString());
        addAddressBean.setName(etReceiverName.getText().toString());
        addAddressBean.setRegoin(tvAddress.getText().toString());
        addAddressBean.setTel(etPhoneNum.getText().toString().trim());
        if(cbSelect.isChecked()){
            addAddressBean.setUsed(1);
        }else{
            addAddressBean.setUsed(0);
        }
        return addAddressBean;
    }

    @Override
    public ModifyAddressBean getModifyAddressBean() {
        ModifyAddressBean modifyAddressBean = new ModifyAddressBean();
        modifyAddressBean.setToken(SPUtils.getToken(mContext));
        modifyAddressBean.setAddr(etDetialAddress.getText().toString());
        modifyAddressBean.setName(etReceiverName.getText().toString());
        modifyAddressBean.setRegoin(tvAddress.getText().toString());
        modifyAddressBean.setTel(etPhoneNum.getText().toString().trim());
        if(cbSelect.isChecked()){
            modifyAddressBean.setUsed(1);
        }else{
            modifyAddressBean.setUsed(0);
        }
        modifyAddressBean.setId(addressId);
        return modifyAddressBean;
    }

    @Override
    public DeleteLocationBean getDeleteLocationBean() {
        DeleteLocationBean delBean = new DeleteLocationBean();
        delBean.setToken(SPUtils.getToken(mContext));
        delBean.setIds(String.valueOf(addressId));
        return delBean;
    }

    @Override
    public void getLocation(List<PoiItem> poiItems) {

    }

    @Override
    public void getCurrAddress(String address) {
        tvAddress.setText(address);
    }

    @OnClick({R.id.layoutBack,R.id.tvClear, R.id.layoutselectReceive, R.id.layoutSelectAddress,
            R.id.layoutSelected, R.id.btnSaveAndUse,R.id.layoutDel})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.tvClear:
                etReceiverName.setText("");
                break;
            case R.id.layoutselectReceive:
                getAddressBookPermission();
                break;
            case R.id.layoutSelectAddress:
                intent = new Intent(AddRecipient.this,SelectAddress.class);
                startActivityForResult(intent,2);
                overridePendingTransition(R.anim.bottom_in,0);
                break;
            case R.id.layoutSelected:
                if (!isDefault){
                    cbSelect.setChecked(true);
                    isDefault = true;
                }else{
                    cbSelect.setChecked(false);
                    isDefault = false;
                }
                break;
            case R.id.btnSaveAndUse:
                if(etReceiverName.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入收件人姓名");
                }else if(etPhoneNum.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入收件人联系号码");
                }else if(tvAddress.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请选择收件人地址");
                }else if(etDetialAddress.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入收件人详细地址");
                }else{
                    if(requestType==0){
                        addPre.addLocition();
                    }else if(requestType==1){
                        modifyPre.modifyLocation();
                    }
                }
                break;
            case R.id.layoutDel:
             //   progressShow();
                deletePre.deleteLocation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String address = data.getStringExtra("address");
        if (resultCode == Activity.RESULT_OK) {
            //处理返回的data,获取选择的联系人信息
            Uri uri=data.getData();
            String[] contacts = getPhoneContacts(uri);
            assert contacts != null;
            etReceiverName.setText(contacts[0]);
            if(contacts[1].contains("+86")){
                etPhoneNum.setText(contacts[1].substring(3,contacts[1].length()).replaceAll("\\D+", ""));
            }else{
                etPhoneNum.setText(contacts[1].replaceAll("\\D+", ""));
            }

        }else if(resultCode==2){
            tvAddress.setText("");
            tvAddress.setText(address);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
                //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
                //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            assert phone != null;
            phone.close();
            cursor.close();
        }else{
            return null;
        }
        return contact;
    }




    /*private void getContactPhone(Intent data) {
        Uri contactData = data.getData();
        if (contactData == null) {
            return ;
        }
        ContentResolver reContentResolverol = getContentResolver();
        Cursor cursor = managedQuery(contactData, null, null, null, null);
        cursor.moveToFirst();
        // 获得DATA表中的名字
        username = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        // 条件为联系人ID
        String contactId = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts._ID));
        // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
        Cursor phone = reContentResolverol.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                        + contactId, null, null);
        while (phone.moveToNext()) {
            usernumber = phone
                    .getString(phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        etPhoneNum.setText(usernumber.replace(" ",""));
        etReceiverName.setText(username);
        phone.close();
    }*/




    private void getAddressBookPermission() {
        String[] perms = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            this.startActivityForResult(intent, 0);
        } else {
            EasyPermissions.requestPermissions(this, "通讯录需要以下权限:\n1.READ_CONTACTS" +
                    "\n2.WRITE_CONTACTS", REQUEST_CODE_PERMISSION_ADDRESS_BOOK, perms);
        }
    }

    private void getLocationPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            nPresenter.getLocation();
        } else {
            EasyPermissions.requestPermissions(this, "定位需要以下权限:" +
                    "\n1.ACCESS_COARSE_LOCATION" + "\n2.ACCESS_FINE_LOCATION" +
                    "\n3.WRITE_EXTERNAL_STORAGE" + "\n4.READ_EXTERNAL_STORAGE" +
                    "\n5.READ_PHONE_STATE", REQUEST_CODE_PERMISSION_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_ADDRESS_BOOK) {
            Toast.makeText(this, "您拒绝了「通讯录」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        } if (requestCode == REQUEST_CODE_PERMISSION_LOCATION) {
            Toast.makeText(this, "您拒绝了「定位」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void toShowAddResult(final AddAddressResultBean addAddressResultBean, String msg) {
        if(addAddressResultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            //        progressDimiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result",addAddressResultBean);
                    intent.putExtras(bundle);
                    AddRecipient.this.setResult(1, intent);
                    AppManager.getAppManager().finishActivity(AddRecipient.this);
                }
            },1000);

        }
    }

    @Override
    public void toShowModifyResult(SuccessResultBean successResultBean, String msg) {
        if(successResultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
              //      progressDimiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result",getModifyAddressBean());
                    bundle.putInt("modifyId",position);
                    bundle.putString("type","modify");
                    intent.putExtras(bundle);
                    AddRecipient.this.setResult(2, intent);
                    AppManager.getAppManager().finishActivity(AddRecipient.this);
                }
            },1000);
        }
    }

    @Override
    public void toShowDeleteResult(SuccessResultBean successResultBean, String msg) {
        if(successResultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
               //     progressDimiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("modifyId",position);
                    bundle.putString("type","delete");
                    intent.putExtras(bundle);
                    AddRecipient.this.setResult(2, intent);
                    AppManager.getAppManager().finishActivity(AddRecipient.this);
                }
            },1000);
        }
    }


    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
        if (dialog == null) {
            dialog = new CustomProgressDialog(AddRecipient.this,"加载中...", R.drawable.pb_loading_logo_1);
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
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
        nPresenter.stopLocation();
        nPresenter.destroyLocation();
    }

}
