package com.comp5216.healthguard.fragment.enter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.obj.entity.User;
import com.comp5216.healthguard.util.CustomAnimationUtil;
import com.comp5216.healthguard.viewmodel.AttributeViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

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
    // 邮箱框的layout
    TextInputLayout textInputEmail;
    // 密码框的layout
    TextInputLayout textInputPassword1;
    // 确认密码框的layout
    TextInputLayout textInputPassword2;
    // 错误提醒
    TextView textViewError;
    // firebase的认证实例
    FirebaseAuth auth;
    // 要注册的user
    User userNew;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;
    // 整个页面的layout
    LinearLayout linearLayoutMain;
    // 注册进度条
    LinearLayout linearLayoutProgressIndicator;
    // 用户的viewModel
    UserViewModel userViewModel;
    // 用户预警信息的viewModel
    AttributeViewModel attributeViewModel;


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
        // 绑定要加入数据库的user数据
        userNew = new User();
        // 绑定回退按钮
        buttonBack = view.findViewById(R.id.button_back_sign);
        // 绑定继续按钮
        buttonContinue = view.findViewById(R.id.button_continue_sign);
        // 绑定邮箱输入框
        editTextEmail = view.findViewById(R.id.text_edit_email_sign);
        // 绑定姓名输入框
        editTextName = view.findViewById(R.id.text_edit_name_sign);
        // 绑定密码输入框
        editTextPassword1 = view.findViewById(R.id.text_edit_password1_sign);
        // 绑定确认密码输入框
        editTextPassword2 = view.findViewById(R.id.text_edit_password2_sign);
        // 绑定性别选择框
        editTextGender = view.findViewById(R.id.text_edit_gender_sign);
        editTextGender.setAdapter(adapter);
        // 绑定邮箱框的layout
        textInputEmail = view.findViewById(R.id.text_input_email_sign);
        // 绑定密码框的layout
        textInputPassword1 = view.findViewById(R.id.text_input_password1_sign);
        // 绑定确认密码框的layout
        textInputPassword2 = view.findViewById(R.id.text_input_password2_sign);
        // 绑定错误提醒
        textViewError = view.findViewById(R.id.text_view_error_sign);
        // 绑定整个页面的layout
        linearLayoutMain = view.findViewById(R.id.linear_layout_main_sign);
        // 绑定注册进度条页面的layout
        linearLayoutProgressIndicator = view.findViewById(R.id.linear_layout_progress_indicator_sign);
        // 绑定用户的view model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // 绑定用户预警信息的view model
        attributeViewModel = new ViewModelProvider(this).get(AttributeViewModel.class);
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
        // 1.检查所有输入框是否有内容，如果有的输入框没有内容，则禁用登录按钮
        // 2.检查用户邮箱输入格式是否正确，如果不正确，则禁用按钮
        // 3.检查用户输入的密码是否符合格式，如果不符合则禁用按钮
        // 4.检查用户输入的两次密码是否一致，如果不一致则禁用按钮
        if(checkEditTexts(editTextEmail.getText())
                && checkEditTexts(editTextName.getText())
                && checkEditTexts(editTextPassword1.getText())
                && checkEditTexts(editTextPassword2.getText())
                && checkEditTexts(editTextGender.getText())
                && isValidEmail(editTextEmail.getText().toString().trim())
                && isValidPassword(editTextPassword1.getText().toString().trim())
                && isConsistentPassword(editTextPassword2.getText().toString().trim())
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

        // 检查用户输入的密码是否符合格式，如果不符合，给出提示
        if (!isValidPassword(editTextPassword1.getText().toString().trim())){
            // 修改密码框的样式，重点提醒
            textInputPassword1.setErrorEnabled(true);
            textInputPassword1.setError(getString(R.string.error_illegal_password1));
        }else if (isValidPassword(editTextPassword1.getText().toString().trim())){
            // 修改密码框的样式，取消提醒
            textInputPassword1.setError(null);
            textInputPassword1.setErrorEnabled(false);
        }

        // 检查用户输入的两次密码是否一致，如果不一致，给出提示
        if(!isConsistentPassword(editTextPassword2.getText().toString().trim())){
            // 修改密码框的样式，重点提醒
            textInputPassword2.setErrorEnabled(true);
            textInputPassword2.setError(getString(R.string.error_illegal_password2));
        }else if (isConsistentPassword(editTextPassword2.getText().toString().trim())){
            // 修改密码框的样式，取消提醒
            textInputPassword2.setError(null);
            textInputPassword2.setErrorEnabled(false);
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

            // 获取用户信息,不会有空指针
            String email = editTextEmail.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String password = editTextPassword1.getText().toString().trim();
            String gender = editTextGender.getText().toString().trim();
            userNew.setUserEmail(email);
            userNew.setUserName(name);
            userNew.setUserPassword(password);
            userNew.setUserGender(gender);

            // 验证邮箱是否存在，之后验证连接发送，打开新的dialog，进行密码等操作的设置
            isVerifyEmail(email, exists -> {
                if (exists) {
                    // 处理邮箱不存在的情况
                    createAccount(userNew);
                } else {
                    // 邮箱已经存在，显示错误提示
                    showError(getString(R.string.error_exist_email));
                    // 验证失败,清空输入框内容
                    emptyInput();
                }
            });
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
     * 判断用户输入的第二次密码是否与第一次一致
     * @param conformPassword 用户输入的确认密码
     * @return 符合与否
     */
    public boolean isConsistentPassword(String conformPassword) {
        if(Objects.equals(conformPassword, "")){
            return true;
        }
        return conformPassword.equals(editTextPassword1.getText().toString().trim());
    }

    /**
     * 验证用户的邮箱
     *
     * @param userNew 新添加的user到fireStore
     */
    private void createAccount(User userNew) {
        // 加载进度条
        linearLayoutMain.setVisibility(View.GONE);
        linearLayoutProgressIndicator.setVisibility(View.VISIBLE);
        // 创建新用户到fire auth
        auth.createUserWithEmailAndPassword(userNew.getUserEmail(), userNew.getUserPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 注册成功，将用户的数据加密之后存储到数据库,将firebase的auth中的uid设置为User表中的userId
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            userNew.setUserId(uid);
                        }
                        storeUser(userNew);
                    } else {
                        // 处理未知错误
                        showError(getString(R.string.error_network));
                        // 验证失败,清空输入框内容
                        emptyInput();
                    }
                });
    }

    /**
     * 加密之后，存储用户信息到数据库
     * @param userNew 用户的数据
     */
    private void storeUser(User userNew) {

        // 将用户预警信息存到数据库
        attributeViewModel.storeAttribute(userNew);
        // 加密数据,并将用户信息存到数据库
        userViewModel.storeUser(userNew,
                aVoid -> {
                    // 注册成功,当前dialog关闭
                    dismiss();
                    // 提醒用户
                    Toast.makeText(getActivity(), "Congratulations！", Toast.LENGTH_SHORT).show();
                },
                e -> {
                    // 取消进度条
                    linearLayoutMain.setVisibility(View.VISIBLE);
                    linearLayoutProgressIndicator.setVisibility(View.GONE);
                    // 处理错误
                    showError(getString(R.string.error_network));
                    // 清空输入框内容
                    emptyInput();
                }
        );
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
}