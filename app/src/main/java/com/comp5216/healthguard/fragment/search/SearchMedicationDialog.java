package com.comp5216.healthguard.fragment.search;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.RemindersListAdapter;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.entity.MedicationReminder;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.comp5216.healthguard.viewmodel.MedicationReminderViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * search界面的medication fragment的各个dialog
 * <p>
 * search界面的medication fragment的各个dialog
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchMedicationDialog extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;

    // firebase的auth
    FirebaseAuth auth;
    // firebase user
    FirebaseUser firebaseUser;
    // 用户的uid
    String userUid;

    MedicationReminderViewModel medicationReminderViewModel;

    MedicationReminder medicationReminder;

    TextView m1;
    TextView m2;
    TextView m3;
    TextView m4;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation01);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_search_medication, null);

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
        buttonBack = view.findViewById(R.id.dialog_medication_back);
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();

        medicationReminderViewModel = new ViewModelProvider(requireActivity()).get(MedicationReminderViewModel.class);

        medicationReminder = new MedicationReminder();
        m1 = view.findViewById(R.id.m1);
        m2 = view.findViewById(R.id.m2);
        m3 = view.findViewById(R.id.m3);
        m4 = view.findViewById(R.id.m4);

    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 监听用户药物提醒数据
        observeUserMedication();
    }

    /**
     * 监听用户药物提醒数据
     */
    private void observeUserMedication() {
        medicationReminderViewModel.getMedicationReminder().observe(this, medicationReminder ->
        {
            m1.setText(medicationReminder.getMedicationReminderDrugName());
            m2.setText(medicationReminder.getMedicationReminderDrugDosage());
            m3.setText(medicationReminder.getMedicationReminderDrugTime());
            m4.setText(medicationReminder.getMedicationReminderDrugNote());
    });
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

    @Override
    public void dismiss() {
        super.dismiss();
    }

}