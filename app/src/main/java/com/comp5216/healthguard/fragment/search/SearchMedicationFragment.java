package com.comp5216.healthguard.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.RemindersListAdapter;
import com.comp5216.healthguard.adapter.SearchReportAdapter;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.viewmodel.MedicationReminderViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * search界面的medication fragment
 * <p>
 * search界面的medication fragment页面的逻辑
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchMedicationFragment extends Fragment {
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

    RecyclerView recyclerView;

    RemindersListAdapter remindersListAdapter;

    MedicationReminderViewModel medicationReminderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_search_medication, container, false);

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

        recyclerView = view.findViewById(R.id.search_medication_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 绑定用户用药提醒的view model
        medicationReminderViewModel = new ViewModelProvider(requireActivity()).get(MedicationReminderViewModel.class);
    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 观察用户recycle view的数据
        medicationReminderRecycleView();
    }

    /**
     * 观察用户recycle view的数据
     */
    private void medicationReminderRecycleView() {
        // 观察LiveData中的数据变化，并相应地更新UI
        medicationReminderViewModel.getAllMedicationReminderByUserId(userUid).observe(getViewLifecycleOwner(), medicationReminderList -> {
            // 如果从数据库中获取的用户数据不为空
            if (medicationReminderList != null) {
                // 如果列表的适配器尚未初始化
                if (remindersListAdapter == null) {
                    // 初始化适配器，并设置它为recyclerView的适配器
                    remindersListAdapter = new RemindersListAdapter(getContext(), medicationReminderList);
                    recyclerView.setAdapter(remindersListAdapter);

                    // 为recycleView的每个item设置带点击监听器
                    remindersListAdapter.setItemClickListener(new RemindersListAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(MedicationReminder medicationReminder) {
                            // 展示每个卡片的内容
                            showEachMedicationReminderDetails(medicationReminder);
                        }
                    });
                } else {
                    // 如果适配器已经初始化，只需更新数据并刷新列表
                    remindersListAdapter.updateData(medicationReminderList);
                }

            }
        });
    }

    /**
     * 展示一个新的Dialog。用于展示药品详情
     * @param medicationReminder 药品信息
     */
    private void showEachMedicationReminderDetails(MedicationReminder medicationReminder) {

        // 存储与当前用户进行对话的用户的信息，到view model
        SearchMedicationDialog medicationDialog = new SearchMedicationDialog();
        medicationReminderViewModel.setMedicationReminder(medicationReminder);

        // 加载注册的fragment
        medicationDialog.show(getParentFragmentManager(), "MedicationDialog");
    }

}

