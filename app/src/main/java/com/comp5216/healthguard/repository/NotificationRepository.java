package com.comp5216.healthguard.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
    // 所有的相关通知
    private final MutableLiveData<List<Notification>> notificationLiveData;

    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
        notificationLiveData = new MutableLiveData<>();
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

    /**
     * 通过用户id查询所有相关通知
     *
     * @param userId 用户Id
     * @return 所有的相关通知
     */
    public LiveData<List<Notification>> getAllNotificationByUserId(String userId) {


        // 查询出所有相关的用户
        db.collection("relationship")
                .whereEqualTo("relationshipObserveId", userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    // 当前用户相关好友
                    ArrayList<String> userIds = new ArrayList<>();
                    for (DocumentSnapshot doc : documentSnapshot) {
                        Relationship relationship = doc.toObject(Relationship.class);
                        if(!userIds.contains(relationship.getRelationshipObserveId())) {
                            userIds.add(relationship.getRelationshipObservedId());
                        }
                    }
                    // 在这里查询通知，以确保userIds已被填充
                    queryNotifications(userIds);

                })
                .addOnFailureListener(e -> {
                    Log.w("djl", "listen:error");
                });


        return notificationLiveData;
    }


    /**
     * 查询好友的通知
     * @param userIds 所有的相关好友
     */
    private void queryNotifications(ArrayList<String> userIds) {
        // 如果没有好友，不执行查询
        if (userIds.isEmpty()) {
            Log.w("djl", "No friends found");
            return;
        }

        // 查询删除不为0的通知
        db.collection("notification")
                .whereIn("userId", userIds)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("djl", "listen:error");
                        return;
                    }
                    List<Notification> notificationList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        Notification notification = doc.toObject(Notification.class);
                        // 查询未删除的数据
                        if (notification.getNotificationDeleteStatus() == 0) {
                            try {
                                notification.setNotificationNote(CustomEncryptUtil.decryptByAES(notification.getNotificationNote()));
                                if(!notificationList.contains(notification)) {
                                    notificationList.add(notification);
                                }
                            } catch (NoSuchPaddingException | IllegalBlockSizeException |
                                     NoSuchAlgorithmException | BadPaddingException |
                                     InvalidKeyException ex) {
                                // 抛出自定义异常
                                throw new EncryptionException(ex);
                            }
                        }
                    }
                    // 根据notificationDate排序通知列表
                    notificationList.sort((n1, n2) -> {
                        return Long.compare(n2.getNotificationDate(), n1.getNotificationDate());  // 假设你想要降序排序
                    });

                    notificationLiveData.setValue(notificationList);
                });
    }

}
