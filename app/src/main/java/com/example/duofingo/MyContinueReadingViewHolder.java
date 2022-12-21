package com.example.duofingo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyContinueReadingViewHolder extends RecyclerView.ViewHolder{

    TextView completionStatus;
    TextView continueReadingText;
    public RelativeLayout continueReadingLayout;

    public MyContinueReadingViewHolder(@NonNull View itemView) {
        super(itemView);
        continueReadingText = itemView.findViewById(R.id.continueReadingText);
        completionStatus = itemView.findViewById(R.id.continueReadingCompletion);
        continueReadingLayout = itemView.findViewById(R.id.continueReadingLayout);
    }

    @SuppressLint("SetTextI18n")
    public void bindListData(ContinueReadingDataSource status) {
        completionStatus.setText(status.getCurrentChapter()+"/"+status.getTotalChapter());
        continueReadingText.setText(status.getTopicName());
    }

}
