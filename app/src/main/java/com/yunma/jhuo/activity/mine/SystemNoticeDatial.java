package com.yunma.jhuo.activity.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.SystemNotices;
import com.yunma.greendao.SystemNoticesDao;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemNoticeDatial extends MyCompatActivity {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvNoticeTittle) TextView tvNoticeTittle;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice_datial);
        ButterKnife.bind(this);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        AppManager.getAppManager().addActivity(this);
        iniUI();
        getDatas();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void iniUI() {
        webView = this.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 在APP内部打开链接，不要调用系统浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void getDatas() {
        int position = Integer.valueOf(getIntent().getStringExtra("position"));
        List<SystemNotices> datasList = getSystemNoticesDao().loadAll();
        SystemNotices systemNotices = new SystemNotices();
        systemNotices.setId(datasList.get(position).getId());
        systemNotices.setTittle(datasList.get(position).getTittle());
        systemNotices.setContent(datasList.get(position).getContent());
        systemNotices.setTime(datasList.get(position).getTime());
        systemNotices.setPublisher(datasList.get(position).getPublisher());
        systemNotices.setIsRead("yes");
        getSystemNoticesDao().save(systemNotices);
        tvNoticeTittle.setText(Html.fromHtml(datasList.get(position).getTittle()));
        String sb = "<html>" +
                "<meta charset='UTF-8'>" +
                "<body style='height:100%'>" +
                datasList.get(position).getContent() +
                "</body>" +
                "</html>";
        Document doc_Dis = Jsoup.parse(sb);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0){
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "width:100%;height:auto");
            }
        }
        Elements ele_text = doc_Dis.getElementsByTag("p");
        if (ele_text.size() != 0){
            for (Element text : ele_text) {
                text.attr("style", "font-size:44px");//;text-align:center
            }
        }
        Elements ele_text1 = doc_Dis.getElementsByTag("font");
        if (ele_text1.size() != 0){
            for (Element text : ele_text1) {
                text.attr("style", "font-size:40px;");
            }
        }
        String newHtmlContent = doc_Dis.toString();

        LogUtils.json("newHtmlContent：" + newHtmlContent);
        webView.loadDataWithBaseURL(null,newHtmlContent,"text/html", "utf-8",null);
    }


    @OnClick(R.id.layoutBack)
    public void onClick(View view) {
        AppManager.getAppManager().finishActivity(this);
    }

    private SystemNoticesDao getSystemNoticesDao() {
        return GreenDaoManager.getInstance().getSession().getSystemNoticesDao();
    }

}
