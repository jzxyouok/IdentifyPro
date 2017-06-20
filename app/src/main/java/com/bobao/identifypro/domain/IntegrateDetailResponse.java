package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/4/12.
 */
public class IntegrateDetailResponse extends BaseResponse{
    /**
     * id : 205955
     * note : 积分置换礼品
     * num : -5
     * addtime : 2014-11-20 08:54:04
     * state : 1
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String note;
        private String num;
        private String addtime;
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
