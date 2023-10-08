package com.comp5216.healthguard.fragment.search;


import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.MessagesListAdapter;
import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.entity.MedicalReport;
import com.comp5216.healthguard.viewmodel.ChatViewModel;
import com.comp5216.healthguard.viewmodel.MedicalReportViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * search界面的report fragment的各个dialog
 * <p>
 * search界面的report fragment页面的逻辑 的各个dialog
 * </p>
 *
 * @author X
 * @version 1.0
 * @since 2023-10-08
 */
public class SearchReportDialog extends DialogFragment {
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
    // 报告
    MedicalReport medicalReport;

    // 报告视图模型
    MedicalReportViewModel medicalReportViewModel;

    TextView textViewTime;

    TextView textViewNote;

    // 创建一个SimpleDateFormat对象，指定所需的格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ImageView imageViewImage;

    LinearLayout linearLayoutIndicator;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation01);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_search_report, null);

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
        buttonBack = view.findViewById(R.id.dialog_report_back);
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();

        medicalReport = new MedicalReport();

        medicalReportViewModel = new ViewModelProvider(requireActivity()).get(MedicalReportViewModel.class);

        textViewTime = view.findViewById(R.id.dialog_report_time);

        textViewNote = view.findViewById(R.id.dialog_report_note);

        imageViewImage = view.findViewById(R.id.dialog_report_image);

        linearLayoutIndicator = view.findViewById(R.id.dialog_report_indicator);
    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 监听用户报告数据
        observeUserReport();
    }

    /**
     * 监听用户报告数据
     */
    private void observeUserReport() {
        medicalReportViewModel.getMedicalReport().observe(this,medicalReport ->{

            // 将时间戳转换为Date对象
            Date date = new Date(medicalReport.getMedicalReportDate());

            // 格式化日期
            String formattedDate = sdf.format(date);

            textViewTime.setText(formattedDate);

            textViewNote.setText("note: " + medicalReport.getMedicalReportNote());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String fileName = medicalReport.getMedicalReportId() + ".jpg";  // 替换为你的文件名
            StorageReference fileRef = storageRef.child(fileName);

            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(getContext())
                            .load(uri)
                            .error(R.drawable.load_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // 处理加载失败的情况
                                    Log.e("Glide", "Load failed", e);
                                    return false;  // 返回false表示该事件没有被处理，Glide应该继续处理
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // 图片加载成功但尚未设置到ImageView
                                    Log.d("Glide", "Resource ready");
                                    return false;  // 返回false表示该事件没有被处理，Glide应该继续处理
                                }
                            })
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    // 图片已经加载并设置到ImageView
                                    imageViewImage.setImageDrawable(resource);
                                    // 获取图片的URL
                                    linearLayoutIndicator.setVisibility(View.GONE);
                                    imageViewImage.setVisibility(View.VISIBLE);
                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // 获取图片的URL失败，处理错误
                    linearLayoutIndicator.setVisibility(View.GONE);
                    imageViewImage.setVisibility(View.VISIBLE);
                }
            });

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