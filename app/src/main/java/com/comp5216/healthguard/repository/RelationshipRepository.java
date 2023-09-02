package com.comp5216.healthguard.repository;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.entity.UserWithMessage;
import com.comp5216.healthguard.exception.EncryptionException;
import com.comp5216.healthguard.exception.QueryException;
import com.comp5216.healthguard.util.CustomEncryptUtil;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.Value;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    // firebase realtime database数据库实例
    private final FirebaseDatabase  db1;
    // 使用LiveData观察数据变化，通过当前用户的ID获取的所有好友的信息，展示在聊天页面的好友列表
    private final MutableLiveData<List<User>> friendsLiveData;
    // 使用LiveData观察数据变化，通过当前聊天室的ID获取最新消息和未读条数，展示在聊天页面的好友列表
    private final MutableLiveData<List<UserWithMessage>> friendsWithMessageLiveData;

    String currentUserId;

    /**
     * RelationShipRepository的构造方法。
     */
    public RelationshipRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.db1 =  FirebaseDatabase.getInstance();
        this.friendsLiveData = new MutableLiveData<>();
        this.friendsWithMessageLiveData = new MutableLiveData<>();
        this.currentUserId = FirebaseAuth.getInstance().getUid();
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
        relationship.setRelationshipChatId(CustomIdGeneratorUtil.generateUniqueId());
        relationship2.setRelationshipId(CustomIdGeneratorUtil.generateUniqueId());
        relationship2.setRelationshipChatId(relationship.getRelationshipChatId());

        relationship2.setRelationshipObserveId(relationship.getRelationshipObservedId());
        relationship2.setRelationshipObservedId(relationship.getRelationshipObserveId());

        // 在数据库中查询是否存在此关系
        db.collection("relationship")
                .whereEqualTo("relationshipObserveId", relationship.getRelationshipObserveId())
                .whereEqualTo("relationshipObservedId", relationship.getRelationshipObservedId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().isEmpty()) {
                        // 如果结果显示不存在此关系，则在数据库中添加此关系
                        db.collection("relationship")
                                .document(relationship.getRelationshipId())
                                .set(relationship)
                                .addOnSuccessListener(success -> {
                                    // 当第一条关系成功存储后，存储反向关系
                                    db.collection("relationship")
                                            .document(relationship2.getRelationshipId())
                                            .set(relationship2)
                                            .addOnSuccessListener(successReverse -> callback.accept(true))
                                            .addOnFailureListener(failureReverse -> callback.accept(false));
                                })
                                .addOnFailureListener(failure -> callback.accept(false));
                    } else {
                        // 如果关系已经存在，则直接回调false
                        callback.accept(false);
                    }
                });

    }

    /**
     * 查找当前用户的所有好友，并返回所有好友的信息，用livedata存储，因为这个值会变化，当用户添加新的好友之后
     * @param userId 当前登录的用户的Id
     * @return 通过当前用户的ID查找的所有好友的信息，展示在聊天页面的好友列表
     */
    public LiveData<List<User>> findAllFriendsByUserId(String userId) {
        // 用于存储用户的livedata
        db.collection("relationship")
                .whereEqualTo("relationshipObserveId", userId)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null || documentSnapshots == null) {
                        // Handle errors
                        throw new QueryException();
                    }
                    List<User> friends = new ArrayList<>();
                    for (QueryDocumentSnapshot document : documentSnapshots) {
                        Relationship relationship = document.toObject(Relationship.class);
                        db.collection("users")
                                .document(relationship.getRelationshipObservedId())
                                .addSnapshotListener((documentSnapshot, e1) -> {
                                    if (e1 != null || documentSnapshot == null) {
                                        // Handle errors
                                        throw new QueryException();
                                    }
                                    User user = documentSnapshot.toObject(User.class);
                                    try {
                                        // 使用AES解密用户的姓名、邮箱和性别
                                        user.setUserName(CustomEncryptUtil.decryptByAES(user.getUserName()));
                                        user.setUserEmail(CustomEncryptUtil.decryptByAES(user.getUserEmail()));
                                        user.setUserGender(CustomEncryptUtil.decryptByAES(user.getUserGender()));
                                        // 通过relationship表搜出来的好友，此时将搜出来的relationship中的chatID封装到user的chatId属性上并返回
                                        user.setChatId(relationship.getRelationshipChatId());
                                    } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                                             InvalidKeyException | BadPaddingException |
                                             IllegalBlockSizeException ex) {
                                        // 抛出加密异常
                                        throw new EncryptionException(ex);
                                    }

                                    friends.add(user);
                                    if (friends.size() == documentSnapshots.size()) {
                                        friendsLiveData.setValue(friends);
                                    }
                                });
                    }
                });
        return friendsLiveData;
    }

    /**
     * 通过用户获取最新的聊天信息和未读条数
     * @param friend 好友列表的条数
     * @param callback 用于监测数据的变化，把未读消息数量和最新消息加到回调
     */
    private void fetchLatestMessageAndUnreadNumber(User friend, Consumer<UserWithMessage> callback) {
        // 获取未读消息的数量
        DatabaseReference mDatabaseReference = db1.getReference("chats").child(friend.getChatId());
        mDatabaseReference
                .orderByChild("chatMessageReadStatus")
                .equalTo("0")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long unreadCount = 0;

                        // 手动过滤出那些不是由当前用户发送的消息
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chat chat = snapshot.getValue(Chat.class);
                            if (!chat.getChatMessageSenderID().equals(currentUserId)) {
                                unreadCount++;
                            }
                        }

                        // 获取最新的消息
                        long finalUnreadCount = unreadCount;
                        mDatabaseReference
                                .orderByChild("chatMessageTimestamp")
                                .limitToLast(1)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String latestMessageText = "";
                                        String latestMessageTimeStamp = "";
                                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                            Chat message = messageSnapshot.getValue(Chat.class);
                                            try {
                                                latestMessageText = CustomEncryptUtil.decryptByAES(message.getChatMessageText());
                                                latestMessageTimeStamp = message.getChatMessageTimestamp();
                                            } catch (NoSuchPaddingException |
                                                     NoSuchAlgorithmException |
                                                     InvalidKeyException |
                                                     BadPaddingException |
                                                     IllegalBlockSizeException ex) {
                                                // 抛出加密异常
                                                throw new EncryptionException(ex);
                                            }
                                        }

                                        UserWithMessage userWithMsg = new UserWithMessage();
                                        userWithMsg.setUser(friend);
                                        userWithMsg.setLastMessage(latestMessageText);
                                        userWithMsg.setLastMessageTimeStamp(latestMessageTimeStamp);
                                        userWithMsg.setUnreadMessageNumber(String.valueOf(finalUnreadCount));
                                        callback.accept(userWithMsg);  // 使用回调方法返回结果
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                        throw new QueryException();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                        throw new QueryException();
                    }
                });

    }

    /**
     * 通过Id查询该用户所有的好友,合并两个消息观察者
     * @param userId 当前用户id
     * @return 所有的好友信息,以及未读条数和最新信息
     */
    public LiveData<List<UserWithMessage>> getUserWithMessagesData(String userId) {
        return Transformations.switchMap(findAllFriendsByUserId(userId), friendsList -> {

            // 使用一个Map来存储数据，键为UserId，值为UserWithMessage
            Map<String, UserWithMessage> userWithMessageMap = new HashMap<>();

            if (friendsList.isEmpty()) {
                friendsWithMessageLiveData.setValue(new ArrayList<>());
                return friendsWithMessageLiveData;
            }

            for (User friend : friendsList) {
                fetchLatestMessageAndUnreadNumber(friend, userWithMsg -> {
                    // 直接更新Map中的数据
                    userWithMessageMap.put(userWithMsg.getUser().getUserId(), userWithMsg);

                    if (userWithMessageMap.size() == friendsList.size()) {
                        // 根据消息时间排序列表
                        List<UserWithMessage> sortedList = userWithMessageMap.values().stream()
                                .sorted(Comparator.comparing(UserWithMessage::getLastMessageTimeStamp).reversed())
                                .collect(Collectors.toList());
                        friendsWithMessageLiveData.setValue(sortedList);
                    }
                });
            }

            return friendsWithMessageLiveData;
        });
    }



    /**
     * addSnapshotListener:
     * 这是一个实时的数据监听器，它会在每次数据发生变化时被触发。对于那些需要实时更新 UI 或执行其他操作的场景，这是一个很有用的工具。
     * 它可以监听单个文档或整个集合的变化。
     * addOnSuccessListener:
     * 这个监听器在操作成功完成时被触发。例如，当您成功读取数据、写入数据、删除数据等操作时，它会返回成功的结果。
     * 它提供了一个回调，这个回调包含操作的结果（例如，读取操作会返回读取的数据）。
     * addOnFailureListener:
     * 当操作失败时触发此监听器。
     * 它提供了一个回调，这个回调包含导致失败的异常，从而您可以知道是什么原因导致的失败，例如网络问题、权限问题等。
     * addOnCompleteListener
     * 无论成功还是失败，当操作完成时都会触发此监听器。
     * 它提供了一个回调，这个回调包含了操作的结果，您可以检查操作是否成功，并据此获取相应的数据或错误。
     */

}
