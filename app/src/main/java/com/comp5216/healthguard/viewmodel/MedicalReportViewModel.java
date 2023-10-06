package com.comp5216.healthguard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.repository.HealthInformationRepository;
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

    public MedicalReportViewModel() {
        this.repository = new MedicalReportRepository();
    }

    /**
     * 存储用户医疗报告信息到数据库
     * @param medicalReport 用户医疗报告信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeMedicalReport(MedicalReport medicalReport, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        repository.storeMedicalReport(medicalReport, successListener, failureListener);
    }

    /**
     * 通过用户ID获取所有的医疗报告数据
     * @return 所有的所有的医疗报告数据
     */
    public LiveData<List<MedicalReport>> getReportDataByUserId(String userId) {
        return repository.getReportDataByUserId(userId);
    }
}
