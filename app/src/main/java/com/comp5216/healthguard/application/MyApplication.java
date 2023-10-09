package com.comp5216.healthguard.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.lifecycle.ViewModelProvider;

import com.comp5216.healthguard.viewmodel.NotificationViewModel;


/**
 * application class to process notification
 * <p>
 * application class to process notification
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-09
 */
public class MyApplication extends Application {

    public static final String CHANNEL_ID = "example_channel";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        CharSequence name = "Example Channel";
        String description = "Channel for example notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
