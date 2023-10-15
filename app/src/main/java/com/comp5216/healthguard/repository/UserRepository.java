package com.comp5216.healthguard.repository;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    // 使用LiveData观察数据变化,通过用户的ID获取用户的信息,展示在聊天页面的上方
    private final MutableLiveData<User> userLiveData;

    /**
     * UserRepository的构造方法。
     */
    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
    }

    /**
     * 存储用户信息到数据库
     *
     * @param user            用户信息
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



        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }

        db.collection("users").document(user.getUserId())
                .set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    /**
     * 通过用户的ID获取用户的信息
     *
     * @param userId user的UID
     * @return 解密之后的用户信息，展示在聊天页面的上方
     */
    public LiveData<User> getUserByUserId(String userId) {

        // 获取文档
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(success -> {
                    // 将文档转换为User对象
                    User user = success.toObject(User.class);
                    // 通过AES解密用户信息
                    try {
                        user.setUserEmail(CustomEncryptUtil.decryptByAES(user.getUserEmail()));
                        user.setUserName(CustomEncryptUtil.decryptByAES(user.getUserName()));
                        user.setUserGender(CustomEncryptUtil.decryptByAES(user.getUserGender()));
                    } catch (NoSuchPaddingException | IllegalBlockSizeException |
                             NoSuchAlgorithmException | BadPaddingException |
                             InvalidKeyException e) {
                        // 抛出自定义异常
                        throw new EncryptionException(e);
                    }

                    // 更新LiveData的值
                    userLiveData.setValue(user);
                })
                .addOnFailureListener(failure -> {
                    // 发生错误时，设置LiveData为null
                    userLiveData.setValue(null);
                });
        // 返回LiveData对象
        return userLiveData;
    }

    /**
     * 通过邮箱获取用户信息，并判断用户输入的验证码（id），与这个查询出的id一不一致
     *
     * @param email        用户加密前的email
     * @param verification 用户输入的验证码（id）
     * @param callback     正确与否
     */
    public void verifyUserByEmail(String email, String verification, Consumer<Boolean> callback) {
        try {
            email = CustomEncryptUtil.encryptByAES(email);
        } catch (NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            // 抛出自定义异常
            throw new EncryptionException(e);
        }


        db.collection("users")
                .whereEqualTo("userEmail", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            if (userId.equals(verification)) {
                                callback.accept(true);
                                return;
                            }
                        }
                        callback.accept(false);
                    } else {
                        callback.accept(false);
                    }
                });
    }


}

