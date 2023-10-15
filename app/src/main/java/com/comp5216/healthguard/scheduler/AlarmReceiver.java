package com.comp5216.healthguard.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.comp5216.healthguard.util.CustomFCMSender;

/**
 * 用于接收用户发送吃药提醒的alarm广播
 * <p>
 * 用于接收用户发送吃药提醒的alarm广播
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-15
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 从Intent获取你的提醒ID或其他数据
        String reminderId = intent.getStringExtra("REMINDER_ID");
        String userFCM = intent.getStringExtra("USER_FCM");
        String reminderNote = intent.getStringExtra("REMINDER_NOTE");

        // 这里执行当闹钟触发时需要完成的工作
        Log.d("djl", "提醒ID: " + reminderId); // 打印接收到的提醒ID
        Log.d("djl", "用户FCM: " + userFCM); // 打印用户FCM token
        Log.d("djl", "提醒备注: " + reminderNote); // 打印提醒备注

        // 发送通知，提醒用户吃药
        CustomFCMSender.sendFCMMessage(context,userFCM,"HealthGuard",reminderNote);


    }
}
