package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-03-18
 *
 * @author Json.
 */

public class BannerResultBean {

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean {
        /**
         * type : adword
         * value : 明天会更好,大河向东流,你好，我也好。,乐事无极限。
         */

        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
