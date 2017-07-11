package com.fugao.formula.ui.box;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.formula.R;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.PersonEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class PersonAdapter extends BaseQuickAdapter<PersonEntity, BaseViewHolder> {
    public PersonAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonEntity item) {
        helper.setText(R.id.tv_person_bedNo, item.BedNo);
        helper.setText(R.id.tv_person_name, item.PatName);
        helper.setText(R.id.tv_person_pid, item.ZYH);
        helper.setText(R.id.tv_person_milkName, item.MilkName + "/" + item.MilkQuantity);
        helper.setText(R.id.tv_person_division, item.WardName);
        if ("1".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_person_boxId, item.MilkBoxID + "/已装箱");
        } else if ("2".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_person_boxId, item.MilkBoxID + "/已签发");
        } else if ("3".equals(item.MilkBoxStatus)) {
            helper.setText(R.id.tv_person_boxId, item.MilkBoxID + "/已签收");
        }
    }
}
