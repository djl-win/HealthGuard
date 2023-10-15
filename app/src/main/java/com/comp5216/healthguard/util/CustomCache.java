package com.comp5216.healthguard.util;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义缓存类，用于存储各种缓存信息
 * <p>
 * 比如USER FCM, 用户的所有药物提醒的ID
 * </p>
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class CustomCache {

    // 文件名用于存储shared preferences
    private static final String PREFERENCES_FILE_NAME = "HealthGuardPreferences";

    // 新增的键，用于存储提醒ID
    private static final String KEY_REMINDER_IDS = "ReminderIds";


    // 键名常量，用于在存取数据时引用特定数据项
    private static final String KEY_USER_FCM = "UserFCM";

    // SharedPreferences实例，用于读写数据
    private SharedPreferences sharedPreferences;

    // 构造函数，初始化shared preferences
    public CustomCache(Context context) {
        // MODE_PRIVATE: 该文件只能被当前应用访问
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    // 存储userFCM到shared preferences
    public void saveUserFCM(String userFCM) {
        // 获取SharedPreferences编辑器对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将userFCM存储到SharedPreferences
        editor.putString(KEY_USER_FCM, userFCM);
        // 异步保存更改
        editor.apply();
    }

    // 从shared preferences中检索userFCM
    public String getUserFCM() {
        // 如果找不到值，返回空字符串（""）作为默认值
        return sharedPreferences.getString(KEY_USER_FCM, "");
    }

    // 清除shared preferences中的所有数据
    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 清除所有数据
        editor.clear();
        // 异步保存更改
        editor.apply();
    }

    // 从shared preferences中移除userFCM
    public void removeUserFCM() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 移除特定的键值对
        editor.remove(KEY_USER_FCM);
        // 异步保存更改
        editor.apply();
    }

    // 存储提醒ID到shared preferences
    public void storeReminderId(String reminderId) {
        String jsonIds = this.sharedPreferences.getString(KEY_REMINDER_IDS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonIds);

            // 检查列表中是否已包含相同的提醒ID
            if (!jsonArray.toString().contains(reminderId)) {
                jsonArray.put(reminderId);
                SharedPreferences.Editor editor = this.sharedPreferences.edit();
                editor.putString(KEY_REMINDER_IDS, jsonArray.toString());
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace(); // 处理解析异常
        }
    }

    // 从shared preferences检索所有提醒ID，返回字符串列表
    public List<String> retrieveAllReminderIds() {
        String jsonIds = this.sharedPreferences.getString(KEY_REMINDER_IDS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonIds);
            List<String> reminderIds = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                reminderIds.add(jsonArray.getString(i));
            }
            return reminderIds;
        } catch (JSONException e) {
            e.printStackTrace(); // 处理解析异常
            return new ArrayList<>(); // 如果出现异常，返回空的字符串列表
        }
    }
    // 清除所有提醒ID
    public void clearAllReminderIds() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(KEY_REMINDER_IDS, "[]"); // 清空JSON数组
        editor.apply();
    }
}
