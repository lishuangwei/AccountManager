package com.android.accountmanager.model;

import java.io.Serializable;

public class LoginTemplate implements Serializable {

    /**
     * data : {"token":"8ffd6b05db96446d9544b829696d7b67","userinfo":{"account":"","area":"","atype":0,"birthday":"","email":"","fullname":"","icon":"","id":"5a9a571e4e1ee82073282602","imei":"","sex":0,"tel":"13912430710"}}
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
         * token : 8ffd6b05db96446d9544b829696d7b67
         * userinfo : {"account":"","area":"","atype":0,"birthday":"","email":"","fullname":"","icon":"","id":"5a9a571e4e1ee82073282602","imei":"","sex":0,"tel":"13912430710"}
         */

        private String token;
        private UserinfoBean userinfo;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

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
             * id : 5a9a571e4e1ee82073282602
             * imei :
             * sex : 0
             * tel : 13912430710
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
