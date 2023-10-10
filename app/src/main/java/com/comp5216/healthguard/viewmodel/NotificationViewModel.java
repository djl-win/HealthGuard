package com.comp5216.healthguard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.Notification;
import com.comp5216.healthguard.repository.NotificationRepository;

import java.util.List;

/**
 * 通知视图模型类
 * <p>
 * 处理通知视图模型类
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-07
 */
public class NotificationViewModel extends ViewModel {
    // 用户通知信息仓库
    private final NotificationRepository repository;


    public NotificationViewModel() {
        this.repository = new NotificationRepository();
    }

    /**
     * 通过用户id查询所有相关通知
     *
     * @param userId 用户Id
     * @return 所有的相关通知
     */
    public LiveData<List<Notification>> getAllNotificationByUserId(String userId) {
            return repository.getAllNotificationByUserId(userId);
    }

}
