package com.fugao.formula.constant;

import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.MilkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Constant {
    /**
     * 联新pda广播
     */
    public static String LACH_SIS = "lachesis_barcode_value_notice_broadcast";
    public static String GETDIVISION = "getDivision";
    public static String GETMILKNAMES = "getMilkNames";
    public static String TIME_LIST = "time_list";
    public static String MILK_NAME = "milk_name";
    public static Boolean CANCEL_BOXING=false;
    public static List<AdviceEntity> ADVICE_BOX_LIST = new ArrayList<>();
    public static List<MilkBean> SELECT_MILK_NAME = new ArrayList<>();
    public static String SELECT_PLACE="未核对";
}
