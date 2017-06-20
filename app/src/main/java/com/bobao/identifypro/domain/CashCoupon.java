package com.bobao.identifypro.domain;

/**
 * Created by star on 15/9/21.
 */
public class CashCoupon extends BaseResponse {
    /**
     * error : false
     * data : {"status":"ok","amount":"200"}
     * message :
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * status : ok
         * amount : 200
         */

        private String status;
        private String amount;

        public void setStatus(String status) {
            this.status = status;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public String getAmount() {
            return amount;
        }
    }
}
