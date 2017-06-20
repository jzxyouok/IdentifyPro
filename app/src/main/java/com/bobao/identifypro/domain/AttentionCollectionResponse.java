package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/6/3.
 */
public class AttentionCollectionResponse extends BaseResponse {

    /**
     * data : 关注成功！
     * collect : true
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String data;
        private boolean collect;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }
    }
}
