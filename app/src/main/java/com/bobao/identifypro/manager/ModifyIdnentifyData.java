package com.bobao.identifypro.manager;

import java.util.List;

/**
 * Created by you on 2015/7/14.
 */
public class ModifyIdnentifyData {

    /**
     * data : {"charge_type":"0","charge_other":"0","specify_expert_id":"0","is_jiaji":"0","source":"0","type":"2","specify_expert_star_level":"0","jb_reason":"","price":"0.00","product_id":"0","modified":"0","id":"494254","state":"0","guide":"","charge_time":"0","is_sale":"0","is_sendmail":"0","created":"1436842167","kind":"7","charge_price":"20.00","is_delete_time":"0","charge_edit_type":"1","jiaji_isvalid":"","user_id":"796991","phone":"13986045178","jb_score":"0","admin_id":"0","name":"","is_gujia":"0","size_info":"","charged":"0","charge_edit_time":"0","note":"  ","user_name":"artxun_15946556","reply_time":"0","jb_score_time":"0","recommend":"0","video":"","sid":"0","group_report":"","uip":"","jb_flower_to":"","jb_type":"1","deliver_time":"0","comments":"0","original":["","","",""],"photo":["","","",""],"mail_addr":"","is_delete":"0","jb_flower":"0","hits":"0","charge_num":"20.00","is_public":"0","report":"","updatetime":""}
     * error : false
     * message :
     */
    private DataEntity data;
    private boolean error;
    private String message;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {
        /**
         * charge_type : 0
         * charge_other : 0
         * specify_expert_id : 0
         * is_jiaji : 0
         * source : 0
         * type : 2
         * specify_expert_star_level : 0
         * jb_reason :
         * price : 0.00
         * product_id : 0
         * modified : 0
         * id : 494254
         * state : 0
         * guide :
         * charge_time : 0
         * is_sale : 0
         * is_sendmail : 0
         * created : 1436842167
         * kind : 7
         * charge_price : 20.00
         * is_delete_time : 0
         * charge_edit_type : 1
         * jiaji_isvalid :
         * user_id : 796991
         * phone : 13986045178
         * jb_score : 0
         * admin_id : 0
         * name :
         * is_gujia : 0
         * size_info :
         * charged : 0
         * charge_edit_time : 0
         * note :
         * user_name : artxun_15946556
         * reply_time : 0
         * jb_score_time : 0
         * recommend : 0
         * video :
         * sid : 0
         * group_report :
         * uip :
         * jb_flower_to :
         * jb_type : 1
         * deliver_time : 0
         * comments : 0
         * original : ["","","",""]
         * photo : ["","","",""]
         * serve_open: "0,0,1,1",
         * serve_price: "0,0,800,800"
         * mail_addr :
         * is_delete : 0
         * jb_flower : 0
         * hits : 0
         * charge_num : 20.00
         * is_public : 0
         * report :
         * updatetime :
         */
        private String charge_type;
        private String charge_other;
        private String specify_expert_id;
        private String is_jiaji;
        private String source;
        private String type;
        private String specify_expert_star_level;
        private String jb_reason;
        private String price;
        private String product_id;
        private String modified;
        private String id;
        private String state;
        private String guide;
        private String charge_time;
        private String is_sale;
        private String is_sendmail;
        private String created;
        private String kind;
        private String charge_price;
        private String is_delete_time;
        private String charge_edit_type;
        private String jiaji_isvalid;
        private String user_id;
        private String phone;
        private String jb_score;
        private String admin_id;
        private String name;
        private String is_gujia;
        private String size_info;
        private String charged;
        private String charge_edit_time;
        private String note;
        private String user_name;
        private String reply_time;
        private String jb_score_time;
        private String recommend;
        private String video;
        private String sid;
        private String group_report;
        private String uip;
        private String jb_flower_to;
        private String jb_type;
        private String serve_open;
        private String serve_price;
        private String expert_from;
        private String deliver_time;
        private String comments;
        private List<String> original;
        private List<String> photo;
        private String mail_addr;
        private String is_delete;
        private String jb_flower;
        private String hits;
        private String charge_num;
        private String is_public;
        private String report;
        private String updatetime;

        public void setCharge_type(String charge_type) {
            this.charge_type = charge_type;
        }

        public void setCharge_other(String charge_other) {
            this.charge_other = charge_other;
        }

        public void setSpecify_expert_id(String specify_expert_id) {
            this.specify_expert_id = specify_expert_id;
        }

        public void setIs_jiaji(String is_jiaji) {
            this.is_jiaji = is_jiaji;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSpecify_expert_star_level(String specify_expert_star_level) {
            this.specify_expert_star_level = specify_expert_star_level;
        }

        public void setJb_reason(String jb_reason) {
            this.jb_reason = jb_reason;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setGuide(String guide) {
            this.guide = guide;
        }

        public void setCharge_time(String charge_time) {
            this.charge_time = charge_time;
        }

        public void setIs_sale(String is_sale) {
            this.is_sale = is_sale;
        }

        public void setIs_sendmail(String is_sendmail) {
            this.is_sendmail = is_sendmail;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setCharge_price(String charge_price) {
            this.charge_price = charge_price;
        }

        public void setIs_delete_time(String is_delete_time) {
            this.is_delete_time = is_delete_time;
        }

        public void setCharge_edit_type(String charge_edit_type) {
            this.charge_edit_type = charge_edit_type;
        }

        public void setJiaji_isvalid(String jiaji_isvalid) {
            this.jiaji_isvalid = jiaji_isvalid;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setJb_score(String jb_score) {
            this.jb_score = jb_score;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIs_gujia(String is_gujia) {
            this.is_gujia = is_gujia;
        }

        public void setSize_info(String size_info) {
            this.size_info = size_info;
        }

        public void setCharged(String charged) {
            this.charged = charged;
        }

        public void setCharge_edit_time(String charge_edit_time) {
            this.charge_edit_time = charge_edit_time;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }

        public void setJb_score_time(String jb_score_time) {
            this.jb_score_time = jb_score_time;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setGroup_report(String group_report) {
            this.group_report = group_report;
        }

        public void setUip(String uip) {
            this.uip = uip;
        }

        public void setJb_flower_to(String jb_flower_to) {
            this.jb_flower_to = jb_flower_to;
        }

        public void setJb_type(String jb_type) {
            this.jb_type = jb_type;
        }

        public void setServe_open(String serve_open) {
            this.serve_open = serve_open;
        }

        public void setExpert_from(String expert_from){
            this.expert_from = expert_from;
        }

        public void setServe_price(String serve_price){
            this.serve_price = serve_price;
        }

        public void setDeliver_time(String deliver_time) {
            this.deliver_time = deliver_time;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public void setOriginal(List<String> original) {
            this.original = original;
        }

        public void setPhoto(List<String> photo) {
            this.photo = photo;
        }

        public void setMail_addr(String mail_addr) {
            this.mail_addr = mail_addr;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        public void setJb_flower(String jb_flower) {
            this.jb_flower = jb_flower;
        }

        public void setHits(String hits) {
            this.hits = hits;
        }

        public void setCharge_num(String charge_num) {
            this.charge_num = charge_num;
        }

        public void setIs_public(String is_public) {
            this.is_public = is_public;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getCharge_type() {
            return charge_type;
        }

        public String getCharge_other() {
            return charge_other;
        }

        public String getSpecify_expert_id() {
            return specify_expert_id;
        }

        public String getIs_jiaji() {
            return is_jiaji;
        }

        public String getSource() {
            return source;
        }

        public String getType() {
            return type;
        }

        public String getSpecify_expert_star_level() {
            return specify_expert_star_level;
        }

        public String getJb_reason() {
            return jb_reason;
        }

        public String getPrice() {
            return price;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getModified() {
            return modified;
        }

        public String getId() {
            return id;
        }

        public String getState() {
            return state;
        }

        public String getGuide() {
            return guide;
        }

        public String getCharge_time() {
            return charge_time;
        }

        public String getIs_sale() {
            return is_sale;
        }

        public String getIs_sendmail() {
            return is_sendmail;
        }

        public String getCreated() {
            return created;
        }

        public String getKind() {
            return kind;
        }

        public String getCharge_price() {
            return charge_price;
        }

        public String getIs_delete_time() {
            return is_delete_time;
        }

        public String getCharge_edit_type() {
            return charge_edit_type;
        }

        public String getJiaji_isvalid() {
            return jiaji_isvalid;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getPhone() {
            return phone;
        }

        public String getJb_score() {
            return jb_score;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public String getName() {
            return name;
        }

        public String getIs_gujia() {
            return is_gujia;
        }

        public String getSize_info() {
            return size_info;
        }

        public String getCharged() {
            return charged;
        }

        public String getCharge_edit_time() {
            return charge_edit_time;
        }

        public String getNote() {
            return note;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getReply_time() {
            return reply_time;
        }

        public String getJb_score_time() {
            return jb_score_time;
        }

        public String getRecommend() {
            return recommend;
        }

        public String getVideo() {
            return video;
        }

        public String getSid() {
            return sid;
        }

        public String getGroup_report() {
            return group_report;
        }

        public String getUip() {
            return uip;
        }

        public String getJb_flower_to() {
            return jb_flower_to;
        }

        public String getJb_type() {
            return jb_type;
        }

        public String getServe_open() {
            return serve_open;
        }

        public String getExpert_from(){
            return expert_from;
        }

        public String getServe_price(){
            return serve_price;
        }

        public String getDeliver_time() {
            return deliver_time;
        }

        public String getComments() {
            return comments;
        }

        public List<String> getOriginal() {
            return original;
        }

        public List<String> getPhoto() {
            return photo;
        }

        public String getMail_addr() {
            return mail_addr;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public String getJb_flower() {
            return jb_flower;
        }

        public String getHits() {
            return hits;
        }

        public String getCharge_num() {
            return charge_num;
        }

        public String getIs_public() {
            return is_public;
        }

        public String getReport() {
            return report;
        }

        public String getUpdatetime() {
            return updatetime;
        }
    }
}
