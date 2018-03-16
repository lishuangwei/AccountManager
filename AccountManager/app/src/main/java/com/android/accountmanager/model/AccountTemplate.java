package com.android.accountmanager.model;

import java.io.Serializable;

public class AccountTemplate implements Serializable {

    /**
     * data : {"account":"677780127"}
     * resultCode : 1
     */

    private DataBean data;
    private int resultCode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public static class DataBean {
        /**
         * account : 677780127
         */

        private String account;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
