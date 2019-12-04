package com.ad.admain.utils;

import java.util.Objects;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public class Strings {


    public static String underlineNameToPropertyName(String underlineName) {
        final String extractName=underlineName==null ? "" : underlineName;
        char underlineCharacter='_';
        StringBuilder nameBuilder=new StringBuilder();
        boolean isUnderline=false;
        for (int i=0; i < extractName.length(); i++) {
            char c=extractName.charAt(i);
            if (Objects.equals(underlineCharacter, c)) {
                isUnderline=true;
                continue;
            }
            if (isUnderline) {
                isUnderline=false;
                c=Character.toUpperCase(c);
            }
            nameBuilder.append(c);
        }
        return nameBuilder.toString();
    }

    public static String nameToUnderlinePattern(String name) {
        return null;
    }


    /**
     * 发现第一个大写字符
     *
     * @param str str
     * @return -1 :无大写字符
     */
    public static int findFirstUpperCharacter(String str) {
        if (isEmpty(str)) {
            return -1;
        }
        for (int i=0; i < str.length(); i++) {
            char c=str.charAt(i);
            if (Character.isUpperCase(c)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isEmpty(String string) {
        return string==null || string.isEmpty();
    }

    /**
     * 提取标准的getter 方法的属性名至下划线形式
     *
     * @param getterName like getFullName
     * @return full_name
     */
    public static String extractGetterAndSetterUnderlineProperty(String getterName) {
        StringBuilder nameBuilder=new StringBuilder();
        for (int i=0; i < getterName.length(); i++) {
            char s=getterName.charAt(i);
            if (Character.isUpperCase(s)) {
                nameBuilder.append("_");
                s=Character.toLowerCase(s);
            }
            nameBuilder.append(s);
        }
        String result=nameBuilder.toString();
        return result.substring(result.indexOf("_") + 1);
    }

    /**
     * 提取标准的 getter 属性名,省略了第一个大写之前的所有字符
     *
     * @param getterName getFullName
     * @return fullName
     */
    public static String extractGetterAndSetterProperty(String getterName) {
        String propertyName=getterName==null ? "" : getterName;
        int index=findFirstUpperCharacter(propertyName);
        if (index!=-1) {
            return Character.toLowerCase(propertyName.charAt(index)) + propertyName.substring(index + 1);
        }
        return "";
    }
}
