package com.yunma.jhuo.general;

import android.os.Bundle;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Json on 10/05/2016.
 */
public class BaseActivity extends EaseBaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        MobclickAgent.onPause(this);
    }

}
