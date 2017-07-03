package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.TimeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class TimeAdapter extends BaseQuickAdapter<TimeEntity, BaseViewHolder> {
    public TimeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeEntity item) {
        helper.setText(R.id.tv_time_item_name, item.Name);
        if ("0".equals(item.State)) {
            helper.setBackgroundRes(R.id.tv_time_item_name, R.drawable.time_bg1);
        } else {
            helper.setBackgroundRes(R.id.tv_time_item_name, R.drawable.time_bg2);
        }
    }
}
