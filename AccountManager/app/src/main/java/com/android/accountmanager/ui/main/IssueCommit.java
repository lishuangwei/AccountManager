package com.android.accountmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.ui.account.AccountActivity;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.ui.login.LoginActivity;
import com.android.accountmanager.utils.AppUtils;
import com.github.ybq.android.spinkit.style.Circle;


public class IssueCommit extends AppCompatActivity implements FeedbackContract.FeedbackView, View.OnClickListener, TextWatcher {
    private FeedbackContract.FeedBackPresenter mPresenter;
    private Toast mToast;
    private Button mBtcommit;
    private EditText mFeedbackContent;
    private String mTile, mPkname, mIcon;
    private Circle mCircleDrawable;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_commit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        mTile = intent == null ? "" : intent.getStringExtra("title");
        mPkname = intent == null ? "" : intent.getStringExtra("package");
        Log.d("test", "onCreate: " + mTile);
        toolbar.setTitle(mTile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        mToast = Toast.makeText(this, "RegisterActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mPresenter = new FeedBackPresenter(this);
        mBtcommit = (Button) findViewById(R.id.bt_commit);
        mBtcommit.setOnClickListener(this);
        mFeedbackContent = (EditText) findViewById(R.id.feedback_content);
        mFeedbackContent.addTextChangedListener(this);

        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);

        mText = (TextView) findViewById(R.id.loading);
        mText.setBackground(mCircleDrawable);
        mText.setOnClickListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_feedback, menu);
//        mMenuItem = menu.findItem(R.id.commit);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//            case R.id.commit:
//                if (TextUtils.isEmpty(mFeedbackContent.getText().toString())) {
//                    showAction(R.string.feedback_commit_null);
//                }
//                startLoad();
//                item.setIcon(R.drawable.ic_no_image);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        commit(mTile, mPkname, "");
//                    }
//                }, 1000);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void stopLoad() {
        if (mText != null) {
            mText.setText(R.string.feedback_commit);
            mText.setBackground(null);
            mCircleDrawable.stop();
        }
    }

    private void startLoad() {
        if (mText != null) {
            mText.setText(null);
            mText.setBackground(mCircleDrawable);
            mCircleDrawable.start();
        }
    }


    @Override
    public void commit(String name, String content, String icon) {
        mPresenter.commit(name, content, icon);
    }

    @Override
    public void startMain() {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showAction(CharSequence actionString) {
        mToast.setText(actionString);
        mToast.show();
        stopLoad();
    }

    @Override
    public void showAction(int strId) {
        mToast.setText(strId);
        mToast.show();
        stopLoad();
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
        stopLoad();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading:
                if (TextUtils.isEmpty(mFeedbackContent.getText().toString())) {
                    showAction(R.string.feedback_commit_null);
                    return;
                }
                startLoad();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commit(mTile, mPkname, "");
                    }
                }, 1000);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean enable = !TextUtils.isEmpty(mFeedbackContent.getText().toString());
        Log.d("test", "onTextChanged: " + enable);
        mBtcommit.setEnabled(enable);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
