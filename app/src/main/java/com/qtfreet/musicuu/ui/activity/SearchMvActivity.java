package com.qtfreet.musicuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
public class SearchMvActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnVideoClickListener {
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.lv_search_result)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;
    private MvDetailAdatper mAdapter;
    private List<MvBean.DataBean> dataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initData() {
        String keyWord = getIntent().getStringExtra(Constants.YinyueTai);
        if (keyWord.isEmpty()) {
            return;
        }
        requestData(keyWord);
    }

    private void initView() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbarTitle.setText("搜索");
            }
        }
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R
                .color.holo_orange_light, android.R.color.holo_green_light);
        refresh.setOnRefreshListener(this);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
                    positions = mStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    for (int position : positions) {
                        if (position == mStaggeredGridLayoutManager.getItemCount() - 1) {
                            Toast.makeText(SearchMvActivity.this, "木有更多了哦", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showRefreshing(boolean isShow) {
        if (isShow) {
            refresh.setProgressViewOffset(false, 0, (int) (getResources().getDisplayMetrics().density * 24 +
                    0.5f));
            refresh.setRefreshing(true);
        } else {
            refresh.setRefreshing(false);
        }
    }

    public void requestData(String keyWord) {
        showRefreshing(true);
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
                    showRefreshing(false);
                    Gson gson = new Gson();
                    dataBean = gson.fromJson(message.obj.toString(), MvBean.class).getData();
                    mAdapter = new MvDetailAdatper(SearchMvActivity.this, dataBean);
                    mAdapter.setOnVideoClickListener(SearchMvActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    break;
                case 1:
                    Toast.makeText(SearchMvActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    showRefreshing(false);
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
    public void onRefresh() {
        requestData(getIntent().getStringExtra(Constants.YinyueTai));
    }

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
