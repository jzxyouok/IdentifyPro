package com.bobao.identifypro.domain;

/**
 * Created by Fang on 2016/3/10.
 */
public class ServiceNoteResponse extends BaseResponse {
    /**
     * intro : 1上门鉴定是专家去客户家登门鉴定 当场给与藏品鉴定意见 不限藏品件数 对于家中有众多藏品或者藏品不宜移动的藏家较为合适。等等等等等等
     * duration : 14~6小时
     * response : 1工作日内回应
     * phone : 18911901129
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String intro;
        private String duration;
        private String response;
        private String phone;
        private String meet_time;

        public void setMeet_time(String meet_time){
            this.meet_time = meet_time;
        }

        public String getMeet_time(){
            return meet_time;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIntro() {
            return intro;
        }

        public String getDuration() {
            return duration;
        }

        public String getResponse() {
            return response;
        }

        public String getPhone() {
            return phone;
        }
    }
}
