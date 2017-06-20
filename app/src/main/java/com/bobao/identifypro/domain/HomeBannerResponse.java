package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by star on 15/6/4.
 */
public class HomeBannerResponse extends BaseResponse {
    /**
     * type : expert
     * id : 41
     * image : http://c2c.ig365.cn/data/files/old_shop/gh_93/store_848093/jianbao/20160307/201603071635259126.jpg@400w_300h_1e_1c
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String type;
        private String id;
        private String image;

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getImage() {
            return image;
        }
    }
}
