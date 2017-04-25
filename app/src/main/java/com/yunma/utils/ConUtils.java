package com.yunma.utils;

/**
 * Created by Json on 2016-12-02.
 */

public class ConUtils {
    //测试地址1
    //private static final String URL = "http://192.168.1.149:8080/api";
    //测试地址2

    //private static final String URL = "http://www.bruto.cn:9999/api";
    //发布地址
    private static final String URL = "https://app.beilyton.com";
    //用户注册
    public static final String USER_REGISTER
            = URL + "/user/add";
    //用户登录
    public static final String USER_LOGIN
            = URL + "/login";
    //用户忘记密码
    public static final String FORGET_PASSWORD
            = URL + "/user/pass/forget";
    //用户修改密码
    public static final String MODIFY_PASSWORD
            = URL + "/user/pass/mod";
    //用户信息查询
    public static final String USER_INFOS_QUERY
            = URL + "/user/info";
    //用户信息修改
    public static final String USER_INFOS_MODIFY
            = URL + "/user/modify";
    //上传头像
    public static final String UPLOADER_PHOTO
            = URL + "/user/pic";
    //获取公告
    public static final String GET_NOTICES
            = URL + "/notice/get";

/* ----------------------------------     大仓        --------------------------------------*/

    //获取大仓所有商品
    public static final String GET_GOODS
            = URL + "/goods/get";
    //获取精品
    public static final String GET_GOODS_RECOMMEND
            = URL + "/goods/recommend";
    //获取热款
    public static final String GET_GOODS_HOT
            = URL + "/goods/hot";
    //获取新品
    public static final String GET_GOODS_NEW
            = URL + "/goods/new";
    //添加购物车
    public static final String ADD_BASKET
            = URL + "/cart/add";
    //获取购物车
    public static final String GET_SHOPPING_CARTS
            = URL + "/cart/get";
    //删除购物车商品
    public static final String DEL_SHOPPING_CARTS
            = URL + "/cart/del";
    //货号查询
    public static final String SEARCH_GOODS
            = URL + "/goods/search";

/* -------------------------    自仓商品     ---------------------------------*/

    //获取自仓货品
    public static final String GET_SELF_GOODS
            = URL + "/yunma/get";
    //自仓商品查询
    public static final String SEARCH_SELF_GOODS
            = URL + "/yunma/search";
    //自仓商品添加
    public static final String ADD_SELF_GOODS
            = URL + "/yunma/add";
    //自仓商品删除
    public static final String DEL_SELF_GOODS
            = URL + "/yunma/del";
    //自仓商品修改
    public static final String MOD_SELF_GOODS
            = URL + "/yunma/mod";
    //快速发布货品
    public static final String PUBLISH_SELF_GOODS
            = URL + "/yunma/issue";

/* -------------------------    订    单     ---------------------------------*/
    //保存订单
    public static final String SAVE_ORDERS
        = URL + "/order/add";
    //查找未款订单
    public static final String ORDER_UNPAY
            = URL + "/order/get/unpay";
    //待发货订单
    public static final String ORDER_PAY_UNSEND
            = URL + "/order/get/pay";
    //查找已发货订单
    public static final String ORDER_SEND
            = URL + "/order/get/send";
    //删除订单
    public static final String ORDER_DEL
            = URL + "/order/del";
    //查看某个订单
    public static final String LOOL_ORDER
            = URL + "/order/info";
    //查看物流
    public static final String SEARCH_EXPRESS
            = URL + "/express/traces";
    //浏览购买记录
    public static final String LOOK_BUY_RECORD
            = URL + "/order/get/buy";
    //过期订单
    public static final String CHECK_ORDER
            = URL + "/order/check";
    //获取到货提醒
    public static final String GET_REMIND
            = URL + "/newRemind/get";
    //添加到货提醒
    public static final String ADD_REMIND
            = URL + "/newRemind/add";
    //删除到货提醒
    public static final String Del_REMIND
            = URL + "/newRemind/del";
/* -------------------------    售    后     ---------------------------------*/

    /*
    reund 0 为新添加售后
    1 同意退款 并且可向/service/refund 添加快递
    -1 拒绝退款
    2 快递添加成功，显示快递
    3 退款完成
    */

    //添加售后
    public static final String ADD_SERVICE
        = URL + "/service/add";
    //添加售后快递
    public static final String ADD_SERVICE_EXPRESS
            = URL + "/service/refund";
    //删除售后
    public static final String DEL_SERVICE
            = URL + "/service/del";
    //获取售后
    public static final String GET_SERVICE
            = URL + "/service/get";
    //单个售后
    public static final String SINGLE_SERVICE
            = URL + "/service/info";

/* -------------------------    收藏夹    ---------------------------------*/

    //添加收藏夹 collect
    public static final String ADD_COLLECT
            = URL + "/collect/add";
    //删除收藏夹
    public static final String DEL_COLLECT
            = URL + "/collect/del";
    //获取收藏夹
    public static final String GET_COLLECT
            = URL + "/collect/get";
    //判断收藏
    public static final String IF_COLLECT
            = URL + "/collect/search";

/* -------------------------    收件人管理     ---------------------------------*/

    //查询
    public static final String RRECIPIENT_REQURY
            =URL +  "/address/get";
    //添加
    public static final String RRECIPIENT_ADD
            = URL + "/address/add";
    //修改
    public static final String RRECIPIENT_MODIFY
            = URL + "/address/mod";
    //删除
    public static final String RRECIPIENT_DELETE
            = URL + "/address/del";

/* -------------------------    七牛云存储    ---------------------------------*/

    /**
     * 缩略图 /min
     */
    //获取Token
    public static final String GET_QINIU_TOKEN
            = URL + "/qiniu/get";
    //获取头像
    public static final String HEAD_IMAGE_URL
            ="http://user.yunma-j.com/";
    //获取大仓商品
    public static final String GOODS_IMAGE_URL
            ="http://goods.yunma-j.com/";
    //获取自仓商品
    public static final String SElF_GOODS_IMAGE_URL
            = "http://yunma.yunma-j.com/";
    //获取售后图片
    public static final String SERVICE_IMAGE_URL
            = "http://service.yunma-j.com/";
    //发票
    public static final String GET_INVIOCE_IMAGE
            = "http://invoice.yunma-j.com/";
    //系统banner
    public static final String BANNER_IMAGE
            = "http://system.yunma-j.com/";

/* -----------------------       发票        --------------------------------*/
    //获取增值税
    public static final String GET_INVOICE
            = URL + "/invoice/getvats";
    //添加增值税
    public static final String ADD_INVOICE
            = URL + "/invoice/addvat";
    //修改增值税
    public static final String EDIT_INVOICE
            = URL + "/invoice/modvat";
    //删除增值税
    public static final String DEL_INVOICE
            = URL + "/invoice/delvat";
    //开发票
    public static final String ASK_FOR_INVOICE
            = URL + "/invoice/add";
/* -------------------------    优惠卷    ---------------------------------*/
    //获取优惠卷
    public static final String GET_VOLUME_LIST
            = URL + "/coupon/get";

/* -------------------------    支付宝支付    ---------------------------------*/

    public static final String GET_PAY_ORDER_INFO
            = URL + "/pay/alipay";
    /**
     * 投诉建议
     */
    public static final String USER_FEEDBACK
            = URL + "/suggest/add";
    /***
     * Banner
     */
    public static final String BANNER
            = URL + "/business/get";

    /**
     * 广告页
     *  http://www.bruto.cn:9999/api/business/banner
     */
    public static final String AD
            = URL + "/business/banner";
    /* public static final String GET_IMAGES
            = "http://www.bruto.cn:8888/upload/";*/

}
