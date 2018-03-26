package com.android.accountmanager.base;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.event.FinishEvent;
import com.android.accountmanager.event.NetStateChangeEvent;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.event.VercodeStateChangeEvent;
import com.android.accountmanager.receiver.NetBroadcastReceiver;
import com.android.accountmanager.utils.AppUtils;

public abstract class BaseActivity extends AppCompatActivity implements HandlerBus.EventChangeListener {
    private final int REQUEST_CODE_PERMISSION = 0;
    public static int sNetState;
    public Context mApplicationContext;
    private NetBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = getApplicationContext();
        sNetState = InternetHelper.getNetState(mApplicationContext);
        HandlerBus.getDefault().register(this);
        initReceiver();
    }

    protected void initReceiver() {
        Log.d("shuang", "initReceiver: ");
        mReceiver = new NetBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mReceiver, intentFilter);
    }

    public static int getNetState() {
        return sNetState;
    }

    @Override
    public void netChangeEvent(NetStateChangeEvent netStateChangeEvent) {
        sNetState = InternetHelper.getNetState(this);
    }

    @Override
    public void vercodeChangeEvent(VercodeStateChangeEvent event) {
        View view = findViewById(R.id.send_vercode);
        if (view != null && view instanceof TextView) {
            TextView sendVercodeText = (TextView) view;
            int remain = event.getRemain();
            sendVercodeText.setEnabled(remain < 1);
            sendVercodeText.setText(remain < 1 ? String.format(getString(R.string.action_send_vercode))
                    : String.format(getString(R.string.action_send_vercode_disable), event.getRemain()));
        }
    }

    @Override
    public void signOutEvent(SignOutEvent event) {
        Log.d("test", "signOutEvent: BaseActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            HandlerBus.getDefault().unRegister(this);
            Log.d("shuang", "unregister: ");
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void finishEvent(FinishEvent event) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        Toast.makeText(this, R.string.toast_permission_not_granted, Toast.LENGTH_SHORT).show();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, R.string.toast_permission_granted, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean checkHasPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void requestPermission(String permission) {
        if (!checkHasPermission(this, permission)) {
            requestPermissions(new String[]{permission}, REQUEST_CODE_PERMISSION);
        }
    }
}
