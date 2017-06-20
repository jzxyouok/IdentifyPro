package com.bobao.identifypro.domain;

import java.util.List;

/**
 * Created by Fang on 2016/4/11.
 */
public class UserIdentifyPayInfoResponse extends BaseResponse{

    /**
     * goods : {"id":"184","user_id":"984636","charge_price":"0.00","charged":"1","serve":"2","serve_name":{"serve":"视频鉴定","type_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png"}}
     * money : 0.01
     * paylist : [{"id":"0","name":"余额支付","note":"用户财务中心余额支付"},{"id":"2","name":"支付宝支付","note":"支付宝快捷支付"}]
     * payment_type : {"MALIPAY":1,"WXPAY":0,"BAIFUBAO_WAP":0}
     * is_new_user : 0
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * id : 184
         * user_id : 984636
         * charge_price : 0.00
         * charged : 1
         * serve : 2
         * serve_name : {"serve":"视频鉴定","type_img":"http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png"}
         */

        private GoodsEntity goods;
        private double money;
        /**
         * MALIPAY : 1
         * WXPAY : 0
         * BAIFUBAO_WAP : 0
         */

        private PaymentTypeEntity payment_type;
        private int is_new_user;
        /**
         * id : 0
         * name : 余额支付
         * note : 用户财务中心余额支付
         */

        private List<PaylistEntity> paylist;

        public GoodsEntity getGoods() {
            return goods;
        }

        public void setGoods(GoodsEntity goods) {
            this.goods = goods;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public PaymentTypeEntity getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(PaymentTypeEntity payment_type) {
            this.payment_type = payment_type;
        }

        public int getIs_new_user() {
            return is_new_user;
        }

        public void setIs_new_user(int is_new_user) {
            this.is_new_user = is_new_user;
        }

        public List<PaylistEntity> getPaylist() {
            return paylist;
        }

        public void setPaylist(List<PaylistEntity> paylist) {
            this.paylist = paylist;
        }

        public static class GoodsEntity {
            private String id;
            private String user_id;
            private String charge_price;
            private String charged;
            private String serve;
            /**
             * serve : 视频鉴定
             * type_img : http://jianbao.artxun.com/external/modules/jbapp/templates/images/type_1.png
             */

            private ServeNameEntity serve_name;

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

            public String getCharge_price() {
                return charge_price;
            }

            public void setCharge_price(String charge_price) {
                this.charge_price = charge_price;
            }

            public String getCharged() {
                return charged;
            }

            public void setCharged(String charged) {
                this.charged = charged;
            }

            public String getServe() {
                return serve;
            }

            public void setServe(String serve) {
                this.serve = serve;
            }

            public ServeNameEntity getServe_name() {
                return serve_name;
            }

            public void setServe_name(ServeNameEntity serve_name) {
                this.serve_name = serve_name;
            }

            public static class ServeNameEntity {
                private String serve;
                private String type_img;

                public String getServe() {
                    return serve;
                }

                public void setServe(String serve) {
                    this.serve = serve;
                }

                public String getType_img() {
                    return type_img;
                }

                public void setType_img(String type_img) {
                    this.type_img = type_img;
                }
            }
        }

        public static class PaymentTypeEntity {
            private int MALIPAY;
            private int WXPAY;
            private int BAIFUBAO_WAP;

            public int getMALIPAY() {
                return MALIPAY;
            }

            public void setMALIPAY(int MALIPAY) {
                this.MALIPAY = MALIPAY;
            }

            public int getWXPAY() {
                return WXPAY;
            }

            public void setWXPAY(int WXPAY) {
                this.WXPAY = WXPAY;
            }

            public int getBAIFUBAO_WAP() {
                return BAIFUBAO_WAP;
            }

            public void setBAIFUBAO_WAP(int BAIFUBAO_WAP) {
                this.BAIFUBAO_WAP = BAIFUBAO_WAP;
            }
        }

        public static class PaylistEntity {
            private String id;
            private String name;
            private String note;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }
        }
    }
}
