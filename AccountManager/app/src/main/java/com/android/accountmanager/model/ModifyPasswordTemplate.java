package com.android.accountmanager.model;

/**
 * Created by root on 18-3-22.
 */

public class ModifyPasswordTemplate {

    /**
     * data : {"account":"717413443","area":"","birthday":"1995-03-16","email":"","fullname":"jiaohddjdj","icon":"content://com.android.accountmanager.files/my_cache/AccountPhoto-IMG_20180322_151115-cropped.jpg","id":"5aa7b7474e1ee84ecf8da29e","imei":"","password":"eabd8ce9404507aa8c22714d3f5eada9","sex":0,"tel":"13912430000"}
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
         * account : 717413443
         * area :
         * birthday : 1995-03-16
         * email :
         * fullname : jiaohddjdj
         * icon : content://com.android.accountmanager.files/my_cache/AccountPhoto-IMG_20180322_151115-cropped.jpg
         * id : 5aa7b7474e1ee84ecf8da29e
         * imei :
         * password : eabd8ce9404507aa8c22714d3f5eada9
         * sex : 0
         * tel : 13912430000
         */

        private String account;
        private String area;
        private String birthday;
        private String email;
        private String fullname;
        private String icon;
        private String id;
        private String imei;
        private String password;
        private int sex;
        private String tel;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
