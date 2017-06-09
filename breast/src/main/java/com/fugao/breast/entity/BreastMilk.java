package com.fugao.breast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class BreastMilk implements Serializable {
    //接收ID
    public String Id;
    //姓名
    public String Name;
    //性别
    public String Sex;
    //住院号
    public String Pid;
    //床位号
    public String BedNo;
    //科室ID
    public String DepartmentId;
    //病区ID
    public String WardId;
    //科室名称
    public String DepartmentName;
    //病区名称
    public String WardName;
    //医嘱频次
    public String YZTime;
    //医嘱剂量
    public String YZdosis;
    //解冻量
    public String ThawAmount;
    //解冻瓶数
    public String ThawAccount;
    //医嘱总量
    public String YZZL;
    //房间号
    public String RoomNo;
    //明细
    public List<BreastMilkDetial> BreastMilkItems;
}
