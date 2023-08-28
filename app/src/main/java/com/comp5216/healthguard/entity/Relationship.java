package com.comp5216.healthguard.entity;

/**
 * 好友关系表
 * <p>
 * 存储用户间的好友关系
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-29
 */
public class Relationship {
    private String relationshipId;  // 用String类型来存28位唯一标识符
    private String relationshipObserveId; // 观察者的用户ID
    private String relationshipObservedId; // 被观察者的用户ID

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getRelationshipObserveId() {
        return relationshipObserveId;
    }

    public void setRelationshipObserveId(String relationshipObserveId) {
        this.relationshipObserveId = relationshipObserveId;
    }

    public String getRelationshipObservedId() {
        return relationshipObservedId;
    }

    public void setRelationshipObservedId(String relationshipObservedId) {
        this.relationshipObservedId = relationshipObservedId;
    }
}
