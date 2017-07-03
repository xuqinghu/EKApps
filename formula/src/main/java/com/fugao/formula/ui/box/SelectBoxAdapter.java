package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.BoxListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class SelectBoxAdapter extends BaseQuickAdapter<BoxListEntity, BaseViewHolder> {
    public SelectBoxAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoxListEntity item) {
        helper.setText(R.id.tv_code, item.MilkBoxID);
        helper.setText(R.id.tv_size, item.MilkBoxSpec);
        helper.setText(R.id.tv_count, item.MilkQuantity);
    }
}


