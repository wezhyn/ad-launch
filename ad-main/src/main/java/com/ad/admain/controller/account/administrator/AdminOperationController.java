package com.ad.admain.controller.account.administrator;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.account.user.UserDto;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.GenericUserMapper;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@RestController
@RequestMapping("/api/admin/operate")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOperationController extends AbstractBaseController<UserDto, Integer, GenericUser> {

    private final GenericUserService genericUserService;
    private final GenericUserMapper genericUserMapper;


    public AdminOperationController(GenericUserService genericUserService, GenericUserMapper genericUserMapper) {
        this.genericUserService=genericUserService;
        this.genericUserMapper=genericUserMapper;
    }


    @PostMapping("/verify/user")
    public ResponseResult verifyRealName(@RequestBody UserDto userDto) {
        final UserDto updateUserDto=UserDto.builder()
                .id(userDto.getId())
                .status(userDto.getStatus())
                .build();
        return update(updateUserDto);
    }


    @Override
    public GenericUserService getService() {
        return genericUserService;
    }

    @Override
    public AbstractMapper<GenericUser, UserDto> getConvertMapper() {
        return genericUserMapper;
    }
}
