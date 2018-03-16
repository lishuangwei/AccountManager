package com.android.accountmanager.event;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class FinishEvent extends BaseEvent {

    private FinishEvent () {
        what = EVENT_FINISH;
    }

    public final static FinishEvent newInstance() {
        return new FinishEvent();
    }
}
