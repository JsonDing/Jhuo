package com.yunma;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.yunma.utils.LogUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yunma", appContext.getPackageName());
    }

    @Test
    public void apiTest() {
        RequestParams params = new RequestParams("http://v0715.beilyton.com/fenxiao/user/queryUserCollect");
        TestBean testBean = new TestBean();
        testBean.setUserCode("2017012114215763195");
        testBean.setCurrentPage(1);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(testBean);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CacheCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public boolean onCache(String result) {
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                    LogUtils.log("test result: " + result);
                    System.out.println("test result: " + result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                LogUtils.log("Test Error: ---> " + ex.getMessage());
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    // ...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {

                }
            }
        });
    }

    private class TestBean{

        private String userCode;
        private int currentPage;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }
    }

    @Test
    public void splitTest(){
        String s = "0,44,0,";
        String m[] = s.split(",");
        System.out.println("m.length ---> " + s.substring(0,s.length()-1));

        /*for (String aM : m) {
            System.out.println("m.length ---> " + m.length + "---> " + aM);
        }*/
    }

    @Test
    public void text1(){
        List<Map<Integer,String>> mapList = new ArrayList<>();
        Map<Integer,String> list = new HashMap<>();

       for(int j=0;j<2;j++){
           for (int i=0;i<5;i++){
               list.put(i,i+"");
           }
           mapList.add(list);
       }

        for (int i=0;i<mapList.size();i++){
            for(int j=0;j<list.size();j++){
                System.out.println("---------------> " + i + " --- "  + list.get(j));
            }
        }
    }

}
