package com.ad.admain.controller.account;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import com.ad.admain.convert.GenericUserMapper;
import com.ad.admain.security.AdAuthentication;
import com.alibaba.fastjson.JSONObject;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/authentication")
@RestController
public class CertificationController extends AbstractBaseController<UserDto, Integer, GenericUser> {

    @Autowired
    private QiNiuProperties qiNiuProperties;
    @Autowired
    private GenericUserService genericUserService;
    @Autowired
    private GenericUserMapper genericUserMapper;

    @GetMapping("/list")
    public ResponseResult listAuth(@RequestParam(value = "auth", defaultValue = "true") boolean isAuth,
                                   @RequestParam(name = "limit", defaultValue = "10") int limit,
                                   @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        return doResponse(getService().getUserListWithAuth(isAuth, PageRequest.of(page - 1, limit)));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseResult verify(UserDto userDto) {
        return update(userDto);
    }

    @PostMapping("/update")
    public ResponseResult verifyRealNameAuthentication(@RequestBody JSONObject info, @AuthenticationPrincipal AdAuthentication authentication) {
        final String realname = info.getString("realname");
        final String idCard = info.getString("idCard");
        final String idCardPreImg = info.getString("idCardPreImg");
        final String idCardAftImg = info.getString("idCardAftImg");
        final Optional<GenericUser> updateUser = getService().updateUserAuthenticationInfo(realname, idCard, idCardPreImg, idCardAftImg, authentication.getId());
        return doResponse(updateUser, "更新成功", "更新失败");
    }

    @Override
    public GenericUserService getService() {
        return genericUserService;
    }

    @Override
    public GenericUserMapper getConvertMapper() {
        return genericUserMapper;
    }
}
