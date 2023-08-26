package com.comp5216.healthguard.repository;

import android.util.Log;

import com.comp5216.healthguard.obj.entity.Attribute;
import com.comp5216.healthguard.obj.entity.User;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * 用户属性，预警信息仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户预警信息的增删改查的操作
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class AttributeRepository {

    // firebase数据库实例
    private final FirebaseFirestore db;

    /**
     * AttributeRepository的构造方法。
     */
    public AttributeRepository() {
        this.db =  FirebaseFirestore.getInstance();
    }

    /**
     * 通过用户的信息，来生成默认的预警信息存储到数据库
     * @param user 用户的详细信息
     */
    public void storeAttribute(User user){
        Attribute attribute = new Attribute();
        // 存储用户预警信息到数据库
        if("Male".equals(user.getUserGender()) || "Other".equals(user.getUserGender())) {
            // 生成唯一Id到数据库
            attribute.setAttributeId(CustomIdGeneratorUtil.generateUniqueId());
            attribute.setUserId(user.getUserId()); // 假设user对象有这个字段和方法

            // 假设的男性值
            attribute.setAttributeSystolicLow("90");
            attribute.setAttributeSystolicHigh("140");
            attribute.setAttributeDiastolicLow("60");
            attribute.setAttributeDiastolicHigh("90");
            attribute.setAttributeHeartRateLow("60");
            attribute.setAttributeHeartRateHigh("100");
            attribute.setAttributeBodyTemperatureLow("36.1");
            attribute.setAttributeBodyTemperatureHigh("37.2");
            attribute.setAttributeBloodOxygenLow("95");
            attribute.setAttributeBloodOxygenHigh("100");

        } else if("Female".equals(user.getUserGender())) {
            // 生成唯一Id到数据库
            attribute.setAttributeId(CustomIdGeneratorUtil.generateUniqueId());
            attribute.setUserId(user.getUserId());

            // 假定的女性值
            attribute.setAttributeSystolicLow("85");
            attribute.setAttributeSystolicHigh("135");
            attribute.setAttributeDiastolicLow("55");
            attribute.setAttributeDiastolicHigh("85");
            attribute.setAttributeHeartRateLow("65");
            attribute.setAttributeHeartRateHigh("95");
            attribute.setAttributeBodyTemperatureLow("36.1");
            attribute.setAttributeBodyTemperatureHigh("37.2");
            attribute.setAttributeBloodOxygenLow("95");
            attribute.setAttributeBloodOxygenHigh("100");
        }
        Log.d("dongjiale",attribute.getAttributeId());
        DocumentReference attributeRef = db.collection("attribute").document(attribute.getAttributeId());
        attributeRef.set(attribute);
    }

}
