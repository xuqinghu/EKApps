package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/25 0025.
 */

public class TodayBoxingAdapter extends BaseQuickAdapter<BoxListEntity, BaseViewHolder> {
    public TodayBoxingAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoxListEntity item) {
        if ("0".equals(item.MilkBoxID)) {
            helper.setText(R.id.tv_milk_code, "备用奶");
        } else {
            helper.setText(R.id.tv_milk_code, item.MilkBoxID + "/");
        }
        helper.setText(R.id.tv_milk_size, item.MilkBoxSpec);
        if (!StringUtils.StringIsEmpty(item.MilkJL)) {
            helper.setVisible(R.id.tv_milk_amount, true);
            helper.setText(R.id.tv_milk_amount, item.MilkJL);
        } else {
            helper.setVisible(R.id.tv_milk_amount, false);
        }
        helper.setText(R.id.tv_count, item.MilkQuantity);
        helper.setText(R.id.tv_division_name, item.WardName);
        helper.setText(R.id.tv_binning_date, item.LoadTime);
        if ("1".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_milk_state, "已装箱");
        } else if ("2".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_milk_state, "已签发");
        } else if ("3".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_milk_state, "已签收");
        } else if ("0".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_milk_state, "");
        }
    }
}
