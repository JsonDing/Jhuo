package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-03-04
 *
 * @author Json.
 */

public class LookOrderBean {


    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

        private int id;
        private long date;
        private long payDate;
        private int userId;
        private String name;
        private String addr;
        private String tel;
        private String phone;
        private String expressname;
        private String expressnumber;
        private double expresscost;
        private double totalmoney;
        private double totalcost;
        private String totalnum;
        private int pay;
        private int send;
        private int service;
        private String userorderid;
        private String express;
        private RepoBean repo;
        private UserBean user;
        private Object yunma;
        private int finished;
        private List<OrderdetailsBean> orderdetails;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getExpressname() {
            return expressname;
        }

        public void setExpressname(String expressname) {
            this.expressname = expressname;
        }

        public String getExpressnumber() {
            return expressnumber;
        }

        public void setExpressnumber(String expressnumber) {
            this.expressnumber = expressnumber;
        }

        public double getExpresscost() {
            return expresscost;
        }

        public void setExpresscost(double expresscost) {
            this.expresscost = expresscost;
        }

        public double getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(double totalmoney) {
            this.totalmoney = totalmoney;
        }

        public double getTotalcost() {
            return totalcost;
        }

        public void setTotalcost(double totalcost) {
            this.totalcost = totalcost;
        }

        public String getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }

        public int getPay() {
            return pay;
        }

        public void setPay(int pay) {
            this.pay = pay;
        }

        public int getSend() {
            return send;
        }

        public void setSend(int send) {
            this.send = send;
        }

        public int getService() {
            return service;
        }

        public void setService(int service) {
            this.service = service;
        }

        public String getUserorderid() {
            return userorderid;
        }

        public void setUserorderid(String userorderid) {
            this.userorderid = userorderid;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public RepoBean getRepo() {
            return repo;
        }

        public void setRepo(RepoBean repo) {
            this.repo = repo;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public Object getYunma() {
            return yunma;
        }

        public void setYunma(Object yunma) {
            this.yunma = yunma;
        }

        public int getFinished() {
            return finished;
        }

        public void setFinished(int finished) {
            this.finished = finished;
        }

        public List<OrderdetailsBean> getOrderdetails() {
            return orderdetails;
        }

        public void setOrderdetails(List<OrderdetailsBean> orderdetails) {
            this.orderdetails = orderdetails;
        }

        public class RepoBean {
            /**
             * id : 24
             * name : 厦门耐克
             * repoorder : null
             * type : null
             * row : null
             */

            private int id;
            private String name;
            private Object repoorder;
            private Object type;
            private Object row;

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

            public Object getRepoorder() {
                return repoorder;
            }

            public void setRepoorder(Object repoorder) {
                this.repoorder = repoorder;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

            public Object getRow() {
                return row;
            }

            public void setRow(Object row) {
                this.row = row;
            }
        }

        public class UserBean {

            private int id;
            private Object user;
            private Object pass;
            private Object account;
            private Object balance;
            private Object credit;
            private Object confirmorder;
            private Object dealorder;
            private int deliveryorder;
            private Object levelid;
            private Object agentid;
            private Object active;
            private Object name;
            private Object tel;
            private Object qq;
            private Object orgPass;
            private Object newPass;
            private Object money;
            private Object payPassword;
            private Object headImg;
            private Object deposit;
            private Object agent;
            private Object lvl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getUser() {
                return user;
            }

            public void setUser(Object user) {
                this.user = user;
            }

            public Object getPass() {
                return pass;
            }

            public void setPass(Object pass) {
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

            public int getDeliveryorder() {
                return deliveryorder;
            }

            public void setDeliveryorder(int deliveryorder) {
                this.deliveryorder = deliveryorder;
            }

            public Object getLevelid() {
                return levelid;
            }

            public void setLevelid(Object levelid) {
                this.levelid = levelid;
            }

            public Object getAgentid() {
                return agentid;
            }

            public void setAgentid(Object agentid) {
                this.agentid = agentid;
            }

            public Object getActive() {
                return active;
            }

            public void setActive(Object active) {
                this.active = active;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getTel() {
                return tel;
            }

            public void setTel(Object tel) {
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

            public Object getHeadImg() {
                return headImg;
            }

            public void setHeadImg(Object headImg) {
                this.headImg = headImg;
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

        public class OrderdetailsBean {

            private int id;
            private int oid;
            private String info;
            private String number;
            private String size;
            private double price;
            private Object discount;
            private double userprice;
            private Object userdiscount;
            private int num;
            private double money;
            private String gid;
            private int sid;
            private String repo;
            private Object expressname;
            private Object expresscode;
            private Object expressnumber;
            private Object postage;
            private Object supplypostage;
            private int send;
            private int repoid;
            private String pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOid() {
                return oid;
            }

            public void setOid(int oid) {
                this.oid = oid;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public double getUserprice() {
                return userprice;
            }

            public void setUserprice(double userprice) {
                this.userprice = userprice;
            }

            public Object getUserdiscount() {
                return userdiscount;
            }

            public void setUserdiscount(Object userdiscount) {
                this.userdiscount = userdiscount;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public int getSid() {
                return sid;
            }

            public void setSid(int sid) {
                this.sid = sid;
            }

            public String getRepo() {
                return repo;
            }

            public void setRepo(String repo) {
                this.repo = repo;
            }

            public Object getExpressname() {
                return expressname;
            }

            public void setExpressname(Object expressname) {
                this.expressname = expressname;
            }

            public Object getExpresscode() {
                return expresscode;
            }

            public void setExpresscode(Object expresscode) {
                this.expresscode = expresscode;
            }

            public Object getExpressnumber() {
                return expressnumber;
            }

            public void setExpressnumber(Object expressnumber) {
                this.expressnumber = expressnumber;
            }

            public Object getPostage() {
                return postage;
            }

            public void setPostage(Object postage) {
                this.postage = postage;
            }

            public Object getSupplypostage() {
                return supplypostage;
            }

            public void setSupplypostage(Object supplypostage) {
                this.supplypostage = supplypostage;
            }

            public int getSend() {
                return send;
            }

            public void setSend(int send) {
                this.send = send;
            }

            public int getRepoid() {
                return repoid;
            }

            public void setRepoid(int repoid) {
                this.repoid = repoid;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
