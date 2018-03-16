package com.android.accountmanager.event;

import android.os.Message;

/**
 * Created by fantao on 18-2-9.
 */

public class SignOutEvent extends BaseEvent {

    private SignOutEvent () {
        what = EVENT_SIGNOUT;
    }

    public final static SignOutEvent newInstance() {
        return new SignOutEvent();
    }
}
