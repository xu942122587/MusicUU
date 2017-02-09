package com.qtfreet.musicuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.iflytek.sunflower.FlowerCollector;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.YinyueTai.MvBean;
import com.qtfreet.musicuu.model.Bean.YinyueTai.MvPlayBean;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.model.OnVideoClickListener;
import com.qtfreet.musicuu.ui.BaseActivity;
import com.qtfreet.musicuu.ui.adapter.MvDetailAdatper;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qtfreet00 on 2016/9/1.
 */
public class SearchMvActivity extends BaseActivity implements OnVideoClickListener {
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    @Bind(R.id.lv_search_result)
    RecyclerView recyclerView;
    private MvDetailAdatper mAdapter;
    private List<MvBean.DataBean> dataBean;
    private String KeyWord;
    private BuildBean buildBean = null;

    @Override
    protected void onResume() {
        super.onResume();
        FlowerCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FlowerCollector.onPause(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_mv);
        setupWindowAnimations();
        ButterKnife.bind(this);
        setTitleName("搜索", true);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        initData();
    }

    private void initData() {
        buildBean = DialogUIUtils.showMdLoading(this, "加载中...", true, true, false, true);
        KeyWord = getIntent().getStringExtra(Constants.YinyueTai);
        if (KeyWord.isEmpty()) {
            return;
        }
        requestData(KeyWord);
    }


    public void requestData(String keyWord) {
        RequestQueue stringRequest = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest("http://mapiv2.yinyuetai.com/search/video.json?&order=&sourceVersion=&area=&singerType=&offset=0&size=20&keyword=" + keyWord);
        request.headers().add("App-Id", "10201041");
        request.headers().add("Device-Id", "178bc560c9e8d719e048c7e8f2d25fcb");
        request.headers().add("Device-V", "QW5kcm9pZF80LjQuMl83NjgqMTE4NF8xMDAwMDEwMDA=");
        stringRequest.add(0, request, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = response.get();
                hanlder.sendMessage(msg);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }
        });

    }

    private Handler hanlder = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    DialogUIUtils.dismiss(buildBean);
                    dataBean = JSON.parseObject(message.obj.toString(), MvBean.class).getData();
                    if (dataBean == null) {
                        Toast.makeText(SearchMvActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    mAdapter = new MvDetailAdatper(SearchMvActivity.this, dataBean);
                    mAdapter.setOnVideoClickListener(SearchMvActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    break;
                case 1:
                    DialogUIUtils.dismiss(buildBean);
                    Toast.makeText(SearchMvActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    MvPlayBean mv = JSON.parseObject(message.getData().getString("response"), MvPlayBean.class);
                    String mvUrl;
                    String ShdUrl = mv.getData().getShdUrl();
                    String uhdUrl = mv.getData().getUhdUrl();
                    String hdUrl = mv.getData().getHdUrl();
                    String mdUrl = mv.getData().getUrl();
                    if (!TextUtils.isEmpty(ShdUrl)) {
                        mvUrl = ShdUrl;
                    } else if (!TextUtils.isEmpty(uhdUrl)) {
                        mvUrl = uhdUrl;
                    } else if (!TextUtils.isEmpty(hdUrl)) {
                        mvUrl = hdUrl;
                    } else if (!TextUtils.isEmpty(mdUrl)) {
                        mvUrl = mdUrl;
                    } else {
                        Toast.makeText(SearchMvActivity.this, "获取播放链接失败！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    String songName = message.getData().getString("name");
                    Intent i = new Intent(SearchMvActivity.this, VideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.NAME, songName);
                    bundle.putString(Constants.URL, mvUrl);
                    i.putExtras(bundle);
                    startActivity(i);

                    break;
            }

            return false;
        }
    });


    @Override
    public void click(View v, int postion) {
        int id = dataBean.get(postion).getVideoId();
        String videoName = dataBean.get(postion).getTitle();
        requestVideo(id, videoName);
    }

    private void requestVideo(int id, final String videoName) {

        RequestQueue stringRequest = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest("http://mapiv2.yinyuetai.com/video/play.json?&id=" + id + "&type=1");
        request.headers().add("App-Id", "10201041");
        request.headers().add("Device-Id", "178bc560c9e8d719e048c7e8f2d25fcb");
        request.headers().add("Device-V", "QW5kcm9pZF80LjQuMl83NjgqMTE4NF8xMDAwMDEwMDA=");
        stringRequest.add(0, request, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                Message msg = Message.obtain();
                msg.what = 2;
                Bundle b = new Bundle();
                b.putString("response", response.get());
                b.putString("name", videoName);
                msg.setData(b);
                hanlder.sendMessage(msg);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }
        });

    }
}
