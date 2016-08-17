package com.qtfreet.musicuu.ui.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Constant.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment mSettingsFragment;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_name)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }


    }

    private void initView() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbarTitle.setText("设置");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private EditTextPreference editTextPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            editTextPreference = (EditTextPreference) getPreferenceManager().findPreference(Constants.SAVE_PATH);
            editTextPreference.setOnPreferenceChangeListener(this);

            String path = editTextPreference.getSharedPreferences().getString(Constants.SAVE_PATH, "");
            if (!path.equals("musicuu")) {
                editTextPreference.setSummary("当前保存在sd卡下的 " + path + " 文件夹下");
            }
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if (preference == editTextPreference) {
                editTextPreference.setText(editTextPreference.getSharedPreferences().getString(Constants.SAVE_PATH, ""));
                return true;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == editTextPreference) {
                editTextPreference.setSummary("当前保存在sd卡下的 " + newValue.toString() + " 文件夹下");
                return true;
            }
//            editTextPreference.getSharedPreferences().edit().putString("SavePath", newValue.toString()).commit();

            return false;
        }
    }

}
