package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by kakaxicm on 2015/12/15.
 */
public class DealRecordReponse extends BaseResponse {
    /**
     * from : out
     * time : 2015-10-16
     * price :
     * type_name : 急速鉴定
     * note : 支付0.00元使用0积分
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String from;
        private String time;
        private String price;
        private String type_name;
        private String note;

        public void setFrom(String from) {
            this.from = from;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getFrom() {
            return from;
        }

        public String getTime() {
            return time;
        }

        public String getPrice() {
            return price;
        }

        public String getType_name() {
            return type_name;
        }

        public String getNote() {
            return note;
        }
    }
}
