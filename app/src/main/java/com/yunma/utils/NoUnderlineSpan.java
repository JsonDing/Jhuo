package com.yunma.utils;

import android.annotation.SuppressLint;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by Json on 2017/1/17.
 */

@SuppressLint("ParcelCreator")
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
