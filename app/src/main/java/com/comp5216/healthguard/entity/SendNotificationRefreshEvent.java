package com.comp5216.healthguard.entity;

public class SendNotificationRefreshEvent{
    private String type;
    private String order;
    public SendNotificationRefreshEvent(String type,String order){
        this.type = type;
        this.order = order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
