package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.NoticeBean;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class GetNoticeInterFace {
    public interface NoticeModel{
        void getNotice(Context mContext,OnNoticeListener onNoticeListener);
    }

    public interface NoticeView{
        void showNoticeInfo(NoticeBean noticeBean,String msg);
    }

    public interface OnNoticeListener{
        void onListener(NoticeBean noticeBean,String msg);
    }
}
