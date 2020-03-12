package com.ad.admain.remote;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.remote.convert.RemoteUserMapper;
import com.ad.launch.user.AdUser;
import com.ad.launch.user.RemoteUserServiceI;
import com.ad.launch.user.exception.NotUserException;

/**
 * 远程用户信息获取
 *
 * @author wezhyn
 * @since 03.12.2020
 */
public class RemoteUserService implements RemoteUserServiceI<Integer> {


    private final GenericUserService genericUserService;

    private final RemoteUserMapper remoteUserMapper;

    public RemoteUserService(GenericUserService genericUserService, RemoteUserMapper remoteUserMapper) {
        this.genericUserService=genericUserService;
        this.remoteUserMapper=remoteUserMapper;
    }

    @Override
    public AdUser loadUser(Integer id) throws NotUserException {
        final GenericUser user=genericUserService.getById(id)
                .orElseThrow(()->new NotUserException("无当前id用户"));
//        throw new NotUserException("test");
        return remoteUserMapper.toDto(user);
    }
}
