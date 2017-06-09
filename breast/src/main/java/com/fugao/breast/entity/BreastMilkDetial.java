package com.fugao.breast.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class BreastMilkDetial implements Serializable {
    //住院号
    public String Pid;
    //奶量
    public String Amount;
    //乳箱ID
    public String MilkBoxId;
    //乳箱编号
    public String MilkBoxNo;
    //分格ID
    public String CoorDinateID;
    //分格坐标
    public String CoorDinate;
    //备注
    public String Remarks;
    //状态
    public String State;
    //汇总ID
    public String SummaryId;
    //打印状态
    public String PrintState;
    //解冻日期
    public String ThawDate;
    //解冻时间
    public String ThawTime;
    //解冻人员工号
    public String ThawGH;
    //泵乳日期
    public String MilkPumpDate;
    //泵乳时间
    public String MilkPumpTime;
    //二维码
    public String QRcode;
    //有效期
    public String YXQ;
    //乳箱状态 1：被释放  2:被占用
    public String MilkBoxState;
}
