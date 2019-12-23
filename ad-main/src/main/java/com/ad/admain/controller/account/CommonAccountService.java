package com.ad.admain.controller.account;

import com.ad.admain.controller.impl.IFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.03.2019
 */
public interface CommonAccountService<T, ID> {


    /**
     * 获取用户头像
     *
     * @param username username
     * @return username：null | “” 返回Optional.empty()
     */
    Optional<String> getUserAvatar(String username);

    /**
     * 修改用户头像
     *
     * @param username 主键
     * @param avatar   头像地址 {@link IFileUpload#getRelativeName()}
     * @return 1
     */
    int modifyUserAvatar(String username, String avatar);

    /**
     * 获取对应的repository
     *
     * @return jpsRepository
     */
    JpaRepository<T, ID> getRepository();

}
