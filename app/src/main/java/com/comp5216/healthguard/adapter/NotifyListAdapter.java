package com.comp5216.healthguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comp5216.healthguard.obj.portal.Notification;
import com.comp5216.healthguard.R;
import java.util.List;

public class NotifyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Notification> notificationList;

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

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.listview_notify_item,null);
        TextView tv_notify_content = view.findViewById(R.id.tv_notify_item_content);
        TextView tv_notify_date = view.findViewById(R.id.tv_notify_item_content);
        tv_notify_content.setText(notificationList.get(pos).getNotification_note());
        tv_notify_date.setText(notificationList.get(pos).getNotification_date());
        return view;
    }
}
