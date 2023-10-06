package com.comp5216.healthguard.entity;


/**
 * 用户的通知信息
 * <p>
 * 存储用户的通知信息
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class Notification {

    private String notificationId;  // 28位唯一标识符
    private String userId;  // 与User表的userId关联

    private String notificationNote;  // 加密后的通知内容或消息
    private long notificationDate;  // 加密后的通知日期或时间
    private int notificationType;  // 加密后的通知类型 0 健康问题， 1 报告
    private int notificationReadStatus;  // 0: 未读, 1: 已读
    private int notificationDeleteStatus;  // 0: 未删除, 1: 删除

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotificationNote() {
        return notificationNote;
    }

    public void setNotificationNote(String notificationNote) {
        this.notificationNote = notificationNote;
    }

    public long getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(long notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getNotificationReadStatus() {
        return notificationReadStatus;
    }

    public void setNotificationReadStatus(int notificationReadStatus) {
        this.notificationReadStatus = notificationReadStatus;
    }

    public int getNotificationDeleteStatus() {
        return notificationDeleteStatus;
    }

    public void setNotificationDeleteStatus(int notificationDeleteStatus) {
        this.notificationDeleteStatus = notificationDeleteStatus;
    }
}
