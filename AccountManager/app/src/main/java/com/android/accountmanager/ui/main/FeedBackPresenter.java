package com.android.accountmanager.ui.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.model.FeedBackTemplate;
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.TemplateUtils;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.JackSonUtil;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by root on 18-3-5.
 */

public class FeedBackPresenter implements FeedbackContract.FeedBackPresenter {

    private FeedbackContract.FeedbackView mFeedbackView;
    private CompositeSubscription mSubscriptions;

    public FeedBackPresenter(@NonNull FeedbackContract.FeedbackView loginView) {
        mFeedbackView = loginView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void commit(final String name, final String content, final String icon) {
        int netState = InternetHelper.getNetState(mFeedbackView.getContext());;
        Log.d("test", "commit: "+netState);
        if (netState == InternetHelper.I_NET_NONE) {
            mFeedbackView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .saveModule(mFeedbackView.getContext(), 0, name, content,icon);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "commit: " + s);
                        FeedBackTemplate resultTemplate = JackSonUtil.json2Obj(s, FeedBackTemplate.class);
                        if (resultTemplate == null) {
                            mFeedbackView.showAction(R.string.toast_unknown_error);
                            mFeedbackView.startMain();
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    mFeedbackView.showAction(R.string.feedback_commit_toast);
                                    mFeedbackView.startMain();
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void sendVercode(String type, String phoneNumber) {

    }

    @Override
    public boolean verifyCode(String type, String phoneNumber, String vercode) {
        return false;
    }

}
