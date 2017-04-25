package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class GoodsRemindResultBean {

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {
        private List<NewRemindsBean> newReminds;

        public List<NewRemindsBean> getNewReminds() {
            return newReminds;
        }

        public void setNewReminds(List<NewRemindsBean> newReminds) {
            this.newReminds = newReminds;
        }

        public class NewRemindsBean {

            private int id;
            private long remindTime;
            private int remindUser;
            private int goodsId;
            private int isLogicDel;
            private int number;
            private String goodsNum;
            private String goodsName;
            private String goodsPic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getRemindTime() {
                return remindTime;
            }

            public void setRemindTime(long remindTime) {
                this.remindTime = remindTime;
            }

            public int getRemindUser() {
                return remindUser;
            }

            public void setRemindUser(int remindUser) {
                this.remindUser = remindUser;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getIsLogicDel() {
                return isLogicDel;
            }

            public void setIsLogicDel(int isLogicDel) {
                this.isLogicDel = isLogicDel;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(String goodsNum) {
                this.goodsNum = goodsNum;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsPic() {
                return goodsPic;
            }

            public void setGoodsPic(String goodsPic) {
                this.goodsPic = goodsPic;
            }
        }
    }
}
