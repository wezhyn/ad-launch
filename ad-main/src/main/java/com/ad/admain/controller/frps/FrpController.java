package com.ad.admain.controller.frps;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 01.18.2020
 */
@RestController
public class FrpController {

    @RequestMapping("/status")
    public String frpStatus() {
        return "true";
    }
}
