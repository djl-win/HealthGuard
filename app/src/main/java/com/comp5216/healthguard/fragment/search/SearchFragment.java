package com.comp5216.healthguard.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.viewmodel.NotificationViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * search界面的fragment
 * <p>
 * search页面的逻辑
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchFragment extends Fragment {
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

    // fragment manager
    FragmentManager childFragmentManager;

    // tab layout
    TabLayout tabLayoutChangePage;
    // health page
    SearchHealthFragment searchHealthFragment;

    // report page
    SearchReportFragment searchReportFragment;

    // medication page
    SearchMedicationFragment searchMedicationFragment;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_search, container, false);

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

        // 上部导航栏
        tabLayoutChangePage = view.findViewById(R.id.search_change_page);

        // 获取FragmentManager
        childFragmentManager = getChildFragmentManager();

        // 创建并加载子Fragment
        searchHealthFragment = new SearchHealthFragment();
        searchReportFragment = new SearchReportFragment();
        searchMedicationFragment = new SearchMedicationFragment();

        childFragmentManager.beginTransaction()
                .add(R.id.search_container, searchHealthFragment)
                .add(R.id.search_container,searchReportFragment)
                .add(R.id.search_container,searchMedicationFragment)
                .hide(searchReportFragment)
                .hide(searchMedicationFragment)
                .show(searchHealthFragment)
                .commit();
    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        changeChildFragment();
    }

    /**
     * 切换子fragment
     */
    private void changeChildFragment() {
        tabLayoutChangePage.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    // 显示健康页面
                    childFragmentManager.beginTransaction()
                            .hide(searchMedicationFragment)
                            .hide(searchReportFragment)
                            .show(searchHealthFragment)
                            .commit();
                } else if (tab.getPosition() == 1) {
                    // 显示报告页面
                    childFragmentManager.beginTransaction()
                            .hide(searchMedicationFragment)
                            .hide(searchHealthFragment)
                            .show(searchReportFragment)
                            .commit();
                } else if (tab.getPosition() == 2){
                    // 显示药物页面
                    childFragmentManager.beginTransaction()
                            .hide(searchHealthFragment)
                            .hide(searchReportFragment)
                            .show(searchMedicationFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}