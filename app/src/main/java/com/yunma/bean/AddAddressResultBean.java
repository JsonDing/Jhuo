package com.yunma.bean;

import java.io.Serializable;

/**
 * Created on 2017-02-21
 *
 * @author Json.
 */

public class AddAddressResultBean implements Serializable {

    /**
     * success : {"id":7,"name":"老王","tel":123456,"regoin":"北京","addr":"天安门","userId":61}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable {
        /**
         * id : 7
         * name : 老王
         * tel : 123456
         * regoin : 北京
         * addr : 天安门
         * userId : 61
         */

        private int id;
        private String name;
        private String tel;
        private String regoin;
        private String addr;
        private int userId;
        private int used;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getRegoin() {
            return regoin;
        }

        public void setRegoin(String regoin) {
            this.regoin = regoin;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUsed() {
            return used;
        }

        public void setUsed(int used) {
            this.used = used;
        }
    }
}
