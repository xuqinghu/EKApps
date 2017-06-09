package com.fugao.breast.constant;

/**
 * Created by huxq on 2017/5/31 0031.
 */

public class BreastApi {
    public static String IP = "";
    public static String API_NAME = "api";
    // 公用的地址
    public static String BASE_URL = API_NAME + "/";
    public static String MODULE = "BreastMilk";
    public static String BREAST = BASE_URL + MODULE;

    public static String getBreastData(String patId, String deptId, String wardId, String date, String type) {
        String resultString = IP + BREAST + "?patId=" + "" + patId + "&deptid=" + deptId + "&WardId=" + wardId + "&date=" + date
                + "&type=" + type;
        return resultString;
    }

}
