package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by you on 2015/6/3.
 */
public class FeedBackResponse extends BaseResponse {

    /**
     * data : [{"belong":"0","user_id":"796991","addtime":"1425366351","send_type":1,"id":"1","content":"求交往"}]
     * error : false
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
         * belong : 0
         * user_id : 796991
         * addtime : 1425366351
         * send_type : 1
         * id : 1
         * content : 求交往
         */
        private String belong;
        private String user_id;
        private String addtime;
        private int send_type;
        private String id;
        private String content;

        public void setBelong(String belong) {
            this.belong = belong;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setSend_type(int send_type) {
            this.send_type = send_type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getBelong() {
            return belong;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public int getSend_type() {
            return send_type;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}
