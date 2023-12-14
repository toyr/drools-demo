package com.example.droolsdemo.parse;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/6/1 8:32 下午
 */
public class JavassistDateUtils {

    /**
     * 判断是否在范围内  日期格式为"yyyy-MM-dd hh:mm:ss"
     *
     * @param str
     * @param beginStr
     * @param endStr
     * @return
     */
    public static boolean compare(String str, String beginStr, String endStr) {
        if (StringUtils.isBlank(beginStr)) {
            beginStr = "1900-01-01 00:00:00";
        }
        if (StringUtils.isBlank(endStr)) {
            endStr = "2100-12-31 00:00:00";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime l = LocalDateTime.parse(str, formatter);
        LocalDateTime begin = LocalDateTime.parse(beginStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return l.isAfter(begin) && l.isBefore(end);
    }

}
