package com.comp5216.healthguard.fragment.enter;

import static androidx.appcompat.content.res.AppCompatResources.getColorStateList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.util.CustomAnimationUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注册界面的fragment
 * <p>
 * 注册界面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class SignFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // 继续按钮
    Button buttonContinue;
    // 邮箱输入框
    TextInputEditText editTextEmail;
    // 姓名输入框
    TextInputEditText editTextName;
    // 密码输入框
    TextInputEditText editTextPassword1;
    // 确认密码输入框
    TextInputEditText editTextPassword2;
    // 下拉框的适配器
    ArrayAdapter<String> adapter;
    // 下拉框中的数据
    String[] items = {"Male", "Female", "Other"};
    // 下拉框
    AutoCompleteTextView editTextGender;
    // 密码框的layout
    TextInputLayout textInputPassword1;
    // 确认密码框的layout
    TextInputLayout textInputPassword2;
    // 错误提醒
    TextView textViewError;
    // firebase的认证实例
    FirebaseAuth auth;
    // 当前用户
    FirebaseUser user;
    // 要注册的user
    User userNew;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sign, null);

        // 设置Dialog的内容视图
        dialog.setContentView(view);

        // 设置软键盘模式，当键盘弹出时调整布局
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        }

        // 初始化adapter
        initAdapter();
        // 初始化获取xml中组件
        init(view);
        // 给页面组件设置监听器
        setListeners();

        // 返回设置好的Dialog实例
        return dialog;
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {

        // 渲染性别的下拉列表
        if(getActivity() != null) {
            adapter = new ArrayAdapter<>(
                    getActivity(),
                    R.layout.dropdown_list_item,
                    items
            );
        }
    }

    /**
     * 获取xml中组件
     *
     * @param view 页面底层view组件
     */
    private void init(View view) {
        // 获取firebase认证的实例
        auth = FirebaseAuth.getInstance();
        // 获取firebase当前用户
        user = auth.getCurrentUser();
        // 绑定要加入数据库的user数据
        userNew = new User();
        // 绑定回退按钮
        buttonBack = view.findViewById(R.id.button_back);
        // 绑定继续按钮
        buttonContinue = view.findViewById(R.id.button_continue);
        // 绑定邮箱输入框
        editTextEmail = view.findViewById(R.id.text_edit_email);
        // 绑定姓名输入框
        editTextName = view.findViewById(R.id.text_edit_name);
        // 绑定密码输入框
        editTextPassword1 = view.findViewById(R.id.text_edit_password1);
        // 绑定确认密码输入框
        editTextPassword2 = view.findViewById(R.id.text_edit_password2);
        // 绑定性别选择框
        editTextGender = view.findViewById(R.id.text_edit_gender);
        editTextGender.setAdapter(adapter);
        // 绑定密码框的layout
        textInputPassword1 = view.findViewById(R.id.text_input_password1);
        // 绑定确认密码框的layout
        textInputPassword2 = view.findViewById(R.id.text_input_password2);
        // 绑定错误提醒
        textViewError = view.findViewById(R.id.text_view_error);
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
        editTextName.addTextChangedListener(generalTextWatcher);
        editTextPassword1.addTextChangedListener(generalTextWatcher);
        editTextPassword2.addTextChangedListener(generalTextWatcher);
        editTextGender.addTextChangedListener(generalTextWatcher);
        // 监听性别输入框的触碰事件，在此收起小键盘
        editTextGenderIntouchListener();
        // 继续按钮监听
        buttonContinueListener();
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
        // 检查每个输入框的内容
        if(checkEditTexts(editTextEmail.getText())
                && checkEditTexts(editTextName.getText())
                && checkEditTexts(editTextPassword1.getText())
                && checkEditTexts(editTextPassword2.getText())
                && checkEditTexts(editTextGender.getText())
        ){
            // 当EditText有内容时，恢复ImageButton原色
            recoverButtons();
        }
        // 密码输入不符合格式时进行检测
        else if (!isValidPassword(editTextPassword1.getText().toString().trim())){
            // 修改密码框的样式，重点提醒
            textInputPassword1.setErrorEnabled(true);
            textInputPassword1.setError(getString(R.string.error_invalid_password));
            // 显示错误信息
            showError(getString(R.string.error_illegal_password));
            unableButtons();
        }else if (isValidPassword(editTextPassword1.getText().toString().trim())){
            // 修改密码框的样式，取消提醒
            textInputPassword1.setError(null);
            textInputPassword1.setErrorEnabled(false);
            hideError();
        } else {
            // 当EditText没有内容时，设置SVG的颜色为灰色
            unableButtons();
            }
    }

    /**
     * 禁用按钮
     */
    private void unableButtons(){
        buttonContinue.setEnabled(false);
        buttonContinue.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.gray)));
    }

    /**
     * 回复按钮
     */
    private void recoverButtons(){
        buttonContinue.setEnabled(true);
        buttonContinue.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.medium_gray_blue)));
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
     * 下拉列表监听器，当下拉列表打开时收起小键盘
     */
    @SuppressLint("ClickableViewAccessibility")
    private void editTextGenderIntouchListener() {
        editTextGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard(v);
                    // 设置延迟，先让小键盘收回再打开下拉框
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editTextGender.showDropDown();
                        }
                    }, 200);  // 200毫秒的延迟
                    return true; // 返回true表示事件已被处理
                }
                return false;
            }
        });
    }

    /**
     * 继续按钮监听器
     */
    private void buttonContinueListener() {
        buttonContinue.setOnClickListener(view -> {

            // 用户点击之后，禁用继续按钮，和登录按钮，防止用户在加载动画时重复点击按钮
            buttonContinue.setEnabled(false);

            // 开始按钮动画,这里使用自定义按钮动画
            CustomAnimationUtil.ButtonsAnimation(getActivity(), buttonContinue);

            // 获取邮箱,不会有空指针，之前设置了没有字，继续按钮不可用
            String email = editTextEmail.getText().toString().trim();

            // 检查邮箱格式是否正确
            if (!isValidEmail(email)) {
                // 邮箱格式不正确，显示错误提示
                showError(getString(R.string.error_invalid_email));
                // 验证失败,清空输入框内容
                emptyInput();
                return;
            }

            // 验证邮箱是否存在，之后验证连接发送，打开新的dialog，进行密码等操作的设置
            if(!isVerifyEmail(email)){
                // 邮箱已经存在，显示错误提示
                showError(getString(R.string.error_exist_email));
                // 验证失败,清空输入框内容
                emptyInput();
                return;
            }

            // 用户姓名无需验证

            // 验证用户密码，是否符合格式

            // 验证用户再次输入的密码是否和之前一直

            // 下拉框选取性别


            createAccount(userNew);
        });
    }

    /**
     * 检查邮箱格式是否正确
     *
     * @param email 用户输入的邮箱
     * @return 正确与否
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
     * 隐藏错误提醒
     */
    private void hideError() {
        textViewError.setText("");
        textViewError.setVisibility(View.GONE);
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
        if (editTextName != null) {
            editTextName.setText("");
        }

        // 使输入框失去焦点
        if (editTextName != null) {
            editTextName.clearFocus();
        }

        // 清空输入框内容
        if (editTextPassword1 != null) {
            editTextPassword1.setText("");
        }

        // 使输入框失去焦点
        if (editTextPassword1 != null) {
            editTextPassword1.clearFocus();
        }

        // 清空输入框内容
        if (editTextPassword2 != null) {
            editTextPassword2.setText("");
        }

        // 使输入框失去焦点
        if (editTextPassword2 != null) {
            editTextPassword2.clearFocus();
        }

        // 清空输入框内容
        if (editTextGender != null) {
            editTextGender.setText("");
        }

        // 使输入框失去焦点
        if (editTextGender != null) {
            editTextGender.clearFocus();
        }

        // 关闭小键盘(不能把代码直接写这里,不然无法关闭)
        hideKeyboard(view);
    }

    /**
     * 验证邮箱是否已经存在，存在则显示错误信息，不存在则进行下一步操作
     *
     * @param email 用户邮箱
     * @return 邮箱存在返回false，邮箱不存在返回true
     */
    private boolean isVerifyEmail(String email) {
        AtomicBoolean flag = new AtomicBoolean(false);
        // 添加监听器，异步操作
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (result != null && result.getSignInMethods() != null) {
                            if (result.getSignInMethods().size() > 0) {
                                // 验证失败,邮箱已存在，返回false
                                flag.set(false);
                            } else {
                                // 验证成功，邮箱不存在，返回true
                                flag.set(false);
                            }
                        } else {
                            // 出现网络错误
                            showError(getString(R.string.error_network));
                            // 验证失败,清空输入框内容
                            emptyInput();
                        }
                    }
                });
        return flag.get();
    }

    /**
     * 验证密码是否符合规范
     * @param password 用户输入密码
     * @return 符合与否
     */
    public boolean isValidPassword(String password) {
        if(Objects.equals(password, "")){
            return true;
        }
        // 至少有一个大写字母，至少有一个小写字母，至少有一个数字，长度为6个字符及以上
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        return password.matches(regex);
    }

    /**
     * 验证用户的邮箱
     *
     * @param userNew 新添加的user到fireStore
     */
    private void createAccount(User userNew) {
        dismiss();
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

    public void waste(String email) {
        auth.createUserWithEmailAndPassword(email, "temporary_password")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            // 邮件发送成功，打开新的dialog，设置用户的基本信息
                                            showError(getString(R.string.log_in));

                                        } else {
                                            // 邮件发送失败处理失败信息
                                            showError(getString(R.string.error_unsend_email));
                                            // 验证失败,清空输入框内容
                                            emptyInput();

                                        }
                                    });
                        }
                    } else {
                        // 处理未知错误
                        showError(getString(R.string.error_network));
                        // 验证失败,清空输入框内容
                        emptyInput();
                    }
                });
    }
}