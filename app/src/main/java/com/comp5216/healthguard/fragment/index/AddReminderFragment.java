package com.comp5216.healthguard.fragment.index;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.comp5216.healthguard.viewmodel.MedicationReminderViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

/**
 * 添加reminder的fragment
 * <p>
 * 添加reminder的fragment
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-06
 */
public class AddReminderFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // 药品名称
    TextInputEditText textInputEditTextName;
    // 药品剂量
    TextInputEditText textInputEditTextDosage;
    // 药品说明
    TextInputEditText textInputEditTextNote;
    // 服药时间
    TimePicker timePickerTime;
    // submit button
    Button buttonSubmit;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;
    // 指示条
    LinearLayout linearLayoutIndicator;
    // 主页面
    LinearLayout linearLayoutMain;
    // 用药提醒视图模型
    MedicationReminderViewModel medicationReminderViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_reminder, null);

        // 设置Dialog的内容视图
        dialog.setContentView(view);

        // 设置软键盘模式，当键盘弹出时调整布局
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        // 初始化获取xml中组件
        init(view);
        // 给页面组件设置监听器
        setListeners();

        // 返回设置好的Dialog实例
        return dialog;
    }

    /**
     * 获取xml中组件
     *
     * @param view 页面底层view组件
     */
    private void init(View view){
        // 绑定回退按钮
        buttonBack = view.findViewById(R.id.reminder_back);
        textInputEditTextName = view.findViewById(R.id.reminder_name_input);
        textInputEditTextDosage = view.findViewById(R.id.reminder_dosage_input);
        textInputEditTextNote = view.findViewById(R.id.reminder_note_input);
        timePickerTime = view.findViewById(R.id.reminder_time);
        timePickerTime.setIs24HourView(true); // 24小时制
        buttonSubmit = view.findViewById(R.id.reminder_submit);

        linearLayoutIndicator = view.findViewById(R.id.reminder_indicator);
        linearLayoutMain = view.findViewById(R.id.reminder_main);
        medicationReminderViewModel = new ViewModelProvider(this).get(MedicationReminderViewModel.class);
    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 初始化输入框内容监听器
        initTextWatcher();
        // input listener
        textInputEditTextName.addTextChangedListener(generalTextWatcher);
        textInputEditTextDosage.addTextChangedListener(generalTextWatcher);
        textInputEditTextNote.addTextChangedListener(generalTextWatcher);
        // 提交按钮监听器
        buttonSubmitListener();
    }

    /**
     * 回退按钮监听器
     */
    private void buttonBackListener() {
        buttonBack.setOnClickListener(view -> {
            // 当回退按钮被点击时，调用dismiss()方法，使对话框消失
            dismiss();
        });
    }

    /**
     * 输入框内容监听器
     */
    private void initTextWatcher() {
        generalTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在此处不执行任何操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 在此处不执行任何操作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 检查输入框是否有内容
                checkAllEditTexts();
            }
        };
    }

    /**
     * 检查所以输入框是否都有内容
     */
    private void checkAllEditTexts() {
        // 检查所有输入框是否有内容，如果有的输入框没有内容，则禁用登录按钮
        if(checkEditTexts(textInputEditTextName.getText())
                && checkEditTexts(textInputEditTextDosage.getText())
                && checkEditTexts(textInputEditTextNote.getText())
        ){
            // 恢复按钮
            recoverButtons();
        }else {
            // 禁用按钮
            unableButtons();
        }
    }

    /**
     * 检查输入框是否有内容
     * @param s 输入框获取的内容
     * @return 有内容返回 true，反正
     */
    private boolean checkEditTexts(Editable s) {
        return s != null && s.length() > 0;
    }

    /**
     * 禁用按钮
     */
    private void unableButtons(){
        buttonSubmit.setEnabled(false);
        buttonSubmit.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.gray)));
    }

    /**
     * 回复按钮
     */
    private void recoverButtons(){
        buttonSubmit.setEnabled(true);
        buttonSubmit.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.medium_gray_blue)));
    }

    /**
     * 提交按钮监听器
     */
    private void buttonSubmitListener() {
        buttonSubmit.setOnClickListener(view1 -> {
            // 存储信息到数据库
            storeReminder();
        });
    }

    /**
     * 存储信息到数据库
     */
    private void storeReminder() {
        // 收起小键盘
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        linearLayoutIndicator.setVisibility(View.VISIBLE);
        linearLayoutMain.setVisibility(View.GONE);

        MedicationReminder medicationReminder = new MedicationReminder();
        medicationReminder.setMedicationReminderDrugName(textInputEditTextName.getText().toString().trim());
        medicationReminder.setMedicationReminderDrugDosage(textInputEditTextDosage.getText().toString().trim());
        medicationReminder.setMedicationReminderDrugNote(textInputEditTextNote.getText().toString().trim());

        // 获取选定的小时和分钟
        int hour = timePickerTime.getHour();
        int minute = timePickerTime.getMinute();

        // 将小时和分钟格式化为"HH:mm"格式
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        medicationReminder.setMedicationReminderDrugTime(formattedTime);

        medicationReminderViewModel.storeMedicationReminder(medicationReminder ,
                aVoid -> {
                    // 存储成功,当前dialog关闭
                    dismiss();
                    // 提醒用户
                    Toast.makeText(getActivity(), "Congratulations！", Toast.LENGTH_SHORT).show();
                },
                e -> {
                    // 取消进度条
                    linearLayoutIndicator.setVisibility(View.GONE);
                    linearLayoutMain.setVisibility(View.VISIBLE);
                    // 提醒用户
                    Toast.makeText(getActivity(), "Something Wrong！", Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * 重写dismiss方法，使得当对话框消失的时候，按钮可用
     *
     * @param dialog 当前dialog
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}