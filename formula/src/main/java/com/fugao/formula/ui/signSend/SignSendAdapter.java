package com.fugao.formula.ui.signSend;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.formula.R;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.swipe.SwipeLayout;
import com.fugao.formula.utils.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class SignSendAdapter extends RecyclerSwipeAdapter<SignSendAdapter.SimpleViewHolder> {
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button button;
        TextView tv_code;
        TextView tv_size;
        TextView tv_count;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            button = (Button) itemView.findViewById(R.id.btn1);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }

    }

    private Activity activity;
    private List<BoxListEntity> mData;

    public SignSendAdapter(Activity activity, List<BoxListEntity> data) {
        this.activity = activity;
        this.mData = data;
    }

    public void setData(List<BoxListEntity> data) {
        this.mData = data;
        notifyDatasetChanged();
    }

    @Override
    public SignSendAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_send, parent, false);
        return new SignSendAdapter.SimpleViewHolder(view);
    }

    //取消签发
    private void cancel(final BoxListEntity bean) {
        List<PostEntity> postData = new ArrayList<>();
        DialogUtils.showProgressDialog(activity, "取消签发中...");
        PostEntity postBean = new PostEntity();
        postBean.CurOperation = "取消签发";
        postBean.HZID = bean.HZID;
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        mData.remove(bean);
                        setData(mData);
                        ToastUtils.showShort(activity, "取消签发成功");
                    }
                } else {
                    ToastUtils.showShort(activity, "服务器异常");
                }

            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(activity, "取消签发失败");
            }
        };
        OkHttpUtils.post(url, callback, json);

    }

    @Override
    public void onBindViewHolder(SignSendAdapter.SimpleViewHolder viewHolder, final int position) {
        BoxListEntity item = mData.get(position);
        viewHolder.tv_code.setText(item.MilkBoxID);
        viewHolder.tv_size.setText(item.MilkBoxSpec);
        viewHolder.tv_count.setText(item.MilkQuantity);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭侧滑
                mItemManger.closeAllItems();
                cancel(mData.get(position));
            }
        });
        mItemManger.bind(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
