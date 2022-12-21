package com.example.duofingo;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class DashBoardRankingGlobalViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView globalRank;
    public TextView countryName;
    public RelativeLayout dashBoardRankingListLayout;

    public DashBoardRankingGlobalViewHolder(@NonNull View itemView) {
        super(itemView);

//        Disregard the naming, since used the same structure as of country rank list
        this.userName = itemView.findViewById(R.id.dashBoardRankingListUsername);
        this.globalRank = itemView.findViewById(R.id.dashBoardRankingListCountryRank);
        this.countryName = itemView.findViewById(R.id.dashBoardRankingListGlobalRank);
        this.dashBoardRankingListLayout = itemView.findViewById(R.id.dashBoardRankingListLayout);
    }


    public void bindListData(DashBoardRankingDataSourceSet DashBoardRankingDataSource, String myUserName) {

//        RelativeLayout.LayoutParams paramsMsg = (RelativeLayout.LayoutParams)dashBoardRankingListLayout.getLayoutParams();

        if(Objects.equals(DashBoardRankingDataSource.getUserName(), myUserName)) {
            userName.setTextColor(Color.RED);
            globalRank.setTextColor(Color.RED);
            countryName.setTextColor(Color.RED);
        }
        userName.setText(DashBoardRankingDataSource.getUserName());
        globalRank.setText(DashBoardRankingDataSource.getRank());
        countryName.setText(DashBoardRankingDataSource.getCountry());
    }
}
