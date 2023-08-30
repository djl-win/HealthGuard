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
import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.entity.User;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 好友列表的的adapter
 * <p>
 * 渲染好友列表，并处理逻辑
 * </p>
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ChatViewHolder> {

    List<User> users;
    Context context;

    public FriendsListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item,viewGroup,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        initItemView(holder,position);
    }


    /**
     * 初始化每个item中的数据，
     * @param holder ChatViewHolder
     * @param position 列表中item的position
     */
    private void initItemView(@NonNull ChatViewHolder holder, int position){
        String avatarUrl = "https://api.dicebear.com/6.x/bottts-neutral/png?seed="+users.get(position).getUserName();
        // 加载用户头像到xml
        Glide.with(context)
                .load(avatarUrl)
                .transform(new RoundedCorners(20))
                .error(R.drawable.load_image)
                .into(holder.imageViewAvatar);
        holder.textViewUsername.setText(users.get(position).getUserName());
    }

    /**
     * 更新recycle view中的数据
     * @param newUsers 传入的新user
     */
    public void updateData(List<User> newUsers) {
        this.users.clear();
        this.users.addAll(newUsers);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        // 头像组件
        ImageView imageViewAvatar;
        // 用户的姓名
        TextView textViewUsername;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar_chat_item);
            textViewUsername = itemView.findViewById(R.id.text_view_username_chat_item);
        }

    }
}


