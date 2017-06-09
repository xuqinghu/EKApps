package com.fugao.breast.ui.thaw;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fugao.breast.entity.ThawListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class SearchAdapter extends BaseQuickAdapter<ThawListBean, BaseViewHolder> {
    public SearchAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThawListBean item) {

    }
}
