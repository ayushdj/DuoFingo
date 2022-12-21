package com.example.duofingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashBoardRankingGlobalAdapter extends RecyclerView.Adapter<DashBoardRankingGlobalViewHolder> {

    private ArrayList<DashBoardRankingDataSourceSet> dashBoardRankingDataSource;
    private final Context context;
    private final String userName;

    public DashBoardRankingGlobalAdapter(ArrayList<DashBoardRankingDataSourceSet> dashBoardRankingDataSource, Context context, String userName) {
        this.dashBoardRankingDataSource = dashBoardRankingDataSource;
        this.context = context;
        this.userName = userName;
    }

    @NonNull
    @Override
    public DashBoardRankingGlobalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashBoardRankingGlobalViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_ranking_list,null));
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardRankingGlobalViewHolder holder, int position) {
        holder.bindListData(dashBoardRankingDataSource.get(position), userName);
    }

    @Override
    public int getItemCount() {
        return dashBoardRankingDataSource.size();
    }

}
