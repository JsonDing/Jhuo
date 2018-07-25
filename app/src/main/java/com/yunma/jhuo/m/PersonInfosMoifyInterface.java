package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;

/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class PersonInfosMoifyInterface {

    public interface PersonInfosMoifyModel{
        void PersonInfosMoify(Context mContext, String nickName, String gender, String realName,
                              String qq,OnPersonInfosMoify onPersonInfosMoify);
    }

    public interface PersonInfosMoifyView{
        void showPersonInfosMoifyResult(SuccessResultBean successResultBean,String msg);
    }

    public interface OnPersonInfosMoify{
        void onMoifyListenter(SuccessResultBean successResultBean,String msg);
    }
}
