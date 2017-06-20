package com.bobao.identifypro.domain;

/**
 * Created by Fang on 2016/3/14.
 */
public class EditServiceAppointmentResponse extends BaseResponse {


    /**
     * id : 2
     * user_id : 984636
     * type : 1
     * serve : 2
     * kind : 2
     * size : 1
     * note : 保真
     * charge_price : 1000.00
     * type_name : 自选专家
     * kind_name : 玉器
     * serve_name : 视频鉴定
     * price : 1800
     * time : 件
     * eid : 1
     * name : 李知宴
     * honors :
     * head_img :
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String user_id;
        private String type;
        private String serve;
        private String kind;
        private String size;
        private String note;
        private String charge_price;
        private String type_name;
        private String kind_name;
        private String serve_name;
        private String price;
        private String time;
        private String eid;
        private String name;
        private String honors;
        private String head_img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getServe() {
            return serve;
        }

        public void setServe(String serve) {
            this.serve = serve;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getCharge_price() {
            return charge_price;
        }

        public void setCharge_price(String charge_price) {
            this.charge_price = charge_price;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getKind_name() {
            return kind_name;
        }

        public void setKind_name(String kind_name) {
            this.kind_name = kind_name;
        }

        public String getServe_name() {
            return serve_name;
        }

        public void setServe_name(String serve_name) {
            this.serve_name = serve_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getEid() {
            return eid;
        }

        public void setEid(String eid) {
            this.eid = eid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHonors() {
            return honors;
        }

        public void setHonors(String honors) {
            this.honors = honors;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }
    }
}
