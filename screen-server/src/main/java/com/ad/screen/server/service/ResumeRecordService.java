package com.ad.screen.server.service;

import com.ad.screen.server.entity.ResumeRecord;
import com.wezhyn.project.BaseService;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
public interface ResumeRecordService extends BaseService<ResumeRecord, String> {

    /**
     * 获取当前服务器的重启记录
     *
     * @return resumeRecord 0: if not
     */
    int resumeRecord();

}
