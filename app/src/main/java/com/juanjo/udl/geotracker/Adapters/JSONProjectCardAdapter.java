package com.juanjo.udl.geotracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanjo.udl.geotracker.Activities.Layouts.GeneralMapActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;
import com.juanjo.udl.geotracker.R;

import java.util.ArrayList;

public class JSONProjectCardAdapter extends RecyclerView.Adapter<JSONProjectCardAdapter.ViewHolder>{
    private ArrayList<JSONProject> mDataset;
    private static JSONUser user;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProject;
        public JSONProject project;
        public Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, GeneralMapActivity.class);
                    it.putExtra("project", project);
                    it.putExtra("user", user);
                    context.startActivity(it);
                }
            });
            txtProject = v.findViewById(R.id.info_text);
        }
    }

    public JSONProjectCardAdapter (ArrayList<JSONProject> mDataset, JSONUser user) {
        this.mDataset = mDataset;
        this.user = user;
    }

    @Override
    public JSONProjectCardAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_project, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtProject.setText(mDataset.get(position).getName());
        holder.project = mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
