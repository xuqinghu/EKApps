package com.fugao.breast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huxq on 2017/5/23 0023.
 */

public class ThawListBean implements Serializable{
    //病人名字
    public String name;
    //住院号
    public String patId;
    //床号
    public String bedNo;
    //房间号
    public String roomNo;
    //医嘱剂量
    public String adviceSize;
    //医嘱执行时间
    public String startTime;
    //医嘱频次
    public String adiviceFq;
    //医嘱停止时间
    public String endTime;
    //有效期
    public String validDate;
    //总奶量
    public String totalSize;
    //解冻奶量
    public String thawSize;
    //解冻瓶数
    public int thawCount;
    //解冻时间
    public String thawTime;
    public List<BreastInfo> infoList;
}
