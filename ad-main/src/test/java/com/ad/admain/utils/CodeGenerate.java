package com.ad.admain.utils;

import org.junit.Before;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 02.24.2020
 */
public class CodeGenerate {


    private String msg;

    @Before
    public void init() {
        msg="INVALID_PARAMETER \t参数有误参数有误 \t请根据入参说明检查请求参数合法性。\n" +
                "SYSTEM_ERROR \t系统繁忙 \t可能发生了网络或者系统异常，导致无法判定准确的转账结果。此时，商户不能直接当做转账成功或者失败处理，可以考虑采用相同的out_biz_no重发请求，或者通过调用“(alipay.fund.trans.order.query)”来查询该笔转账订单的最终状态。\n" +
                "EXCEED_LIMIT_SM_AMOUNT \t单笔额度超限 \t请根据接入文档检查amount字段\n" +
                "EXCEED_LIMIT_MM_AMOUNT \t月累计金额超限 \t请根据接入文档说明检查本月请求总金额+本次请求金额是否超限。\n" +
                "PAYCARD_UNABLE_PAYMENT \t付款账户余额支付功能不可用 \t请付款账户登录支付宝账户开启余额支付功能。\n" +
                "PAYER_STATUS_ERROR \t付款账号状态异常 \t请检查付款方是否进行了自助挂失，如果无，请联系支付宝客服检查用户状态是否正常。\n" +
                "PAYER_CERTIFY_CHECK_FAIL \t付款方人行认证受限 \t付款方请升级认证等级。\n" +
                "PAYER_STATUS_ERROR \t付款方用户状态不正常 \t请检查付款方是否进行了自助挂失，如果无，请联系支付宝客服检查用户状态是否正常。\n" +
                "PAYER_BALANCE_NOT_ENOUGH \t付款方余额不足 \t支付时间点付款方余额不足，请向付款账户余额充值后再原请求重试。\n" +
                "PAYER_USER_INFO_ERROR \t付款用户姓名或其它信息不一致 \t检查付款用户姓名payer_real_name与真实姓名一致性。\n" +
                "PAYMENT_INFO_INCONSISTENCY \t两次请求商户单号一样，但是参数不一致 \t如果想重试前一次的请求，请用原参数重试，如果重新发送，请更换单号。\n" +
                "CARD_BIN_ERROR \t收款人银行账号不正确 \t请确认收款人银行账号正确性，要求为借记卡卡号。\n" +
                "PAYEE_CARD_INFO_ERROR \t收款方卡信息错误 \t请联系收款方确认卡号与姓名一致性。\n" +
                "INST_PAY_UNABLE \t资金流出能力不具备 \t可能由于银行渠道在维护或无T0渠道，与联系支付宝客服确认。\n" +
                "MEMO_REQUIRED_IN_TRANSFER_ERROR \t根据监管部门的要求，单笔转账金额达到50000元时，需要填写付款理由 \t请填写remark或memo字段。\n" +
                "PERMIT_CHECK_PERM_IDENTITY_THEFT \t您的账户存在身份冒用风险，请进行身份核实解除限制 \t您的账户存在身份冒用风险，请进行身份核实解除限制\n" +
                "REMARK_HAS_SENSITIVE_WORD \t转账备注包含敏感词，请修改备注文案后重试 \t转账备注包含敏感词，请修改备注文案后重试\n" +
                "EXCEED_LIMIT_DM_AMOUNT \t日累计额度超限 \t请根据接入文档说明检查本日请求总金额+本次请求金额是否超限。\n" +
                "NO_ACCOUNT_RECEIVE_PERMISSION \t没有该账户的收款权限 \t请更改收款账号\n" +
                "BALANCE_IS_NOT_ENOUGH \t付款方余额不足 \t请更换付款方或者充值后再重试\n" +
                "NO_ACCOUNT_PAYMENT_PERMISSION \t没有该账户的支付权限 \t请更换付款方再重试\n" +
                "INVALID_PARAMETER \t参数有误参数有误 \t请根据入参说明检查请求参数合法性\n" +
                "PAYER_NOT_EXIST \t付款方不存在 \t请更换付款方再重试\n" +
                "PRODUCT_NOT_SIGN \t产品未签约 \t请签约产品之后再使用该接口\n" +
                "SYSTEM_ERROR \t系统繁忙 \t请联系支付宝工程师排查\n" +
                "PAYMENT_TIME_EXPIRE \t请求已过期 \t请求单据已过期，重新发起一笔。\n" +
                "PAYEE_NOT_EXIST \t收款用户不存在 \t请换收款方账号再重试。\n" +
                "PAYEE_ACCOUNT_STATUS_ERROR \t收款方账号异常 \t请换收款方账号再重试。\n" +
                "PERMIT_NON_BANK_LIMIT_PAYEE \t收款方未完善身份信息或未开立余额账户，无法收款 \t根据监管部门的要求，收款方未完善身份信息或未开立余额账户，无法收款\n" +
                "PERMIT_NON_BANK_LIMIT_PAYEE \t收款方未完善身份信息或未开立余额账户，无法收款 \t根据监管部门的要求，收款方未完善身份信息或未开立余额账户，无法收款\n" +
                "PAYEE_TRUSTEESHIP_ACC_OVER_LIMIT \t收款方托管子户累计收款金额超限 \t收款方托管子户累计收款金额超限，请绑定支付宝后完成收款。\n" +
                "NO_PERMISSION_ACCOUNT \t无权限操作当前付款账号 \t无权限操作当前付款账号\n" +
                "TRUSTEESHIP_ACCOUNT_NOT_EXIST \t托管子户查询不存在 \t托管子户查询不存在\n" +
                "PAYEE_ACCOUNT_NOT_EXSIT \t收款账号不存在 \t请检查收款方支付宝余额账号是否存在\n" +
                "ORDER_NOT_EXIST \toriginal_order_id错误 原单据不存在 \toriginal_order_id数据错误，原单据不存在\n" +
                "PAYEE_USERINFO_STATUS_ERROR \t收款方用户状态不正常 \t收款方用户状态不正常无法用于收款\n" +
                "PAYMENT_MONEY_NOT_ENOUGH \t可用金额为0或不足 \t资金池领取金额为0或转账金额超过资金池里可使用的金额，比如红包领取 领取金额大于可领取的金额\n" +
                "TRUSTEESHIP_RECIEVE_QUOTA_LIMIT \t收款方收款额度超限，请绑定支付宝账户 \t收款方收款额度超限，请绑定支付宝账户。\n" +
                "SECURITY_CHECK_FAILED \t本次请求有风险 \t本次请求有风险导致失败\n" +
                "NO_ORDER_PERMISSION \torinal_order_id错误，不具有操作权限 \torinal_order_id错误，不具有操作权限\n" +
                "ORDER_STATUS_INVALID \t原始单据状态异常，不可操作 \torinal_order_id对应的原始单据状态异常，不可继续操作\n" +
                "PERM_AML_NOT_REALNAME_REV \t根据监管部门的要求，需要收款用户补充身份信息才能继续操作 \t请联系收款方登录支付宝站内或手机客户端补充身份信息\n" +
                "PERM_AML_NOT_REALNAME_REV \t根据监管部门的要求，需要收款用户补充身份信息才能继续操作 \t请联系收款方登录支付宝站内或手机客户端补充身份信息\n" +
                "USER_AGREEMENT_VERIFY_FAIL \t用户协议校验失败 \t确认入参中协议号是否正确\n" +
                "PAYER_NOT_EQUAL_PAYEE_ERROR \t托管场景提现收付款方账号不一致 \t请检查收付款方账号是否一致\n" +
                "EXCEED_LIMIT_DC_RECEIVED \t收款方单日收款笔数超限 \t收款方向同一个付款用户每日只能收款固定的笔数，超限后请让用户第二天再收。\n" +
                "PAYER_PERMLIMIT_CHECK_FAILURE \t付款方限权校验不通过不允许支付 \t付款方限权校验不通过不允许支付，联系支付宝客服检查付款方受限原因。\n" +
                "PAYEE_ACC_OCUPIED \t收款方登录号有多个支付宝账号，无法确认唯一收款账号 \t收款方登录号有多个支付宝账号，无法确认唯一收款账号，请收款方变更登录号或提供其他支付宝账号进行收款。\n" +
                "PAYER_PAYEE_CANNOT_SAME \t收付款方不能相同 \t收付款方不能是同一个人，请修改收付款方信息\n" +
                "PERMIT_CHECK_PERM_LIMITED \t根据监管部门的要求，请补全您的身份信息解除限制 \t根据监管部门的要求，请补全您的身份信息解除限制\n" +
                "PERMIT_CHECK_PERM_LIMITED \t根据监管部门的要求，请补全您的身份信息解除限制 \t根据监管部门的要求，请补全您的身份信息解除限制\n" +
                "RESOURCE_LIMIT_EXCEED \t请求超过资源限制 \t发起请求并发数超出支付宝处理能力，请降低请求并发\n" +
                "INVALID_PAYER_ACCOUNT \t付款方不在设置的付款账户列表中 \t请核对付款方是否在销售方案付款账户列表中\n" +
                "EXCEED_LIMIT_DM_MAX_AMOUNT \t超出单日转账限额 \t超出单日转账限额\n" +
                "EXCEED_LIMIT_PERSONAL_SM_AMOUNT \t超出转账给个人支付宝账户的单笔限额 \t超出转账给个人支付宝账户的单笔限额\n" +
                "EXCEED_LIMIT_UNRN_DM_AMOUNT \t收款账户未实名，超出其单日收款限额 \t收款账户未实名，超出其单日收款限额\n" +
                "INVALID_CARDNO \t无效的收款卡号 \t无效的收款卡号，请确认\n" +
                "RELEASE_USER_FORBBIDEN_RECIEVE \t支付宝手机号二次放号禁止收款 \t联系收款用户，更换支付宝账号后收款\n" +
                "PAYEE_USER_TYPE_ERROR \t不支持的收款用户类型 \t不支持的收款用户类型，请联系收款用户，更换支付宝账户后收款\n" +
                "EXCEED_LIMIT_SM_MIN_AMOUNT \t请求金额低于单笔最小转账金额 \t请修改转账金额。\n" +
                "PERMIT_CHECK_RECEIVE_LIMIT \t您的账户限制收款，请咨询95188电话咨询 \t请咨询95188电话咨询\n" +
                "NOT_IN_WHITE_LIST \t您的请求不在白名单范围内 \t您的请求不在白名单范围内，请咨询95188电话咨询\n" +
                "MONEY_PAY_CLOSED \t付款账号余额关闭 \t付款账号余额关闭，请拨打95188咨询\n" +
                "NO_AVAILABLE_PAYMENT_TOOLS \t您当前无法支付，请咨询95188 \t您当前无法支付，请咨询95188\n" +
                "PAYEE_NOT_RELNAME_CERTIFY \t收款方未实名认证 \t收款方未实名认证\n" +
                "OVERSEA_TRANSFER_CLOSE \t您无法进行结汇业务，请联系95188 \t您无法进行结汇业务，请联系95188\n" +
                "PAYMENT_FAIL \t支付失败 \t支付失败 \n";
    }

    @Test
    public void handle() {
        final String[] lines=msg.split("\n");
        for (String s : lines) {
            final String[] strs=s.split("\t");
            System.out.printf("%s(\"%s\"),\n", strs[0].trim(), strs[1].trim());
        }
    }
}
