package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class BuyRecordBean {

    /**
     * success : {"endRow":2,"firstPage":1,"hasNextPage":true,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":false,"lastPage":8,"list":[{"info":"阿迪达斯 阿迪达斯 C77124","num":1,"number":"C77124","pic":"C77124.jpg,iosYM2978944712996.jpg,iosYM2978944717284.jpg,iosYM2978944719402.jpg,iosYM2978944722378.jpg,iosYM2978944726548.jpg,iosYM2978944732622.jpg","repo":"自仓","repoid":1,"size":"3.5","userprice":520},{"info":"阿迪达斯 Adidas NEO阿迪休闲 男鞋 篮球鞋 CTX9TIS MID F99657","num":1,"number":"F99657","pic":"F99657.jpg,iosYM2978945248158.jpg,iosYM2978945250092.jpg,iosYM2978945291614.jpg,iosYM2978945296182.jpg,iosYM2978945299702.jpg,iosYM2978945302982.jpg","repo":"自仓","repoid":1,"size":"7.5","userprice":290}],"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"nextPage":2,"pageNum":1,"pageSize":2,"pages":20,"prePage":0,"size":2,"startRow":1,"total":40}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

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

        public class ListBean implements Serializable {

            private String info;
            private int num;
            private String number;
            private String pic;
            private String repo;
            private int repoid;
            private String size;
            private double userprice;

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
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
        }
    }
}
