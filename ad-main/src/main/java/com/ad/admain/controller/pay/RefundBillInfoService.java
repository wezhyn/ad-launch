package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.wezhyn.project.BaseService;

public interface RefundBillInfoService extends BaseService<RefundBillInfo, Integer> {

    Double sumRefunds(Integer uid);
}
