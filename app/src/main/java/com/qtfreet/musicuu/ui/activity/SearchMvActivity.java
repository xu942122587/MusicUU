package com.qtfreet.musicuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mingle.widget.LoadingView;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.YinyueTai.MvBean;
import com.qtfreet.musicuu.model.Bean.YinyueTai.MvPlayBean;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.model.OnVideoClickListener;
import com.qtfreet.musicuu.ui.BaseActivity;
import com.qtfreet.musicuu.ui.adapter.MvDetailAdatper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

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
    @Bind(R.id.loadView)
    LoadingView loadingView;


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
        KeyWord = getIntent().getStringExtra(Constants.YinyueTai);
        if (KeyWord.isEmpty()) {
            return;
        }
        requestData(KeyWord);
    }


    public void requestData(String keyWord) {
        OkHttpUtils.get().url("http://mapiv2.yinyuetai.com/search/video.json?&order=&sourceVersion=&area=&singerType=&offset=0&size=20&keyword=" + keyWord).addHeader("App-Id", "10201041").addHeader("Device-Id", "178bc560c9e8d719e048c7e8f2d25fcb").addHeader("Device-V", "QW5kcm9pZF80LjQuMl83NjgqMTE4NF8xMDAwMDEwMDA=").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                hanlder.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", response);
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = response;
                hanlder.sendMessage(msg);

            }
        });
    }

    private Handler hanlder = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    loadingView.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    dataBean = gson.fromJson(message.obj.toString(), MvBean.class).getData();
                    if (dataBean == null) {
                        Toast.makeText(SearchMvActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    mAdapter = new MvDetailAdatper(SearchMvActivity.this, dataBean);
                    mAdapter.setOnVideoClickListener(SearchMvActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    break;
                case 1:
                    loadingView.setVisibility(View.GONE);
                    Toast.makeText(SearchMvActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Gson gson2 = new Gson();
                    MvPlayBean mv = gson2.fromJson(message.getData().getString("response"), MvPlayBean.class);
                    String mvUrl;
                    String ShdUrl = mv.getData().getShdUrl();
                    String uhdUrl = mv.getData().getUhdUrl();
                    String hdUrl = mv.getData().getHdUrl();
                    String mdUrl = mv.getData().getUrl();
                    if (ShdUrl != null && !ShdUrl.isEmpty()) {
                        mvUrl = ShdUrl;
                    } else if (uhdUrl != null && !uhdUrl.isEmpty()) {
                        mvUrl = uhdUrl;
                    } else if (hdUrl != null && !hdUrl.isEmpty()) {
                        mvUrl = hdUrl;
                    } else if (mdUrl != null && !mdUrl.isEmpty()) {
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
        OkHttpUtils.get().url("http://mapiv2.yinyuetai.com/video/play.json?&id=" + id + "&type=1").addHeader("App-Id", "10201041").addHeader("Device-Id", "178bc560c9e8d719e048c7e8f2d25fcb").addHeader("Device-V", "QW5kcm9pZF80LjQuMl83NjgqMTE4NF8xMDAwMDEwMDA=").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                hanlder.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", response);
                Message msg = Message.obtain();
                msg.what = 2;
                Bundle b = new Bundle();
                b.putString("response", response);
                b.putString("name", videoName);
                msg.setData(b);
                hanlder.sendMessage(msg);

            }
        });
    }
}
