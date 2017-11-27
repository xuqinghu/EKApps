package com.fugao.breast.constant;

import com.fugao.breast.entity.PutBreastMilk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class Constant {
    /**
     * 联新pda广播
     */
    public static String LACH_SIS = "lachesis_barcode_value_notice_broadcast";
    /**
     * honeywell扫描头的结果
     */
    public static String RECEIVER_SCAN_RESULT_HONEYWELL = "com.honeywell.tools.action.scan_result";

    /**
     * 本地数据库名称
     */
    public static String BREAST_LIST = "breast_list";
    public static String BEAST_DETAIL = "breast_detail";
    public static String PUT_LIST="put_list";
    public static String BREAST_REGIST="breast_regist";
    //已解冻奶量
    public static int THAW_AMOUNT;
    //已解冻瓶数
    public static int THAW_ACCOUNT;
    //存放奶量
    public static int PUT_AMOUNT;
    //存放瓶数
    public static int PUT_ACCOUNT;
}
