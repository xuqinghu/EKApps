package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.AdviceEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/25 0025.
 */

public class BoxDetaiAdviceAdapter extends BaseQuickAdapter<AdviceEntity, BaseViewHolder> {
    public BoxDetaiAdviceAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdviceEntity item) {
        helper.setText(R.id.tv_box_detail_item_name, item.PatName);
        helper.setText(R.id.tv_box_detail_item_pid, item.HosptNo);
        helper.setText(R.id.tv_box_detail_item_milkName, item.MilkName);
        helper.setText(R.id.tv_box_detail_item_count, item.Quantity);
    }
}
