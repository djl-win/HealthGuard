package com.comp5216.healthguard.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.entity.Relationship;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 用药提醒仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户用药提醒的增删改查的操作
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class MedicationReminderRepository {
    // firebase数据库实例
    private final FirebaseFirestore db;
    // auth
    private final FirebaseAuth auth;
    // 用户所有的用药提醒
    private final MutableLiveData<List<MedicationReminder>> medicationReminderLiveData;

    public MedicationReminderRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.medicationReminderLiveData = new MutableLiveData<>();
    }

    /**
     * 存储用户用要提醒信息到数据库
     * @param medicationReminder 用户用药提醒
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeMedicationReminder(MedicationReminder medicationReminder, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {

        // SHA256加密用户健康信息
        try {
            medicationReminder.setMedicationReminderId(CustomIdGeneratorUtil.generateUniqueId());
            medicationReminder.setUserId(auth.getUid());
            medicationReminder.setMedicationReminderDrugName(CustomEncryptUtil.encryptByAES(medicationReminder.getMedicationReminderDrugName()));
            medicationReminder.setMedicationReminderDrugDosage(CustomEncryptUtil.encryptByAES(medicationReminder.getMedicationReminderDrugDosage()));
            medicationReminder.setMedicationReminderDrugNote(CustomEncryptUtil.encryptByAES(medicationReminder.getMedicationReminderDrugNote()));


        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }

        db.collection("medicationReminder").document(medicationReminder.getMedicationReminderId())
                .set(medicationReminder)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    /**
     * 通过用户的ID查询所有的用药提醒
     * @param userId 用户id
     * @return 所有的用药提醒
     */
    public LiveData<List<MedicationReminder>> getAllMedicationReminderByUserId(String userId){
        // 查询出所有相关的用户
        db.collection("medicationReminder")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("djl", "listen:error");
                        return;
                    }
                    List<MedicationReminder> medicationReminders = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        MedicationReminder medicationReminder = doc.toObject(MedicationReminder.class);
                        // 查询未删除的数据
                        if (medicationReminder.getMedicationReminderDeleteStatus() == 0) {
                            try {
                                //解密
                                medicationReminder.setMedicationReminderDrugName(CustomEncryptUtil.decryptByAES(medicationReminder.getMedicationReminderDrugName()));
                                medicationReminder.setMedicationReminderDrugDosage(CustomEncryptUtil.decryptByAES(medicationReminder.getMedicationReminderDrugDosage()));
                                medicationReminder.setMedicationReminderDrugNote(CustomEncryptUtil.decryptByAES(medicationReminder.getMedicationReminderDrugNote()));
                                if(!medicationReminders.contains(medicationReminder)) {
                                    medicationReminders.add(medicationReminder);
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
                    medicationReminders.sort((r1, r2) -> {
                        LocalTime time1 = LocalTime.parse(r1.getMedicationReminderDrugTime());
                        LocalTime time2 = LocalTime.parse(r2.getMedicationReminderDrugTime());
                        return time1.compareTo(time2);
                    });

                    medicationReminderLiveData.setValue(medicationReminders);
                });

        return medicationReminderLiveData;
    }
}
