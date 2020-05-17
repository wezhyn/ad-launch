package com.ad.admain.controller.account.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
public interface SocialUserRepository extends JpaRepository<SocialUser, Integer> {


    Boolean existsBySocialTypeAndUid(SocialType type, Integer uid);
}
