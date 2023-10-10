package com.comp5216.healthguard.entity;

/**
 * 用户的服药信息
 * <p>
 * 存储用户的服药信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-07
 */
public class MedicationReminder {
    private String medicationReminderId; // 主键(PK)。这是药物提醒的唯一标识符。
    private String userId; // 外键(FK)。与User表的user_id关联。
    private String medicationReminderDrugName; // 药物的名称。-- 加密
    private String medicationReminderDrugDosage; // 药物的剂量描述。-- 加密
    private String medicationReminderDrugTime; // 药物的服用时间。
    private String medicationReminderDrugNote; // 有关药物的附加注释或注意事项。-- 加密
    private int medicationReminderDeleteStatus = 0; // 表示药物提醒的删除状态。该字段的取值范围是：'0', '1'。0：记录未删除，1：记录已删除

    public String getMedicationReminderId() {
        return medicationReminderId;
    }

    public void setMedicationReminderId(String medicationReminderId) {
        this.medicationReminderId = medicationReminderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicationReminderDrugName() {
        return medicationReminderDrugName;
    }

    public void setMedicationReminderDrugName(String medicationReminderDrugName) {
        this.medicationReminderDrugName = medicationReminderDrugName;
    }

    public String getMedicationReminderDrugDosage() {
        return medicationReminderDrugDosage;
    }

    public void setMedicationReminderDrugDosage(String medicationReminderDrugDosage) {
        this.medicationReminderDrugDosage = medicationReminderDrugDosage;
    }

    public String getMedicationReminderDrugTime() {
        return medicationReminderDrugTime;
    }

    public void setMedicationReminderDrugTime(String medicationReminderDrugTime) {
        this.medicationReminderDrugTime = medicationReminderDrugTime;
    }

    public String getMedicationReminderDrugNote() {
        return medicationReminderDrugNote;
    }

    public void setMedicationReminderDrugNote(String medicationReminderDrugNote) {
        this.medicationReminderDrugNote = medicationReminderDrugNote;
    }

    public int getMedicationReminderDeleteStatus() {
        return medicationReminderDeleteStatus;
    }

    public void setMedicationReminderDeleteStatus(int medicationReminderDeleteStatus) {
        this.medicationReminderDeleteStatus = medicationReminderDeleteStatus;
    }

    @Override
    public String toString() {
        return "MedicationReminder{" +
                "medicationReminderId='" + medicationReminderId + '\'' +
                ", userId='" + userId + '\'' +
                ", medicationReminderDrugName='" + medicationReminderDrugName + '\'' +
                ", medicationReminderDrugDosage='" + medicationReminderDrugDosage + '\'' +
                ", medicationReminderDrugTime='" + medicationReminderDrugTime + '\'' +
                ", medicationReminderDrugNote='" + medicationReminderDrugNote + '\'' +
                ", medicationReminderDeleteStatus=" + medicationReminderDeleteStatus +
                '}';
    }
}
