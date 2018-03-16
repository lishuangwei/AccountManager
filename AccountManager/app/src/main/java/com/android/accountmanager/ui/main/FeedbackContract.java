package com.android.accountmanager.ui.main;

import android.support.v4.app.Fragment;

import com.android.accountmanager.base.BasePresenter;
import com.android.accountmanager.base.BaseView;

/**
 * Created by mn on 2017/2/5 0005.
 */
public class FeedbackContract {
    public interface FeedbackView extends BaseView<FeedBackPresenter> {
        void commit(String name, String content, String icon);
        void startMain();
    }

    interface FeedBackPresenter extends BasePresenter {
        void commit(String name, String content, String icon);
    }
}
