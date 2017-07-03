package com.fugao.formula.constant;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class FormulaApi {
    public static String API_NAME = "api/";
    /**
     * 登录的基本URL
     */
    public static String LOGIN_URL = API_NAME + "User";
    public static String FORMULA_URL = API_NAME + "BreastMilk";
    public static String FORMULA_POST = API_NAME + "FormulaMilk";

    //取奶名、医嘱、箱号、今日装箱
    public static String getAdviceData(String type, String ssbq, String yzdm, String zxsj) {
        String resultString = FORMULA_URL + "?type=" + "" + type + "&ssbq=" + ssbq + "&yzdm=" + yzdm + "&zxsj=" + zxsj;
        return resultString;
    }

    //今日装箱
    public static String getBoxData(String type, String ssbq, String date, String gh) {
        String resultString = FORMULA_URL + "?type=" + "" + type + "&ssbq=" + ssbq + "&date=" + date + "&gh=" + gh;
        return resultString;
    }
}
