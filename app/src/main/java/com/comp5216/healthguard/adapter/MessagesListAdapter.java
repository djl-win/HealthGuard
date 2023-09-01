package com.comp5216.healthguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.entity.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * 聊天框的的adapter
 * <p>
 * 渲染聊天信息列表，并处理逻辑
 * </p>
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-09-01
 */
public class MessagesListAdapter  extends RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder>{

    Context context;
    List<Chat> chats;
    // 消息接收者的姓名
    String receiverName;
    // 消息发送者的姓名
    String senderName;
    String userId = FirebaseAuth.getInstance().getUid();

    public MessagesListAdapter(Context context, List<Chat> chats,String senderName, String receiverName) {
        this.receiverName = receiverName;
        this.senderName = senderName;
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public MessagesListAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_list_item,viewGroup,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesListAdapter.MessageViewHolder holder, int position) {
        initItemView(holder,position);
    }

    /**
     * 初始化每个item中的数据，
     * @param holder ChatViewHolder
     * @param position 列表中item的position
     */
    private void initItemView(@NonNull MessagesListAdapter.MessageViewHolder holder, int position) {
        String avatarUrl;
        if(userId.equals(chats.get(position).getChatMessageSenderID())){
            avatarUrl = "https://api.dicebear.com/6.x/fun-emoji/png?seed=" + senderName;
            // 加载用户头像到xml
        }else {
            avatarUrl = "https://api.dicebear.com/6.x/fun-emoji/png?seed=" + receiverName;
            // 加载用户头像到xml
        }
        Glide.with(context)
                .load(avatarUrl)
                .transform(new RoundedCorners(20))
                .error(R.drawable.load_image)
                .into(holder.imageViewAvatar);

        holder.textViewContent.setText(chats.get(position).getChatMessageText());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    /**
     * 更新recycle view中的数据
     * @param newChats 传入的新chats
     */
    public void updateData(List<Chat> newChats) {
        this.chats.clear();
        this.chats.addAll(newChats);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        // 头像组件
        ImageView imageViewAvatar;
        // 用户的姓名
        TextView textViewContent;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar_message_item);
            textViewContent = itemView.findViewById(R.id.text_view_content_message_item);
        }

    }
}
