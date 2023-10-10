package com.comp5216.healthguard.fragment.notify;

import android.app.Activity;
import android.os.Bundle;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.NotificationListAdapter;
import com.comp5216.healthguard.viewmodel.NotificationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * notify界面的fragment
 * <p>
 * notify页面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-07
 */
public class NotifyFragment extends Fragment {
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

    // 通知列表
    RecyclerView recyclerViewBody;

    // 适配器
    NotificationListAdapter notificationListAdapter;

    // notification view model
    NotificationViewModel notificationViewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_notify, container, false);

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
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();
        // 初始化仓库
        notificationViewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);

        recyclerViewBody = view.findViewById(R.id.notify_body);
        // 绑定用户列表的适配器
        recyclerViewBody.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 监听通知列表的信息，实时更新recycle view
        observeNotificationListData();

    }

    /**
     * 监听通知列表的信息，实时更新recycle view
     */
    private void observeNotificationListData() {
        // 观察LiveData中的数据变化，并相应地更新UI
        notificationViewModel.getAllNotificationByUserId(userUid).observe(getViewLifecycleOwner(), notificationList -> {
            Log.d("djl", "observeNotificationListData: ");
            // 如果从数据库中获取的用户数据不为空
            if (notificationList != null) {
                    // 如果列表的适配器尚未初始化
                    if (notificationListAdapter == null) {
                        // 初始化适配器，并设置它为recyclerView的适配器
                        notificationListAdapter = new NotificationListAdapter(getContext(), notificationList);
                        recyclerViewBody.setAdapter(notificationListAdapter);
                    } else {
                        // 如果适配器已经初始化，只需更新数据并刷新列表
                        notificationListAdapter.updateData(notificationList);
                    }

            }
        });
    }
}