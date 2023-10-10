package com.comp5216.healthguard.entity;

/**
 * 用户的健康信息
 * <p>
 * 存储用户的健康信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-07
 */
public class HealthInformation {

    String healthInformationId;
    String userId;
    String healthInformationSystolic;
    String healthInformationDiastolic;
    String healthInformationHeartRate;
    String healthInformationBodyTemperature;
    String healthInformationBloodOxygen;
    long healthInformationDate;
    int healthInformationHealthStatus;
    int healthInformationDeleteStatus;

    public String getHealthInformationId() {
        return healthInformationId;
    }

    public void setHealthInformationId(String healthInformationId) {
        this.healthInformationId = healthInformationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHealthInformationSystolic() {
        return healthInformationSystolic;
    }

    public void setHealthInformationSystolic(String healthInformationSystolic) {
        this.healthInformationSystolic = healthInformationSystolic;
    }

    public String getHealthInformationDiastolic() {
        return healthInformationDiastolic;
    }

    public void setHealthInformationDiastolic(String healthInformationDiastolic) {
        this.healthInformationDiastolic = healthInformationDiastolic;
    }

    public String getHealthInformationHeartRate() {
        return healthInformationHeartRate;
    }

    public void setHealthInformationHeartRate(String healthInformationHeartRate) {
        this.healthInformationHeartRate = healthInformationHeartRate;
    }

    public String getHealthInformationBodyTemperature() {
        return healthInformationBodyTemperature;
    }

    public void setHealthInformationBodyTemperature(String healthInformationBodyTemperature) {
        this.healthInformationBodyTemperature = healthInformationBodyTemperature;
    }

    public String getHealthInformationBloodOxygen() {
        return healthInformationBloodOxygen;
    }

    public void setHealthInformationBloodOxygen(String healthInformationBloodOxygen) {
        this.healthInformationBloodOxygen = healthInformationBloodOxygen;
    }

    public long getHealthInformationDate() {
        return healthInformationDate;
    }

    public void setHealthInformationDate(long healthInformationDate) {
        this.healthInformationDate = healthInformationDate;
    }

    public int getHealthInformationHealthStatus() {
        return healthInformationHealthStatus;
    }

    public void setHealthInformationHealthStatus(int healthInformationHealthStatus) {
        this.healthInformationHealthStatus = healthInformationHealthStatus;
    }

    public int getHealthInformationDeleteStatus() {
        return healthInformationDeleteStatus;
    }

    public void setHealthInformationDeleteStatus(int healthInformationDeleteStatus) {
        this.healthInformationDeleteStatus = healthInformationDeleteStatus;
    }

    @Override
    public String toString() {
        return "HealthInformation{" +
                "healthInformationId='" + healthInformationId + '\'' +
                ", userId='" + userId + '\'' +
                ", healthInformationSystolic='" + healthInformationSystolic + '\'' +
                ", healthInformationDiastolic='" + healthInformationDiastolic + '\'' +
                ", healthInformationHeartRate='" + healthInformationHeartRate + '\'' +
                ", healthInformationBodyTemperature='" + healthInformationBodyTemperature + '\'' +
                ", healthInformationBloodOxygen='" + healthInformationBloodOxygen + '\'' +
                ", healthInformationDate=" + healthInformationDate +
                ", healthInformationHealthStatus=" + healthInformationHealthStatus +
                ", healthInformationDeleteStatus=" + healthInformationDeleteStatus +
                '}';
    }
}

