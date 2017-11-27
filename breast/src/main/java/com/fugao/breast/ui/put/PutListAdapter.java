package com.fugao.breast.ui.put;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.PutBreastMilk;
import com.fugao.breast.entity.Status;
import com.fugao.breast.utils.StringUtils;

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
        if (!StringUtils.StringIsEmpty(item.Remarks)) {
            String remarks[] = item.Remarks.split("/");
            if (remarks.length == 2) {
                helper.setText(R.id.tv_put_list_item_place2, remarks[0]);
                helper.setText(R.id.tv_put_list_item_place3, remarks[1]);
            } else {
                helper.setText(R.id.tv_put_list_item_place2, item.Remarks);
            }
        }
        helper.setText(R.id.tv_put_list_item_bedNo, item.BedNo);
        helper.setText(R.id.tv_put_list_item_name, item.Name);
        helper.setText(R.id.tv_put_list_item_pid, item.Pid);
        helper.setText(R.id.tv_put_list_item_count, item.CFAccount);
        if ("0".equals(item.MilkBoxOutherState)) {
            helper.setText(R.id.tv_put_list_item_state, "冷藏");
        } else if ("1".equals(item.MilkBoxOutherState)) {
            helper.setText(R.id.tv_put_list_item_state, "冷冻");
        }

    }
}
