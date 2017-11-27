package com.fugao.formula.ui.box;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.formula.R;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.MilkDetail;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.swipe.SwipeLayout;
import com.fugao.formula.utils.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class BoxDetailAdviceAdapter1 extends RecyclerSwipeAdapter<BoxDetailAdviceAdapter1.SimpleViewHolder> {
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button button;
        TextView bedNo;
        TextView name;
        TextView pid;
        TextView milkName;
        TextView count;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            button = (Button) itemView.findViewById(R.id.btn_cancle);
            bedNo = (TextView) itemView.findViewById(R.id.tv_box_detail_item_bedNo);
            name = (TextView) itemView.findViewById(R.id.tv_box_detail_item_name);
            pid = (TextView) itemView.findViewById(R.id.tv_box_detail_item_pid);
            milkName = (TextView) itemView.findViewById(R.id.tv_box_detail_item_milkName);
            count = (TextView) itemView.findViewById(R.id.tv_box_detail_item_count);
        }
    }

    private Activity activity;
    private List<AdviceEntity> mData;
    private String state;
    private String HZID;
    private String milkBoxID;

    public BoxDetailAdviceAdapter1(Activity activity, List<AdviceEntity> data) {
        this.activity = activity;
        this.mData = data;

    }

    public void setData(List<AdviceEntity> data, String state, String HZID, String milkBoxID) {
        this.mData = data;
        this.state = state;
        this.HZID = HZID;
        this.milkBoxID = milkBoxID;
        notifyDatasetChanged();
    }

    @Override
    public BoxDetailAdviceAdapter1.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_box_detail_item1, parent, false);
        return new BoxDetailAdviceAdapter1.SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoxDetailAdviceAdapter1.SimpleViewHolder viewHolder, final int position) {
        final AdviceEntity item = mData.get(position);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭侧滑
                mItemManger.closeAllItems();
                if ("1".equals(state)) {
                    cancel(item);
                } else {
                    ToastUtils.showShort(activity, "箱子已经被签发,不能进行此操作");
                }
            }
        });
        viewHolder.bedNo.setText(item.BedNo);
        viewHolder.name.setText(item.PatName);
        viewHolder.pid.setText(item.HosptNo);
        viewHolder.milkName.setText(item.MilkName);
        viewHolder.count.setText(item.Quantity);
        mItemManger.bind(viewHolder.itemView, position);
    }

    private String getAdviceIDs(List<MilkDetail> beans) {
        String adviceIds = "";
        for (MilkDetail bean : beans) {
            adviceIds = adviceIds + bean.MilkNo + ";";
        }
        if (!StringUtils.StringIsEmpty(adviceIds)) {
            adviceIds = adviceIds.substring(0, adviceIds.length() - 1);
        }
        return adviceIds;
    }

    //取消装箱
    private void cancel(final AdviceEntity bean) {
        List<PostEntity> postData = new ArrayList<>();
        DialogUtils.showProgressDialog(activity, "取消装箱中...");
        PostEntity postBean = new PostEntity();
        postBean.NPID = getAdviceIDs(bean.FormulaMilkDetail);
        postBean.MilkBoxID = milkBoxID;
        postBean.CurOperation = "取消装箱";
        postBean.HZID = HZID;
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    mData.remove(bean);
                    setData(mData, state, HZID, milkBoxID);
                    ToastUtils.showShort(activity, "取消装箱成功");
                    Constant.CANCEL_BOXING=true;
                } else {
                    ToastUtils.showShort(activity, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(activity, "取消核对失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.post(url, callback, json);
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
