package com.bobao.identifypro.domain;

/**
 * Created by Fang on 2016/4/11.
 */
public class GetOrderIdResponse extends BaseResponse{

    /**
     * id : 29
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
