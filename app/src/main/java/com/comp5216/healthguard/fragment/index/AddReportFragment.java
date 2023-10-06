package com.comp5216.healthguard.fragment.index;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.util.CustomIdGeneratorUtil;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * 添加report的fragment
 * <p>
 * 添加report的fragment
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-06
 */
public class AddReportFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // 报告备注
    TextInputEditText textInputEditTextNote;
    ActivityResultLauncher<String> mGetContent;
    // 上传到storage的图片
    private StorageReference mStorageRef;
    // upload button
    ImageButton imageButtonUpload;
    // report to add
    MedicalReport medicalReport;
    // upload indicator
    CircularProgressIndicator circularProgressIndicatorIndicator;
    // success notify
    ImageView imageViewSuccess;
    // submit button
    Button buttonSubmit;
    // 指示条
    LinearLayout linearLayoutIndicator;
    // 主页面
    LinearLayout linearLayoutMain;
    // 视图模型
    MedicalReportViewModel medicalReportViewModel;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_report, null);

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
    private void init(View view) {
        // 绑定回退按钮
        buttonBack = view.findViewById(R.id.report_back);
        textInputEditTextNote = view.findViewById(R.id.report_note_input);
        // 上传使用
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                // 处理选择的图片，上传到 Firestore
               uploadImageToFirestore(uri);
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageButtonUpload = view.findViewById(R.id.report_upload);
        medicalReport = new MedicalReport();
        circularProgressIndicatorIndicator = view.findViewById(R.id.report_indicator);
        imageViewSuccess = view.findViewById(R.id.report_success);
        buttonSubmit = view.findViewById(R.id.report_submit);
        linearLayoutIndicator = view.findViewById(R.id.report_indicator_plus);
        linearLayoutMain = view.findViewById(R.id.report_main);
        // 绑定用户医疗报告的view model
        medicalReportViewModel = new ViewModelProvider(this).get(MedicalReportViewModel.class);
    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 上传按钮监听
        imageButtonUploadListener();
        // 提交按钮监听
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
     * 上传按钮监听
     */
    private void imageButtonUploadListener() {
        imageButtonUpload.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
    }

    /**
     * 处理选择的图片，上传到 Firestore
     * @param uri url
     */
    private void uploadImageToFirestore(Uri uri) {
        // 加载加载动画
        circularProgressIndicatorIndicator.setVisibility(View.VISIBLE);
        // 设置report的id，不需要在仓库和repository设置了
        medicalReport.setMedicalReportId(CustomIdGeneratorUtil.generateUniqueId());
        StorageReference imgRef = mStorageRef.child( medicalReport.getMedicalReportId() + ".jpg");
        imgRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // 图片上传成功
                    // 取消加载动画
                    circularProgressIndicatorIndicator.setVisibility(View.GONE);
                    // 提示上传成功
                    imageViewSuccess.setVisibility(View.VISIBLE);
                    // 禁止再次上传
                    imageButtonUpload.setEnabled(false);
                    // 恢复提交按钮
                    recoverButtons();
                })
                .addOnFailureListener(e -> {
                    // 处理错误
                    // 取消加载动画
                    circularProgressIndicatorIndicator.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Wrong！", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 回复按钮
     */
    private void recoverButtons(){
        buttonSubmit.setEnabled(true);
        buttonSubmit.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.medium_gray_blue)));
    }


    /**
     * 提交按钮监听
     */
    private void buttonSubmitListener() {
        buttonSubmit.setOnClickListener(view1 -> {
            // 存储信息到数据库
            storeReport();
        });
    }

    /**
     * 存储信息到数据库
     */
    private void storeReport() {
        // 收起小键盘
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        linearLayoutIndicator.setVisibility(View.VISIBLE);
        linearLayoutMain.setVisibility(View.GONE);

        if(textInputEditTextNote.getText().toString().trim().isEmpty()){
            medicalReport.setMedicalReportNote("No description");
        }else {
            medicalReport.setMedicalReportNote(textInputEditTextNote.getText().toString().trim());
        }

        medicalReport.setMedicalReportDate(System.currentTimeMillis());
        medicalReport.setUserId(FirebaseAuth.getInstance().getUid());
        medicalReport.setMedicalReportDeleteStatus(0);

        // 存储报告到数据库
        medicalReportViewModel.storeMedicalReport(medicalReport ,
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
