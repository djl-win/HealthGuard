package com.comp5216.healthguard.viewmodel;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.exception.QueryException;
import com.comp5216.healthguard.repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * 用户视图模型类，处理用户信息，返回到view层
 * <p>
 * 处理用户信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class UserViewModel extends ViewModel {

    // 用户信息仓库
    private final UserRepository repository;
    // 用户点击好友列表，之后打开的聊天框中的，与其对话的好友
    private final MutableLiveData<User> chatFriendLiveData;


    /**
     * UserViewModel 的构造方法
     */
    public UserViewModel() {
        this.repository = new UserRepository();
        this.chatFriendLiveData = new MutableLiveData<>();
    }

    /**
     * 存储用户信息到数据库
     * @param user 用户信息
     * @param successListener 成功监听器
     * @param failureListener 失败监听器
     */
    public void storeUser(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        repository.storeUser(user, successListener, failureListener);
    }

    /**
     * 通过用户UID获取用户的所有信息
     * @param userId 用户UID
     * @return 用户信息的Live Data
     */
    public LiveData<User> getUserByUserId(String userId) {
        // 返回LiveData对象
        return repository.getUserByUserId(userId);
    }

    /**
     * 通过用户输入的验证码（需要添加的用户id），来判断用户输入的id是否与此邮箱对应的id一致
     * @param email 用户加密前的id
     * @param verification 用户输入的验证码（id）
     * @param callback 正确与否
     */
    public void verifyUserByEmail(String email,String verification, Consumer<Boolean> callback) {
        repository.verifyUserByEmail(email, verification,callback);
    }

    /**
     * 存储与当前用户进行对话的用户的信息，到view model
     * @param user chatFriend
     */
    public void setChatFriend(User user) {
        chatFriendLiveData.setValue(user);
    }

    public LiveData<User> getChatFriendLiveData() {
        return chatFriendLiveData;
    }

}

