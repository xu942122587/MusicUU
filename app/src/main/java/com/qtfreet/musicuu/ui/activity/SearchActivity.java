package com.qtfreet.musicuu.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.APi;
import com.qtfreet.musicuu.model.ApiService;
import com.qtfreet.musicuu.model.DownListener;
import com.qtfreet.musicuu.model.MusicBean;
import com.qtfreet.musicuu.model.resultBean;
import com.qtfreet.musicuu.ui.OnMusicClickListener;
import com.qtfreet.musicuu.ui.adapter.SongDetailAdapter;
import com.qtfreet.musicuu.utils.DownloadUtil;
import com.qtfreet.musicuu.utils.SPUtils;
import com.qtfreet.musicuu.wiget.ActionSheetDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.uiview.UIButton;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qtfreet on 2016/3/20.
 */
public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, OnMusicClickListener, PopupMenu.OnMenuItemClickListener {

    private SongDetailAdapter searchResultAdapter;

    @Bind(R.id.lv_search_result)
    RecyclerView search_list;
    private static final int REQUECT_CODE_SDCARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MPermissions.requestPermissions(SearchActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        initview();
        initData();
        firstuse();
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
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APi.MUSIC_HOST).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<resultBean>> call = apiService.GetInfo(getIntent().getExtras().getString("type"), getIntent().getExtras().getString("key"));
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
    public void popupMenuClick(View v,int postion) {
        postion1  = postion;
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.main_popup_menu);
        popupMenu.show();
    }

    private void initview() {
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
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

    private void firstuse() {
        boolean isfirst = (boolean) SPUtils.get(this, "isdownload", true);
        if (isfirst) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");

            builder.setMessage("当前版本的下载功能还不完善，所以麻烦大家在下载期间请勿随意切换页面。");
            builder.setCancelable(false);
            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPUtils.put(SearchActivity.this, "isdownload", false);
                }
            });
            builder.show();
        }
    }

    private void Download(int position) {
         final String SongId =  result.get(position).getSongId();
         final String SongName  = result.get(position).getSongName();
         final String Artist = result.get(position).getArtist();
         String SqUrl = result.get(position).getSqUrl();
         String HqUrl =  result.get(position).getHqUrl();
         String LqUrl =  result.get(position).getLqUrl();
         final String VideoUrl = result.get(position).getVideoUrl();
         String MusicUrl="";
        if(!SqUrl.equals("")){
            MusicUrl =SqUrl;
        }else if(!HqUrl.equals("")){
           MusicUrl=HqUrl;
        }else if(!LqUrl.equals("")){
          MusicUrl =LqUrl;
        }
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(SearchActivity.this).builder();
        actionSheetDialog.setTitle("选择");
        final String finalMusicUrl = MusicUrl;
        actionSheetDialog.addSheetItem("MP3", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                download(SongName + "-" + Artist + "-L" + ".mp3", finalMusicUrl, SongId);
            }
        });
        if (!VideoUrl.equals("")) {
            actionSheetDialog.addSheetItem("MV", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {

                    download(SongName + "-" + Artist + ".mp4", VideoUrl, SongId);

                }
            });
        }
        actionSheetDialog.show();
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

    private void download(String name, String url, String tag) {

        DownloadUtil.StartDownload(this, name, url, tag);

    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                break;
        }
    }

    @Override
    public void Music(View itemView, int position) {

        MusicBean.listenUrl = result.get(position).getListenUrl();
        MusicBean.pic = result.get(position).getPicUrl();
        MusicBean.time = result.get(position).getLength();
        MusicBean.videoUrl = result.get(position).getVideoUrl();
        MusicBean.songName = result.get(position).getSongName();
        MusicBean.artist = result.get(position).getArtist();
        MusicBean.hqUrl = result.get(position).getHqUrl();
        MusicBean.sqUrl = result.get(position).getSqUrl();
        MusicBean.lqUrl = result.get(position).getLqUrl();
        Intent i = new Intent(SearchActivity.this, MusicPlayer.class);
        startActivity(i);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                Download(postion1);
                return true;
            case R.id.mv:
                String    MVurl = result.get(postion1).getMvUrl();
                Log.e("qtfreet",MVurl);
                String Songname =   result.get(postion1).getSongName()+"--"+ result.get(postion1).getArtist();
                Intent i = new Intent(SearchActivity.this,VideoActivity.class);
                Bundle bundle   = new Bundle();
                bundle.putString("name",Songname);
                bundle.putString("url",MVurl);
                i.putExtras(bundle);
                startActivity(i);
                return true;


        }
        return false;
    }
}
