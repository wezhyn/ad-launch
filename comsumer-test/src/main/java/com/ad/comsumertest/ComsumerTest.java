package com.ad.comsumertest;

import com.ad.launch.user.AdUser;
import com.ad.launch.user.RemoteUserServiceI;
import com.ad.launch.user.exception.NotUserException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Component
public class ComsumerTest implements BeanFactoryAware {


    @Resource
    private RemoteUserServiceI<Integer> remoteUserServiceI;


    public AdUser load(int i) throws NotUserException {
        return remoteUserServiceI.loadUser(i);
    }


    @Override
    @SuppressWarnings("unchecked")
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.remoteUserServiceI=(RemoteUserServiceI<Integer>) beanFactory.getBean("remoteUserService");
    }
}
