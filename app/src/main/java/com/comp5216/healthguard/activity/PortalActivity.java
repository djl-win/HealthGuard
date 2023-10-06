package com.comp5216.healthguard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.fragment.chat.ChatFragment;
import com.comp5216.healthguard.fragment.index.IndexFragment;
import com.comp5216.healthguard.fragment.notify.NotifyFragment;
import com.comp5216.healthguard.fragment.search.SearchFragment;
import com.comp5216.healthguard.fragment.setting.SettingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class PortalActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_indexIcon;
    private ImageView iv_searchIcon;
    private ImageView iv_notifyIcon;
    private ImageView iv_chatIcon;
    private ImageView iv_settingIcon;
    private Animation animation;
    private FirebaseFirestore db;
    private String user_id;

    private ChatFragment chatFragment;

    private IndexFragment indexFragment;

    private NotifyFragment notifyFragment;

    private SearchFragment searchFragment;

    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
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
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_menu);

        db = FirebaseFirestore.getInstance();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chatFragment = new ChatFragment();
        indexFragment = new IndexFragment();
        notifyFragment = new NotifyFragment();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_index, chatFragment)
                .add(R.id.fl_index, indexFragment)
                .add(R.id.fl_index, notifyFragment)
                .add(R.id.fl_index, settingFragment)
                .add(R.id.fl_index, searchFragment)
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(notifyFragment)
                .hide(settingFragment)
                .show(indexFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v == iv_indexIcon) {
            showIndex();
        } else if (v == iv_searchIcon) {
            showSearch();
        } else if (v == iv_notifyIcon) {
            showNotify();
        } else if (v == iv_chatIcon) {
            showChat();
        } else if (v == iv_settingIcon) {
            showSetting();
        }
    }

    private void showIndex() {
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_indexIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(indexFragment)
                .commit();
    }

    private void showSearch() {
        iv_indexIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_searchIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(indexFragment)
                .hide(chatFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(searchFragment)
                .commit();
    }

    private void showNotify() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_notifyIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(indexFragment)
                .show(notifyFragment)
                .commit();
    }

    private void showChat() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_chatIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(indexFragment)
                .hide(searchFragment)
                .hide(settingFragment)
                .hide(notifyFragment)
                .show(chatFragment)
                .commit();
    }

    private void showSetting() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_chatIcon.clearAnimation();
        iv_settingIcon.startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .hide(chatFragment)
                .hide(searchFragment)
                .hide(indexFragment)
                .hide(notifyFragment)
                .show(settingFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}