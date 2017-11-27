package com.fugao.breast.ui.thaw;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.R;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.entity.ThawListBean;
import com.fugao.breast.utils.StringUtils;
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
        helper.setText(R.id.tv_thaw_item_count, FloatUtil.moveZero(item.CFAmount) + "ml");
        float amount1 = Float.parseFloat(item.CFAmount);
        float amount2 = Float.parseFloat(item.YZZL);
        if ("无".equals(item.IsNoYZ)) {
            helper.setBackgroundColor(R.id.ll_thaw_item_bg, Color.parseColor("#FFFFAA"));
        } else if (!"无".equals(item.IsNoYZ) && amount1 < amount2) {
            helper.setBackgroundColor(R.id.ll_thaw_item_bg, Color.parseColor("#FFB5B5"));
        } else if (amount1 >= amount2 && !"无".equals(item.IsNoYZ)) {
            helper.setBackgroundColor(R.id.ll_thaw_item_bg, Color.parseColor("#FFFFFF"));
        }
        if ("新医嘱".equals(item.DepartmentName)) {
            helper.setVisible(R.id.drugs_sign, true);
        } else {
            helper.setVisible(R.id.drugs_sign, false);
        }
    }
}
