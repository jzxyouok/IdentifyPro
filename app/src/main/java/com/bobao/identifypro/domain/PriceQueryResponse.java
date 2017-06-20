package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by you on 2015/6/17.
 */
public class PriceQueryResponse extends BaseResponse{

    /**
     * data : [{"ask_addtime":"2015-06-16","ask_content":"几毛钱？","price":"0.00","report":"真的是真的","photo":"","from":"881902","id":"491910","state":"真","ask_user_name":"i-Feel","ask_head_img":""}]
     * error : false
     * message : 1
     */
    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity {
        /**
         * ask_addtime : 2015-06-16
         * ask_content : 几毛钱？
         * price : 0.00
         * report : 真的是真的
         * photo :
         * from : 881902
         * to:
         * id : 491910
         * state : 真
         * ask_user_name : i-Feel
         * ask_head_img :
         */
        private String ask_addtime;
        private String ask_content;
        private String price;
        private String report;
        private String photo;
        private String from;
        private String to;

        private String id;
        private String state;
        private String ask_user_name;
        private String ask_head_img;

        public void setAsk_addtime(String ask_addtime) {
            this.ask_addtime = ask_addtime;
        }

        public void setAsk_content(String ask_content) {
            this.ask_content = ask_content;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setAsk_user_name(String ask_user_name) {
            this.ask_user_name = ask_user_name;
        }

        public void setAsk_head_img(String ask_head_img) {
            this.ask_head_img = ask_head_img;
        }

        public String getAsk_addtime() {
            return ask_addtime;
        }

        public String getAsk_content() {
            return ask_content;
        }

        public String getPrice() {
            return price;
        }

        public String getReport() {
            return report;
        }

        public String getPhoto() {
            return photo;
        }

        public String getFrom() {
            return from;
        }

        public String getId() {
            return id;
        }

        public String getState() {
            return state;
        }

        public String getAsk_user_name() {
            return ask_user_name;
        }

        public String getAsk_head_img() {
            return ask_head_img;
        }
    }
}
