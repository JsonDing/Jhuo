package com.yunma.bean;

/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class UserInfosResultBean {



    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

        private Long id;
        private String user;
        private String pass;
        private double account;
        private double balance;
        private double credit;
        private int confirmorder;
        private int dealorder;
        private int deliveryorder;
        private int levelid;
        private int agentid;
        private int active;
        private String name;
        private String tel;
        private String qq;
        private Object orgPass;
        private Object newPass;
        private Object money;
        private Object payPassword;
        private String headImg;
        private Object deposit;
        private AgentBean agent;
        private LvlBean lvl;
        private int points;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getCredit() {
            return credit;
        }

        public void setCredit(double credit) {
            this.credit = credit;
        }

        public int getConfirmorder() {
            return confirmorder;
        }

        public void setConfirmorder(int confirmorder) {
            this.confirmorder = confirmorder;
        }

        public int getDealorder() {
            return dealorder;
        }

        public void setDealorder(int dealorder) {
            this.dealorder = dealorder;
        }

        public int getDeliveryorder() {
            return deliveryorder;
        }

        public void setDeliveryorder(int deliveryorder) {
            this.deliveryorder = deliveryorder;
        }

        public int getLevelid() {
            return levelid;
        }

        public void setLevelid(int levelid) {
            this.levelid = levelid;
        }

        public int getAgentid() {
            return agentid;
        }

        public void setAgentid(int agentid) {
            this.agentid = agentid;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
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

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public Object getOrgPass() {
            return orgPass;
        }

        public void setOrgPass(Object orgPass) {
            this.orgPass = orgPass;
        }

        public Object getNewPass() {
            return newPass;
        }

        public void setNewPass(Object newPass) {
            this.newPass = newPass;
        }

        public Object getMoney() {
            return money;
        }

        public void setMoney(Object money) {
            this.money = money;
        }

        public Object getPayPassword() {
            return payPassword;
        }

        public void setPayPassword(Object payPassword) {
            this.payPassword = payPassword;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public Object getDeposit() {
            return deposit;
        }

        public void setDeposit(Object deposit) {
            this.deposit = deposit;
        }

        public AgentBean getAgent() {
            return agent;
        }

        public void setAgent(AgentBean agent) {
            this.agent = agent;
        }

        public LvlBean getLvl() {
            return lvl;
        }

        public void setLvl(LvlBean lvl) {
            this.lvl = lvl;
        }

        public class AgentBean {

            private int id;
            private double discount;
            private String name;
            private Object nick;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getNick() {
                return nick;
            }

            public void setNick(Object nick) {
                this.nick = nick;
            }
        }

        public class LvlBean {

            private int id;
            private String value;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
