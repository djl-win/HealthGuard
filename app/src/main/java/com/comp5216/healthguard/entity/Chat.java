package com.comp5216.healthguard.entity;

/**
 * 用户的聊天信息
 * <p>
 * 存储用户预警的属性
 * </p>
 *
 * @author Jiale Dong
 * @version 1.1
 * @since 2023-09-01
 */
public class Chat {
    // 聊天的ID
    String chatId;
    // 聊天信息的ID
    String chatMessageId;
    // 消息发送者的ID
    String chatMessageSenderID;
    // 消息的内容
    String chatMessageText;
    // 消息的发送时间
    String chatMessageTimestamp;
    // 消息的已读状态，0未读，1已读
    String chatMessageReadStatus;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public String getChatMessageSenderID() {
        return chatMessageSenderID;
    }

    public void setChatMessageSenderID(String chatMessageSenderID) {
        this.chatMessageSenderID = chatMessageSenderID;
    }

    public String getChatMessageText() {
        return chatMessageText;
    }

    public void setChatMessageText(String chatMessageText) {
        this.chatMessageText = chatMessageText;
    }


    public void setChatMessageTimestamp(String chatMessageTimestamp) {
        this.chatMessageTimestamp = chatMessageTimestamp;
    }


    public String getChatMessageTimestamp() {
        return chatMessageTimestamp;
    }

    public String getChatMessageReadStatus() {
        return chatMessageReadStatus;
    }

    public void setChatMessageReadStatus(String chatMessageReadStatus) {
        this.chatMessageReadStatus = chatMessageReadStatus;
    }
}
