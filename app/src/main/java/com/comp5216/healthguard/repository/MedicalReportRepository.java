package com.comp5216.healthguard.repository;

import android.content.Context;
import android.service.autofill.CustomDescription;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomFCMSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
 * 医疗报告仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户医疗报告的增删改查的操作
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class MedicalReportRepository {
    // firebase数据库实例
    private final FirebaseFirestore db;
    // firebase的auth
    FirebaseAuth auth;
    // notification repository
    NotificationRepository notificationRepository;

    // 用户的所有医疗报告
    private final MutableLiveData<List<MedicalReport>> medicalReportLiveData;

    public MedicalReportRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        notificationRepository = new NotificationRepository();
        this.medicalReportLiveData =  new MutableLiveData<>();
    }

    /**
     * 存储用户医疗报告信息到数据库
     * @param medicalReport 用户医疗报告信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeMedicalReport(Context context, MedicalReport medicalReport, List<User> friends, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // SHA256加密用户健康信息
        try {
            medicalReport.setMedicalReportNote(CustomEncryptUtil.encryptByAES(medicalReport.getMedicalReportNote()));

        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }

        db.collection("medicalReport").document(medicalReport.getMedicalReportId())
                .set(medicalReport)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);

        // 发送通知到notification
        Notification notification = new Notification();
        notification.setNotificationDate(medicalReport.getMedicalReportDate());
        notification.setUserId(medicalReport.getUserId());

        // 查询用户姓名
        db.collection("users")
                .document(medicalReport.getUserId())
                .get()

                .addOnSuccessListener(success ->{
                    // 将文档转换为User对象
                    User user = success.toObject(User.class);
                    try {
                        notification.setNotificationNote( CustomEncryptUtil.decryptByAES(user.getUserName()) + " new medical report, please check");
                        notification.setNotificationType(1);

                        // 发送提醒给相关用户
                        if(friends.size() != 0) {
                            for (int i = 0; i < friends.size(); i++) {
                                CustomFCMSender.sendFCMMessage(context, friends.get(i).getUserFCM(),"HealthGuard",notification.getNotificationNote());
                            }
                        }


                        notificationRepository.storeNotification(notification);
                    } catch (NoSuchPaddingException | IllegalBlockSizeException |
                             NoSuchAlgorithmException | BadPaddingException |
                             InvalidKeyException e) {
                        // 抛出自定义异常
                        throw new EncryptionException(e);
                    }
                });
    }

    /**
     * 通过用户ID获取所有的医疗报告数据
     * @param userId 当前用户的ID
     * @return 所有的所有的医疗报告数据
     */
    public LiveData<List<MedicalReport>> getReportDataByUserId(String userId) {


        db.collection("medicalReport")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("djl", "listen:error", e);
                        return;
                    }

                    List<MedicalReport> medicalReportList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        MedicalReport medicalReport = doc.toObject(MedicalReport.class);
                        try {
                            medicalReport.setMedicalReportNote(CustomEncryptUtil.decryptByAES(medicalReport.getMedicalReportNote()));
                        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                                 NoSuchAlgorithmException | BadPaddingException |
                                 InvalidKeyException ex) {
                            // 抛出自定义异常
                            throw new EncryptionException(ex);
                        }
                        medicalReportList.add(medicalReport);
                    }
                    medicalReportLiveData.setValue(medicalReportList);
                });

        return medicalReportLiveData;
    }
}
