package com.yunma.jhuo.general;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunma.R;
import com.yunma.bean.AgentListBean;
import com.yunma.bean.BannerResultBean;
import com.yunma.bean.BrandsSizeResultBean;
import com.yunma.bean.ContactBean;
import com.yunma.bean.CouponsBean;
import com.yunma.bean.GoodsBrandsResultBean;
import com.yunma.bean.GoodsSizeResultBean;
import com.yunma.bean.GoodsTypesBean;
import com.yunma.bean.InfoBean;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.dao.AgentList;
import com.yunma.dao.AppInfo;
import com.yunma.dao.ContactsInfos;
import com.yunma.dao.GoodsBrands;
import com.yunma.dao.GoodsSize;
import com.yunma.dao.GoodsType;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.AgentListDao;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.m.LoginInterface;
import com.yunma.jhuo.m.SelfGoodsInterFace.GainGoodsBrandView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GainGoodsSizeView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GainGoodsTypeView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSplashPageWordsView;
import com.yunma.jhuo.m.UserInfoInterface;
import com.yunma.jhuo.p.GainGoodsBrandsPre;
import com.yunma.jhuo.p.GainGoodsSizePre;
import com.yunma.jhuo.p.GainGoodsTypePre;
import com.yunma.jhuo.p.GetAgentListPre;
import com.yunma.jhuo.p.LoginPre;
import com.yunma.jhuo.p.NewVersionRemindPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.yunma.dao.GreenDaoUtils.getAppDao;
import static com.yunma.dao.GreenDaoUtils.getContactsDao;
import static com.yunma.dao.GreenDaoUtils.getGoodsBrandsDao;
import static com.yunma.dao.GreenDaoUtils.getGoodsSizeDao;
import static com.yunma.dao.GreenDaoUtils.getGoodsTypeDao;

public class SplashPageActivity extends AppCompatActivity implements
        LoginInterface.LoginView, GetSplashPageWordsView,EasyPermissions.PermissionCallbacks,
        GainGoodsBrandView, GainGoodsSizeView, GainGoodsTypeView, UserInfoInterface.GetAgentListView {
    private int REQUEST_CODE_PERMISSION_ADDRESS_BOOK = 1;
    @BindView(R.id.tvSpWord) TextView tvSpWord;
    private String phoneNumber = null;
    private String passWd = null;
    private boolean isCanIntent = true;
    private boolean isLoadSplashWords = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        SPUtils.setStatusHeight(this,ScreenUtils.getStatusHeight(this));
        initData();
    }

    private void initData() {
        NewVersionRemindPre newVersionRemindPre = new NewVersionRemindPre(SplashPageActivity.this);
        newVersionRemindPre.getNewVersion(SplashPageActivity.this);
        GainGoodsTypePre gainGoodsTypePre = new GainGoodsTypePre(this);
        gainGoodsTypePre.getGoodsTypes(this);
        GainGoodsBrandsPre brandsPre = new GainGoodsBrandsPre(this);
        brandsPre.getGoodsBrand(this);
        GainGoodsSizePre sizePre = new GainGoodsSizePre(this);
        sizePre.getGoodsSize(this);
        GetAgentListPre mPre = new GetAgentListPre(this);
        mPre.getAgentList();
        String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            writeConacts();
        } else {
            EasyPermissions.requestPermissions(this, "需要以下权限:\n1.READ_CONTACTS" +
                    "\n2.WRITE_CONTACTS", REQUEST_CODE_PERMISSION_ADDRESS_BOOK, perms);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isCanIntent = true;
        if (isLoadSplashWords) {
            NewVersionRemindPre newVersionRemindPre = new NewVersionRemindPre(SplashPageActivity.this);
            newVersionRemindPre.getNewVersion(SplashPageActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isLoadSplashWords = true;
    }

    @Override
    public void showNewsVersion(BannerResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        } else {
            for (int i = 0; i < resultBean.getSuccess().size(); i++) {
                if (resultBean.getSuccess().get(i).getType().equals("spword")) {
                    tvSpWord.setText(resultBean.getSuccess()
                            .get(i).getValue());
                }
            }
            getIsFirstOpen();
        }
    }

    // 判断是否首次打开app
    private void getIsFirstOpen() {
        List<AppInfo> info = getAppDao().loadAll();
        if (info.size() == 0) {
            AppInfo infos = new AppInfo(null, 1, 0, null, 0);
            getAppDao().save(infos);
        }
        getIsAutoLogin();
    }

    private void getIsAutoLogin() {
        String userId = SPUtils.getUserId(this);
        LogUtils.test("UserId: " + userId);
        if (userId != null && !userId.equals("J·货")) {
            // 存在用户
            UserInfos userMation = getUserDao().load(Long.valueOf(userId));
            if(userMation!= null && userMation.getIsAutoLogin()){
                LoginPre mPresenter = new LoginPre(SplashPageActivity.this);
                phoneNumber = userMation.getPhoneNumber();
                passWd = userMation.getPassWd();//此处passwd已MD5加密处理
                mPresenter.login(this, phoneNumber, passWd);
                LogUtils.test("trace: " + 0);
            } else {
                LogUtils.test("trace: " + 1);
                intentTOMain();
            }
        } else {
            LogUtils.test("trace: " + 2);
            intentTOMain();
        }
    }

    @Override
    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            intentTOMain();
        } else {
            String token = resultBean.getSuccess().getToken();
            saveSharePreferences(token,resultBean.getSuccess());
            updateAppInfos(token);
            updateUserInfos(resultBean.getSuccess().getInfo());
            if(resultBean.getSuccess().getCoupons().size()!= 0){ //判断是否可以获取赠送的优惠券
                showDialog(resultBean.getSuccess().getCoupons().get(0));
            }else{
                LogUtils.json("token: " + SPUtils.getToken(this));
                intentTOMain();
            }
        }
    }

    private void saveSharePreferences(String token, LoginSuccessResultBean.SuccessBean useInfo) {
        SPUtils.setToken(this, token);
        InfoBean.LvlBean mLvl = useInfo.getInfo().getLvl();
        if (mLvl != null){
            SPUtils.setRole(this, mLvl.getValue()); // 角色：运营、移动用户、、、
            SPUtils.setRoleId(this,mLvl.getId()); // 角色ID
        }
        SPUtils.setAgentId(this,useInfo.getAgent().getId());
        SPUtils.setAgentDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getDiscount()));
        SPUtils.setAgentName(this,useInfo.getAgent().getName());
        String mNick = useInfo.getAgent().getNick();
        if (mNick != null){
            SPUtils.setAgentNick(this,useInfo.getAgent().getNick());
        }else {
            SPUtils.setAgentNick(this,"");
        }
        SPUtils.setParentDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getParentDiscount()));
        SPUtils.setRootDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getRootDiscount()));
        SPUtils.setAgentPoints(this,useInfo.getAgent().getPoints());
        SPUtils.setUserId(this,String.valueOf(useInfo.getInfo().getId()));
        SPUtils.setIntegral(this,useInfo.getInfo().getPoints());
        SPUtils.setPhoneNumber(this, phoneNumber);
        SPUtils.setPassWd(this,passWd);
        int isAgent = useInfo.getInfo().getIsAgent();
        if (isAgent == 0){
            SPUtils.setIsAnget(this,false);
        } else {
            SPUtils.setIsAnget(this,true);
        }
    }

    private void updateAppInfos(String token) {
        LogUtils.test("trace: " + 3);
        AppInfoDao appInfoDao = GreenDaoManager.getInstance().getSession().getAppInfoDao();
        List<AppInfo> appInfos = appInfoDao.loadAll();
        AppInfo infos = new AppInfo(appInfos.get(0).getId(),1,1,
                token,appInfos.get(0).getIsFirstSetting());
        appInfoDao.save(infos);
    }

    private void updateUserInfos(InfoBean useInfo) {
        UserInfos userInfos = getUserDao().load(Long.valueOf(String.valueOf(useInfo.getId())));
        UserInfos ui=  new UserInfos();
        ui.setId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setUserId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setPhoneNumber(useInfo.getTel());
        ui.setPassWd(useInfo.getPass());
        ui.setImgsPhotos(useInfo.getHeadImg());
        ui.setNickName(useInfo.getName());
        ui.setIsAutoLogin(userInfos.getIsAutoLogin());
        ui.setGender(userInfos.getGender());
        ui.setRealName(useInfo.getUser());
        ui.setQq(useInfo.getQq());
        if (useInfo.getLvl() != null){
            ui.setRoleName(useInfo.getLvl().getValue());
            ui.setRoleId(useInfo.getLvl().getId());
        }
        ui.setPoints(useInfo.getPoints());
        getUserDao().save(ui);
    }

    private void showDialog(CouponsBean coupons) {
        String strCondition = "";
        String strVolumeUseRange = "";
        if (coupons.getType() == 1) {
            strCondition = "包邮";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 2) {
            strCondition = coupons.getAstrict() + "件内可用";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 3) {
            strCondition = "不限件包邮";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 4) {
            strCondition = "满" + coupons.getAstrict() + "可用";
            strVolumeUseRange = "满减券";
        } else if (coupons.getType() == 5) {
            strCondition = "不限消费金额";
            strVolumeUseRange = "满减券";
        }
        final Dialog mDialog = new Dialog(this, R.style.CenterDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.show_largess_coupon, null);
        //初始化视图
        root.findViewById(R.id.btnKnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                intentTOMain();
            }
        });
        TextView tvMoney = root.findViewById(R.id.tvMoney);
        TextView tvCondition = root.findViewById(R.id.tvCondition);
        TextView tvVolumeName = root.findViewById(R.id.tvVolumeName);
        TextView tvVolumeUseRange = root.findViewById(R.id.tvVolumeUseRange);
        tvMoney.setText(String.valueOf(coupons.getMoney()));
        tvCondition.setText(strCondition);
        tvVolumeName.setText(coupons.getName());
        tvVolumeUseRange.setText(strVolumeUseRange);
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,32); // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mDialog.dismiss();
                intentTOMain();
            }
        });
        mDialog.show();
    }

    private void intentTOMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isCanIntent){
                    Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                }
            }
        }, 3000);
    }

    @Override
    public void showGoodsType(final GoodsTypesBean resultBean, String msg) {
        if(resultBean != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<GoodsTypesBean.SuccessBean> typeList = resultBean.getSuccess();
                    Collections.reverse(typeList);
                    int size = typeList.size();
                   /* boolean isExist = sqlTableIsExist(GoodsTypeDao.TABLENAME);
                    LogUtils.json("判断数据库是否存在商品类型表单：" + isExist);
                    if(isExist){

                    }*/
                    getGoodsTypeDao().deleteAll();
                    for (int i = 0; i < size; i++) {
                        GoodsType gt = new GoodsType();
                        gt.setId(null);
                        gt.setTypeId(String.valueOf(typeList.get(i).getId()));
                        gt.setTypeName(typeList.get(i).getName());
                        gt.setIsSelect(false);
                        getGoodsTypeDao().save(gt);
                    }
                }
            }).start();
        }
    }

    @Override
    public void showGoodsBrand(final GoodsBrandsResultBean resultBean, String msg) {
        if(resultBean != null){
            List<String> brandList = resultBean.getSuccess();
            final List<BrandsSizeResultBean> mBrandList = new ArrayList<>();
            int mSize = brandList.size();
            for(int i = 0;i<mSize;i++){
                BrandsSizeResultBean bsb = new BrandsSizeResultBean(brandList.get(i));
                mBrandList.add(bsb);
            }
            //对集合排序
            Collections.sort(mBrandList, new Comparator<BrandsSizeResultBean>() {
                @Override
                public int compare(BrandsSizeResultBean lhs, BrandsSizeResultBean rhs) {
                    //根据拼音进行排序
                    return lhs.getHeaderWord().compareTo(rhs.getHeaderWord());
                }
            });
            new Thread(new Runnable() {
                int size = mBrandList.size();
                @Override
                public void run() {
                   /* boolean isExist = sqlTableIsExist(GoodsBrandsDao.TABLENAME);
                    LogUtils.json("判断数据库是否存在品牌表单：" + isExist);
                    if(isExist){

                    }*/
                    getGoodsBrandsDao().deleteAll();
                    for (int i = 0; i < size; i++) {
                        String brand = mBrandList.get(i).getName();
                        if(!brand.isEmpty()){
                            GoodsBrands gb = new GoodsBrands();
                            gb.setId(null);
                            gb.setBrand(brand);
                            gb.setIsSelect(false);
                            if(brand.equals("耐克")||brand.equals("安德玛")||brand.equals("迪士尼")
                                    ||brand.equals("彪马")||brand.equals("阿迪达斯")||brand.equals("三叶草")
                                    ||brand.equals("NEO")||brand.equals("乔丹")||brand.equals("鬼冢虎")
                                    ||brand.equals("范斯")||brand.equals("回力")||brand.equals("狼爪")
                                    ||brand.equals("新百伦")||brand.equals("乐斯菲斯")||brand.equals("回力")
                                    ||brand.equals("匡威")||brand.equals("亚瑟士")){
                                gb.setIsUsually(1);
                            }else{
                                gb.setIsUsually(0);
                            }
                            getGoodsBrandsDao().save(gb);
                        }

                    }
                }
            }).start();
        }
    }

    @Override
    public void showGoodsSize(final GoodsSizeResultBean resultBean, String msg) {
        if(resultBean != null){
            final List<String> sizeList = resultBean.getSuccess();
            new Thread(new Runnable() {
                int size = sizeList.size();
                @Override
                public void run() {
               /* boolean isExist = sqlTableIsExist(GoodsSizeDao.TABLENAME);
                    LogUtils.json("判断数据库是否存在尺码表单：" + isExist);
                    if(isExist){

                    }*/
                    getGoodsSizeDao().deleteAll();
                    for (int i = 0; i < size; i++) {
                        String mSize = sizeList.get(i);
                        if(!mSize.isEmpty()){
                            GoodsSize gs = new GoodsSize();
                            gs.setId(null);
                            gs.setSize(sizeList.get(i));
                            gs.setIsSelect(false);
                            if(mSize.equals("36")||mSize.equals("37")|| mSize.equals("38")||
                                    mSize.equals("39")|| mSize.equals("40")||mSize.equals("41")||
                                    mSize.equals("42")|| mSize.equals("S") || mSize.equals("M")||
                                    mSize.equals("L") ||  mSize.equals("XL")||mSize.equals("XXL")||
                                    mSize.equals("均码")){
                                gs.setIsUsually(1);
                            }else{
                                gs.setIsUsually(0);
                            }
                            getGoodsSizeDao().save(gs);
                        }
                    }
                }
            }).start();
        }
    }

    private void writeConacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* boolean isExist = sqlTableIsExist(ContactsInfosDao.TABLENAME);
                LogUtils.json("判断数据库是否存在联系人表单：" + isExist);
                if(isExist){

                }*/
                getContactsDao().deleteAll();
                List<ContactBean> mList = getPhoneNumberFromMobile();
                int size = mList.size();
                for (int i = 0; i < size; i++) {
                    ContactsInfos cl = new ContactsInfos();
                    cl.setId(null);
                    cl.setName(mList.get(i).getName());
                    cl.setTel(mList.get(i).getNumber());
                    cl.setIsIntro(false);
                    getContactsDao().save(cl);
                }
            }
        }).start();

    }

    public List<ContactBean> getPhoneNumberFromMobile() {
        List<ContactBean> contactBeanList = new ArrayList<>();
        Cursor cursor = SplashPageActivity.this.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);
        if (cursor != null) {
            //moveToNext方法返回的是一个boolean类型的数据
            while (cursor.moveToNext()) {
                //读取通讯录的姓名
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //读取通讯录的号码
                String temp = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String number = null;
                if(temp != null){
                    number = temp.replaceAll("\\D+", "");
                }
                if(name != null && number != null){
                    ContactBean phoneInfo = new ContactBean(name, number);
                    contactBeanList.add(phoneInfo);
                }
            }
            cursor.close();
        }
        return contactBeanList;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_ADDRESS_BOOK) {
            Toast.makeText(this, "您拒绝了「通讯录」所需要的相关权限!", Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        isCanIntent = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (passWd != null) {
            passWd = null;
        }
        if (phoneNumber != null) {
            phoneNumber = null;
        }
        GlideUtils.glidClearMemory(SplashPageActivity.this);
    }

    /*public boolean sqlTableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Jhuo_db", Context.MODE_PRIVATE, null);
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim() + "' ";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            LogUtils.json("e: " + e.getMessage());
        }
        return result;
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

        }
        return false;
    }

    @Override
    public void showAgentList(AgentListBean resultBean, String msg) {
        if(resultBean != null){
            final List<AgentListBean.SuccessBean> agentList = resultBean.getSuccess();
            new Thread(new Runnable() {
                int size = agentList.size();
                @Override
                public void run() {
                   /* boolean isExist = sqlTableIsExist(AgentListDao.TABLENAME);
                    LogUtils.json("判断数据库是否存在代理表单：" + isExist);
                    if(isExist){

                    }*/
                    getAgentListDao().deleteAll();
                    for (int i = 0; i < size; i++) {
                        AgentList al = new AgentList();
                        String id = String.valueOf(agentList.get(i).getId());
                        LogUtils.json("代理Id：" + id);
                        al.setId(Long.valueOf(id));
                        al.setDiscount(agentList.get(i).getDiscount());
                        al.setName(agentList.get(i).getName());
                        al.setNick(agentList.get(i).getNick());
                        al.setParentDiscount(agentList.get(i).getParentDiscount());
                        al.setRootDiscount(agentList.get(i).getRootDiscount());
                        al.setPoints(agentList.get(i).getPoints());
                        al.setGrade((i+1));
                        try {
                            getAgentListDao().insert(al);
                        } catch (Exception e){
                            LogUtils.json("添加代理失败");
                        }
                    }
                }
            }).start();
        }
    }

    public AgentListDao getAgentListDao() {
        return GreenDaoManager.getInstance().getSession().getAgentListDao();
    }

    public UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }
}
