package com.android.accountmanager.model;

import com.android.accountmanager.utils.JackSonUtil;

/**
 * Created by fantao on 18-2-8.
 */

public class TemplateUtils {
    public static LoginTemplate.DataBean.UserinfoBean getUserInfoFromResult(LoginTemplate resultTemplate) {
        return resultTemplate.getData().getUserinfo();
    }

    public static DataTemplate getDataFromResult(ResultTemplate resultTemplate) {
        String data = resultTemplate.getData();
        return JackSonUtil.json2Obj(data, DataTemplate.class);
    }
}
