package com.android.accountmanager.model;

/**
 * Created by root on 18-3-6.
 */

public class UserUpdateTemplate {
    /**
     * data : {"userinfo":{"account":"","area":"","atype":0,"birthday":"","email":"","fullname":"","icon":"","id":"5a9cf7bb4e1ee8207328260a","imei":"","password":"db2b6e4e774a656aac18f3fecec80f65","sex":0,"tel":"15921260529"}}
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
         * userinfo : {"account":"","area":"","atype":0,"birthday":"","email":"","fullname":"","icon":"","id":"5a9cf7bb4e1ee8207328260a","imei":"","password":"db2b6e4e774a656aac18f3fecec80f65","sex":0,"tel":"15921260529"}
         */

        private UserinfoBean userinfo;

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoBean {
            /**
             * account :
             * area :
             * atype : 0
             * birthday :
             * email :
             * fullname :
             * icon :
             * id : 5a9cf7bb4e1ee8207328260a
             * imei :
             * password : db2b6e4e774a656aac18f3fecec80f65
             * sex : 0
             * tel : 15921260529
             */

            private String account;
            private String area;
            private int atype;
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

            public int getAtype() {
                return atype;
            }

            public void setAtype(int atype) {
                this.atype = atype;
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
}
