package com.example.duofingo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder>{

    // the list of pairs
    private final ArrayList<String> lst;

    // the context
    private final Context context;

    String currentTopic;
    String userName;
    private final TopicToIdMap topicToIdMap = new TopicToIdMap();
    Map<String, Integer> map = topicToIdMap.map;

    public TopicAdapter(ArrayList<String> lst, Context context, String userName) {
        this.lst = lst;
        this.context = context;
        this.userName = userName;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        currentTopic = lst.get(position);

        holder.cardRelativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), ChapterSelectionActivity.class);
            intent.putExtra("topic", holder.actualTopicName);
            intent.putExtra("userName", userName);
            context.startActivity(intent);
        });

        holder.bindThisData(currentTopic);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }
}
