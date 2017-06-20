package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/4/1.
 */
public class PriceQueryContentResponse extends BaseResponse {

    /**
     * data : [{"isme":0,"user_id":"","user_name":"","addtime":"10:57","head_img":"","id":"","type":0,"content":""},{"isme":0,"user_id":"881902","user_name":"i-Feel","addtime":"10:57","head_img":"","id":"1","type":1,"content":"三毛卖不卖"}]
     * error : false
     * message : 4
     */
    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * isme : 0
         * user_id :
         * user_name :
         * addtime : 10:57
         * head_img :
         * id :
         * type : 0
         * content :
         */
        private int isme;
        private String user_id;
        private String user_name;
        private String addtime;
        private String head_img;
        private String id;
        private int type;
        private String content;

        public void setIsme(int isme) {
            this.isme = isme;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsme() {
            return isme;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}
