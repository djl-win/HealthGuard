package com.comp5216.healthguard.obj.portal;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Notification implements Comparable<Notification>{
    private String notification_id;
    private String user_id;
    private String notification_note;
    private String notification_date;
    // Notification Type
    // 0 - Time to eat
    // 1 - Did not eat
    // 2 - File upload
    // 3 - Healthy abnormal
    // 4 - 0 time
    private String notification_type;
    private String notification_read_status;
    private String notification_delete_status;

    public Notification(String notification_id, String user_id, String notification_note, String notification_date, String notification_type, String notification_read_status, String notification_delete_status){
        String doc_id;
        this.notification_id = notification_id;
        this.user_id = user_id;
        this.notification_note = notification_note;
        this.notification_date = notification_date;
        this.notification_type = notification_type;
        this.notification_read_status = notification_read_status;
        this.notification_delete_status = notification_delete_status;
    }
    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_id() {
        return this.notification_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setNotification_note(String notification_note) {
        this.notification_note = notification_note;
    }

    public String getNotification_note() {
        return this.notification_note;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_date() {
        return this.notification_date;
    }


    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getNotification_type() {
        return this.notification_type;
    }

    public void setNotification_read_status(String notification_read_status) {
        this.notification_read_status = notification_read_status;
    }

    public String getNotification_read_status() {
        return this.notification_read_status;
    }

    public void setNotification_delete_status(String notification_delete_status) {
        this.notification_delete_status = notification_delete_status;
    }

    public String getNotification_delete_status() {
        return this.notification_delete_status;
    }

    @Override
    public int compareTo(Notification notification) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int date = 0;
        try {
            date = (int) (dateFormat.parse(this.notification_date).getTime() - dateFormat.parse(notification.getNotification_date()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
