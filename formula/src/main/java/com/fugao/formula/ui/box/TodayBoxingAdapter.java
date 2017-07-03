package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.BoxListEntity;

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
        helper.setText(R.id.tv_milk_code, item.MilkBoxID);
        helper.setText(R.id.tv_milk_size, item.MilkBoxSpec);
        helper.setText(R.id.tv_count, item.MilkQuantity);
        helper.setText(R.id.tv_division_name, item.WardName);
        helper.setText(R.id.tv_binning_date, item.LoadTime);
    }
}
