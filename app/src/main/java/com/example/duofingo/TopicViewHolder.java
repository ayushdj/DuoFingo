package com.example.duofingo;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicViewHolder extends RecyclerView.ViewHolder{

    public TextView topicName;
    public RelativeLayout cardRelativeLayout;
    String actualTopicName;

    public TopicViewHolder(@NonNull View itemView) {
        super(itemView);

        topicName = itemView.findViewById(R.id.topic_name);
        this.cardRelativeLayout = itemView.findViewById(R.id.topic_card_relativeLayout);
    }

    public void bindThisData(String dataToBind) {
        actualTopicName = dataToBind;
        this.topicName.setText(dataToBind);

    }
}
