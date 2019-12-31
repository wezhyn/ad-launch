package com.ad.admain.controller.activity;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.ActivityMapper;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.BaseService;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController extends AbstractBaseController<ActivityDto, Integer, Activity> {


    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    public ActivityController(ActivityService activityService, ActivityMapper activityMapper) {
        this.activityService=activityService;
        this.activityMapper=activityMapper;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseResult createActivity(@RequestBody ActivityDto activityDto, @AuthenticationPrincipal AdAuthentication authentication) {
        activityDto.setAid(authentication.getId());
        return createTo(activityDto);
    }


    @GetMapping("/list")
    public ResponseResult listActivities(
            @RequestParam(name="limit", defaultValue="10") int limit,
            @RequestParam(name="page", defaultValue="1") int page) {
        return listDto(limit, page);
    }

    @PostMapping("/delete")
    public ResponseResult deleteActivity(@RequestBody ActivityDto activityDto) {
        return delete(activityDto);
    }

    @PostMapping("/update")
    public ResponseResult updateActivity(@RequestBody ActivityDto activityDto, @AuthenticationPrincipal AdAuthentication authentication) {
        activityDto.setAid(authentication.getId());
        return update(activityDto);
    }

    @GetMapping("/{id}")
    public ResponseResult showCurrentActivity(@PathVariable Integer id) {
        return currentIdResult(id);
    }


    @Override
    public BaseService<Activity, Integer> getService() {
        return activityService;
    }

    @Override
    public AbstractMapper<Activity, ActivityDto> getConvertMapper() {
        return activityMapper;
    }

}
