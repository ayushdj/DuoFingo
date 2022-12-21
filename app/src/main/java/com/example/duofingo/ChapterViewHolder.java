package com.example.duofingo;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterViewHolder extends RecyclerView.ViewHolder{

    public TextView chapterName;
    public RelativeLayout cardRelativeLayout;
    String actualChapterName;


    public ChapterViewHolder(@NonNull View itemView) {
        super(itemView);

        chapterName = itemView.findViewById(R.id.chapter_name);
        this.cardRelativeLayout = itemView.findViewById(R.id.chapter_card_relativeLayout);
    }

    public void bindThisData(String dataToBind) {
        // extracting name and URL
        actualChapterName = dataToBind;
        this.chapterName.setText(dataToBind);

    }
}
