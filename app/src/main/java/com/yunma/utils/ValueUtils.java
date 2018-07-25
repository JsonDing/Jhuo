package com.yunma.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */

public class ValueUtils {

    //缩进一格
    public static final String ONE_SPACE = "\u3000";
    //
    public static final String TWO_SPACE = "\u3000\u3000";

    public static String toOneDecimal(double value){
        BigDecimal b = new BigDecimal(value);
        double d = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.format(Locale.CHINA,"%.1f",d);
    }

    public static String toTwoDecimal(double value){
        BigDecimal b = new BigDecimal(value);
        double d = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.format(Locale.CHINA,"%.2f",d);
    }

    public static String toThreeDecimal(double value){
        BigDecimal b = new BigDecimal(value);
        double d = b.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.format(Locale.CHINA,"%.3f",d);
    }

    public static SpannableStringBuilder changeText(Context mContext, int color, String value, int textStyle,
                                                    int textSize, int startIndex, int endIndex){
        ColorStateList mColor = ColorStateList.valueOf(
                ContextCompat.getColor(mContext,color));
        SpannableStringBuilder spanBuilder
                = new SpannableStringBuilder(value);
        //style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
        //size  为0 即采用原始的正常的 size大小
        spanBuilder.setSpan(new TextAppearanceSpan(null, textStyle,
                        DensityUtils.sp2px(mContext,textSize), mColor, null)
                , startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spanBuilder;
    }

    public static SpannableStringBuilder changeTextSize(Context mContext,String value,
                                                    int textSize, int startIndex, int endIndex){
        SpannableStringBuilder spanBuilder
                = new SpannableStringBuilder(value);
        spanBuilder.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(mContext,textSize)),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanBuilder;
    }

    public static String TwoSpaceText(String text){
        return TWO_SPACE + text;
    }

    public static String hideNumber(String number){
        if (number != null) {
            if (number.length() == 11) {
                return number.substring(0,3) + "****" + number.substring(7,number.length());
            }
        }
       return "手机号码异常";
    }

    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public static String removeSpace(String value){
        return value.replaceAll(" ","");
    }


    public static SpannableStringBuilder getNewPrice(Context mContext,double value,
                                     int textSize){
        String s = toTwoDecimal(value);
        SpannableStringBuilder spanBuilder
                = new SpannableStringBuilder(s);
        spanBuilder.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(mContext,textSize)),
                0, s.indexOf("."), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanBuilder;
    }

    public static boolean equals(String value, String eq) {
        return value != null && value.equals(eq);
    }
}
