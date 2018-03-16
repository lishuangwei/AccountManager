package com.android.accountmanager.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.ui.account.AccountActivity;
import com.android.accountmanager.ui.login.LoginActivity;
import com.android.accountmanager.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPage;
    private ArrayList<Fragment> mList;
    private MyFragmentAdapter mAdapter;
    private RadioGroup mRadioGroup;
    private RadioButton mProblemRadio, mFeedBackRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        mViewPage = (ViewPager) findViewById(R.id.viewpage);
        mList = new ArrayList<Fragment>();
        Fragment f1 = NormalProblemFragment.newInstance();
        Fragment f2 = MyFeedbackFragment.newInstance();
        mList.add(f1);
        mList.add(f2);

        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new MyFragmentAdapter(fm, mList);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);

        mProblemRadio = (RadioButton) findViewById(R.id.page_normalproblem);
        mFeedBackRadio = (RadioButton) findViewById(R.id.page_myfeedback);

        mViewPage.setAdapter(mAdapter);
        mViewPage.setOnPageChangeListener(this);
        mViewPage.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isLogin = AppUtils.isLogined(this);
        Log.d("test", "onPrepareOptionsMenu===================: " + isLogin);
        menu.findItem(R.id.action_account).setIcon(isLogin ? R.drawable.ic_account_login : R.drawable.ic_account_unlogin);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!AppUtils.isLogined(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(null);
            builder.setMessage(R.string.feedback_need_login);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.feedback_login,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    null);
            builder.create().show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_account:
                Intent intent = null;
                if (AppUtils.isLogined(this)) {
                    intent = new Intent(MainActivity.this, AccountActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("test", "onPageScrolled: " + position);

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("test", "onPageSelected: " + position);
        if (position == 0) {
            mProblemRadio.setChecked(true);
        } else {
            mFeedBackRadio.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (!AppUtils.isLogined(this)) {
            Toast.makeText(this, R.string.feedback_need_login, Toast.LENGTH_SHORT).show();
            mProblemRadio.setChecked(true);
            return;
        }
        switch (i) {
            case R.id.page_normalproblem:
                mViewPage.setCurrentItem(0);
                break;
            case R.id.page_myfeedback:
                mViewPage.setCurrentItem(1);
                break;
        }

    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        private FragmentManager fragmetnmanager;
        private List<Fragment> listfragment;

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmetnmanager = fm;
            this.listfragment = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return listfragment.get(arg0);
        }

        @Override
        public int getCount() {
            return listfragment.size();
        }


    }
}
