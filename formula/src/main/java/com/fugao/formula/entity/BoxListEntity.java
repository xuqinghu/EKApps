package com.fugao.formula.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class BoxListEntity implements Serializable {
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
    //汇总ID
    public String HZID;
    //剂量
    public String MilkJL;
    public List<AdviceEntity> MilkDetail;


}
