package com.android.accountmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.SparseArray;
import android.view.View;

import java.util.Iterator;
import java.util.List;

public class FeedBackUtils {
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

}
