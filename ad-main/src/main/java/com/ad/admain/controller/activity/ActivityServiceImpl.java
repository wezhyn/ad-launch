package com.ad.admain.controller.activity;

import com.wezhyn.project.AbstractBaseService;
import org.springframework.stereotype.Service;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@Service
public class ActivityServiceImpl extends AbstractBaseService<Activity, Integer> implements ActivityService {

    private final ActivityRepository activityRepository;


    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository=activityRepository;
    }

    @Override
    public ActivityRepository getRepository() {
        return activityRepository;
    }
}
