package com.bobao.identifypro.domain;

/**
 * Created by star on 15/6/29.
 */
public class LoginStepResponse extends BaseResponse {
    /**
     * user_id : 796991
     * user_name : 小花
     * token : 5f9arFuZe2CMvFI6oWLHz0qYpMQ9x7ZWmlwue8FH%2BKb9BIwi%2FJhHlg8WVqbBPNg%2B1%2BnC
     * headimg : http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20151010/201510101048145463.jpg@92w_92h
     * mobile : 13141114229
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int user_id;
        private String user_name;
        private String token;
        private String headimg;
        private String mobile;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
