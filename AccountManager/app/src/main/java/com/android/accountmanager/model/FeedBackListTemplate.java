package com.android.accountmanager.model;

import java.util.List;

/**
 * Created by root on 18-3-6.
 */

public class FeedBackListTemplate {
    /**
     * data : [{"account":"13682449162","mtcontent":"测试反馈1","mtdate":"2012-12-12","mtname":"相机"},{"account":"13682449162","mtcontent":"测试反馈1","mtdate":"2012-12-12","mtname":"相机"},{"account":"13682449162","mtcontent":"测试反馈1","mtdate":"2012-12-12","mtname":"相机"},{"account":"13682449162","mtcontent":"测试反馈1","mtdate":"2012-12-12","mtname":"相机"}]
     * resultCode : 1
     */

    private int resultCode;
    private List<DataBean> data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : 13682449162
         * mtcontent : 测试反馈1
         * mtdate : 2012-12-12
         * mtname : 相机
         */

        private String account;
        private String mtcontent;
        private String mtdate;
        private String mtname;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getMtcontent() {
            return mtcontent;
        }

        public void setMtcontent(String mtcontent) {
            this.mtcontent = mtcontent;
        }

        public String getMtdate() {
            return mtdate;
        }

        public void setMtdate(String mtdate) {
            this.mtdate = mtdate;
        }

        public String getMtname() {
            return mtname;
        }

        public void setMtname(String mtname) {
            this.mtname = mtname;
        }
    }
}
