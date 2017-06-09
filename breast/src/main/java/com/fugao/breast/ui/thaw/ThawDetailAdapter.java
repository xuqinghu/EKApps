package com.fugao.breast.ui.thaw;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.BreastInfo;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.Status;
import com.fugao.breast.utils.common.FloatUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class ThawDetailAdapter extends BaseQuickAdapter<BreastMilkDetial, BaseViewHolder> {
    public ThawDetailAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreastMilkDetial item) {
        helper.setText(R.id.tv_thaw_detail_item_size, item.Amount);
        if ("3".equals(item.State)) {
            helper.setTextColor(R.id.tv_thaw_detail_item_state, Color.parseColor("#FF0000"));
            helper.setText(R.id.tv_thaw_detail_item_state, "已解冻");
        } else {
            helper.setText(R.id.tv_thaw_detail_item_state, "未解冻");
            helper.setTextColor(R.id.tv_thaw_detail_item_state, Color.parseColor("#6f6a6a"));
        }
        helper.setText(R.id.tv_thaw_detail_item_size, FloatUtil.moveZero(item.Amount) + "ml");
        helper.setText(R.id.tv_thaw_detail_item_time, item.MilkPumpDate + " " + item.MilkPumpTime);
        helper.setText(R.id.tv_thaw_detail_item_place1, item.MilkBoxNo);
        helper.setText(R.id.tv_thaw_detail_item_place2, item.Remarks);
    }
}
