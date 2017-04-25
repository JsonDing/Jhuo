package com.yunma.bean;

/**
 * Created on 2017-01-20
 *
 * @author Json.
 */

public class RegisterSuccessResultBean {

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

        private Long id;//用户id
        private Object user;
        private String pass;
        private Object account;//帐户余额(元)
        private Object balance;//可用余额(元)
        private Object credit;//信用额度(元)
        private Object confirmorder;//未处理订单
        private Object dealorder;//未处理售后
        private Object deliveryorder;//未发货订单
        private Object levelid;//用户组id
        private int agentid;//代理id
        private int active;//1启用 0禁用
        private Object name;
        private String tel;
        private Object qq;
        private Object orgPass;
        private Object newPass;
        private Object money;
        private Object payPassword;
        private Object deposit;
        private Object agent;
        private Object lvl;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public Object getAccount() {
            return account;
        }

        public void setAccount(Object account) {
            this.account = account;
        }

        public Object getBalance() {
            return balance;
        }

        public void setBalance(Object balance) {
            this.balance = balance;
        }

        public Object getCredit() {
            return credit;
        }

        public void setCredit(Object credit) {
            this.credit = credit;
        }

        public Object getConfirmorder() {
            return confirmorder;
        }

        public void setConfirmorder(Object confirmorder) {
            this.confirmorder = confirmorder;
        }

        public Object getDealorder() {
            return dealorder;
        }

        public void setDealorder(Object dealorder) {
            this.dealorder = dealorder;
        }

        public Object getDeliveryorder() {
            return deliveryorder;
        }

        public void setDeliveryorder(Object deliveryorder) {
            this.deliveryorder = deliveryorder;
        }

        public Object getLevelid() {
            return levelid;
        }

        public void setLevelid(Object levelid) {
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

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public Object getQq() {
            return qq;
        }

        public void setQq(Object qq) {
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

        public Object getDeposit() {
            return deposit;
        }

        public void setDeposit(Object deposit) {
            this.deposit = deposit;
        }

        public Object getAgent() {
            return agent;
        }

        public void setAgent(Object agent) {
            this.agent = agent;
        }

        public Object getLvl() {
            return lvl;
        }

        public void setLvl(Object lvl) {
            this.lvl = lvl;
        }
    }
}
