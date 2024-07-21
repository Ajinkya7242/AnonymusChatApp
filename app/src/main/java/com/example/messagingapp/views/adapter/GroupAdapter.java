package com.example.messagingapp.views.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp.model.ChatGroup;
import com.example.messagingapp.R;
import com.example.messagingapp.databinding.ItemCardBinding;
import com.example.messagingapp.views.ChatActivity;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private static ArrayList<ChatGroup> groupArrayList;

    public GroupAdapter(ArrayList<ChatGroup> groupArrayList) {
        this.groupArrayList = groupArrayList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_card, parent, false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        ChatGroup currentUser = groupArrayList.get(position);
        holder.itemCardBinding.setChatGroup(currentUser);
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        private ItemCardBinding itemCardBinding;

        public GroupViewHolder(ItemCardBinding itemCardBinding) {
            super(itemCardBinding.getRoot());
            this.itemCardBinding = itemCardBinding;

            itemCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ChatGroup clickedChatGroup=groupArrayList.get(position);
                    Intent i= new Intent(itemView.getContext(), ChatActivity.class);
                    i.putExtra("GROUP_NAME",clickedChatGroup.getGroupName());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
