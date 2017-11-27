package com.fugao.test1;

import android.content.Context;

import com.fugao.breast.utils.listview.CommonLVAdapter;
import com.fugao.breast.utils.listview.ViewHolder;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class WardAdapter extends CommonLVAdapter<WardBean> {
    public WardAdapter(int layoutId, Context context) {
        super(layoutId, context);
    }

    @Override
    public void convert(ViewHolder helper, WardBean item) {
        helper.setText(R.id.wardName, item.BQMC);
        helper.setVisible(R.id.checkBoxIv, false);
    }
}
