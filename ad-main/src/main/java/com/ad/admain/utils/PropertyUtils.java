package com.ad.admain.utils;

import com.ad.admain.dto.GenericUser;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.ad.admain.to.UserTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;


public class PropertyUtils {

    /**
     * @param source 数据源(即通过主键查询出来的数据)
     * @param target 目标数据(请求跟新的数据)
     *
     */
    public static void copyProperties(Object source,Object target){
        BeanUtils.copyProperties(source,target,getNullPropertyNames(source));
    }

    public static void main(String[] args){
//        GenericUser genericUser1 = new GenericUser();
//        genericUser1.setRealName("兆兆");
//        genericUser1.setUsername("zhaoo");
//        genericUser1.setSex(SexEnum.MALE);
//        genericUser1.setRoles(AuthenticationEnum.ADMIN);
//        System.out.println(genericUser1.toString());
//
//        GenericUser genericUser2 = new GenericUser();
//        genericUser2.setRealName("dzj");
//        genericUser2.setSex(SexEnum.MALE);
//        genericUser2.setRoles(AuthenticationEnum.USER);
//        genericUser2.setEmail("fuckyou@dzj.com");
//        copyProperties(genericUser2,genericUser1);
//        System.out.println("=======================");
//        System.out.println(genericUser2.toString());
//        System.out.println(genericUser1.toString());
        //        System.out.println("========================");
//        GenericUser genericUser2 = new GenericUser();
//        genericUser2.setEmail("fuck you");
//        copyProperties(genericUser1,genericUser2);
//        String[] pro = getNullPropertyNames(genericUser2);
//        for (String str :pro){
//            System.out.println(str);
//        }
//        UserTo userTo = UserTo.fromGenericUser(genericUser1);
//        System.out.println(userTo.toString());
//        GenericUser genericUser2 = userTo.toGenericUser();
//        System.out.println(genericUser2);


    }






    public static String[] getNullPropertyNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor propertyDescriptor:pds){
            Object srcValue = src.getPropertyValue(propertyDescriptor.getName());
            if (srcValue==null)
                emptyNames.add(propertyDescriptor.getName());
        }
        String [] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
