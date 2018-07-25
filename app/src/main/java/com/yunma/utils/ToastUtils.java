package com.yunma.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;


/**
 * Created by Json on 2016-11-24.
 */

public class ToastUtils {
    private static boolean isShow = true;
    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showSuccess(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    public static void showWarning(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void showError(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void showInfo(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void showDefault(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void showConfusing(Context context,String text){
        TastyToast.makeText(context, text,
                TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
    }


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void show(Context context, String text, int duration) {
        if (isShow)
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    private static int textview_id;
    private static Toast toast;

    public static void show(Context context, String str) {
        if (toast == null)
            toast = Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_LONG);
        else
            toast.setText(str);
        if (textview_id == 0)
            textview_id = Resources.getSystem().getIdentifier("message", "id", "android");
        ((TextView) toast.getView().findViewById(textview_id)).setGravity(Gravity.CENTER);
        ((TextView) toast.getView().findViewById(textview_id)).setLineSpacing(8,1f);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
