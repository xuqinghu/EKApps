package com.fugao.formula.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class PostEntity implements Serializable {
    //奶箱编号
    public String MilkBoxID;
    //奶箱状态
    public String MilkBoxStatus;
    //奶箱规格
    public String MilkBoxSpec;
    //奶瓶数
    public String MilkQuantity;
    //病区号
    public String WardCode;
    //病区名
    public String WardName;
    //装箱时间
    public String LoadTime;
    //装箱人姓名
    public String LoadName;
    //装箱人工号
    public String LoadGH;
    //配送人姓名
    public String TransName;
    //配送人工号
    public String TransGH;
    //配送时间
    public String TransTime;
    //接收人姓名
    public String RevName;
    //接收人工号
    public String RevGH;
    //接收时间
    public String RevTime;
    public String HZID;
    public String NPID;
    public String OperatorDate;
    public String OperatorTime;
    public String OperatorGH;
    public String OperatorName;
    public String IsZx;
    public String CurOperation;
    //执行时间
    public String ExecFrequency;
    //奶名
    public String MilkName;
}
