package com.comp5216.healthguard.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.repository.MedicalReportRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * 医疗报告视图模型类
 * <p>
 * 处理医疗报告视图模型类
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class MedicalReportViewModel  extends ViewModel {
    // 用户医疗报告仓库
    private final MedicalReportRepository repository;

    // 用户的报告数据
    private final MutableLiveData<MedicalReport> medicalReportMutableLiveData;


    public MedicalReportViewModel() {
        this.repository = new MedicalReportRepository();
        this.medicalReportMutableLiveData = new MutableLiveData<>();
    }

    /**
     * 存储用户医疗报告信息到数据库
     * @param medicalReport 用户医疗报告信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeMedicalReport(Context context, MedicalReport medicalReport, List<User> friends, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        repository.storeMedicalReport(context,medicalReport,friends, successListener, failureListener);
    }

    /**
     * 通过用户ID获取所有的医疗报告数据
     * @return 所有的所有的医疗报告数据
     */
    public LiveData<List<MedicalReport>> getReportDataByUserId(String userId) {
        return repository.getReportDataByUserId(userId);
    }

    /**
     * 存储用户的报告数据
     * @param medicalReport 用户的报告数据
     */
    public void setMedicalReport(MedicalReport medicalReport) {
        medicalReportMutableLiveData.setValue(medicalReport);
    }

    /**
     * 获得用户的报告数据
     * @return 用户的报告数据
     */
    public LiveData<MedicalReport> getMedicalReport() {
        return medicalReportMutableLiveData;
    }

}
