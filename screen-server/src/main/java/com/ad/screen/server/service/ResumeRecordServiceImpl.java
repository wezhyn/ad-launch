package com.ad.screen.server.service;

import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.dao.ResumeRecordRepository;
import com.ad.screen.server.entity.ResumeRecord;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Service
public class ResumeRecordServiceImpl extends AbstractBaseService<ResumeRecord, String> implements ResumeRecordService {

    @Autowired
    private ResumeRecordRepository resumeRecordRepository;

    @Autowired
    private GlobalIdentify globalIdentify;

    @Override
    public ResumeRecordRepository getRepository() {
        return resumeRecordRepository;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int resumeRecord() {
        final Optional<ResumeRecord> record=resumeRecordRepository.findById(globalIdentify.getId());
        if (!record.isPresent()) {
            ResumeRecord resumeRecord=new ResumeRecord();
            resumeRecord.setWordIdentity(globalIdentify.getId());
            resumeRecord.setLastResumeId(0);
            resumeRecordRepository.save(resumeRecord);
            return 0;
        } else {
            return record.get().getLastResumeId();
        }
    }
}
