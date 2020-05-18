package com.ad.admain.controller.account.impl;

import com.ad.admain.controller.account.user.SocialType;
import com.ad.admain.controller.account.user.SocialUser;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
public interface SocialUserService {


    boolean isAuth(Integer userId, SocialType type);


    SocialUser bindUser(Integer uid, String socialId, SocialType type);


    SocialUser getUser(Integer userId, SocialType type);
}
