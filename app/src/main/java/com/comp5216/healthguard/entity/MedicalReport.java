package com.comp5216.healthguard.entity;

/**
 * 用户的报告信息
 * <p>
 * 存储用户的报告信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-07
 */
public class MedicalReport {

    private String medicalReportId;  // 主键
    private String userId;  // 外键
    private String medicalReportNote;  // 医疗报告的文字说明
    private long medicalReportDate;  // 医疗报告的日期
    private int medicalReportDeleteStatus;  // 医疗报告的删除状态

    public String getMedicalReportId() {
        return medicalReportId;
    }

    public void setMedicalReportId(String medicalReportId) {
        this.medicalReportId = medicalReportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicalReportNote() {
        return medicalReportNote;
    }

    public void setMedicalReportNote(String medicalReportNote) {
        this.medicalReportNote = medicalReportNote;
    }

    public long getMedicalReportDate() {
        return medicalReportDate;
    }

    public void setMedicalReportDate(long medicalReportDate) {
        this.medicalReportDate = medicalReportDate;
    }

    public int getMedicalReportDeleteStatus() {
        return medicalReportDeleteStatus;
    }

    public void setMedicalReportDeleteStatus(int medicalReportDeleteStatus) {
        this.medicalReportDeleteStatus = medicalReportDeleteStatus;
    }

    @Override
    public String toString() {
        return "MedicalReport{" +
                "medicalReportId='" + medicalReportId + '\'' +
                ", userId='" + userId + '\'' +
                ", medicalReportNote='" + medicalReportNote + '\'' +
                ", medicalReportDate=" + medicalReportDate +
                ", medicalReportDeleteStatus=" + medicalReportDeleteStatus +
                '}';
    }
}
