package com.comp5216.healthguard.viewmodel;

import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.obj.entity.User;
import com.comp5216.healthguard.repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * 用户视图模型类，处理用户信息，返回到view层
 * <p>
 * 处理用户信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class UserViewModel extends ViewModel {

    // 用户信息仓库
    private final UserRepository repository;

    /**
     * UserViewModel 的构造方法
     */
    public UserViewModel() {
        this.repository = new UserRepository();
    }

    /**
     * 存储用户信息到数据库
     * @param user 用户信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeUser(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        repository.storeUser(user, successListener, failureListener);
    }

}

