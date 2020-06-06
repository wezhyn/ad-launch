package com.ad.admain.controller.income;

import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@RestController
@RequestMapping("/api/wallet")
@PreAuthorize("isAuthenticated()")
public class DeliverController {

    private final DeliverIncomeService deliverIncomeService;

    public DeliverController(DeliverIncomeService deliverIncomeService) {
        this.deliverIncomeService = deliverIncomeService;
    }


    @GetMapping(value = "/details/day")
    public ResponseResult detailsDay(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        final List<DriverInComeDayRecord> records = deliverIncomeService.weekRevenue(adAuthentication.getId(), LocalDate.now());
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
        final Page<DriverInComeDetails> details = deliverIncomeService.getDetails(adAuthentication.getId(), PageRequest.of(page - 1, limit));
        return ResponseResult.forSuccessBuilder()
                .withData("items", details.getContent())
                .withData("total", details.getTotalElements())
                .build();
    }

    @GetMapping(value = "/withdraw")
    public ResponseResult withdrawRecord(@AuthenticationPrincipal AdAuthentication adAuthentication,
                                         @RequestParam(name = "limit", defaultValue = "10") int limit,
                                         @RequestParam(name = "page", defaultValue = "1") int page) {
        final Page<DriverInComeDetails> details = deliverIncomeService.getWithdrawRecord(adAuthentication.getId(), 0d, PageRequest.of(
                page - 1, limit, Sort.Direction.DESC, "id"));
        return ResponseResult.forSuccessBuilder()
                .withData("items", details.getContent())
                .withData("total", details.getTotalElements())
                .build();

    }

    @PostMapping(value = "/withdraw")
    public ResponseResult withdraw(@RequestBody WithdrawWrap withdrawWrap, @AuthenticationPrincipal AdAuthentication adAuthentication) {
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

    @GetMapping("/profit")
    public ResponseResult userProfit(@AuthenticationPrincipal AdAuthentication adAuthentication) {
        return ResponseResult.forSuccessBuilder()
                .withData("mount", deliverIncomeService.totalAmount(adAuthentication.getId()))
                .build();
    }

    @Data
    public static class WithdrawWrap {
        private Double mount;
    }
}
