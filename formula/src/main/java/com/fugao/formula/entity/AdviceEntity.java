package com.fugao.formula.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class AdviceEntity implements Serializable {
    //病区代码
    public String WardCode;
    //病区名称
    public String WardName;
    //奶代码
    public String MilkCode;
    //奶名称
    public String MilkName;
    //病人姓名
    public String PatName;
    //住院号
    public String HosptNo;
    //房间号
    public String RoomNo;
    //床位号
    public String BedNo;
    public String DocCode;
    public String Frequency;
    public String FreqDesc;
    public String ReguTime;
    public String AdviceNo;
    public String AdviceStatus;
    public String Quantity;
    public String ShowMsg;
    public List<MilkDetail> FormulaMilkDetail;
}
