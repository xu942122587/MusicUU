package com.qtfreet.musicuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.qtfreet.musicuu.utils.FileUtils;
import com.qtfreet.musicuu.utils.SPUtils;
import com.qtfreet.musicuu.wiget.ActionSheetDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.ib_search_btn)
    ImageButton mSearchButton;
    @Bind(R.id.et_search_content)
    EditText mSearchEditText;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firstUse();
        initView();
        checkUpdate();

    }


    private void initView() {
        ButterKnife.bind(this);
        mistype = "wy";
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


    private void firstUse() {
        boolean isFirst = (boolean) SPUtils.get(this, Constants.IS_FIRST_RUN, true);
        if (isFirst) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.start_info);

            builder.setMessage(getString(R.string.description));
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.i_know, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPUtils.put(MainActivity.this, Constants.IS_FIRST_RUN, false);
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
        if (!(boolean) SPUtils.get(Constants.MUSICUU_PREF, this, Constants.AUTO_CHECK, true)) {
            return;
        }
        PgyUpdateManager.register(this);
    }


    private void initDir() {
        String path = (String) SPUtils.get(Constants.MUSICUU_PREF, this, Constants.SAVE_PATH, "musicuu");
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

    String mistype = "";
    @Bind(R.id.btn_search)
    Button btn_search;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private void startSearchSong() {
        String text = mSearchEditText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.no_music_name, Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY, text);
        bundle.putString(Constants.TYPE, mistype);
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }


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
                        mistype = "wy";
                    }
                }).addSheetItem("电信爱音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "dx";
                    }
                }).addSheetItem("百度音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "bd";
                    }
                }).addSheetItem("天天动听", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "tt";
                    }
                }).addSheetItem("虾米音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "xm";
                    }
                }).addSheetItem("酷我音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "kw";
                    }
                }).addSheetItem("酷狗音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "kg";
                    }
                }).addSheetItem("多米音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "dm";
                    }
                }).addSheetItem("萌否音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "mf";
                    }
                }).addSheetItem("QQ音乐", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mistype = "qq";
                    }
                }).show();
                break;
        }
    }
}
