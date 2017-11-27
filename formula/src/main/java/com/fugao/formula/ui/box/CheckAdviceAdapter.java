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
import com.fugao.formula.constant.Constant;
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
    private TextView personCount, milkCount;
    private String milkCode = "";

    public CheckAdviceAdapter(Activity activity, List<AdviceEntity> data, TextView t1, TextView t2) {
        this.activity = activity;
        this.mData = data;
        this.personCount = t1;
        this.milkCount = t2;
    }

    public void setData(List<AdviceEntity> data, String milkCode) {
        this.milkCode = milkCode;
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
        postBean.WardCode = XmlDB.getInstance(activity).getKeyString("divisionCode", "");
        postBean.ExecFrequency = XmlDB.getInstance(activity).getKeyString("time", "");
        postBean.NPID = getAdviceIDs(bean.FormulaMilkDetail);
        postBean.MilkName = milkCode;
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
                        mData.clear();
                        if (!"[]".equals(response)) {
                            List<AdviceEntity> beans = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            if (beans.size() > 0) {
                                mData = beans;
                            }
                        }
                        setData(mData, milkCode);
                        update(mData);
                        ToastUtils.showShort(activity, "取消核对成功");
                    } else {
                        ToastUtils.showShort(activity, "服务器异常");
                    }
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

    //刷新人数和瓶数

    private void update(List<AdviceEntity> beans) {
        int count = 0;
        List<AdviceEntity> beans1 = new ArrayList<>();
        List<AdviceEntity> beans2 = new ArrayList<>();
        //筛选未核对数据
        for (AdviceEntity bean : beans) {
            if ("0".equals(bean.AdviceStatus)) {
                beans1.add(bean);
            }
        }
        personCount.setText("人数:" + beans1.size());
        //筛选已核对数据
        for (AdviceEntity bean : beans) {
            if ("1".equals(bean.AdviceStatus)) {
                beans2.add(bean);
            }
        }
        Constant.ADVICE_BOX_LIST = beans2;
        //计算已核对的瓶数
        for (AdviceEntity bean : beans2) {
            count = count + Integer.parseInt(bean.Quantity);
        }
        milkCount.setText("瓶数:" + count);
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
                intent.putExtra("state", "已核对");
                intent.setClass(activity, AdviceDetailActivity.class);
                activity.startActivity(intent);
            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭侧滑
                mItemManger.closeAllItems();
                if ("1".equals(mData.get(position).AdviceStatus)) {
                    cancel(mData.get(position));
                } else if ("2".equals(mData.get(position).AdviceStatus)) {
                    ToastUtils.showShort(activity, "处于装箱状态，不能取消核对");
                } else if ("3".equals(mData.get(position).AdviceStatus)) {
                    ToastUtils.showShort(activity, "处于签发状态，不能取消核对");
                } else if ("4".equals(mData.get(position).AdviceStatus)) {
                    ToastUtils.showShort(activity, "处于签收状态，不能取消核对");
                }
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
        } else if ("2".equals(item.AdviceStatus)) {
            viewHolder.state.setText("已装箱");
            viewHolder.state.setTextColor(activity.getResources().getColor(R.color.text_yellow));
        } else if ("3".equals(item.AdviceStatus)) {
            viewHolder.state.setText("已签发");
            viewHolder.state.setTextColor(activity.getResources().getColor(R.color.text_pink));
        } else if ("4".equals(item.AdviceStatus)) {
            viewHolder.state.setText("已签收");
            viewHolder.state.setTextColor(activity.getResources().getColor(R.color.text_red));
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
