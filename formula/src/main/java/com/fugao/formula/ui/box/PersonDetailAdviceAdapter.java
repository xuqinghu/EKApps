package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.PersonEntity;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class PersonDetailAdviceAdapter extends BaseQuickAdapter<PersonEntity, BaseViewHolder> {
    public PersonDetailAdviceAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonEntity item) {
        helper.setText(R.id.tv_box_detail_item_name, item.PatName);
        helper.setText(R.id.tv_box_detail_item_pid, item.ZYH);
        helper.setText(R.id.tv_box_detail_item_milkName, item.MilkName);
        helper.setText(R.id.tv_box_detail_item_count, item.MilkQuantity);

    }
}
