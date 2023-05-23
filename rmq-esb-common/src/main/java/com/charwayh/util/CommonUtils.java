package com.charwayh.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.util
 * @date:2023/5/23
 */
public class CommonUtils {

    /**
     * @Author SangYD
     * @Description 生成 客户端调用 connect方法时产生的标识clientId，简称cid
     * @Date 13:42 2019/4/17
     * @Return cid
     **/
    public static String createCid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }


    public static boolean ipPermissionCheck(String ip,String... ips) {
        if (StringUtils.isNotBlank(ip) && ips != null && ips.length > 0) {
            HashSet<String> ipSet = new HashSet<>(Arrays.asList(ips));
            if (ipSet.contains(ip)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

    /**
     * list 转换成数组
     *
     * @param list
     * @return
     */
    public static String[] listToArray(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public static int getTimeInfo(long date, int type) {
        if (date <= 0 || type < 0) {return -1;}
        /**赋值处理时间*/
        Calendar handleDate = Calendar.getInstance();
        handleDate.setTimeInMillis(date);
        return handleDate.get(type);
    }

    public static String transforType(Integer type) {
        switch (type) {
            case 1:
                return "String";
            case 2:
                return "Integer";
            case 3:
                return "Boolean";
            case 4:
                return "Long";
            case 5:
                return "Short";
            case 6:
                return "Double";
            case 7:
                return "Float";
            case 8:
                return "Byte";
            case 9:
                return "Character";
            default:
                return "String";
        }
    }

}
