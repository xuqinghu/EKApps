package com.fugao.formula.ui.box;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.AdviceEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class NotCheckAdapter extends BaseQuickAdapter<AdviceEntity, BaseViewHolder> {
    public NotCheckAdapter(@LayoutRes int layoutResId, @Nullable List<AdviceEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdviceEntity item) {
        helper.setText(R.id.tv_not_check_item_bedNo, item.BedNo);
        helper.setTextColor(R.id.tv_not_check_item_bedNo, Color.parseColor("#FF0000"));
        helper.setText(R.id.tv_not_check_item_name, item.PatName);
        helper.setText(R.id.tv_not_check_item_pid, item.HosptNo);
        helper.setText(R.id.tv_not_check_item_milkName, item.MilkName);
        helper.setText(R.id.tv_not_check_item_count, item.Quantity);
        if ("0".equals(item.AdviceStatus)) {
            helper.setText(R.id.tv_not_check_item_state, "未核对");
            helper.setTextColor(R.id.tv_not_check_item_state, Color.parseColor("#36B34A"));
        }

    }
}
