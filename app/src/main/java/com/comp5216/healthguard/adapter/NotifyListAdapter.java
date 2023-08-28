package com.comp5216.healthguard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.comp5216.healthguard.obj.portal.Notification;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.obj.portal.SendNotificationRefreshEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NotifyListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private List<Notification> notificationList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NotifyListAdapter(List<Notification> notificationList, Context mContext){
        this.notificationList = notificationList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.listview_notify_item,null);
        TextView tv_notify_content = view.findViewById(R.id.tv_notify_item_content);
        TextView tv_notify_date = view.findViewById(R.id.tv_notify_item_date);
        Switch sw_notify_item_swtich = view.findViewById(R.id.sw_notify_item_swtich);
        ImageView iv_notify_item_icon = view.findViewById(R.id.iv_notify_item_icon);
        Notification notification = notificationList.get(pos);
        if (notification.getNotification_type().equals("0")){
            sw_notify_item_swtich.setVisibility(View.VISIBLE);
            sw_notify_item_swtich.setOnCheckedChangeListener(this);
        }
        if (notification.getNotification_type().equals("1")){
            tv_notify_content.setTextColor(Color.argb(255,255,0,0));
            tv_notify_date.setTextColor(Color.argb(255,255,0,0));
            iv_notify_item_icon.setImageResource(R.drawable.ic_medicine_red);
        }
        if (notification.getNotification_type().equals("2")){
            iv_notify_item_icon.setImageResource(R.drawable.ic_file);
        }
        if (notification.getNotification_type().equals("3")){
            tv_notify_content.setTextColor(Color.argb(255,255,0,0));
            tv_notify_date.setTextColor(Color.argb(255,255,0,0));
            iv_notify_item_icon.setImageResource(R.drawable.ic_warning);
        }
        tv_notify_content.setText(notification.getNotification_note());
        tv_notify_date.setText(notification.getNotification_date());
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.isChecked()){
            DocumentReference notify_delete_Ref = db.collection("notification").document("YyKfIet5AQOVxLOAOyjd");
            notify_delete_Ref
                    .update("notification_delete_status", "1")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
            SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh","notification_refresh");
            EventBus.getDefault().postSticky(notification_refresh_event);
        }
    }
}
