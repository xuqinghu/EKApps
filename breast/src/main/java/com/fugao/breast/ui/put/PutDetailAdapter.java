package com.fugao.breast.ui.put;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.utils.common.FloatUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class PutDetailAdapter extends BaseQuickAdapter<BreastMilkDetial, BaseViewHolder> {
    public PutDetailAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreastMilkDetial item) {
        helper.setText(R.id.tv_put_detail_item_date, item.MilkPumpDate + " " + item.MilkPumpTime);
        helper.setText(R.id.tv_put_detail_item_amount, FloatUtil.moveZero(item.Amount) + "ml");
        helper.setText(R.id.tv_put_detail_item_validDate, item.YXQ);

    }
}
