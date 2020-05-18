package com.ad.admain.controller.account.impl;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.user.*;
import com.ad.admain.security.jwt.JwtDetailService;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 当查找不到时不返回 Null ,默认返回 Empty_User
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Service
public class GenericUserServiceImpl extends AbstractBaseService<GenericUser, Integer> implements GenericUserService, SocialUserService {

    private final GenericUserRepository genericUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtDetailService jwtDetailService;
    @Autowired
    private CertificationCardRepository certificationCardRepository;

    @Autowired
    private SocialUserRepository socialUserRepository;

    @Autowired
    public GenericUserServiceImpl(GenericUserRepository genericUserRepository) {
        this.genericUserRepository = genericUserRepository;
    }

    /*
        /**********************************************************
        /* 成员方法
        /**********************************************************
    */

    @Override
    public GenericUser save(GenericUser object) {
        return super.save(object);
    }

    @Override
    public GenericUserRepository getRepository() {
        return this.genericUserRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modifyUserPasswordById(Integer id, String username, String password) {
        String newPasword = passwordEncoder.encode(password);
        String secret = com.wezhyn.project.utils.StringUtils.getRandomString(50);
//        todo:解耦
        jwtDetailService.saveSecretByUsername(id, username, secret);
        return genericUserRepository.updateUserPassword(id, newPasword);
    }

    @Override
    public Page<GenericUser> getUserListWithAuth(boolean auth, Pageable pageable) {
        return getRepository().findGenericUsersByEnable(auth ? GenericUser.UserEnable.NORMAL : GenericUser.UserEnable.NOT_AUTHENTICATION,
                pageable);
    }

    @Override
    public Optional<GenericUser> getUserByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    @Override
    public Optional<String> getUserAvatar(String username) {
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        Optional<GenericUser> user = getRepository().findByUsername(username);
        return user.map(GenericUser::getAvatar);
    }

    @Override
    public int updateUserAvatar(String username, String avatarKey) {
        return getRepository().updateUserAvatar(username, avatarKey);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<GenericUser> updateUserAuthenticationInfo(String realName, String idCard, String preImg, String aftImg, Integer id) {
        return genericUserRepository.findById(id)
                .filter(g -> g.getEnable() == GenericUser.UserEnable.NOT_AUTHENTICATION)
                .map(u -> {
                    CertificationCard certificationCard = u.getCertificationCard();
                    if (certificationCard == null) {
                        certificationCard = new CertificationCard();
                    }
                    certificationCard.setIdCard(idCard);
                    certificationCard.setRealName(realName);
                    certificationCard.setIdCardPreImg(preImg);
                    certificationCard.setIdCardAftImg(aftImg);
                    certificationCard.setUid(id);
                    certificationCard = certificationCardRepository.save(certificationCard);
                    u.setCertificationCard(certificationCard);
                    return genericUserRepository.save(u);
                });
    }

    @Override
    public Optional<GenericUser> getOneByUsernameOrPhone(String s) {
        if (Character.isDigit(s.charAt(0))) {
            return getRepository().findByMobilePhone(s);
        }
        return getRepository().findByUsername(s);
    }

    @Override
    public int modifyUserAvatar(String username, String avatar) {
        return genericUserRepository.updateUserAvatar(username, avatar);
    }

    @Override
    public boolean isAuth(Integer userId, SocialType type) {
        return socialUserRepository.existsBySocialTypeAndUid(type, userId);
    }

    @Override
    public SocialUser getUser(Integer userId, SocialType type) {
        return socialUserRepository.findByUidAndSocialType(userId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SocialUser bindUser(Integer uid, String socialId, SocialType type) {
        SocialUser socialUser = new SocialUser(null, null, uid, socialId, type, null);
        return this.socialUserRepository.save(socialUser);
    }
}
