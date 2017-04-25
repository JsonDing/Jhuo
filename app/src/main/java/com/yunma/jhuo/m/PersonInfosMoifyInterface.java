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
                              OnPersonInfosMoify onPersonInfosMoify);
    }

    public interface PersonInfosMoifyView{
        Context getContext();
        String getNickname();
        String getGender();
        String getRealName();
        void showPersonInfosMoifyResult(SuccessResultBean successResultBean,String msg);
    }

    public interface OnPersonInfosMoify{
        void onMoifyListenter(SuccessResultBean successResultBean,String msg);
    }
}
