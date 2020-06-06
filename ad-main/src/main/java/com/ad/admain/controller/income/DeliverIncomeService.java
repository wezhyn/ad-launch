package com.ad.admain.controller.income;

import com.ad.admain.controller.account.impl.SocialUserService;
import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.controller.account.user.SocialUser;
import com.ad.admain.controller.pay.to.OrderVerify;
import com.ad.admain.controller.pay.to.TransferOrder;
import com.ad.admain.pay.AliPayHolder;
import com.ad.admain.pay.WithDrawNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ad.admain.controller.pay.PayController.TRANSFER_MODEL_MAPPER;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Service
public class DeliverIncomeService {


    @Autowired
    private DeliverIncomeRepository deliverIncomeRepository;
    @Autowired
    private SocialUserService socialUserService;
    @Autowired
    private DeliverIncomeDetailsRepository deliverIncomeDetailsRepository;


    public Page<DriverInComeDetails> getDetails(Integer did, Pageable pageable) {
        return deliverIncomeDetailsRepository.findByDriverId(did, pageable);
    }

    public Page<DriverInComeDetails> getWithdrawRecord(Integer did, double minAmount, Pageable pageable) {
        return deliverIncomeDetailsRepository.findByDriverIdAndAmountLessThan(did, minAmount, pageable);
    }


    public List<DriverInComeDayRecord> weekRevenue(Integer did, LocalDate endDate) {
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.MIN);
        return deliverIncomeDetailsRepository.getDriverRevenueWithDays(did, endTime.minusDays(7), endTime.plusDays(1));
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveOrIncr(Integer driverId, Double amount) {
        deliverIncomeRepository.saveOrUpdate(driverId, amount);
        deliverIncomeDetailsRepository.save(new DriverInComeDetails(driverId, amount));
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawAmount(Integer userId, Double amount, SocialType type) {
        if (deliverIncomeRepository.getUserAmountSync(userId) > amount) {
            SocialUser socialUser = socialUserService.getUser(userId, type);
            if (socialUser == null || socialUser.getSocialAccountId() == null) {
                throw new RuntimeException("当前账户无绑定第三方支付信息");
            }
            deliverIncomeRepository.decrUserAmount(userId, amount);
            final DriverInComeDetails savedDetails = deliverIncomeDetailsRepository.save(new DriverInComeDetails(userId, -amount));
            TransferOrder order = TransferOrder.builder(amount, userId)
                    .verify(OrderVerify.PASSING_VERIFY)
                    .identify(socialUser.getSocialAccountId())
                    .identityType("ALIPAY_USER_ID")
                    .remark(String.format("%s -test", LocalDateTime.now()))
                    .build();

            order.setId(savedDetails.getId());
            WithDrawNotification response = AliPayHolder.handleWithDraw(order, TRANSFER_MODEL_MAPPER);
            if (response.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException("提现失败：" + response.msg());
            }
        } else {
            return false;
        }
    }

    public double totalAmount(Integer userId) {
        return deliverIncomeRepository.getUserAmount(userId);
    }
}
