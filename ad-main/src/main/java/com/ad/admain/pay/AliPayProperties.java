package com.ad.admain.pay;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 初始化支付宝账号信息
 *
 * @author wezhyn
 * @since 11.22.2019
 */
public class AliPayProperties {


    public static final String SERVER_URL;
    public static final String APP_ID;
    public static final String APP_PRIVATE_KEY;
    public static final String CHARSET;
    public static final String ALIPAY_PUBLIC_KEY;
    public static final String SIGN_TYPE;
    public static final String CALLBACK_NOTIFY_URL;
    public static final String AD_SYSTEM_SELLER_ID;
    public static final CertificateType CERTIFICATE_TYPE;
    public static final String CERT_LOC;
    public static final String CERT_PATH;
    public static final String ROOT_CERT_PATH;
    public static final String PUBLIC_CERT_PATH;


    static {
        Properties properties=new Properties();
        try {
            properties.load(new ClassPathResource("zfb-dev.properties").getInputStream());
            SERVER_URL = (String) properties.get("server_url");
            APP_ID = (String) properties.get("app_id");
            APP_PRIVATE_KEY = (String) properties.get("app_private_key");
            CHARSET = (String) properties.get("charset");
            ALIPAY_PUBLIC_KEY = (String) properties.get("alipay_public_key");
            SIGN_TYPE = (String) properties.get("sign_type");
            CALLBACK_NOTIFY_URL = properties.getProperty("alipay_notify_url");
            AD_SYSTEM_SELLER_ID = properties.getProperty("alipay_ad_seller_system_id");
            CERT_LOC = properties.getProperty("cert_loc");
            CERTIFICATE_TYPE = CertificateType.valueOf(properties.getProperty("certificate_type"));
            if ("classpath".equals(CERT_LOC.trim())) {
                CERT_PATH = new ClassPathResource(properties.getProperty("cert_path")).getFile().getAbsolutePath();
                ROOT_CERT_PATH = new ClassPathResource(properties.getProperty("root_cert_path")).getFile().getAbsolutePath();
                PUBLIC_CERT_PATH = new ClassPathResource(properties.getProperty("public_cert_path")).getFile().getAbsolutePath();
            } else {
                CERT_PATH = splicePath(properties.getProperty("cert_path"));
                ROOT_CERT_PATH = splicePath(properties.getProperty("root_cert_path"));
                PUBLIC_CERT_PATH = splicePath(properties.getProperty("public_cert_path"));
            }
        } catch (IOException e) {
            throw new RuntimeException("无支付宝应用设置");
        }
    }

    private static String splicePath(String fileName) {
        return Paths.get(CERT_LOC, fileName).toAbsolutePath().toString();
    }

    enum CertificateType {
        /**
         * 支付宝证书类型
         */
        CERTIFICATE, PRIVATE_KEY;
    }
}
