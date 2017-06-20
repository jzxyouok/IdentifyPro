package com.bobao.identifypro.domain;

/**
 * Created by Fang on 2016/4/12.
 */
public class MyIntegrateResponse extends BaseResponse{
    /**
     * user_id : 796991
     * integral : 17
     * edition : 1
     * slogan : 积分当钱花，10积分=1元，多攒一些吧！
     * rule : 10积分可抵扣1元，且积分按照10的倍数使用。例：普通鉴定5元，账户内有25积分，则可以使用20积分抵扣2元。/n每单可使用积分抵扣40%鉴定费用。例：极速鉴定20元，最多可使用80积分抵扣8元。/n新用户首次可使用赠送的50积分全额支付一次普通鉴定。/n用户积分无使用期限。
     * mode : 手机首次登录手机鉴宝并注册可获得50积分。/n成功分享晒单、专家、资讯即可获得5积分（每日限3次）/n每充值10元可获得5积分。例充值100元获得50积分。/n成功邀请一位好友获得50积分。（对方注册登录后输入邀请码方可生效，可无限邀请）
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String user_id;
        private String integral;
        private int edition;
        private String slogan;
        private String rule;
        private String mode;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public int getEdition() {
            return edition;
        }

        public void setEdition(int edition) {
            this.edition = edition;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
