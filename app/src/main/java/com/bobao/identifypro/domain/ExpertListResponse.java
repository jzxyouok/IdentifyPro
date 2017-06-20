package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/3/15.
 */
public class ExpertListResponse extends BaseResponse {

    /**
     * id : 41
     * name : 杨宝杰
     * kind : 杂项
     * org : 7,2
     * honors : 首都博物馆文物鉴定委员会委员
     * head_img : http://jianbao.artxun.com/external/modules/jbapp/templates/headimg/expert_41.jpg
     * star_level : 特聘
     * tui : 1
     * jbapp_pt : 0
     * jbapp_js : 0
     * jbapp_sp : 1
     * jbapp_yy : 1
     * pt_price : 0
     * js_price : 0
     * sp_price : 800
     * yy_price : 800
     * star_level_num : 6
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String name;
        private String kind;
        private String org;
        private String honors;
        private String head_img;
        private String star_level;
        private String tui;
        private String jbapp_pt;
        private String jbapp_js;
        private String jbapp_sp;
        private String jbapp_yy;
        private String pt_price;
        private String js_price;
        private String sp_price;
        private String yy_price;
        private String star_level_num;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public void setHonors(String honors) {
            this.honors = honors;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setStar_level(String star_level) {
            this.star_level = star_level;
        }

        public void setTui(String tui) {
            this.tui = tui;
        }

        public void setJbapp_pt(String jbapp_pt) {
            this.jbapp_pt = jbapp_pt;
        }

        public void setJbapp_js(String jbapp_js) {
            this.jbapp_js = jbapp_js;
        }

        public void setJbapp_sp(String jbapp_sp) {
            this.jbapp_sp = jbapp_sp;
        }

        public void setJbapp_yy(String jbapp_yy) {
            this.jbapp_yy = jbapp_yy;
        }

        public void setPt_price(String pt_price) {
            this.pt_price = pt_price;
        }

        public void setJs_price(String js_price) {
            this.js_price = js_price;
        }

        public void setSp_price(String sp_price) {
            this.sp_price = sp_price;
        }

        public void setYy_price(String yy_price) {
            this.yy_price = yy_price;
        }

        public void setStar_level_num(String star_level_num) {
            this.star_level_num = star_level_num;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getKind() {
            return kind;
        }

        public String getOrg() {
            return org;
        }

        public String getHonors() {
            return honors;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getStar_level() {
            return star_level;
        }

        public String getTui() {
            return tui;
        }

        public String getJbapp_pt() {
            return jbapp_pt;
        }

        public String getJbapp_js() {
            return jbapp_js;
        }

        public String getJbapp_sp() {
            return jbapp_sp;
        }

        public String getJbapp_yy() {
            return jbapp_yy;
        }

        public String getPt_price() {
            return pt_price;
        }

        public String getJs_price() {
            return js_price;
        }

        public String getSp_price() {
            return sp_price;
        }

        public String getYy_price() {
            return yy_price;
        }

        public String getStar_level_num() {
            return star_level_num;
        }
    }
}
