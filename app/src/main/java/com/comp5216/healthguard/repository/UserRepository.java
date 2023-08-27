package com.comp5216.healthguard.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.exception.EncryptionException;
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

        // SHA256加密用户密码,AES128加密邮箱、名字和性别
        try {
            // SHA256加密用户密码
            user.setUserPassword(CustomEncryptUtil.encryptBySHA256(user.getUserPassword()));
            // AES128加密邮箱，名字和性别
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

    /**
     * 通过用户的ID获取用户的信息
     * @param userId user的UID
     * @return 解密之后的用户信息
     */
    public LiveData<User> getUser(String userId) {
        // 使用LiveData观察数据变化
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        // 创建文档引用
        DocumentReference userRef = db.collection("users").document(userId);
        // 获取文档
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // 将文档转换为User对象
                        User user = documentSnapshot.toObject(User.class);
                        // 通过AES解密用户信息
                        try {
                            assert user != null;
                            user.setUserId(userId);
                            user.setUserEmail(CustomEncryptUtil.decryptByAES(user.getUserEmail()));
                            user.setUserName(CustomEncryptUtil.decryptByAES(user.getUserName()));
                            user.setUserGender(CustomEncryptUtil.decryptByAES(user.getUserGender()));
                        }catch (NoSuchPaddingException | IllegalBlockSizeException |
                                NoSuchAlgorithmException | BadPaddingException |
                                InvalidKeyException e) {
                            // 抛出自定义异常
                            throw new EncryptionException(e);
                        }

                        // 更新LiveData的值
                        userLiveData.setValue(user);
                    } else {
                        userLiveData.setValue(null);  // 如果没有该文档，设置LiveData为null
                    }
                })
                .addOnFailureListener(e -> {
                    // 发生错误时，设置LiveData为null
                    userLiveData.setValue(null);
                });
        // 返回LiveData对象
        return userLiveData;
    }
}

