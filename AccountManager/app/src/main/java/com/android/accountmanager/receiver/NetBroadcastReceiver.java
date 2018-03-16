package com.android.accountmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.event.NetStateChangeEvent;


public class NetBroadcastReceiver extends BroadcastReceiver {
	private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("shuang", "onReceive: "+intent.getAction());
		if (intent.getAction().equals(NET_CHANGE_ACTION)) {
			HandlerBus.getDefault().post(NetStateChangeEvent.newInstance()
					.setNetState(InternetHelper.getNetState(context)));
		}
	}
}
