package com.comp5216.healthguard.obj.entity;

/**
 * 用户实体类
 * <p>
 * 存储用户基本信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-23
 */
public class User {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userGender;
    // 临时变量，用户所处的聊天室ID
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
