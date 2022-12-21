package com.example.duofingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpecificChapterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DB_DASHBOARD";

    ArrayList<String> chapter_paragraphs = new ArrayList<>();
//    Button floatingActionButton;
    RecyclerView chapterContentRecyclerView;
    TextView chapterName;
    Button nextChapter;
    Button previousChapter;

    boolean isLastChapter = true;
    boolean isFirstChapter = true;
    Long index;
    String topic;
    String chapter;

    int currentIndex;
    int totalChapterCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chapter);

        Bundle extras = getIntent().getExtras();
        String currentChapter = extras.getString("chapter");
        String userName = extras.getString("userName");
        String currentTopic = extras.getString("topic");
        currentIndex = extras.getInt("currentIndex");
        totalChapterCount = extras.getInt("totalChapter");

        chapterContentRecyclerView = findViewById(R.id.chapter_content_recycler_view);
        chapterName = findViewById(R.id.CHAPTER_NAME);
        nextChapter = findViewById(R.id.next_chapter);
        previousChapter = findViewById(R.id.previous_chapter);

//        floatingActionButton = findViewById(R.id.finishChapter);
//        floatingActionButton.setVisibility(View.INVISIBLE);
        chapterName.setText(currentChapter);
        if(currentIndex == 0) {
            previousChapter.setVisibility(View.GONE);
        }

        if(currentIndex + 1 == totalChapterCount) {
            nextChapter.setText(" FINISH ");
        }
//        if(isLastChapter) {
//            nextChapter.setVisibility(View.GONE);
//        }
//        if(isFirstChapter) previousChapter.setVisibility(View.GONE);

        nextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentIndex + 1 == totalChapterCount) {
                    // Update the db for last page of the chapter.
                    // Move to dashboard.
                    // Update UserScore
                    String[] Id = new String[1];
                    db.collection("user_topics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Integer currentDbIndex = 0;
                            Integer totalLength = 0;
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    if (Objects.equals(documentSnapshot.get("userID"), userName)
                                            && Objects.equals(documentSnapshot.get("topicName"), currentTopic)) {
                                        currentDbIndex = Integer.parseInt(documentSnapshot.get("chapterID").toString());
                                        totalLength = Integer.parseInt(documentSnapshot.get("total_chapters").toString());
                                        Id[0] = documentSnapshot.getId();
                                        break;
                                    }
                                }

                                if(currentDbIndex < totalLength) {
                                    // Update UserScore
                                    Util.updateUserScore(userName, Constants.USER_CHAPTER_COMPLETION_BONUS);
                                }

                                DocumentReference dr = db.collection("user_topics").document(Id[0]);
                                dr.update("chapterID", totalLength);

                                // Navigate to dashboard.
                                Intent intent = new Intent(SpecificChapterActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        }
                    });


                }

                // Load next chapter's content

                String[] Id = new String[1];
                db.collection("user_topics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Integer currentDbIndex = 0;
//                        Integer totalChapterCount = 0;
                        List<String> arr = null;
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                if (Objects.equals(documentSnapshot.get("userID"), userName)
                                        && Objects.equals(documentSnapshot.get("topicName"), currentTopic)) {
                                    //Toast.makeText(getApplicationContext(), "current COUNT: " + documentSnapshot.get("chapterID").toString(), Toast.LENGTH_SHORT).show();
                                    currentDbIndex = Integer.parseInt(documentSnapshot.get("chapterID").toString());
                                    totalChapterCount = Integer.parseInt(documentSnapshot.get("total_chapters").toString());
                                    arr = (List<String>) documentSnapshot.get("completed");
                                    Id[0] = documentSnapshot.getId();
                                    break;
                                }
                            }


                            Log.d(TAG, "Before if statement " +currentDbIndex);
                            if (currentIndex < totalChapterCount && currentIndex + 1 > currentDbIndex) {
                                Log.d(TAG, "After if statement: " + currentIndex);
                                DocumentReference dr = db.collection("user_topics").document(Id[0]);
                                dr.update("chapterID", currentIndex + 1);

                                // Update UserScore
                                Util.updateUserScore(userName, Constants.USER_CHAPTER_COMPLETION_BONUS);

                                currentIndex++;
                                //dr.update("completed", arr);
                            }
                            else if(currentIndex < totalChapterCount){
                                currentIndex++;
                            }

                        }
                    }
                });


                db.collection("chapters").whereEqualTo("topicName", topic)
                        .whereEqualTo("index", index+1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                System.out.println("in");
                                System.out.println(task.isSuccessful());
                                if (task.isSuccessful()) {
                                    System.out.println(task.getResult());
                                    for (DocumentSnapshot document : task.getResult()) {
//                                System.out.println("if");
//                                chapter_paragraphs.add(document.getString("heading"));

                                        List<String> data = (List<String>)document.get("body");
                                        boolean lFlag = (boolean)document.get("lastChapter");
                                        index = (Long) document.get("index");
                                        chapter = (String) document.get("heading");
                                        System.out.println(chapter);
                                        System.out.println(index);

                                        chapter_paragraphs.clear();
                                        for(String a : data) {
                                            chapter_paragraphs.add(a);
                                        }
//                                chapter_paragraphs.add(document.get("body").);

                                        chapterName.setText(chapter);
                                        isLastChapter = lFlag;
                                        if(isLastChapter) {
                                            nextChapter.setVisibility(View.VISIBLE);
                                            nextChapter.setText(  " FINISH "  );
//                                            floatingActionButton.setVisibility(View.VISIBLE);
                                        } else {
                                            nextChapter.setVisibility(View.VISIBLE);
                                            nextChapter.setText("  NEXT  ");
                                        }
                                        if(index==1) {
                                            previousChapter.setVisibility(View.GONE);
                                        } else {
                                            previousChapter.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpecificChapterActivity.this);
                                    chapterContentRecyclerView.setLayoutManager(linearLayoutManager);
                                    chapterContentRecyclerView.setAdapter(new ChapterAdapter(chapter_paragraphs,SpecificChapterActivity.this, userName, currentTopic));

                                } else {
                                    System.out.println("else");
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        previousChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("chapters").whereEqualTo("topicName", topic)
                        .whereEqualTo("index", index - 1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                System.out.println("in");
                                System.out.println(task.isSuccessful());
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
//                                System.out.println("if");
//                                chapter_paragraphs.add(document.getString("heading"));

                                        List<String> data = (List<String>)document.get("body");
                                        boolean lFlag = (boolean)document.get("lastChapter");
                                        index = (Long) document.get("index");
                                        chapter = (String) document.get("heading");

                                        chapter_paragraphs.clear();
                                        for(String a : data) {
                                            chapter_paragraphs.add(a);
                                        }

                                        chapterName.setText(chapter);
                                        isLastChapter = lFlag;
                                        if(isLastChapter) {
                                            nextChapter.setVisibility(View.VISIBLE);
                                            nextChapter.setText(  " FINISH "  );
//                                            floatingActionButton.setVisibility(View.VISIBLE);
                                        } else {
//                                            floatingActionButton.setVisibility(View.INVISIBLE);
                                            nextChapter.setVisibility(View.VISIBLE);
                                            nextChapter.setText("  NEXT  ");
                                        }
                                        if(index==1) {
                                            previousChapter.setVisibility(View.GONE);
//                                            floatingActionButton.setVisibility(View.INVISIBLE);
                                        } else {
                                            previousChapter.setVisibility(View.VISIBLE);
//                                            floatingActionButton.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpecificChapterActivity.this);
                                    chapterContentRecyclerView.setLayoutManager(linearLayoutManager);
                                    chapterContentRecyclerView.setAdapter(new ChapterAdapter(chapter_paragraphs, SpecificChapterActivity.this, userName, currentTopic));


                                } else {
                                    System.out.println("else");
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        //chapter_paragraphs.add("two");
        //chapter_paragraphs.add("My very efficient mother just served us nuts");

        db.collection("chapters").whereEqualTo("heading", currentChapter).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("in");
                        System.out.println(task.isSuccessful());
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
//                                System.out.println("if");
//                                chapter_paragraphs.add(document.getString("heading"));

                                List<String> data = (List<String>) document.get("body");
                                boolean lFlag = (boolean) document.get("lastChapter");
                                index = (Long) document.get("index");
                                topic = (String) document.get("topicName");
                                System.out.println(topic);
                                System.out.println(index);

                                chapter_paragraphs.clear();
                                for (String a : data) {
                                    chapter_paragraphs.add(a);
                                }
//                                chapter_paragraphs.add(document.get("body").);
                                isLastChapter = lFlag;
                                if (!isLastChapter) nextChapter.setVisibility(View.VISIBLE);
                                if (index != 1) previousChapter.setVisibility(View.VISIBLE);

                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpecificChapterActivity.this);
                            chapterContentRecyclerView.setLayoutManager(linearLayoutManager);
                            chapterContentRecyclerView.setAdapter(new ChapterAdapter(chapter_paragraphs, SpecificChapterActivity.this, userName, currentTopic));

                            //continueReadingRV = findViewById(R.id.continueReadingRecycleView);
                            //continueReadingRV.setHasFixedSize(true);
                            //continueReadingRV.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            //continueReadingRV.setAdapter(new MyContinueReadingAdapter(continueReadingDataSource, DashboardActivity.this, DashboardActivity.this));

                        } else {
                            System.out.println("else");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String[] Id = new String[1];
//                db.collection("user_topics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Integer currentDbIndex = 0;
//                        Integer totalLength = 0;
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                                if (Objects.equals(documentSnapshot.get("userID"), userName)
//                                        && Objects.equals(documentSnapshot.get("topicName"), currentTopic)) {
//                                    currentDbIndex = Integer.parseInt(documentSnapshot.get("chapterID").toString());
//                                    totalLength = Integer.parseInt(documentSnapshot.get("total_chapters").toString());
//                                    Id[0] = documentSnapshot.getId();
//                                    break;
//                                }
//                            }
//
//                            if(currentDbIndex < totalLength) {
//                                // Update UserScore
//                                Util.updateUserScore(userName, Constants.USER_CHAPTER_COMPLETION_BONUS);
//                            }
//
//                            DocumentReference dr = db.collection("user_topics").document(Id[0]);
//                            dr.update("chapterID", totalLength);
//
//                            Intent intent = new Intent(SpecificChapterActivity.this, DashboardActivity.class);
//
//                            startActivity(intent);
//                            finish();
//
//
//                        }
//                    }
//                });
//            }
//        });

    }
}