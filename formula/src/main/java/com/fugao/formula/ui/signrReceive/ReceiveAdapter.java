package com.fugao.formula.ui.signrReceive;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.AdviceEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10 0010.
 */

public class ReceiveAdapter extends BaseQuickAdapter<AdviceEntity, BaseViewHolder> {
    public ReceiveAdapter(@LayoutRes int layoutResId, @Nullable List<AdviceEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdviceEntity item) {
        helper.setText(R.id.tv_receive_item_boxNo, item.BoxNo);
        helper.setText(R.id.tv_receive_item_bedNo, item.BedNo);
        helper.setText(R.id.tv_receive_item_name, item.PatName);
        helper.setText(R.id.tv_receive_item_pid, item.HosptNo);
        helper.setText(R.id.tv_receive_item_milkName, item.MilkName);
        helper.setText(R.id.tv_receive_item_count, item.Quantity);
    }
}
