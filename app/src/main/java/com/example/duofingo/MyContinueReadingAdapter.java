package com.example.duofingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyContinueReadingAdapter extends RecyclerView.Adapter<MyContinueReadingViewHolder> {

    ArrayList<ContinueReadingDataSource> continueReadingDataSource;
    final Context context;
    private ContinueReadingChapterSelectListener continueReadingChapterSelectListener;

    public MyContinueReadingAdapter(ArrayList<ContinueReadingDataSource> continueReadingDataSource, Context context,
                                    ContinueReadingChapterSelectListener continueReadingChapterSelectListener) {
        this.continueReadingDataSource = continueReadingDataSource;
        this.context = context;
        this.continueReadingChapterSelectListener = continueReadingChapterSelectListener;
    }
    @NonNull
    @Override
    public MyContinueReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyContinueReadingViewHolder(LayoutInflater.from(context).inflate(R.layout.continue_reading_list,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyContinueReadingViewHolder holder, int position) {
        holder.bindListData(continueReadingDataSource.get(position));

        holder.continueReadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReadingChapterSelectListener.onSelectChapter(continueReadingDataSource.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return continueReadingDataSource.size();
    }
}
