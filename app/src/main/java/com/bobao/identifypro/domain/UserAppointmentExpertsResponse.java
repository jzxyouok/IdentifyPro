package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/4/1.
 */
public class UserAppointmentExpertsResponse extends BaseResponse {

    /**
     * id : 1
     * user_id : 796991
     * serve : 视频鉴定
     * note : 保真
     * eid : 4
     * addtime : 1459322628
     * apply_time : 预约时间：2015年06月25日
     * name : 黄秀纯
     * honors : 北京文物研究所副研究员
     * head_img : http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_4.jpg
     * type_img : http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png
     * timeout : 0
     * refund : [{"name":"20.00","time":"1448528190"}]
     * iscomment : 1
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
        private String user_id;
        private String serve;
        private String note;
        private String eid;
        private String addtime;
        private String apply_time;
        private String name;
        private String honors;
        private String head_img;
        private String type_img;
        private int timeout;
        private int iscomment;
        /**
         * name : 20.00
         * time : 1448528190
         */

        private List<RefundEntity> refund;

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

        public String getServe() {
            return serve;
        }

        public void setServe(String serve) {
            this.serve = serve;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getEid() {
            return eid;
        }

        public void setEid(String eid) {
            this.eid = eid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getApply_time() {
            return apply_time;
        }

        public void setApply_time(String apply_time) {
            this.apply_time = apply_time;
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

        public String getType_img() {
            return type_img;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getIscomment() {
            return iscomment;
        }

        public void setIscomment(int iscomment) {
            this.iscomment = iscomment;
        }

        public List<RefundEntity> getRefund() {
            return refund;
        }

        public void setRefund(List<RefundEntity> refund) {
            this.refund = refund;
        }

        public static class RefundEntity {
            private String name;
            private String time;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
