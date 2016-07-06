package com.qtfreet.musicuu.ui.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.MusicBean;
import com.qtfreet.musicuu.utils.MyThreadPool;
import com.qtfreet.musicuu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import butterknife.Bind;
import butterknife.ButterKnife;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by qtfreet on 2016/3/20.
 */
public class MusicPlayer extends AppCompatActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, View.OnClickListener {
    @Bind(R.id.iv_play)
    ImageView iv_play;
    @Bind(R.id.tv_play_name)
    TextView songTitle;
    @Bind(R.id.sb_play)
    AppCompatSeekBar sb_play;
    @Bind(R.id.tv_current_time)
    TextView tv_current_time;
    @Bind(R.id.tv_duration_time)
    TextView tv_duration_time;
    @Bind(R.id.fab_2)
    FloatingActionButton play;
    @Bind(R.id.fab_1)
    FloatingActionButton prev;
    @Bind(R.id.fab_3)
    FloatingActionButton next;
    private IjkMediaPlayer ijkExoMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        pic = MusicBean.pic;
        MvUrl = MusicBean.videoUrl;
        if (!MusicBean.sqUrl.equals("")) {
            url = MusicBean.sqUrl;
        } else if (!MusicBean.hqUrl.equals("")) {
            url = MusicBean.hqUrl;
        } else if (!MusicBean.lqUrl.equals("")) {
            url = MusicBean.lqUrl;
        } else {
            Toast.makeText(this, "未找到播放链接", Toast.LENGTH_SHORT).show();
            return;
        }
        name = MusicBean.songName;
        time = MusicBean.time;
        initview();
        play.setOnClickListener(this);
        initThread();
    }


    String pic;
    String MvUrl;
    String url;
    String time;
    String name;


    private void initThread() {
        ExecutorService e = MyThreadPool.getInstance().getMyExecutorService();
        e.execute(new Runnable() {
            @Override
            public void run() {
                while (isShowCurrentTime) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updataProgressBar();
                        }
                    });

                }

            }
        });

    }
    private boolean isShowCurrentTime = true;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;

    private void updataProgressBar() {
        if (ijkExoMediaPlayer == null) {
            return;
        }
        if (!isShowCurrentTime) {
            return;
        }
        int currentTime = (int) ijkExoMediaPlayer.getCurrentPosition();
        tv_current_time.setText(SystemUtil.generateTime(ijkExoMediaPlayer.getCurrentPosition()));
        sb_play.setProgress(currentTime);
    }

    private void loadCover(final String pic, final String url) {
        Picasso.with(MusicPlayer.this)
                .load(pic).into(iv_play);
        ijkExoMediaPlayer = new IjkMediaPlayer();
        ijkExoMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            ijkExoMediaPlayer.setDataSource(this, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        songTitle.setText(name);
        ijkExoMediaPlayer.prepareAsync();
        ijkExoMediaPlayer.setOnPreparedListener(this);
        ijkExoMediaPlayer.setOnBufferingUpdateListener(this);
        ijkExoMediaPlayer.setOnCompletionListener(this);
    }

    private void initview() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbarTitle.setText("播放");
            }
        }
        loadCover(pic, url);
        sb_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                if (seekBar == sb_play) {
                    ijkExoMediaPlayer.seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isShowCurrentTime = false;
        if (ijkExoMediaPlayer != null) {
            ijkExoMediaPlayer.reset();
            ijkExoMediaPlayer.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ijkExoMediaPlayer.isPlaying()){
            play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            ijkExoMediaPlayer.pause();
        }

    }


    @Override
    public void onPrepared(IMediaPlayer mp) {
        mp.pause();
        sb_play.setMax((int) mp.getDuration());
        tv_duration_time.setText(SystemUtil.generateTime(mp.getDuration()));
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        isShowCurrentTime = false;
        sb_play.setProgress(0);
        mp.pause();
        mp.seekTo(0);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_2:
                if(!ijkExoMediaPlayer.isPlaying()){
                    ijkExoMediaPlayer.start();
                    play.setImageResource(R.drawable.ic_pause_white_24dp);
                }else {
                    play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    ijkExoMediaPlayer.pause();
                }
                break;
        }
    }
}
