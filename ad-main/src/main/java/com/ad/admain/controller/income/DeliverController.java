package com.ad.admain.controller.income;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.RefundBillInfoService;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@RestController
@RequestMapping("/api/wallet")
@PreAuthorize("isAuthenticated()")
public class DeliverController {

    private final DeliverIncomeService deliverIncomeService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private RefundBillInfoService refundBillInfoService;

    public DeliverController(DeliverIncomeService deliverIncomeService) {
        this.deliverIncomeService = deliverIncomeService;
    }

    @GetMapping(value = "/details/day")
    public ResponseResult detailsDay(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        final List<DriverInComeDayRecord> records = deliverIncomeService.weekRevenue(adAuthentication.getId(),
            LocalDate.now());
        final ResponseResult.Builder builder = ResponseResult.forSuccessBuilder();
        for (DriverInComeDayRecord record : records) {
            builder.withData(record.getDate(), record.getAmount());
        }
        return builder.build();
    }

    @GetMapping(value = "/details")
    public ResponseResult details(@AuthenticationPrincipal AdAuthentication adAuthentication,
        @RequestParam(name = "limit", defaultValue = "10") int limit,
        @RequestParam(name = "page", defaultValue = "1") int page) {
        // 当前设备收益
        final PageRequest pageable = PageRequest.of(page - 1, Integer.MAX_VALUE);
        final Page<DriverInComeDetails> details = deliverIncomeService.getDetails(adAuthentication.getId(), pageable);
        // 订单支付费用
        final Page<AdOrder> orders = adOrderService.listUserOrders(adAuthentication.getId(), pageable);
        // 退款费用
        final RefundBillInfo billExample = new RefundBillInfo();
        billExample.setBuyerId(adAuthentication.getId().toString());
        billExample.setDelete(false);
        final Page<RefundBillInfo> refunds = refundBillInfoService.getList(Example.of(billExample), pageable);

        List<IncomeDetailsVo> results = new ArrayList<>();
        if (details != null) {
            details.forEach(d -> results.add(new IncomeDetailsVo(d.getId(), d.getAmount(), d.getRecordTime(), "收益")));
        }
        if (orders != null) {
            orders.forEach(
                o -> results.add(new IncomeDetailsVo(o.getId(), -1 * o.getTotalAmount(), o.getCreateTime(), "订单支付")));
        }
        if (refunds != null) {
            refunds.forEach(
                b -> results.add(new IncomeDetailsVo(b.getId(), b.getTotalAmount(), b.getGmtCreate(), "退款")));
        }
        return ResponseResult.forSuccessBuilder()
            .withData("items", results)
            .withData("total", results.size())
            .build();
    }

    @GetMapping("/profit")
    public ResponseResult userProfit(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        final double equipIncome = deliverIncomeService.totalAmount(adAuthentication.getId());
        final Double orderOutcome = adOrderService.sumUserOrders(adAuthentication.getId());
        final Double refundIncome = refundBillInfoService.sumRefunds(adAuthentication.getId());
        BigDecimal result = BigDecimal.valueOf(equipIncome).subtract(BigDecimal.valueOf(orderOutcome)).add(
            BigDecimal.valueOf(refundIncome));
        result = result.setScale(2, RoundingMode.HALF_UP);

        return ResponseResult.forSuccessBuilder()
            .withData("mount", result.doubleValue())
            .build();
    }

    @GetMapping(value = "/withdraw")
    public ResponseResult withdrawRecord(@AuthenticationPrincipal AdAuthentication adAuthentication,
        @RequestParam(name = "limit", defaultValue = "10") int limit,
        @RequestParam(name = "page", defaultValue = "1") int page) {
        final Page<DriverInComeDetails> details = deliverIncomeService.getWithdrawRecord(adAuthentication.getId(), 0d,
            PageRequest.of(
                page - 1, limit, Sort.Direction.DESC, "id"));
        return ResponseResult.forSuccessBuilder()
            .withData("items", details.getContent())
            .withData("total", details.getTotalElements())
            .build();

    }

    @PostMapping(value = "/withdraw")
    public ResponseResult withdraw(@RequestBody WithdrawWrap withdrawWrap,
        @AuthenticationPrincipal AdAuthentication adAuthentication) {
        double money = withdrawWrap.getMount();
        if (money == 0) {
            return ResponseResult.forFailureBuilder()
                .withMessage("提现金额为 0 元")
                .build();
        }
        if (deliverIncomeService.withdrawAmount(adAuthentication.getId(), money, SocialType.ALIPAY)) {
            return ResponseResult.forSuccessBuilder()
                .withMessage("提现成功").build();
        } else {
            return ResponseResult.forFailureBuilder()
                .withMessage("系统繁忙，稍后再试")
                .build();
        }
    }

    @Data
    public static class WithdrawWrap {
        private Double mount;
    }
}
