package com.ad.admain.pay;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * 初始化支付宝账号信息
 *
 * @author wezhyn
 * @since 11.22.2019
 */
public class ZfbPayProperties {

    public static final String SERVER_URL;
    public static final String APP_ID;
    public static final String APP_PRIVATE_KEY;
    public static final String CHARSET;
    public static final String ALIPAY_PUBLIC_KEY;
    public static final String SIGN_TYPE;
    public static final String CALLBACK_NOTIFY_URL;
    public static final String AD_SYSTEM_SELLER_ID;


    static {
        Properties properties=new Properties();
        try {
            properties.load(new ClassPathResource("zfb-dev.properties").getInputStream());
            SERVER_URL=(String) properties.get("server_url");
            APP_ID=(String) properties.get("app_id");
            APP_PRIVATE_KEY=(String) properties.get("app_private_key");
            CHARSET=(String) properties.get("charset");
            ALIPAY_PUBLIC_KEY=(String) properties.get("alipay_public_key");
            SIGN_TYPE=(String) properties.get("sign_type");
            CALLBACK_NOTIFY_URL=properties.getProperty("alipay_notify_url");
            AD_SYSTEM_SELLER_ID=properties.getProperty("alipay_ad_seller_system_id");
        } catch (IOException e) {
            throw new RuntimeException("无支付宝应用设置");
        }
    }

}
