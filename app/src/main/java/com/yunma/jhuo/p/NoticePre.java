package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.NoticeBean;
import com.yunma.jhuo.i.NoticeImpl;
import com.yunma.jhuo.m.GetNoticeInterFace.*;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class NoticePre {
    private NoticeModel mModel = null;
    private NoticeView mView = null;

    public NoticePre(NoticeView mView) {
        this.mView = mView;
        this.mModel = new NoticeImpl();
    }

    public void getNotices(Context mContext){
        mModel.getNotice(mContext, new OnNoticeListener() {
            @Override
            public void onListener(NoticeBean noticeBean, String msg) {
                mView.showNoticeInfo(noticeBean,msg);
            }
        });
    }
}
