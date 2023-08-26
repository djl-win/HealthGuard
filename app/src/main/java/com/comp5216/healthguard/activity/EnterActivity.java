package com.comp5216.healthguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.fragment.enter.LoginFragment;
import com.comp5216.healthguard.fragment.enter.SignFragment;
import com.comp5216.healthguard.util.CustomAnimationUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    // firebase的认证实例
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        // 初始化获取xml中组件
        init();
        // 给页面组件设置监听器
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 判断用户是否已经登录，如果未登录，则进入登录页面，如果已经登录，进入主页面
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(EnterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        // 获取firebase认证的实例
        auth = FirebaseAuth.getInstance();
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
            loginFragment.show(getSupportFragmentManager(),"LoginFragment");
        });
    }

}