package com.comp5216.healthguard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.NotifyListAdapter;
import com.comp5216.healthguard.fragment.portal.ChatFragment;
import com.comp5216.healthguard.fragment.portal.IndexFragment;
import com.comp5216.healthguard.fragment.portal.NotifyFragment;
import com.comp5216.healthguard.fragment.portal.SearchFragment;
import com.comp5216.healthguard.fragment.portal.SettingFragment;
import com.comp5216.healthguard.obj.SPConstants;
import com.comp5216.healthguard.obj.portal.Notification;
import com.comp5216.healthguard.obj.portal.SendNotificationRefreshEvent;
import com.comp5216.healthguard.service.NotifyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Map;


public class PortalActivity extends AppCompatActivity implements View.OnClickListener {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ImageView iv_indexIcon;
    private ImageView iv_searchIcon;
    private ImageView iv_notifyIcon;
    private ImageView iv_chatIcon;
    private ImageView iv_settingIcon;
    private Animation animation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        //TEST SERVICE
        Intent intent_notify_service = new Intent(this, NotifyService.class);
        startService(intent_notify_service);
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
        // 预加载notice size
        getDefaultNoticeSize();
        // 初始化Notify
        showIndex();
    }

    private void getDefaultNoticeSize() {
        CollectionReference notifyRef = db.collection("notification");
        notifyRef.whereEqualTo("user_id",user_id)
                .whereEqualTo("notification_read_status","0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int sum = 0;
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                if (!documentSnapshot.getData().get("notification_type").equals("4")
                                && documentSnapshot.getData().get("notification_delete_status").equals("0")){
                                    sum++;
                                }
                            }
                            SPUtils.getInstance().put(SPConstants.NOTIFICATION_SIZE,task.getResult().size());
                            SPUtils.getInstance().put(SPConstants.NOTIFICATION_LIST_SIZE,sum);
                            LogUtils.e(SPUtils.getInstance().getInt(SPConstants.NOTIFICATION_LIST_SIZE));
                        }
                    }
                });
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
    }

    private void showChat() {
        iv_indexIcon.clearAnimation();
        iv_searchIcon.clearAnimation();
        iv_notifyIcon.clearAnimation();
        iv_settingIcon.clearAnimation();
        iv_chatIcon.startAnimation(animation);
        replaceFragment(new ChatFragment());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TEST SERVICE
        Intent intent_notify_service = new Intent(this,NotifyService.class);
        stopService(intent_notify_service);
    }
}