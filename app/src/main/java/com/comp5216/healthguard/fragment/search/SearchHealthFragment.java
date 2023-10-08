package com.comp5216.healthguard.fragment.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.FriendsListAdapter;
import com.comp5216.healthguard.adapter.RemindersListAdapter;
import com.comp5216.healthguard.adapter.SearchHealthAdapter;
import com.comp5216.healthguard.entity.Attribute;
import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.viewmodel.AttributeViewModel;
import com.comp5216.healthguard.viewmodel.HealthInformationViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * search界面的health fragment
 * <p>
 * search界面的health fragment页面的逻辑
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchHealthFragment extends Fragment {
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

    // 下拉框的适配器
    ArrayAdapter<String> adapter;

    // 下拉框中的数据
    String[] items = {"Systolic Pressure", "Diastolic Pressure", "Heart Rate", "Body Temperature", "Blood Oxygen"};

    // 下拉框
    AutoCompleteTextView autoCompleteTextViewSelect;

    // 图片
    LineChart lineChartHealthInfo;

    // health info view model
    HealthInformationViewModel healthInformationViewModel;

    // 用户健康数据
    List<HealthInformation> healthInformations;

    // flag 用于判断图表的显示类型
    int flagChartType = 0;

    // recycle view
    RecyclerView recyclerViewHealth;

    // 适配器
    SearchHealthAdapter searchHealthAdapter;

    // 用户属性值
    Attribute attribute = new Attribute();
    AttributeViewModel attributeViewModel;

    // 创建一个SimpleDateFormat对象，指定所需的格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 展示的属性
    TextView textViewSystolic;
    TextView textViewDiastolic;
    TextView textViewHeart;
    TextView textViewBody;
    TextView textViewBlood;
    TextView textViewTime;
    TextView textViewStatus;
    ImageButton imageButtonBack;
    LinearLayout linearLayout01;
    LinearLayout linearLayout02;
    TextView textViewR1;
    TextView textViewR2;
    TextView textViewR3;
    TextView textViewR4;
    TextView textViewR5;
    TextView z1;
    TextView z2;
    TextView z3;
    TextView z4;
    TextView z5;
    TextView zz1;
    TextView zz2;
    TextView zz3;
    TextView zz4;
    TextView zz5;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        view = inflater.inflate(R.layout.fragment_search_health, container, false);

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

        autoCompleteTextViewSelect = view.findViewById(R.id.search_health_select_input);

        lineChartHealthInfo = view.findViewById(R.id.search_health_chart);

        healthInformationViewModel = new ViewModelProvider(requireActivity()).get(HealthInformationViewModel.class);
        attributeViewModel = new ViewModelProvider(requireActivity()).get(AttributeViewModel.class);

        healthInformations = new ArrayList<>();

        recyclerViewHealth = view.findViewById(R.id.search_health_recycle);

        recyclerViewHealth.setLayoutManager(new LinearLayoutManager(getContext()));

        textViewSystolic = view.findViewById(R.id.search_health_systolic);
        textViewDiastolic = view.findViewById(R.id.search_health_diastolic);
        textViewHeart = view.findViewById(R.id.search_health_heart);
        textViewBody = view.findViewById(R.id.search_health_body);
        textViewBlood = view.findViewById(R.id.search_health_blood);
        textViewTime = view.findViewById(R.id.search_health_time);
        textViewStatus = view.findViewById(R.id.search_health_status);
        imageButtonBack = view.findViewById(R.id.search_health_back);
        linearLayout01 = view.findViewById(R.id.search_health_01);
        linearLayout02 = view.findViewById(R.id.search_health_02);
        textViewR1 = view.findViewById(R.id.search_health_r1);
        textViewR2 = view.findViewById(R.id.search_health_r2);
        textViewR3 = view.findViewById(R.id.search_health_r3);
        textViewR4 = view.findViewById(R.id.search_health_r4);
        textViewR5 = view.findViewById(R.id.search_health_r5);

        z1 = view.findViewById(R.id.z1);
        z2 = view.findViewById(R.id.z2);
        z3 = view.findViewById(R.id.z3);
        z4 = view.findViewById(R.id.z4);
        z5 = view.findViewById(R.id.z5);
        zz1 = view.findViewById(R.id.zz1);
        zz2 = view.findViewById(R.id.zz2);
        zz3 = view.findViewById(R.id.zz3);
        zz4 = view.findViewById(R.id.zz4);
        zz5 = view.findViewById(R.id.zz5);

    }

    /**
     * 为组件添加监听器
     */
    private void setListener() {
        // 初始化下拉框
        if(getActivity() != null) {
            adapter = new ArrayAdapter<>(
                    getActivity(),
                    R.layout.dropdown_list_item,
                    items
            );
            autoCompleteTextViewSelect.setAdapter(adapter);
        }

        // 监听下拉列表
        selectListener();

        // 监控用户属性值
        observeUserAttribute();

        // 监听所有的健康数据
        observeHealthInfo();

        // 监听回退按钮
        imageButtonBackListener();
    }

    /**
     * 监听回退按钮
     */
    private void imageButtonBackListener() {
        imageButtonBack.setOnClickListener(view1 -> {
            linearLayout01.setVisibility(View.VISIBLE);
            linearLayout02.setVisibility(View.GONE);

            textViewSystolic.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            textViewDiastolic.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            textViewHeart.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            textViewBody.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            textViewBlood.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            z1.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            zz1.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            z2.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            zz2.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            z3.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            zz3.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            z4.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            zz4.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            z5.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            zz5.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
        });
    }

    /**
     *  监听下拉列表
     */
    private void selectListener() {
        autoCompleteTextViewSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中的项
                String itemText = (String) parent.getItemAtPosition(position);
                if("Systolic Pressure".equals(itemText)){
                    flagChartType = 0;
                } else if ("Diastolic Pressure".equals(itemText)) {
                    flagChartType = 1;
                } else if ("Heart Rate".equals(itemText)){
                    flagChartType = 2;
                } else if("Body Temperature".equals(itemText)){
                    flagChartType = 3;
                } else if ("Blood Oxygen".equals(itemText)) {
                    flagChartType = 4;
                }
                renderChart();

            }
        });

    }

    /**
     * 监听所有的健康数据
     */
    private void observeHealthInfo() {
        // 观察LiveData中的数据变化，并相应地更新UI
        healthInformationViewModel.getInformationDataByUserId(FirebaseAuth.getInstance().getUid())
                .observe(getViewLifecycleOwner(), healthInformationList -> {
                    if (healthInformationList != null && !healthInformationList.isEmpty()) {
                        healthInformations = healthInformationList;
                        renderChart();

                        // 如果列表的适配器尚未初始化
                        if (searchHealthAdapter == null) {
                            // 初始化适配器，并设置它为recyclerView的适配器
                            healthInformationList.sort((o1, o2) -> Long.compare(o2.getHealthInformationDate(), o1.getHealthInformationDate()));
                            searchHealthAdapter = new SearchHealthAdapter(getContext(), healthInformationList);
                            recyclerViewHealth.setAdapter(searchHealthAdapter);
                            // 为recycleView的每个item设置带点击监听器
                            searchHealthAdapter.setItemClickListener(new SearchHealthAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(HealthInformation healthInformation) {
                                    // 展示每个卡片的内容
                                    showEachHealthInformationDetails(healthInformation);
                                }
                            });
                        } else {
                            // 初始化适配器，并设置它为recyclerView的适配器
                            healthInformationList.sort((o1, o2) -> Long.compare(o2.getHealthInformationDate(), o1.getHealthInformationDate()));
                            // 如果适配器已经初始化，只需更新数据并刷新列表
                            searchHealthAdapter.updateData(healthInformationList);
                        }

                    } else {
                        // 没读到数据什么都不做
                    }

                });
    }

    /**
     * 展示每个信息的具体内容
     */
    private void showEachHealthInformationDetails(HealthInformation healthInformation) {
        textViewSystolic.setText(healthInformation.getHealthInformationSystolic());
        textViewDiastolic.setText(healthInformation.getHealthInformationDiastolic());
        textViewHeart.setText(healthInformation.getHealthInformationHeartRate());
        textViewBody.setText(healthInformation.getHealthInformationBodyTemperature());
        textViewBlood.setText(healthInformation.getHealthInformationBloodOxygen());

        // 将时间戳转换为Date对象
        Date date = new Date(healthInformation.getHealthInformationDate());
        // 格式化日期
        String formattedDate = sdf.format(date);
        textViewTime.setText(formattedDate);

        // 健康
        if (healthInformation.getHealthInformationHealthStatus() == 0) {
           textViewStatus.setText("Normal");
            textViewStatus.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));
            textViewTime.setTextColor((ContextCompat.getColor(getContext(), R.color.black)));;
        }
        // 不健康
        else {
            textViewStatus.setText("Abnormal");
            textViewStatus.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            textViewTime.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));;
        }

        // 检查收缩压
        if (!isWithinRange(
                healthInformation.getHealthInformationSystolic(),
                attribute.getAttributeSystolicLow(),
                attribute.getAttributeSystolicHigh())) {

            textViewSystolic.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            z1.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            zz1.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
        }
        // 检查舒张压
        if (!isWithinRange(
                healthInformation.getHealthInformationDiastolic(),
                attribute.getAttributeDiastolicLow(),
                attribute.getAttributeDiastolicHigh())) {
            textViewDiastolic.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            z2.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            zz2.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
        }
        // 检查心率
        if (!isWithinRange(
                healthInformation.getHealthInformationHeartRate(),
                attribute.getAttributeHeartRateLow(),
                attribute.getAttributeHeartRateHigh())) {
            textViewHeart.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            z3.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            zz3.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
        }
        // 检查体温
        if (!isWithinRange(
                healthInformation.getHealthInformationBodyTemperature(),
                attribute.getAttributeBodyTemperatureLow(),
                attribute.getAttributeBodyTemperatureHigh())) {
            textViewBody.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            z4.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            zz4.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
        }
        // 检查血氧
        if (!isWithinRange(
                healthInformation.getHealthInformationBloodOxygen(),
                attribute.getAttributeBloodOxygenLow(),
                attribute.getAttributeBloodOxygenHigh())) {
            textViewBlood.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            z5.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
            zz5.setTextColor((ContextCompat.getColor(getContext(), R.color.red)));
        }



        linearLayout01.setVisibility(View.GONE);
        linearLayout02.setVisibility(View.VISIBLE);
    }

    /**
     * 判断是否用户身体处于正常范围
     * @param valueStr 用户数据
     * @param lowStr 低数据
     * @param highStr 高数据
     * @return 是否
     */
    private static boolean isWithinRange(String valueStr, String lowStr, String highStr) {
        try {
            float value = Float.parseFloat(valueStr);
            float low = Float.parseFloat(lowStr);
            float high = Float.parseFloat(highStr);
            return value >= low && value <= high;
        } catch (NumberFormatException e) {
            // Handle parsing exception if necessary
            return false;
        }
    }

    /**
     * 监控用户属性值
     */
    private void observeUserAttribute() {
    // 观察LiveData中的数据变化，并相应地更新UI
        attributeViewModel.getAttributeById(FirebaseAuth.getInstance().getUid())
                .observe(getViewLifecycleOwner(), observedAttribute -> {
                    if(observedAttribute != null){
                        attribute = observedAttribute;
                        textViewR1.setText(observedAttribute.getAttributeSystolicLow() + "-" + observedAttribute.getAttributeSystolicHigh());
                        textViewR2.setText(observedAttribute.getAttributeDiastolicLow() + "-" + observedAttribute.getAttributeDiastolicHigh());
                        textViewR3.setText(observedAttribute.getAttributeHeartRateLow() + "-" + observedAttribute.getAttributeHeartRateHigh());
                        textViewR4.setText(observedAttribute.getAttributeBodyTemperatureLow() + "-" + observedAttribute.getAttributeBodyTemperatureHigh());
                        textViewR5.setText(observedAttribute.getAttributeBloodOxygenLow() + "-" + observedAttribute.getAttributeBloodOxygenHigh());
                    }
                });
    }


    /**
     * 渲染图表
     */
    private void renderChart(){
        if (healthInformations != null && !healthInformations.isEmpty()) {
            // 按日期降序排序
            healthInformations.sort((o1, o2) -> Long.compare(o2.getHealthInformationDate(), o1.getHealthInformationDate()));
            // 如果记录多于6条，只取最新的6条记录
            List<HealthInformation> latestSix = healthInformations.subList(0, Math.min(6, healthInformations.size()));

            Collections.reverse(latestSix);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 保持原来的日期格式

            TreeMap<String, Float> dateToSystolicMap = new TreeMap<>();
            int index = 0;  // 添加一个索引来保持记录的顺序

            for (HealthInformation info : latestSix) {
                String date = sdf.format(new Date(info.getHealthInformationDate()));
                try {
                    float value;

                    // diastolic
                    if (flagChartType == 1){
                        value = Float.parseFloat(info.getHealthInformationDiastolic());
                    }
                    // heart rate
                    else if (flagChartType == 2){
                        value = Float.parseFloat(info.getHealthInformationHeartRate());
                    }
                    // body
                    else if (flagChartType == 3){
                        value = Float.parseFloat(info.getHealthInformationBodyTemperature());
                    }
                    // blood
                    else if (flagChartType == 4){
                        value = Float.parseFloat(info.getHealthInformationBloodOxygen());
                    }
                    // systolic
                    else {
                        value = Float.parseFloat(info.getHealthInformationSystolic());
                    }
                    String uniqueKey = date + ":" + index;  // 使用日期和索引生成一个唯一的键
                    dateToSystolicMap.put(uniqueKey, value);
                } catch (NumberFormatException e) {
                    Log.e("djl", "Unable to parse String value in this chart, please check your database of healthInformation ");
                }
                index++;  // 增加索引以保证下一个键的唯一性
            }

            List<Entry> entries = new ArrayList<>();
            index = 0;

            for (Map.Entry<String, Float> entry : dateToSystolicMap.entrySet()) {
                entries.add(new Entry(index++, entry.getValue()));
            }

            //修改显示在y轴的元素
            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextSize(12f);
            dataSet.setDrawCircles(true);
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    String unit;
                    if (flagChartType == 1) {
                        unit = " mmHg";
                    } else if (flagChartType == 2) {
                        unit = " bpm";
                    } else if (flagChartType == 3) {
                        unit = " °C";
                    } else if (flagChartType == 4) {
                        unit = " %";
                    } else {
                        unit = " mmHg";
                    }
                    return (int) value + unit;
                }
            });

            LineData lineData = new LineData(dataSet);
            lineChartHealthInfo.setData(lineData);
            lineChartHealthInfo.setExtraOffsets(35f, 0f, 35f, 0f);

            // 配置X轴显示日期
            XAxis xAxis = lineChartHealthInfo.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int idx = (int) value;
                    if (idx >= 0 && idx < dateToSystolicMap.keySet().size()) {
                        String fullDate = new ArrayList<>(dateToSystolicMap.keySet()).get(idx);
                        return fullDate.substring(5);  // 只显示月和日
                    }
                    return "";
                }
            });
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setLabelRotationAngle(-45);  // 设置标签的旋转角度，以便更好地适应空间


            // 隐藏不需要的元素
            lineChartHealthInfo.getXAxis().setDrawGridLines(false);
            lineChartHealthInfo.getAxisLeft().setDrawGridLines(false);
            lineChartHealthInfo.getAxisLeft().setEnabled(false);
            lineChartHealthInfo.getAxisRight().setEnabled(false);
            lineChartHealthInfo.getLegend().setEnabled(false);
            lineChartHealthInfo.getDescription().setEnabled(false);
            lineChartHealthInfo.invalidate();
        } else {
            // 没读到数据什么都不做
        }

    }



}
