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
import com.iflytek.sunflower.FlowerCollector;
import com.mingle.widget.LoadingView;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.Mango.MangoBean;
import com.qtfreet.musicuu.model.Bean.Mango.MangoDetailBean;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.model.OnVideoClickListener;
import com.qtfreet.musicuu.ui.BaseActivity;
import com.qtfreet.musicuu.ui.adapter.MangoDetailAdatper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by qtfreet00 on 2016/9/5.
 */
public class SearchMangoActivity extends BaseActivity implements OnVideoClickListener {
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    @Bind(R.id.lv_search_result)
    RecyclerView recyclerView;
    private MangoDetailAdatper mangoDetaiAdapter;
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
        String KeyWord = getIntent().getStringExtra(Constants.Mango);
        if (KeyWord.isEmpty()) {
            return;
        }
        try {
            requestData(KeyWord);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void requestData(String keyWord) throws UnsupportedEncodingException {
        OkHttpUtils.get().url("http://mobile.api.hunantv.com/v5/search/getResult?name=" + URLEncoder.encode(keyWord, "UTF-8")).build().execute(new StringCallback() {
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

    public void AnalyzeWebResource(int VideoID) {

        OkHttpUtils.get().url("http://mobile.api.hunantv.com/v2/video/getMultiplyList?appVersion=4.7.2.2&videoId=" + VideoID).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                hanlder.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", response);
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = response;
                hanlder.sendMessage(msg);
            }
        });

    }

    private void showToast() {
        Toast.makeText(SearchMangoActivity.this, "未搜索到结果", Toast.LENGTH_SHORT).show();
    }

    private List<MangoDetailBean.DataBean> data = null;

    private Handler hanlder = new Handler(new Handler.Callback() {
        Gson gson = new Gson();

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    loadingView.setVisibility(View.GONE);
                    showToast();
                    break;
                case 0:
                    loadingView.setVisibility(View.GONE);
                    MangoBean mangoBean = gson.fromJson(message.obj.toString(), MangoBean.class);
                    if (mangoBean == null) {
                        showToast();
                        return false;
                    }
                    List<MangoBean.DataBean.HitDocsBean> hitDocs = mangoBean.getData().getHitDocs();
                    if (hitDocs.size() == 0) {
                        showToast();
                        return false;
                    }
                    MangoBean.DataBean.HitDocsBean hitDocsBean = hitDocs.get(0);
                    int VideoId = hitDocsBean.getVideoId();
                    if (VideoId == 0) {
                        showToast();
                        return false;
                    }

                    AnalyzeWebResource(VideoId);
                    break;
                case 2:
                    if (data != null) {
                        data.clear();
                    }
                    MangoDetailBean mangoDetailBean = gson.fromJson(message.obj.toString(), MangoDetailBean.class);
                    if (mangoDetailBean == null) {
                        showToast();
                        return false;
                    }
                    data = mangoDetailBean.getData();
                    if (data.size() == 0) {
                        showToast();
                        return false;
                    }
                    mangoDetaiAdapter = new MangoDetailAdatper(SearchMangoActivity.this, data);
                    mangoDetaiAdapter.setOnVideoClickListener(SearchMangoActivity.this);
                    recyclerView.setAdapter(mangoDetaiAdapter);

                    break;
                case 3:
                    //此处使用Gson和fastjson均会解析失败，故使用原生json
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        JSONObject data = obj.getJSONObject("data");
                        JSONArray stream_domain = data.getJSONArray("stream_domain");
                        String baseUrl = stream_domain.getString(stream_domain.length() - 1);
                        JSONArray stream = data.getJSONArray("stream");
                        String fid;
                        String url = stream.getJSONObject(stream.length() - 1).getString("url");
                        String path = url.substring(url.indexOf("fid="), url.lastIndexOf("%"));
                        if (path.contains("_mp4")) {
                            path = path.replace("_mp4", ".mp4");
                        }
                        String file = path.substring(path.indexOf("file=") + 5, path.lastIndexOf("4"));
                        if (!"/".equals(String.valueOf(file.charAt(0)))) {
                            file = "/" + file + "4";
                        }
                        if (path.contains("&arange=")) {
                            fid = path.substring(path.indexOf("fid="), path.lastIndexOf("&arange="));
                        } else {
                            fid = path.substring(path.indexOf("fid="), path.lastIndexOf("&file"));
                        }
                        path = new StringBuilder(baseUrl).append(file).append("?ver=0.1&fmt=4&pno=1031&").append(fid).toString();
                        Log.i("path", path);
                        Intent i = new Intent(SearchMangoActivity.this, VideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.NAME, "Mango");
                        bundle.putString(Constants.URL, path);
                        i.putExtras(bundle);
                        startActivity(i);

                    } catch (Exception e) {

                    }

                    break;
            }
            return false;
        }
    });

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
    public void click(View v, int postion) {
        Log.e("TAG", data.get(postion).getVideoId() + "  ");
        requestVideoData(data.get(postion).getVideoId());
    }

    private void requestVideoData(int videoId) {
        OkHttpUtils.get().url("http://v.api.mgtv.com/player/video?retry=1&video_id=" + videoId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                hanlder.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = Message.obtain();
                msg.what = 3;
                msg.obj = response;
                hanlder.sendMessage(msg);
            }
        });
    }
}
