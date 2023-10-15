package com.comp5216.healthguard.fragment.search;

import android.app.Activity;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.SearchReportAdapter;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * search界面的report fragment
 * <p>
 * search界面的report fragment页面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchReportFragment extends Fragment {
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

    // 报告视图模型
    MedicalReportViewModel medicalReportViewModel;

    // 适配器
    SearchReportAdapter searchReportAdapter;

    // recycle view
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_search_report, container, false);

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

        medicalReportViewModel = new ViewModelProvider(requireActivity()).get(MedicalReportViewModel.class);

        recyclerView = view.findViewById(R.id.search_report_recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 监听所有报告数据
        observeReports();
    }

    /**
     * 监听所有报告数据
     */
    private void observeReports() {
        // 观察LiveData中的数据变化，并相应地更新UI
        medicalReportViewModel.getReportDataByUserId(userUid)
                .observe(getViewLifecycleOwner(), reports -> {
                    if (reports != null) {
                        // 如果列表的适配器尚未初始化
                        if (searchReportAdapter == null) {
                            // 初始化适配器，并设置它为recyclerView的适配器
                            reports.sort((o1, o2) -> Long.compare(o2.getMedicalReportDate(), o1.getMedicalReportDate()));
                            searchReportAdapter = new SearchReportAdapter(getContext(), reports);
                            recyclerView.setAdapter(searchReportAdapter);
                            // 为recycleView的每个item设置带点击监听器
                            searchReportAdapter.setItemClickListener(new SearchReportAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(MedicalReport medicalReport) {
                                    // 展示每个卡片的内容
                                    showEachMedicalReportDetails(medicalReport);
                                }
                            });
                        } else {
                            // 初始化适配器，并设置它为recyclerView的适配器
                            reports.sort((o1, o2) -> Long.compare(o2.getMedicalReportDate(), o1.getMedicalReportDate()));
                            // 如果适配器已经初始化，只需更新数据并刷新列表
                            searchReportAdapter.updateData(reports);
                        }
                    } else {
                        // do noting if no data
                    }
                });
    }

    /**
     * 打开新的dialog，展示报告的详细内容
     *
     * @param medicalReport 报告
     */
    private void showEachMedicalReportDetails(MedicalReport medicalReport) {

        BatteryManager batteryManager = (BatteryManager) getContext().getSystemService(Context.BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        if (battery > 50) {
            // 存储与当前用户进行对话的用户的信息，到view model
            SearchReportDialog reportDialog = new SearchReportDialog();
            medicalReportViewModel.setMedicalReport(medicalReport);

            // 加载注册的fragment
            reportDialog.show(getParentFragmentManager(), "ReportDialog");
        }else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Low Battery Warning")
                    .setMessage("Battery is too low to view report details. Please charge your device.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        // You can add logic here for when the user clicks the positive button, if needed
                        dialog.dismiss();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert) // This adds the standard alert icon; you can change this to a custom one if you prefer
                    .show();
        }


    }

}

