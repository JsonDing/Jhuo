package com.yunma.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.yunma.R;
import com.yunma.adapter.UploadListAdapter;
import com.yunma.bean.PublishGoodsBean;
import com.yunma.bean.YunmasBeanList;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.LogUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.widget.MySpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;

import static org.xutils.common.util.DensityUtil.dip2px;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class UploadList extends MyCompatActivity implements UploadListAdapter.OnAddGoodClick {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.activity_upload_list)
    LinearLayout activityUploadList;
    private GridLayoutManager gridLayoutManager;
    private UploadListAdapter mAdapter;
    private List<YunmasBeanList> yunmasBeanList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        ButterKnife.bind(this);
        int statusHeight = ScreenUtils.getStatusHeight(UploadList.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        initRecyclerview();
    }

    private void initRecyclerview() {
        recyclerview.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        yunmasBeanList = new ArrayList<>();
        mAdapter = new UploadListAdapter(this,yunmasBeanList,UploadList.this);
        recyclerview.setAdapter(mAdapter);

    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        finish();
    }

    @Override
    public void toAddGoodInfos() {
        Intent intent = new Intent(UploadList.this,ToAddGoods.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            YunmasBeanList yunmasBean = (YunmasBeanList) data.getSerializableExtra("yunmasBean");
            yunmasBeanList.add(yunmasBean);
            mAdapter.notifyDataSetChanged();
            PublishGoodsBean publishGoodsBean = new PublishGoodsBean();
            publishGoodsBean.setList("1");
            publishGoodsBean.setToken("123456789");
            publishGoodsBean.setYunmas(yunmasBeanList);
            LogUtils.log("---> " + new Gson().toJson(publishGoodsBean));

        }
    }
}
