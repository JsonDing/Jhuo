package com.yunma;

import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.widget.*;

import com.yunma.bean.*;
import com.yunma.jhuo.general.CheckPermissionsActivity;
import com.yunma.utils.*;

import java.io.*;
import java.util.*;

import butterknife.*;
import jxl.*;

public class TestActivity extends CheckPermissionsActivity {

    @BindView(R.id.text)
    TextView text;
    private List<String> phoneList = new ArrayList<>();
    private List<String> passdList = new ArrayList<>();
    private List<ExcelBean> excelBeanList = new ArrayList<>();
    private List<DocBean> docBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);
    //    readExcel();
        getMoboleInfo();
    }

    private void getMoboleInfo() {
        LogUtils.log("手机信息：" + MobileUtils.getMobileBrand() + "\n" + MobileUtils.getMobileModel()
        + "\n" + MobileUtils.getMobileSDK() + "\n" + MobileUtils.getMobileOSVersion());
    }

    public void readExcel() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            long start = System.currentTimeMillis();
            try {
                InputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + "/excel/test.xls");
                Workbook book = Workbook.getWorkbook(is);
                Sheet sheet = book.getSheet(0);
                int Rows = sheet.getRows();
                for (int j = 1; j < Rows; ++j) {
                    phoneList.add(sheet.getCell(0, j).getContents());
                    passdList.add(sheet.getCell(1, j).getContents());
                }
                LogUtils.log("phone ---> " + ValueUtils.listToString(phoneList) + "\n" + "passwd ---> " +
                        ValueUtils.listToString(passdList));
                LogUtils.log("消耗时间  2 ：" + (System.currentTimeMillis() - start));
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

    @OnClick(R.id.text)
    public void onViewClicked() {
        queryFiles(".xls");
    }

    private void queryFiles(String str) {
        String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%" + str},
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
                            cursor.getString(dataIndex).lastIndexOf("/")+1);
                    docBean.setName(name);
                    docBeanList.add(docBean);
                    LogUtils.log("docBean: ---> " +docBean.toString());
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    }
}
