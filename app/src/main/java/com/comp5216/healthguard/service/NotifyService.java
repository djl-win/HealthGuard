package com.comp5216.healthguard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.comp5216.healthguard.fragment.portal.NotifyFragment;
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
import com.google.firebase.firestore.FirebaseFirestore;
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
    private List<Notification> notification_type_0_list = new ArrayList<>();
    public NotifyService() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = dateFormat.format(currentDate);
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
                        notifyRef.whereEqualTo("user_id",user_id)
                                .whereEqualTo("notification_read_status","0")
                                .whereEqualTo("notification_delete_status","0")
//                                .whereEqualTo("notification_type","0")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            if (!notification_type_0_list.isEmpty()){
                                                notification_type_0_list.clear();
                                            }
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                Map<String, Object> data = documentSnapshot.getData();
                                                String str_item_date = documentSnapshot.get("notification_date").toString();
                                                // current_date > item_date  === item is early 8:25 - 8.20 > 0
                                                if (documentSnapshot.get("notification_type").toString().equals("4")
                                                && DifferentTime(currentDate,str_item_date,dateFormat) >= 0){
                                                    notification_type_0_list.add(new Notification(
                                                            data.get("notification_id").toString(),
                                                            data.get("user_id").toString(),
                                                            data.get("notification_note").toString(),
                                                            data.get("notification_date").toString(),
                                                            data.get("notification_type").toString(),
                                                            data.get("notification_read_status").toString(),
                                                            data.get("notification_delete_status").toString()
                                                    ));
                                                }
                                            }
                                            Collections.sort(notification_type_0_list);
                                            LogUtils.e(notification_type_0_list.size());
                                            if (task.getResult().size() > SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_SIZE)){
                                                // TODO Refresh
                                                SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh","notification_refresh");
                                                EventBus.getDefault().postSticky(notification_refresh_event);
                                            }
                                        }
                                    }
                                });

                        for (int i = 0;i<notification_type_0_list.size();i++){
                            LogUtils.e(notification_type_0_list.get(i).getNotification_date());
                        }
                        // TODO 判断15分钟未吃
                        // TODO Document name
                        if (!notification_type_0_list.isEmpty()){
                            if (DifferentTime(currentDate,notification_type_0_list.get(0).getNotification_date(),dateFormat) == 0){
                                DocumentReference notify_update_Ref = db.collection("notification").document("YyKfIet5AQOVxLOAOyjd");
                                notify_update_Ref
                                        .update("notification_type", "0")
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
                            if (DifferentTime(currentDate,notification_type_0_list.get(0).getNotification_date(),dateFormat) >= 15){
                                CollectionReference notify_add_Ref = db.collection("notification");
                                Map<String,Object> new_notify = new HashMap<>();
                                new_notify.put("user_id",notification_type_0_list.get(0).getUser_id());
                                new_notify.put("notification_date",formattedDate);
                                new_notify.put("notification_id","test_08");
                                new_notify.put("notification_note","You did not eat xxx");
                                new_notify.put("notification_type","1");
                                new_notify.put("notification_read_status","0");
                                new_notify.put("notification_delete_status","0");
                                notify_add_Ref.document("test08").set(new_notify);
                                // detele type 0
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
                            }
                            SendNotificationRefreshEvent notification_refresh_event = new SendNotificationRefreshEvent("send_notification_refresh","notification_refresh");
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

}