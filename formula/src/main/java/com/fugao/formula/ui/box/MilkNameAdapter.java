package com.fugao.formula.ui.box;

import android.content.Context;

import com.fugao.formula.R;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.utils.listview.CommonLVAdapter;
import com.fugao.formula.utils.listview.ViewHolder;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class MilkNameAdapter extends CommonLVAdapter<MilkBean> {
    public MilkNameAdapter(int layoutId, Context context) {
        super(layoutId, context);
    }

    @Override
    public void convert(ViewHolder helper, MilkBean item) {
        helper.setText(R.id.milkName, item.MilkName);
    }

}
