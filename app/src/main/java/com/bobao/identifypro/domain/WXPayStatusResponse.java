package com.bobao.identifypro.domain;

/**
 * Created by star on 16/3/23.
 */
public class WXPayStatusResponse extends BaseResponse {
    /**
     * charged : 1
     * isPay : 0
     * timeout : 0
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String charged;
        private int isPay;
        private int timeout;

        public void setCharged(String charged) {
            this.charged = charged;
        }

        public void setIsPay(int isPay) {
            this.isPay = isPay;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public String getCharged() {
            return charged;
        }

        public int getIsPay() {
            return isPay;
        }

        public int getTimeout() {
            return timeout;
        }
    }
}
