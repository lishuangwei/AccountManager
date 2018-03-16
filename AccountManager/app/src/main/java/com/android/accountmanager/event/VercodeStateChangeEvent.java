package com.android.accountmanager.event;

import android.os.Message;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class VercodeStateChangeEvent extends BaseEvent {

    private VercodeStateChangeEvent () {
        what = EVENT_VERCODE;
        state = 60;
    }

    public final static VercodeStateChangeEvent newInstance() {
        return new VercodeStateChangeEvent();
    }

    public final static VercodeStateChangeEvent getInstance(BaseEvent event) {
        return new VercodeStateChangeEvent().setRemain(event.state);
    }

    public int getRemain() {
        return state;
    }

    public VercodeStateChangeEvent setRemain(int remain) {
        state = remain;
        return this;
    }
}
