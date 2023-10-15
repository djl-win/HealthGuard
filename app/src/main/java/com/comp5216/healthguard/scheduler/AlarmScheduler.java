package com.comp5216.healthguard.scheduler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 用于给用户发送吃药提醒的alarm
 * <p>
 * 用于给用户发送吃药提醒的alarm
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-15
 */
public class AlarmScheduler {

    /**
     * 计划一个新的提醒，并在设置之前取消任何现有的相同ID提醒。
     *
     * @param context                应用程序的上下文
     * @param timeInMillis           提醒触发的时间（以毫秒为单位）
     * @param medicationReminderId   药物提醒的唯一ID（字符串）
     */
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleReminder(Context context, long timeInMillis, String medicationReminderId, String userFCM, String reminderNote) {

        // 将提醒ID从字符串转换为整数
        int requestCode = medicationReminderId.hashCode();


        // 创建一个指向广播接收器的Intent，该广播接收器将执行提醒通知的逻辑
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("REMINDER_ID", medicationReminderId); // 将提醒ID作为额外的数据放入Intent
        intent.putExtra("USER_FCM", userFCM); // 将用户FCM作为额外的数据放入Intent
        intent.putExtra("REMINDER_NOTE", reminderNote); // 将用户FCM作为额外的数据放入Intent


        // 获取AlarmManager实例
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 没权限就不通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!alarmManager.canScheduleExactAlarms()){
                return;
            }
        }

        // 创建一个PendingIntent，当提醒触发时，系统将发送这个Intent
        // 使用由ID哈希生成的请求代码确保每个提醒都有一个唯一的PendingIntent
        // 在这里添加FLAG_IMMUTABLE标志
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 取消之前的提醒（如果存在的话）
        alarmManager.cancel(pendingIntent);

        // 设置新的单次提醒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    /**
     * 取消一个已经计划的提醒。
     *
     * @param context                应用程序的上下文
     * @param medicationReminderId   药物提醒的唯一ID（字符串）
     */
    public static void cancelReminder(Context context, String medicationReminderId) {

        // 将提醒ID从字符串转换为整数
        int requestCode = medicationReminderId.hashCode();

        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 没权限就不通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!alarmManager.canScheduleExactAlarms()){
                return;
            }
        }

        // 在这里添加FLAG_IMMUTABLE标志
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 取消与此PendingIntent匹配的提醒
        alarmManager.cancel(pendingIntent);
    }
}
