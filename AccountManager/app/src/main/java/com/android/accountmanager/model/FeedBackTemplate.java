package com.android.accountmanager.model;

import java.util.List;

/**
 * Created by root on 18-3-5.
 */

public class FeedBackTemplate {
    /**
     * data : [{"id":"5a787fae3f06b2138cc73db2","mticon":"http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg","mtname":"相机","mtno":"1"},{"id":"5a787fbe3f06b2138cc73db3","mticon":"http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg","mtname":"第三方应用","mtno":"2"},{"id":"5a964d054e1ee80a046cf7be","mticon":"http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg","mtname":"第三方应用","mtno":"2"},{"id":"5a97c0354e1ee80a046cf7c0","mticon":"http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg","mtname":"第三方应用","mtno":"2"},{"id":"5a98e9bf4e1ee80a046cf7c2","mticon":"http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg","mtname":"第三方应用","mtno":"2"}]
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
         * id : 5a787fae3f06b2138cc73db2
         * mticon : http://c.hiphotos.baidu.com/image/pic/item/91529822720e0cf3457ad8150146f21fbf09aa4b.jpg
         * mtname : 相机
         * mtno : 1
         */

        private String id;
        private String mticon;
        private String mtname;
        private String mtno;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMticon() {
            return mticon;
        }

        public void setMticon(String mticon) {
            this.mticon = mticon;
        }

        public String getMtname() {
            return mtname;
        }

        public void setMtname(String mtname) {
            this.mtname = mtname;
        }

        public String getMtno() {
            return mtno;
        }

        public void setMtno(String mtno) {
            this.mtno = mtno;
        }
    }
}
