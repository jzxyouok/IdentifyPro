package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by star on 15/6/5.
 */
public class HomeOrderResponse {
    /**
     * data : [{"user_id":"875526","created":"2015-06-05","user_name":"artxun_159..","head_img":"","width":200,"photo":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/2b873dcf2c49790f3cbeca4032f32708/5ncGQuMjU5NjEwMjA3NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamYvMjU1Nz84ZV9ydG9zNiUyXzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491666","height":149},{"user_id":"875526","created":"2015-06-05","user_name":"artxun_159..","head_img":"","width":200,"photo":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/9ddefe6fafd0f5462fde0184017a6d67/5ncGguMTEwMjc1MTA3NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamYvMjU1Nz84ZV9ydG9zNiUyXzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491665","height":149},{"user_id":"875526","created":"2015-06-05","user_name":"artxun_159..","head_img":"","width":200,"photo":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/b3e8eb79147cfb1675dfca9f8400dcfc/5ncGEuOTk2OTIxMTA3NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamYvMjU1Nz84ZV9ydG9zNiUyXzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491664","height":149},{"user_id":"878192","created":"2015-06-05","user_name":"大侠","head_img":"","width":200,"photo":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/8112c5c0dfd10b0d5c6294cc7dfa5f1a/5ncGEuOTQ4MTkyNTA2NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvOTgxNz84ZV9ydG9zMiE5XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491663","height":112},{"user_id":"878192","created":"2015-06-05","user_name":"大侠","head_img":"","width":200,"photo":"http://img403.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/ca5e49ddf42c433b4f48b86021f9fbbe/5ncGEuMjI4NDYzNDA2NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvOTgxNz84ZV9ydG9zMiE5XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491662","height":112},{"user_id":"878192","created":"2015-06-05","user_name":"大侠","head_img":"","width":200,"photo":"http://img401.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/558042470e3dae26d90d997bee8e2730/5ncGYuNzEzODg0MzA2NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvOTgxNz84ZV9ydG9zMiE5XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491661","height":112},{"user_id":"878372","created":"2015-06-05","user_name":"薄晓强","head_img":"http://img402.artxun.com/pictures/a830c265d225500d188909529c95f57a/be8989842eebe7bc0c34713049b5bc10/A=LzhGV2JWNElSa3FTWFJFNXk2WndFUHJlaWtTREYwMkxCb0VZYm9DaGliZGV1OFA1YVZpdGNmcTliVGd0YlxBTUg0V0p4MlpOVGBXenJzVDFkajc3MFkzRFVITHpUa19NOEN0RX9ZbiBlb31tL2NuLmdvb2FsLnd4L3ovcDR0aH/thumbnail_92_92.jpg","width":200,"photo":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/b84427075ecc073436f04bf11d96f7df/5ncGQuMjMwNDIwMzA2NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvNzgzNz84ZV9ydG9zMiM3XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491660","height":150},{"user_id":"878192","created":"2015-06-05","user_name":"大侠","head_img":"","width":200,"photo":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/0fb9cb8870eb96c99f4c2609cef15bd0/5ncGAuMTc1Mjc0MTA2NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvOTgxNz84ZV9ydG9zMiE5XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491658","height":112},{"user_id":"878192","created":"2015-06-05","user_name":"大侠","head_img":"","width":200,"photo":"http://img402.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/99e15be97ebe73eb9535f473e9c762fc/5ncGcuNDQ5NzgxNTA1NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamIvOTgxNz84ZV9ydG9zMiE5XzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491657","height":112},{"user_id":"748323","created":"2015-06-05","user_name":"车韦810","head_img":"","width":112,"photo":"http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/0aeeef7d34fefe691a994e6c349535d4/5ncGEuNjM5NDkwMzA1NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamMvMjgzND83ZV9ydG9zMyMyXzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg","id":"491656","height":200}]
     * error : false
     * message : 82765
     */
    private List<DataEntity> data;
    private boolean error;
    private String message;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
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
         * user_id : 875526
         * created : 2015-06-05
         * user_name : artxun_159..
         * head_img :
         * width : 200
         * photo : http://img4.artxun.com/pictures/8920bd42913f3f18d0d8a3367badb704/2b873dcf2c49790f3cbeca4032f32708/5ncGQuMjU5NjEwMjA3NTYwMDE1MD8yNSYwMDE1MD8ybyJhbmlhamYvMjU1Nz84ZV9ydG9zNiUyXzdoL29waG9zZF9sL2VzbGZpL2RhYX9kbSNvLmVueHJ0YX8uYW5iYWppL2ovcDR0aH/thumbnail_400_400.jpg
         * id : 491666
         * height : 149
         */
        private String user_id;
        private String created;
        private String user_name;
        private String head_img;
        private int width;
        private String photo;
        private String id;
        private int height;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getCreated() {
            return created;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getHead_img() {
            return head_img;
        }

        public int getWidth() {
            return width;
        }

        public String getPhoto() {
            return photo;
        }

        public String getId() {
            return id;
        }

        public int getHeight() {
            return height;
        }
    }
}
