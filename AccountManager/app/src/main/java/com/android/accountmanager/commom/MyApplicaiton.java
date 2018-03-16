package com.android.accountmanager.commom;

import android.app.Application;
import android.os.Message;

import com.android.accountmanager.utils.AppUtils;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class MyApplicaiton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        HandlerBus.getDefault().register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        HandlerBus.getDefault().unRegister(this);
    }

}
