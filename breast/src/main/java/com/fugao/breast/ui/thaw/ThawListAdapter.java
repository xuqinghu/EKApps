package com.fugao.breast.ui.thaw;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.entity.ThawListBean;
import com.fugao.breast.utils.common.FloatUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class ThawListAdapter extends BaseQuickAdapter<BreastMilk, BaseViewHolder> {
    public ThawListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreastMilk item) {
        helper.setText(R.id.tv_thaw_item_name, item.Name);
        helper.setText(R.id.tv_thaw_item_patId, item.Pid);
        helper.setText(R.id.tv_thaw_item_bedNo, item.BedNo);
        helper.setText(R.id.tv_thaw_item_totalSize, FloatUtil.moveZero(item.YZZL) + "ml");
        helper.setText(R.id.tv_thaw_item_size, FloatUtil.moveZero(item.ThawAmount) + "ml");
        helper.setText(R.id.tv_thaw_item_count, item.ThawAccount);
    }
}
