package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderUnPayResultBean implements Serializable{

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable{

        private int endRow;
        private int firstPage;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private int lastPage;
        private int navigatePages;
        private int nextPage;
        private String orderBy;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int prePage;
        private int size;
        private int startRow;
        private int total;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public class ListBean implements Serializable{

            private String addr;
            private long date;
            private double expresscost;
            private int finished;
            private int id;
            private int invoice;
            private String name;
            private int pay;
            private long payDate;
            private int send;
            private int service;
            private String tel;
            private double totalcost;
            private double totalmoney;
            private int userId;
            private List<OrderdetailsBean> orderdetails;
            private double couponMoney;
            private String couponType;

            public double getCouponMoney() {
                return couponMoney;
            }

            public void setCouponMoney(double couponMoney) {
                this.couponMoney = couponMoney;
            }

            public String getCouponType() {
                return couponType;
            }

            public void setCouponType(String couponType) {
                this.couponType = couponType;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public double getExpresscost() {
                return expresscost;
            }

            public void setExpresscost(double expresscost) {
                this.expresscost = expresscost;
            }

            public int getFinished() {
                return finished;
            }

            public void setFinished(int finished) {
                this.finished = finished;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getInvoice() {
                return invoice;
            }

            public void setInvoice(int invoice) {
                this.invoice = invoice;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public long getPayDate() {
                return payDate;
            }

            public void setPayDate(long payDate) {
                this.payDate = payDate;
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

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public double getTotalcost() {
                return totalcost;
            }

            public void setTotalcost(double totalcost) {
                this.totalcost = totalcost;
            }

            public double getTotalmoney() {
                return totalmoney;
            }

            public void setTotalmoney(double totalmoney) {
                this.totalmoney = totalmoney;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public List<OrderdetailsBean> getOrderdetails() {
                return orderdetails;
            }

            public void setOrderdetails(List<OrderdetailsBean> orderdetails) {
                this.orderdetails = orderdetails;
            }

            public class OrderdetailsBean implements Serializable{
                private String expresscode;
                private String expressname;
                private String expressnumber;
                private String gid;
                private String id;
                private String info;
                private double money;
                private int num;
                private String number;
                private String oid;
                private String pic;
                private double price;
                private String repo;
                private int repoid;
                private int send;
                private int sid;
                private String size;
                private double userprice;
                private String newPic;
                private String remark = "";
                private String couponMoney;
                private int selectedPos = 0;
                private List<PathBean> imgsPath;


                public String getCouponMoney() {
                    return couponMoney;
                }

                public void setCouponMoney(String couponMoney) {
                    this.couponMoney = couponMoney;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public int getSelectedPos() {
                    return selectedPos;
                }

                public void setSelectedPos(int selectedPos) {
                    this.selectedPos = selectedPos;
                }

                public String getExpresscode() {
                    return expresscode;
                }

                public void setExpresscode(String expresscode) {
                    this.expresscode = expresscode;
                }

                public String getNewPic() {
                    return newPic;
                }

                public void setNewPic(String newPic) {
                    this.newPic = newPic;
                }

                public List<PathBean> getImgsPath() {
                    return imgsPath;
                }

                public void setImgsPath(List<PathBean> imgsPath) {
                    this.imgsPath = imgsPath;
                }

                public String getExpressname() {
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

                public String getGid() {
                    return gid;
                }

                public void setGid(String gid) {
                    this.gid = gid;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public double getMoney() {
                    return money;
                }

                public void setMoney(double money) {
                    this.money = money;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getOid() {
                    return oid;
                }

                public void setOid(String oid) {
                    this.oid = oid;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public String getRepo() {
                    return repo;
                }

                public void setRepo(String repo) {
                    this.repo = repo;
                }

                public int getRepoid() {
                    return repoid;
                }

                public void setRepoid(int repoid) {
                    this.repoid = repoid;
                }

                public int getSend() {
                    return send;
                }

                public void setSend(int send) {
                    this.send = send;
                }

                public int getSid() {
                    return sid;
                }

                public void setSid(int sid) {
                    this.sid = sid;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public double getUserprice() {
                    return userprice;
                }

                public void setUserprice(double userprice) {
                    this.userprice = userprice;
                }

                @Override
                public String toString() {
                    return "OrderDetailsBean{" +
                            ", money=" + money +
                            ", num=" + num +
                            ", size='" + size + '\'' +
                            ", userprice=" + userprice +
                            '}';
                }
            }
        }
    }
}
