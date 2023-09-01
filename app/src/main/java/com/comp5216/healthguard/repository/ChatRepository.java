package com.comp5216.healthguard.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天信息仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户聊天信息的增删改查的操作
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class ChatRepository {
    // firebase realtime数据库实例
    FirebaseDatabase db;

    public ChatRepository() {
        this.db = FirebaseDatabase.getInstance();
    }

    /**
     * 将用户的聊天记录插入到realtime数据库
     * @param chatMessage 要插入数据库的数据
     */
    public void insertMessage(Chat chatMessage) {
        DatabaseReference mDatabaseReference = db.getReference("chats");

        // 生成新的ID
        String messageId = CustomIdGeneratorUtil.generateUniqueId();
        chatMessage.setChatMessageId(messageId);

        // 设置消息的读取状态为未读
        chatMessage.setChatMessageReadStatus("0");

        // 插入或更新消息
        mDatabaseReference
                .child(chatMessage.getChatId())
                .child(chatMessage.getChatMessageId())
                .setValue(chatMessage);
    }


}
