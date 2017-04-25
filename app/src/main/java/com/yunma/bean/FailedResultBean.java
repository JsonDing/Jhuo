package com.yunma.bean;

/**
 * Created on 2017-02-14
 *
 * @author Json.
 */

public class FailedResultBean {

    private FailedBean failed;

    public FailedBean getFailed() {
        return failed;
    }

    public void setFailed(FailedBean failed) {
        this.failed = failed;
    }

    public class FailedBean {

        private int errorType;
        private String errorMsg;

        public int getErrorType() {
            return errorType;
        }

        public void setErrorType(int errorType) {
            this.errorType = errorType;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
