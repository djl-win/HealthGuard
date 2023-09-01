package com.comp5216.healthguard.fragment.chat;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.viewmodel.ChatViewModel;
import com.comp5216.healthguard.viewmodel.RelationShipViewModel;
import com.comp5216.healthguard.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * 聊天界面的fragment
 * <p>
 * 聊天界面的逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-09-01
 */
public class MessageFragment extends DialogFragment {
    // 页面底层view组件
    View view;
    // 回退按钮
    ImageButton buttonBack;
    // 用户的view model
    UserViewModel userViewModel;
    // 好友聊天信息的view model
    ChatViewModel chatViewModel;
    // 与当前用户进行对话的好友名称
    TextView textViewUsername;
    // 用户的输入框
    EditText editTextContent;
    // 发送信息按钮
    ImageButton buttonSend;
    // 用户要发送的聊天信息
    Chat chatMessage;
    // firebase的auth
    FirebaseAuth auth;
    // firebase user
    FirebaseUser firebaseUser;
    // 用户的uid
    String userUid;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 设置对话框的为全屏，因为系统默认的对话框是浮动的，无法全屏，这里去style文件里修改到全屏，并取消浮动
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyleAndAnimation01);

        // 获取的Dialog实例
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // 将对话框的布局文件转化为页面底层view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, null);

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
        buttonBack = view.findViewById(R.id.button_back_message);
        // 初始化用户的view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // 初始化用户聊天信息的view model
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        // 绑定用户姓名组件
        textViewUsername = view.findViewById(R.id.text_view_username_message);
        // 绑定用户的输入框组件
        editTextContent = view.findViewById(R.id.edit_text_content_message);
        // 绑定发送信息按钮
        buttonSend = view.findViewById(R.id.button_send_message);
        // 初始化用户要发送的聊天信息
        chatMessage = new Chat();
        // 初始化firebase auth
        auth = FirebaseAuth.getInstance();
        // 初始化firebase user
        firebaseUser = auth.getCurrentUser();
        // 初始化用户的UID
        userUid = firebaseUser.getUid();

    }

    /**
     * 为组件添加监听器
     */
    private void setListeners() {
        // 回退按钮监听
        buttonBackListener();
        // 绑定与当前用户对话的好友信息
        bindCurrentFriend();
        // 发送按钮监听
        buttonSendListener();
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
     * 绑定与当前用户对话的好友信息,并更新用户名到上面
     */
    private void bindCurrentFriend() {
        // 但在DialogFragment的onCreateDialog方法中，getViewLifecycleOwner()还不可用，因为视图还没有被创建。在这种情况下，如果您想立即开始观察LiveData，您只能使用this（代表DialogFragment本身）
        // userViewModel.getChatFriendLiveData().observe(getViewLifecycleOwner(),user ->{
        // textViewUsername.setText(user.getUserName());
        //  });
        userViewModel.getChatFriendLiveData().observe(this,user ->{
            textViewUsername.setText(user.getUserName());
            // 把当前聊天窗口的id传到message里
            chatMessage.setChatId(user.getChatId());
            // 把当前的发送消息的人的ID存进message
            chatMessage.setChatMessageSenderID(userUid);
        });
    }

    /**
     * 发送信息按钮监听器
     */
    private void buttonSendListener() {

        buttonSend.setOnClickListener(view ->{
            // 把当前聊天窗口的信息传到message里
            chatMessage.setChatMassageText(editTextContent.getText().toString());
            // 把当前的发送消息的时间存进message
            long currentTimestamp = System.currentTimeMillis();
            chatMessage.setChatMessageTimestamp(String.valueOf(currentTimestamp));
            // 将发送的聊天信息存到数据库
            chatViewModel.insertMessage(chatMessage);
            // 把聊天框字清除
            editTextContent.setText("");

        });
    }
}