package com.comp5216.healthguard.fragment.portal;

import android.os.Bundle;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.adapter.NotifyListAdapter;
import com.comp5216.healthguard.obj.portal.Notification;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initView() {
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notificationList.add(new Notification("1","1","Time to eat ibuprofen","2023-08-25 00:00","1","1","1"));
        notifyListAdapter = new NotifyListAdapter(notificationList,getContext());
        listView = getView().findViewById(R.id.lv_notify);
        listView.setAdapter(notifyListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        listView = container.findViewById(R.id.lv_notify);
//        listView.setAdapter(notifyListAdapter);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify, container, false);
    }
}