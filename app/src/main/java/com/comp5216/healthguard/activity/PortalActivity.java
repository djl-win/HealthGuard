package com.comp5216.healthguard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.fragment.portal.ChatFragment;
import com.comp5216.healthguard.fragment.portal.IndexFragment;
import com.comp5216.healthguard.fragment.portal.NotifyFragment;
import com.comp5216.healthguard.fragment.portal.SearchFragment;
import com.comp5216.healthguard.fragment.portal.SettingFragment;
import com.comp5216.healthguard.service.NotifyService;


public class PortalActivity extends AppCompatActivity implements View.OnClickListener {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ImageView iv_indexIcon;
    private ImageView iv_searchIcon;
    private ImageView iv_notifyIcon;
    private ImageView iv_chatIcon;
    private ImageView iv_settingIcon;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
//        Button button = findViewById(R.id.button_logout);
//        button.setOnClickListener( view ->{
//
//            db.collection("users").document("lSX43pBjbxWyOGRZwI648xbzIVf2")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
//                                } else {
//                                    Log.d("Firestore", "No such document");
//                                }
//                            } else {
//                                Log.d("Firestore", "get failed with ", task.getException());
//                            }
//                        }
//                    });
//            auth.signOut();
//
//            // 跳转回登录页面
//            startActivity(new Intent(PortalActivity.this, EnterActivity.class));
//            finish();
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }


    private void initView() {
        iv_indexIcon = findViewById(R.id.iv_indexIcon);
        iv_searchIcon = findViewById(R.id.iv_searchIcon);
        iv_notifyIcon = findViewById(R.id.iv_notifyIcon);
        iv_chatIcon = findViewById(R.id.iv_chatIcon);
        iv_settingIcon = findViewById(R.id.iv_settingIcon);
        iv_indexIcon.setOnClickListener(this);
        iv_searchIcon.setOnClickListener(this);
        iv_notifyIcon.setOnClickListener(this);
        iv_chatIcon.setOnClickListener(this);
        iv_settingIcon.setOnClickListener(this);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_menu);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_indexIcon){
            showIndex();
        }else if (v == iv_searchIcon){
            showSearch();
        }else if (v == iv_notifyIcon){
            showNotify();
        }else if (v == iv_chatIcon){
            showChat();
        }else if (v == iv_settingIcon){
            showSetting();
        }
    }

    private void showIndex() {
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_indexIcon.startAnimation(animation);
        replaceFragment(new IndexFragment());
    }

    private void showSearch() {
        iv_indexIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_searchIcon.startAnimation(animation);
        replaceFragment(new SearchFragment());
    }

    private void showNotify() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_notifyIcon.startAnimation(animation);
        replaceFragment(new NotifyFragment());
        //TODO TEST SERVICE
        Intent intent_notify_service = new Intent(this, NotifyService.class);
        startService(intent_notify_service);
    }

    private void showChat() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_chatIcon.startAnimation(animation);
        replaceFragment(new ChatFragment());
        //TODO TEST SERVICE
        Intent intent_notify_service = new Intent(this,NotifyService.class);
        stopService(intent_notify_service);
    }

    private void showSetting() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.startAnimation(animation);
        replaceFragment(new SettingFragment());
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_index,fragment)
                .commit();
    }

}