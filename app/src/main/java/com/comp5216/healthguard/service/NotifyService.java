package com.comp5216.healthguard.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.NotifyListAdapter;
import com.comp5216.healthguard.obj.SPConstants;
import com.comp5216.healthguard.obj.portal.Notification;
import com.comp5216.healthguard.obj.portal.SendNotificationRefreshEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


//Notification
//        Service:
//        轮询:
//        for pending吃药事件 Medication Record
//        判断当前时间是否到达
//        if true: 创建Notification，点击消失
//        判断15分钟
//        if 无操作，消失，新建警告提醒
//        for 提醒事件 Notification
//        判断当前数量是否比SP多
//        if true: 增加的提醒添加（刷新页面），消息提醒
//        Other:
//        上传报告:
//        完成上传后相关数据添加到提醒表，轮询
//        上传数据:
//        // if health_information_health_status == 1
//        相关数据添加到提醒表，轮询
public class NotifyService extends Service {

    private Disposable subscription;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private List<Notification> notification_list = new ArrayList<>();
    private List<Notification> notification_type_4_list = new ArrayList<>();
    private Date currentDate = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String formattedDate = dateFormat.format(currentDate);
    private Map<String,String> document_id = new HashMap<>();

    public NotifyService() {

        // Rxjava 轮询 5s
        subscription = Observable.interval(1000*5,1000*5, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Throwable {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        // min 为当前时间和item时间差值 判断=15
                        CollectionReference notifyRef = db.collection("notification");
                        notifyRef.whereEqualTo("userId",user_id)
                                .whereEqualTo("notificationReadStatus","0")
//                                .whereEqualTo("notification_delete_status","0")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (!notification_list.isEmpty()) {
                                                notification_list.clear();
                                            }
                                            if (!notification_type_4_list.isEmpty()) {
                                                notification_type_4_list.clear();
                                            }
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                Map<String, Object> data = documentSnapshot.getData();
                                                String str_item_date = documentSnapshot.get("notificationDate").toString();
                                                if (data.get("notificationDeleteStatus").toString().equals("0")){
                                                    notification_list.add(new Notification(
                                                            data.get("notificationId").toString(),
                                                            data.get("userId").toString(),
                                                            data.get("notificationNote").toString(),
                                                            data.get("notificationDate").toString(),
                                                            data.get("notificationType").toString(),
                                                            data.get("notificationReadStatus").toString(),
                                                            data.get("notificationDeleteStatus").toString()
                                                    ));
                                                    SPUtils.getInstance().put(SPConstants.NOTIFICATION_SIZE,notification_list.size());
                                                    // current_date > item_date  === item is early 8:25 - 8.20 > 0
                                                    if (documentSnapshot.get("notificationType").toString().equals("4")
                                                            && DifferentTime(currentDate, str_item_date, dateFormat) >= 0) {
                                                        notification_type_4_list.add(new Notification(
                                                                data.get("notificationId").toString(),
                                                                data.get("userId").toString(),
                                                                data.get("notificationNote").toString(),
                                                                data.get("notificationDate").toString(),
                                                                data.get("notificationType").toString(),
                                                                data.get("notificationReadStatus").toString(),
                                                                data.get("notificationDeleteStatus").toString()
                                                        ));
                                                        document_id.put(data.get("notificationId").toString(), documentSnapshot.getId());
                                                    }
                                                }
                                            }
                                            Collections.sort(notification_type_4_list);
//                                            LogUtils.e(notification_type_4_list.size());
//                                            if (task.getResult().size() > SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_SIZE)){
//                                                // NotificationManager
//                                                int new_notice = task.getResult().size() - SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_SIZE);
//                                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//                                                    NotificationChannel channel=new NotificationChannel("Notification","Notification",
//                                                            NotificationManager.IMPORTANCE_HIGH);
//                                                    manager.createNotificationChannel(channel);
//                                                }
//                                                if (new_notice >= 3){
//                                                    android.app.Notification note = new NotificationCompat.Builder(getBaseContext(),"Notification")
//                                                            .setContentTitle("New Notice")
//                                                            .setContentText("You have " + new_notice + " new notice !!!")
//                                                            .setSmallIcon(R.drawable.ic_notify)
//                                                            .build();
//                                                    manager.notify(1,note);
//                                                }else {
//                                                    for (int i = SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_SIZE);i<task.getResult().size();i++){
//                                                        android.app.Notification note = new NotificationCompat.Builder(getBaseContext(),"Notification")
//                                                                .setContentTitle("New Notice")
//                                                                .setContentText(notification_list.get(i).getNotification_note())
//                                                                .setSmallIcon(R.drawable.ic_notify)
//                                                                .build();
//                                                        manager.notify(1,note);
//                                                    }
//                                                }
                                            // Refresh
//                                                SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh", "notification_refresh");
//                                                EventBus.getDefault().postSticky(notification_refresh_event);
//                                            }
                                        }
                                    }
                                });

//                        for (int i = 0;i<notification_type_4_list.size();i++){
//                            LogUtils.e(notification_type_4_list.get(i).getNotification_date());
//                        }
                        // 判断15分钟未吃
                        // TODO Document name
                        if (!notification_type_4_list.isEmpty()){
                            if (DifferentTime(currentDate,notification_type_4_list.get(0).getNotification_date(),dateFormat) == 0){
                                DocumentReference notify_update_Ref = db.collection("notification").document(document_id.get(notification_type_4_list.get(0).getNotification_id()));
                                notify_update_Ref
                                        .update("notificationType", "0")
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
                            }
                            if (DifferentTime(currentDate,notification_type_4_list.get(0).getNotification_date(),dateFormat) >= 15){
                                CollectionReference notify_add_Ref = db.collection("notification");
                                Map<String,Object> new_notify = new HashMap<>();
                                new_notify.put("userId",notification_type_4_list.get(0).getUser_id());
                                new_notify.put("notificationDate",formattedDate);
                                new_notify.put("notificationId","test_08");
                                new_notify.put("notificationNote","You did not eat xxx");
                                new_notify.put("notificationType","1");
                                new_notify.put("notificationReadStatus","0");
                                new_notify.put("notificationDeleteStatus","0");
                                notify_add_Ref.document("test08").set(new_notify);
                                // detele type 0
                                DocumentReference notify_delete_Ref = db.collection("notification").document("YyKfIet5AQOVxLOAOyjd");
                                notify_delete_Ref
                                        .update("notificationDeleteStatus", "1")
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
                            }
                            SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh","notification_refresh");
                            EventBus.getDefault().postSticky(notification_refresh_event);
                        }
                    }
                });

        CollectionReference notifyRef = db.collection("notification");
        notifyRef.whereEqualTo("userId",user_id)
                .whereEqualTo("notificationReadStatus","0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null){
                            if (!notification_list.isEmpty()){
                                notification_list.clear();
                            }
                            for (QueryDocumentSnapshot documentSnapshot : value){
                                // ADD LIST
                                if (!documentSnapshot.get("notificationType").toString().equals("4")
                                        && documentSnapshot.get("notificationDeleteStatus").toString().equals("0")){
                                    Map<String, Object> data = documentSnapshot.getData();
                                    notification_list.add(new Notification(
                                            data.get("notificationId").toString(),
                                            data.get("userId").toString(),
                                            data.get("notificationNote").toString(),
                                            data.get("notificationDate").toString(),
                                            data.get("notificationType").toString(),
                                            data.get("notificationReadStatus").toString(),
                                            data.get("notificationDeleteStatus").toString()
                                    ));
                                }
                            }
//                            LogUtils.e(notification_list.size());
//                            LogUtils.e(value.size());
//                            LogUtils.e(SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_SIZE));
                            if (SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE) < notification_list.size()) {
                                // NotificationManager
                                int new_notice = notification_list.size() - SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE);
                                LogUtils.e(notification_list.size());
                                LogUtils.e(SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE));
                                LogUtils.e(new_notice);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("Notification", "Notification",
                                            NotificationManager.IMPORTANCE_DEFAULT);
                                    manager.createNotificationChannel(channel);
                                }
//                                if (new_notice >= 2) {
//                                    android.app.Notification note_2 = new NotificationCompat.Builder(getBaseContext(), "Notification")
//                                            .setContentTitle("New Notice")
//                                            .setContentText("You have " + new_notice + " new notice !!!")
//                                            .setSmallIcon(R.drawable.ic_notify)
//                                            .build();
//                                    manager.notify(2, note_2);
//                                } else {
//                                    for (int i = SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE); i < notification_list.size(); i++) {
                                        android.app.Notification note = new NotificationCompat.Builder(getBaseContext(), "Notification")
                                                .setContentTitle("New Notice")
                                                .setContentText(notification_list.get(SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE)).getNotification_note())
                                                .setSmallIcon(R.drawable.ic_notify)
                                                .build();
                                        manager.notify(1, note);
//                                    }
//                                }
                            }
                            SPUtils.getInstance().put(SPConstants.NOTIFICATION_SIZE,value.size());
                            SPUtils.getInstance().put(SPConstants.NOTIFICATION_LIST_SIZE,notification_list.size());
                            SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh", "notification_refresh");
                            EventBus.getDefault().postSticky(notification_refresh_event);
                        }
                    }
                });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private int DifferentTime(Date currentDate,String itemDate,SimpleDateFormat dateFormat){
        Date item_date = null;
        try {
            item_date = dateFormat.parse(itemDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long min = ( currentDate.getTime() - item_date.getTime() ) / (1000 * 60);
        return (int)min;
    }
    private void doAddTest(){
        CollectionReference notify_add_Ref = db.collection("notification");
        Map<String,Object> new_notify = new HashMap<>();
        new_notify.put("userId",user_id);
        new_notify.put("notificationDate",formattedDate);
        new_notify.put("notificationId","test_test");
        new_notify.put("notificationNote","Test");
        new_notify.put("notificationType","1");
        new_notify.put("notificationReadStatus","0");
        new_notify.put("notificationDeleteStatus","0");
        notify_add_Ref.document("test_test").set(new_notify);
        Map<String,Object> new_notify2 = new HashMap<>();
        new_notify2.put("userId",user_id);
        new_notify2.put("notificationDate",formattedDate);
        new_notify2.put("notificationId","new_notify2");
        new_notify2.put("notificationNote","Test");
        new_notify2.put("notificationType","1");
        new_notify2.put("notificationReadStatus","0");
        new_notify2.put("notificationDeleteStatus","0");
        notify_add_Ref.document("new_notify2").set(new_notify);
    }

}