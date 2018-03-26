package com.android.accountmanager.model;

import android.graphics.drawable.Drawable;

/**
 * Created by lsw on 18-3-21.
 */

public class AppInfoTemplate {
    String appName;
    String packageName;
    Drawable appIcon;

    public AppInfoTemplate(){}

    public AppInfoTemplate(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "AppInfoTemplate{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appIcon=" + appIcon +
                '}';
    }
}
