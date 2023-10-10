package com.comp5216.healthguard.adapter;

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
import com.comp5216.healthguard.entity.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 通知栏的adapter
 * <p>
 * 渲染通知栏列表，并处理逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-08
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {

    Context context;
    List<Notification> notifications;

    public NotificationListAdapter(Context context, List<Notification> notifications) {

        this.context = context;
        this.notifications = notifications;
    }


    @NonNull
    @Override
    public NotificationListAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_item, viewGroup, false);

        return new NotificationListAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.NotificationViewHolder holder, int position) {
        initItemView(holder, position);
    }

    /**
     * 初始化每个item中的数据，
     *
     * @param holder   ChatViewHolder
     * @param position 列表中item的position
     */
    private void initItemView(@NonNull NotificationListAdapter.NotificationViewHolder holder, int position) {
        holder.textViewNote.setText(notifications.get(position).getNotificationNote());
        // 日期
        Date date = new Date(notifications.get(position).getNotificationDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        holder.textViewDate.setText(sdf.format(date));
        if (notifications.get(position).getNotificationType() == 0) {
            holder.imageViewLogo.setImageResource(R.drawable.ic_warning);
            holder.textViewNote.setTextColor((ContextCompat.getColor(context, R.color.red)));
            holder.textViewDate.setTextColor((ContextCompat.getColor(context, R.color.red)));
        } else if (notifications.get(position).getNotificationType() == 1) {
            holder.imageViewLogo.setImageResource(R.drawable.ic_report);
            holder.textViewNote.setTextColor((ContextCompat.getColor(context, R.color.black)));
            holder.textViewDate.setTextColor((ContextCompat.getColor(context, R.color.black)));
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * 更新recycle view中的数据
     *
     * @param newNotification 传入的新chats
     */
    public void updateData(List<Notification> newNotification) {
        this.notifications.clear();
        this.notifications.addAll(newNotification);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLogo;
        TextView textViewNote;
        TextView textViewDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLogo = itemView.findViewById(R.id.notification_list_logo);
            textViewNote = itemView.findViewById(R.id.notification_list_note);
            textViewDate = itemView.findViewById(R.id.notification_list_date);
        }

    }
}

