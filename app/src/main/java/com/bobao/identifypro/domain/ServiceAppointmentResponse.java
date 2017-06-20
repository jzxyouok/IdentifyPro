package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/3/14.
 */
public class ServiceAppointmentResponse extends BaseResponse {

    /**
     * list : [{"name":"李知宴","org":"国家博物馆","price":"1800","time":"件"},{"name":"叶佩兰","org":"天下收藏 ","price":"5000","time":"件"},{"name":"张宁","org":"首都博物馆","price":"1800","time":"件"},{"name":"杨静荣","org":"一槌定音","price":"8000","time":"件"},{"name":"陈润民","org":"故宫博物院","price":"1500","time":"件"},{"name":"李宗杨","org":"北京文物局","price":"1500","time":"件"},{"name":"张广文","org":"一槌定音","price":"1500","time":"件"},{"name":"张晓晖","org":"学院协会","price":"1500","time":"件"},{"name":"单国强","org":"故宫博物院","price":"1800","time":"件"},{"name":"刘静","org":"天下收藏 ","price":"1800","time":"件"},{"name":"张如兰","org":"北京文物局","price":"1800","time":"件"},{"name":"戴志强","org":"博物馆社科院","price":"1500","time":"件"},{"name":"杨宝杰","org":"一槌定音","price":"1500","time":"件"},{"name":"须小龙","org":"博物馆社科院","price":"1500","time":"件"},{"name":"李晨","org":"北京文物局","price":"1500","time":"件"},{"name":"孔晨","org":"故宫博物院","price":"1500","time":"件"},{"name":"张淑芬","org":"天下收藏 ","price":"1500","time":"件"},{"name":"邓丁三","org":"一槌定音","price":"1800","time":"件"},{"name":"宋海洋","org":"故宫博物院","price":"1500","time":"件"},{"name":"王培伍","org":"博物馆社科院","price":"1500","time":"件"}]
     * tui : {"price":"10000","time":"件"}
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * price : 10000
         * time : 件
         */

        private TuiEntity tui;
        /**
         * name : 李知宴
         * org : 国家博物馆
         * price : 1800
         * time : 件
         */

        private List<ListEntity> list;

        public void setTui(TuiEntity tui) {
            this.tui = tui;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public TuiEntity getTui() {
            return tui;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class TuiEntity {
            private String price;
            private String time;

            public void setPrice(String price) {
                this.price = price;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPrice() {
                return price;
            }

            public String getTime() {
                return time;
            }
        }

        public static class ListEntity {
            private String id;
            private String name;
            private String org;
            private String price;
            private String time;

            public ListEntity(String id, String name,String price, String time) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.time = time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOrg(String org) {
                this.org = org;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getName() {
                return name;
            }

            public String getOrg() {
                return org;
            }

            public String getPrice() {
                return price;
            }

            public String getTime() {
                return time;
            }
        }
    }
}
