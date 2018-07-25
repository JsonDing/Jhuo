package com.yunma.utils;

import android.os.Environment;

/**
 * Created by Json on 2016-12-02.
 */

public class ConUtils {
    //测试地址1
    //private static final String URL = "http://test.yunma-m.com:8083/api";
    //测试地址2
    //private static final String URL = "http://192.168.1.103:8083/api";
    //private static final String URL = "http://www.bruto.cn:9999/api";
    //发布地址
      private static final String URL = "https://app.yunma-j.com";
    //用户注册
    public static final String USER_REGISTER
            = URL + "/user/reg";
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
    //判断是否注册
    public static final String IF_REGISTER
            = URL + "/user/check";
    //验证码
    public static final String IDENTIFYING_CODE
            = URL + "/sms/code";
    // 获取代理等级
    public static final String Get_AGENT_LIST
            = URL + "/business/agent";

/* -------------------------     自仓商品     ---------------------------------*/

    //获取自仓货品
    public static final String GET_SELF_GOODS
            = URL + "/yunma/get";
    public static final String GET_TOMORROW_UPDATA
            = URL + "/newRemind/getMark";
    //自仓商品查询
    public static final String SEARCH_SELF_GOODS
            = URL + "/yunma/search";
    //热门搜索
    public static final String HOT_SEARCH
            = URL + "/search/hot";
    //获取上新提醒
    public static final String GET_REMIND
            = URL + "/newRemind/get";
    public static final String ADD_REMIND
    //添加上新提醒
            = URL + "/newRemind/add";
    //删除上新提醒
    public static final String Del_REMIND
            = URL + "/newRemind/del";
    //分类-无货-喜欢的添加提醒
    public static final String ADD_REMIND_LIKE
            = URL + "/goods/remind";
    //短信提醒
    public static final String MSG_REMIND
            = URL + "/sms/add";
    //获取自仓商品类型 /yunma/type
    public static final String GOODS_TYPE
            = URL + "/yunma/type";
    //添加购物车
    public static final String ADD_BASKET
            = URL + "/cart/add";
    //获取购物车
    public static final String GET_SHOPPING_CARTS
            = URL + "/cart/get";
    //删除购物车商品
    public static final String DEL_SHOPPING_CARTS
            = URL + "/cart/del";
    //获取单个商品详情
    public static final String GOODS_DETIALS
            = URL + "/yunma/info";
    //获取特殊商品- Vip等级
    public static final String SPECIAL_GOODS
            = URL + "/goods/get";
    //购买VIP等级
    public static final String BUY_VIP_GRADE
            = URL + "/order/agent";
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
    //图片下载地址
    public static final String DOWN_LOAD_PATH =
            Environment.getExternalStorageDirectory().getPath().toString() + "/YunMa/Jhuo/download/" ;

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
    // 获取优惠卷
    public static final String GET_VOLUME_LIST
            = URL + "/coupon/get";
    // 获取积分兑换优惠券列表
    public static final String Get_Integral_USE
            = URL + "/coupon/points";
    // 积分兑换优惠券
    public static final String POINTS_EXCHANNGE
            = URL + "/coupon/convert";

/* -------------------------    支付宝支付    ---------------------------------*/

    public static final String ALIPAY_PAY
            = URL + "/pay/alipay";



    /* -------------------------    微信支付    ---------------------------------*/

    public static final String WECHAT_ID
            = "wx129574b4dc257e3d";

    public static final String WECHAT_PAY
            = URL + "/pay/wechatPay";
    /**
     * 投诉建议
     */
    public static final String USER_FEEDBACK
            = URL + "/suggest/add";
    /***
     * Banner
     */
    public static final String AD_WORDS
            = URL + "/business/get";

    /**
     * 首页顶部Banner页
     */
    public static final String BANNER
            = URL + "/business/banner";

    /**
     * 我的 界面提醒
     */
    public static final String REMIND
            = URL + "/statistic/get";

    /*                              积   分                                     */
    //分享获取积分
    public static final String INTEGRAL_FROM_SHARE
              = URL + "/user/share";

    // 签到获取积分  签到，type: sign ，取得最近签到 type: get
    public static final String INTEGRAL_FROM_SIGN
             = URL + "/user/signed";

    //积分列表
    public static final String INTEGRAL_LIST
             = URL + "/user/points";

    /**
     * 积分变动记录。
     * action 1首次 2签到 3购买货品 4推荐码 5分享 6退货
     * type 0减少 1增加
     */

    /**
     * 自定义首页model
     */
    public static final String MODEL
            = URL + "/business/defined/new";
    /**
     * 自定义首页数据
     */
    public static final String MODEL_DATA
            = URL + "/yunma/search";

    /**
     * 获取所有品牌
     */
    public static final String BRAND
            = URL + "/yunma/brand";

    /**
     * 获取所有尺码
     */
    public static final String GOODS_SIZE
            = URL + "/yunma/size";

    /**
     * 返利列表
     * 参数 :
     - token : 如果只传token就是获取全部
     - status : 0未返 1已返
     */
    public static final String MY_RETURN_LIST
            = URL + "/rebate/get";

    /**
     * 用户推荐好友列表
     * 参数 :
     - token : 如果只传token就是获取全部
     */
    public static final String INVITED_USER_LIST
            = URL + "/user/down";

    //添加分享到相册库
    public static final String ADD_SHARE_STOCKS =
            URL + "/share/add";
    //获取分享
    public static final String GET_SHARE_STOCKS =
            URL + "/share/search";
    //删除分享
    public static final String DELETE_SHARE =
            URL + "/share/del";
    // 搜索视频
    public static final String VIDEO_SEARCH =
            URL + "/file/search";
    // 视频点击
    public static final String VIDEO_CLICK =
            URL + "/file/click";
    //video地址
    public static final String VIDEO_BASE_URL
            = "http://file.yunma-j.com/";
}
