package com.comp5216.healthguard.fragment.index;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.comp5216.healthguard.adapter.NotificationListAdapter;
import com.comp5216.healthguard.adapter.RemindersListAdapter;
import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.viewmodel.HealthInformationViewModel;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.comp5216.healthguard.viewmodel.MedicationReminderViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * index界面的fragment
 * <p>
 * index页面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-06
 */
public class IndexFragment extends Fragment {
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

    // 用户的view model
    UserViewModel userViewModel;

    // 日期textview
    TextView textViewDate;

    // 星期textview
    TextView textViewWeekend;

    // 头像imageview
    ImageView imageViewAvatar;

    // name textview
    TextView textViewName;

    // id textview
    TextView textViewId;

    // information date
    TextView textViewDateInformation;

    // report date
    TextView textViewDateReport;

    // reminder date
    TextView textViewDateReminder;

    // add information image button
    ImageButton imageButtonAddInformation;

    // add Report image button
    ImageButton imageButtonAddReport;

    // add Reminder image button
    ImageButton imageButtonAddReminder;

    // some fragment
    AddInformationFragment addInformationFragment;

    AddReportFragment addReportFragment;

    AddReminderFragment addReminderFragment;

    // line chart HealthInformation
    LineChart lineChartHealthInformation;

    // health information view model
    HealthInformationViewModel healthInformationViewModel;

    // line chart medical report
    LineChart lineChartMedicalReport;

    // medical report view model
    MedicalReportViewModel medicalReportViewModel;

    // recycle view for reminder
    RecyclerView recyclerViewReminder;

    // medication reminder view model
    MedicationReminderViewModel medicationReminderViewModel;

    // adapter for recycle view
    RemindersListAdapter remindersListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_index, container, false);

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
        // 初始化用户的view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        textViewDate = view.findViewById(R.id.index_date);
        textViewWeekend = view.findViewById(R.id.index_weekend);
        textViewDateInformation = view.findViewById(R.id.index_date_information);
        textViewDateReport = view.findViewById(R.id.index_date_report);
        textViewDateReminder = view.findViewById(R.id.index_date_reminder);
        getTodayInfo();
        imageViewAvatar = view.findViewById(R.id.index_avatar);
        textViewName = view.findViewById(R.id.index_name);
        textViewId = view.findViewById(R.id.index_id);
        setAvatarAndUserInfo();

        // fragments
        addInformationFragment = new AddInformationFragment();
        addReportFragment = new AddReportFragment();
        addReminderFragment = new AddReminderFragment();
        imageButtonAddInformation = view.findViewById(R.id.index_add_information);
        imageButtonAddReport = view.findViewById(R.id.index_add_report);
        imageButtonAddReminder = view.findViewById(R.id.index_add_reminder);
        // 用户健康信息折线图
        lineChartHealthInformation = view.findViewById(R.id.index_information_chart);
        // 绑定用户健康信息的view model
        healthInformationViewModel = new ViewModelProvider(this).get(HealthInformationViewModel.class);
        // 用户医疗报告折线图
        lineChartMedicalReport = view.findViewById(R.id.index_report_chart);
        // 绑定用户医疗报告信息的view model
        medicalReportViewModel = new ViewModelProvider(this).get(MedicalReportViewModel.class);
        // 用户用药的recycle view
        recyclerViewReminder = view.findViewById(R.id.index_recycle_reminder);
        // 绑定用户用药提醒的view model
        medicationReminderViewModel = new ViewModelProvider(this).get(MedicationReminderViewModel.class);
        // 绑定用户列表的适配器
        recyclerViewReminder.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        addNewInformation();
        addNewReport();
        addNewReminder();
        // 观察第一个健康信息图表的数据
        healthInformationChart();
        // 观察第二个健康信息图表的数据
        medicalReportChart();
        // 观察第三个用户recycle view的数据
        medicationReminderRecycleView();

    }

    /**
     * 获取当天日期
     */
    private void getTodayInfo() {
        // 获取当前日期
        Date today = new Date();

        // 格式化日期为 yy/DD
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        String formattedDate = dateFormat.format(today);
        textViewDate.setText(formattedDate);

        // 获取今天是星期几
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        textViewWeekend.setText(days[dayOfWeek-1]);

        // 格式化日期为 dd/MM/yyyy
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat01 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate01 = dateFormat01.format(today);
        textViewDateInformation.setText(formattedDate01);
        textViewDateReport.setText(formattedDate01);
        textViewDateReminder.setText(formattedDate01);
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


    /**
     * 添加information
     */
    private void addNewInformation() {
        imageButtonAddInformation.setOnClickListener(view -> {
            // 加载注册的fragment
            addInformationFragment.show(getParentFragmentManager(), "AddInformationFragment");
        });
    }

    /**
     * 添加report
     */
    private void addNewReport() {
        imageButtonAddReport.setOnClickListener(view -> {
            // 加载注册的fragment
            addReportFragment.show(getParentFragmentManager(), "AddReportFragment");
        });
    }

    /**
     * 添加reminder
     */
    private void addNewReminder() {
        imageButtonAddReminder.setOnClickListener(view -> {
            // 加载注册的fragment
            addReminderFragment.show(getParentFragmentManager(), "AddReminderFragment");
        });
    }

    /**
     * 健康信息图表数据
     */
    private void healthInformationChart() {
        // 观察LiveData中的数据变化，并相应地更新UI
        healthInformationViewModel.getInformationDataByUserId(FirebaseAuth.getInstance().getUid())
                .observe(getViewLifecycleOwner(), healthInformationList -> {
                    if (healthInformationList != null && !healthInformationList.isEmpty()) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 保持原来的日期格式

                        TreeMap<String, Integer> dateToCountMap = new TreeMap<>();

                        for (HealthInformation info : healthInformationList) {
                            String date = sdf.format(new Date(info.getHealthInformationDate()));
                            dateToCountMap.put(date, dateToCountMap.getOrDefault(date, 0) + 1);
                        }

                        List<Entry> entries = new ArrayList<>();
                        int index = 0;

                        for (Map.Entry<String, Integer> entry : dateToCountMap.entrySet()) {
                            entries.add(new Entry(index++, entry.getValue()));
                        }

                        LineDataSet dataSet = new LineDataSet(entries, "");
                        dataSet.setColor(Color.BLUE);
                        dataSet.setValueTextSize(12f);
                        dataSet.setDrawCircles(true);
                        dataSet.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) value);
                            }
                        });

                        LineData lineData = new LineData(dataSet);
                        lineChartHealthInformation.setData(lineData);

                        // 配置X轴显示日期
                        XAxis xAxis = lineChartHealthInformation.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int idx = (int) value;
                                if (idx >= 0 && idx < dateToCountMap.keySet().size()) {
                                    String fullDate = new ArrayList<>(dateToCountMap.keySet()).get(idx);
                                    return fullDate.substring(5);  // 只显示月和日
                                }
                                return "";
                            }
                        });
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);

                        // 隐藏不需要的元素
                        lineChartHealthInformation.getXAxis().setDrawGridLines(false);
                        lineChartHealthInformation.getAxisLeft().setDrawGridLines(false);
                        lineChartHealthInformation.getAxisLeft().setEnabled(false);
                        lineChartHealthInformation.getAxisRight().setEnabled(false);
                        lineChartHealthInformation.getLegend().setEnabled(false);
                        lineChartHealthInformation.getDescription().setEnabled(false);
                        lineChartHealthInformation.invalidate();


                    } else {
                        // 没读到数据什么都不做
                    }
                });
    }

    /**
     * 医疗报告图表数据
     */
    private void medicalReportChart() {
        // 观察LiveData中的数据变化，并相应地更新UI
        medicalReportViewModel.getReportDataByUserId(FirebaseAuth.getInstance().getUid())
                .observe(getViewLifecycleOwner(), medicalReportList -> {
                    if (medicalReportList != null && !medicalReportList.isEmpty()) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 保持原来的日期格式

                        TreeMap<String, Integer> dateToCountMap = new TreeMap<>();

                        for (MedicalReport info : medicalReportList) {
                            String date = sdf.format(new Date(info.getMedicalReportDate()));
                            dateToCountMap.put(date, dateToCountMap.getOrDefault(date, 0) + 1);
                        }

                        List<Entry> entries = new ArrayList<>();
                        int index = 0;

                        for (Map.Entry<String, Integer> entry : dateToCountMap.entrySet()) {
                            entries.add(new Entry(index++, entry.getValue()));
                        }

                        LineDataSet dataSet = new LineDataSet(entries, "");
                        dataSet.setColor(Color.BLUE);
                        dataSet.setValueTextSize(12f);
                        dataSet.setDrawCircles(true);
                        dataSet.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) value);
                            }
                        });

                        LineData lineData = new LineData(dataSet);
                        lineChartMedicalReport.setData(lineData);

                        // 配置X轴显示日期
                        XAxis xAxis = lineChartMedicalReport.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int idx = (int) value;
                                if (idx >= 0 && idx < dateToCountMap.keySet().size()) {
                                    String fullDate = new ArrayList<>(dateToCountMap.keySet()).get(idx);
                                    return fullDate.substring(5);  // 只显示月和日
                                }
                                return "";
                            }
                        });
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);

                        // 隐藏不需要的元素
                        lineChartMedicalReport.getXAxis().setDrawGridLines(false);
                        lineChartMedicalReport.getAxisLeft().setDrawGridLines(false);
                        lineChartMedicalReport.getAxisLeft().setEnabled(false);
                        lineChartMedicalReport.getAxisRight().setEnabled(false);
                        lineChartMedicalReport.getLegend().setEnabled(false);
                        lineChartMedicalReport.getDescription().setEnabled(false);
                        lineChartMedicalReport.invalidate();


                    } else {
                        // 没读到数据什么都不做
                    }
                });
    }

    /**
     * 观察第三个用户recycle view的数据
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
                    recyclerViewReminder.setAdapter(remindersListAdapter);
                } else {
                    // 如果适配器已经初始化，只需更新数据并刷新列表
                    remindersListAdapter.updateData(medicationReminderList);
                }

            }
        });
    }
}