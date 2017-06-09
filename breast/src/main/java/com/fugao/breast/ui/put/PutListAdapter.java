package com.fugao.breast.ui.put;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.PutBreastMilk;
import com.fugao.breast.entity.Status;

import java.util.List;

/**
 * Created by huxq on 2017/5/22 0022.
 */

public class PutListAdapter extends BaseQuickAdapter<PutBreastMilk, BaseViewHolder> {
    public PutListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PutBreastMilk item) {
        helper.setText(R.id.tv_put_list_item_place1, item.MilkBoxNo);
        helper.setText(R.id.tv_put_list_item_place2, item.Remarks);
        helper.setText(R.id.tv_put_list_item_bedNo, item.BedNo);
        helper.setText(R.id.tv_put_list_item_name, item.Name);
        helper.setText(R.id.tv_put_list_item_pid, item.Pid);
        helper.setText(R.id.tv_put_list_item_count, item.CFAccount);
    }
}
