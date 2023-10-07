package com.comp5216.healthguard.fragment.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.util.CustomAnimationUtil;
import com.comp5216.healthguard.viewmodel.RelationShipViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

/**
 * 添加好友界面的fragment
 * <p>
 * 添加好友界面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-28
 */
public class AddFriendsFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // title
    TextView textViewTitle;
    // 邮箱输入框
    TextInputEditText editTextEmail;
    // 邮箱输入框layout
    TextInputLayout textInputEmail;
    // 验证码输入框
    TextInputEditText editTextVerification;
    // 验证码输入框的layout
    TextInputLayout textInputVerification;
    // 继续按钮
    Button buttonContinue;
    // 输入框内容监听器
    TextWatcher generalTextWatcher;
    // 用于判断是否进入输入验证码的界面
    Boolean verificationPageFlag = false;
    // 整个页面的layout
    LinearLayout linearLayoutMain;
    // 注册进度条
    LinearLayout linearLayoutProgressIndicator;
    // 错误提醒文字
    TextView textViewError;
    // firebase的认证实例
    FirebaseAuth auth;
    // 用户的viewModel
    UserViewModel userViewModel;
    // 好友关系的viewModel
    RelationShipViewModel relationshipViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_friends, null);

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
        buttonBack = view.findViewById(R.id.button_back_add_friends);
        // 绑定title
        textViewTitle = view.findViewById(R.id.text_view_title_add_friends);
        // 绑定邮箱输入框
        editTextEmail = view.findViewById(R.id.text_edit_email_add_friends);
        // 绑定邮箱输入框的layout
        textInputEmail = view.findViewById(R.id.text_input_email_add_friends);
        // 绑定验证码输入框
        editTextVerification = view.findViewById(R.id.text_edit_verification_add_friends);
        // 绑定验证码输入框的layout
        textInputVerification = view.findViewById(R.id.text_input_verification_add_friends);
        // 绑定继续按钮
        buttonContinue = view.findViewById(R.id.button_continue_add_friends);
        // 绑定整个页面的layout
        linearLayoutMain = view.findViewById(R.id.linear_layout_main_add_friends);
        // 绑定注册进度条页面的layout
        linearLayoutProgressIndicator = view.findViewById(R.id.linear_layout_progress_indicator_add_friends);
        // 绑定错误提醒
        textViewError = view.findViewById(R.id.text_view_error_add_friends);
        // 获取firebase认证的实例
        auth = FirebaseAuth.getInstance();
        // 绑定用户的view model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // 绑定好友关系的viewModel
        relationshipViewModel = new ViewModelProvider(this).get(RelationShipViewModel.class);

    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 继续按钮监听器
        buttonContinueListener();
        // 初始化输入框内容监听器
        initTextWatcher();
        // 所有输入框内容输入框监听
        editTextEmail.addTextChangedListener(generalTextWatcher);
        editTextVerification.addTextChangedListener(generalTextWatcher);
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
     * 继续按钮监听器
     */
    private void buttonContinueListener() {
        buttonContinue.setOnClickListener(view -> {
            // 加载进度条
            showLoadingAnim();
            // 收起小键盘
            hideKeyboard(view);
            // 开始按钮动画,这里使用自定义按钮动画
            CustomAnimationUtil.ButtonsAnimation(getActivity(), buttonContinue);
            // 邮箱
            String email = editTextEmail.getText().toString().trim();
            // 验证码
            String verification = editTextVerification.getText().toString().trim();

            // 如果是在输入邮箱的界面执行这个逻辑
            if (!verificationPageFlag) {

                //验证邮箱是否存在，存在进入下个验证页面，不存在，返回并给出错误提醒
                isVerifyEmail(email, exists -> {
                    if (exists) {
                        // 处理邮箱不存在的情况
                        showError(getString(R.string.error_no_find_user));
                        // 验证失败,清空输入框内容
                        editTextEmail.setText("");
                        // 取消进度条，重新出现页面
                        hideLoadingAnim();
                    } else {
                        // 更改标题
                        textViewTitle.setText(R.string.enter_your_verification_code);
                        // 取消掉错误提醒
                        hideError();
                        // 判断进入第二个验证页面
                        verificationPageFlag = true;
                        // 使得继续按钮不可以用
                        unableButtons();
                        // 消失掉第一个页面的内容
                        textInputEmail.setVisibility(View.GONE);
                        // 出现第二个页面内容
                        textInputVerification.setVisibility(View.VISIBLE);
                        // 取消进度条，重新出现页面
                        hideLoadingAnim();
                    }
                });
            }
            // 如果是在输入验证码的界面执行这个逻辑
            else {
                // 验证是不是自己添加自己为好友，如果是的话就返回false
                if (verification.equals(auth.getCurrentUser().getUid())) {
                    // 提醒用户,长时间
                    Toast.makeText(getActivity(), getString(R.string.error_wrong_add_self), Toast.LENGTH_LONG).show();
                    // 处理验证码输入错误的情况
                    dismiss();
                } else {
                    // 验证用户是否输入了正确的验证码，验证成功，添加两个人的聊天框到chat界面，添加失败，重新输入
                    userViewModel.verifyUserByEmail(email, verification, exist -> {
                        if (exist) {
                            // 添加两个人的关系到数据库
                            Relationship relationship = new Relationship();
                            relationship.setRelationshipObserveId(auth.getCurrentUser().getUid());
                            relationship.setRelationshipObservedId(verification);
                            // 判断是否添加成功
                            relationshipViewModel.storeRelationShip(relationship, flag->{
                                if(flag){
                                    dismiss();
                                }else {
                                    // 提醒用户,长时间
                                    Toast.makeText(getActivity(), getString(R.string.error_already_added), Toast.LENGTH_LONG).show();
                                    // 处理添加好友失败的情况
                                    dismiss();
                                }
                            });
                        } else {
                            // 处理验证码输入错误的情况
                            showError(getString(R.string.error_wrong_verification));
                            // 验证失败,清空输入框内容
                            editTextVerification.setText("");
                            // 取消进度条，重新出现页面
                            hideLoadingAnim();
                        }
                    });
                }
            }
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
        if((!verificationPageFlag && isValidEmail(editTextEmail.getText().toString().trim()) && checkEditTexts(editTextEmail.getText())) || checkEditTexts(editTextVerification.getText())){
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
                            // 清空输入框内容
                            editTextEmail.setText("");
                        }
                    } else {
                        // Handle the error
                        showError(getString(R.string.error_network));
                        // 清空输入框内容
                        editTextEmail.setText("");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    showError(e.getMessage());
                    // 清空输入框内容
                    editTextEmail.setText("");
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
     * 出现加载动画
     */
    private void showLoadingAnim() {
        linearLayoutMain.setVisibility(View.GONE);
        linearLayoutProgressIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * 消失加载动画
     */
    private void hideLoadingAnim() {
        linearLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutProgressIndicator.setVisibility(View.GONE);
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
        // 恢复按钮,并且使得chat fragment的recycle view变为白色，不然颜色会变化
        if (getActivity() != null) {
            getActivity().findViewById(R.id.button_add_friends_chat).setEnabled(true);
        }
        // 去除所有输入的内容
        verificationPageFlag = false;
        editTextEmail.setText("");
        editTextVerification.setText("");

    }

}