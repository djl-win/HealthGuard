package com.comp5216.healthguard.repository;

import android.util.Log;

import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 通知信息仓库类，处理数据库查询语句
 * <p>
 * 提供了对通知信息的增删改查的操作
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class NotificationRepository {

    // firebase数据库实例
    private final FirebaseFirestore db;

    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void storeNotification(Notification notification) {
        notification.setNotificationId(CustomIdGeneratorUtil.generateUniqueId());
        notification.setNotificationReadStatus(0);
        notification.setNotificationDeleteStatus(0);

        // SHA256加密通知信息
        try {
            // SHA256加密
            notification.setNotificationNote(CustomEncryptUtil.encryptByAES(notification.getNotificationNote()));

        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }

        db.collection("notification").document(notification.getNotificationId())
                .set(notification);
    }
}
