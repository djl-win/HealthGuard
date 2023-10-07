package com.comp5216.healthguard.fragment.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.FriendsListAdapter;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.viewmodel.RelationShipViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * 聊天界面的fragment
 * <p>
 * 聊天页面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-27
 */
public class ChatFragment extends Fragment {

    // 定义一个rootView成员变量
    private View view;
    // 定义activity
    Activity activity;
    // 头像组件
    ImageView imageViewAvatar;
    // 用户的姓名
    TextView textViewUsername;
    // 添加好友的按钮
    ImageButton buttonAddFriends;
    // 添加好友的fragment
    AddFriendsFragment addFriendsFragment;
    // 好友列表
    RecyclerView recyclerViewFriends;
    // 用户的view model
    UserViewModel userViewModel;
    // 好友关系的view model
    RelationShipViewModel relationShipViewModel;
    // firebase的auth
    FirebaseAuth auth;
    // firebase user
    FirebaseUser firebaseUser;
    // 用户的uid
    String userUid;
    // 用户列表的适配器
    FriendsListAdapter friendsListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 实例化 ShopItemRepository，定义定义activity
        activity = getActivity();

        // 初始化获取xml中组件
        initViews();
        // 给页面组件设置监听器
        setListener();

        return view;
    }

    /**
     * 获取xml中组件
     */
    private void initViews() {
        // 绑定头像组件
        imageViewAvatar = view.findViewById(R.id.image_view_avatar_chat);
        // 绑定用户姓名组件
        textViewUsername = view.findViewById(R.id.text_view_username_chat);
        // 绑定添加好友的按钮
        buttonAddFriends = view.findViewById(R.id.button_add_friends_chat);
        // 创建Add Friends Fragment实例
        addFriendsFragment = new AddFriendsFragment();
        // 绑定好友列表的recycle view
        recyclerViewFriends = view.findViewById(R.id.recycler_view_friends_chat);
        // 初始化用户的view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // 初始化好友关系的view model
        relationShipViewModel = new ViewModelProvider(requireActivity()).get(RelationShipViewModel.class);
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();
        // 绑定用户列表的适配器
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 初始化用户信息，比如头像，name等
        loadUserInformation();
        // 添加好友按钮今天器
        ButtonAddFriendsListener();
        // 监听好友列表的信息，实时更新recycle view
        observeFriendListData();
    }

    /**
     * 初始化用户信息，比如头像，name等
     */
    private void loadUserInformation() {
        // 观察LiveData中的数据变化，并相应地更新UI
        userViewModel.getUserByUserId(userUid).observe(getViewLifecycleOwner(), user -> {
            // 如果从数据库中获取的用户数据不为空
            if (user != null) {

                // 通过用户的name给每个用户设置不同的头像
                String avatarUrl = "https://api.dicebear.com/6.x/fun-emoji/png?seed=" + user.getUserName();
                // 加载用户头像到xml
                Glide.with(this)
                        .load(avatarUrl)
                        .transform(new RoundedCorners(20))
                        .error(R.drawable.load_image)
                        .into(imageViewAvatar);
                // 加载用户姓名到xml进行显示
                textViewUsername.setText(user.getUserName());
            }
        });
    }

    /**
     * 添加好友的监听器
     */
    private void ButtonAddFriendsListener() {
        buttonAddFriends.setOnClickListener(view -> {
            // 禁用按钮，防止用户重复点击，导致程序崩溃
            buttonAddFriends.setEnabled(false);
            // 加载注册的fragment
            addFriendsFragment.show(getParentFragmentManager(), "AddFriendsFragment");
        });
    }

    /**
     * 监听当前用户的所有好友信息，并加载和更新适配器
     */
    private void observeFriendListData() {
        // 从ViewModel获取所有与当前用户ID关联的好友数据，并注册LiveData的观察者
        relationShipViewModel.getUserWithMessagesData(userUid).observe(getViewLifecycleOwner(), usersWithMessage -> {
            // 如果从数据库中获取的用户数据不为空
            if (usersWithMessage != null) {

                // 如果列表的适配器尚未初始化
                if (friendsListAdapter == null) {
                    // 初始化适配器，并设置它为recyclerView的适配器
                    friendsListAdapter = new FriendsListAdapter(getContext(), usersWithMessage);
                    recyclerViewFriends.setAdapter(friendsListAdapter);
                    // 为recycleView的每个item设置带点击监听器
                    friendsListAdapter.setItemClickListener(new FriendsListAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(User user) {
                            showMessageFragment(user);
                        }
                    });
                } else {
                    // 如果适配器已经初始化，只需更新数据并刷新列表
                    friendsListAdapter.updateData(usersWithMessage);
                }
            }
        });


    }


    /**
     * 出现用户聊天的Dialog
     */
    private void showMessageFragment(User user) {
        // 存储与当前用户进行对话的用户的信息，到view model
        MessageFragment messageFragment = new MessageFragment();
        userViewModel.setChatFriend(user);
        messageFragment.setChatId(user.getChatId());
        messageFragment.setSenderName(user.getUserName());
        messageFragment.setReceiverName(textViewUsername.getText().toString());
        // 加载注册的fragment
        messageFragment.show(getParentFragmentManager(), "MessageFragment");
    }


}