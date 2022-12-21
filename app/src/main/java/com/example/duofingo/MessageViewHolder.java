package com.example.duofingo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView messageBody;
    public TextView messageTime;
    public RelativeLayout messageLayout;

    @SuppressLint("ResourceType")
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        messageBody = itemView.findViewById(R.id.message_body);
        messageTime = itemView.findViewById(R.id.timestamp);
        messageLayout = itemView.findViewById(R.layout.their_message);
    }

    public void bindListData(ChatMessage chatItem, String username) {

        // TODO : If the username is same as the sender name
        RelativeLayout.LayoutParams paramsMsg = (RelativeLayout.LayoutParams)
                messageLayout.getLayoutParams();
        if (chatItem.getMessageUser().equals(username)) {
            paramsMsg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsMsg.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

        } else {
            paramsMsg.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsMsg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        }
        messageLayout.setLayoutParams(paramsMsg);
    }
}