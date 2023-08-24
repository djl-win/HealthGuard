package com.comp5216.healthguard.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.fragment.enter.LoginFragment;
import com.comp5216.healthguard.fragment.enter.SignFragment;
import com.comp5216.healthguard.util.CustomAnimationUtil;

/**
 * 进入页面的Activity
 * <p>
 * 进入页面的逻辑
 * </p>
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class EnterActivity extends AppCompatActivity {


    // 注册按钮组件
    Button buttonSign;
    // 登录按钮组件
    Button buttonLogin;
    // 注册的fragment
    SignFragment signFragment;
    // 登录的fragment
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        // 初始化获取xml中组件
        init();
        // 给页面组件设置监听器
        setListeners();
    }

    /**
     * 获取xml中组件
     */
    private void init(){
        // 创建Sign Fragment实例
        signFragment = new SignFragment();
        // 创建Login Fragment实例
        loginFragment = new LoginFragment();
        // 绑定注册按钮
        buttonSign = findViewById(R.id.button_sign);
        // 绑定登录按钮
        buttonLogin = findViewById(R.id.button_login);
    }

    /**
     * 为组件添加监听器
     */
    private void setListeners(){
        // 注册按钮监听
        ButtonSignListener();
        // 登录按钮监听
        ButtonLoginListener();
    }

    /**
     * 注册按钮监听器
     */
    private void ButtonSignListener(){

        buttonSign.setOnClickListener(view ->{
            // 用户点击之后，禁用注册按钮，和登录按钮，防止用户在加载动画时重复点击按钮
            buttonSign.setEnabled(false);
            buttonLogin.setEnabled(false);
            // 开始按钮动画,这里使用自定义按钮动画
            CustomAnimationUtil.ButtonsAnimation(EnterActivity.this,buttonSign);
            // 加载注册的fragment
            signFragment.show(getSupportFragmentManager(),"SignFragment");
        });

    }

    /**
     * 登录按钮监听器
     */
    private void ButtonLoginListener(){

        buttonLogin.setOnClickListener(view ->{
            // 用户点击之后，禁用注册按钮，和登录按钮，防止用户在加载动画时重复点击按钮
            buttonSign.setEnabled(false);
            buttonLogin.setEnabled(false);
            // 开始按钮动画,这里使用自定义按钮动画
            CustomAnimationUtil.ButtonsAnimation(EnterActivity.this,buttonLogin);
            // 加载登录的fragment
//            loginFragment.show(getSupportFragmentManager(),"LoginFragment");
        });
    }

}