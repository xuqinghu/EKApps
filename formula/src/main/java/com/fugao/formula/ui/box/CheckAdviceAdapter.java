package com.fugao.formula.ui.box;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.formula.R;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.MilkDetail;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.swipe.SwipeLayout;
import com.fugao.formula.utils.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class CheckAdviceAdapter extends RecyclerSwipeAdapter<CheckAdviceAdapter.SimpleViewHolder> {
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView bedNo;
        TextView name;
        TextView pid;
        TextView milkName;
        TextView count;
        TextView state;
        Button button;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            button = (Button) itemView.findViewById(R.id.btn1);
            bedNo = (TextView) itemView.findViewById(R.id.tv_check_advice_item_bedNo);
            name = (TextView) itemView.findViewById(R.id.tv_check_advice_item_name);
            pid = (TextView) itemView.findViewById(R.id.tv_check_advice_item_pid);
            milkName = (TextView) itemView.findViewById(R.id.tv_check_advice_item_milkName);
            count = (TextView) itemView.findViewById(R.id.tv_check_advice_item_count);
            state = (TextView) itemView.findViewById(R.id.tv_check_advice_item_state);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private Activity activity;
    private List<AdviceEntity> mData;

    public CheckAdviceAdapter(Activity activity, List<AdviceEntity> data) {
        this.activity = activity;
        this.mData = data;
    }

    public void setData(List<AdviceEntity> data) {
        this.mData = data;
        notifyDatasetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_check_advice_item, parent, false);
        return new SimpleViewHolder(view);
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

    //取消核对
    private void cancel(final AdviceEntity bean) {
        List<PostEntity> postData = new ArrayList<>();
        DialogUtils.showProgressDialog(activity, "取消核对中...");
        PostEntity postBean = new PostEntity();
        postBean.OperatorName = XmlDB.getInstance(activity).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(activity).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.NPID = getAdviceIDs(bean.FormulaMilkDetail);
        postBean.CurOperation = "取消核对";
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(activity, "没有数据");
                        } else {
                            List<AdviceEntity> beans = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            if (beans.size() > 0) {
                                mData.remove(bean);
                                mData.add(0, beans.get(0));
                                setData(mData);
                                ToastUtils.showShort(activity, "取消核对成功");
                            }
                        }
                    }
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
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        AdviceEntity item = mData.get(position);
        //列表点击事件
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("advice", mData.get(position));
                intent.setClass(activity, AdviceDetailActivity.class);
                activity.startActivity(intent);
            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭侧滑
                mItemManger.closeAllItems();
                Log.d("TAG", position + "");
                cancel(mData.get(position));
            }
        });
        viewHolder.bedNo.setText(item.BedNo);
        viewHolder.bedNo.setTextColor(activity.getResources().getColor(R.color.text_red));
        viewHolder.name.setText(item.PatName);
        viewHolder.pid.setText(item.HosptNo);
        viewHolder.milkName.setText(item.MilkName);
        viewHolder.count.setText(item.Quantity);
        if ("0".equals(item.AdviceStatus)) {
            viewHolder.state.setText("未核对");
            viewHolder.state.setTextColor(activity.getResources().getColor(R.color.text_green));
        } else if ("1".equals(item.AdviceStatus)) {
            viewHolder.state.setText("已核对");
            viewHolder.state.setTextColor(activity.getResources().getColor(R.color.text_blue));
        } else {
            viewHolder.state.setText("已装箱");
        }
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
