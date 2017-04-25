package com.yunma;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.*;
import jxl.Sheet;
import jxl.Workbook;

public class AddMoreUser extends MyCompatActivity {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.layoutSearch)
    LinearLayout layoutSearch;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.listview)
    ListView listview;
    private Context mContext;
    private List<DocBean> docBeanList;
    private DocBeanAdapter docBeanAdapter = null;
    private List<String> phoneList = new ArrayList<>();
    private List<String> passdList = new ArrayList<>();
    private List<ExcelBean> excelBeanList = new ArrayList<>();
    private RegisterSuccessResultBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_user);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initTittlebar();
    }

    private void initTittlebar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(AddMoreUser.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = docBeanList.get(position).getPath();
                LogUtils.log("----> " + path);
                readExcel(path);
                for(int i=0;i<excelBeanList.size();i++){
                    startLogin(excelBeanList.get(i).getPhone(),excelBeanList.get(i).getPasswd());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLogin(final String phone, String passwd) {
        RequestParams params = new RequestParams(ConUtils.USER_REGISTER);
        RegisterBean mBean = new RegisterBean();
        mBean.setTel(phone);
        mBean .setPass(MD5Utils.getMD5(passwd));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(mBean);
        params.setBodyContent(strLogin);
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
                                    RegisterSuccessResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(mContext,"解析出错");
                            e.printStackTrace();
                        }
                        LogUtils.log("帐号：" +resultBean.getSuccess().getTel() + "   " +
                        "密码：" + resultBean.getSuccess().getPass());
                    //    ToastUtils.showSuccess(mContext,resultBean.getSuccess().getTel() + " 注册成功！");
                    }else {
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        //    ToastUtils.showError(mContext,failedResultBean.getFailed().getErrorMsg());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        LogUtils.log("帐号：" + phone + "  "+ failedResultBean.getFailed().getErrorMsg());

                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                hasError = true;
                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                    ToastUtils.showError(mContext,"网络错误");
                } else { // 其他错误
                    ToastUtils.showError(mContext,"服务器异常");
                    LogUtils.log("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {

                }
            }
        });
    }


    @OnClick({R.id.layoutBack, R.id.layoutSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutSearch:
                if (EmptyUtil.isNotEmpty(etInput.getText().toString().trim())) {
                    if (docBeanAdapter == null) {
                        docBeanAdapter = new DocBeanAdapter(mContext, queryFiles(etInput.getText().toString().trim()));
                        listview.setAdapter(docBeanAdapter);
                    } else {
                        docBeanAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    private List<DocBean> queryFiles(String str) {
        docBeanList = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE,
        };
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%." + str},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns._ID);
                int dataIndex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int sizeIndex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                do {
                    DocBean docBean = new DocBean();
                    docBean.setId(cursor.getString(idIndex));
                    docBean.setPath(cursor.getString(dataIndex));
                    docBean.setSize(cursor.getString(sizeIndex));
                    String name = cursor.getString(dataIndex).substring(
                            cursor.getString(dataIndex).lastIndexOf("/") + 1);
                    docBean.setName(name);
                    docBeanList.add(docBean);
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return docBeanList;
    }

    public void readExcel(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                InputStream is = new FileInputStream(path);
                Workbook book = Workbook.getWorkbook(is);
                Sheet sheet = book.getSheet(0);
                int Rows = sheet.getRows();
                for (int j = 0; j < Rows; ++j) {
                    phoneList.add(sheet.getCell(0, j).getContents());
                    passdList.add(sheet.getCell(1, j).getContents());
                    ExcelBean excelBean = new ExcelBean();
                    excelBean.setPhone(sheet.getCell(0, j).getContents());
                    excelBean.setPasswd(sheet.getCell(1, j).getContents());
                    excelBeanList.add(excelBean);
                }
                LogUtils.log("帐号密码：---》 " + excelBeanList.toString());
                book.close();
            } catch (Exception e) {
                LogUtils.log("e:---> " + e.getMessage());
            }
        } else {
            // 此时SDcard不存在或者不能进行读写操作的
            Toast.makeText(getApplicationContext(),
                    "此时SDcard不存在或者不能进行读写操作", Toast.LENGTH_SHORT).show();
        }
    }

     class DocBeanAdapter extends BaseAdapter {
        private Context mContext;
        private List<DocBean> docBeen;
        private LayoutInflater inflater;

        DocBeanAdapter(Context mContext, List<DocBean> docBeen) {
            this.mContext = mContext;
            this.docBeen = docBeen;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return docBeen.size();
        }

        @Override
        public Object getItem(int position) {
            return docBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.item_doc, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder.tvFileName.setText(docBeen.get(position).getName());
            File file = new File(docBeen.get(position).getPath());
            holder.tvCreateTime.setText(DateTimeUtils.getTime(file.lastModified(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)));
            return view;
        }

        class ViewHolder {
            @BindView(R.id.tvFileName) TextView tvFileName;
            @BindView(R.id.tvCreateTime) TextView tvCreateTime;
            @BindView(R.id.line) View line;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
