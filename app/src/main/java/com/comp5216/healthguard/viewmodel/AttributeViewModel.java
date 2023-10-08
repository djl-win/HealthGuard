package com.comp5216.healthguard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.Attribute;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.repository.AttributeRepository;

/**
 * 属性视图模型类，处理用户的预警信息，返回到view层
 * <p>
 * 处理用户的预警信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class AttributeViewModel  extends ViewModel {

    // 用户预警信息仓库
    private final AttributeRepository repository;

    /**
     * UserViewModel 的构造方法
     */
    public AttributeViewModel() {
        this.repository = new AttributeRepository();
    }

    /**
     * 通过用户的信息，来生成默认的预警信息存储到数据库
     * @param user 用户的详细信息
     */
    public void storeAttribute(User user){
        repository.storeAttribute(user);
    }

    /**
     * 通过用户Id获取用户的属性值
     * @param userId 用户Id
     */
    public LiveData<Attribute> getAttributeById(String userId){
        return repository.getAttributeById(userId);
    }

}
