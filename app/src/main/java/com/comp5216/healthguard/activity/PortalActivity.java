package com.comp5216.healthguard.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.UserWithMessage;
import com.comp5216.healthguard.fragment.chat.ChatFragment;
import com.comp5216.healthguard.fragment.index.IndexFragment;
import com.comp5216.healthguard.fragment.notify.NotifyFragment;
import com.comp5216.healthguard.fragment.search.SearchFragment;
import com.comp5216.healthguard.fragment.setting.SettingFragment;
import com.comp5216.healthguard.util.CustomNotificationUtil;
import com.comp5216.healthguard.util.CustomCache;
import com.comp5216.healthguard.viewmodel.NotificationViewModel;
import com.comp5216.healthguard.viewmodel.RelationShipViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


public class PortalActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_indexIcon;
    private ImageView iv_searchIcon;
    private ImageView iv_notifyIcon;
    private ImageView iv_chatIcon;
    private ImageView iv_settingIcon;
    private Animation animation;
    private FirebaseFirestore db;
    private String user_id;

    private ChatFragment chatFragment;

    private IndexFragment indexFragment;

    private NotifyFragment notifyFragment;

    private SearchFragment searchFragment;

    private SettingFragment settingFragment;

    private RelationShipViewModel relationShipViewModel;

    private TextView textViewChatBudge;

    private NotificationViewModel notificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        initView();
        // 给每个user设置一个FCM token
        setFCMToken();
    }

    private void initView() {
        relationShipViewModel = new ViewModelProvider(this).get(RelationShipViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        // 观测未读消息数量
        textViewChatBudge = findViewById(R.id.portal_chat_budge);
        observeUnreadMessages();

        iv_indexIcon = findViewById(R.id.iv_indexIcon);
        iv_searchIcon = findViewById(R.id.iv_searchIcon);
        iv_notifyIcon = findViewById(R.id.iv_notifyIcon);
        iv_chatIcon = findViewById(R.id.iv_chatIcon);
        iv_settingIcon = findViewById(R.id.iv_settingIcon);
        iv_indexIcon.setOnClickListener(this);
        iv_searchIcon.setOnClickListener(this);
        iv_notifyIcon.setOnClickListener(this);
        iv_chatIcon.setOnClickListener(this);
        iv_settingIcon.setOnClickListener(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_menu);

        db = FirebaseFirestore.getInstance();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chatFragment = new ChatFragment();
        indexFragment = new IndexFragment();
        notifyFragment = new NotifyFragment();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_index, chatFragment)
                .add(R.id.fl_index, indexFragment)
                .add(R.id.fl_index, notifyFragment)
                .add(R.id.fl_index, settingFragment)
                .add(R.id.fl_index, searchFragment)
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(notifyFragment)
                .hide(settingFragment)
                .show(indexFragment)
                .commit();

        askNotificationPermission();
        askAlarmPermission();
    }


    /**
     * 检测用户是否开启了通知
     */
    private void askNotificationPermission() {
        if (!CustomNotificationUtil.areNotificationsEnabled(this)) {
            showEnableNotificationDialog();
        }
    }

    /**
     * 展示页面，提醒用户开启通知
     */
    private void showEnableNotificationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("The notification permission is disabled")
                .setMessage("We need to send a health notice. Please enable notification.")
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomNotificationUtil.openNotificationSettings(PortalActivity.this);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * 请求用户alarm权限
     */
    private void askAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isAlarmPermissionGranted()) {
                showAlarmPermissionDialog();
            }
        }
        // 如果操作系统版本低于Android 12，您的应用可能不需要请求此权限。
    }

    /**
     * 检查是否已经具有闹钟权限
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    private boolean isAlarmPermissionGranted() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        return alarmManager.canScheduleExactAlarms();
    }

    /**
     * 显示闹钟权限对话框
     */
    private void showAlarmPermissionDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            new AlertDialog.Builder(this)
                    .setMessage("Your app needs precise alarm permissions to schedule reminders accurately. Please open it.")
                    .setPositiveButton("Open", (dialog, which) -> openAlarmSettings())
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    /**
     * 打开闹钟权限设置页面
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void openAlarmSettings() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if (v == iv_indexIcon) {
            showIndex();
        } else if (v == iv_searchIcon) {
            showSearch();
        } else if (v == iv_notifyIcon) {
            showNotify();
        } else if (v == iv_chatIcon) {
            showChat();
        } else if (v == iv_settingIcon) {
            showSetting();
        }
    }

    private void showIndex() {
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_indexIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(indexFragment)
                .commit();
    }

    private void showSearch() {
        iv_indexIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_searchIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(indexFragment)
                .hide(chatFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(searchFragment)
                .commit();
    }

    private void showNotify() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_notifyIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(indexFragment)
                .show(notifyFragment)
                .commit();
    }

    private void showChat() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_chatIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(indexFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(chatFragment)
                .commit();
    }

    private void showSetting() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(indexFragment)
                .hide(notifyFragment)
                .show(settingFragment)
                .commit();
    }


    /**
     * 观察未读消息的数量
     */
    private void observeUnreadMessages() {
        // 从ViewModel获取所有与当前用户ID关联的好友数据，并注册LiveData的观察者
        relationShipViewModel.getUserWithMessagesData(user_id).observe(this, usersWithMessage -> {
            // 如果从数据库中获取的用户数据不为空
            if (usersWithMessage != null) {
                int count = 0;
                for (UserWithMessage userWithMessage : usersWithMessage) {
                    count += Integer.parseInt(userWithMessage.getUnreadMessageNumber());
                }

                if(count!=0){
                    textViewChatBudge.setText(String.valueOf(count));
                    textViewChatBudge.setVisibility(View.VISIBLE);
                }else {
                    textViewChatBudge.setVisibility(View.GONE);
                }

            }
        });

    }

    /**
     *  给每个user设置一个FCM token
     */
    private void setFCMToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("djl", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        CustomCache customCache = new CustomCache(PortalActivity.this); // 'this' 是 Context
                        customCache.saveUserFCM(token);

                        // 获取对用户文档的引用
                        DocumentReference userRef = db.collection("users").document(user_id);

                        // 或更新现有用户的属性
                        userRef.update("userFCM", token);
                    }
                });

    }


}