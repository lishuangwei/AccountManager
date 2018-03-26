package com.android.accountmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseView;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.account.AccountActivity;
import com.android.accountmanager.ui.account.PhotoSelectionHandler;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.ui.login.LoginActivity;
import com.android.accountmanager.utils.AppUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, BaseView {
    private ViewPager mViewPage;
    private ArrayList<Fragment> mList;
    private MyFragmentAdapter mAdapter;
    private RadioGroup mRadioGroup;
    private RadioButton mProblemRadio, mFeedBackRadio;
    private Toast mToast;
    private Button mBtCancel, mBtOk;
    private AlertDialog mDialog;
    private ImageView mImgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("shuang", "onCreate: MainActivity");
        setContentView(R.layout.activity_main);
        AppUtils.setNoActionbarTheme(this);
        init();
    }

    private void init() {
        mToast = Toast.makeText(this, "MainActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
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

        mImgIcon = (ImageView) findViewById(R.id.image_icon);
        mImgIcon.setOnClickListener(mClick);
        mImgIcon.setImageDrawable(getDrawable(R.drawable.ic_account_unlogin));
        updateIcon(this, mImgIcon);

    }

    private void updateIcon(Context context, ImageView imageView) {
        if (AppUtils.isLogined(context)) {
            String uriStr = getSharedPreferences("current_account", Context.MODE_PRIVATE).getString(UserInfoTemplate.KEY_ACCOUNT_ICON, null);
            Drawable drawable = null;
            if (!TextUtils.isEmpty(uriStr)) {
                try {
                    drawable = PhotoSelectionHandler
                            .getRoundDrawableFromUri(this, Uri.parse(uriStr));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageDrawable(drawable == null ? getDrawable(R.drawable.ic_account_login) : drawable);
        }
        Log.d("shuang", "updateIcon: MainActivity -----" + AppUtils.isLogined(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.custom_feedback_layout, null);
        mBtCancel = (Button) layout.findViewById(R.id.bt_cancel);
        mBtCancel.setOnClickListener(mClick);
        mBtOk = (Button) layout.findViewById(R.id.bt_setting);
        mBtOk.setOnClickListener(mClick);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.NetWorkDialogStyle);
        builder.setCancelable(false);
        builder.setView(layout);
        mDialog = builder.create();
        if (!AppUtils.isLogined(this)) {
            mDialog.show();
            Window window = mDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            window.getDecorView().setPadding(0, 0, 0, 0);
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

        }
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_cancel:
                    mDialog.dismiss();
                    break;
                case R.id.bt_setting:
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.image_icon:
                    Intent intent1 = null;
                    if (AppUtils.isLogined(MainActivity.this)) {
                        intent1 = new Intent(MainActivity.this, AccountActivity.class);
                    } else {
                        intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    startActivity(intent1);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("shuang", "onResume: MainActivity");
        updateIcon(this, mImgIcon);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
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
            showAction(R.string.feedback_need_login);
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showAction(CharSequence actionString) {
        mToast.setText(actionString);
        mToast.show();

    }

    @Override
    public void showAction(int strId) {
        mToast.setText(strId);
        mToast.show();

    }

    @Override
    public void sendVercode(String type, String phoneNumber) {

    }

    @Override
    public boolean verifyCode(String type, String phoneNumber, String vercode) {
        return false;
    }

    @Override
    public void networkAnomaly() {
        SetNetworkFragment fragment = new SetNetworkFragment();
        fragment.show(getSupportFragmentManager(), getClass().toString());

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
