package com.ad.admain.utils;

import com.ad.admain.constants.QiNiuProperties;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QiniuTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QiNiuProperties qiNiuProperties;

    @Test
    public void uploadLocalPic() {
        Configuration configuration=new Configuration(Zone.zone0());
        UploadManager uploadManager=new UploadManager(configuration);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String pic="C:\\Users\\13366\\Pictures\\Saved Pictures\\6.jpeg";
        Auth auth=Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
        StringMap putPolicy=new StringMap();
        putPolicy.put("forceSaveKey", true);
        putPolicy.put("saveKey", "$(year)/$(mon)/$(day) $(hour):$(min):$(sec)&$(endUser)&$(fname)");
        putPolicy.put("endUser", "wezhyn-test");
        String uploadToken=auth.uploadToken(qiNiuProperties.getBucketName(),null,0L,putPolicy);

        try {
            Response response=uploadManager.put(pic,null, uploadToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }
}


