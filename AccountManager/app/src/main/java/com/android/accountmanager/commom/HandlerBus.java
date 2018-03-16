package com.android.accountmanager.commom;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.accountmanager.event.BaseEvent;
import com.android.accountmanager.event.FinishEvent;
import com.android.accountmanager.event.NetStateChangeEvent;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.event.VercodeStateChangeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fantao on 18-1-25.
 */

public class HandlerBus {
    private static HandlerBus sDefaultInstance;
    private List<EventChangeListener> mListeners;
    private Handler mHandler;

    private HandlerBus() {
        mListeners = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BaseEvent.EVENT_NET:
                        for (EventChangeListener listener : mListeners) {
                            listener.netChangeEvent((NetStateChangeEvent) msg.obj);
                        }
                        break;
                    case BaseEvent.EVENT_VERCODE:
                        VercodeStateChangeEvent vercodeEvent = (VercodeStateChangeEvent) msg.obj;
                        for (EventChangeListener listener : mListeners) {
                            listener.vercodeChangeEvent(vercodeEvent);
                        }
                        int remain = vercodeEvent.getRemain();
                        if (remain > 0) {
                            vercodeEvent.setRemain(remain - 1);
                            HandlerBus.this.postDelayed(vercodeEvent, 1000);
                        }
                        break;
                    case BaseEvent.EVENT_SIGNOUT:
                        for (EventChangeListener listener : mListeners) {
                            listener.signOutEvent((SignOutEvent) msg.obj);
                        }
                        break;
                    case BaseEvent.EVENT_FINISH:
                        for (EventChangeListener listener : mListeners) {
                            listener.finishEvent((FinishEvent) msg.obj);
                        }
                        break;
                }
            }
        };
    }

    public static final HandlerBus getDefault() {
        synchronized (HandlerBus.class) {
            if (sDefaultInstance == null) {
                sDefaultInstance = new HandlerBus();
            }
        }
        return sDefaultInstance;
    }

    public final void register(EventChangeListener listener) {
        mListeners.add(listener);
    }

    public final void unRegister(EventChangeListener listener) {
        mListeners.remove(listener);
    }

    public final void post(BaseEvent event) {
        mHandler.sendMessage(event.toMessage());
    }

    public final void postDelayed(BaseEvent event, long delayMillis) {
        mHandler.sendMessageDelayed(event.toMessage(), delayMillis);
    }

    public interface EventChangeListener {
        void netChangeEvent(NetStateChangeEvent event);
        void vercodeChangeEvent(VercodeStateChangeEvent event);
        void signOutEvent(SignOutEvent event);
        void finishEvent(FinishEvent event);
    }
}
