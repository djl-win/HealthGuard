package com.comp5216.healthguard.viewmodel;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.repository.MedicationReminderRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


/**
 * 用要提醒视图模型类
 * <p>
 * 处理用要提醒视图模型类
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class MedicationReminderViewModel extends ViewModel {

    // 用户医疗报告仓库
    private final MedicationReminderRepository repository;

    public MedicationReminderViewModel() {
        this.repository = new MedicationReminderRepository();
    }

    /**
     * 存储用户用要提醒信息到数据库
     * @param medicationReminder 用户用药提醒
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeMedicationReminder(MedicationReminder medicationReminder, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
         repository.storeMedicationReminder(medicationReminder,successListener,failureListener);
    }

    /**
     * 通过用户的ID查询所有的用药提醒
     * @param userId 用户id
     * @return 所有的用药提醒
     */
    public LiveData<List<MedicationReminder>> getAllMedicationReminderByUserId(String userId){
        return repository.getAllMedicationReminderByUserId(userId);
    }
}
