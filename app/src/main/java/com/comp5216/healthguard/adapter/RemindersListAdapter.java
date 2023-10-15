package com.comp5216.healthguard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.google.android.material.card.MaterialCardView;


import java.util.List;

/**
 * 主页药物提醒的adapter
 * <p>
 * 渲染用药列表，并处理逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-08
 */
public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.RemindersViewHolder>  {

    Context context;
    List<MedicationReminder> medicationReminders;

    // 回调接口，获取单机事件的回调
    public interface ItemClickListener {
        void onItemClick(MedicationReminder medicationReminder);
    }

    private RemindersListAdapter.ItemClickListener itemClickListener;

    /**
     * 为此recycle view设置单机事件
     *
     * @param itemClickListener 传进来当前视图
     */
    public void setItemClickListener(RemindersListAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RemindersListAdapter(Context context, List<MedicationReminder> medicationReminders) {

        this.context = context;
        this.medicationReminders = medicationReminders;
    }


    @NonNull
    @Override
    public RemindersListAdapter.RemindersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.reminders_list_item, viewGroup, false);

        return new RemindersListAdapter.RemindersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersListAdapter.RemindersViewHolder holder, int position) {
        initItemView(holder, position);
    }

    /**
     * 初始化每个item中的数据，
     *
     * @param holder   ChatViewHolder
     * @param position 列表中item的position
     */
    @SuppressLint("SetTextI18n")
    private void initItemView(@NonNull RemindersListAdapter.RemindersViewHolder holder, int position) {
        holder.textViewDrugTime.setText(medicationReminders.get(position).getMedicationReminderDrugTime());
        holder.textViewDrugName.setText(medicationReminders.get(position).getMedicationReminderDrugName());
        holder.textViewDrugDosage.setText(medicationReminders.get(position).getMedicationReminderDrugDosage());
        holder.textViewDrugNote.setText("note: " + medicationReminders.get(position).getMedicationReminderDrugNote());

        // 给卡片设置单机事件
        holder.materialCardView.setOnClickListener(view -> {
            // 调用接口的回调方法，将点击的位置的用户信息传递给 MainActivity
            if (itemClickListener != null) {
                itemClickListener.onItemClick(medicationReminders.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationReminders.size();
    }

    /**
     * 更新recycle view中的数据
     *
     * @param medicationReminder 传入的新的列表
     */
    public void updateData(List<MedicationReminder> medicationReminder) {
        this.medicationReminders.clear();
        this.medicationReminders.addAll(medicationReminder);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    static class RemindersViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDrugTime;
        TextView textViewDrugName;
        TextView textViewDrugDosage;
        TextView textViewDrugNote;

        MaterialCardView materialCardView;
        public RemindersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDrugName = itemView.findViewById(R.id.reminders_list_drug_name);
            textViewDrugTime = itemView.findViewById(R.id.reminders_list_drug_time);
            textViewDrugDosage = itemView.findViewById(R.id.reminders_list_drug_dosage);
            textViewDrugNote = itemView.findViewById(R.id.reminders_list_drug_note);
            materialCardView = itemView.findViewById(R.id.reminders_list_cardview);
        }

    }
}

