package com.yunma.utils;

/**
 * Created by Json on 2017/3/30.
 */

public class ExpressUtils {

    public static int getExpressCost(String value, int nums){

        if(value.contains("江苏省")||value.contains("上海市")||
                value.contains("浙江省")){
            if(nums<=2){
                return 8;
            }else{
                return 2*nums+4;// 8 + (nums - 2) x 2
            }
        }else if(value.contains("安徽省")){
            if(nums<=2){
                return 8;
            }else{
                return 6*nums-4;// 8 + (nums - 2) x 6
            }
        }else if(value.contains("北京市")||value.contains("山东省")){
            if(nums<=2){
                return 10;
            }else{
                return 8*nums-6;// 10 + (nums - 2) x 8
            }
        }else if(value.contains("河北省")||value.contains("河南省")||value.contains("湖南省")||
                value.contains("湖北省")||value.contains("江西省")||value.contains("天津市")||
                value.contains("广东省")||value.contains("福建省")||value.contains("陕西省")||
                value.contains("山西省")){
            if(nums<=2){
                return 12;
            }else{
                return 8*nums-4;// 12 + (nums - 2) x 8
            }

        }else if(value.contains("四川省")||value.contains("重庆市")||value.contains("贵州省")||
                value.contains("广西省")||value.contains("辽宁省")){
            if(nums<=2){
                return 8;
            }else{
                return 8*nums - 2; // 14 + (nums - 2) x 8
            }
        }else if(value.contains("西藏自治区")||value.contains("新疆维吾尔族自治区")){
            if(nums<=2){
                return 20;
            }else{
                return nums*10; // 20 + (nums - 2) x 10
            }
        }else{
            if(nums<=2){  // 黑龙江、吉林、云南、甘肃、宁夏、青海、海南、内蒙古
                return 14;
            }else{
                return nums*10-6; // 14 + (nums - 2) x 10
            }
        }

    }

}
