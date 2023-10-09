package com.comp5216.healthguard.fragment.enter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.DialogFragment;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.activity.PortalActivity;
import com.comp5216.healthguard.util.CustomAnimationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

/**
 * 登录界面的fragment
 * <p>
 * 登录界面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class LoginFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;
    // 邮箱输入框
    TextInputEditText editTextEmail;
    // 密码输入框
    TextInputEditText editTextPassword1;
    // 错误提醒文字
    TextView textViewError;
    // 邮箱框layout
    TextInputLayout textInputEmail;
    // 继续按钮
    Button buttonLogin;
    // 整个页面的layout
    LinearLayout linearLayoutMain;
    // 注册进度条
    LinearLayout linearLayoutProgressIndicator;
    // firebase的认证实例
    FirebaseAuth auth;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);

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
        buttonBack = view.findViewById(R.id.button_back_login);
        // 绑定邮箱输入框
        editTextEmail = view.findViewById(R.id.text_edit_email_login);
        // 绑定密码输入框
        editTextPassword1 = view.findViewById(R.id.text_edit_password1_login);
        // 绑定错误提醒
        textViewError = view.findViewById(R.id.text_view_error_login);
        // 绑定邮箱输入框layout
        textInputEmail = view.findViewById(R.id.text_input_email_login);
        // 绑定整个页面的layout
        linearLayoutMain = view.findViewById(R.id.linear_layout_main_login);
        // 绑定注册进度条页面的layout
        linearLayoutProgressIndicator = view.findViewById(R.id.linear_layout_progress_indicator_login);
        // 绑定登录按钮
        buttonLogin = view.findViewById(R.id.button_login_login);
        // 获取firebase认证的实例
        auth = FirebaseAuth.getInstance();
    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 初始化输入框内容监听器
        initTextWatcher();
        // 所有输入框内容输入框监听
        editTextEmail.addTextChangedListener(generalTextWatcher);
        editTextPassword1.addTextChangedListener(generalTextWatcher);
        // 登录按钮监听
        buttonLoginListener();
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
        // 1.检查所有输入框是否有内容，如果有的输入框没有内容，则禁用登录按钮
        // 2.检查用户邮箱输入格式是否正确，如果不正确，则禁用按钮
        if(checkEditTexts(editTextEmail.getText())
                && checkEditTexts(editTextPassword1.getText())
                && isValidEmail(editTextEmail.getText().toString().trim())
        ){
            // 恢复按钮
            recoverButtons();
        }else {
            // 禁用按钮
            unableButtons();
        }

        // 检查用户邮箱输入格式是否正确，如果不正确，给出提示
        if(!isValidEmail(editTextEmail.getText().toString().trim())){
            // 修改密码框的样式，重点提醒
            textInputEmail.setErrorEnabled(true);
            textInputEmail.setError(getString(R.string.error_illegal_email));
        }
        else if(isValidEmail(editTextEmail.getText().toString().trim())){
            // 修改邮箱框的样式，取消提醒
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
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
     * 检查邮箱格式是否正确
     *
     * @param email 用户输入的邮箱
     * @return 正确与否
     */
    private boolean isValidEmail(String email) {
        if(Objects.equals(email, "")){
            return true;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * 禁用按钮
     */
    private void unableButtons(){
        buttonLogin.setEnabled(false);
        buttonLogin.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.gray)));
    }

    /**
     * 回复按钮
     */
    private void recoverButtons(){
        buttonLogin.setEnabled(true);
        buttonLogin.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.medium_gray_blue)));
    }

    /**
     * 登录按钮监听器
     */
    private void buttonLoginListener() {
        buttonLogin.setOnClickListener(view -> {
            // 加载进度条
            linearLayoutMain.setVisibility(View.GONE);
            linearLayoutProgressIndicator.setVisibility(View.VISIBLE);
            // 收起小键盘
            hideKeyboard(view);

            // 用户点击之后，禁用继续按钮，和登录按钮，防止用户在加载动画时重复点击按钮
            buttonLogin.setEnabled(false);

            // 开始按钮动画,这里使用自定义按钮动画
            CustomAnimationUtil.ButtonsAnimation(getActivity(), buttonLogin);

            // 获取用户信息,不会有空指针
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword1.getText().toString().trim();

            // 验证邮箱是否存在
            isVerifyEmail(email, exists -> {
                if (exists) {
                    // 处理邮箱不存在的情况
                    showError(getString(R.string.error_no_exist_email));
                    // 验证失败,清空输入框内容
                    emptyInput();
                    // 取消进度条，重新出现登录页面
                    linearLayoutMain.setVisibility(View.VISIBLE);
                    linearLayoutProgressIndicator.setVisibility(View.GONE);
                } else {
                    login(email,password);
                }
            });
        });
    }

    /**
     * 用户进行登录，成功就转到主activity，失败进行处理
     * @param email 用户邮箱
     * @param password 用户密码
     */
    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 登录成功,把当前activity向下收回，进入主页  --


                        setFCMToken();

                        // 使用Intent进入MainActivity
                        if(getActivity() != null) {
                            Intent intent = new Intent(getActivity(), PortalActivity.class);
                            startActivity(intent);
                            // 关闭当前Activity,enter页面
                            getActivity().finish();
                        }
                    } else {

                        // 登录失败，展示失败信息，取消掉进度条，让用户重新登录
                        showError(getString(R.string.error_incorrect_password));
                        emptyInput();
                        // 取消进度条，重新出现登录页面
                        linearLayoutMain.setVisibility(View.VISIBLE);
                        linearLayoutProgressIndicator.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 验证邮箱是否已经存在，存在则显示错误信息，不存在则进行下一步操作
     *
     * @param email 用户邮箱
     * @param callback 结果回调，邮箱存在返回false，邮箱不存在返回true
     */
    private void isVerifyEmail(String email, Consumer<Boolean> callback) {
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (result != null && result.getSignInMethods() != null) {
                            callback.accept(result.getSignInMethods().size() == 0);
                        } else {
                            showError(getString(R.string.error_network));
                            emptyInput();
                        }
                    } else {
                        // Handle the error
                        showError("An error occurred.");
                        emptyInput();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    showError(e.getMessage());
                    emptyInput();
                });
    }

    /**
     * 显示错误提醒
     *
     * @param errorMessage 错误的提示信息
     */
    private void showError(String errorMessage) {
        // 在这里显示错误提示，可以使用Toast或者TextView来显示错误消息
        // 例如，你可以将错误消息显示在一个TextView上，然后将TextView设置为可见
        // 或者使用Toast来显示一个短暂的错误消息
        textViewError.setText(errorMessage);
        textViewError.setVisibility(View.VISIBLE);
    }

    /**
     * 验证错误之后,失去焦点,缩回小键盘,并清空输入框内容
     */
    private void emptyInput() {
        // 清空输入框内容
        if (editTextEmail != null) {
            editTextEmail.setText("");
        }

        // 使输入框失去焦点
        if (editTextEmail != null) {
            editTextEmail.clearFocus();
        }

        // 清空输入框内容
        if (editTextPassword1 != null) {
            editTextPassword1.setText("");
        }

        // 使输入框失去焦点
        if (editTextPassword1 != null) {
            editTextPassword1.clearFocus();
        }

        // 关闭小键盘(不能把代码直接写这里,不然无法关闭)
        hideKeyboard(view);
    }

    /**
     * 隐藏小键盘
     *
     * @param view 当前dialog底层视图
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 重写dismiss方法，使得当对话框消失的时候，按钮可用
     *
     * @param dialog 当前dialog
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // 重新启用登录和注册按钮
        if (getActivity() != null) {
            getActivity().findViewById(R.id.button_login).setEnabled(true);
            getActivity().findViewById(R.id.button_sign).setEnabled(true);
        }
    }

    /**
     *  给每个user设置一个FCM token
     */
    private void setFCMToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("djl", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // 获取对用户文档的引用
                        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());

                        // 或更新现有用户的属性
                        userRef.update("userFCM", token);
                    }
                });

    }
}