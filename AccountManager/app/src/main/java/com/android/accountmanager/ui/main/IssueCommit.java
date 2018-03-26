package com.android.accountmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.utils.AppUtils;
import com.github.ybq.android.spinkit.style.Circle;


public class IssueCommit extends AppCompatActivity implements FeedbackContract.FeedbackView, View.OnClickListener, TextWatcher {
    private FeedbackContract.FeedBackPresenter mPresenter;
    private LinearLayout mLayout;
    private Toast mToast;
    private EditText mFeedbackContent;
    private String mTile, mPkname, mContent;
    private Circle mCircleDrawable;
    private TextView mText, mTextTile;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_commit);
        AppUtils.setNoActionbarTheme(this);

        Intent intent = getIntent();
        mTile = intent == null ? "" : intent.getStringExtra("title");
        mPkname = intent == null ? "" : intent.getStringExtra("package");
        Log.d("test", "onCreate: " + mTile);

        mTextTile = (TextView) findViewById(R.id.custom_actionbar_title);
        mTextTile.setText(mTile);
        mImageView = (ImageView) findViewById(R.id.image_back);
        mImageView.setOnClickListener(this);

        mToast = Toast.makeText(this, "RegisterActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mPresenter = new FeedBackPresenter(this);
        mFeedbackContent = (EditText) findViewById(R.id.feedback_content);
        mFeedbackContent.addTextChangedListener(this);
        mLayout = (LinearLayout) findViewById(R.id.success_layout);

        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);

        mText = (TextView) findViewById(R.id.loading);
        mText.setBackground(mCircleDrawable);
        mText.setOnClickListener(this);

    }

    private void setSuccess() {
        mLayout.setVisibility(View.VISIBLE);
    }

    private void setError() {
        mLayout.setVisibility(View.INVISIBLE);
    }

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
        setSuccess();
        stopLoad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoad();
        setError();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showAction(CharSequence actionString) {
        Log.d("test", "showAction111: IssueCommit");
        stopLoad();
        mToast.setText(actionString);
        mToast.show();
    }

    @Override
    public void showAction(int strId) {
        Log.d("test", "showAction222: IssueCommit");
        stopLoad();
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
        stopLoad();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading:
                if (TextUtils.isEmpty(mFeedbackContent.getText().toString())) {
                    showAction(R.string.feedback_commit_null);
                    setError();
                    return;
                }
                startLoad();
                setError();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commit(mTile, mContent, "");
                    }
                }, 1000);
                break;
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mContent = mFeedbackContent.getText().toString();

    }
}
