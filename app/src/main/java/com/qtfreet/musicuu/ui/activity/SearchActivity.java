package com.qtfreet.musicuu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.ApiService;
import com.qtfreet.musicuu.model.Bean.resultBean;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.ui.OnMusicClickListener;
import com.qtfreet.musicuu.ui.adapter.SongDetailAdapter;
import com.qtfreet.musicuu.ui.service.DownloadService;
import com.qtfreet.musicuu.utils.NetUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qtfreet on 2016/3/20.
 */
public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnMusicClickListener, PopupMenu.OnMenuItemClickListener {

    private SongDetailAdapter searchResultAdapter;

    @Bind(R.id.lv_search_result)
    RecyclerView search_list;
    private static final int REQUECT_CODE_SDCARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MPermissions.requestPermissions(SearchActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        initView();
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {

    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        Toast.makeText(this, "未获取到SD卡权限!", Toast.LENGTH_SHORT).show();

    }

    private void initData() {
        showRefreshing(true);
        ApiService apiService = NetUtil.getInstance().create(ApiService.class);
        Call<List<resultBean>> call = apiService.GetInfo(getIntent().getExtras().getString(Constants.TYPE), getIntent().getExtras().getString(Constants.KEY));
        call.enqueue(new Callback<List<resultBean>>() {
            @Override
            public void onResponse(Call<List<resultBean>> call, Response<List<resultBean>> response) {
                showRefreshing(false);
                if (response.body().size() == 0) {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                    return;
                }
                result = response.body();
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(Call<List<resultBean>> call, Throwable t) {
                showRefreshing(false);

                handler.sendEmptyMessage(REQUEST_ERROR);
            }
        });
    }

    private SwipeRefreshLayout refresh;
    private List<resultBean> result = new ArrayList<>();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;

    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    if (result == null) {
                        handler.sendEmptyMessage(REQUEST_ERROR);
                        return true;
                    }
                    searchResultAdapter = new SongDetailAdapter(SearchActivity.this, result);
                    searchResultAdapter.setOnMusicClickListener(SearchActivity.this);
                    search_list.setAdapter(searchResultAdapter);
                    break;
                case REQUEST_ERROR:
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

    private int postion1;

    public void popupMenuClick(View v, int postion) {
        postion1 = postion;
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.main_popup_menu);
        popupMenu.show();
    }

    private void initView() {
        ButterKnife.bind(this);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbarTitle.setText("搜索");
            }
        }

        search_list.setLayoutManager(new LinearLayoutManager(this));
        search_list.setHasFixedSize(true);

    }


    private void Download(int position) {
        final String SongName = result.get(position).getSongName();
        final String SongID = result.get(position).getSongId();
        final String Artist = result.get(position).getArtist();
        String SqUrl = result.get(position).getSqUrl();
        String HqUrl = result.get(position).getHqUrl();
        String LqUrl = result.get(position).getLqUrl();
        String MusicUrl = "";
        if (!SqUrl.equals("")) {
            MusicUrl = SqUrl;
        } else if (!HqUrl.equals("")) {
            MusicUrl = HqUrl;
        } else if (!LqUrl.equals("")) {
            MusicUrl = LqUrl;
        }
        download(SongName + "-" + Artist + "-L", MusicUrl, SongID);
    }


    private static final int REQUEST_SUCCESS = 1;
    private static final int REQUEST_ERROR = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void download(String name, String url, String id) {
        Intent i = new Intent(this, DownloadService.class);
        Bundle b = new Bundle();
        b.putString(Constants.URL, url);
        b.putString(Constants.NAME, name);
        b.putString(Constants.SONG_ID, id);
        i.putExtras(b);
        startService(i);
    }

    @Override
    public void onRefresh() {
        initData();
    }


    @Override
    public void Music(View itemView, int position) {
        int size = result.size();
        String[] songs = new String[size];
        for (int i = 0; i < size; i++) {
            songs[i] = result.get(i).getListenUrl();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                Download(postion1);
                return true;
            case R.id.mv:
                String mvUrl = result.get(postion1).getMvUrl();
                if (mvUrl.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "无MV信息", Toast.LENGTH_SHORT).show();
                    return false;
                }
                String songName = result.get(postion1).getSongName() + "--" + result.get(postion1).getArtist();
                Intent i = new Intent(SearchActivity.this, VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NAME, songName);
                bundle.putString(Constants.URL, mvUrl);
                i.putExtras(bundle);
                startActivity(i);
                return true;


        }
        return false;
    }
}
