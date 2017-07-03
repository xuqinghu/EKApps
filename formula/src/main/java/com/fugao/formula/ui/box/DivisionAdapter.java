package com.fugao.formula.ui.box;

import android.content.Context;

import com.fugao.formula.R;
import com.fugao.formula.entity.DivisionEntity;
import com.fugao.formula.entity.WardBean;
import com.fugao.formula.utils.listview.CommonLVAdapter;
import com.fugao.formula.utils.listview.ViewHolder;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class DivisionAdapter extends CommonLVAdapter<WardBean> {
    public DivisionAdapter(int layoutId, Context context) {
        super(layoutId, context);
    }

    @Override
    public void convert(ViewHolder helper, WardBean item) {
        helper.setText(R.id.wardName, item.BQMC);
    }

}
