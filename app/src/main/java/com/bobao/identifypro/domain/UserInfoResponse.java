package com.bobao.identifypro.domain;

/**
 * Created by Fang on 2016/3/29.
 */
public class UserInfoResponse extends BaseResponse{
    /**
     * user_data : {"user_id":"796991","user_name":"artxun_15946556","nikename":"小花","head_img":"http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20151010/201510101048145463.jpg@400w_400h"}
     * pro_goods : {"wait":"1","conduct":"0","complete":"0"}
     * feedback : 62
     * verName : 1.4.39
     * verCode : 65
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * user_id : 796991
         * user_name : artxun_15946556
         * nikename : 小花
         * head_img : http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20151010/201510101048145463.jpg@400w_400h
         */

        private UserDataEntity user_data;
        /**
         * wait : 1
         * conduct : 0
         * complete : 0
         */

        private ProGoodsEntity pro_goods;
        private String feedback;
        private String verName;
        private String verCode;

        public UserDataEntity getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataEntity user_data) {
            this.user_data = user_data;
        }

        public ProGoodsEntity getPro_goods() {
            return pro_goods;
        }

        public void setPro_goods(ProGoodsEntity pro_goods) {
            this.pro_goods = pro_goods;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getVerName() {
            return verName;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public String getVerCode() {
            return verCode;
        }

        public void setVerCode(String verCode) {
            this.verCode = verCode;
        }

        public static class UserDataEntity {
            private String user_id;
            private String user_name;
            private String nikename;
            private String head_img;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getNikename() {
                return nikename;
            }

            public void setNikename(String nikename) {
                this.nikename = nikename;
            }

            public String getHead_img() {
                return head_img;
            }

            public void setHead_img(String head_img) {
                this.head_img = head_img;
            }
        }

        public static class ProGoodsEntity {
            private String wait;
            private String conduct;
            private String complete;

            public String getWait() {
                return wait;
            }

            public void setWait(String wait) {
                this.wait = wait;
            }

            public String getConduct() {
                return conduct;
            }

            public void setConduct(String conduct) {
                this.conduct = conduct;
            }

            public String getComplete() {
                return complete;
            }

            public void setComplete(String complete) {
                this.complete = complete;
            }
        }
    }
}
