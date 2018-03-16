package com.android.accountmanager.event;

import android.os.Message;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class NetStateChangeEvent extends BaseEvent {

    private NetStateChangeEvent () {
        what = EVENT_NET;
    }

    public final static NetStateChangeEvent newInstance() {
        return new NetStateChangeEvent();
    }

    public final static NetStateChangeEvent getInstance(BaseEvent event) {
        return new NetStateChangeEvent().setNetState(event.state);
    }

    public int getNetState() {
        return state;
    }

    public NetStateChangeEvent setNetState(int netState) {
        state = netState;
        return this;
    }
}
