package com.qtfreet.musicuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.APi;
import com.qtfreet.musicuu.model.ApiService;
import com.qtfreet.musicuu.model.RecomendResult;
import com.qtfreet.musicuu.ui.adapter.SimpleAdapter;
import com.qtfreet.musicuu.utils.FileUtils;
import com.qtfreet.musicuu.utils.SPUtils;
import com.qtfreet.musicuu.wiget.ActionSheetDialog;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firstuse();
        initview();
        checkUpdate();

    }


    private void firstuse() {
        boolean isfirst = (boolean) SPUtils.get(this, "isfirst", true);
        if (isfirst) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");

            builder.setMessage(getString(R.string.description));
            builder.setCancelable(false);
            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPUtils.put(MainActivity.this, "isfirst", false);
                }
            });
            builder.show();
        }
    }


    @Override
    protected void onResume() {
        initDir();
        super.onResume();
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1100);
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(MainActivity.this);

    }

    private void checkUpdate() {
        if (!(boolean) SPUtils.get("com.qtfreet.musicuu_preferences", this, "AutoCheck", true)) {
            return;
        }
        PgyUpdateManager.register(this);
    }


    private void initDir() {
        String path = (String) SPUtils.get("com.qtfreet.musicuu_preferences", this, "SavePath", "musicuu");
        if (FileUtils.getInstance().isSdCardAvailable()) {
            if (!FileUtils.getInstance().isFileExist(path)) {
                FileUtils.getInstance().creatSDDir(path);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyFeedbackShakeManager.unregister();
    }

    String musictype = "";
    @Bind(R.id.btn_search)
    Button btn_search;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private void startSearchSong() {
        String text = mSearchEditText.getText().toString();
        if (text.equals("")) {
            Toast.makeText(MainActivity.this, "你还没有输入歌曲名字呢", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("key", text);
        bundle.putString("type", musictype);
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }


    @Bind(R.id.ib_search_btn)
    ImageButton mSearchButton;
    @Bind(R.id.et_search_content)
    EditText mSearchEditText;

    private void initview() {
        ButterKnife.bind(this);
        musictype = "wy";
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                toolbarTitle.setText(R.string.main_title);
            }
        }
        floatingActionButton.setOnClickListener(this);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchSong();

            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchSong();
            }
        });

    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                new ActionSheetDialog(MainActivity.this).builder().setTitle("选择音源").addSheetItem("网易云音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "wy";
                    }
                }).addSheetItem("电信爱音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "dx";
                    }
                }).addSheetItem("百度音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "bd";
                    }
                }).addSheetItem("天天动听", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "tt";
                    }
                }).addSheetItem("虾米音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "xm";
                    }
                }).addSheetItem("酷我音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "kw";
                    }
                }).addSheetItem("酷狗音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "kg";
                    }
                }).addSheetItem("多米音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "dm";
                    }
                }).addSheetItem("萌否音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "mf";
                    }
                }).addSheetItem("QQ音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        musictype = "qq";
                    }
                }).show();
                break;
        }
    }
}
