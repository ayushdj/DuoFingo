package com.example.duofingo;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class DashBoardRankingViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView countryRank;

    public RelativeLayout countryRankingListLayout;

    public DashBoardRankingViewHolder(@NonNull View itemView) {
        super(itemView);

        this.userName = itemView.findViewById(R.id.countryRankingListUsername);
        this.countryRank = itemView.findViewById(R.id.countryRankingListRank);

        this.countryRankingListLayout = itemView.findViewById(R.id.countryRankingListLayout);
    }


    public void bindListData(CountryRankingDataSourceSet DashBoardRankingDataSource, String myUserName) {
        if(Objects.equals(DashBoardRankingDataSource.getUserName(), myUserName)) {
            userName.setTextColor(Color.RED);
            countryRank.setTextColor(Color.RED);

        }
        this.userName.setText(DashBoardRankingDataSource.getUserName());
        countryRank.setText(DashBoardRankingDataSource.getRank());
    }
}
