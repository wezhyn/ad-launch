package com.ad.screen.server.service;

import com.ad.screen.server.dao.ResumeRecordRepository;
import com.ad.screen.server.entity.ResumeRecord;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Service
public class ResumeRecordServiceImpl extends AbstractBaseService<ResumeRecord, String> implements ResumeRecordService {

    @Autowired
    private ResumeRecordRepository resumeRecordRepository;


    @Override
    public ResumeRecordRepository getRepository() {
        return resumeRecordRepository;
    }
}
