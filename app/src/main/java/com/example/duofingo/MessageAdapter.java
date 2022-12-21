package com.example.duofingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ChatMessage> messages = new ArrayList<ChatMessage>();
    Context context;
    String username;

    public MessageAdapter(Context context, List<ChatMessage> chatList, String userName) {
        this.messages = chatList;
        this.context = context;
        this.username = userName;
    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV,dateTV, userTV;

        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.message_body);
            dateTV = itemView.findViewById(R.id.timestamp);
            userTV = itemView.findViewById(R.id.name);
        }

        void bind(int position) {
            ChatMessage messageModel = messages.get(position);
            messageTV.setText(messageModel.getMessageText());
            userTV.setText(messageModel.getMessageUser());
            SimpleDateFormat sdf = new SimpleDateFormat("   MM/dd HH:mm",  Locale.US);
            String time = sdf.format(new Date((long) messages.get(position).getMessageTime()));
            dateTV.setText(time);
        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV,dateTV, userTV;

        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.message_body);
            dateTV = itemView.findViewById(R.id.timestamp);
            userTV = itemView.findViewById(R.id.name);
        }
        void bind(int position) {
            ChatMessage messageModel = messages.get(position);
            messageTV.setText(messageModel.getMessageText());
            userTV.setText(messageModel.getMessageUser());
            SimpleDateFormat sdf = new SimpleDateFormat("   MM/dd HH:mm",  Locale.US);
            String time = sdf.format(new Date((long) messages.get(position).getMessageTime()));
            dateTV.setText(time);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.their_message, parent, false));
        }
        return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.my_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (messages.get(position).isMessageTypeIncoming()) {
            ((MessageInViewHolder) holder).bind(position);
        } else {
            ((MessageOutViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isMessageTypeIncoming()? 1: 0;
    }
}