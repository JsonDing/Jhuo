package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.PersonInfosMoifyImpl;
import com.yunma.jhuo.m.PersonInfosMoifyInterface.*;

/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class PersonInfosMoifyPre {
    private PersonInfosMoifyModel mModel;
    private PersonInfosMoifyView mView;

    public PersonInfosMoifyPre(PersonInfosMoifyView mView) {
        this.mView = mView;
        this.mModel = new PersonInfosMoifyImpl();
    }

    public void modifyPersonalInfos(Context context,String nickName,
                                    String gender,String realName, String qq){
        mModel.PersonInfosMoify(context, nickName,gender,
                realName,qq, new OnPersonInfosMoify() {
                    @Override
                    public void onMoifyListenter(SuccessResultBean successResultBean, String msg) {
                        mView.showPersonInfosMoifyResult(successResultBean,msg);
                    }
                });
    }
}
