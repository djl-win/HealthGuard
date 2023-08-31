package com.comp5216.healthguard.entity;

/**
 * 用户的聊天信息
 * <p>
 * 存储用户预警的属性
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class Chat {
    String chatId;
    String chatSenderID;
    String chatMassageText;
    String chatTimestamp;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatSenderID() {
        return chatSenderID;
    }

    public void setChatSenderID(String chatSenderID) {
        this.chatSenderID = chatSenderID;
    }

    public String getChatMassageText() {
        return chatMassageText;
    }

    public void setChatMassageText(String chatMassageText) {
        this.chatMassageText = chatMassageText;
    }

    public String getChatTimestamp() {
        return chatTimestamp;
    }

    public void setChatTimestamp(String chatTimestamp) {
        this.chatTimestamp = chatTimestamp;
    }
}
