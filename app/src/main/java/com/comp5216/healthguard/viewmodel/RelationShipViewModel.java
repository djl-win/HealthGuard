package com.comp5216.healthguard.viewmodel;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.entity.UserWithMessage;
import com.comp5216.healthguard.repository.RelationshipRepository;

import java.util.List;

/**
 * 用户间的好友关系的视图模型，返回到view层
 * <p>
 * 处理间的好友关系逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-29
 */
public class RelationShipViewModel extends ViewModel {
    // 好友关系信息仓库
    private final RelationshipRepository repository;

    /**
     * RelationShipViewModel 的构造方法
     */
    public RelationShipViewModel() {
        this.repository = new RelationshipRepository();
    }

    /**
     * 存储好友关系信息到数据库
     * @param relationship 用户信息
     * @param callback 成功与否
     */
    public void storeRelationShip(Relationship relationship, Consumer<Boolean> callback) {
        repository.storeRelationShip(relationship,callback);
    }


    /**
     * 通过Id查询该用户所有的好友
     * @param userId 当前用户id
     * @return 所有的好友信息,以及未读条数和最新信息
     */
    public LiveData<List<UserWithMessage>> getUserWithMessagesData(String userId) {
        return repository.getUserWithMessagesData(userId);
    }




}
