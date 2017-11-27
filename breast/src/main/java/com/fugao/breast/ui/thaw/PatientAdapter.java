package com.fugao.breast.ui.thaw;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.utils.common.FloatUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class PatientAdapter extends BaseQuickAdapter<BreastMilk, BaseViewHolder> {
    public PatientAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreastMilk item) {
        helper.setText(R.id.item_name, "姓名:" + item.Name);
        helper.setText(R.id.item_patId, "住院号:" + item.Pid);
        helper.setText(R.id.item_bedNo, "床号:" + item.BedNo);
        helper.setText(R.id.item_roomNo, "房间号:" + item.RoomNo);
        helper.setText(R.id.item_division, "病区:" + item.WardName);
        helper.setText(R.id.item_adviceSize, "医嘱剂量:" + FloatUtil.moveZero(item.YZdosis) + "ml");
        helper.setText(R.id.item_adviceFq, "医嘱频次:" + item.YZTime);
        helper.setText(R.id.item_totalAmount, "总量:" + FloatUtil.moveZero(item.YZZL) + "ml");

    }
}
