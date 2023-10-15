package com.comp5216.healthguard.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.entity.Attribute;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
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

    // 用户属性livedata
    private final MutableLiveData<Attribute> attributeMutableLiveData;

    /**
     * AttributeRepository的构造方法。
     */
    public AttributeRepository() {
        this.db = FirebaseFirestore.getInstance();
        attributeMutableLiveData = new MutableLiveData<>();
    }

    /**
     * 通过用户的信息，来生成默认的预警信息存储到数据库
     *
     * @param user 用户的详细信息
     */
    public void storeAttribute(User user) {

        // 创建一个新的属性对象
        Attribute attribute = new Attribute();

        // 判断用户的性别，并为其设置对应的健康属性阈值
        if ("Male".equals(user.getUserGender()) || "Other".equals(user.getUserGender())) {
            // 为属性对象生成一个唯一的ID
            attribute.setAttributeId(CustomIdGeneratorUtil.generateUniqueId());
            // 设置属性对象的用户ID
            attribute.setUserId(user.getUserId()); // 假设user对象有这个字段和方法

            // 设置男性的血压、心率、体温和血氧的预设范围值
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

        } else if ("Female".equals(user.getUserGender())) {
            // 为属性对象生成一个唯一的ID
            attribute.setAttributeId(CustomIdGeneratorUtil.generateUniqueId());
            attribute.setUserId(user.getUserId());

            // 设置女性的血压、心率、体温和血氧的预设范围值
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

        // 将属性对象保存到数据库的"attribute"集合中
        db.collection("attribute")
                .document(attribute.getAttributeId())
                .set(attribute);
    }

    /**
     * 通过用户id获取用户属性
     * @param userId 用户id
     */
    public void getAttributeByUserId(String userId, OnSuccessListener<Attribute> successListener, OnFailureListener failureListener) {
        // 查询集合根据userId
        db.collection("attribute")
                .whereEqualTo("userId", userId)
                .limit(1)  // Limit the results to 1 document, since userId should be unique
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Attribute attribute = queryDocumentSnapshots.getDocuments().get(0).toObject(Attribute.class);
                        successListener.onSuccess(attribute);
                    } else {
                        failureListener.onFailure(new Exception("No document found"));
                    }
                })
                .addOnFailureListener(failureListener);
    }

    /**
     * 通过用户Id获取用户的属性值
     * @param userId 用户Id
     */
    public LiveData<Attribute> getAttributeById(String userId){
        db.collection("attribute")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("djl", "listen:error");
                        return;
                    }
                    Attribute attribute = new Attribute();
                    for (DocumentSnapshot doc : snapshots) {
                       attribute = doc.toObject(Attribute.class);
                    }
                    attributeMutableLiveData.setValue(attribute);
                });
        return attributeMutableLiveData;
    }
}
