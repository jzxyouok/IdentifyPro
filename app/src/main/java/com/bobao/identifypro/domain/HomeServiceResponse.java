package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/3/11.
 */
public class HomeServiceResponse extends BaseResponse {

    /**
     * id : 5
     * bg_image : http://c2c.ig365.cn/data/files/old_shop/gh_779/store_470779/jianbao/20160114/201601141101158875.png
     * font_img : http://c2c.ig365.cn/data/files/old_shop/gh_779/store_470779/jianbao/20160114/201601141101158875.png
     * name : 专家团
     * note : 三们专家为您会审
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
        private String bg_image;
        private String font_img;
        private String name;
        private String note;

        public void setId(String id) {
            this.id = id;
        }

        public void setBg_image(String bg_image) {
            this.bg_image = bg_image;
        }

        public void setFont_img(String font_img) {
            this.font_img = font_img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getId() {
            return id;
        }

        public String getBg_image() {
            return bg_image;
        }

        public String getFont_img() {
            return font_img;
        }

        public String getName() {
            return name;
        }

        public String getNote() {
            return note;
        }
    }
}
