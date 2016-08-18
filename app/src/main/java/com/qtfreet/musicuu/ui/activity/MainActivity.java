package com.qtfreet.musicuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
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
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchSong();
            }
        });
        mSearchEditText.setOnKeyListener(this);
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
                new BottomDialog(MainActivity.this)
                        .title(getString(R.string.sel_music_type))             //设置标题

                        .inflateMenu(R.menu.music_type)         //传人菜单内容
                        .itemClick(new OnItemClickListener() {  //设置监听
                            @Override
                            public void click(Item item) {
                                String type = item.getTitle();
                                if (type.equals(getString(R.string.music_wy))) {
                                    mistype = "wy";
                                } else if (type.equals(getString(R.string.music_qq))) {
                                    mistype = "qq";
                                } else if (type.equals(getString(R.string.music_kg))) {
                                    mistype = "kg";
                                } else if (type.equals(getString(R.string.music_kw))) {
                                    mistype = "kw";
                                } else if (type.equals(getString(R.string.music_bd))) {
                                    mistype = "bd";
                                } else if (type.equals(getString(R.string.music_tt))) {
                                    mistype = "tt";
                                } else if (type.equals(getString(R.string.music_dx))) {
                                    mistype = "dx";
                                } else if (type.equals(getString(R.string.music_xm))) {
                                    mistype = "xm";
                                }
                                Toast.makeText(MainActivity.this, "已切换成 " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // 先隐藏键盘
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
            startSearchSong();
        }
        return false;
    }
}
