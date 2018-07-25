package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class ServiceResultBean {

    /**
     * success : {"endRow":1,"firstPage":1,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":true,"lastPage":1,"list":[{"date":1489461010000,"id":59,"moneyout":0,"oid":23693,"order":{"date":1488934407000,"expresscost":10,"payDate":1488934413000,"totalcost":10},"refund":0,"serviceDetails":[{"id":22,"num":1,"orderdetail":{"info":"你","pic":"iosYM2976925392238.jpg,iosYM2976925393664.jpg,iosYM2976925396112.jpg,iosYM2976925398766.jpg,iosYM2976925401316.jpg,iosYM2976925413408.jpg","repoid":1,"userprice":0},"orderdetailId":31403,"pic":"Jhuo_Service_295bc937537c8a30384435e42df5fb751.png,Jhuo_Service_295bc937537c8a30384435e42df5fb752.png,Jhuo_Service_295bc937537c8a30384435e42df5fb750.png,","reason":"质量问题","refundMoney":0,"remark":"","serviceId":59,"size":"35"}]}],"navigatePages":8,"navigatepageNums":[1],"nextPage":0,"pageNum":1,"pageSize":10,"pages":1,"prePage":0,"size":1,"startRow":1,"total":1}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable{
        /**
         * endRow : 1
         * firstPage : 1
         * hasNextPage : false
         * hasPreviousPage : false
         * isFirstPage : true
         * isLastPage : true
         * lastPage : 1
         * list : [{"date":1489461010000,"id":59,"moneyout":0,"oid":23693,"order":{"date":1488934407000,"expresscost":10,"payDate":1488934413000,"totalcost":10},"refund":0,"serviceDetails":[{"id":22,"num":1,"orderdetail":{"info":"你","pic":"iosYM2976925392238.jpg,iosYM2976925393664.jpg,iosYM2976925396112.jpg,iosYM2976925398766.jpg,iosYM2976925401316.jpg,iosYM2976925413408.jpg","repoid":1,"userprice":0},"orderdetailId":31403,"pic":"Jhuo_Service_295bc937537c8a30384435e42df5fb751.png,Jhuo_Service_295bc937537c8a30384435e42df5fb752.png,Jhuo_Service_295bc937537c8a30384435e42df5fb750.png,","reason":"质量问题","refundMoney":0,"remark":"","serviceId":59,"size":"35"}]}]
         * navigatePages : 8
         * navigatepageNums : [1]
         * nextPage : 0
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * prePage : 0
         * size : 1
         * startRow : 1
         * total : 1
         */

        private int endRow;
        private int firstPage;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private int lastPage;
        private int navigatePages;
        private int nextPage;
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
            /**
             * date : 1489461010000
             * id : 59
             * moneyout : 0.0
             * oid : 23693
             * order : {"date":1488934407000,"expresscost":10,"payDate":1488934413000,"totalcost":10}
             * refund : 0
             * serviceDetails : [{"id":22,"num":1,"orderdetail":{"info":"你","pic":"iosYM2976925392238.jpg,iosYM2976925393664.jpg,iosYM2976925396112.jpg,iosYM2976925398766.jpg,iosYM2976925401316.jpg,iosYM2976925413408.jpg","repoid":1,"userprice":0},"orderdetailId":31403,"pic":"Jhuo_Service_295bc937537c8a30384435e42df5fb751.png,Jhuo_Service_295bc937537c8a30384435e42df5fb752.png,Jhuo_Service_295bc937537c8a30384435e42df5fb750.png,","reason":"质量问题","refundMoney":0,"remark":"","serviceId":59,"size":"35"}]
             */
            private String express;
            private String expressNumber;
            private long date;
            private long refundDate;
            private String id;
            private double moneyout;
            private String oid;
            private OrderBean order;
            private int refund;
            private String uid;
            private List<ServiceDetailsBean> serviceDetails;

            public String getExpressNumber() {
                return expressNumber;
            }

            public void setExpressNumber(String expressNumber) {
                this.expressNumber = expressNumber;
            }

            public String getExpress() {
                return express;
            }

            public void setExpress(String express) {
                this.express = express;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public long getRefundDate() {
                return refundDate;
            }

            public void setRefundDate(long refundDate) {
                this.refundDate = refundDate;
            }

            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getMoneyout() {
                return moneyout;
            }

            public void setMoneyout(double moneyout) {
                this.moneyout = moneyout;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public OrderBean getOrder() {
                return order;
            }

            public void setOrder(OrderBean order) {
                this.order = order;
            }

            public int getRefund() {
                return refund;
            }

            public void setRefund(int refund) {
                this.refund = refund;
            }

            public List<ServiceDetailsBean> getServiceDetails() {
                return serviceDetails;
            }

            public void setServiceDetails(List<ServiceDetailsBean> serviceDetails) {
                this.serviceDetails = serviceDetails;
            }

            public class OrderBean implements Serializable{
                /**
                 * date : 1488934407000
                 * expresscost : 10.0
                 * payDate : 1488934413000
                 * totalcost : 10.0
                 */

                private long date;
                private double expresscost;
                private long payDate;
                private double totalcost;

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

                public long getPayDate() {
                    return payDate;
                }

                public void setPayDate(long payDate) {
                    this.payDate = payDate;
                }

                public double getTotalcost() {
                    return totalcost;
                }

                public void setTotalcost(double totalcost) {
                    this.totalcost = totalcost;
                }
            }

            public class ServiceDetailsBean implements Serializable{
                /**
                 * id : 22
                 * num : 1
                 * orderdetail : {"info":"你","pic":"iosYM2976925392238.jpg,iosYM2976925393664.jpg,iosYM2976925396112.jpg,iosYM2976925398766.jpg,iosYM2976925401316.jpg,iosYM2976925413408.jpg","repoid":1,"userprice":0}
                 * orderdetailId : 31403
                 * pic : Jhuo_Service_295bc937537c8a30384435e42df5fb751.png,Jhuo_Service_295bc937537c8a30384435e42df5fb752.png,Jhuo_Service_295bc937537c8a30384435e42df5fb750.png,
                 * reason : 质量问题
                 * refundMoney : 0
                 * remark :
                 * serviceId : 59
                 * size : 35
                 */

                private int id;
                private int num;
                private OrderdetailBean orderdetail;
                private int orderdetailId;
                private String pic;
                private String reason;
                private double refundMoney;
                private String remark;
                private String serviceId;
                private String size;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public OrderdetailBean getOrderdetail() {
                    return orderdetail;
                }

                public void setOrderdetail(OrderdetailBean orderdetail) {
                    this.orderdetail = orderdetail;
                }

                public int getOrderdetailId() {
                    return orderdetailId;
                }

                public void setOrderdetailId(int orderdetailId) {
                    this.orderdetailId = orderdetailId;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }

                public double getRefundMoney() {
                    return refundMoney;
                }

                public void setRefundMoney(double refundMoney) {
                    this.refundMoney = refundMoney;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getServiceId() {
                    return serviceId;
                }

                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public class OrderdetailBean implements Serializable{
                    /**
                     * info : 你
                     * pic : iosYM2976925392238.jpg,iosYM2976925393664.jpg,iosYM2976925396112.jpg,iosYM2976925398766.jpg,iosYM2976925401316.jpg,iosYM2976925413408.jpg
                     * repoid : 1
                     * userprice : 0.0
                     */

                    private String info;
                    private String pic;
                    private int repoid;
                    private double userprice;

                    public String getInfo() {
                        return info;
                    }

                    public void setInfo(String info) {
                        this.info = info;
                    }

                    public String getPic() {
                        return pic;
                    }

                    public void setPic(String pic) {
                        this.pic = pic;
                    }

                    public int getRepoid() {
                        return repoid;
                    }

                    public void setRepoid(int repoid) {
                        this.repoid = repoid;
                    }

                    public double getUserprice() {
                        return userprice;
                    }

                    public void setUserprice(double userprice) {
                        this.userprice = userprice;
                    }
                }
            }
        }
    }
}
