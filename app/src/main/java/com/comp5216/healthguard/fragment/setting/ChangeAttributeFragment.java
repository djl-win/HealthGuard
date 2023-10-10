package com.comp5216.healthguard.fragment.setting;

import android.app.Activity;
import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Attribute;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户个人信息处理页面的修改属性
 * <p>
 * 处理用户信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-11
 */
public class ChangeAttributeFragment extends DialogFragment {
    // 页面底层view组件
    View view;

    // 回退按钮
    ImageButton buttonBack;

    // some attribute
    TextInputEditText sysLow;
    TextInputEditText sysHigh;
    TextInputEditText diaLow;
    TextInputEditText diaHigh;
    TextInputEditText heartLow;
    TextInputEditText heartHigh;
    TextInputEditText bodyLow;
    TextInputEditText bodyHigh;
    TextInputEditText bloodLow;
    TextInputEditText bloodHigh;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;

    Button buttonSubmit;

    LinearLayout linearLayoutMain;
    LinearLayout linearLayoutIndicator;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_attribute, null);

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
        buttonBack = view.findViewById(R.id.change_attribute_back);
        // 绑定更新按钮
        buttonSubmit = view.findViewById(R.id.change_attribute_update);

        // some attribute
        sysLow = view.findViewById(R.id.change_attribute_sys_low);
        sysHigh = view.findViewById(R.id.change_attribute_sys_high);
        diaLow = view.findViewById(R.id.change_attribute_dia_low);
        diaHigh = view.findViewById(R.id.change_attribute_dia_high);
        heartLow = view.findViewById(R.id.change_attribute_heart_low);
        heartHigh = view.findViewById(R.id.change_attribute_heart_high);
        bodyLow = view.findViewById(R.id.change_attribute_body_low);
        bodyHigh = view.findViewById(R.id.change_attribute_body_high);
        bloodLow = view.findViewById(R.id.change_attribute_blood_low);
        bloodHigh = view.findViewById(R.id.change_attribute_blood_high);

        linearLayoutMain = view.findViewById(R.id.change_attribute_main);
        linearLayoutIndicator = view.findViewById(R.id.change_attribute_indicator);

    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        buttonBackListener();
        // 初始化输入框内容监听器
        initTextWatcher();
        sysLow.addTextChangedListener(generalTextWatcher);
        sysHigh.addTextChangedListener(generalTextWatcher);
        diaLow.addTextChangedListener(generalTextWatcher);
        diaHigh.addTextChangedListener(generalTextWatcher);
        heartLow.addTextChangedListener(generalTextWatcher);
        heartHigh.addTextChangedListener(generalTextWatcher);
        bodyLow.addTextChangedListener(generalTextWatcher);
        bodyHigh.addTextChangedListener(generalTextWatcher);
        bloodLow.addTextChangedListener(generalTextWatcher);
        bloodHigh.addTextChangedListener(generalTextWatcher);

        // 监听提交按钮
        buttonSubmitListener();



    }

    /**
     * 监听提交按钮
     */
    private void buttonSubmitListener() {
        buttonSubmit.setOnClickListener(view1 -> {
            changeAttribute();
        });
    }


    /**
     * 修改属性
     */
    private void changeAttribute(){
        // 收起小键盘
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        linearLayoutMain.setVisibility(View.GONE);
        linearLayoutIndicator.setVisibility(View.VISIBLE);

        Attribute attribute = new Attribute();
        attribute.setAttributeSystolicLow(sysLow.getText().toString().trim());
        attribute.setAttributeSystolicHigh(sysHigh.getText().toString().trim());
        attribute.setAttributeDiastolicLow(diaLow.getText().toString().trim());
        attribute.setAttributeDiastolicHigh(diaHigh.getText().toString().trim());
        attribute.setAttributeHeartRateLow(heartLow.getText().toString().trim());
        attribute.setAttributeHeartRateHigh(heartHigh.getText().toString().trim());
        attribute.setAttributeBodyTemperatureLow(bodyLow.getText().toString().trim());
        attribute.setAttributeBodyTemperatureHigh(bodyHigh.getText().toString().trim());
        attribute.setAttributeBloodOxygenLow(bloodLow.getText().toString().trim());
        attribute.setAttributeBloodOxygenHigh(bloodHigh.getText().toString().trim());
        attribute.setUserId(FirebaseAuth.getInstance().getUid());

        // 直接写在这里了数据库更新，没写到mvvm框架
        FirebaseFirestore.getInstance().collection("attribute")
                .whereEqualTo("userId", attribute.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // 获取文档的引用
                                DocumentReference docRef = FirebaseFirestore.getInstance().collection("attribute").document(document.getId());

                                // 创建一个Map来存储你想要更新的字段和值
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("attributeSystolicLow", attribute.getAttributeSystolicLow());
                                updates.put("attributeSystolicHigh", attribute.getAttributeSystolicHigh());
                                updates.put("attributeDiastolicLow", attribute.getAttributeDiastolicLow());
                                updates.put("attributeDiastolicHigh", attribute.getAttributeDiastolicHigh());
                                updates.put("attributeHeartRateLow", attribute.getAttributeHeartRateLow());
                                updates.put("attributeHeartRateHigh", attribute.getAttributeHeartRateHigh());
                                updates.put("attributeBodyTemperatureLow", attribute.getAttributeBodyTemperatureLow());
                                updates.put("attributeBodyTemperatureHigh", attribute.getAttributeBodyTemperatureHigh());
                                updates.put("attributeBloodOxygenLow", attribute.getAttributeBloodOxygenLow());
                                updates.put("attributeBloodOxygenHigh", attribute.getAttributeBloodOxygenHigh());

                                // 使用update方法更新文档
                                docRef.update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dismiss();
                                                Log.d("djl", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                linearLayoutMain.setVisibility(View.VISIBLE);
                                                linearLayoutIndicator.setVisibility(View.GONE);
                                                Log.w("djl", "Error updating document", e);
                                            }
                                        });
                            }
                        } else {
                            linearLayoutMain.setVisibility(View.VISIBLE);
                            linearLayoutIndicator.setVisibility(View.GONE);
                            Log.d("djl", "Error getting documents: ", task.getException());
                        }
                    }
                });


        Log.d("djl", attribute.toString());


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
        if(checkEditTexts(sysLow.getText())
                && checkEditTexts(sysHigh.getText())
                && checkEditTexts(diaLow.getText())
                && checkEditTexts(diaHigh.getText())
                && checkEditTexts(heartLow.getText())
                && checkEditTexts(heartHigh.getText())
                && checkEditTexts(bodyLow.getText())
                && checkEditTexts(bodyHigh.getText())
                && checkEditTexts(bloodLow.getText())
                && checkEditTexts(bloodHigh.getText())
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

}
