package com.example.duofingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChapterSelectionActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> chapters = new ArrayList<>();

    RecyclerView chapterRecyclerView;
    Button takeQuiz;
    String userName;
    String currentTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_selection);
        //String currentTopic = null;
        //String userName = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentTopic = extras.getString("topic");
            userName = extras.getString("userName");

            String finalUserName = userName;
            String finalCurrentTopic = currentTopic;
            db.collection("user_topics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                boolean exists = false;
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            if(Objects.equals(documentSnapshot.get("userID"), finalUserName)
                                    && Objects.equals(documentSnapshot.get("topicName"), finalCurrentTopic)) {
                                exists = true;
                            }
                        }
                    }

                    if (!exists) {
                        TopicNameToChapterList topicNameToChapterList = new TopicNameToChapterList();

                        List<String> lst = new ArrayList<>();

                        UserTopicsModel userTopicsModel = new UserTopicsModel();
                        userTopicsModel.chapterID = 0;
                        userTopicsModel.topicName = finalCurrentTopic;
                        userTopicsModel.userID = finalUserName;
                        userTopicsModel.completed = lst;
                        userTopicsModel.total_chapters = Objects.requireNonNull(topicNameToChapterList.map.get(finalCurrentTopic)).length;
                        db.collection("user_topics").add(userTopicsModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i(TAG, "User topics Successfully pushed");
                            }
                        });
                    }
                }
            });

        }
        
        

        TopicNameToChapterList topicNameToChapterList = new TopicNameToChapterList();

        Map<String, String[]> map = topicNameToChapterList.map;

        String[] chapterNames = map.get(currentTopic);

        chapters.addAll(Arrays.asList(chapterNames));

        chapterRecyclerView = findViewById(R.id.chapter_recycler_view);
        takeQuiz = findViewById(R.id.take_quiz);

        takeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChapterSelectionActivity.this, QuizStartActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("quizType", QuestionType.CHAPTER);
                intent.putExtra("topicName", currentTopic);
                startActivity(intent);
            }
        });

        //topicRecyclerView.setHasFixedSize(true);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChapterSelectionActivity.this);
        chapterRecyclerView.setLayoutManager(linearLayoutManager);
        chapterRecyclerView.setAdapter(new ChapterAdapter(chapters, ChapterSelectionActivity.this, userName, currentTopic));

    }
}