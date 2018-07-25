package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class GoodsRemindResultBean {


    /**
     * success : {"newReminds":[{"id":40,"remindTime":1494309714000,"remindUser":1110,"goodsId":null,"isLogicDel":null,"number":4,"goodsNum":"asf2765867","goodsName":"有困咯DJ音乐8轮拇指玩铁路局9吨女输液通知我？","goodsPic":"asf2765867.jpg,asf2765867_yunma_1.jpg,asf2765867_yunma_2.jpg,","mark":null,"issue":0},{"id":39,"remindTime":1494309472000,"remindUser":1110,"goodsId":null,"isLogicDel":null,"number":6,"goodsNum":"asdcz78958","goodsName":"4曲看完了鼠狼口有啦恩赖曾裕童suns有他","goodsPic":"asdcz78958.jpg,asdcz78958_yunma_1.jpg,asdcz78958_yunma_2.jpg,asdcz78958_yunma_3.jpg,asdcz78958_yunma_4.jpg,","mark":null,"issue":0},{"id":33,"remindTime":1493879606000,"remindUser":1110,"goodsId":null,"isLogicDel":null,"number":7,"goodsNum":"zx19910927","goodsName":"新百伦男鞋冬季运动会加油吧我们合不来哈皮哈皮妹纸利润率性而为","goodsPic":"zx19910927.jpg,zx19910927_yunma_1.jpg,zx19910927_yunma_2.jpg,","mark":null,"issue":0},{"id":32,"remindTime":1493869598000,"remindUser":1110,"goodsId":null,"isLogicDel":null,"number":4,"goodsNum":"AQ257034680","goodsName":"伤心啦上新啦！快来抢吧！下手慢了就没有啦没有啦！速度啊！","goodsPic":"AQ257034680.jpg,AQ257034680_yunma_1.jpg,AQ257034680_yunma_2.jpg,","mark":null,"issue":0},{"id":31,"remindTime":1493805398000,"remindUser":1110,"goodsId":null,"isLogicDel":null,"number":3,"goodsNum":"488656898","goodsName":"阿迪达斯三叶草外套女鞋之都","goodsPic":"488656898.jpg,488656898_yunma_1.jpg,488656898_yunma_2.jpg,","mark":null,"issue":0}]}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public static class SuccessBean {
        private List<NewRemindsBean> newReminds;

        public List<NewRemindsBean> getNewReminds() {
            return newReminds;
        }

        public void setNewReminds(List<NewRemindsBean> newReminds) {
            this.newReminds = newReminds;
        }

        public static class NewRemindsBean {
            /**
             * id : 40
             * remindTime : 1494309714000
             * remindUser : 1110
             * goodsId : null
             * isLogicDel : null
             * number : 4
             * goodsNum : asf2765867
             * goodsName : 有困咯DJ音乐8轮拇指玩铁路局9吨女输液通知我？
             * goodsPic : asf2765867.jpg,asf2765867_yunma_1.jpg,asf2765867_yunma_2.jpg,
             * mark : null
             * issue : 0
             */

            private int id;
            private long remindTime;
            private int remindUser;
            private String goodsId;
            private String isLogicDel;
            private int number;
            private String goodsNum;
            private String goodsName;
            private String goodsPic;
            private String mark;
            private int issue;

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

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getIsLogicDel() {
                return isLogicDel;
            }

            public void setIsLogicDel(String isLogicDel) {
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

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public int getIssue() {
                return issue;
            }

            public void setIssue(int issue) {
                this.issue = issue;
            }
        }
    }
}
