package com.comp5216.healthguard.fragment.portal;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.NotifyListAdapter;
import com.comp5216.healthguard.obj.SPConstants;
import com.comp5216.healthguard.obj.portal.Notification;
import com.comp5216.healthguard.obj.portal.SendNotificationRefreshEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotifyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifyFragment newInstance(String param1, String param2) {
        NotifyFragment fragment = new NotifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ListView listView;
    private List<Notification> notificationList = new ArrayList<>();
    private NotifyListAdapter notifyListAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Map<String, String> doc_id = new HashMap<>();

    private ImageView test;

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initView() {
        CollectionReference notifyRef = db.collection("notification");
        if (!notificationList.isEmpty()){
            notificationList.clear();
        }
        notifyRef.whereEqualTo("user_id",user_id)
                .whereEqualTo("notification_read_status","0")
                .whereEqualTo("notification_delete_status","0")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                // ADD LIST
//                                if (!documentSnapshot.get("notification_type").toString().equals("4")){
//                                    Map<String, Object> data = documentSnapshot.getData();
//                                    notificationList.add(new Notification(
//                                            data.get("notification_id").toString(),
//                                            data.get("user_id").toString(),
//                                            data.get("notification_note").toString(),
//                                            data.get("notification_date").toString(),
//                                            data.get("notification_type").toString(),
//                                            data.get("notification_read_status").toString(),
//                                            data.get("notification_delete_status").toString()
//                                    ));
//                                }
//
//                            }
//                            SPUtils.getInstance().put(SPConstants.NOTIFICATION_SIZE,task.getResult().size());
//                            notifyListAdapter = new NotifyListAdapter(notificationList,getContext());
//                            listView = getView().findViewById(R.id.lv_notify);
//                            listView.setAdapter(notifyListAdapter);
//                        }
//                    }
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error == null){
                                if (!notificationList.isEmpty()){
                                    notificationList.clear();
                                }
                                for (QueryDocumentSnapshot documentSnapshot : value){
                                    // ADD LIST
                                    if (!documentSnapshot.get("notification_type").toString().equals("4")){
                                        Map<String, Object> data = documentSnapshot.getData();
                                        doc_id.put(data.get("notification_id").toString(),documentSnapshot.getId());
                                        notificationList.add(new Notification(
                                                data.get("notification_id").toString(),
                                                data.get("user_id").toString(),
                                                data.get("notification_note").toString(),
                                                data.get("notification_date").toString(),
                                                data.get("notification_type").toString(),
                                                data.get("notification_read_status").toString(),
                                                data.get("notification_delete_status").toString()
                                        ));
                                    }

                                }
                                SPUtils.getInstance().put(SPConstants.NOTIFICATION_SIZE,value.size());
                                notifyListAdapter = new NotifyListAdapter(notificationList,doc_id,getContext());
                                listView = getView().findViewById(R.id.lv_notify);
                                listView.setAdapter(notifyListAdapter);
                            }
                        }
                });
        test = getView().findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddTest();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        listView = container.findViewById(R.id.lv_notify);
//        listView.setAdapter(notifyListAdapter);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void doAddTest(){
        CollectionReference notify_add_Ref = db.collection("notification");
        Map<String,Object> new_notify = new HashMap<>();
        new_notify.put("user_id",user_id);
        new_notify.put("notification_date","2023-08-31 15:36");
        new_notify.put("notification_id","test_test");
        new_notify.put("notification_note","Test");
        new_notify.put("notification_type","0");
        new_notify.put("notification_read_status","0");
        new_notify.put("notification_delete_status","0");
        notify_add_Ref.document("test_test").set(new_notify);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SendNotificationRefreshEvent sendNotificationRefreshEvent){
        if (sendNotificationRefreshEvent.getType().equals("send_notification_refresh")){
            if (sendNotificationRefreshEvent.getOrder().equals("notification_refresh")){
                if (isVisible()){
                    this.onResume();
                    LogUtils.e("DO REFRESH SUCCESSFULLY");
                }else {
                    LogUtils.e("FRAGMENT NOT CREATE");
                }
            }
        }
    }
}