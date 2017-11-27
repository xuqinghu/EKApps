package com.fugao.formula.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理集合
 *
 * @ClassName: StringUtils
 * @Description: TODO
 * @author: 陈亮    chenliang@fugao.com
 * @date: 2014年3月4日 下午9:47:23
 */
public class StringUtils {

    /**
     * 判断 字符串是否是空的
     *
     * @param string
     * @return true 空字符串，false 不为空
     */
    public static boolean StringIsEmpty(String string) {
        if (string == null || ("").equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到字符串的非空指针
     *
     * @param str 要转换的字符串
     * @return
     */
    public static String getString(String str) {
        if (str == null || str.equals("null") || str.equals("")) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * 去除字符串中的 换行符，制表符  回车符     ，（不包含 空格）
     *
     * @param str
     * @return
     * @Title: getStringContainSpecialFlag
     * @Description: TODO
     * @return: String
     */
    public static String getStringContainSpecialFlag(String str) {

        String dest = "";

        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str.trim()).matches();
        }
    }

    /**
     * 判断是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {

        if (str == null || "".equals(str.trim())) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
            return pattern.matcher(str.trim()).matches();
        }
    }

    /**
     * 拼接字符床   将一个字符 按照 分隔符拼接起来
     *
     * @param strings
     * @param flag
     * @return
     * @Title: appendStringByFlags
     * @Description: TODO
     * @return: String
     */
    public static String appendStringByFlags(ArrayList<String> strings, String flag) {
        Iterator<String> it = strings.iterator();
        StringBuffer sbBuffer = new StringBuffer();
        for (String string : strings) {
            sbBuffer.append(string);
            it.next();
            if (it.hasNext()) {
                sbBuffer.append(flag);
            }
        }
        return sbBuffer.toString();
    }

    /**
     * 是都是
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[a-z]*||[A-Z]*");
            return pattern.matcher(str.trim()).matches();
        }
    }

}
