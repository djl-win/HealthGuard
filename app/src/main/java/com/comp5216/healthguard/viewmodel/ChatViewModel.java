package com.comp5216.healthguard.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.repository.AttributeRepository;
import com.comp5216.healthguard.repository.ChatRepository;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天信息视图模型类，处理用户的聊天信息信息，返回到view层
 * <p>
 * 处理用户的聊天信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-09-01
 */
public class ChatViewModel extends ViewModel {
    // 用户聊天信息仓库
    private final ChatRepository repository;

    public ChatViewModel() {
        this.repository = new ChatRepository();
    }

    /**
     * 将用户的聊天记录插入到realtime数据库
     * @param chatMessage 要插入数据库的数据
     */
    public void insertMessage(Chat chatMessage) {
        repository.insertMessage(chatMessage);
    }



    /**
     * 通过用户的chatID获取对话框中的聊天信息
     * @return 所有的聊天信息
     */
    public LiveData<List<Chat>> getChatMessages(String chatId) {
        return repository.getChatMessages(chatId);
    }
}
