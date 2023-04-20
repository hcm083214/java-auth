package com.hcm.common.utils;

import java.security.SecureRandom;

/**
 * @author pc
 */
public class BaseUtils {
    /**
     * 是否为空字符串
     *
     * @param str str
     * @return {@link Boolean}
     */
    public static Boolean isEmptyString(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 得到随机整数
     *
     * @param start 开始
     * @param end   结束
     * @return {@link Integer}
     */
    public static Integer getRandomInt(Integer start, Integer end) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(end - start) + start;
    }
}
