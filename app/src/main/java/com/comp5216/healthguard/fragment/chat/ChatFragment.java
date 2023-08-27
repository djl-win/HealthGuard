package com.comp5216.healthguard.fragment.chat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.comp5216.healthguard.R;
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
    // 用户的view model
    UserViewModel viewModel;
    // firebase的auth
    FirebaseAuth auth;
    // firebase user
    FirebaseUser firebaseUser;
    // 用户的uid
    String userUid;

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
     *
     */
    private void initViews() {
        // 绑定头像组件
        imageViewAvatar = view.findViewById(R.id.image_view_avatar_chat);
        // 绑定用户姓名组件
        textViewUsername = view.findViewById(R.id.text_view_username_chat);
        // 初始化用户的view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        assert firebaseUser != null;
        userUid = firebaseUser.getUid();

    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 初始化用户信息，比如头像，name等
        loadUserInformation();
    }

    /**
     * 初始化用户信息，比如头像，name等
     */
    private void loadUserInformation() {
        // 观察LiveData中的数据变化，并相应地更新UI
        viewModel.getUser(userUid).observe(getViewLifecycleOwner(), user -> {
            // 通过用户的name给每个用户设置不同的头像
            String avatarUrl = "https://api.dicebear.com/6.x/bottts-neutral/png?seed=" + user.getUserName();
            // 加载用户头像到xml
            Glide.with(this)
                    .load(avatarUrl)
                    .transform(new RoundedCorners(20))
                    .error(R.drawable.load_image)
                    .into(imageViewAvatar);
            // 加载用户姓名到xml进行显示
            textViewUsername.setText(user.getUserName());

        });
    }
}