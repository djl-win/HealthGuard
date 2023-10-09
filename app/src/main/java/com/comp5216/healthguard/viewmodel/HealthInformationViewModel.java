package com.comp5216.healthguard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.repository.HealthInformationRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * 健康信息视图模型类
 * <p>
 * 处理用户的健康信息
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-07
 */
public class HealthInformationViewModel extends ViewModel {
    // 用户健康信息仓库
    private final HealthInformationRepository repository;

    public HealthInformationViewModel() {
        this.repository = new HealthInformationRepository();
    }

    /**
     * 存储用户健康信息到数据库
     * @param healthInformation 用户健康信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeHealthInformation(HealthInformation healthInformation, List<User> friends, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        repository.storeHealthInformation(healthInformation, successListener, failureListener);
    }

    /**
     * 通过用户ID获取所有的健康信息数据
     * @return 所有的所有的健康信息数据
     */
    public LiveData<List<HealthInformation>> getInformationDataByUserId(String userId) {
        return repository.getInformationDataByUserId(userId);
    }
}
