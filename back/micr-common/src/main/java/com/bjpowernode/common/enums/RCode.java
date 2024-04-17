package com.bjpowernode.common.enums;

public enum RCode {

    UNKOWN(0,"请稍候重试"),
    SUCC(1000,"请求成功"),
    REQUEST_PARAM_ERR(1001,"请求参数有误"),
    REQUEST_PRODUCT_TYPE_ERR(1002,"产品类型有误"),
    PRODUCT_OFFLINE(1003,"产品已经下线"),
    PHONE_FORMAT_ERR(1004,"手机号格式不正确"),
    PHONE_EXISTS(1005,"手机号已注册"),
    SMS_CODE_CAN_USE(1006,"验证码可以继续使用"),
    SMS_CODE_INVALID(1007,"验证码无效"),
    PHONE_PASSWORD_LOGIN_INVALID(1008,"手机号或密码无效"),
    REALNAME_FAIL(1009,"实名认证失败"),
    REALNAME_RETRY(1010,"已通过实名认证"),
    TOKEN_INVALID(3000,"token无效"),;

    /**
     * 0默认
     *1000-2000是请求参数有误，逻辑的问题
     *2000-3000是服务器请求错误
     *3000-4000是访问dubbo的应答结果错误
    */
    private int code;
    private String text;

    RCode(int c,String t){
        this.code = c;
        this.text = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
