package com.android.accountmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.util.SparseArray;
import android.view.View;

import com.android.accountmanager.R;
import com.android.accountmanager.model.AppInfoTemplate;
import com.android.accountmanager.model.FeedBackTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FeedBackUtils {
    public static final int FEEDBACK_REQUEST = 1;
    public static final int FEEDBACK_RESULT = 2;
    public static List<ResolveInfo> loadApps(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        Iterator<ResolveInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            ResolveInfo info = iterator.next();
            String pkgName = info.activityInfo.packageName;
            PackageInfo packageInfo = null;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    iterator.remove();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static class CommonViewHolder {
        public static <T extends View> T get(View view, int id) {

            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View chidlView = viewHolder.get(id);
            if (chidlView == null) {
                chidlView = view.findViewById(id);
                viewHolder.put(id, chidlView);
            }
            return (T) chidlView;
        }
    }

    public static List<AppInfoTemplate> loadModules(Context context) {
        ArrayList<AppInfoTemplate> info = new ArrayList<>();
        String[] name = context.getResources().getStringArray(R.array.feeedback_label);
        TypedArray icon = context.getResources().obtainTypedArray(R.array.feedbak_icon);
        for (int i = 0; i < name.length; i++) {
            AppInfoTemplate single = new AppInfoTemplate();
            single.setAppName(name[i]);
            single.setAppIcon(context.getDrawable(icon.getResourceId(i, 0)));
            info.add(single);
        }
        icon.recycle();
        return info;
    }

}
