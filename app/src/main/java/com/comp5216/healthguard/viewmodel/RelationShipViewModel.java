package com.comp5216.healthguard.viewmodel;

import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.repository.RelationshipRepository;

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




}
