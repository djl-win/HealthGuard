package com.comp5216.healthguard.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;

/**
 * 用于提醒用户开启通知的工具类
 * <p>
 * 用于提醒用户开启通知的工具类
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class CustomNotificationUtil {

    // 检查通知是否被允许
    public static boolean areNotificationsEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    // 引导用户到系统设置页面来开启通知
    public static void openNotificationSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        // for Android 5-7
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        // for Android 8 and above
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
        context.startActivity(intent);
    }
}
