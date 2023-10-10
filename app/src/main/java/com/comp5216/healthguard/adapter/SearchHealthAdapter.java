package com.comp5216.healthguard.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Attribute;
import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.User;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 搜索页面第一个的health的适配器
 * <p>
 * 渲染健康信息列表，并处理逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchHealthAdapter extends RecyclerView.Adapter<SearchHealthAdapter.HealthViewHolder> {

    // 回调接口，获取单机事件的回调
    public interface ItemClickListener {
        void onItemClick(HealthInformation healthInformation);
    }

    private ItemClickListener itemClickListener;

    Context context;
    List<HealthInformation> healthInformations;


    /**
     * 为此recycle view设置单机事件
     *
     * @param itemClickListener 传进来当前视图
     */
    public void setItemClickListener(SearchHealthAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public SearchHealthAdapter(Context context, List<HealthInformation> healthInformations) {

        this.context = context;
        this.healthInformations = healthInformations;
    }


    @NonNull
    @Override
    public SearchHealthAdapter.HealthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.information_list_item, viewGroup, false);

        return new SearchHealthAdapter.HealthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHealthAdapter.HealthViewHolder holder, int position) {
        initItemView(holder, position);
    }

    /**
     * 初始化每个item中的数据，
     *
     * @param holder   ChatViewHolder
     * @param position 列表中item的position
     */
    @SuppressLint("SetTextI18n")
    private void initItemView(@NonNull SearchHealthAdapter.HealthViewHolder holder, int position) {
        // 创建一个SimpleDateFormat对象，指定所需的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // 将时间戳转换为Date对象
        Date date = new Date(healthInformations.get(position).getHealthInformationDate());

        // 格式化日期
        String formattedDate = sdf.format(date);

        holder.textViewTime.setText(formattedDate);

        // 健康
        if (healthInformations.get(position).getHealthInformationHealthStatus() == 0) {
            holder.textViewStatus.setText("Normal");
            holder.textViewStatus.setTextColor((ContextCompat.getColor(context, R.color.black)));
            holder.textViewTime.setTextColor((ContextCompat.getColor(context, R.color.black)));;
        }
        // 不健康
        else {
            holder.textViewStatus.setText("Abnormal");
            holder.textViewStatus.setTextColor((ContextCompat.getColor(context, R.color.red)));
            holder.textViewTime.setTextColor((ContextCompat.getColor(context, R.color.red)));;
        }

        // 给卡片设置单机事件
        holder.materialCardView.setOnClickListener(view -> {
            // 调用接口的回调方法，将点击的位置的用户信息传递给 MainActivity
            if (itemClickListener != null) {
                itemClickListener.onItemClick(healthInformations.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthInformations.size();
    }

    /**
     * 更新recycle view中的数据
     *
     * @param newHealthInformations 传入的新的列表
     */
    public void updateData(List<HealthInformation> newHealthInformations) {
        this.healthInformations.clear();
        this.healthInformations.addAll(newHealthInformations);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    static class HealthViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;
        TextView textViewStatus;

        MaterialCardView materialCardView;

        public HealthViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.information_list_time);
            textViewStatus = itemView.findViewById(R.id.information_list_status);
            materialCardView = itemView.findViewById(R.id.information_list_cardview);
        }

    }
}
