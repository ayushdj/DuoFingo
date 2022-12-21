package com.example.duofingo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TopicSelectionActivity extends AppCompatActivity {

    ArrayList<String> topics = new ArrayList<>();
    RecyclerView topicRecyclerView;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("userName");
        }
        // Hardcoding the topics as they are fixed in the applications.
        topics.add("Budgeting");
        topics.add("Investing");
        topics.add("Taxes");
        topics.add("Debt");
        topics.add("Home Ownership");
        topics.add("Savings");
        topics.add("Net Worth");
        topics.add("Credit");

        topicRecyclerView = findViewById(R.id.topic_recycler_view);
        //topicRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TopicSelectionActivity.this);
        topicRecyclerView.setLayoutManager(gridLayoutManager);
        topicRecyclerView.setAdapter(new TopicAdapter(topics, TopicSelectionActivity.this, userName));

    }
}