package com.comp5216.healthguard.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
    // 用户间的聊天信息
    private final MutableLiveData<List<Chat>> chatMessagesLiveData;

    public ChatRepository() {
        this.db = FirebaseDatabase.getInstance();
        this.chatMessagesLiveData = new MutableLiveData<>();
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

    /**
     * 通过用户的chatID获取对话框中的聊天信息
     * @param chatId 当前聊天框的ID
     * @return 所有的聊天信息
     */
    public LiveData<List<Chat>> getChatMessages(String chatId) {
        DatabaseReference mDatabaseReference = db.getReference("chats");
        mDatabaseReference.child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chat> chatMessages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Chat chatMessage = messageSnapshot.getValue(Chat.class);
                    chatMessages.add(chatMessage);
                }

                // 按时间戳升序排序
                chatMessages.sort((o1, o2) -> {
                    long timestamp1 = Long.parseLong(o1.getChatMessageTimestamp());
                    long timestamp2 = Long.parseLong(o2.getChatMessageTimestamp());
                    return Long.compare(timestamp1, timestamp2);
                });

                chatMessagesLiveData.setValue(chatMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        return chatMessagesLiveData;
    }

}