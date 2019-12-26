package com.wezhyn.project.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author wezhyn
 * @since 12.26.2019
 */
public class HttpServletRequests {

    private static final Charset DEFAULT_CHARSET=StandardCharsets.UTF_8;

    public static Charset getRequestCharset(HttpServletRequest request) {
        String contextTypeStr=request.getHeader("Content-Type");
        String[] contextType=StringUtils.isEmpty(contextTypeStr) ? null : contextTypeStr.split(";");
        if (contextType!=null && contextType.length==2) {
            Properties properties=new Properties();
            try (InputStream inputStream=new ByteArrayInputStream(contextType[1].getBytes())) {
                properties.load(inputStream);
            } catch (IOException ignore) {
            }
            String charset=properties.getProperty("charset");
            return StringUtils.isEmpty(charset) ? DEFAULT_CHARSET : Charset.forName(charset);
        } else {
            return DEFAULT_CHARSET;
        }

    }
}
