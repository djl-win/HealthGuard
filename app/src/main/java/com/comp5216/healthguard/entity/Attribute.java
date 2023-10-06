package com.comp5216.healthguard.entity;

/**
 * 用户属性
 * <p>
 * 存储用户预警的属性
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class Attribute {
    private String attributeId;  // 用String类型来存28位唯一标识符
    private String userId;  // 外键, 与User表的userId关联

    private String attributeSystolicLow;
    private String attributeSystolicHigh;

    private String attributeDiastolicLow;
    private String attributeDiastolicHigh;

    private String attributeHeartRateLow;
    private String attributeHeartRateHigh;

    private String attributeBodyTemperatureLow;
    private String attributeBodyTemperatureHigh;

    private String attributeBloodOxygenLow;
    private String attributeBloodOxygenHigh;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttributeSystolicLow() {
        return attributeSystolicLow;
    }

    public void setAttributeSystolicLow(String attributeSystolicLow) {
        this.attributeSystolicLow = attributeSystolicLow;
    }

    public String getAttributeSystolicHigh() {
        return attributeSystolicHigh;
    }

    public void setAttributeSystolicHigh(String attributeSystolicHigh) {
        this.attributeSystolicHigh = attributeSystolicHigh;
    }

    public String getAttributeDiastolicLow() {
        return attributeDiastolicLow;
    }

    public void setAttributeDiastolicLow(String attributeDiastolicLow) {
        this.attributeDiastolicLow = attributeDiastolicLow;
    }

    public String getAttributeDiastolicHigh() {
        return attributeDiastolicHigh;
    }

    public void setAttributeDiastolicHigh(String attributeDiastolicHigh) {
        this.attributeDiastolicHigh = attributeDiastolicHigh;
    }

    public String getAttributeHeartRateLow() {
        return attributeHeartRateLow;
    }

    public void setAttributeHeartRateLow(String attributeHeartRateLow) {
        this.attributeHeartRateLow = attributeHeartRateLow;
    }

    public String getAttributeHeartRateHigh() {
        return attributeHeartRateHigh;
    }

    public void setAttributeHeartRateHigh(String attributeHeartRateHigh) {
        this.attributeHeartRateHigh = attributeHeartRateHigh;
    }

    public String getAttributeBodyTemperatureLow() {
        return attributeBodyTemperatureLow;
    }

    public void setAttributeBodyTemperatureLow(String attributeBodyTemperatureLow) {
        this.attributeBodyTemperatureLow = attributeBodyTemperatureLow;
    }

    public String getAttributeBodyTemperatureHigh() {
        return attributeBodyTemperatureHigh;
    }

    public void setAttributeBodyTemperatureHigh(String attributeBodyTemperatureHigh) {
        this.attributeBodyTemperatureHigh = attributeBodyTemperatureHigh;
    }

    public String getAttributeBloodOxygenLow() {
        return attributeBloodOxygenLow;
    }

    public void setAttributeBloodOxygenLow(String attributeBloodOxygenLow) {
        this.attributeBloodOxygenLow = attributeBloodOxygenLow;
    }

    public String getAttributeBloodOxygenHigh() {
        return attributeBloodOxygenHigh;
    }

    public void setAttributeBloodOxygenHigh(String attributeBloodOxygenHigh) {
        this.attributeBloodOxygenHigh = attributeBloodOxygenHigh;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attributeId='" + attributeId + '\'' +
                ", userId='" + userId + '\'' +
                ", attributeSystolicLow='" + attributeSystolicLow + '\'' +
                ", attributeSystolicHigh='" + attributeSystolicHigh + '\'' +
                ", attributeDiastolicLow='" + attributeDiastolicLow + '\'' +
                ", attributeDiastolicHigh='" + attributeDiastolicHigh + '\'' +
                ", attributeHeartRateLow='" + attributeHeartRateLow + '\'' +
                ", attributeHeartRateHigh='" + attributeHeartRateHigh + '\'' +
                ", attributeBodyTemperatureLow='" + attributeBodyTemperatureLow + '\'' +
                ", attributeBodyTemperatureHigh='" + attributeBodyTemperatureHigh + '\'' +
                ", attributeBloodOxygenLow='" + attributeBloodOxygenLow + '\'' +
                ", attributeBloodOxygenHigh='" + attributeBloodOxygenHigh + '\'' +
                '}';
    }
}
