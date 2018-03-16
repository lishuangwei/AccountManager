package com.android.accountmanager.model;

import java.io.Serializable;

/**
 * Created by fantao on 18-2-8.
 */

public class ResultTemplate implements Serializable {
    private int resultCode;
    private String data;

    public ResultTemplate() {
    }

    public ResultTemplate(int resultCode, String data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public ResultTemplate setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getData() {
        return data;
    }

    public ResultTemplate setData(String data) {
        this.data = data;
        return this;
    }
}
