package com.comp5216.healthguard.adapter;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.Chat;
import com.comp5216.healthguard.entity.Relationship;
import com.comp5216.healthguard.entity.User;
import com.comp5216.healthguard.entity.UserWithMessage;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 好友列表的的adapter
 * <p>
 * 渲染好友列表，并处理逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ChatViewHolder> {

    // 回调接口，获取单机事件的回调
    public interface ItemClickListener {
        void onItemClick(User user);
    }

    private ItemClickListener itemClickListener;
    List<UserWithMessage> usersWithMessage;
    Context context;


    public FriendsListAdapter(Context context, List<UserWithMessage> usersWithMessage) {
        this.context = context;
        this.usersWithMessage = usersWithMessage;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, viewGroup, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        initItemView(holder, position);
    }


    /**
     * 初始化每个item中的数据，
     *
     * @param holder   ChatViewHolder
     * @param position 列表中item的position
     */
    private void initItemView(@NonNull ChatViewHolder holder, int position) {
        String avatarUrl = "https://api.dicebear.com/6.x/fun-emoji/png?seed=" + usersWithMessage.get(position).getUser().getUserName();
        // 加载用户头像到xml
        Glide.with(context)
                .load(avatarUrl)
                .transform(new RoundedCorners(20))
                .error(R.drawable.load_image)
                .into(holder.imageViewAvatar);
        holder.textViewUsername.setText(usersWithMessage.get(position).getUser().getUserName() + " " + usersWithMessage.get(position).getUnreadMessageNumber());
        holder.textViewLatestMessage.setText(usersWithMessage.get(position).getLastMessage());
        // 给卡片设置单机事件
        holder.materialCardView.setOnClickListener(view -> {
            // 调用接口的回调方法，将点击的位置的用户信息传递给 MainActivity
            if (itemClickListener != null) {
                itemClickListener.onItemClick(usersWithMessage.get(position).getUser());
            }
        });
    }

    /**
     * 更新recycle view中的数据
     *
     * @param newUsersWithMessage 传入的新user with message
     */
    public void updateData(List<UserWithMessage> newUsersWithMessage) {
        this.usersWithMessage.clear();
        this.usersWithMessage.addAll(newUsersWithMessage);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }


    @Override
    public int getItemCount() {
        return usersWithMessage.size();
    }

    /**
     * 为此recycle view设置单机事件
     *
     * @param itemClickListener 传进来当前视图
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        // 头像组件
        ImageView imageViewAvatar;
        // 用户的姓名
        TextView textViewUsername;
        // 用户的最新信息
        TextView textViewLatestMessage;

        // 整个card的组件
        MaterialCardView materialCardView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar_chat_item);
            textViewUsername = itemView.findViewById(R.id.text_view_username_chat_item);
            textViewLatestMessage = itemView.findViewById(R.id.text_view_latest_message_chat_item);
            materialCardView = itemView.findViewById(R.id.material_card_view_chat_item);
        }

    }
}


