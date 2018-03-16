package com.android.accountmanager.event;

import android.os.Message;

/**
 * Created by fantao on 18-2-9.
 */

public abstract class BaseEvent {
    public final static int EVENT_BASE = 0x10000;
    public final static int EVENT_NET = 0x10001;
    public final static int EVENT_VERCODE = 0x10002;
    public final static int EVENT_SIGNOUT = 0x10003;
    public final static int EVENT_FINISH = 0x10004;

    public int what;
    public int state;

    public Message toMessage() {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = this;
        return msg;
    }
}
