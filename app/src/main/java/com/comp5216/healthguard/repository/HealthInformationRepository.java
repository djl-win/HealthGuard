package com.comp5216.healthguard.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 健康信息仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户健康信息的增删改查的操作
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class HealthInformationRepository {
    // firebase数据库实例
    private final FirebaseFirestore db;
    // firebase的auth
    FirebaseAuth auth;
    // attribute repository
    AttributeRepository attributeRepository;
    // notification repository
    NotificationRepository notificationRepository;
    // 用户的所有健康数据
    private final MutableLiveData<List<HealthInformation>> healthInformationLiveData;

    public HealthInformationRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        attributeRepository = new AttributeRepository();
        notificationRepository = new NotificationRepository();
        this.healthInformationLiveData = new MutableLiveData<>();
    }

    /**
     * 存储用户健康信息到数据库
     *
     * @param healthInformation 用户信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeHealthInformation(HealthInformation healthInformation, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        // id
        healthInformation.setHealthInformationId(CustomIdGeneratorUtil.generateUniqueId());
        // userid
        healthInformation.setUserId(auth.getUid());
        // date
        healthInformation.setHealthInformationDate(System.currentTimeMillis());

        // 判断指标是否超出异常范围，如果超出异常，存到通知库里面
        attributeRepository.getAttributeByUserId(healthInformation.getUserId(),
                attribute -> {
                    if (
                                    Double.parseDouble(healthInformation.getHealthInformationSystolic()) > Double.parseDouble(attribute.getAttributeSystolicHigh()) ||
                                    Double.parseDouble(healthInformation.getHealthInformationSystolic()) < Double.parseDouble(attribute.getAttributeSystolicLow()) ||

                                    Double.parseDouble(healthInformation.getHealthInformationDiastolic()) > Double.parseDouble(attribute.getAttributeDiastolicHigh()) ||
                                    Double.parseDouble(healthInformation.getHealthInformationDiastolic()) < Double.parseDouble(attribute.getAttributeDiastolicLow()) ||

                                    Double.parseDouble(healthInformation.getHealthInformationHeartRate()) > Double.parseDouble(attribute.getAttributeHeartRateHigh()) ||
                                    Double.parseDouble(healthInformation.getHealthInformationHeartRate()) < Double.parseDouble(attribute.getAttributeHeartRateLow()) ||

                                    Double.parseDouble(healthInformation.getHealthInformationBodyTemperature()) > Double.parseDouble(attribute.getAttributeBodyTemperatureHigh()) ||
                                    Double.parseDouble(healthInformation.getHealthInformationBodyTemperature()) < Double.parseDouble(attribute.getAttributeBodyTemperatureLow()) ||

                                    Double.parseDouble(healthInformation.getHealthInformationBloodOxygen()) > Double.parseDouble(attribute.getAttributeBloodOxygenHigh()) ||
                                    Double.parseDouble(healthInformation.getHealthInformationBloodOxygen()) < Double.parseDouble(attribute.getAttributeBloodOxygenLow())
                    ) {

                        // 身体不健康
                        healthInformation.setHealthInformationHealthStatus(1);
                        // 查询用户姓名
                        db.collection("users")
                                .document(healthInformation.getUserId())
                                .get()

                                .addOnSuccessListener(success ->{
                                    // 将文档转换为User对象
                                    User user = success.toObject(User.class);
                                    try {
                                        // 发送通知到notification
                                        Notification notification = new Notification();
                                        notification.setNotificationDate(healthInformation.getHealthInformationDate());
                                        notification.setUserId(healthInformation.getUserId());
                                        notification.setNotificationNote(CustomEncryptUtil.decryptByAES(user.getUserName()) + " abnormal healthy data, please check");
                                        notification.setNotificationType(0);
                                        notificationRepository.storeNotification(notification);


                                    } catch (NoSuchPaddingException | IllegalBlockSizeException |
                                             NoSuchAlgorithmException | BadPaddingException |
                                             InvalidKeyException e) {
                                        // 抛出自定义异常
                                        throw new EncryptionException(e);
                                    }
                                });
                    } else {
                        // 身体健康
                        healthInformation.setHealthInformationHealthStatus(0);
                    }
                    // SHA256加密用户健康信息
                    try {
                        healthInformation.setHealthInformationSystolic(CustomEncryptUtil.encryptByAES(healthInformation.getHealthInformationSystolic()));
                        healthInformation.setHealthInformationDiastolic(CustomEncryptUtil.encryptByAES(healthInformation.getHealthInformationDiastolic()));
                        healthInformation.setHealthInformationHeartRate(CustomEncryptUtil.encryptByAES(healthInformation.getHealthInformationHeartRate()));
                        healthInformation.setHealthInformationBodyTemperature(CustomEncryptUtil.encryptByAES(healthInformation.getHealthInformationBodyTemperature()));
                        healthInformation.setHealthInformationBloodOxygen(CustomEncryptUtil.encryptByAES(healthInformation.getHealthInformationBloodOxygen()));

                    } catch (NoSuchPaddingException | IllegalBlockSizeException |
                             NoSuchAlgorithmException | BadPaddingException |
                             InvalidKeyException e) {
                        // 抛出自定义异常
                        throw new EncryptionException(e);
                    }

                    db.collection("healthInformation").document(healthInformation.getHealthInformationId())
                            .set(healthInformation)
                            .addOnSuccessListener(successListener)
                            .addOnFailureListener(failureListener);

                },
                e -> {
                    // 查询身体健康属性失败，暂时不做处理
                });
    }

    /**
     * 通过用户的ID获取所有的健康信息
     * @param userId 当前用户的ID
     * @return 所有的用户相关健康信息
     */
    public LiveData<List<HealthInformation>> getInformationDataByUserId(String userId) {


        db.collection("healthInformation")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("djl", "listen:error", e);
                        return;
                    }

                    List<HealthInformation> healthInformationList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        HealthInformation healthInformation = doc.toObject(HealthInformation.class);

                        // SHA256解密用户健康信息
                        try {
                            healthInformation.setHealthInformationSystolic(CustomEncryptUtil.decryptByAES(healthInformation.getHealthInformationSystolic()));
                            healthInformation.setHealthInformationDiastolic(CustomEncryptUtil.decryptByAES(healthInformation.getHealthInformationDiastolic()));
                            healthInformation.setHealthInformationHeartRate(CustomEncryptUtil.decryptByAES(healthInformation.getHealthInformationHeartRate()));
                            healthInformation.setHealthInformationBodyTemperature(CustomEncryptUtil.decryptByAES(healthInformation.getHealthInformationBodyTemperature()));
                            healthInformation.setHealthInformationBloodOxygen(CustomEncryptUtil.decryptByAES(healthInformation.getHealthInformationBloodOxygen()));

                        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                                 NoSuchAlgorithmException | BadPaddingException |
                                 InvalidKeyException ex) {
                            // 抛出自定义异常
                            throw new EncryptionException(ex);
                        }
                        if(!healthInformationList.contains(healthInformation)) {
                            healthInformationList.add(healthInformation);
                        }
                    }
                    healthInformationLiveData.setValue(healthInformationList);
                });

        return healthInformationLiveData;
    }


}
