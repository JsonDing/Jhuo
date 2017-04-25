package com.yunma.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 2017-01-19
 *
 * @author Json.
 */

public class TimeUtils {

    private int time;
    private Timer timer;
    private TimerTask task;
    private TextView view;
    private String text;
    private Context mContext;
    private int drawable;
    public TimeUtils(Context mContext,TextView view, String text, int time,int drawable) {
        super();
        this.view = view;
        this.time = time;
        this.text = text;
        this.drawable = drawable;
        this.mContext = mContext;
    }

    public void RunTimer(){
        timer=new Timer();
        task=new TimerTask() {

            @Override
            public void run(){

                time--;
                Message msg = handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        };

        timer.schedule(task, 100, 1000);
    }

    public void destroyHandler(){
        timer.cancel();
        task.cancel();
        handler.removeCallbacksAndMessages(null);
    }

    private Handler handler =new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if(time>0){
                        view.setEnabled(false);
                        view.setText(time + "s");
                        if(drawable!=-1){
                            view.setBackground(mContext.getResources().getDrawable(drawable));
                        }
                    }else{
                        timer.cancel();
                        view.setText(text);
                        view.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }

        }
    };


}
