package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-03-17
 *
 * @author Json.
 */

public class ExpressTracesBean {

    /**
     * success : {"EBusinessID":"1269807","ShipperCode":"YD","Success":true,"LogisticCode":"1000824342255","State":"3","Traces":[{"AcceptTime":"2017-03-16 18:49:31","AcceptStation":"到达：江苏无锡市滨湖区华庄公司 已收件"},{"AcceptTime":"2017-03-16 20:42:24","AcceptStation":"到达：江苏无锡市滨湖区华庄公司 发往：安徽蚌埠分拨中心"},{"AcceptTime":"2017-03-16 21:56:07","AcceptStation":"到达：江苏苏州分拨中心 上级站点：江苏无锡市滨湖区华庄公司"},{"AcceptTime":"2017-03-16 22:03:11","AcceptStation":"到达：江苏苏州分拨中心 发往：安徽蚌埠分拨中心"},{"AcceptTime":"2017-03-17 07:37:15","AcceptStation":"到达：安徽蚌埠分拨中心 上级站点：江苏苏州分拨中心"},{"AcceptTime":"2017-03-17 07:41:42","AcceptStation":"到达：安徽蚌埠分拨中心 发往：安徽蚌埠新城区公司"},{"AcceptTime":"2017-03-17 09:34:18","AcceptStation":"到达：安徽蚌埠新城区公司 上级站点：安徽蚌埠分拨中心 发往："},{"AcceptTime":"2017-03-17 13:00:53","AcceptStation":"到达：安徽蚌埠新城区公司 指定：李波(18909623505) 派送"},{"AcceptTime":"2017-03-17 17:00:32","AcceptStation":"到达：安徽蚌埠新城区公司 由 已签收 签收"}]}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public static class SuccessBean {
        /**
         * EBusinessID : 1269807
         * ShipperCode : YD
         * Success : true
         * LogisticCode : 1000824342255
         * State : 3
         * Traces : [{"AcceptTime":"2017-03-16 18:49:31","AcceptStation":"到达：江苏无锡市滨湖区华庄公司 已收件"},{"AcceptTime":"2017-03-16 20:42:24","AcceptStation":"到达：江苏无锡市滨湖区华庄公司 发往：安徽蚌埠分拨中心"},{"AcceptTime":"2017-03-16 21:56:07","AcceptStation":"到达：江苏苏州分拨中心 上级站点：江苏无锡市滨湖区华庄公司"},{"AcceptTime":"2017-03-16 22:03:11","AcceptStation":"到达：江苏苏州分拨中心 发往：安徽蚌埠分拨中心"},{"AcceptTime":"2017-03-17 07:37:15","AcceptStation":"到达：安徽蚌埠分拨中心 上级站点：江苏苏州分拨中心"},{"AcceptTime":"2017-03-17 07:41:42","AcceptStation":"到达：安徽蚌埠分拨中心 发往：安徽蚌埠新城区公司"},{"AcceptTime":"2017-03-17 09:34:18","AcceptStation":"到达：安徽蚌埠新城区公司 上级站点：安徽蚌埠分拨中心 发往："},{"AcceptTime":"2017-03-17 13:00:53","AcceptStation":"到达：安徽蚌埠新城区公司 指定：李波(18909623505) 派送"},{"AcceptTime":"2017-03-17 17:00:32","AcceptStation":"到达：安徽蚌埠新城区公司 由 已签收 签收"}]
         */
        private String EBusinessID;
        private String ShipperCode;
        private boolean Success;
        private String LogisticCode;
        private String State;
        private List<TracesBean> Traces;

        public String getEBusinessID() {
            return EBusinessID;
        }

        public void setEBusinessID(String EBusinessID) {
            this.EBusinessID = EBusinessID;
        }

        public String getShipperCode() {
            return ShipperCode;
        }

        public void setShipperCode(String ShipperCode) {
            this.ShipperCode = ShipperCode;
        }

        public boolean isSuccess() {
            return Success;
        }

        public void setSuccess(boolean Success) {
            this.Success = Success;
        }

        public String getLogisticCode() {
            return LogisticCode;
        }

        public void setLogisticCode(String LogisticCode) {
            this.LogisticCode = LogisticCode;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public List<TracesBean> getTraces() {
            return Traces;
        }

        public void setTraces(List<TracesBean> Traces) {
            this.Traces = Traces;
        }

        public static class TracesBean {
            /**
             * AcceptTime : 2017-03-16 18:49:31
             * AcceptStation : 到达：江苏无锡市滨湖区华庄公司 已收件
             */

            private String AcceptTime;
            private String AcceptStation;

            public String getAcceptTime() {
                return AcceptTime;
            }

            public void setAcceptTime(String AcceptTime) {
                this.AcceptTime = AcceptTime;
            }

            public String getAcceptStation() {
                return AcceptStation;
            }

            public void setAcceptStation(String AcceptStation) {
                this.AcceptStation = AcceptStation;
            }
        }
    }
}
