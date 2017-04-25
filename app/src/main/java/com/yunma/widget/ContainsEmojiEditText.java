package com.yunma.widget;

import android.content.Context;
import android.text.*;
import android.util.AttributeSet;

import com.yunma.utils.ToastUtils;

import java.util.regex.*;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class ContainsEmojiEditText extends android.support.v7.widget.AppCompatEditText {
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private Context mContext;
    private final String reg ="^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?" +
            "\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|" +
            "[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
    private Pattern pattern = Pattern.compile(reg);
    public ContainsEmojiEditText(Context context) {
        super(context);
        this.mContext = context;
        initEditText();
    }

    public ContainsEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initEditText();
    }

    public ContainsEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    cursorPos = getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = s.toString();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {

                    if (count-before >= 2) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(start+before, start + count);
                        if (containsEmoji(input.toString())) {
                            resetText = true;
                            ToastUtils.showError(mContext, "不支持输入表情符号");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            setText(inputAfterText);
                            CharSequence text = getText();
                            if (text != null) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                            return;
                        }else if(count-before >= 3){
                            //正则匹配是否是表情符号
                            Matcher matcher = pattern.matcher(input.toString());
                            if(!matcher.matches()){
                                resetText = true;
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                setText(inputAfterText);
                                ToastUtils.showError(mContext, "不支持输入表情符号");
                                CharSequence text = getText();
                                if (text != null) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                                return;
                            }
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

}