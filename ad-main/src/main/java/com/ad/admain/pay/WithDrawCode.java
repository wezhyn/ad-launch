package com.ad.admain.pay;

import lombok.AllArgsConstructor;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
@AllArgsConstructor
public enum WithDrawCode {

    /**
     * 无错误码
     */
    SUCCESS("调用成功"),
    /**
     * 业务错误码
     */
    INVALID_PARAMETER("参数有误参数有误"),
    SYSTEM_ERROR("系统繁忙"),
    EXCEED_LIMIT_SM_AMOUNT("单笔额度超限"),
    EXCEED_LIMIT_MM_AMOUNT("月累计金额超限"),
    PAYCARD_UNABLE_PAYMENT("付款账户余额支付功能不可用"),
    PAYER_STATUS_ERROR("付款账号状态异常"),
    PAYER_CERTIFY_CHECK_FAIL("付款方人行认证受限"),
    PAYER_BALANCE_NOT_ENOUGH("付款方余额不足"),
    PAYER_USER_INFO_ERROR("付款用户姓名或其它信息不一致"),
    PAYMENT_INFO_INCONSISTENCY("两次请求商户单号一样，但是参数不一致"),
    CARD_BIN_ERROR("收款人银行账号不正确"),
    PAYEE_CARD_INFO_ERROR("收款方卡信息错误"),
    INST_PAY_UNABLE("资金流出能力不具备"),
    MEMO_REQUIRED_IN_TRANSFER_ERROR("根据监管部门的要求，单笔转账金额达到50000元时，需要填写付款理由"),
    PERMIT_CHECK_PERM_IDENTITY_THEFT("您的账户存在身份冒用风险，请进行身份核实解除限制"),
    REMARK_HAS_SENSITIVE_WORD("转账备注包含敏感词，请修改备注文案后重试"),
    EXCEED_LIMIT_DM_AMOUNT("日累计额度超限"),
    NO_ACCOUNT_RECEIVE_PERMISSION("没有该账户的收款权限"),
    BALANCE_IS_NOT_ENOUGH("付款方余额不足"),
    NO_ACCOUNT_PAYMENT_PERMISSION("没有该账户的支付权限"),
    PAYER_NOT_EXIST("付款方不存在"),
    PRODUCT_NOT_SIGN("产品未签约"),
    PAYMENT_TIME_EXPIRE("请求已过期"),
    PAYEE_NOT_EXIST("收款用户不存在"),
    PAYEE_ACCOUNT_STATUS_ERROR("收款方账号异常"),
    PERMIT_NON_BANK_LIMIT_PAYEE("收款方未完善身份信息或未开立余额账户，无法收款"),
    PAYEE_TRUSTEESHIP_ACC_OVER_LIMIT("收款方托管子户累计收款金额超限"),
    NO_PERMISSION_ACCOUNT("无权限操作当前付款账号"),
    TRUSTEESHIP_ACCOUNT_NOT_EXIST("托管子户查询不存在"),
    PAYEE_ACCOUNT_NOT_EXSIT("收款账号不存在"),
    ORDER_NOT_EXIST("original_order_id错误 原单据不存在"),
    PAYEE_USERINFO_STATUS_ERROR("收款方用户状态不正常"),
    PAYMENT_MONEY_NOT_ENOUGH("可用金额为0或不足"),
    TRUSTEESHIP_RECIEVE_QUOTA_LIMIT("收款方收款额度超限，请绑定支付宝账户"),
    SECURITY_CHECK_FAILED("本次请求有风险"),
    NO_ORDER_PERMISSION("orinal_order_id错误，不具有操作权限"),
    ORDER_STATUS_INVALID("原始单据状态异常，不可操作"),
    PERM_AML_NOT_REALNAME_REV("根据监管部门的要求，需要收款用户补充身份信息才能继续操作"),
    USER_AGREEMENT_VERIFY_FAIL("用户协议校验失败"),
    PAYER_NOT_EQUAL_PAYEE_ERROR("托管场景提现收付款方账号不一致"),
    EXCEED_LIMIT_DC_RECEIVED("收款方单日收款笔数超限"),
    PAYER_PERMLIMIT_CHECK_FAILURE("付款方限权校验不通过不允许支付"),
    PAYEE_ACC_OCUPIED("收款方登录号有多个支付宝账号，无法确认唯一收款账号"),
    PAYER_PAYEE_CANNOT_SAME("收付款方不能相同"),
    PERMIT_CHECK_PERM_LIMITED("根据监管部门的要求，请补全您的身份信息解除限制"),
    RESOURCE_LIMIT_EXCEED("请求超过资源限制"),
    INVALID_PAYER_ACCOUNT("付款方不在设置的付款账户列表中"),
    EXCEED_LIMIT_DM_MAX_AMOUNT("超出单日转账限额"),
    EXCEED_LIMIT_PERSONAL_SM_AMOUNT("超出转账给个人支付宝账户的单笔限额"),
    EXCEED_LIMIT_UNRN_DM_AMOUNT("收款账户未实名，超出其单日收款限额"),
    INVALID_CARDNO("无效的收款卡号"),
    RELEASE_USER_FORBBIDEN_RECIEVE("支付宝手机号二次放号禁止收款"),
    PAYEE_USER_TYPE_ERROR("不支持的收款用户类型"),
    EXCEED_LIMIT_SM_MIN_AMOUNT("请求金额低于单笔最小转账金额"),
    PERMIT_CHECK_RECEIVE_LIMIT("您的账户限制收款，请咨询95188电话咨询"),
    NOT_IN_WHITE_LIST("您的请求不在白名单范围内"),
    MONEY_PAY_CLOSED("付款账号余额关闭"),
    NO_AVAILABLE_PAYMENT_TOOLS("您当前无法支付，请咨询95188"),
    PAYEE_NOT_RELNAME_CERTIFY("收款方未实名认证"),
    OVERSEA_TRANSFER_CLOSE("您无法进行结汇业务，请联系95188"),
    PAYMENT_FAIL("支付失败");

    private String subMsg;


    public String getSubMsg() {
        return subMsg;
    }
}
