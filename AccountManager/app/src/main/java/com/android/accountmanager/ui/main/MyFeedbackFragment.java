package com.android.accountmanager.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.model.FeedBackListTemplate;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.FeedBackUtils;
import com.android.accountmanager.utils.JackSonUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyFeedbackFragment extends Fragment {
    private static MyFeedbackFragment mFragment;
    private ListView mListView;
    private List<FeedBackListTemplate.DataBean> mDate = new ArrayList<>();
    private CompositeSubscription mSubscriptions;
    private ProgressBar mProgress;
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_myfeedback, container, false);
        mListView = (ListView) parentView.findViewById(R.id.feedback_listview);
        mProgress = (ProgressBar) parentView.findViewById(R.id.progressBar);
        mSubscriptions = new CompositeSubscription();
        myAdapter = new MyAdapter();
        TextView empyty = (TextView) parentView.findViewById(R.id.feedback_empty);
        mListView.setEmptyView(empyty);

        return parentView;
    }

    public static MyFeedbackFragment newInstance() {
        if (null == mFragment) {
            mFragment = new MyFeedbackFragment();
        }
        return mFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
    }

    public void initDate() {
        final String token = AppUtils.getCurrentToken(getActivity());
        Log.d("test", "initDate: token=====" + token);
        if (TextUtils.isEmpty(token)) return;

        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getFeedbackLis(getActivity(), token);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "initDate: " + s);
                        FeedBackListTemplate resultTemplate = JackSonUtil.json2Obj(s, FeedBackListTemplate.class);
                        mDate = resultTemplate.getData();
                        if (resultTemplate == null) {
                            Toast.makeText(getActivity(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    mProgress.setVisibility(View.GONE);
                                    mListView.setVisibility(View.VISIBLE);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    Toast.makeText(getActivity(), "token无效", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    mListView.setVisibility(View.GONE);
                                    break;

                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
        mListView.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDate.size();
        }

        @Override
        public Object getItem(int i) {
            return mDate.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.feedback_item, null);
            }
            TextView name = FeedBackUtils.CommonViewHolder.get(view, R.id.mtname);
            TextView date = FeedBackUtils.CommonViewHolder.get(view, R.id.mtdate);
            TextView content = FeedBackUtils.CommonViewHolder.get(view, R.id.mtcontent);
            name.setText(mDate.get(i).getMtname());
            date.setText(mDate.get(i).getMtdate());
            content.setText(mDate.get(i).getMtcontent());
            return view;
        }
    }

}
