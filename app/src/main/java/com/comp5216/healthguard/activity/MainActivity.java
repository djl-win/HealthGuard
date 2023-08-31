package com.comp5216.healthguard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.fragment.chat.ChatFragment;
import com.comp5216.healthguard.viewmodel.RelationShipViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    UserViewModel userViewModel;
    RelationShipViewModel relationShipViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在此activity中初始化所有view model,之后所有的fragment都可以通过userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);获取同一个view model
        // 初始化用户的view model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // 初始化好友关系的view model
        relationShipViewModel = new ViewModelProvider(this).get(RelationShipViewModel.class);

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button_logout);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        button.setOnClickListener( view ->{

            auth.signOut();

            // 跳转回登录页面
            startActivity(new Intent(MainActivity.this, EnterActivity.class));
            finish();

        });

        ChatFragment chatFragment = new ChatFragment();
        // 首先将Fragment添加到FragmentManager，并隐藏chatFragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, chatFragment)
                .hide(chatFragment)
                .commit();

        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.page_2) {
                button.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .show(chatFragment)
                        .commit();
            }
            if(item.getItemId() == R.id.page_1){
                button.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .hide(chatFragment)
                        .commit();
            }
            return true;  // 返回true表示已处理选择事件
        });
    }
}