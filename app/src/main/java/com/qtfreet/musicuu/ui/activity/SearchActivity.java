package com.qtfreet.musicuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iflytek.sunflower.FlowerCollector;
import com.mingle.widget.LoadingView;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.ApiService;
import com.qtfreet.musicuu.model.Bean.MusicUU.resultBean;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.model.OnMusicClickListener;
import com.qtfreet.musicuu.ui.BaseActivity;
import com.qtfreet.musicuu.ui.adapter.SongDetailAdapter;
import com.qtfreet.musicuu.ui.service.DownloadService;
import com.qtfreet.musicuu.utils.NetUtil;
import com.qtfreet.musicuu.utils.SPUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qtfreet on 2016/3/20.
 */
public class SearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnMusicClickListener, OnSwipeMenuItemClickListener {

    private SongDetailAdapter searchResultAdapter;

    @Bind(R.id.lv_search_result)
    SwipeMenuRecyclerView search_list;
    private SwipeRefreshLayout refresh;
    private List<resultBean> result = new ArrayList<>();
    @Bind(R.id.loadView)
    LoadingView loadingView;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        setupWindowAnimations();
        ButterKnife.bind(this);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(this);
        setTitleName("搜索", true);
        search_list.setLayoutManager(new LinearLayoutManager(this));
        search_list.setHasFixedSize(true);
        //search_list.setItemViewSwipeEnabled(true);// 开启滑动删除。
        search_list.setSwipeMenuCreator(swipeMenuCreator);
        search_list.setSwipeMenuItemClickListener(this);
        initData();
        firstUse();
    }

    private void firstUse() {
        boolean isFirst = (boolean) SPUtils.get(this, Constants.IS_FIRST_SEARCH, true);
        if (isFirst) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
            sweetAlertDialog.setTitleText(getString(R.string.start_info));
            sweetAlertDialog.setContentText(getString(R.string.description_search)).setConfirmText(getString(R.string.i_know));
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    SPUtils.put(SearchActivity.this, Constants.IS_FIRST_SEARCH, false);
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            sweetAlertDialog.show();
        }
    }


    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_width);
            SwipeMenuItem downMuic = new SwipeMenuItem(SearchActivity.this)
                    .setBackgroundDrawable(R.color.swipe_one)
                    .setText("下载")
                    .setWidth(width) // 宽度。
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT); // 高度。
            swipeRightMenu.addMenuItem(downMuic); // 添加一个按钮到左侧菜单。

            SwipeMenuItem watchMV = new SwipeMenuItem(SearchActivity.this)
                    .setBackgroundDrawable(R.color.swipe_two)
                    .setText("MV")
                    .setWidth(width)
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(watchMV);// 添加一个按钮到右侧侧菜单。.

            // 上面的菜单哪边不要菜单就不要添加。
        }
    };

    private void initData() {
        if (loadingView.getVisibility() == View.GONE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        ApiService apiService = NetUtil.getInstance().create(ApiService.class);
        Call<List<resultBean>> call = apiService.GetInfo(getIntent().getExtras().getString(Constants.TYPE), getIntent().getExtras().getString(Constants.KEY));
        call.enqueue(new Callback<List<resultBean>>() {
            @Override
            public void onResponse(Call<List<resultBean>> call, Response<List<resultBean>> response) {
                try {
                    if (response == null) {
                        handler.sendEmptyMessage(REQUEST_ERROR);
                        return;
                    }
                    if (response.body().size() == 0) {
                        handler.sendEmptyMessage(REQUEST_ERROR);
                        return;
                    }
                    result = response.body();
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                }


            }

            @Override
            public void onFailure(Call<List<resultBean>> call, Throwable t) {
                handler.sendEmptyMessage(REQUEST_ERROR);
            }
        });
    }


    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    loadingView.setVisibility(View.GONE);
                    if (result == null) {
                        handler.sendEmptyMessage(REQUEST_ERROR);
                        return true;
                    }
                    searchResultAdapter = new SongDetailAdapter(SearchActivity.this, result);
                    searchResultAdapter.setOnMusicClickListener(SearchActivity.this);
                    search_list.setAdapter(searchResultAdapter);
                    break;
                case REQUEST_ERROR:
                    loadingView.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private void showRefreshing(boolean isShow) {
        if (isShow) {
            refresh.setProgressViewOffset(false, 0, (int) (getResources().getDisplayMetrics().density * 24 +
                    0.5f));
            refresh.setRefreshing(true);
        } else {
            refresh.setRefreshing(false);
        }
    }

    private void Download(int position) {
        final String SongName = result.get(position).getSongName();
        final String SongID = result.get(position).getSongId();
        final String Artist = result.get(position).getArtist();
        final String SqUrl = result.get(position).getSqUrl();
        final String HqUrl = result.get(position).getHqUrl();
        final String LqUrl = result.get(position).getLqUrl();
        final String flacUrl = result.get(position).getFlacUrl();
        final String aacUrl = result.get(position).getAacUrl();
        List<String> arrayList = new ArrayList();
        arrayList.add("标准");
        arrayList.add("高");
        arrayList.add("极高");
        arrayList.add("无损");
        String[] urls = arrayList.toArray(new String[arrayList.size()]);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setItems(urls, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = SongName + "-" + Artist;
                String url = "";
                if (i == 0) {
                    name += "-L";
                    if (!LqUrl.isEmpty()) {
                        url = LqUrl;
                    } else {
                        showError();
                        return;
                    }
                } else if (i == 1) {
                    name += "-H";
                    if (!HqUrl.isEmpty()) {
                        url = HqUrl;
                    } else {
                        showError();
                        return;
                    }

                } else if (i == 2) {
                    if (!SqUrl.isEmpty()) {
                        url = SqUrl;
                    } else {
                        showError();
                        return;
                    }
                    name += "-S";

                } else if (i == 3) {
                    name += "-F";
                    if (!flacUrl.isEmpty()) {
                        url = flacUrl;
                    } else if (!aacUrl.isEmpty()) {
                        url = aacUrl;
                    } else {
                        showError();
                        return;
                    }
                }
                download(name, url, SongID);
            }
        });
        dialog.show();
    }

    private void showError() {
        Toast.makeText(this, "该音乐没有此音质链接~", Toast.LENGTH_SHORT).show();
    }


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

    private static final int REQUEST_SUCCESS = 1;
    private static final int REQUEST_ERROR = 0;

    private void download(String name, String url, String id) {
        Intent i = new Intent(this, DownloadService.class);
        Bundle b = new Bundle();
        if (url.isEmpty() || name.isEmpty() || id.isEmpty() || name == null || url == null || id == null) {
            Toast.makeText(this, "获取下载链接失败，请重新尝试", Toast.LENGTH_SHORT).show();
            return;
        }
        b.putString(Constants.URL, url);
        b.putString(Constants.NAME, name);
        b.putString(Constants.SONG_ID, id);
        i.putExtras(b);
        startService(i);
    }

    @Override
    public void onRefresh() {
        showRefreshing(false);
        initData();
    }


    @Override
    public void Music(View itemView, int position) {
        int size = result.size();
        String[] songs = new String[size];
        for (int i = 0; i < size; i++) {
            if (!result.get(i).getLqUrl().isEmpty()) {
                songs[i] = result.get(i).getLqUrl();  //考虑性能问题，默认播放低音质
            } else {
                songs[i] = result.get(i).getHqUrl();
            }
        }
        String[] lrcs = new String[size];
        for (int i = 0; i < size; i++) {
            lrcs[i] = result.get(i).getLrcUrl();
        }
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = result.get(i).getSongName() + "--" + result.get(i).getArtist();
        }
        Intent i = new Intent(SearchActivity.this, PlayMusicActivity.class);
        Bundle b = new Bundle();
        b.putStringArray(Constants.PLAY_URLS, songs);
        b.putStringArray(Constants.PLAY_LRCS, lrcs);
        b.putStringArray(Constants.PLAY_NAMES, names);
        b.putInt(Constants.POSITION, position);
        i.putExtras(b);
        startActivity(i);
    }


    public void downLoadMusic(int position) {
        Download(position);
    }


    public void playMV(int position) {
        String mvUrl = result.get(position).getMvUrl();
        if (mvUrl.isEmpty()) {
            Toast.makeText(SearchActivity.this, "无MV信息", Toast.LENGTH_SHORT).show();
            return;
        }
        String songName = result.get(position).getSongName() + "--" + result.get(position).getArtist();
        Intent i = new Intent(SearchActivity.this, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NAME, songName);
        bundle.putString(Constants.URL, mvUrl);
        i.putExtras(bundle);
        startActivity(i);
    }


    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            if (menuPosition == 0) {
                downLoadMusic(adapterPosition);
            } else if (menuPosition == 1) {
                playMV(adapterPosition);
            }
            closeable.smoothCloseRightMenu();

        }
    }
}
