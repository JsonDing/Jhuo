package com.yunma;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void splitTest(){
        List<String> mList = new ArrayList<>();
        mList.add("逼1");
        mList.add("我");
        mList.add("去");
        mList.add("逼2");
        mList.add("你");
        mList.add("妈");
        mList.add("逼3");
        mList.add("的");
        System.out.println("mList 1: " + mList.toString());
        List<String> nList = new ArrayList<>();
        nList.add("逼1");
        nList.add("逼2");
        nList.add("逼3");
        mList.removeAll(nList);
        System.out.println("mList 2: " + mList.toString());
        mList.addAll(nList);
        System.out.println("mList 3: " + mList.toString());
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

    @Test
    public void text2(){
        List<Integer> mList = new ArrayList<>();
        mList.add(1);
        mList.add(2);
        mList.add(3);
        mList.add(4);
        mList.add(5);
        mList.add(6);
        mList.add(7);
        mList.add(8);
        mList.add(9);
        mList.add(10);
        if(mList.size()%4!=0){
            int temp = mList.size()%4;
            mList = mList.subList(0,mList.size()-temp);
        }
        System.out.println("---------------------> " + mList.toString());
        List<List<Integer>> lists=averageAssign(mList,2);
        System.out.println(lists);
    }

    public static <T> List<List<T>> averageAssign(List<T> source,int n){
        List<List<T>> result=new ArrayList<>();
        int remaider = source.size()%n;  //(先计算出余数)
        int number = source.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }


}
