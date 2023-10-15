package com.comp5216.healthguard.fragment.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.activity.EnterActivity;
import com.comp5216.healthguard.scheduler.AlarmScheduler;
import com.comp5216.healthguard.util.CustomCache;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;


/**
 * 用户个人信息处理页面
 * <p>
 * 处理用户信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-11
 */
public class SettingFragment extends Fragment {

    // 定义一个rootView成员变量
    private View view;

    // 定义activity
    Activity activity;

    // firebase的auth
    FirebaseAuth auth;
    // firebase user
    FirebaseUser firebaseUser;
    // 用户的uid
    String userUid;

    // 头像imageview
    ImageView imageViewAvatar;

    // name textview
    TextView textViewName;

    // id textview
    TextView textViewId;

    // 用户的view model
    UserViewModel userViewModel;

    MaterialCardView cardViewAttribute;

    ChangeAttributeFragment changeAttributeFragment;







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 实例化 ShopItemRepository，定义定义activity
        activity = getActivity();

        // 初始化获取xml中组件
        initViews();

        // 设置监听器
        setListeners();

        return view;
    }

    /**
     * 获取xml中组件
     */
    private void initViews() {
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();

        imageViewAvatar = view.findViewById(R.id.setting_avatar);
        textViewName = view.findViewById(R.id.setting_name);
        textViewId = view.findViewById(R.id.setting_id);

        cardViewAttribute = view.findViewById(R.id.setting_attribute);


        // 初始化用户的view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        ImageButton button = view.findViewById(R.id.setting_logout);
        button.setOnClickListener( view ->{
            auth.signOut();

            CustomCache customCache = new CustomCache(getContext()); // 'this' 是 Context
            // 清除消息提醒
            List<String> strings = customCache.retrieveAllReminderIds();
            for (String string : strings) {
                AlarmScheduler.cancelReminder(getContext(),string);
                Log.d("djl", "取消的提醒ID为: " + string);
            }
            // 清除缓存
            customCache.removeUserFCM();
            customCache.clearAllReminderIds();

            // 跳转回登录页面
            startActivity(new Intent(activity, EnterActivity.class));
            activity.finish();
        });
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        setAvatarAndUserInfo();
        listenChangeAttribute();
    }

    /**
     * 监控用户修改个人属性
     */
    private void listenChangeAttribute() {
        cardViewAttribute.setOnClickListener(view1 -> {
            changeAttributeFragment = new ChangeAttributeFragment();
            // 加载修改属性的fragment
            changeAttributeFragment.show(getParentFragmentManager(), "ChangeAttributeFragment");
        });
    }

    /**
     * 获取用户头像和用户信息
     */
    @SuppressLint("SetTextI18n")
    private void setAvatarAndUserInfo() {
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
                textViewName.setText(user.getUserName());
                textViewId.setText("USER ID: " + user.getUserId());
            }
        });
    }



}