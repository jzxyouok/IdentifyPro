package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class ResumeDetailResponse extends BaseResponse {

    /**
     * money : 15.78
     * list : [{"from":"enter","time":"2016-03-30 15:23","price":"0.01","type_name":"微信充值","note":"使用微信充值充值0.01元"},{"from":"enter","time":"2016-03-30 15:21","price":"0.01","type_name":"微信充值","note":"使用微信充值充值0.01元"},{"from":"enter","time":"2015-05-27 12:02","price":"0.01","type_name":"微信充值","note":"使用微信充值充值0.01元"}]
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private double money;
        /**
         * from : enter
         * time : 2016-03-30 15:23
         * price : 0.01
         * type_name : 微信充值
         * note : 使用微信充值充值0.01元
         */

        private List<ListEntity> list;

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity {
            private String from;
            private String time;
            private String price;
            private String type_name;
            private String note;

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }
        }
    }
}
