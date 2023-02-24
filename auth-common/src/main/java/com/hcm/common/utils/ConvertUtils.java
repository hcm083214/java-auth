package com.hcm.common.utils;

/**
 * 类型转换器
 *
 * @author pc
 * @date 2023/02/22
 */
public class ConvertUtils {
    /**
     * 转换为字符串
     * 如果给定的值为null，或者转换失败，返回默认值
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 默认值
     * @return {@link String}
     */
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 转换为字符串
     *
     * @param value 被转换的值
     * @return {@link String}
     */
    public static String toStr(Object value) {
        return toStr(value, null);
    }

    /**
     * 转换为boolean
     * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        switch (valueStr) {
            case "true":
            case "yes":
            case "ok":
            case "1":
                return true;
            case "false":
            case "no":
            case "0":
                return false;
            default:
                return defaultValue;
        }
    }

    /**
     * 转换为boolean
     * 如果给定的值为空，或者转换失败，返回默认值null
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }
}
