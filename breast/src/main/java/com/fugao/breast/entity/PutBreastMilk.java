package com.fugao.breast.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class PutBreastMilk extends BreastMilk implements Serializable {
    //分格坐标
    public String CoorDinate;
    //备注
    public String Remarks;
    //分格ID
    public String CoorDinateID;
    public String MilkBoxId;
    public String MilkBoxNo;
    //乳箱状态
    public String MilkBoxState;
    //存放瓶数
    public String CFAccount;
    //存放量
    public String CFAmount;
}
