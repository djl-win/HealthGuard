package com.comp5216.healthguard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.entity.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 主页药物提醒的adapter
 * <p>
 * 渲染用药列表，并处理逻辑
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.RemindersViewHolder>  {

    Context context;
    List<MedicationReminder> medicationReminders;

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
        public RemindersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDrugName = itemView.findViewById(R.id.reminders_list_drug_name);
            textViewDrugTime = itemView.findViewById(R.id.reminders_list_drug_time);
            textViewDrugDosage = itemView.findViewById(R.id.reminders_list_drug_dosage);
            textViewDrugNote = itemView.findViewById(R.id.reminders_list_drug_note);
        }

    }
}

