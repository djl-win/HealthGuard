package com.comp5216.healthguard.repository;

import android.util.Log;

import com.comp5216.healthguard.Exception.EncryptionException;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * 用户仓库类，处理数据库查询语句
 * <p>
 * 提供了对用户的增删改查的操作
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class UserRepository {

    // firebase数据库实例
    private final FirebaseFirestore db;

    /**
     * UserRepository的构造方法。
     */
    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * 存储用户信息到数据库
     * @param user 用户信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeUser(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        // SHA256加密用户密码
        user.setUserPassword(CustomEncryptUtil.encryptBySHA256(user.getUserPassword()));
        // AES128加密邮箱，名字和性别
        try {
            // 加密过程
            user.setUserEmail(CustomEncryptUtil.encryptByAES(user.getUserEmail()));
            user.setUserName(CustomEncryptUtil.encryptByAES(user.getUserName()));
            user.setUserGender(CustomEncryptUtil.encryptByAES(user.getUserGender()));
            Log.d("A","解密的邮箱: "+CustomEncryptUtil.decryptByAES(user.getUserEmail()));
            Log.d("A","解密的姓名: "+CustomEncryptUtil.decryptByAES(user.getUserName()));
            Log.d("A","解密的性别: "+CustomEncryptUtil.decryptByAES(user.getUserGender()));

        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }
        DocumentReference userRef = db.collection("users").document(user.getUserId());
        userRef.set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

}

