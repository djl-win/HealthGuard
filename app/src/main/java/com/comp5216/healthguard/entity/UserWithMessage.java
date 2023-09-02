package com.comp5216.healthguard.entity;

/**
 * 用户实体类带有最新的未读信息和未读消息数量，DTO类
 * <p>
 * 存储用户基本信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-23
 */
public class UserWithMessage {

    private User user;
    // 临时变量，用户的最新消息
    private String lastMessage;
    // 临时变量，用户的最新消息的时间
    private String lastMessageTimeStamp;
    // 临时变量，用户的未读消息数量
    private String unreadMessageNumber;

    public String getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(String lastMessageTimeStamp) {
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUnreadMessageNumber() {
        return unreadMessageNumber;
    }

    public void setUnreadMessageNumber(String unreadMessageNumber) {
        this.unreadMessageNumber = unreadMessageNumber;
    }
}
