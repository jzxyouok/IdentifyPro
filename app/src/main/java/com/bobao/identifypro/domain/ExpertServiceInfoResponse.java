package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/3/16.
 */
public class ExpertServiceInfoResponse extends BaseResponse{
    /**
     * price : 15000-20000
     * name : 上门鉴定
     * unit : 天
     * id : 1
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String price;
        private String name;
        private String unit;
        private int id;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
