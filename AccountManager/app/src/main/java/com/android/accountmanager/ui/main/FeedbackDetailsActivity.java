package com.android.accountmanager.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.accountmanager.R;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.FeedBackUtils;

public class FeedbackDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextContent, mTextDate, mTextFeedback;
    private ImageView mImgBack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback_details);
        AppUtils.setNoActionbarTheme(this);
        mTextContent = (TextView) findViewById(R.id.text_content);
        mTextDate = (TextView) findViewById(R.id.text_date);
        mImgBack = (ImageView) findViewById(R.id.image_back);
        mImgBack.setOnClickListener(this);
        mTextFeedback = (TextView) findViewById(R.id.text_feedback);

        Intent intent = getIntent();
        if (intent != null) {
            String content = intent.getBundleExtra("bundle").getString("content");
            String date = intent.getBundleExtra("bundle").getString("date");
            mTextContent.setText(content);
            mTextDate.setText(date);
        }


    }

    @Override
    public void onBackPressed() {
        setResult(FeedBackUtils.FEEDBACK_RESULT);
        super.onDestroy();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }
}
