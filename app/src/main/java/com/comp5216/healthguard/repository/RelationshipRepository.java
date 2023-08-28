package com.comp5216.healthguard.repository;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * 用户好友之间关系类，处理数据库查询语句
 * <p>
 * 提供了对用户的增删改查的操作
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-29
 */
public class RelationshipRepository {
    // firebase数据库实例
    private final FirebaseFirestore db;

    /**
     * RelationShipRepository的构造方法。
     */
    public RelationshipRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * 存储好友关系信息到数据库
     * @param relationship 用户信息
     * @param callback 成功与否
     */
    public void storeRelationShip(Relationship relationship, Consumer<Boolean> callback) {
        // 双向添加好友
        Relationship relationship2 = new Relationship();
        relationship.setRelationshipId(CustomIdGeneratorUtil.generateUniqueId());
        relationship2.setRelationshipId(CustomIdGeneratorUtil.generateUniqueId());

        relationship2.setRelationshipObserveId(relationship.getRelationshipObservedId());
        relationship2.setRelationshipObservedId(relationship.getRelationshipObserveId());

        // 获取Fire store集合的引用
        CollectionReference relationshipRef = db.collection("relationship");

        // 查询是否已有相应的关系记录
        relationshipRef.whereEqualTo("relationshipObserveId", relationship.getRelationshipObserveId())
                .whereEqualTo("relationshipObservedId", relationship.getRelationshipObservedId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().isEmpty()) {
                            // 如果没有预先存在的关系记录，进行存储
                            relationshipRef.document(relationship.getRelationshipId())
                                    .set(relationship)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 存储第二条关系
                                            relationshipRef.document(relationship2.getRelationshipId())
                                                    .set(relationship2)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            callback.accept(true);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            callback.accept(false);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            callback.accept(false);
                                        }
                                    });
                        } else {
                            // 如果已存在关系记录，不进行存储并直接回调
                            callback.accept(false);
                        }
                    }
                });

    }

}
