package com.comp5216.healthguard.viewmodel;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.MedicalReport;
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

    // 用户的用药提醒数据
    private final MutableLiveData<MedicationReminder> medicationReminderMutableLiveData;

    public MedicationReminderViewModel() {
        this.repository = new MedicationReminderRepository();
        this.medicationReminderMutableLiveData = new MutableLiveData<>();
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


    /**
     * 设置用户的用要提醒数据
     */
    public void setMedicationReminder(MedicationReminder medicationReminder) {
        medicationReminderMutableLiveData.setValue(medicationReminder);
    }


    /**
     * 获得用户的用要提醒数据
     * @return 用户的用药提醒数据
     */
    public LiveData<MedicationReminder> getMedicationReminder() {
        return medicationReminderMutableLiveData;
    }
}
