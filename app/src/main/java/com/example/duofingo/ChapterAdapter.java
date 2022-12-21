package com.example.duofingo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterViewHolder>{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // the list of pairs
    private final ArrayList<String> lst;

    // the context
    private final Context context;

    String currentChapter;

    String userInfo;

    String topicName;

    public ChapterAdapter(ArrayList<String> lst, Context context, String userInfo, String topicName) {
        this.lst = lst;
        this.context = context;
        this.userInfo = userInfo;
        this.topicName = topicName;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterViewHolder(LayoutInflater.from(context).inflate(R.layout.slab_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        String currentChapter = lst.get(position);
        holder.cardRelativeLayout.setOnClickListener(v -> {

            Toast.makeText(context.getApplicationContext(), "chapter belongs to: " + topicName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context.getApplicationContext(), SpecificChapterActivity.class);
            intent.putExtra("chapter", holder.actualChapterName);
            intent.putExtra("userName", userInfo);
            intent.putExtra("topic", this.topicName);
            intent.putExtra("currentIndex", position);
            intent.putExtra("totalChapter", getItemCount());


            context.startActivity(intent);
        });

        holder.bindThisData(currentChapter);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }
}
