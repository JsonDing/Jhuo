package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Json on 2017/2/8.
 */

public class RecipientManageBean implements Serializable{

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable {

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public class ListBean implements Serializable {

            private String addr;
            private int id;
            private String name;
            private String regoin;
            private String tel;
            private int used;
            private int userId;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

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

            public String getRegoin() {
                return regoin;
            }

            public void setRegoin(String regoin) {
                this.regoin = regoin;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public int getUsed() {
                return used;
            }

            public void setUsed(int used) {
                this.used = used;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
