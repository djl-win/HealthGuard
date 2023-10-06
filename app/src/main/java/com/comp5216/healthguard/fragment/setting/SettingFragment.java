package com.comp5216.healthguard.fragment.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.activity.EnterActivity;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


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




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 实例化 ShopItemRepository，定义定义activity
        activity = getActivity();

        // 初始化获取xml中组件
        initViews();

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

        Button button = view.findViewById(R.id.setting_logout);
        button.setOnClickListener( view ->{
            auth.signOut();

            // 跳转回登录页面
            startActivity(new Intent(activity, EnterActivity.class));
            activity.finish();
        });
    }

}