package com.android.accountmanager.ui.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.model.ResultTemplate;
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
        int netState = InternetHelper.getNetState(mFeedbackView.getContext());
        ;
        Log.d("test", "commit: " + netState);
        if (netState == InternetHelper.I_NET_NONE) {
            mFeedbackView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
//                        return RequestServer.getInstance()
//                                .saveModule(mFeedbackView.getContext(), 0, name, content,icon);
                        //modify by john , 修改了"用户反馈信息" 接口
                        final String token = AppUtils.getCurrentToken(mFeedbackView.getContext());
                        final String account = AppUtils.getCurrentAccount(mFeedbackView.getContext()).getAccount();
                        return RequestServer.getInstance()
                                .saveFeatbackInfo(mFeedbackView.getContext(), token, name, content, account);
                        //modify by john , 修改了"用户反馈信息" 接口


                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "commit: " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mFeedbackView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    mFeedbackView.startMain();
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mFeedbackView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RETCODE_PATAM_TYPE_ERR:
                                    mFeedbackView.showAction(R.string.toast_param_error);
                                    break;
                                case ResultCode.RETCODE_USER_NON_EXIST:
                                    mFeedbackView.showAction(R.string.toast_account_not_exist);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mFeedbackView.showAction(R.string.toast_vercode_error);
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
        mSubscriptions.clear();
    }

    @Override
    public void sendVercode(String type, String phoneNumber) {

    }

    @Override
    public boolean verifyCode(String type, String phoneNumber, String vercode) {
        return false;
    }

}
